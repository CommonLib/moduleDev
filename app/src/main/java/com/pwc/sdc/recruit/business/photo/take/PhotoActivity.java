package com.pwc.sdc.recruit.business.photo.take;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.SingleFragmentActivity;
import com.pwc.sdc.recruit.business.photo.confirm.PhotoConfirmFragment;

import java.io.File;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoActivity extends SingleFragmentActivity<PhotoPresenter> implements PhotoConstract.View {
    public static final int TAKE_PHOTO_REQUEST_CODE = 100;

    private PhotoFragment mPhotoFragment;
    private PhotoConfirmFragment mPhotoConfirmFragment;

    @Override
    protected BaseFragment getFirstFragment() {
        mPhotoFragment = obtainFragment(PhotoFragment.class);
        return mPhotoFragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected PhotoPresenter instancePresenter() {
        return new PhotoPresenter(this, new PhotoMode());
    }

    public void openCamera(File storagePath) {
        String action = MediaStore.ACTION_IMAGE_CAPTURE;
        Intent intent = new Intent(action);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(storagePath));
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
