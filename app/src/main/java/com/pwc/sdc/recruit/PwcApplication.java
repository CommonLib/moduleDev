package com.pwc.sdc.recruit;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;
import com.pwc.sdc.recruit.base.MessageEvent;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.thirdparty.proxy.base.BaseApplication;
import com.thirdparty.proxy.cache.DiskLruCacheUtil;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.utils.WindowUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


/**
 * 全局应用程序类：
 *
 * @author
 * @created
 */
public class PwcApplication extends BaseApplication {

    private static PwcApplication mPwcApplication;
    public static boolean DEBUG_MODE = BuildConfig.DEBUG_MODE;

    @Override
    public void onCreate() {
        super.onCreate();
        mPwcApplication = this;
        init(DEBUG_MODE);
        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void init(Boolean debug) {
        super.init(debug);
        initDataBase();
        ButterKnife.setDebug(debug);
        SharedPreHelper.init(AppConfig.SHARED_PRE_FILE_NAME);
        DiskLruCacheUtil.init(AppConfig.MAX_CACHE_VALUE, AppConfig.CACHE_FOLDER);
        AppConfig.MODE_CONNECTION = (boolean) SharedPreHelper
                .get(this, Constants.MODE_CONNECTION, true);
        registerActivityLifecycleCallbacks(new ActivityLifeRecycleHandler());
    }

    private void initDataBase() {

    }

    public static PwcApplication getInstance() {
        return mPwcApplication;
    }


    /**
     * 退出应用程序
     */
    public void exit() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_FINISH));
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    /**
     * 获取一个新的Intent
     *
     * @param action
     * @param category
     * @return
     */
    public Intent newIntent(String action, String category) {
        Intent intent = new Intent(action);
        intent.addCategory(category);
        return intent;
    }

    public void sendBroadCast(String action, String category) {
        Intent intent = newIntent(action, category);
        sendBroadcast(intent);
    }

    public int getResColor(int resId) {
        return PwcApplication.getInstance().getResources().getColor(resId);
    }


    /**
     * 获取相应的缓存目录
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment
                .isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File cache = new File(cachePath + File.separator + uniqueName);

        if (!cache.exists()) {
            cache.mkdir();
        }
        return cache;
    }

    /**
     * @return 返回头像储存的位置， 为sd卡picture文件夹，如果sd卡没有安装，返回null
     */
    public static File getHeaderStorageFile() {
        File header = null;
        if (WindowUtils.isSdcardReady()) {
            header = new File(Environment.getExternalStorageDirectory(), AppConfig.PATH_HEAD);
            if (!header.exists()) {
                header.mkdirs();
            }
        }
        return header;
    }

    /**
     * @return 返回日志所在的文件夹，如果sd卡没有安装，返回data/data/包名/cache
     */
    public static File getCrashStorageFile() {
        File file = null;
        if (WindowUtils.isSdcardReady()) {
            file = new File(AppConfig.CRASH_LOG_STORAGE_PATH);
        } else {
            file = PwcApplication.getInstance().getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean isApplicationVisible() {
        return ActivityLifeRecycleHandler.isApplicationVisible();
    }

    public static boolean isApplicationInForeground() {
        return ActivityLifeRecycleHandler.isApplicationInvisible();
    }
}
