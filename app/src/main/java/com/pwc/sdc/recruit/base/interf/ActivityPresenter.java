package com.pwc.sdc.recruit.base.interf;

import android.content.Intent;
import android.os.Bundle;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author:dongpo 创建时间: 6/27/2016
 * 描述:
 * 修改:
 */
public interface ActivityPresenter extends PresenterLayer{
    void handleIntent(Intent intent);
    void onRestoreInstanceState(Bundle savedInstanceState);
    void onRefresh(PtrFrameLayout frame);
    void onActivitySaveInstanceState(Bundle outState);
    void onFragmentSaveInstanceState(Bundle outState);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onActivityStop();
    void onActivityCreate();
}
