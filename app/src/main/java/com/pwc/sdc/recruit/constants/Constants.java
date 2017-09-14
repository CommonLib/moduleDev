package com.pwc.sdc.recruit.constants;

/**
 * 常量类
 * 
 * @author
 * @version
 * 
 */

public class Constants {

    //这个广播是注销登陆会发送的
    public static final String ACTION_LOGOUT = "pwc.intent.action.logout";
    public static final String ACTION_LOGIN = "pwc.intent.action.login";
    public static final String ACTION_UPLOAD_SUCCESS = "pwc.intent.action.upload.success";
    public static final String ACTION_UPLOAD_FAILURE = "pwc.intent.action.upload.failure";
    public static final String ACTION_UPLOAD_UPDATE_PROGRESS = "pwc.intent.action.upload.progress";

    public static final String CATEGORY_DEFAULT = "pwc.intent.category.default";

    /**------------网络请求的状态码，用来更新界面----------------*/
    public static final int STATE_USER = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_EMPTY = 3;

    public static final int API_CODE_PROFILE_SEARCH_EMPTY = 7;
    public static final int API_CODE_EMPLOYEE_SEARCH_EMPTY = 15;
    public static final int API_CODE_INVALID_TOKEN = 11;
    public static final int API_CODE_LOGOUT_FAILURE = 17;

    /**------------上传文件----------------*/
    public static final String UPLOAD_TOTAL_SIZE = "total_size";
    public static final String UPLOAD_CURRENT_SIZE = "current_size";
    public static final String UPLOAD_TAG = "upload_tag";
    public static final String UPLOAD_ERROR_MESSAGE = "upload_error";
    public static final String UPLOAD_ERROR_CODE = "upload_error_code";
    public static final String NETWORK_SPEED = "network_speed";
    public static final String ACTION_SHOW_DIALOG = "action_show_dialog";

    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REMEMBER_ACCOUNT = "remember_account";

    public static final String BASE_URL = "base_url";
    public static final String MODE_CONNECTION = "no_connection";
    public static final String PROFILE_LANGUAGE_FORMAT_ENGLISH = "english";
    public static final String PROFILE_LANGUAGE_FORMAT_CHNIESE = "chinese";
    public static final String KEY_TOKEN = "token";
    public static final String TOKEN_DES_PRIVATE = "pwc/eApplication";
    public static final String KEY_LANGUAGE = "language";
    public static final String LANGUAGE_CHINESE = "chinese";
    public static final String LANGUAGE_ENGLISH = "english";

    public static final String SEARCH_FIELD_CHINESE_NAME = "chineseName";
    public static final String SEARCH_FIELD_PINYIN = "pinYin";
    public static final String SEARCH_FIELD_ENGLISH_NAME = "englishName";
    public static final String SEARCH_FIELD_MOBILE = "mobile";
    public static final String SEARCH_FIELD_EMAIL = "email";
}
