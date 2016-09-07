package com.pwc.sdc.recruit;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.pwc.sdc.recruit.base.BaseActivity;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.squareup.leakcanary.LeakCanary;
import com.thirdparty.proxy.base.BaseApplication;
import com.thirdparty.proxy.cache.DiskLruCacheUtil;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.utils.WindowUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

import butterknife.ButterKnife;


/**
 * 全局应用程序类：
 *
 * @author
 * @created
 */
public class PwcApplication extends BaseApplication {

    private LinkedList<BaseActivity> mActivities;
    private static PwcApplication mPwcApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mPwcApplication = this;
        mActivities = new LinkedList<>();
        init(BuildConfig.DEBUG_MODE);
    }

    @Override
    public void init(Boolean debug) {
        super.init(debug);
        LeakCanary.install(this);
        initDataBase();
        ButterKnife.setDebug(BuildConfig.DEBUG_MODE);
        SharedPreHelper.init(AppConfig.SHARED_PRE_FILE_NAME);
        DiskLruCacheUtil.init(AppConfig.MAX_CACHE_VALUE, AppConfig.CACHE_FOLDER);
        AppConfig.MODE_CONNECTION = (boolean) SharedPreHelper.get(this, Constants.MODE_CONNECTION, true);
        registerActivityLifecycleCallbacks(new ActivityLifeRecycleHandler());
    }

    private void initDataBase() {

    }

    public static PwcApplication getInstance() {
        return mPwcApplication;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(BaseActivity paramActivity) {
        mActivities.add(paramActivity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            mActivities.remove(activity);
            activity = null;
        }
    }

    public BaseActivity getTopActivity() {
        return mActivities.getFirst();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        ListIterator<BaseActivity> iterator = mActivities.listIterator();
        while (iterator.hasNext()) {
            BaseActivity activity = iterator.next();
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 结束除栈顶外的所有activity
     */
    public void finishBeforeActivity() {
        while (mActivities.size() > 1) {
            BaseActivity activity = mActivities.removeFirst();
            activity.finish();
        }
    }

    /**
     * 退出应用程序
     */
    public void exit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
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
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
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
