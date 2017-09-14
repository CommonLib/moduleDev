package com.pwc.sdc.recruit.data.remote;

import com.pwc.sdc.recruit.manager.AccountManager;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.utils.WindowUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author:dongpo 创建时间: 9/1/2016
 * 描述:
 * 修改:
 */
public class TokenInterceptor implements Interceptor {
    //缓存策略，如果请求成功，则缓存数据一定时间，失败拿缓存（如果不超时就拿到，否则拿不到）;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (!WindowUtils.hasInternet()) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        } else {
            builder.addHeader("Accept", "application/json");
            AccountManager accountManager = AccountManager.getInstance();
            if (accountManager.isLogin()) {
                builder.addHeader("token", accountManager.getUser().token);
            }
        }
        Request newRequest = builder.build();
        printRequestInfo(newRequest);
        //获取请求头中的缓存时间
        String cacheControl = request.cacheControl().toString();
        Response response = chain.proceed(newRequest);


        if (WindowUtils.hasInternet()) {
            response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
        printResponseInfo(response);
        return response;
    }

    private void printRequestInfo(Request request) {
        if (TLog.DEBUG) {
            try {
                TLog.d("---------------------------Request Start---------------------------");
                TLog.d("method = " + request.method());
                TLog.d("url = " + request.url().toString());
                Headers headers = request.headers();
                for (int i = 0; i < headers.size(); i++) {
                    String name = headers.name(i);
                    String value = headers.get(name);
                    TLog.d("header: " + name + ", " + value);
                }
                if (request.body() != null) {
                    if (request.body().contentLength() > 102400) {
                        TLog.d("body: body is too long to print, ignore..");
                    } else {
                        TLog.d("body:" + request.body().toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                TLog.d("IOException:" + e.getMessage());
            }
            TLog.d("---------------------------Request End-----------------------------");
        }
    }

    private void printResponseInfo(Response response) {

        if (TLog.DEBUG) {
            try {
                TLog.d("---------------------------Response start---------------------------");
                TLog.d("code = " + response.code());
                TLog.d("url = " + response.request().url().toString());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    String name = headers.name(i);
                    String value = headers.get(name);
                    TLog.d("header: " + name + ", " + value);
                }
                if (response.body() != null) {
                    if (response.body().contentLength() > 102400) {
                        TLog.d("body: body is too long to print, ignore..");
                    } else {
                        TLog.d("body:" + response.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                TLog.d("IOException:" + e.getMessage());
            }
            TLog.d("---------------------------Response end-----------------------------");
        }
    }
}
