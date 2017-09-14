package com.pwc.sdc.recruit.base;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.base.interf.ActivityModel;
import com.pwc.sdc.recruit.data.remote.RxHelper;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;
import com.thirdparty.proxy.utils.WindowUtils;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 2016/6/25
 * 描述:
 * 修改:
 */
public abstract class BaseModel implements ActivityModel {
    /**
     * 当前activity所持有的所有网络请求
     */
    private HashMap<Observable, Subscription> mRequestSubscriptions;

    public BaseModel() {
        initData();
    }

    protected void initData() {
    }

    public <T> Subscription sendHttpRequest(Observable<HttpResponse<T>> request, HttpSubscriber<T> callBack) {
        if(!WindowUtils.hasInternet()){
            callBack.onError(new ConnectException());
            return null;
        }

        if (mRequestSubscriptions != null && mRequestSubscriptions.containsKey(request)) {
            Subscription lastConnection = mRequestSubscriptions.get(request);
            cancelRequest(request, lastConnection);
        }

        Subscription connection = RxHelper.requestSingle(request, callBack);
        addAsyncRequests(request, connection);
        return connection;
    }

    public PwcApplication getApplicationContext() {
        return PwcApplication.getInstance();
    }

    public void addAsyncRequests(Observable request, Subscription connection) {
        if (this.mRequestSubscriptions == null) {
            mRequestSubscriptions = new HashMap<>();
        }
        mRequestSubscriptions.put(request, connection);
    }

    /**
     * 取消一个网络请求
     *
     * @param request
     */
    public void cancelRequest(Observable request, Subscription connection) {
        if (connection != null && !connection.isUnsubscribed()) {
            connection.unsubscribe();
            if (mRequestSubscriptions.containsKey(request)) {
                mRequestSubscriptions.remove(request);
            }
            request = null;
            connection = null;
        }
    }

    /**
     * 取消当前Activity相关的网络请求
     */
    public void cancelAllRequest() {
        if (mRequestSubscriptions != null) {
            Iterator<Map.Entry<Observable, Subscription>> iterator = mRequestSubscriptions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Observable, Subscription> requests = iterator.next();
                Subscription connection = requests.getValue();
                if (connection != null && !connection.isUnsubscribed()) {
                    connection.unsubscribe();
                }
                iterator.remove();
            }
            mRequestSubscriptions.clear();
        }

    }
}
