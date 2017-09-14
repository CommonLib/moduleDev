package com.pwc.sdc.recruit.business.info.regulation;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.widget.SignatureView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class RegulationFragment extends BaseFragment<RegulationPresenter> {

    @BindView(R.id.regulation_cb_declaration1)
    CheckBox mRegulationCbDeclaration1;
    @BindView(R.id.regulation_cb_declaration2)
    CheckBox mRegulationCbDeclaration2;
    @BindView(R.id.regulation_sv_signatureView)
    SignatureView mRegulationSignatureView;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_regulation;
    }

    @OnClick({R.id.regulation_btn_submit,R.id.regulation_tv_declaration1,R.id
            .regulation_tv_declaration2, R.id.regulation_btn_clean_signature})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regulation_btn_submit:
                mPresenter.submit(mRegulationSignatureView, mRegulationCbDeclaration1,
                        mRegulationCbDeclaration2);
                break;
            case R.id.regulation_tv_declaration1:
                mRegulationCbDeclaration1.setChecked(!mRegulationCbDeclaration1.isChecked());
                break;
            case R.id.regulation_tv_declaration2:
                mRegulationCbDeclaration2.setChecked(!mRegulationCbDeclaration2.isChecked());
                break;
            case R.id.regulation_btn_clean_signature:
                cleanSignature();
                break;
            default:
                break;
        }
    }

    private void cleanSignature() {
        mRegulationSignatureView.cleanSinature();
    }

    public Bitmap getSignatureBitmap() {
        return mRegulationSignatureView.getSignaturePic();
    }
}
