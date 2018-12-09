package com.moreclub.moreapp.util;

/**
 * Created by Captain on 2017/4/25.
 */

public class ClickUtils {
    private static long lastClickTime;
    private static long recordClickTime;
    private static long payClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isRecordClick() {
        long time = System.currentTimeMillis();
        if ( time - recordClickTime < 1000) {
            return true;
        }
        recordClickTime = time;
        return false;
    }

    public synchronized static boolean isPayClick() {
        long time = System.currentTimeMillis();
        if ( time - payClickTime < 1000) {
            return true;
        }
        payClickTime = time;
        return false;
    }
}
