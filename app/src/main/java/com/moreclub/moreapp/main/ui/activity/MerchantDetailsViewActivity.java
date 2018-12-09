package com.moreclub.moreapp.main.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.ui.view.scrollview.ObservableScrollViewCallbacks;
import com.moreclub.common.ui.view.scrollview.ScrollState;
import com.moreclub.common.ui.view.scrollview.ScrollUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.main.model.MerchantDetails;
import com.moreclub.moreapp.main.model.MerchantDetailsItem;
import com.moreclub.moreapp.main.model.MerchantPromo;
import com.moreclub.moreapp.main.model.MerchantSupportCoupon;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.SignPersion;
import com.moreclub.moreapp.main.model.UserSignParam;
import com.moreclub.moreapp.main.model.UserSignResult;
import com.moreclub.moreapp.main.presenter.IMerchantDetailsDataLoader;
import com.moreclub.moreapp.main.presenter.MerchantDetailsDataLoader;
import com.moreclub.moreapp.main.ui.adapter.MerchantCouponSupportAdater;
import com.moreclub.moreapp.main.ui.adapter.MerchantDetailsItemAdapter;
import com.moreclub.moreapp.main.ui.adapter.MerchantDetailsSignItemAdapter;
import com.moreclub.moreapp.main.ui.adapter.MerchantGridViewAdapter;
import com.moreclub.moreapp.main.ui.adapter.MerchantScenePagerAdapter;
import com.moreclub.moreapp.main.ui.adapter.UserCouponSupportAdater;
import com.moreclub.moreapp.main.ui.view.CustomGridView;
import com.moreclub.moreapp.packages.ui.activity.PackageSuperMainListActivity;
import com.moreclub.moreapp.packages.ui.activity.PayBillActivity;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserSecretActivity;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.GlideImageLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.ShareUtils;
import com.moreclub.moreapp.util.UserCollect;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Captain on 2017/2/28.
 */

