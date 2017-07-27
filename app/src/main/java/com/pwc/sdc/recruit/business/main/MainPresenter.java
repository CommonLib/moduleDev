package com.pwc.sdc.recruit.business.main;

import com.pwc.sdc.recruit.base.BasePresenter;

import java.util.List;


/**
 * @author:dongpo 创建时间: 2016/6/25
 * 描述:
 * 修改:
 */
public class MainPresenter extends BasePresenter<MainConstract.View, MainMode> implements MainConstract.Presenter {


    @Override
    public void onclick() {
        //处理逻辑
        List<String> data = mModelLayer.getData();
        mViewLayer.showProgress();
    }
}
