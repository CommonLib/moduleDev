package com.pwc.sdc.recruit.data.model;

/**
 * @author:dongpo 创建时间: 9/7/2016
 * 描述:
 * 修改:
 */
public class UploadInfo {
    public String action;
    public String tag;
    public long totalSize;
    public long currentSize;
    public long speed;
    public int errorCode;
    public String errorMessage;

    public void clear() {
        action = null;
        tag = null;
        totalSize = 0;
        currentSize = 0;
        speed = 0;
        errorCode = 0;
        errorMessage = null;
    }
}