public class MerchantDetailsViewActivity extends BaseListActivity implements
        IMerchantDetailsDataLoader.MerchantDetailsDataBinder,
        ObservableScrollViewCallbacks, OnItemClickListener, OnDismissListener {

    private final static String KEY_CITY_CODE = "city.code";

    private WebView webView;
    private Banner banner;
    private TextView merchantName;
    private TextView merchantSellPointer;
    private TextView merchantPrice;
    private TextView merchantAddress;
    private PopupWindow mPopupWindow;
    private View popupParentView;
    private ViewPagerFixed sceneViewPager;
    private PopupWindow mShareWindow;
    private View popupShareView;

    @BindView(R.id.callPhoneButton)
    Button callPhoneButton;
    @BindView(R.id.navButton)
    Button navButton;
    @BindView(R.id.ll_activities)
    LinearLayout llActivities;

    @BindView(R.id.wineRecyclerView)
    RecyclerView wineRecyclerView;
    @BindView(R.id.musicRecyclerView)
    RecyclerView musicRecyclerView;
    @BindView(R.id.environmentRecyclerView)
    RecyclerView environmentRecyclerView;
    @BindView(R.id.facilitiesGridView)
    CustomGridView facilitiesGridView;
    @BindView(R.id.signRecyclerView)
    RecyclerView signRecyclerView;

    @BindView(R.id.wineLayout)
    LinearLayout wineLayout;
    @BindView(R.id.musicLayout)
    LinearLayout musicLayout;
    @BindView(R.id.environmentLayout)
    LinearLayout environmentLayout;
    @BindView(R.id.facilitiesLayout)
    LinearLayout facilitiesLayout;

    @BindView(R.id.merchantAddressLay)
    LinearLayout merchantAddressLay;
    @BindView(R.id.peopleLay)
    LinearLayout peopleLayout;
    @BindView(R.id.areaLay)
    LinearLayout areaLayout;
    @BindView(R.id.roomLay)
    LinearLayout roomLayout;
    @BindView(R.id.timeLay)
    LinearLayout timeLayout;
    @BindView(R.id.payMethodLay)
    LinearLayout payMethodLayout;
    @BindView(R.id.sign_layout)
    LinearLayout signLayout;

    @BindView(R.id.peopleText)
    TextView peopleText;
    @BindView(R.id.areaText)
    TextView areaText;
    @BindView(R.id.roomText)
    TextView roomText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.payText)
    TextView payText;
    @BindView(R.id.tv_weight)
    TextView tvWeight;

    @BindView(R.id.detailsScrollView)
    ObservableScrollView detailsScrollView;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;

    @BindView(R.id.card_lay)
    LinearLayout cardLay;
    @BindView(R.id.support_card)
    TextView supportCard;
    @BindView(R.id.view_devide)
    View viewDevide;
    @BindView(R.id.orange_card)
    ImageView orangeCard;
    @BindView(R.id.black_card)
    ImageView blackCard;
    @BindView(R.id.orange_arraw)
    ImageView orangeArraw;
    @BindView(R.id.black_arraw)
    ImageView blackArraw;
    @BindView(R.id.collect_button)
    ImageView collectButton;
    @BindView(R.id.share_button)
    ImageView shareButton;
    @BindView(R.id.book_btton)
    TextView bookBtton;
    @BindView(R.id.sell_button)
    TextView sellButton;
    @BindView(R.id.package_button)
    TextView packageButton;
    @BindView(R.id.card_tag)
    TextView cardTag;
    @BindView(R.id.card_devide)
    View cardDevide;
    @BindView(R.id.mspaceCount)
    TextView mspaceCount;
    @BindView(R.id.activity_detail)
    TextView activity_title;
    @BindView(R.id.no_sign_tip)
    ImageView noSignTip;
    @BindView(R.id.gray_layout)
    View grayLayout;
    @BindView(R.id.coupon_lay)
    LinearLayout couponLay;
    @BindView(R.id.ll_bottom_layout)
    LinearLayout bottomLay;
    @BindView(R.id.coupon_des)
    TextView couponDes;


    @BindColor(R.color.black)
    int baseColor;
    @BindColor(R.color.white)
    int titleColor;

    private PopupWindow mSignWindow;
    private View popupSignView;

    private PopupWindow mCouponSupportWindow;
    private View couponSupportView;
    private RecyclerView supportList;

    private MerchantDetailsItemAdapter musicAdapter;
    private MerchantDetailsSignItemAdapter signAdapter;
    private MerchantCouponSupportAdater merchantCouponSupportAdater;
    private UserCouponSupportAdater userCouponSupportAdater;
    private int cardLevel;
    private String bookPhone;
    private String merchantPhone;
    private String merchantLat;
    private String merchantLng;
    private String merchantNameStr;
    private String merchantAddressStr;
    private TextView barTitle;
    private String mid;
    private int toolbarHeight;
    private boolean isCollected = false;
    private String merchantBookPhone[];
    private int userSignCount;
    private AlertView phoneAlertView;
    private ArrayList<SignPersion> signPersionArray;
    private static final int MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_MERCHANT_PHONE = 2;
    private ArrayList<MerchantActivity> mActivities;
    private ArrayList<UserCoupon> userSupportCouponList;
    private ArrayList<MerchantSupportCoupon> merchantSupportCouponList;
    private Integer pn = 0;
    private Integer ps = 10;
    private String logo;
    private String desc;

    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_details_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupView();

        loadData();
    }

    @Override
    protected void onReloadData() {
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void loadData() {

        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() > 0) {
            ((MerchantDetailsDataLoader) mPresenter).onLoadMerchantDetails(mid, "" + MoreUser.getInstance().getUid());
            if (mid != null)
                ((MerchantDetailsDataLoader) mPresenter).onLoadMerchantActivity(Integer.parseInt(mid), MoreUser.getInstance().getUid(), pn, ps);
        } else {
            ((MerchantDetailsDataLoader) mPresenter).onLoadMerchantDetails(mid, "0");
            ((MerchantDetailsDataLoader) mPresenter).onLoadMerchantActivity(Integer.parseInt(mid), 0L, pn, ps);
        }

        ((MerchantDetailsDataLoader) mPresenter).onLoadMerchantRichText(mid);
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() > 0) {
            ((MerchantDetailsDataLoader) mPresenter).loadUserSupportCoupons(""+MoreUser.getInstance().getUid(),mid);
        } else {
            ((MerchantDetailsDataLoader) mPresenter).onLoadMerchantCouponSupport(mid);
        }
    }

    private void setupView() {
        merchantSupportCouponList = new ArrayList<>();
        userSupportCouponList = new ArrayList<>();
        signPersionArray = new ArrayList<>();
        toolbarHeight = ScreenUtil.dp2px(this, 56);
        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");

        webView = (WebView) findViewById(R.id.merchantWebview);
        webView.setSaveEnabled(false);
        banner = (Banner) findViewById(R.id.image_banner);
        merchantName = (TextView) findViewById(R.id.merchantName);
        merchantSellPointer = (TextView) findViewById(R.id.merchantSellPointer);
        merchantPrice = (TextView) findViewById(R.id.merchantPrice);
        merchantAddress = (TextView) findViewById(R.id.merchantAddress);

        LinearLayoutManager wineManager = new LinearLayoutManager(this);
        wineManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager musicManager = new LinearLayoutManager(this);
        musicManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager environmentManager = new LinearLayoutManager(this);
        environmentManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager facilitiesManager = new LinearLayoutManager(this);
        facilitiesManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager signManager = new LinearLayoutManager(this);
        signManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        wineRecyclerView.setLayoutManager(wineManager);
        musicRecyclerView.setLayoutManager(musicManager);
        signRecyclerView.setLayoutManager(signManager);
        environmentRecyclerView.setLayoutManager(environmentManager);
        detailsScrollView.setScrollViewCallbacks(this);

        ImageView back = (ImageView) navLayout.findViewById(R.id.nav_back);
        back.setOnClickListener(backListener);

        callPhoneButton.setOnClickListener(callPhoneListener);
        navButton.setOnClickListener(navMapListener);
        navLayout.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, baseColor));
        barTitle = ButterKnife.findById(navLayout, R.id.activity_title);
        barTitle.setTextColor(ScrollUtils.getColorWithAlpha(0, baseColor));
        merchantAddressLay.setOnClickListener(navMapListener);

        collectButton.setOnClickListener(collectListener);
        shareButton.setOnClickListener(shareListener);
        bookBtton.setOnClickListener(bookListener);
        sellButton.setOnClickListener(sellListener);
        packageButton.setOnClickListener(packageListener);
        orangeCard.setOnClickListener(orangeListener);
        blackCard.setOnClickListener(blackListener);
        couponLay.setOnClickListener(couponListener);

        grayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCouponPopuWindow();
            }
        });
        signAdapter = new MerchantDetailsSignItemAdapter(this,
                R.layout.merchant_details_sign_item, signPersionArray);
        signRecyclerView.setAdapter(signAdapter);
        signAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
