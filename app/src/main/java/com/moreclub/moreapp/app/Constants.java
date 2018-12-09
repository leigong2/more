package com.moreclub.moreapp.app;


import com.moreclub.moreapp.BuildConfig;

/**
 * Created by Administrator on 2017/2/23.
 */

public interface Constants {

    /**
     * zune:测试服务器by 2017-05-12
     **/
//    public static final String BASE_URL = "http://10.150.7.245:8005";

    /**
     * zune:明哥测试服务器
     **/
//    public static final String BASE_URL = "http://10.150.7.248:8001";

    /**
     * zune:任忠测试服务器
     **/
//    public static final String BASE_URL = "http://10.150.7.247:8050";

    /**
     * zune:外网测试服务器
     **/
//    public static final String BASE_URL = "http://59.110.53.149:8088";

    /**
     * zune:正式服务器
     **/
    public static final String BASE_URL = "https://www.moreclub.cn";


    public static final String MORE_PHONE = "400 886 7918";

    /**
     * zune:app版本号
     **/
    public static final String APP_version = MainApp.getInstance().mVersion;

    public static class Config {
        public static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;
    }
}
