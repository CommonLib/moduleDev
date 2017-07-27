package com.pwc.sdc.recruit.business.photo.take;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface PhotoConstract {

    interface View extends ViewLayer{
    }

    interface Presenter extends ActivityPresenter{
        void openCamera();
    }
}
