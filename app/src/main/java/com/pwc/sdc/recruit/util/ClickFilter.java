package com.pwc.sdc.recruit.util;

import android.view.View;

import com.pwc.sdc.recruit.config.AppConfig;
import com.thirdparty.proxy.log.TLog;

/**
 * @author:dongpo 创建时间: 8/22/2016
 * 描述:
 * 修改:
 */
public class ClickFilter {
    private static long INTERVAL = AppConfig.BUTTON_COMMON_CLICK_USELESS_TIME; //防止连续点击的时间间隔

    public static boolean filter(View v) {
        long lastClickTime = 0;
        Object lastClick = v.getTag(v.getId());
        if (lastClick != null) {
            lastClickTime = (long) lastClick;
        }

        long time = System.currentTimeMillis();
        if ((time - lastClickTime) > INTERVAL) {
            v.setTag(v.getId(), time);
            TLog.d("点击有效");
            return false;
        }
        TLog.d("多次频率过快，过滤点击效果");
        v.setTag(v.getId(), time);
        return true;
    }

    public static boolean filter(View v, long filterTime) {
        long lastClickTime = 0;
        Object lastClick = v.getTag(v.getId());
        if (lastClick != null) {
            lastClickTime = (long) lastClick;
        }

        long time = System.currentTimeMillis();
        if ((time - lastClickTime) > filterTime) {
            v.setTag(v.getId(), time);
            TLog.d("点击有效");
            return false;
        }
        TLog.d("多次频率过快，过滤点击效果");
        v.setTag(v.getId(), time);
        return true;
    }
}