/*                SignPersion item = (SignPersion) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        MerchantDetailsViewActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent = new Intent(MerchantDetailsViewActivity.this, UserDetailsActivity.class);
                intent.putExtra("toUid", "" + item.getUid());
                ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());*/
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        MerchantDetailsViewActivity.this, R.anim.push_down_in, R.anim.fade_out);
                Intent intent = new Intent(MerchantDetailsViewActivity.this, SignInterActivity.class);
                intent.putExtra("mid", mid);
                intent.putExtra("name", merchantNameStr);
                intent.putExtra("logo", logo);
                intent.putExtra("desc", desc);
                ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void setupBanner(Banner banner, final List<String> items) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(items);
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        banner.isAutoPlay(true);
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }


    @Override
    protected Class getLogicClazz() {
        return IMerchantDetailsDataLoader.class;
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    private void backClick(){
        Intent intent = getIntent();
        boolean isFromAdert = intent.getBooleanExtra("isFromAdert",false);
        if (isFromAdert){
            AppUtils.pushLeftActivity(this,SuperMainActivity.class);
            finish();
        } else {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        if (musicAdapter != null) {
            musicAdapter.musicClose();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(detailsScrollView.getCurrentScrollY(), false, false);
    }


    /**
     * 图片预览popupWindow
     *
     * @param pics
     * @param clickpos
     */
    public void showAllSubject(ArrayList<MerchantDetailsItem> pics, int clickpos) {
        if (null == mPopupWindow) {
            popupParentView = LayoutInflater.from(this).inflate(
                    R.layout.merchant_details_scene_popuwindow, null);
            popupParentView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            int width = ScreenUtil.getScreenWidth(MerchantDetailsViewActivity.this);
            int height = ScreenUtil.getScreenHeight(MerchantDetailsViewActivity.this);

            //设置弹出部分和面积大小
            mPopupWindow = new PopupWindow(popupParentView, width, height, true);
            //设置动画弹出效果
            mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mPopupWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);

        }
        popupWindowSetupView(popupParentView, pics, clickpos);
        int[] pos = new int[2];

        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
    }

    void popupWindowSetupView(View view, ArrayList<MerchantDetailsItem> pics, int clickPos) {

        int showPos = 0;
        ArrayList<String> tempArray = new ArrayList<String>();
        final ArrayList<String> tipArray = new ArrayList<String>();
        for (int i = 0; i < pics.size(); i++) {
            MerchantDetailsItem item = pics.get(i);

            if (item != null) {
                ArrayList<String> subItemArray = item.getPictures();
                if (subItemArray != null && subItemArray.size() > 0) {
                    if (i == clickPos) {
                        showPos = tempArray.size();
                    }
                    tempArray.addAll(subItemArray);
                    for (int j = 0; j < subItemArray.size(); j++) {
                        tipArray.add(item.getName() + "" + (j + 1) + "/" + subItemArray.size());
                    }
                }
            }
        }

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        final TextView sceneName = (TextView) view.findViewById(R.id.sceneName);

        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(this, tempArray);

        sceneViewPager.setAdapter(adapter);

        adapter.setOnItemClickListener(new MerchantScenePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }
        });

        sceneViewPager.setCurrentItem(showPos);

        sceneViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                sceneName.setText("" + tipArray.get(position));
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 打电话
     */
    View.OnClickListener callPhoneListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (merchantPhone != null) {
                Context context = MerchantDetailsViewActivity.this;
                String str = "merchant_detail_phone_click";
                Map<String, String> map = new HashMap<>();
                map.put("mid", mid);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                if (ContextCompat.checkSelfPermission(MerchantDetailsViewActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MerchantDetailsViewActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_MERCHANT_PHONE);
                } else {
                    callPhone(merchantPhone);
                }
            }
        }
    };


    /**
     * 地图导航
     */
    View.OnClickListener navMapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (merchantLat != null) {
                Context context = MerchantDetailsViewActivity.this;
                String str = "merchant_detail_nav_click";
                Map<String, String> map = new HashMap<>();
                map.put("mid", mid);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                Intent intent = new Intent(MerchantDetailsViewActivity.this,
                        MerchantMapActivity.class);
                intent.putExtra("toLat", Double.valueOf(merchantLat));
                intent.putExtra("toLng", Double.valueOf(merchantLng));
                intent.putExtra("merchantName", merchantNameStr);
                intent.putExtra("merchantAddress", merchantAddressStr);
                MerchantDetailsViewActivity.this.startActivity(intent);
            }
        }
    };

    /**
     * 收藏事件
     */
    View.OnClickListener collectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = MerchantDetailsViewActivity.this;
            String str = "merchant_detail_collect_click";
            Map<String, String> map = new HashMap<>();
            map.put("mid", mid);
            map.put("uid", MoreUser.getInstance().getUid() + "");
            if (Build.VERSION.SDK_INT > 22) {
                int du = 2000000000;
                MobclickAgent.onEventValue(context, str, map, du);
            } else {
                MobclickAgent.onEvent(context, str, map);
            }
            UserCollect userCollect = new UserCollect(MerchantDetailsViewActivity.this,
                    mid, UserCollect.COLLECT_TYPE.MERCHANT_COLLECT.ordinal(),
                    isCollected, new UserCollect.CollectCallBack() {

                @Override
                public void collectSuccess() {
                    if (isCollected) {
                        isCollected = false;
                        collectButton.setImageResource(R.drawable.collect_fav);
                    } else {
                        isCollected = true;
                        collectButton.setImageResource(R.drawable.collect_fav_highlight);
                    }
                }

                @Override
                public void collectFails() {
                }
            });
        }
    };

    /**
     * 分享事件
     */
    View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = MerchantDetailsViewActivity.this;
            String str = "merchant_detail_share_click";
            Map<String, String> map = new HashMap<>();
            map.put("mid", mid);
            map.put("uid", MoreUser.getInstance().getUid() + "");
            if (Build.VERSION.SDK_INT > 22) {
                int du = 2000000000;
                MobclickAgent.onEventValue(context, str, map, du);
            } else {
                MobclickAgent.onEvent(context, str, map);
            }
            showShareView();
        }
    };

    /**
     * 买单事件
     */
    View.OnClickListener sellListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(MerchantDetailsViewActivity.this, UseLoginActivity.class);
            } else {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        MerchantDetailsViewActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(MerchantDetailsViewActivity.this, PayBillActivity.class);
                intent.putExtra("merchantID", mid);
                intent.putExtra("merchantSupportCardLevel", cardLevel);
                intent.putExtra("merchantName", merchantNameStr);
                ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());
            }
        }
    };

    /**
     * 订位事件
     */
    View.OnClickListener bookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (merchantBookPhone != null && merchantBookPhone.length > 1) {
                if (phoneAlertView == null) {
                    phoneAlertView = new AlertView("选择电话", null, "取消", null,
                            merchantBookPhone,
                            MerchantDetailsViewActivity.this, AlertView.Style.ActionSheet, MerchantDetailsViewActivity.this);
                }
                if (!phoneAlertView.isShowing() && !MerchantDetailsViewActivity.this.isFinishing())
                    phoneAlertView.show();
            } else if (merchantBookPhone != null && merchantBookPhone.length == 1) {
                bookPhone = merchantBookPhone[0];
                if (ContextCompat.checkSelfPermission(MerchantDetailsViewActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MerchantDetailsViewActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE);
                } else {
                    callPhone(bookPhone);
                }

            }
        }
    };

    @OnClick({R.id.sign_layout, R.id.sign_lay})
    void SignTagLayClick() {
        if (MoreUser.getInstance().getUid().equals(0L)) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    MerchantDetailsViewActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(MerchantDetailsViewActivity.this, UseLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());
        } else {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    this, R.anim.push_down_in, R.anim.fade_out);
            Intent intent = new Intent(this, SignInterActivity.class);
            intent.putExtra("mid", mid);
            intent.putExtra("name", merchantNameStr);
            intent.putExtra("logo", logo);
            intent.putExtra("desc", desc);
            ActivityCompat.startActivity(this, intent, compat.toBundle());
        }
    }

    @OnClick(R.id.ll_activities)
    public void onViewClicked() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                MerchantDetailsViewActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(MerchantDetailsViewActivity.this, MerchantActivitiesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("mid", Integer.parseInt(mid));
        intent.putExtras(bundle);
        ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());
    }

    /**
     * 分享popupWindow
     *
     * @param
     * @param
     */
    public void showShareView() {
        if (null == mShareWindow) {
            popupShareView = LayoutInflater.from(this).inflate(
                    R.layout.share_view, null);
            popupShareView.findViewById(R.id.view_weight).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mShareWindow != null && mShareWindow.isShowing()) {
                        mShareWindow.dismiss();
                    }
                }
            });
            LinearLayout weixinFriendLay = (LinearLayout) popupShareView.findViewById(R.id.wx_friend_lay);
            LinearLayout weixinGroupLay = (LinearLayout) popupShareView.findViewById(R.id.wx_chat_lay);
            LinearLayout weiboLay = (LinearLayout) popupShareView.findViewById(R.id.wb_lay);
            weixinFriendLay.setOnClickListener(weixinFriendShareListener);
            weixinGroupLay.setOnClickListener(weixinGroupShareListener);
            weiboLay.setOnClickListener(weiboShareListener);

            int width = ScreenUtil.getScreenWidth(MerchantDetailsViewActivity.this);

            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            popupShareView.measure(spec, spec);
            int height = popupShareView.getMeasuredHeight();

            //设置弹出部分和面积大小
            mShareWindow = new PopupWindow(popupShareView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            //设置动画弹出效果
            mShareWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mShareWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
            mShareWindow.setTouchable(true);
            mShareWindow.setFocusable(true);

        }
        int[] pos = new int[2];

        mShareWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], pos[1]);
    }

    View.OnClickListener weixinFriendShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(MerchantDetailsViewActivity.this, 1, 1, 3, mid);
            share.share();

        }
    };

    View.OnClickListener weixinGroupShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(MerchantDetailsViewActivity.this, 0, 1, 3, mid);
            share.share();
        }
    };

    View.OnClickListener weiboShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(MerchantDetailsViewActivity.this, 0, 2, 3, mid);
            share.share();
        }
    };

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == phoneAlertView && position >= 0) {
            bookPhone = merchantBookPhone[position];
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE);
            } else {
                callPhone(bookPhone);
            }
        }
    }

    private void callPhone(String ph) {
        if (ph == null || ph.length() == 0) {
            return;
        }
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + ph));
        startActivity(phoneIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(bookPhone);
            } else {
                Toast.makeText(this,
                        "您拒绝了该权限，可能导致你无法使用该功能",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_MERCHANT_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(merchantPhone);
            } else {
                Toast.makeText(this,
                        "您拒绝了该权限，可能导致你无法使用该功能",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 橙卡信息
     */
    View.OnClickListener orangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = MerchantDetailsViewActivity.this;
            String str = "merchant_detail_orange_card_click";
            Map<String, String> map = new HashMap<>();
            map.put("mid", mid);
            if (Build.VERSION.SDK_INT > 22) {
                int du = 2000000000;
                MobclickAgent.onEventValue(context, str, map, du);
            } else {
                MobclickAgent.onEvent(context, str, map);
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    MerchantDetailsViewActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(MerchantDetailsViewActivity.this, MerchantCardInfoActivity.class);
            intent.putExtra("mid", mid);
            intent.putExtra("cardLevel", 1);
            ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());

        }
    };

    /**
     * 黑卡信息
     */
    View.OnClickListener blackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = MerchantDetailsViewActivity.this;
            String str = "merchant_detail_black_card_click";
            Map<String, String> map = new HashMap<>();
            map.put("mid", mid);
            if (Build.VERSION.SDK_INT > 22) {
                int du = 2000000000;
                MobclickAgent.onEventValue(context, str, map, du);
            } else {
                MobclickAgent.onEvent(context, str, map);
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    MerchantDetailsViewActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(MerchantDetailsViewActivity.this, MerchantCardInfoActivity.class);
            intent.putExtra("mid", mid);
            intent.putExtra("cardLevel", 2);
            ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());
        }
    };

    /**
     * 礼券信息
     */
    View.OnClickListener couponListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            setupCouponSupportWindow();


        }
    };

    /**
     * 套餐事件
     */
    View.OnClickListener packageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    MerchantDetailsViewActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(MerchantDetailsViewActivity.this, PackageSuperMainListActivity.class);
            intent.putExtra("merchantID", mid);
            intent.putExtra("loadType", "merchant_package");
            ActivityCompat.startActivity(MerchantDetailsViewActivity.this, intent, compat.toBundle());
        }
    };

    /**
     * 返回事件
     */
    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backClick();
        }
    };

    /**
     * 网络请求返回
     *
     * @param responce
     */
    @Override
    public void onMerchantDetailsResponse(RespDto responce) {
        final MerchantDetails item = (MerchantDetails) responce.getData();
        if (item == null) {
            showNoData(false);
            return;
        }
        hideNoData();
        setupBanner(banner, item.getPictures());
        merchantName.setText(item.getName());
        logo = item.getLogo();
        desc = item.getSellingPoint();
        barTitle.setText(item.getName());
        merchantSellPointer.setText(item.getSellingPoint());
        StringBuilder moneyString = new StringBuilder();
        if (item.getReferencePrice() != null && item.getReferencePrice().length() > 0) {
            moneyString.append("人均");
            float rp = Float.valueOf(item.getReferencePrice());
            moneyString.append(Integer.valueOf((int) rp));
            moneyString.append("元");
            merchantPrice.setText(moneyString.toString());
        } else {
            merchantPrice.setText("");
        }

        merchantAddress.setText(item.getAddress());
        Logger.d("load MerchantDetails success -> " + responce.getData());

        merchantNameStr = item.getName();
        merchantAddressStr = item.getAddress();
        merchantPhone = item.getPhone();
        merchantLat = item.getLat();
        merchantLng = item.getLng();

        if (item.getDrinkList() != null && item.getDrinkList().size() > 0) {
            MerchantDetailsItemAdapter wineAdapter = new MerchantDetailsItemAdapter(this,
                    R.layout.merchant_child_tag_item, item.getDrinkList(), 1);
            wineRecyclerView.setAdapter(wineAdapter);
        } else {
            wineLayout.setVisibility(View.GONE);
        }


        String musicPlay = item.getMusicPlay();
        ArrayList<MerchantDetailsItem> musicArray = item.getMusicList();
        if (musicPlay != null && musicPlay.length() > 0) {
            MerchantDetailsItem musicItem = new MerchantDetailsItem();
            musicItem.setExistMusic(true);
            musicItem.setMusicPlayUrl(musicPlay);
            musicArray.add(0, musicItem);
        }

        if (musicArray != null && musicArray.size() > 0) {
            musicAdapter = new MerchantDetailsItemAdapter(this,
                    R.layout.merchant_child_tag_item, musicArray, 2);
            musicRecyclerView.setAdapter(musicAdapter);
        } else {
            musicLayout.setVisibility(View.GONE);
        }

        if (item.getScenePictureList() != null && item.getScenePictureList().size() > 0) {
            MerchantDetailsItemAdapter environmentAdapter = new MerchantDetailsItemAdapter(this,
                    R.layout.merchant_details_child_scene, item.getScenePictureList(), 3);
            environmentRecyclerView.setAdapter(environmentAdapter);
            environmentAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, final View view, Object o, final int position) {


//
//                final ViewGroup container = (ViewGroup) view.getParent().getParent();
//                container.addView(view);
//                container.getOverlay().add(view);
//
//                ObjectAnimator animator = ObjectAnimator.ofFloat(this,
//                        "scale", 0.0f, 0.5f).setDuration(1000);
//                animator.start();
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        float cVal = (Float) animation.getAnimatedValue();
//                        view.setAlpha(1.0f - 2.0f * cVal);
//                        view.setScaleX(1.0f + cVal);
//                        view.setScaleY(1.0f + cVal);
//                    }
//                });
//                animator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                        //container.getOverlay().remove(view);
//                        ArrayList<MerchantDetailsItem> array = item.getScenePictureList();
//                        showAllSubject(array, position);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//                    }
//                });

                    ArrayList<MerchantDetailsItem> array = item.getScenePictureList();
                    showAllSubject(array, position);
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view,
                                               Object o, int position) {
                    return false;
                }
            });
        } else {
            environmentLayout.setVisibility(View.GONE);
        }

        if (item.getFacilityList() != null && item.getFacilityList().size() > 0) {
            MerchantGridViewAdapter facilitiesAdapter = new MerchantGridViewAdapter(
                    this, item.getFacilityList());
            facilitiesGridView.setAdapter(facilitiesAdapter);
        } else {
            facilitiesLayout.setVisibility(View.GONE);
        }

        int peopleInt = item.getCapacity();
        if (peopleInt > 0) {
            peopleText.setText(peopleInt + "人");
        } else {
            peopleLayout.setVisibility(View.GONE);
        }

        int areaInt = item.getFloorSpace();
        if (areaInt > 0) {
            areaText.setText(areaInt + "㎡");
        } else {
            areaLayout.setVisibility(View.GONE);
        }

        int roomsInt = item.getRooms();
        if (roomsInt > 0) {
            roomText.setText(roomsInt + "间");
        } else {
            roomLayout.setVisibility(View.GONE);
        }

        String timeString = item.getOpenTime();
        if (timeString != null) {
            timeText.setText(timeString);
        } else {
            timeLayout.setVisibility(View.GONE);
        }

        String payMethodString = item.getPayMethods();
        payMethodString.replaceAll(",", "  ");
        if (payMethodString != null) {
            payText.setText(payMethodString);
        } else {
            payMethodLayout.setVisibility(View.GONE);
        }

        ArrayList<MerchantPromo> promoCard = item.getPromoList();
        if (promoCard != null && promoCard.size() > 0) {
            cardLay.setVisibility(View.VISIBLE);
            viewDevide.setVisibility(View.VISIBLE);
            String supportStr = "本店支持";
            for (int i = 0; i < promoCard.size(); i++) {
                MerchantPromo promoItem = promoCard.get(i);
                if (promoItem != null && promoItem.getCardType() == 1) {
                    supportStr += "橙卡";
                    orangeCard.setVisibility(View.VISIBLE);
                    orangeArraw.setVisibility(View.VISIBLE);
                    if (cardLevel != 2) {
                        cardLevel = 1;
                    }
                } else if (promoItem != null && promoItem.getCardType() == 2) {
                    supportStr += ",黑卡";
                    blackCard.setVisibility(View.VISIBLE);
                    blackArraw.setVisibility(View.VISIBLE);
                    cardLevel = 2;
                }
            }
            supportCard.setText(supportStr);
        } else {
            cardLay.setVisibility(View.GONE);
            viewDevide.setVisibility(View.GONE);
            cardTag.setVisibility(View.GONE);
            cardDevide.setVisibility(View.GONE);
        }

        if (item.getOwnPackage()) {
            packageButton.setEnabled(true);
            packageButton.setTextColor(ContextCompat.getColor(this, R.color.mainItemBarDesc));
        } else {
            packageButton.setEnabled(false);
            packageButton.setTextColor(ContextCompat.getColor(this, R.color.mainItemBarDesc));
            packageButton.setAlpha(0.4f);
        }
        if (item.getOnlineBill()) {
            sellButton.setEnabled(true);
            sellButton.setTextColor(ContextCompat.getColor(this, R.color.mainItemBarDesc));
        } else {
            sellButton.setEnabled(false);
            sellButton.setTextColor(ContextCompat.getColor(this, R.color.mainItemBarDesc));
            sellButton.setAlpha(0.4f);
        }

        if (item.getPhone() != null && item.getPhone().length() > 0) {
            bookBtton.setEnabled(true);
            bookBtton.setTextColor(ContextCompat.getColor(this, R.color.mainItemBarDesc));
            merchantBookPhone = item.getPhone().split(",");
        } else {
            bookBtton.setEnabled(false);
            bookBtton.setTextColor(ContextCompat.getColor(this, R.color.mainItemBarDesc));
            bookBtton.setAlpha(0.4f);
        }

        if (item.getCollected()) {
            isCollected = true;
            collectButton.setImageResource(R.drawable.collect_fav_highlight);
        } else {
            isCollected = false;
            collectButton.setImageResource(R.drawable.collect_fav);
        }

        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() > 0) {
            ((MerchantDetailsDataLoader) mPresenter).onLoadSignList(mid, "" + MoreUser.getInstance().getUid());
        } else {
            ((MerchantDetailsDataLoader) mPresenter).onLoadSignList(mid, "0");
        }
        detailsScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    public void onMerchantDetailsFailure(String msg) {
        showNoData(true);
        Logger.d("load MerchantDetails success -> " + msg);
    }

    @Override
    public void onMerchantDetailsRichTextFailure(String msg) {

    }

    @Override
    public void onMerchantDetailsRichTextResponse(RespDto responce) {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset=\"utf-8\"/>");
        html.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0," +
                "maximum-scale=1.0,user-scalable=no\"/>");
        html.append("<style> img { padding: 0px;max-width:100%;}.box3{overflow-x:hidden}" +
                "p{color: #333333;}</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='box3' style=\"width: 100%;padding:0px 6px 10px 6px;" +
                "box-sizing: border-box;\">");
        html.append((String) responce.getData());
        html.append("</div>");
        html.append("</body>");
        html.append("<script type=\"text/javascript\"> window.onload = function imgcenter() " +
                "{ var box = document.getElementsByClassName(\"box3\"); " +
                "var img = box[0].getElementsByTagName(\"img\"); " +
                "for(i = 0; i < img.length; i++) { " +
                "var imgParentNodes = img[i].parentNode;console.log(imgParentNodes) " +
                "imgParentNodes.style.textIndent = 'inherit';}}</script>");
        html.append("</html>");
        webView.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        detailsScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float alpha = Math.min(1, (float) scrollY / (banner.getHeight() - toolbarHeight));
        //banner.setTranslationY((scrollY - toolbarHeight) / 2);
        navLayout.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        barTitle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, titleColor));
        bottomLay.setAlpha(alpha);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @OnClick(R.id.signButton)
    public void signButtonClick() {
        if (MoreUser.getInstance().getUid() == 0) {
            AppUtils.pushLeftActivity(MerchantDetailsViewActivity.this, UseLoginActivity.class);
        } else {
            UserSignParam param = new UserSignParam();
            param.setUid("" + MoreUser.getInstance().getUid());
            param.setCity(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"));
            param.setMid(mid);
            param.setUserName(MoreUser.getInstance().getNickname());
            param.setMerchantName(merchantNameStr);
            param.setUserLat("" + MoreUser.getInstance().getUserLocationLat());
            param.setUserLng("" + MoreUser.getInstance().getUserLocationLng());
            ((MerchantDetailsDataLoader) mPresenter).userSign(param);
        }
    }

    @Override
    public void onSignFailure(String msg) {

    }

    @Override
    public void onSecretSignFailure(String msg) {

    }

    @Override
    public void onMerchantActivityFailure(String message) {

    }

    /**
     * 礼券支持
     *
     * @param msg
     */
    @Override
    public void onMerchantCouponSupportFailure(String msg) {
        couponLay.setVisibility(View.GONE);
    }



    @Override
    public void onMerchantCouponSupportResponse(RespDto response) {
        ArrayList<MerchantSupportCoupon> result = (ArrayList<MerchantSupportCoupon>) response.getData();
        if (result != null && result.size() > 0) {
            merchantSupportCouponList.addAll(result);
            couponLay.setVisibility(View.VISIBLE);
            couponDes.setText("More礼券");
            new MerchantCouponCountTask(result).execute();
        } else {
            couponLay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserSupportCouponsFailure(String msg) {
        couponLay.setVisibility(View.GONE);
    }

    @Override
    public void onUserSupportCouponsResponse(RespDto response) {
        ArrayList<UserCoupon> result = (ArrayList<UserCoupon>) response.getData();
        if (result != null && result.size() > 0) {
            userSupportCouponList.addAll(result);
            couponLay.setVisibility(View.VISIBLE);
            couponDes.setText("More礼券");
            new CouponCountTask(result).execute();
        } else {
            couponLay.setVisibility(View.GONE);
        }
    }


    final class CouponCountTask extends AsyncTask<Void, Void, String> {

        ArrayList<UserCoupon> array;

        public CouponCountTask(ArrayList<UserCoupon> data) {
            array = data;
        }

        @Override
        protected String doInBackground(Void... params) {

            Set set = new HashSet();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < array.size(); i++) {
                UserCoupon item = array.get(i);
                if (set.add(item.getModalName())) {
                    if (i == 0) {
                        result.append(item.getModalName());
                    } else {
                        result.append("," + item.getModalName());
                    }
                }
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            couponDes.setText(result);
        }
    }

    final class MerchantCouponCountTask extends AsyncTask<Void, Void, String> {

        ArrayList<MerchantSupportCoupon> array;

        public MerchantCouponCountTask(ArrayList<MerchantSupportCoupon> data) {
            array = data;
        }

        @Override
        protected String doInBackground(Void... params) {

            Set set = new HashSet();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < array.size(); i++) {
                MerchantSupportCoupon item = array.get(i);
                if (set.add(item.getModalName())) {
                    if (i == 0) {
                        result.append(item.getModalName());
                    } else {
                        result.append("," + item.getModalName());
                    }
                }
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            couponDes.setText(result);
        }
    }

    /**
     * 活动返回
     *
     * @param body
     */
    @Override
    public void onMerchantActivityResponse(RespDto body) {
        if (body != null && body.getData() != null && body.isSuccess()) {
            mActivities = (ArrayList<MerchantActivity>) body.getData();
            if (mActivities.size() > 0 && mActivities.get(0) != null) {
                cardDevide.setVisibility(View.VISIBLE);
                llActivities.setVisibility(View.VISIBLE);
                tvWeight.setVisibility(View.VISIBLE);
                String title = mActivities.get(0).getTitle();
                activity_title.setText(title);
            } else {
                llActivities.setVisibility(View.GONE);
                tvWeight.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSecretSignResponse(RespDto response) {
        setupSignPopuWindow();
    }

    @Override
    public void onSignResponse(RespDto response) {

        UserSignResult result = (UserSignResult) response.getData();

        if (result.isSignin()) {
            Toast.makeText(MerchantDetailsViewActivity.this, getString(R.string.user_sign_sucess), Toast.LENGTH_SHORT).show();
            if (signPersionArray.size() == 0) {
                noSignTip.setVisibility(View.GONE);

                mspaceCount.setText("M-sapce (" + (userSignCount + 1) + ")");
                SignPersion item = new SignPersion();
                item.setUid(MoreUser.getInstance().getUid());
                item.setUserName(MoreUser.getInstance().getNickname());
                item.setUserAvatar(MoreUser.getInstance().getThumb());
                item.setSigninTime(System.currentTimeMillis());
                signPersionArray.add(item);
                signAdapter.notifyDataSetChanged();
            } else {
                SignPersion first = signPersionArray.get(0);
                if (first.getUid() != MoreUser.getInstance().getUid()) {
                    mspaceCount.setText("M-sapce (" + (userSignCount + 1) + ")");
                    SignPersion item = new SignPersion();
                    item.setUid(MoreUser.getInstance().getUid());
                    item.setUserName(MoreUser.getInstance().getNickname());
                    item.setUserAvatar(MoreUser.getInstance().getThumb());
                    item.setSigninTime(System.currentTimeMillis());
                    signPersionArray.add(0, item);
                    for (int i = 0; i < signPersionArray.size(); i++) {
                        SignPersion signPersion = signPersionArray.get(i);
                        if (i > 0 && signPersion.getUid() == item.getUid()) {
                            signPersionArray.remove(signPersion);
                            userSignCount--;
                        }
                    }
                    if (signPersionArray.size() > 6) {
                        signPersionArray.remove(6);
                    }
                    signAdapter.notifyDataSetChanged();
                }
            }
            if (!MoreUser.getInstance().isManualSignSuccess()) {
                MoreUser.getInstance().setManualSignSuccess(true);

                AutoSigninSettings param = new AutoSigninSettings();
                param.setUid("" + MoreUser.getInstance().getUid());
                param.setAutoSignin(true);
                ((MerchantDetailsDataLoader) mPresenter).onSecretSign("" + MoreUser.getInstance().getUid(), param);
                noSignTip.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(MerchantDetailsViewActivity.this, getString(R.string.user_sign_fails), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 签到列表
     *
     * @param response
     */
    @Override
    public void onSignListResponse(RespDto response) {
        SignList result = (SignList) response.getData();

        if (result.getUsers() == null || result.getUsers().size() == 0) {
            signLayout.setVisibility(View.VISIBLE);
            mspaceCount.setText("M-sapce (0)");
            noSignTip.setVisibility(View.VISIBLE);
            return;
        } else {
            noSignTip.setVisibility(View.GONE);
            signLayout.setVisibility(View.VISIBLE);
            userSignCount = result.getUserCount();
            for (int i = 0; i < result.getUsers().size(); i++) {
                if (result.getUsers().get(i) != null)
                    signPersionArray.add(result.getUsers().get(i));
                if (signPersionArray.size() >= 6)
                    break;
            }
            mspaceCount.setText("M-sapce (" + result.getUserCount() + ")");
            signAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSignListFailure(String msg) {
        signLayout.setVisibility(View.VISIBLE);
        mspaceCount.setText("M-sapce (0)");
        noSignTip.setVisibility(View.VISIBLE);
    }

    private void setupSignPopuWindow() {

        if (null == mSignWindow) {
            popupSignView = LayoutInflater.from(this).inflate(
                    R.layout.merchant_signin_sucess, null);

            LinearLayout signSettingLayout = (LinearLayout) popupSignView.findViewById(R.id.aotuSignSettingLay);
            Button know = (Button) popupSignView.findViewById(R.id.know);
            signSettingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.pushLeftActivity(MerchantDetailsViewActivity.this, UserSecretActivity.class);
                }
            });

            know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSignWindow.dismiss();
                    mSignWindow = null;
                }
            });
            int width = ScreenUtil.getScreenWidth(MerchantDetailsViewActivity.this);
            int height = ScreenUtil.getScreenHeight(MerchantDetailsViewActivity.this);

            //设置弹出部分和面积大小
            mSignWindow = new PopupWindow(popupSignView, width, height - 56, true);
            //设置动画弹出效果
            mSignWindow.setAnimationStyle(R.style.PopupAnimation);

//            mPopupWindow.setClippingEnabled(true);
            mSignWindow.setFocusable(true);
            mSignWindow.setTouchable(true);
            popupSignView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSignWindow.dismiss();
                    mSignWindow = null;
                }
            });
        }
        int[] pos = new int[2];

        mSignWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], 56);
    }

    private void setupCouponSupportWindow() {

        int width = ScreenUtil.getScreenWidth(MerchantDetailsViewActivity.this);
        int height = ScreenUtil.getScreenHeight(MerchantDetailsViewActivity.this);
        if (null == mCouponSupportWindow) {
            couponSupportView = LayoutInflater.from(this).inflate(
                    R.layout.merchant_support_coupon_list, null);

            ImageButton closeView = (ImageButton) couponSupportView.findViewById(R.id.close);
            supportList = (RecyclerView) couponSupportView.findViewById(R.id.support_list);
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeCouponPopuWindow();
                }
            });
            TextView dialogTitle = (TextView) couponSupportView.findViewById(R.id.dialog_title);
            dialogTitle.setText("当前商家支持礼券");
            TextView noUse = (TextView) couponSupportView.findViewById(R.id.no_use_coupon);
            noUse.setVisibility(View.GONE);
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            supportList.setLayoutManager(layoutManager);
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() > 0) {
                userCouponSupportAdater = new UserCouponSupportAdater(MerchantDetailsViewActivity.this,
                        R.layout.merchant_coupon_support_item, userSupportCouponList);
                supportList.setAdapter(userCouponSupportAdater);
            } else {
                merchantCouponSupportAdater = new MerchantCouponSupportAdater(MerchantDetailsViewActivity.this,
                        R.layout.merchant_coupon_support_item, merchantSupportCouponList);
                supportList.setAdapter(merchantCouponSupportAdater);
            }
            supportList.setFocusable(true);
            //设置弹出部分和面积大小
            int heightt = height - ScreenUtil.dp2px(this, 230);
            mCouponSupportWindow = new PopupWindow(couponSupportView, width, heightt, true);
            //设置动画弹出效果
            mCouponSupportWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mCouponSupportWindow.setBackgroundDrawable(dw);
            mCouponSupportWindow.setFocusable(false);
            mCouponSupportWindow.setTouchable(true);
            grayLayout.setVisibility(View.VISIBLE);
        }
        int[] pos = new int[2];

        mCouponSupportWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], 0);

    }

    private void closeCouponPopuWindow() {
        if (mCouponSupportWindow != null && mCouponSupportWindow.isShowing()) {
            mCouponSupportWindow.dismiss();
            mCouponSupportWindow = null;
        }
        grayLayout.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
