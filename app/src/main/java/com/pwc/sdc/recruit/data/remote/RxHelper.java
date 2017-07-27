package com.pwc.sdc.recruit.data.remote;

import com.thirdparty.proxy.net.http.ApiException;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.DownLoadSubscriber;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;
import com.thirdparty.proxy.net.http.retrofit.rx.UpLoadSubscriber;
import com.thirdparty.proxy.net.http.retrofit.rx.adapter.HttpObservable;
import com.thirdparty.proxy.utils.FileUtil;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author:dongpo 创建时间: 8/4/2016
 * 描述: rxjava + retrofit 组合工具类
 * 修改:
 */
public class RxHelper {

    /**
     * 从服务器获取数据，请求一个接口
     *
     * @param observable
     * @param callBack
     * @param <T>
     * @return
     */
    public static <T> Subscription requestSingle(final Observable<HttpResponse<T>> observable, HttpSubscriber<T> callBack) {
        Subscription request = observable.subscribeOn(Schedulers.io()).
                flatMap(new Func1<HttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(HttpResponse<T> tHttpResponse) {
                        if (tHttpResponse.resultCode == 0) {
                            return Observable.just(tHttpResponse.data);
                        } else {
                            return Observable.error(new ApiException(tHttpResponse.resultCode, tHttpResponse.message));
                        }
                    }
                }).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
        return request;
    }

    /**
     * 上传文件
     *
     * @param observable
     * @param callBack
     * @param <T>
     * @return
     */
    public static <T> Subscription upload(final Observable<HttpResponse<T>> observable, UpLoadSubscriber<T> callBack) {
        //add upload call back
        if (observable instanceof HttpObservable) {
            HttpObservable httpObservable = (HttpObservable) observable;
            httpObservable.setUploadListener(callBack);
        }

        Subscription request = observable.
                flatMap(new Func1<HttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(HttpResponse<T> tHttpResponse) {
                        if (tHttpResponse.resultCode == 0) {
                            return Observable.just(tHttpResponse.data);
                        } else {
                            return Observable.error(new ApiException(tHttpResponse.resultCode, tHttpResponse.message));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
        return request;
    }

    /**
     * 上传文件
     *
     * @param observable
     * @param callBack
     * @param <T>
     * @return
     */
    public static <T> Subscription uploadHeader(final Observable<T> observable, UpLoadSubscriber<T> callBack) {
        //add upload call back
        if (observable instanceof HttpObservable) {
            HttpObservable httpObservable = (HttpObservable) observable;
            httpObservable.setUploadListener(callBack);
        }

        Subscription request = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
        return request;
    }

    /**
     * 下载文件，并写入到对应的文件，支持进度监听
     *
     * @param observable 后台对应的观察者
     * @param folder     储存的文件夹
     * @param fileName   文件名称
     * @param callBack   回调
     * @return 下载完成的文件File对象
     */
    public static Subscription download(Observable<ResponseBody> observable, final File folder, final String fileName, DownLoadSubscriber callBack) {
        //add upload call back
        if (observable instanceof HttpObservable) {
            HttpObservable httpObservable = (HttpObservable) observable;
            httpObservable.setDownLoadListener(callBack);
        }
        Subscription request = observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        FileUtil.writeResponseBodyToDisk(folder, fileName, responseBody.byteStream());
                    }
                })
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        return new File(folder, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
        return request;
    }

    /**
     * 先后请求两个request，在firstRequest响应之后，拿到响应的数据，再构建第二个request
     *
     * @param firstRequest 第一个request
     * @param callBack     两次请求成功，才回调success方法
     * @param fun1         由调用者实现，并返回第二个request
     * @param <T>          firstRequest网络请求结果类型
     * @param <K>          secondRequest网络请求结果类型
     * @return
     */
    public static <T, K> Subscription requestDouble(final Observable<HttpResponse<T>> firstRequest, final Func1<T, Observable<HttpResponse<K>>> fun1, HttpSubscriber<K> callBack) {
        Subscription request = firstRequest.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResponse<T>, Observable<HttpResponse<K>>>() {
                    @Override
                    public Observable<HttpResponse<K>> call(HttpResponse<T> tHttpResponse) {
                        if (tHttpResponse.resultCode == 0) {
                            return fun1.call(tHttpResponse.data).subscribeOn(Schedulers.io());
                        } else {
                            return Observable.error(new ApiException(tHttpResponse.resultCode, tHttpResponse.message));
                        }
                    }
                })
                .flatMap(new Func1<HttpResponse<K>, Observable<K>>() {
                    @Override
                    public Observable<K> call(HttpResponse<K> kHttpResponse) {
                        if (kHttpResponse.resultCode == 0) {
                            return Observable.just(kHttpResponse.data);
                        } else {
                            return Observable.error(new ApiException(kHttpResponse.resultCode, kHttpResponse.message));
                        }
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
        return request;
    }
}
