package com.pwc.sdc.recruit.base;

/**
 * Created by byang059 on 8/16/17.
 */

public class MessageEvent {
    public static final int EVENT_FINISH = 0x1;
    public int what;

    public MessageEvent(int what) {
        this.what = what;
    }
}
