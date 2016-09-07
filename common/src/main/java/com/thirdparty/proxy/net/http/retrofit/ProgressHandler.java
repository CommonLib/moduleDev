package com.thirdparty.proxy.net.http.retrofit;

import android.os.Looper;
import android.os.Message;

import com.thirdparty.proxy.log.TLog;

import java.lang.ref.WeakReference;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class ProgressHandler extends android.os.Handler {
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_DOWNLOAD = 2;

    private WeakReference<UploadProgressListener> mUploadListenerWR;
    private WeakReference<DownloadProgressListener> mDownloadListenerWR;

    public ProgressHandler() {
        super(Looper.getMainLooper());
    }

    public ProgressHandler(DownloadProgressListener listener) {
        super(Looper.getMainLooper());
        this.mDownloadListenerWR = new WeakReference<>(listener);
    }

    public void setUploadProgressListener(UploadProgressListener uploadProgressListener) {
        mUploadListenerWR = new WeakReference<>(uploadProgressListener);
    }

    public void setDownLoadProgressListener(DownloadProgressListener downloadProgressListener) {
        mDownloadListenerWR = new WeakReference<>(downloadProgressListener);
    }

    @Override
    public void handleMessage(Message msg) {
        ProgressInfo info = (ProgressInfo) msg.obj;
        switch (msg.what) {
            case TYPE_UPLOAD:
                if (mUploadListenerWR == null) {
                    return;
                }
                UploadProgressListener upListener = mUploadListenerWR.get();
                if (upListener == null) {
                    TLog.e("progress callback is GC by dvm, can't process progress update");
                    return;
                }
                upListener.onUpLoadProgress(info.tag, info.totalSize, info.current, info.networkSpeed, info.isDone);
                break;
            case TYPE_DOWNLOAD:
                if (mDownloadListenerWR == null) {
                    return;
                }
                DownloadProgressListener downListener = mDownloadListenerWR.get();
                if (downListener == null) {
                    TLog.e("progress callback is GC by dvm, can't process progress update");
                    return;
                }
                downListener.onDownLoadProgress(info.totalSize, info.current, info.networkSpeed, info.isDone);
                break;
        }
    }
}
