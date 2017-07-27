package com.thirdparty.proxy.net.http.retrofit.rx;

import com.thirdparty.proxy.net.http.retrofit.UploadProgressListener;

/**
 * @author:dongpo 创建时间: 8/3/2016
 * 描述:
 * 修改:
 */
public abstract class UpLoadSubscriber<T> extends HttpSubscriber<T> implements UploadProgressListener {

    @Override
    public void onUpLoadProgress(String tag, long totalSize, long speed, long current, boolean done) {

    }
}
