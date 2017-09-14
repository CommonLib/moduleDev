package com.pwc.sdc.recruit.base;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;

/**
 * @author:dongpo 创建时间: 6/23/2016
 * 描述:
 * 修改:
 */
public abstract class SingleFragmentActivity<T extends ActivityPresenter> extends BaseActivity<T> {

    private BaseFragment mFirstFragment;

    @Override
    protected void initView() {
        mFirstFragment = getFirstFragment();
        replaceFragment(R.id.single_fl_container, mFirstFragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    protected abstract BaseFragment getFirstFragment();

    @Override
    public void updateViewState(int state) {
        mFirstFragment.updateViewState(state);
    }
}
