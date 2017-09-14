package com.pwc.sdc.recruit.business.main;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;

/**
 * @author:dongpo 创建时间: 2016/6/25
 * 描述:
 * 修改:
 */
public interface MainConstract {

    /**
     * 由view实现方法，供presenter调用
     */
    interface View extends ViewLayer{
        void showProgress();
    }

    /**
     * 有presenter实现，由view层调用
     */
    interface Presenter extends ActivityPresenter{
        void onclick();
    }

}
