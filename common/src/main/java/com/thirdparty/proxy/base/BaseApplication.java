package com.thirdparty.proxy.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.thirdparty.proxy.cache.ACache;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.net.frecso.OkHttpImagePipelineConfigFactory;
import com.thirdparty.proxy.utils.DataCleanUtils;


public class BaseApplication extends Application {

    private static final String PREF_NAME = "baseApplication";
    private static BaseApplication mContext;
    private static Resources mResource;
    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mResource = mContext.getResources();
        mHandler = new Handler(getMainLooper());
    }

    public static BaseApplication getInstance() {
        return mContext;
    }

    public void init(Boolean debug) {
        TLog.init(getPackageName(), debug);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this)
                .build();
        Fresco.initialize(this, config);
        ACache.get(this);
    }


    public static Resources resources() {
        return mResource;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static void post(Runnable run) {
        mHandler.post(run);
    }

    public static String string(int id) {
        return mResource.getString(id);
    }

    public static String string(int id, Object... args) {
        return mResource.getString(id, args);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        SharedPreferences pre = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    public static void showToast(String message, int duration) {
        Toast.makeText(mContext, message, duration).show();
    }

    /**
     * 清除缓存
     */
    public static void cleanCache() {
        DataCleanUtils.cleanInternalCache(mContext);
    }

    public static void wipeData() {
        DataCleanUtils.cleanApplicationData(mContext);
    }


}
