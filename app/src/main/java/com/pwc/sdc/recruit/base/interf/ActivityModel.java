package com.pwc.sdc.recruit.base.interf;

import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 2016/6/25
 * 描述:
 * 修改:
 */
public interface ActivityModel extends ModelLayer {
    <T> Subscription sendHttpRequest(Observable<HttpResponse<T>> observable, HttpSubscriber<T> callBack);
    void addAsyncRequests(Observable request, Subscription connection);
    void cancelRequest(Observable request, Subscription connection);
    void cancelAllRequest();
}
