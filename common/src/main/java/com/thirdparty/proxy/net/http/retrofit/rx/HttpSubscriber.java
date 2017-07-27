package com.thirdparty.proxy.net.http.retrofit.rx;

import android.text.TextUtils;

import com.thirdparty.proxy.R;
import com.thirdparty.proxy.base.BaseApplication;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.net.http.ApiException;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @author:dongpo 创建时间: 6/20/2016
 * 描述:
 * 修改:
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> {

    private static final int TIME_OUT = -1;
    private static final int UN_KNOWN = -2;
    private static final int NO_CONNECTION = -3;
    private static final int NO_HOST = -4;


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        TLog.exception(e);
        if (e instanceof HttpException) {
            HttpException error = (HttpException) e;
            int code = error.code();
            ResponseBody responseBody = error.response().errorBody();
            if (code >= 500) {
                BaseApplication.showToast(BaseApplication.string(R.string.server_error));
            } else if (code >= 400 && code < 500) {
                if(code == 404){
                    BaseApplication.showToast(BaseApplication.string(R.string.not_found_url));
                }else{
                    BaseApplication.showToast(BaseApplication.string(R.string.client_error));
                }
            } else if (code >= 300 && code < 400) {
                BaseApplication.showToast(BaseApplication.string(R.string.server_error));
            } else if (code < 200) {
                BaseApplication.showToast(BaseApplication.string(R.string.server_deal_request));
            }
            try {
                String err = responseBody.string();
                TLog.e("Http请求错误：错误码：" + code + "，错误描述：" + err);
                onFailure(code, err);
            } catch (IOException e1) {
                TLog.exception(e1);
                TLog.e("Http请求错误：错误码：" + code + "，错误描述：转化responseBody错误，" + e1.getMessage());
                onFailure(code, e1.getMessage());
            }
        } else if (e instanceof ApiException) {
            ApiException apiError = (ApiException) e;
            String apiDescription = apiError.getApiDescription();
            if (!TextUtils.isEmpty(apiDescription)) {
                BaseApplication.showToast(apiDescription);
            }
            TLog.e("业务相应错误：错误码：" + apiError.getResultCode() + "，错误描述：" + apiDescription);
            onFailure(apiError.getResultCode(), apiError.getMessage());
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
            TLog.e("网络错误：网络超时");
            onFailure(TIME_OUT, e.getMessage());
            BaseApplication.showToast(BaseApplication.string(R.string.request_time_out));
        } else if (e instanceof ConnectException) {
            TLog.e("网络错误：没有网络连接");
            onFailure(NO_CONNECTION, e.getMessage());
            BaseApplication.showToast(BaseApplication.string(R.string.no_connection));
        } else if (e instanceof UnknownHostException) {
            TLog.e("网络错误：未知主机异常");
            onFailure(NO_HOST, e.getMessage());
            BaseApplication.showToast(BaseApplication.string(R.string.no_found_host));
        }else {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof EOFException) {
                TLog.e("网络错误：没有网络连接");
                onFailure(NO_CONNECTION, e.getMessage());
                BaseApplication.showToast(BaseApplication.string(R.string.no_connection));
            }else{
                TLog.e("未知异常：" + e.getMessage());
                onFailure(UN_KNOWN, e.getMessage());
            }
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onStart() {
        onRequestStart();
    }

    public void onRequestStart() {

    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(int errorCode, String message);


}
