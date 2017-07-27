package com.pwc.sdc.recruit.data.remote;

import com.thirdparty.proxy.utils.WindowUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author:dongpo 创建时间: 9/1/2016
 * 描述:
 * 修改:
 */
public class CacheNetWorkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean hasInternet = WindowUtils.hasInternet();
        if (!hasInternet) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        if (hasInternet) {
            //获取请求头中的缓存时间
            String cacheControl = request.cacheControl().toString();
            response = response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            response = response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=0")
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
