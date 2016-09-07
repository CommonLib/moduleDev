package com.pwc.sdc.recruit;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.thirdparty.proxy.log.TLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:dongpo 创建时间: 8/22/2016
 * 描述:
 * 修改:
 */
public class PwcExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefault;

    public PwcExceptionHandler() {
        mDefault = Thread.getDefaultUncaughtExceptionHandler();
    }

    private void saveCrashReport(Throwable ex) throws IOException, PackageManager.NameNotFoundException {
        File file = new File(PwcApplication.getCrashStorageFile(), getCrashReportFileName());
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        // 导出发生异常的时间
        pw.println(getTime());
        // 导出手机信息
        printPhoneInfo(pw);
        pw.println();
        // 导出异常的调用栈信息
        ex.printStackTrace(pw);
        pw.println();
        pw.close();
    }

    public String getCrashReportFileName() {
        StringBuilder sb = new StringBuilder();
        String time = getDateTime();
        return sb.append("crash_").append(System.currentTimeMillis()).append("_").append(time).append(".log").toString();
    }

    public String getDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return format.format(new Date());
    }

    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(new Date());
    }

    private void printPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = PwcApplication.getInstance().getPackageManager();
        PackageInfo pi = pm.getPackageInfo(PwcApplication.getInstance().getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            saveCrashReport(ex);
            if (TLog.DEBUG) {
                mDefault.uncaughtException(thread, ex);
            }
            PwcApplication.getInstance().exit();
        } catch (Exception e) {
            e.printStackTrace();
            PwcApplication.getInstance().exit();
        }
    }

}
