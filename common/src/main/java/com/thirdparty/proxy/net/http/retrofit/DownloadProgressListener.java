package com.thirdparty.proxy.net.http.retrofit;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public interface DownloadProgressListener {
    void onDownLoadProgress(long totalSize, long current, long speed, boolean done);
}
