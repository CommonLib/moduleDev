package com.pwc.sdc.recruit.business.info.regulation;

import android.widget.CheckBox;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;

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

    @OnClick(R.id.regulation_btn_submit)
    public void onClick() {
        mPresenter.submit(mRegulationCbDeclaration1, mRegulationCbDeclaration2);
    }

}
