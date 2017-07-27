package com.pwc.sdc.recruit.business.photo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.thirdparty.proxy.utils.SensorUtil;

/**
 * @author:dongpo 创建时间: 8/8/2016
 * 描述:
 * 修改:
 */
public class HomeReceiver extends BroadcastReceiver {
    private final IntentFilter mFilter;
    private final boolean mOpenSensor;
    private static boolean isExecute = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isExecute) {
            //只监听一次
            if (mOpenSensor) {
                SensorUtil.openSensor(context);
            } else {
                SensorUtil.closeSensor(context);
            }
            isExecute = true;
        }
    }

    public HomeReceiver(boolean openSensor) {
        mOpenSensor = openSensor;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void startListener(Context context) {
        context.registerReceiver(this, mFilter);
    }

    public void stopListener(Context context) {
        context.unregisterReceiver(this);
    }
}
