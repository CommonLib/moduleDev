package com.pwc.sdc.recruit.business.login;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.base.BaseModel;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.User;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import java.io.File;

import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class LoginMode extends BaseModel {
    public Subscription login(Observable<HttpResponse<User>> request, final HttpSubscriber<User> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            return sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    User user = new User();
                    user.token = "432525324141412412";
                    callBack.onSuccess(user);
                }
            }, AppConfig.DEBUG_WATING);
        }
        return null;
    }

    public void saveAccount(String userName) {
        SharedPreHelper.put(getApplicationContext(), Constants.KEY_ACCOUNT, userName);
        SharedPreHelper.put(getApplicationContext(), Constants.KEY_REMEMBER_ACCOUNT, true);
    }

    public void removeAccount() {
        SharedPreHelper.remove(getApplicationContext(), Constants.KEY_ACCOUNT);
        SharedPreHelper.remove(getApplicationContext(), Constants.KEY_REMEMBER_ACCOUNT);
    }

    public String getAccount() {
        String account = (String) SharedPreHelper.get(getApplicationContext(), Constants.KEY_ACCOUNT, "");
        return account;
    }

    public boolean isAccountSaved() {
        boolean isRemember = (boolean) SharedPreHelper.get(getApplicationContext(), Constants.KEY_REMEMBER_ACCOUNT, false);
        return isRemember;
    }

    /**
     * @return 检查是否有log需要上传
     */
    public boolean checkCrashLog() {
        File crashStorageFile =  PwcApplication.getCrashStorageFile();
        if (crashStorageFile.exists() && crashStorageFile.isDirectory()) {
            return crashStorageFile.listFiles().length > 0;
        }
        return false;
    }

    public void uploadCrashLog() {
    }
}
