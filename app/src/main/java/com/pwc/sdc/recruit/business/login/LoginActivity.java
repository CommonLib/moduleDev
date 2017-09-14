package com.pwc.sdc.recruit.business.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.MessageEvent;
import com.pwc.sdc.recruit.base.SingleFragmentActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class LoginActivity extends SingleFragmentActivity<LoginPresenter>
        implements LoginConstract.View, View.OnClickListener, DialogInterface.OnDismissListener,
                   DialogInterface.OnShowListener {


    private LoginFragment mLoginFragment;
    private TextView mTvNotes;
    private int mDelaySeconds;
    private Runnable mReduceTimeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_FINISH));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
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
    public void refreshActivity() {
        mLoginFragment = new LoginFragment();
        replaceFragment(R.id.single_fl_container, mLoginFragment);
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
        mDelaySeconds = 5;
        AlertDialog alertDialog = showAlertDialog(
                getString(R.string.delay_seconds_exit, mDelaySeconds), R.layout.dialog_success,
                this, null, this, this);
        mTvNotes = (TextView) alertDialog.findViewById(R.id.dialog_tv_title);
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
    protected void handleIntent(@NonNull Intent intent) {
        mPresenter.handleIntent(intent);
    }

    @Override
    public void onClick(View v) {
        PwcApplication.getInstance().exit();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Handler handler = PwcApplication.getHandler();
        if(mReduceTimeRunnable != null){
            handler.removeCallbacks(mReduceTimeRunnable);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        mReduceTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (mDelaySeconds <= 0) {
                    PwcApplication.getInstance().exit();
                } else {
                    --mDelaySeconds;
                    mTvNotes.setText(
                            String.valueOf(getString(R.string.delay_seconds_exit, mDelaySeconds)));
                    PwcApplication.getHandler().postDelayed(this, 1000);
                }

            }
        };
        PwcApplication.getHandler().postDelayed(mReduceTimeRunnable, 1000);
    }
}
