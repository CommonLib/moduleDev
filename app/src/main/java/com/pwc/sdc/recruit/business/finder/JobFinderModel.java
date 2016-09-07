package com.pwc.sdc.recruit.business.finder;

import android.database.Cursor;

import com.pwc.sdc.recruit.base.BaseModel;

/**
 * Created by lhuang126 on 7/13/2016.
 */
public abstract class JobFinderModel extends BaseModel {
    public static final String JOB_FINDER_NAME="tb_name";

    /**
     * 获取应聘者名字集合
     * @param jobName 应聘者名字中包含的字段
     * @param callback 返回结果接口
     */
    protected abstract void getJobFinderNameCursor(String jobName, LoadJobNameCallback callback);
    protected abstract void getJobFinderCursor(String jobFinderName,LoadJobNameCallback callback);
    interface LoadJobNameCallback {

        void onJobNameLoaded(Cursor cursor);

        void onDataNotAvailable();
    }

    /**
     * this is only for test.
     */
    abstract void testInitDb();
}
