package com.moreclub.common.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class PhoneUtil {

    public static boolean getPhoneLockState(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            return true;
        }

        return false;
    }

    /**
     * 获取手机ESN CDMA手机机身号简称ESN-Electronic Serial Number的缩写。
     *
     * 它是一个32bits长度的参数，是手机的惟一标识。
     *
     * GSM手机是IMEI码(International Mobile Equipment Identity)，
     *
     * 国际移动身份码。
     */
    public static String getEsn(final Context mActivity) {
        TelephonyManager tm = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        String esn = tm.getDeviceId();// DeviceId(IMEI)

        if (TextUtils.isEmpty(esn)) {
            esn = "";
        }

        final String esnWithoutSpace = esn.replace(" ", "");
        return esnWithoutSpace;
    }

    public static boolean checkCDMASim(Context mActivity){
        TelephonyManager telManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        if(operator!=null){
           if(operator.equals("46003")){
               return true;
           }
        }
        return false;
    }
    /**
     * 获取手机的电话号码。
     */
    public static String getPhoneNumber(final Context mActivity) {
        TelephonyManager tm = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "";
        }

        final String phoneNumberWithoutSpace = phoneNumber.replace(" ", "");
        return phoneNumberWithoutSpace;
    }
    
    public static String getImsi2(final Context mActivity) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mActivity
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getSubscriberId();

            if (TextUtils.isEmpty(imei)) {
                return getIMEI(mActivity);
            }
            return imei;
        } catch (Exception e){
            return "";
        }
    }
    
    /**
     * // by liuzhiwen add 得到手机imei号
     * 
     * @param mActivity
     * @return
     */
    private static String getIMEI(final Context mActivity) {
        TelephonyManager tm = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
    
    /**
     * 获取android系统发布版本
     * @return
     */
    public static String getVersionRelease() {
        return Build.VERSION.RELEASE;
    }
}