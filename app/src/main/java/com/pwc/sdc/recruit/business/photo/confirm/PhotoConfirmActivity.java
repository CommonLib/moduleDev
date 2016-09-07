package com.pwc.sdc.recruit.business.photo.confirm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.SingleFragmentActivity;
import com.pwc.sdc.recruit.business.photo.take.PhotoActivity;

import java.io.File;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class PhotoConfirmActivity extends SingleFragmentActivity<PhotoConfirmPresenter> implements PhotoConfirmConstract.View {

    private PhotoConfirmFragment mPhotoConfirmFragment;

    @Override
    protected BaseFragment getFirstFragment() {
        mPhotoConfirmFragment = obtainFragment(PhotoConfirmFragment.class);
        return mPhotoConfirmFragment;
    }

    @Override
    protected void initData() {

    }

    public void openCamera(File storagePath) {
        String action = MediaStore.ACTION_IMAGE_CAPTURE;
        Intent intent = new Intent(action);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(storagePath));
        startActivityForResult(intent, PhotoActivity.TAKE_PHOTO_REQUEST_CODE);
    }

    @Override
    protected PhotoConfirmPresenter instancePresenter() {
        return new PhotoConfirmPresenter(this, new PhotoConfirmMode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showPicture(Bitmap pic) {
        mPhotoConfirmFragment.showPicture(pic);
    }
}
