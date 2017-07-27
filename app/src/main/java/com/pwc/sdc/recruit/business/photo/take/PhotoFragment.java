package com.pwc.sdc.recruit.business.photo.take;

import android.widget.ImageView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoFragment extends BaseFragment<PhotoPresenter> {
    @BindView(R.id.photo_iv_take_photo)
    ImageView mPhotoIvTakePhoto;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_take_photo;
    }

    @OnClick(R.id.photo_iv_take_photo)
    public void onClick() {
        mPresenter.openCamera();
    }

}
