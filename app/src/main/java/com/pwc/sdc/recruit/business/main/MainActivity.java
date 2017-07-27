package com.pwc.sdc.recruit.business.main;

import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.SingleFragmentActivity;
import com.pwc.sdc.recruit.business.main.fragment.MainFragment;

public class MainActivity extends SingleFragmentActivity<MainConstract.Presenter> implements MainConstract.View {

    private MainFragment mFirstFragment;

    @Override
    protected BaseFragment getFirstFragment() {
        mFirstFragment = obtainFragment(MainFragment.class);
        return mFirstFragment;
    }


    @Override
    protected void initData() {
        mPresenter.onclick();
    }


    @Override
    public void showProgress() {

    }
}
