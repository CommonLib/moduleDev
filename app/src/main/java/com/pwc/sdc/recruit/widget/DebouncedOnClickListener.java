package com.pwc.sdc.recruit.widget;

import android.os.SystemClock;
import android.view.View;

import com.pwc.sdc.recruit.config.AppConfig;

import java.util.WeakHashMap;

/**
 * @author:dongpo 创建时间: 8/19/2016
 * 描述:
 * 修改:
 */
public abstract class DebouncedOnClickListener implements View.OnClickListener {

    private final long minimumInterval;
    private WeakHashMap<View, Long> lastClickMap;

    /**
     * Implement this in your subclass instead of onClick
     * @param v The view that was clicked
     */
    public abstract void onDebouncedClick(View v);

    /**
     * The one and only constructor
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<View, Long>();
    }
    public DebouncedOnClickListener() {
        this(AppConfig.BUTTON_COMMON_CLICK_USELESS_TIME);
    }


    @Override public void onClick(View clickedView) {
        Long previousClickTimestamp = lastClickMap.get(clickedView);
        long currentTimestamp = SystemClock.uptimeMillis();

        lastClickMap.put(clickedView, currentTimestamp);
        if(previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
            onDebouncedClick(clickedView);
        }
    }
}
