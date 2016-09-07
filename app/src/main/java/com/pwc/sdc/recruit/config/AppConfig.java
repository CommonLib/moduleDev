package com.pwc.sdc.recruit.config;

import com.pwc.sdc.recruit.PwcApplication;

import java.io.File;

/**
 * @author:dongpo 创建时间: 6/24/2016
 * 描述:
 * 修改:
 */
public class AppConfig {

    /**
     * 简历搜索页面每一次刷新简历的个数
     */
    public static final int LOAD_MORE_COUNT = 20;
    public static final int DEBUG_WATING = 100;
    public static boolean MODE_CONNECTION = true;

    /**
     * ------------上传头像的高度和宽度----------------
     */
    public static final int HEADER_WIDTH = 480;
    public static final int HEADER_HEIGHT = 640;

    /**
     * ------------sp文件名----------------
     */
    public static final String SHARED_PRE_FILE_NAME = "setting";
    /**
     * 最大缓存大小 10M
     */
    public static final int MAX_CACHE_VALUE = 10 * 1024 * 1024;
    /**
     * 硬盘缓存存放位置，如果sd卡不可用为data/data/包名/cache/，否则为sd卡 cache文件夹
     */
    public static final String CACHE_FOLDER = "http";
    /**
     * 拍摄头像存放位置，因调用系统相机，路径为 sd卡/android/包名/cache
     */
    public static final String PATH_HEAD = PwcApplication.getInstance().getExternalCacheDir() + File.separator + "Head";
    public static String BASE_URL = "http://sinw069030:8080/RecruitmentSystem/";

    /**
     * 网络请求2秒内多次点击Button或者其他view无效
     */
    public static final long BUTTON_REQUEST_CLICK_USELESS_TIME = 2000;
    /**
     * 普通按钮0.5秒多次点击Button或者其他view无效
     */
    public static final long BUTTON_COMMON_CLICK_USELESS_TIME = 500;
    /**
     * crash日志存放路径，如果当前有sd卡，为cache文件夹
     */
    public static final String CRASH_LOG_STORAGE_PATH = PwcApplication.getInstance().getExternalCacheDir() + File.separator + "Log";
}
