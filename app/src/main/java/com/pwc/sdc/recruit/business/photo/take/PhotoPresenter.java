package com.pwc.sdc.recruit.business.photo.take;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.photo.HomeReceiver;
import com.pwc.sdc.recruit.business.photo.confirm.PhotoConfirmActivity;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.utils.SensorUtil;

import java.io.File;
import java.util.Locale;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoPresenter extends BasePresenter<PhotoActivity, PhotoMode>
        implements PhotoConstract.Presenter {
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
        String language = (String) SharedPreHelper
                .get(mViewLayer, Constants.KEY_LANGUAGE, Constants.LANGUAGE_CHINESE);
        if (TextUtils.equals(language, Constants.LANGUAGE_CHINESE)) {
            setOriginLocale(Locale.CHINESE);
        }else{
            setOriginLocale(Locale.ENGLISH);
        }
        //取消广播接收
        if (mHomeReceiver != null) {
            mHomeReceiver.stopListener(mViewLayer);
        }

        if (resultCode == Activity.RESULT_OK) {
            CandidateManager candidateManager = CandidateManager.getInstance();
            candidateManager.clear();
            candidateManager.setHeaderFile(mHeaderFile);
            mViewLayer.startActivity(PhotoConfirmActivity.class);
        }
    }

    private void setOriginLocale(Locale shortName){
        Resources res = mViewLayer.getResources();
        Configuration config = res.getConfiguration();
        DisplayMetrics metrics = res.getDisplayMetrics();
        config.locale = shortName;
        Locale.setDefault(shortName);
        res.updateConfiguration(config, metrics);
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
