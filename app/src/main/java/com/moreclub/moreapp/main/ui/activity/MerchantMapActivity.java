package com.moreclub.moreapp.main.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.common.util.GPSUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.ui.adapter.RouteLineAdapter;
import com.moreclub.moreapp.util.DrivingRouteOverlay;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.OverlayManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/7.
 */

public class MerchantMapActivity extends BaseActivity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {



    @BindView(R.id.map_view)
    TextureMapView mMapView;

    @BindView(R.id.mapNavLayout)
    RelativeLayout mapNavLayout;

    @BindView(R.id.mapNavButton)
    Button mapNavButton;

    @BindView(R.id.merchantNameTV)
    TextView merchantNameTV;

    @BindView(R.id.merchantAddressTV)
    TextView merchantAddressTV;

    BaiduMap mBaidumap;

    // 搜索相关
    RoutePlanSearch mSearch;

    // 节点索引,供浏览节点时使用
    DrivingRouteResult nowResultdrive;
    RouteLine route;
    OverlayManager routeOverlay;

    double fromLat;
    double fromLng;
    double toLat;
    double toLng;

    String merchantName;
    String merchantAddress;
    boolean isFirstLoc = true; // 是否首次定位
    int nodeIndex = -1;
    boolean useDefaultIcon = false;
    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_map;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);

        initData();
        setupView();
    }


    private void initData(){

        Intent intent = getIntent();
        fromLat = MoreUser.getInstance().getUserLocationLat();
        fromLng =MoreUser.getInstance().getUserLocationLng();
        toLat = intent.getDoubleExtra("toLat",0);
        toLng = intent.getDoubleExtra("toLng",0);

        merchantName = intent.getStringExtra("merchantName");
        merchantAddress = intent.getStringExtra("merchantAddress");
    }


    private void setupView() {

        ImageButton backButton = (ImageButton) findViewById(R.id.nav_back);
        TextView titleView = (TextView) findViewById(R.id.activity_title);
        titleView.setText("商家位置");
        backButton.setOnClickListener(backLinstener);

        merchantNameTV.setText(merchantName);
        merchantAddressTV.setText(merchantAddress);

        mBaidumap = mMapView.getMap();
        mBaidumap.clear();
        routeOverlay= new DrivingRouteOverlay(mBaidumap);
        mBaidumap.setOnMarkerClickListener(routeOverlay);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        mSearch.drivingSearch((new DrivingRoutePlanOption()).
                from(PlanNode.withLocation(new LatLng(fromLat,fromLng))).
                to(PlanNode.withLocation(new LatLng(toLat,toLng))));

        mBaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL); //设置为普通模式的地图
        // 开启定位图层
        mBaidumap.setMyLocationEnabled(true);
        mBaidumap.setMyLocationData(new MyLocationData.Builder().latitude(fromLat).longitude(fromLng).build());
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(fromLat,
                    fromLng);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

            if(isInstallByread("com.baidu.BaiduMap")){
                mapNavLayout.setVisibility(View.VISIBLE);
            } else if (isInstallByread("com.autonavi.minimap")){
                mapNavLayout.setVisibility(View.VISIBLE);
            } else if (isInstallByread("com.google.android.apps.maps")){
                mapNavLayout.setVisibility(View.VISIBLE);
            } else {
                mapNavLayout.setVisibility(View.GONE);
            }

        if (mapNavLayout.getVisibility() == View.VISIBLE){
            mapNavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isInstallByread("com.baidu.BaiduMap")){
                        try {
                            String url = "intent://map/marker?location="+toLat+"," +
                                    toLng+"&title=我的位置&content="+merchantAddress +
                                    "&src=more精选酒吧|more精选酒吧#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                            Intent intent = Intent.parseUri(url,Intent.FLAG_ACTIVITY_NEW_TASK);
                            MerchantMapActivity.this.startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else if (isInstallByread("com.autonavi.minimap")){

                        double tolatlng[] =  GPSUtil.bd09_To_Gcj02(toLat,toLng);
                        try {
                            if (tolatlng.length==2) {
                                String url = "androidamap://viewMap?sourceApplication=more精选酒吧&poiname=" + merchantAddress + "&lat=" + tolatlng[0] + "&lon=" + tolatlng[1] + "&dev=0";
                                Intent intent = Intent.parseUri(url, Intent.FLAG_ACTIVITY_NEW_TASK);
                                MerchantMapActivity.this.startActivity(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else if (isInstallByread("com.google.android.apps.maps")){
                        double tolatlng[] =  GPSUtil.bd09_To_Gcj02(toLat,toLng);
                        if (tolatlng.length==2){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="+tolatlng[0]+","+tolatlng[1]));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            MerchantMapActivity.this.startActivity(i);
                        }
                    }
                }
            });
        }
    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    View.OnClickListener backLinstener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            MerchantMapActivity.this.finish();
        }
    };

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MerchantMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;


            if (result.getRouteLines().size() >=1 ) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                return;
            }
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private  RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List< ? extends RouteLine> transitRouteLines,  RouteLineAdapter.Type
                type) {
            this( context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new  RouteLineAdapter( context, mtransitRouteLines , type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick( position);

                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster( OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
