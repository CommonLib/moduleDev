package com.pwc.sdc.recruit.business.photo.take;

import android.app.Activity;
import android.content.Intent;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.photo.confirm.PhotoConfirmActivity;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.log.TLog;

import java.io.File;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoPresenter extends BasePresenter<PhotoActivity, PhotoMode>
        implements PhotoConstract.Presenter {
    private File mHeaderFile;

    public PhotoPresenter(PhotoActivity viewLayer, PhotoMode modelLayer) {
        super(viewLayer, modelLayer);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            TLog.d("take photo success, storage file : "+ mHeaderFile.getAbsolutePath());
            CandidateManager candidateManager = CandidateManager.getInstance();
            candidateManager.clear();
            candidateManager.setHeaderFile(mHeaderFile);
            mViewLayer.startActivity(PhotoConfirmActivity.class);
        }
    }

    @Override
    public void openCamera() {
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
