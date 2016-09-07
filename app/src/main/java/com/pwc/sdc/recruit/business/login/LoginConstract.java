package com.pwc.sdc.recruit.business.login;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface LoginConstract {

    interface View extends ViewLayer{
        void refreshActivity();
        void setChineseBold();
        void setEnglishBold();
        void showAlertDialog();
        void setAccount(String account);
        void setRemember(boolean isRemember);
        void setPassword(String password);
        void clearPassword();
    }

    interface Presenter extends ActivityPresenter{
        void switchChinese(String userName, String password);
        void switchEnglish(String userName, String password);
        void login(String userName, String password,boolean isRemember);
    }
}
