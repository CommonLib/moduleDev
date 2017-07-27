package com.thirdparty.proxy.net.http.retrofit.rx.adapter;

import com.thirdparty.proxy.net.http.retrofit.ProgressRequestBody;
import com.thirdparty.proxy.net.http.retrofit.ProgressResponseBody;
import com.thirdparty.proxy.net.http.retrofit.rx.DownLoadSubscriber;
import com.thirdparty.proxy.net.http.retrofit.rx.UpLoadSubscriber;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class RxJavaAdapterFactory extends CallAdapter.Factory {

    public CallAdapter<?> mCurrentAdapter = null;

    /**
     * Returns an instance which creates synchronous observables that do not operate on any scheduler
     * by default.
     */
    public static RxJavaAdapterFactory create() {
        return new RxJavaAdapterFactory(null);
    }

    /**
     * Returns an instance which creates synchronous observables that
     * {@linkplain Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
     */
    public static RxJavaAdapterFactory createWithScheduler(Scheduler scheduler) {
        if (scheduler == null) throw new NullPointerException("scheduler == null");
        return new RxJavaAdapterFactory(scheduler);
    }

    private final Scheduler scheduler;

    private RxJavaAdapterFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        String canonicalName = rawType.getCanonicalName();
        boolean isSingle = "rx.Single".equals(canonicalName);
        boolean isCompletable = "rx.Completable".equals(canonicalName);
        if (rawType != Observable.class && !isSingle && !isCompletable) {
            return null;
        }
        if (!isCompletable && !(returnType instanceof ParameterizedType)) {
            String name = isSingle ? "Single" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>");
        }

        if (isCompletable) {
            // Add Completable-converter wrapper from a separate class. This defers classloading such that
            // regular Observable operation can be leveraged without relying on this unstable RxJava API.
            // Note that this has to be done separately since Completable doesn't have a parametrized
            // type.
            mCurrentAdapter = CompletableHelper.createCallAdapter(scheduler);
            return mCurrentAdapter;
        }

        CallAdapter<Observable<?>> callAdapter = getCallAdapter(returnType, scheduler);
        if (isSingle) {
            // Add Single-converter wrapper from a separate class. This defers classloading such that
            // regular Observable operation can be leveraged without relying on this unstable RxJava API.
            mCurrentAdapter = SingleHelper.makeSingle(callAdapter);
            return mCurrentAdapter;
        }
        mCurrentAdapter = callAdapter;
        return mCurrentAdapter;
    }

    private CallAdapter<Observable<?>> getCallAdapter(Type returnType, Scheduler scheduler) {
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized"
                        + " as Response<Foo> or Response<? extends Foo>");
            }
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResponseCallAdapter(responseType, scheduler);
        }

        if (rawObservableType == Result.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized"
                        + " as Result<Foo> or Result<? extends Foo>");
            }
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResultCallAdapter(responseType, scheduler);
        }

        return new SimpleCallAdapter(observableType, scheduler);
    }

    public static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private Call<T> originalCall;
        private DownLoadSubscriber downloadCallBack;
        private UpLoadSubscriber uploadCallBack;

        CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            Call<T> call = originalCall.clone();
            // Wrap the call in a helper which handles both unsubscription and backpressure.
            RequestArbiter<T> requestArbiter = new RequestArbiter<>(call, subscriber, uploadCallBack, downloadCallBack);
            subscriber.add(requestArbiter);
            subscriber.setProducer(requestArbiter);
        }

        public void setDownloadCallBack(DownLoadSubscriber downloadCallBack) {
            this.downloadCallBack = downloadCallBack;
        }

        public void setUploadCallBack(UpLoadSubscriber uploadCallBack) {
            this.uploadCallBack = uploadCallBack;
        }
    }

    static final class RequestArbiter<T> extends AtomicBoolean implements Subscription, Producer {
        private final Call<T> call;
        private final Subscriber<? super Response<T>> subscriber;
        private final UpLoadSubscriber mUpLoadCallBack;
        private final DownLoadSubscriber mDownloadCallBack;

        RequestArbiter(Call<T> call, Subscriber<? super Response<T>> subscriber, UpLoadSubscriber upLoadCallBack, DownLoadSubscriber downloadCallBack) {
            this.mUpLoadCallBack = upLoadCallBack;
            this.mDownloadCallBack = downloadCallBack;
            this.call = call;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (n < 0) throw new IllegalArgumentException("n < 0: " + n);
            if (n == 0) return; // Nothing to do when requesting 0.
            if (!compareAndSet(false, true)) return; // Request was already triggered.

            try {
                if (mUpLoadCallBack != null) {
                    warpRequestBody(call, mUpLoadCallBack);
                }
                Response<T> response = call.execute();
                if (mDownloadCallBack != null) {
                    T body = response.body();
                    if(body instanceof ResponseBody){
                        warpResponseBody(response, mDownloadCallBack);
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        public void warpRequestBody(Call<T> call, UpLoadSubscriber callBack) {
            Request request = call.request();
            RequestBody body = request.body();
            ProgressRequestBody requestBody = new ProgressRequestBody(body, callBack);
            Class<? extends Request> clazz = request.getClass();
            try {
                Field bodyField = clazz.getDeclaredField("body");
                bodyField.setAccessible(true);
                bodyField.set(request, requestBody);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public void warpResponseBody(Response<T> response, DownLoadSubscriber callBack) {
            ResponseBody responseBody = (ResponseBody) response.body();
            ProgressResponseBody requestBody = new ProgressResponseBody(responseBody, callBack);
            Class<? extends Response> clazz = response.getClass();
            try {
                Field bodyField = clazz.getDeclaredField("body");
                bodyField.setAccessible(true);
                bodyField.set(response, requestBody);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void unsubscribe() {
            call.cancel();
        }

        @Override
        public boolean isUnsubscribed() {
            return call.isCanceled();
        }
    }

    static final class ResponseCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final Scheduler scheduler;

        ResponseCallAdapter(Type responseType, Scheduler scheduler) {
            this.responseType = responseType;
            this.scheduler = scheduler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<Response<R>> adapt(Call<R> call) {
            CallOnSubscribe<R> callOnSubscribe = new CallOnSubscribe<>(call);
            Observable<Response<R>> observable = Observable.create(callOnSubscribe);
            if (scheduler != null) {
                observable = observable.subscribeOn(scheduler);
            }
            HttpObservable<Response<R>, R> rHttpObservable = new HttpObservable<>(new TransferOnSubscribe<>(observable));
            rHttpObservable.setCall(call);
            rHttpObservable.setCallOnSubscribe(callOnSubscribe);
            return rHttpObservable;
        }
    }

    static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final Scheduler scheduler;

        SimpleCallAdapter(Type responseType, Scheduler scheduler) {
            this.responseType = responseType;
            this.scheduler = scheduler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            CallOnSubscribe<R> callOnSubscribe = new CallOnSubscribe<>(call);
            Observable<R> observable = Observable.create(callOnSubscribe) //
                    .lift(OperatorMapResponseToBodyOrError.<R>instance());
            if (scheduler != null) {
                observable = observable.subscribeOn(scheduler);
            }
            HttpObservable<R, R> rHttpObservable = new HttpObservable<>(new TransferOnSubscribe<>(observable));
            rHttpObservable.setCall(call);
            rHttpObservable.setCallOnSubscribe(callOnSubscribe);
            return rHttpObservable;
        }
    }

    static final class ResultCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final Scheduler scheduler;

        ResultCallAdapter(Type responseType, Scheduler scheduler) {
            this.responseType = responseType;
            this.scheduler = scheduler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<Result<R>> adapt(Call<R> call) {
            CallOnSubscribe<R> callOnSubscribe = new CallOnSubscribe<>(call);
            Observable<Result<R>> observable = Observable.create(callOnSubscribe) //
                    .map(new Func1<Response<R>, Result<R>>() {
                        @Override
                        public Result<R> call(Response<R> response) {
                            return Result.response(response);
                        }
                    }).onErrorReturn(new Func1<Throwable, Result<R>>() {
                        @Override
                        public Result<R> call(Throwable throwable) {
                            return Result.error(throwable);
                        }
                    });
            if (scheduler != null) {
                observable = observable.subscribeOn(scheduler);
            }
            HttpObservable<Result<R>, R> rHttpObservable = new HttpObservable<>(new TransferOnSubscribe<>(observable));
            rHttpObservable.setCall(call);
            rHttpObservable.setCallOnSubscribe(callOnSubscribe);
            return rHttpObservable;
        }
    }

}
