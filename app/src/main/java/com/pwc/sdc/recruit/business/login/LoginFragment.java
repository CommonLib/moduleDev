package com.pwc.sdc.recruit.business.login;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.business.DebugActivity;
import com.pwc.sdc.recruit.business.photo.take.PhotoActivity;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.utils.WindowUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class LoginFragment extends BaseFragment<LoginPresenter> implements TextView.OnEditorActionListener {
    @BindView(R.id.login_et_user_name)
    EditText mLoginEtUserName;
    @BindView(R.id.login_cb_remember_account)
    CheckBox mLoginCbRememberAccount;
    @BindView(R.id.login_et_user_password)
    EditText mLoginEtUserPassword;
    @BindView(R.id.login_tv_language_cn)
    TextView mLoginTvLanguageCn;
    @BindView(R.id.login_tv_language_en)
    TextView mLoginTvLanguageEn;
    @BindView(R.id.login_btn_recruiter)
    Button mLoginBtnRecruiter;

    private int mClickTime = 0;
    private long mLastClickTime = 0;

    @Override
    protected void initView() {
        mLoginEtUserPassword.setOnEditorActionListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick({R.id.login_btn_create_profile, R.id.login_btn_recruiter, R.id.login_tv_language_cn, R.id.login_tv_language_en, R.id.application_debug_panel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_create_profile:
                startActivity(PhotoActivity.class);
                break;
            case R.id.login_btn_recruiter:
                login();
                break;
            case R.id.login_tv_language_cn:
                mPresenter.switchChinese(getUserName(), getPassword());
                break;
            case R.id.login_tv_language_en:
                mPresenter.switchEnglish(getUserName(), getPassword());
                break;
            case R.id.application_debug_panel:
                openDebugPanel();
                break;
        }
    }

    private void openDebugPanel() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - mLastClickTime <= 1000) {
            mClickTime++;
        } else {
            mClickTime = 0;
        }
        TLog.d(mClickTime);
        if (mClickTime >= 7) {
            mActivity.startActivity(DebugActivity.class);
        }
        mLastClickTime = System.currentTimeMillis();
    }

    private void login() {
        String userName = getUserName();
        String password = getPassword();
        boolean isRemember = isAccountRemember();
        mPresenter.login(userName, password, isRemember);
    }

    public String getUserName() {
        return mLoginEtUserName.getText().toString().trim();
    }

    public String getPassword() {
        return mLoginEtUserPassword.getText().toString().trim();
    }

    public void setChineseBold() {
        mLoginTvLanguageCn.setSelected(true);
        mLoginTvLanguageEn.setSelected(false);
    }

    public void setEnglishBold() {
        mLoginTvLanguageCn.setSelected(false);
        mLoginTvLanguageEn.setSelected(true);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            int action = event.getAction();
            if (action == KeyEvent.ACTION_UP) {
                login();
            } else if (action == KeyEvent.ACTION_DOWN) {
                WindowUtils.hideSoftKeyboard(mLoginEtUserPassword);
            }
            return true;
        }
        return false;
    }

    public void setAccount(String account) {
        mLoginEtUserName.setText(account);
    }

    public void setRemember(boolean isRemember) {
        if (isRemember) {
            mLoginCbRememberAccount.setChecked(isRemember);
        }
    }

    public void setPassword(String password) {
        mLoginEtUserPassword.setText(password);
    }

    public boolean isAccountRemember() {
        return mLoginCbRememberAccount.isChecked();
    }

    public void clearPassword() {
        if(mLoginEtUserPassword != null){
            mLoginEtUserPassword.setText("");
        }
    }
}
