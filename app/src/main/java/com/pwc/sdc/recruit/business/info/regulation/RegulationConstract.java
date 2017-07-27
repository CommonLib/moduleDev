package com.pwc.sdc.recruit.business.info.regulation;

import android.widget.CheckBox;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface RegulationConstract {

    interface View extends ViewLayer {
        void showAlertDialog();

        void showProgressDialog();
        void hideProgressDialog();
        void setUploadDescription(String title);
        void uploadProgressBar(int progress);
    }

    interface Presenter extends ActivityPresenter {
        void submit(CheckBox regulation1, CheckBox regulation2);
        void onDialogDismiss();
    }
}
