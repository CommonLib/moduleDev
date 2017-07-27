package com.pwc.sdc.recruit.business.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.profile.ProfileActivity;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.User;
import com.pwc.sdc.recruit.manager.AccountManager;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;
import com.thirdparty.proxy.utils.CyptoUtils;
import com.thirdparty.proxy.utils.FileUtil;

import java.util.Locale;

import rx.Observable;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class LoginPresenter extends BasePresenter<LoginActivity, LoginMode> implements LoginConstract.Presenter {

    /**
     * 是否弹出对话框
     */
    private boolean mIsShowDialog = false;
    /**
     * 是否要从sp中读取，或者从前一个activity读取（中英文切换时）
     */
    private boolean mIsReadFromSp = true;
    private String mAccount;
    private String mPassword;
    private boolean mIsRemember;
    /**
     * 有没有执行过一段代码，一个标志位
     */
    private boolean isExecuted = false;
    private boolean isLogining = false;

    @Override
    public void switchChinese(String userName, String password) {
        Locale currentLocale = getCurrentLocale();
        if ("zh".equalsIgnoreCase(currentLocale.getLanguage())) {
            return;
        }
        SharedPreHelper.put(mViewLayer,Constants.KEY_LANGUAGE, Constants.LANGUAGE_CHINESE);
        switchLanguage(Locale.CHINESE);
    }

    @Override
    public void switchEnglish(String userName, String password) {
        Locale currentLocale = getCurrentLocale();
        if (currentLocale == Locale.ENGLISH) {
            return;
        }
        SharedPreHelper.put(mViewLayer,Constants.KEY_LANGUAGE, Constants.LANGUAGE_ENGLISH);
        switchLanguage(Locale.ENGLISH);
    }

    @Override
    public void onActivityCreate() {
        if (mModelLayer.checkCrashLog()) {
            mModelLayer.uploadCrashLog();
        }
        logoutIfNot();
    }

    private void logoutIfNot(){
        AccountManager manager = AccountManager.getInstance();
        if(manager.isLogin()){
            manager.logout();
        }else if(manager.isTokenExitSp()){
            manager.logoutFromSp();
        }
    }

    @Override
    public void login(final String userName, String password, boolean isRemember) {
        if(isLogining){
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            mViewLayer.showToast(mViewLayer.getString(R.string.login_username_can_not_null));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mViewLayer.showToast(mViewLayer.getString(R.string.login_password_can_not_null));
            return;
        }

        if (isRemember) {
            mModelLayer.saveAccount(userName);
        } else {
            mModelLayer.removeAccount();
        }

        String md5Password = CyptoUtils.md5Encode(password);

        User user = new User();
        user.userName = userName;
        user.password = md5Password;

        isLogining = true;
        logoutIfNot();
        Observable<HttpResponse<User>> loginApi = getBackPointService().login(user);
        mModelLayer.login(loginApi, new HttpSubscriber<User>() {
            @Override
            public void onSuccess(User user) {
                user.userName = userName;
                AccountManager.getInstance().login(user);
                mViewLayer.clearPassword();
                mViewLayer.startActivity(ProfileActivity.class);
                isLogining = false;
            }

            @Override
            public void onFailure(int errorCode, String message) {
                isLogining = false;
            }
        });
    }

    private void switchLanguage(Locale shortName) {
        Resources res = mViewLayer.getResources();
        Configuration config = res.getConfiguration();
        DisplayMetrics metrics = res.getDisplayMetrics();
        config.locale = shortName;
        Locale.setDefault(shortName);
        res.updateConfiguration(config, metrics);
        mViewLayer.refreshActivity();
    }

    private Locale getCurrentLocale() {
        Resources res = mViewLayer.getResources();
        Configuration config = res.getConfiguration();
        return config.locale;
    }

    @Override
    public void subscribe() {
        if (!isExecuted) {
            initLanguageUI();
            //是否回显示账户
            if (mIsReadFromSp) {
                boolean accountSaved = mModelLayer.isAccountSaved();
                if (accountSaved) {
                    String account = mModelLayer.getAccount();
                    if (!TextUtils.isEmpty(account)) {
                        mViewLayer.setAccount(account);
                        mViewLayer.setRemember(true);
                    }
                }
            } else {
                mViewLayer.setAccount(mAccount);
                mViewLayer.setPassword(mPassword);
                mViewLayer.setRemember(mIsRemember);
            }

            if (mIsShowDialog == true) {
                mViewLayer.showAlertDialog();
                PwcApplication.getInstance().finishBeforeActivity();
                CandidateManager.getInstance().clear();
                FileUtil.deleteDirectory(PwcApplication.getHeaderStorageFile());
            }
            isExecuted = true;
        }
    }

    private void initLanguageUI() {
        Locale currentLocale = getCurrentLocale();
        String language = currentLocale.getLanguage();
        if (TextUtils.equals(Locale.CHINESE.getLanguage(), language) || TextUtils.equals(Locale.CHINA.getLanguage(), language)) {
            mViewLayer.setChineseBold();
        } else {
            mViewLayer.setEnglishBold();
        }
    }

    @Override
    public void handleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAccount = bundle.getString(Constants.KEY_ACCOUNT);
            mPassword = bundle.getString(Constants.KEY_PASSWORD);
            mIsRemember = bundle.getBoolean(Constants.KEY_REMEMBER_ACCOUNT);
            mIsShowDialog = intent.getExtras().getBoolean(Constants.ACTION_SHOW_DIALOG);
            if (mAccount != null && mPassword != null) {
                mIsReadFromSp = false;
            }
        }
    }
}
