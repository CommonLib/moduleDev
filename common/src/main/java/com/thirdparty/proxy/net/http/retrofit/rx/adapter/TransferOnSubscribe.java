package com.thirdparty.proxy.net.http.retrofit.rx.adapter;

import rx.Observable;
import rx.Subscriber;

/**
 * @author:dongpo 创建时间: 8/4/2016
 * 描述:
 * 修改:
 */
public class TransferOnSubscribe<T> implements Observable.OnSubscribe<T> {
    private final Observable<T> mObservable;

    public TransferOnSubscribe(Observable<T> observable){
        mObservable = observable;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        mObservable.unsafeSubscribe(subscriber);
    }
}
