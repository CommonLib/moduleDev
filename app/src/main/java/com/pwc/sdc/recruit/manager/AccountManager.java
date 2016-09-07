package com.pwc.sdc.recruit.manager;

import android.text.TextUtils;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.User;
import com.pwc.sdc.recruit.data.remote.RetrofitHelper;
import com.pwc.sdc.recruit.data.remote.RxHelper;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;
import com.thirdparty.proxy.utils.CyptoUtils;

import rx.Observable;

/**
 * @author
 * @created
 */
public class AccountManager {

    private static AccountManager mInstance = new AccountManager();
    private User mUser;

    private AccountManager() {
    }

    /**
     * 单一实例
     */
    public static AccountManager getInstance() {
        return mInstance;
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public User getUser() {
        return mUser;
    }

    /**
     * 登入
     *
     * @param user
     */
    public void login(User user) {
        mUser = user;
        SharedPreHelper.put(PwcApplication.getInstance(), Constants.KEY_TOKEN, CyptoUtils.desEncode(Constants.TOKEN_DES_PRIVATE, user.token));
        //PwcApplication.getInstance().sendBroadCast(Constants.ACTION_LOGIN, Constants.CATEGORY_DEFAULT);
    }

    /**
     * 从内存中登出
     */
    public void logout() {
        if (!isLogin()) {
            return;
        }

        Observable<HttpResponse<Void>> logoutApi = RetrofitHelper.getInstance().getService().logout();
        RxHelper.requestSingle(logoutApi, new HttpSubscriber<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mUser = null;
                SharedPreHelper.remove(PwcApplication.getInstance(), Constants.KEY_TOKEN);
                //PwcApplication.getInstance().sendBroadCast(Constants.ACTION_LOGOUT, Constants.CATEGORY_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (errorCode == Constants.API_CODE_INVALID_TOKEN || errorCode == Constants.API_CODE_LOGOUT_FAILURE) {
                    SharedPreHelper.remove(PwcApplication.getInstance(), Constants.KEY_TOKEN);
                    mUser = null;
                }
            }
        });
    }

    public boolean isLogin() {
        return mUser != null && mUser.token != null;
    }

    public boolean isTokenExitSp(){
        String cyptoToken = (String) SharedPreHelper.get(PwcApplication.getInstance(), Constants.KEY_TOKEN, "");
        return !TextUtils.isEmpty(cyptoToken);
    }

    /**
     * 从sp文件中登出
     */
    public void logoutFromSp() {
        String cyptoToken = (String) SharedPreHelper.get(PwcApplication.getInstance(), Constants.KEY_TOKEN, "");
        if (!TextUtils.isEmpty(cyptoToken)) {
            String token = CyptoUtils.desDecode(Constants.TOKEN_DES_PRIVATE, cyptoToken);
            User user = new User();
            user.token = token;
            login(user);
            logout();
        }
    }
}
