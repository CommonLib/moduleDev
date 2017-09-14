package com.pwc.sdc.recruit.business.photo.confirm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.info.resume.InfoActivity;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.utils.BitmapUtils;
import com.thirdparty.proxy.utils.FileUtil;

import java.io.File;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoConfirmPresenter extends BasePresenter<PhotoConfirmActivity, PhotoConfirmMode> implements PhotoConfirmConstract.Presenter {

    private File mNewHeaderFile;

    public PhotoConfirmPresenter(PhotoConfirmActivity viewLayer, PhotoConfirmMode modelLayer) {
        super(viewLayer, modelLayer);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            CandidateManager manager = CandidateManager.getInstance();
            manager.setHeaderFile(mNewHeaderFile);
            Bitmap newHeader = BitmapUtils.readBitmapFromFile(mNewHeaderFile, AppConfig
                    .HEADER_WIDTH, AppConfig.HEADER_HEIGHT);
            mViewLayer.showPicture(newHeader);
            manager.recycleHeader();
            manager.setHeader(newHeader);
        }
    }

    @Override
    public void openCamera() {
        FileUtil.deleteDirectory(PwcApplication.getHeaderStorageFile());
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
