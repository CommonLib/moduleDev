package com.thirdparty.proxy.net.http.retrofit.rx.adapter;

import com.thirdparty.proxy.net.http.retrofit.rx.DownLoadSubscriber;
import com.thirdparty.proxy.net.http.retrofit.rx.UpLoadSubscriber;

import retrofit2.Call;
import rx.Observable;

/**
 * @author:dongpo 创建时间: 8/4/2016
 * 描述:
 * 修改:
 */
public class HttpObservable<T, R> extends Observable<T> {
    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p/>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f {@link OnSubscribe} to be executed when {@link(Subscriber)} is called
     */
    protected HttpObservable(OnSubscribe<T> f) {
        super(f);
    }

    private Call<R> mCall;
    private RxJavaAdapterFactory.CallOnSubscribe mCallOnSubscribe;

    public Call<R> getCall() {
        return mCall;
    }

    public void setCall(Call<R> call) {
        mCall = call;
    }

    public RxJavaAdapterFactory.CallOnSubscribe getCallOnSubscribe() {
        return mCallOnSubscribe;
    }

    public void setCallOnSubscribe(RxJavaAdapterFactory.CallOnSubscribe callOnSubscribe) {
        mCallOnSubscribe = callOnSubscribe;
    }

    public void setUploadListener(UpLoadSubscriber listener){
        mCallOnSubscribe.setUploadCallBack(listener);
    }

    public void setDownLoadListener(DownLoadSubscriber listener){
        mCallOnSubscribe.setDownloadCallBack(listener);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }

        if(!(o instanceof HttpObservable)){
            return false;
        }

        HttpObservable httpObservable = (HttpObservable) o;
        return httpObservable.getCall().request().url().equals(getCall().request().url());
    }

    @Override
    public int hashCode() {
        return getCall().request().url().hashCode();
    }

}
