package com.moreclub.moreapp.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.MainApp;

/**
 * Created by Captain on 2017/3/7.
 */

public class MapLocation {
    static MapCallback mapListener;
    static MapLocationListener mapl;
    static LocationService locationService;

    public static void getLocation(Context context, MapCallback listener) {
        if (mapl == null) {
            mapl = new MapLocationListener();
        }

        locationService = MainApp.locationService;
        locationService.registerListener(mapl);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度定位定位模式
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setOpenGps(true);

        locationService.setLocationOption(option);
        Log.i("zunee:", "111111111");
        locationService.start();// 定位SDK
        mapListener = listener;
    }


    /**
     * 实现实位回调监听
     */
    public static class MapLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation location) {
            Log.i("zunee:", "222222222");
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());//获得当前时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());//获得erro code得知定位现状
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//获得纬度
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());//获得经度
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {//通过GPS定位
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());//获得速度
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获得当前地址
                sb.append(location.getDirection());//获得方位
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//通过网络连接定位
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获得当前地址
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());//获得经营商？
            }
            MoreUser.getInstance().setUserLocationLat(location.getLatitude());
            MoreUser.getInstance().setUserLocationLng(location.getLongitude());
            MoreUser.getInstance().setCity(location.getCity());
            MoreUser.getInstance().setLocationCity(location.getCity());
            Log.i("zune:", "定位");
            if (mapListener != null && !TextUtils.isEmpty(location.getCity())) {
                Log.i("zune:", "非空");
                mapListener.dbMapCallback(location.getLatitude(), location.getLongitude(), location.getCity(),location.getProvince(),location.getCountry());
            } else {
                if (mapListener != null) {
                    mapListener.dbMapCallbackFails();
                }
            }

            // 获得位置之后停止定位
            if (location != null && location.getCity() != null && location.getCity().length() > 0) {
                locationService.unregisterListener(mapl); //注销掉监听
                locationService.stop(); //停止定位服务
                mapl = null;
                Log.i("zunee:", "333333333");
            }

        }
    }

    public interface MapCallback {
        void dbMapCallback(double lat, double lng, String city, String province, String country);

        void dbMapCallbackFails();
    }
}
