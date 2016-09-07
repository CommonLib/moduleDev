package com.pwc.sdc.recruit.business.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.SingleFragmentActivity;
import com.pwc.sdc.recruit.constants.Constants;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class LoginActivity extends SingleFragmentActivity<LoginPresenter> implements LoginConstract.View, View.OnClickListener {


    private LoginFragment mLoginFragment;

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BaseFragment getFirstFragment() {
        mLoginFragment = obtainFragment(LoginFragment.class);
        return mLoginFragment;
    }

    @Override
    protected LoginPresenter instancePresenter() {
        return new LoginPresenter(this, new LoginMode());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.activity_right_out);
        PwcApplication.getInstance().exit();
    }

    @Override
    public void refreshActivity() {
        Bundle bundle = obtainBundle();
        bundle.putString(Constants.KEY_ACCOUNT, mLoginFragment.getUserName());
        bundle.putString(Constants.KEY_PASSWORD, mLoginFragment.getPassword());
        bundle.putBoolean(Constants.KEY_REMEMBER_ACCOUNT, mLoginFragment.isAccountRemember());
        startActivity(LoginActivity.class, bundle);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void setChineseBold() {
        mLoginFragment.setChineseBold();
    }

    @Override
    public void setEnglishBold() {
        mLoginFragment.setEnglishBold();
    }

    @Override
    public void showAlertDialog() {
        showAlertDialog(null, R.layout.dialog_success, this);
    }

    @Override
    public void setAccount(String account) {
        mLoginFragment.setAccount(account);
    }

    @Override
    public void setRemember(boolean isRemember) {
        mLoginFragment.setRemember(isRemember);
    }

    @Override
    public void setPassword(String password) {
        mLoginFragment.setPassword(password);
    }

    @Override
    public void clearPassword() {
        mLoginFragment.clearPassword();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void handleIntent(Intent intent) {
        mPresenter.handleIntent(intent);
    }

    @Override
    public void onClick(View v) {
        hideAlertDialog();
    }
}
