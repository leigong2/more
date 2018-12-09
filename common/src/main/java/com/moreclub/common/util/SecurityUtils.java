package com.moreclub.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Captain on 2017/3/15.
 */

public class SecurityUtils {

    public static String createSign(HashMap<String, String> map, String priKey) {

        String data = null;
        data = gennerateSigInput(map, priKey);

        if (data != null) {
            return encript_SHA1(data);

        } else {
            return null;
        }
    }


    private static String gennerateSigInput(HashMap<String, String> map, String priKey) {

        final ArrayList<String> list = new ArrayList<String>();

        for (HashMap.Entry<String, String> m : map.entrySet()) {
            list.add(m.getKey());
        }

        final Object[] arraies = list.toArray();
        Arrays.sort(arraies);

        final StringBuilder buffer = new StringBuilder();
        for (Object array : arraies) {
            buffer.append(array).append(map.get(array));
        }

        String sigString = buffer.toString();
        try {
            sigString = URLEncoder.encode(sigString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sigString+priKey;
    }

    private static String encript_SHA1(String data) {
        String out = "";
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(data.getBytes());
            byte[] sha1 = sha.digest();
            out = byte2hex(sha1);
        } catch (Exception e) {
        }
        return out;
    }

    private static String byte2hex(byte[] b) {
//        final StringBuilder hs = new StringBuilder();
//        String tmpString = "";
//
//        for (int n = 0; n < b.length; n++) {
//            tmpString = (java.lang.Integer.toHexString(b[n] & 0XFF));
//            if (tmpString.length() == 1) {
//                hs.append("0").append(tmpString);
//            } else {
//                hs.append(tmpString);
//            }
//        }
//        return hs.toString();
        // modified by Captain
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static boolean isEmail(String email){
        String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isMobileNO(String mobiles){
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
        if (mobiles==null||mobiles.length()==0)
            return false;
        if (mobiles.length()<11)
            return false;
        return true;
    }

    public static boolean isQQ(String str){
        boolean flag=false;
        String reg="^[1-9][0-9]{4,9}$";
        Pattern pattern = Pattern.compile(reg);
        flag=pattern.matcher(str).matches();
        return flag;
    }
    private static final Pattern REG_UNICODE = Pattern.compile("[0-9A-Fa-f]{4}");
    public static String unicode2String(String str) {
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c1 = str.charAt(i);
            if (c1 == '\\' && i < len - 1) {
                char c2 = str.charAt(++i);
                if (c2 == 'u' && i <= len - 5) {
                    String tmp = str.substring(i + 1, i + 5);
                    Matcher matcher = REG_UNICODE.matcher(tmp);
                    if (matcher.find()) {
                        sb.append((char)Integer.parseInt(tmp, 16));
                        i = i + 4;
                    } else {
                        sb.append(c1).append(c2);
                    }
                } else {
                    sb.append(c1).append(c2);
                }
            } else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }
}
