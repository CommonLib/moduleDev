package com.pwc.sdc.recruit.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.base.interf.ActivityModel;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;
import com.pwc.sdc.recruit.data.remote.BackPointService;
import com.pwc.sdc.recruit.data.remote.RetrofitHelper;
import com.thirdparty.proxy.utils.TUtil;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author:dongpo 创建时间: 6/24/2016
 * 描述: Presenter层基类
 * 修改:
 */
public abstract class BasePresenter<V extends ViewLayer, M extends ActivityModel>{

    public V mViewLayer;
    public M mModelLayer;

    public BasePresenter(){
        mModelLayer = TUtil.getT(this, 1);
    }

    public void onRefresh(PtrFrameLayout frame){}

    public void sendBroadCast(String action, String category) {
        Intent intent = new Intent(action);
        intent.addCategory(category);
        PwcApplication.getInstance().sendBroadcast(intent);
    }

    public void subscribe() {

    }

    public void unSubscribe() {
    }

    public void onActivityStop(){
        mModelLayer.cancelAllRequest();
    }

    public void onActivityCreate(){

    }

    protected BackPointService getBackPointService() {
        return RetrofitHelper.getInstance().getService();
    }

    protected Handler getHandler(){
        return PwcApplication.getHandler();
    }

    public <T extends ActivityPresenter> void setViewLayer(BaseActivity<T> tBaseActivity){
          mViewLayer = (V) tBaseActivity;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {}
    public void onActivitySaveInstanceState(Bundle outState){}
    public void onFragmentSaveInstanceState(Bundle outState){}
    public void handleIntent(Intent intent){}
    public void onRestoreInstanceState(Bundle savedInstanceState){}
}
