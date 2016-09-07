package com.pwc.sdc.recruit.business.photo.take;

import android.app.Activity;
import android.content.Intent;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.photo.HomeReceiver;
import com.pwc.sdc.recruit.business.photo.confirm.PhotoConfirmActivity;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.utils.SensorUtil;

import java.io.File;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoPresenter extends BasePresenter<PhotoActivity, PhotoMode> implements PhotoConstract.Presenter {
    private File mHeaderFile;
    private HomeReceiver mHomeReceiver;
    private boolean mOpenSensor;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //恢复初始值的状态
        if (mOpenSensor) {
            SensorUtil.openSensor(mViewLayer);
        } else {
            SensorUtil.closeSensor(mViewLayer);
        }
        //取消广播接收
        if(mHomeReceiver != null){
            mHomeReceiver.stopListener(mViewLayer);
        }

        if (resultCode == Activity.RESULT_OK) {
            CandidateManager candidateManager = CandidateManager.getInstance();
            candidateManager.clear();
            candidateManager.setHeaderFile(mHeaderFile);
            mViewLayer.startActivity(PhotoConfirmActivity.class);
        }
    }

    @Override
    public void openCamera() {
        //关闭重力感应，设置屏幕为竖屏，并注册广播监听Home键
        //记录初始值
        mOpenSensor = SensorUtil.isOpenSensor(mViewLayer);
        SensorUtil.closeSensor(mViewLayer);
        SensorUtil.setLandscape(mViewLayer);
        mHomeReceiver = new HomeReceiver(mOpenSensor);
        mHomeReceiver.startListener(mViewLayer);
        File headerStorage = PwcApplication.getHeaderStorageFile();
        if (headerStorage == null) {
            mViewLayer.showToast(mViewLayer.getString(R.string.no_sd_card));
        } else {
            String headerName = System.currentTimeMillis() + ".png";
            mHeaderFile = new File(headerStorage, headerName);
            mViewLayer.openCamera(mHeaderFile);
        }
    }
}
