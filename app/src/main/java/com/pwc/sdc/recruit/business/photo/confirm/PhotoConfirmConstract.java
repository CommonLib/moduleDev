package com.pwc.sdc.recruit.business.photo.confirm;

import android.graphics.Bitmap;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface PhotoConfirmConstract {

    interface View extends ViewLayer{
        void showPicture(Bitmap pic);
    }

    interface Presenter extends ActivityPresenter{
        void openCamera();
        void usePhoto();
    }
}
