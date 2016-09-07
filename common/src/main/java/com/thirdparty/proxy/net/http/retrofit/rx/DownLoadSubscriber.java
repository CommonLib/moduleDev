package com.thirdparty.proxy.net.http.retrofit.rx;

import com.thirdparty.proxy.net.http.retrofit.DownloadProgressListener;

import java.io.File;

/**
 * @author:dongpo 创建时间: 8/3/2016
 * 描述:
 * 修改:
 */
public abstract class DownLoadSubscriber extends HttpSubscriber<File> implements DownloadProgressListener {
    @Override
    public void onNext(File downloadFile) {
        onSuccess(downloadFile);
    }

    @Override
    public void onDownLoadProgress(long totalSize, long current, long speed, boolean done) {

    }
}
