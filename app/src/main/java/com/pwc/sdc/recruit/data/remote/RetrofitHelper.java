package com.pwc.sdc.recruit.data.remote;

import android.text.TextUtils;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.net.http.retrofit.gson.GsonConverterFactory;
import com.thirdparty.proxy.net.http.retrofit.rx.adapter.RxJavaAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author:dongpo 创建时间: 6/20/2016
 * 描述:
 * 修改:
 */
public class RetrofitHelper {

    private Retrofit mRetrofit;
    private BackPointService mBackPointService;
    private static RetrofitHelper client = new RetrofitHelper();

    private String BASE_URL = AppConfig.BASE_URL + "eapplication/";
    private final OkHttpClient mOkHttpClient;
    private String mNewBaseUrl;

    private RetrofitHelper() {
        mOkHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new TokenInterceptor()).retryOnConnectionFailure(true)
                .addNetworkInterceptor(new CacheNetWorkInterceptor())
                .cache(new Cache(PwcApplication.getDiskCacheDir(PwcApplication.getInstance(), AppConfig.CACHE_FOLDER), AppConfig.MAX_CACHE_VALUE))
                .readTimeout(20, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(5, 300000, TimeUnit.MILLISECONDS))
                .writeTimeout(20, TimeUnit.SECONDS).
                        connectTimeout(5, TimeUnit.SECONDS).build();
        System.setProperty("http.keepAlive", "false");
        String baseUrl = (String) SharedPreHelper.get(PwcApplication.getInstance(), Constants.BASE_URL, "");
        if (!TextUtils.isEmpty(baseUrl)) {
            BASE_URL = baseUrl + "eapplication/";
        }
        mNewBaseUrl = AppConfig.BASE_URL;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaAdapterFactory.create())
                .build();

        mBackPointService = mRetrofit.create(BackPointService.class);
    }

    public static RetrofitHelper getInstance() {
        return client;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public BackPointService getService() {
        return mBackPointService;
    }

    /**
     * 为了测试方便
     *
     * @param newBaseUrl
     */
    public void setBaseUrl(String newBaseUrl) {
        mNewBaseUrl = newBaseUrl;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(newBaseUrl + "eapplication/")
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaAdapterFactory.create())
                .build();
        mBackPointService = mRetrofit.create(BackPointService.class);
    }

    public String getBaseUrl() {
        return mNewBaseUrl;
    }
}
