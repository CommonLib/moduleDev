package com.pwc.sdc.recruit;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.pwc.sdc.recruit.data.remote.RetrofitHelper;
import com.pwc.sdc.recruit.data.remote.RxHelper;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.UpLoadSubscriber;
import com.thirdparty.proxy.utils.FileUtil;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class LogUploadService extends IntentService {

    private File mCrashStorageFile;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LogUploadService(String name) {
        super(name);
    }

    public LogUploadService() {
        this("logUploadThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mCrashStorageFile = PwcApplication.getCrashStorageFile();
        File[] files = mCrashStorageFile.listFiles();
        HashMap<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isLogFile(file)) {
                RequestBody body = RequestBody.create(MediaType.parse("text/plain"), file);
                map.put("text\"; filename=\"" + file.getName() + "", body);
            }
        }

        Observable<HttpResponse<Void>> uploadCrashReportApi = RetrofitHelper.getInstance().getService().uploadCrashReport(map);
        RxHelper.upload(uploadCrashReportApi, new UpLoadSubscriber<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //上传成功，删除所有的文件
                FileUtil.deleteDirectory(mCrashStorageFile);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                
            }
        });
    }

    private boolean isLogFile(File file) {
        if (file == null || file.isDirectory()) {
            return false;
        }

        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        String suffix = fileName.substring(index + 1);
        if (TextUtils.equals("log", suffix)) {
            return true;
        }
        return false;
    }

}
