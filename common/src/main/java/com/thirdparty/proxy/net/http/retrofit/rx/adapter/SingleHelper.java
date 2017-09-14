package com.thirdparty.proxy.net.http.retrofit.rx.adapter;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import rx.Observable;
import rx.Single;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
final class SingleHelper {
    static CallAdapter<Single<?>> makeSingle(final CallAdapter<Observable<?>> callAdapter) {
        return new CallAdapter<Single<?>>(){
            @Override public Type responseType() {
                return callAdapter.responseType();
            }

            @Override public <R> Single<?> adapt(Call<R> call) {
                Observable<?> observable = callAdapter.adapt(call);
                return observable.toSingle();
            }
        };
    }
}
