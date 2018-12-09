package com.moreclub.moreapp.main.utils;

/**
 * Created by Administrator on 2017-07-21-0021.
 */

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class TestWakeLock {
    private WakeLock mWakeLock;
    private Context mContext;

    public TestWakeLock(Context context) {
        this.mContext = context;
    }

    public void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "More");
            if (mWakeLock != null) {
                mWakeLock.acquire();
                Log.e("zune", "get powermanager wakelock!");
            }
        }
    }

    public void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            Log.e("zune", "release powermanager wakelock!");
        }
    }
}