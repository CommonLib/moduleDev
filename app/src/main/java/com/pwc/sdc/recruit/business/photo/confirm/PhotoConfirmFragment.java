package com.pwc.sdc.recruit.business.photo.confirm;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.utils.BitmapUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/13/2016
 * 描述:
 * 修改:
 */
public class PhotoConfirmFragment extends BaseFragment<PhotoConfirmPresenter> {
    @BindView(R.id.photo_iv_header)
    ImageView mPhotoIvHeader;
    @BindView(R.id.photo_tv_retake)
    TextView mPhotoTvRetake;
    @BindView(R.id.photo_tv_use)
    TextView mPhotoTvUse;

    @Override
    protected void initView() {
        CandidateManager manager = CandidateManager.getInstance();
        Bitmap header = BitmapUtils.readBitmapFromFile(manager.getHeaderFile(), AppConfig
                .HEADER_WIDTH, AppConfig.HEADER_HEIGHT);
        manager.setHeader(header);
        showPicture(header);
    }

    public void showPicture(Bitmap header){
        mPhotoIvHeader.setImageBitmap(header);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_photo;
    }

    @OnClick({R.id.photo_tv_retake, R.id.photo_tv_use})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo_tv_retake:
                mPresenter.openCamera();
                break;
            case R.id.photo_tv_use:
                mPresenter.usePhoto();
                break;
        }
    }

}
