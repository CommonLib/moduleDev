package com.pwc.sdc.recruit.business.photo.confirm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.info.resume.InfoActivity;
import com.pwc.sdc.recruit.business.photo.HomeReceiver;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.utils.BitmapUtils;
import com.thirdparty.proxy.utils.SensorUtil;

import java.io.File;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoConfirmPresenter extends BasePresenter<PhotoConfirmActivity, PhotoConfirmMode> implements PhotoConfirmConstract.Presenter {

    private File mNewHeaderFile;
    private boolean mOpenSensor;
    private HomeReceiver mHomeReceiver;

    public PhotoConfirmPresenter(PhotoConfirmActivity viewLayer, PhotoConfirmMode modelLayer) {
        super(viewLayer, modelLayer);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //恢复初始值的状态
        if (mOpenSensor) {
            SensorUtil.openSensor(mViewLayer);
        } else {
            SensorUtil.closeSensor(mViewLayer);
        }
        //取消广播接收
        if (mHomeReceiver != null) {
            mHomeReceiver.stopListener(mViewLayer);
        }

        if (resultCode == Activity.RESULT_OK) {
            CandidateManager manager = CandidateManager.getInstance();
            manager.setHeaderFile(mNewHeaderFile);
            Bitmap newHeader = BitmapUtils.adjustBitmapRotation(mNewHeaderFile, 270, AppConfig.HEADER_WIDTH, AppConfig.HEADER_HEIGHT);
            mViewLayer.showPicture(newHeader);
            manager.recycleHeader();
            manager.setHeader(newHeader);
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
            mNewHeaderFile = new File(headerStorage, headerName);
            mViewLayer.openCamera(mNewHeaderFile);
        }
    }

    @Override
    public void usePhoto() {
        mViewLayer.startActivity(InfoActivity.class);
    }
}
