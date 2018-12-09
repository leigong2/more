package com.moreclub.moreapp.main.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.ui.activity.ActivitiesActivity;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.Comments;
import com.moreclub.moreapp.main.model.HeadlineCount;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantCount;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.Package;
import com.moreclub.moreapp.main.model.PackageResp;
import com.moreclub.moreapp.main.model.SignInterUser;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.main.model.UserLikesItem;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.presenter.IMainDataLoader;
import com.moreclub.moreapp.main.presenter.MainDataLoader;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.main.ui.activity.MChannelListActivity;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterHotalActivity;
import com.moreclub.moreapp.main.ui.adapter.MainHeaderPeopleAdapter;
import com.moreclub.moreapp.main.ui.adapter.MainMultiTypeAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.PlayMoreActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.GlideImageLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-09-08-0008.
 */

public class MainChannelViewV3 extends BaseTabView implements IMainDataLoader.LoadMainDataBinder{

    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private boolean isMoreChannel = true;
    private final static String PKG_BACKGROUND = "http://more-image.oss-cn-beijing.aliyuncs.com/package/main_pkg_bg.png";

    private final static String KEY_CITY_ID = "city.id";
    private final static String KEY_CITY_CODE = "city.code";
    private final static String KEY_CITY_NAME = "city.name";

    private final static int FROM_SELECT_CITY = 1;

    private ArrayList<Object> dataList;
    private Context mContext;
    private XRecyclerView xRecyclerView;
    private ImageButton nav_back;
    private Banner banner;
    private CycleGalleryViewPager bannerImage;
    private FrameLayout bannerLayout;
    private TextView tvMoreBars;
    private View header;
    //    private View footer;
    private CircleIndicator circleIndicator;
    private MainMultiTypeAdapter adapter;
    private VerticalViewPager verticalViewPager;
    private ArrayList<View> peopleViewParent;
    private ArrayList<BizArea> areas;
    private ArrayList<TagDict> dicts;
    private ArrayList<MerchantItem> merchants;
    private ArrayList<Package> packages;
    private ArrayList<MainChannelItem> channelList;
    private ArrayList<SignInterUser> signInterUserList;
    private int cityId;
    private String cityCode;
    private String cityName;
    private boolean mIsRefreshing = false;
    private ImageView bannelHeadBg;
    private int bannelHeadHeight = 0;
    private int bannelH = 0;
    private ArrayList<HeadlineCount> headlineCounts;
    private ArrayList<MerchantCount> merchantCounts;
    private List<ActivityData.ActivityItem> activities;
    private List<BannerItem> bannerList;
    private int chatPeopleCurrentItem = 0;
    private int bannerCurrentItem = 0;
    private LinearLayout mSpace;
    private LinearLayout chat_people_layout;
    private View.OnClickListener mSpaceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Todo 进入全城签到广场
            AppUtils.pushLeftActivity(mContext, SignInterHotalActivity.class);
        }
    };
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            finish();
        }
    };

    public MainChannelViewV3(Context cxt) {
        mContext = cxt;
        dataList = new ArrayList<>();
    }

    @Override
    public View createView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mchannel_v3_list,
                null, false);
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.main_rv);
        nav_back = (ImageButton) view.findViewById(R.id.nav_back);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        header = LayoutInflater.from(mContext).inflate(R.layout.main_header,
                null, false);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.addHeaderView(header);
        nav_back.setOnClickListener(backListener);
        xRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (isMoreChannel) {
                    page++;
                    ((MainDataLoader) mPresenter).onLoadMainChannelList(MoreUser.getInstance().getUid(), cityId, page, PAGE_SIZE);
                } else {
                    xRecyclerView.loadMoreComplete();
                }
            }
        });

        xRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
//                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
//                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.getCurrentJcvdOnFirtFloor();
//                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
//                        JCVideoPlayer.releaseAllVideos();
//                    }
//                }
            }
        });

        banner = ButterKnife.findById(header, R.id.image_banner);
        bannerImage = ButterKnife.findById(header, R.id.banner_image);
        bannerLayout = ButterKnife.findById(header, R.id.banner_layout);
        circleIndicator = ButterKnife.findById(header, R.id.circle_indicator);
        adapter = new MainMultiTypeAdapter(mContext, areas, dicts, merchants, packages, headlineCounts, merchantCounts, activities, channelList);
        xRecyclerView.setAdapter(adapter);
        verticalViewPager = ButterKnife.findById(header, R.id.chat_people);
        bannelHeadBg = ButterKnife.findById(header, R.id.head_bg);
        bannelHeadHeight = ScreenUtil.dp2px(mContext, 295);
        mSpace = ButterKnife.findById(header, R.id.mspace_img);
        mSpace.setOnClickListener(mSpaceClick);
        chat_people_layout = ButterKnife.findById(header, R.id.chat_people_layout);
        chat_people_layout.setOnClickListener(mSpaceClick);
        LinearLayout mchannelImg = ButterKnife.findById(header, R.id.mchannel_img);
        mchannelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.pushLeftActivity(mContext, MChannelListActivity.class);
            }
        });

        LinearLayout mlivepartyImg = ButterKnife.findById(header, R.id.mliveparty_img);
        mlivepartyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.pushLeftActivity(mContext, ActivitiesActivity.class);
            }
        });

        int sw = ScreenUtil.getScreenWidth(mContext);
        int ssw = (int) (sw * 0.8);
        bannelH = (int) (((float) ScreenUtil.dp2px(mContext, 169) / ScreenUtil.dp2px(mContext, 288)) * ssw);
        if (bannelH == 0) {
            bannelH = ScreenUtil.dp2px(mContext, 170);
        } else {
            int scaleH = bannelH - ScreenUtil.dp2px(mContext, 170);
            bannelHeadHeight = bannelHeadHeight + scaleH;
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(sw, bannelH);
            bannerLayout.setLayoutParams(param);

            RelativeLayout.LayoutParams paramb = new RelativeLayout.LayoutParams(sw, bannelHeadHeight);
            bannelHeadBg.setLayoutParams(paramb);
        }

    }

    @Override
    public void loadData() {

    }


    private void reSetupViews() {
        bannerLayout.setVisibility(View.VISIBLE);
        int sw = ScreenUtil.getScreenWidth(mContext);
        int ssw = (int) (sw * 0.8);
        bannelH = (int) (((float) ScreenUtil.dp2px(mContext, 169) / ScreenUtil.dp2px(mContext, 288)) * ssw);
        if (bannelH == 0) {
            bannelH = ScreenUtil.dp2px(mContext, 170);
        } else {
            int scaleH = bannelH - ScreenUtil.dp2px(mContext, 170);
            bannelHeadHeight = ScreenUtil.dp2px(mContext, 295);
            bannelHeadHeight = bannelHeadHeight + scaleH;
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(sw, bannelH);
            bannerLayout.setLayoutParams(param);
            RelativeLayout.LayoutParams paramb = new RelativeLayout.LayoutParams(sw, bannelHeadHeight);
            bannelHeadBg.setLayoutParams(paramb);
        }
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            xRecyclerView.refreshComplete();
        } else
            xRecyclerView.loadMoreComplete();
    }

    private void setupBanner(Banner banner, final List<BannerItem> items) {
        if (items != null && items.size() == 1) {
            dispatchBannerViewPager(banner, items);
        } else {
            dispatchGrallyViewPager(banner, items);
        }
    }

    private void dispatchBannerViewPager(Banner banner, final List<BannerItem> items) {
        banner.setVisibility(View.VISIBLE);
        bannerImage.setVisibility(View.GONE);
        final ArrayList<String> images = new ArrayList<>();

        for (BannerItem item : items) {
            images.add(item.getBannerPhoto());
        }

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        //banner.setImages(images);
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        banner.isAutoPlay(true);
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerItem item = items.get(position);
                bannerClick(item);
            }
        });
        banner.update(images);
    }

    private void dispatchGrallyViewPager(Banner banner, final List<BannerItem> items) {
        banner.setVisibility(View.GONE);
        bannerImage.setVisibility(View.VISIBLE);
        bannerImage.setAlpha(0f);
        PagerAdapter bAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                View view = mInflater.inflate(R.layout.gallery_item, container, false);
                final BannerItem item = items.get(position);
                final FrameLayout imageViewBg = (FrameLayout) view.findViewById(R.id.imageViewBg);
                imageViewBg.setAlpha(0f);
                Glide.with(mContext)
                        .load(item.getBannerPhoto())
                        .dontAnimate()
                        .placeholder(R.drawable.default_more)
                        .into(new GlideDrawableImageViewTarget(((ImageView) view.findViewById(R.id.imageview))){
                            @Override
                            public void onResourceReady(GlideDrawable resource
                                    , GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                bannerImage.setAlpha(1f);
                                imageViewBg.setAlpha(1f);
                            }
                        });
                container.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerClick(item);
                    }
                });
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public float getPageWidth(int position) {
                return 0.8f;//建议值为0.6~1.0之间
            }
        };
        bannerImage.setAdapter(bAdapter);
        bannerImage.setNarrowFactor(0.9f);
        circleIndicator.setCycleGalleryViewPager(bannerImage);
        startBanner();
    }

    private void bannerClick(BannerItem item) {
        Integer type = item.getClickType();
        if (type < 0)
            type = item.getRelationType();
        /**zune:type跳转类型，-1，0资讯，1活动，2跳内部浏览器，3跳外部浏览器, 4酒吧详情**/
        switch (type) {
            case -1:
            case 0:
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent1 = new Intent(mContext, MChannelDetailsAcitivy.class);
                Long relationId = item.getRelationId();
                int send = Integer.parseInt(relationId + "");
                intent1.putExtra("sid", send);
                ActivityCompat.startActivity(mContext, intent1, compat1.toBundle());
                break;
            case 1:
                if (item != null) {
                    Intent intent2 = new Intent(mContext, LiveActivity.class);
                    intent2.putExtra(Constants.KEY_ACT_ID, Integer.parseInt(item.getRelationId() + ""));
                    ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    ActivityCompat.startActivity(mContext, intent2, compat2.toBundle());
                }
                break;
            case 2:
                if (item != null) {
                    if (item.getRelationId() != null && !item.getRelationId().equals(0L)) {
                        Intent intent2 = new Intent(mContext, LiveActivity.class);
                        intent2.putExtra(Constants.KEY_ACT_ID, Integer.parseInt(item.getRelationId() + ""));
                        ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                                mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                        ActivityCompat.startActivity(mContext, intent2, compat2.toBundle());
                    } else if (!TextUtils.isEmpty(item.getClickLink())){
                        Intent intent2 = new Intent(mContext, PlayMoreActivity.class);
                        intent2.putExtra("webUrl", item.getClickLink());
                        intent2.putExtra("webTitle", item.getTitle());
                        ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                                mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                        ActivityCompat.startActivity(mContext, intent2, compat2.toBundle());
                    }
                }
                break;
            case 3:
                if (item != null && !TextUtils.isEmpty(item.getClickLink())) {
                    String webUrl = item.getClickLink();
                    String url = webUrl; // web address
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    mContext.startActivity(intent);
                }
                break;
            case 4:
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
                intent.putExtra("mid",""+ item.getRelationId());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                break;
            default:
                if (item != null && !TextUtils.isEmpty(item.getClickLink())) {
                    Intent intent2 = new Intent(mContext, PlayMoreActivity.class);
                    intent2.putExtra("webUrl", item.getClickLink());
                    intent2.putExtra("webTitle", item.getTitle());
                    ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    ActivityCompat.startActivity(mContext, intent2, compat2.toBundle());
                }
                break;
        }
    }

    private void setupChatPeople(ArrayList<SignInterUser> userList) {

        ArrayList<List<SignInterUser>> totalArray = new ArrayList<List<SignInterUser>>();

        int start = 0;
        int subLength = 8;
        int count = userList.size() / 8;
        if (userList.size() > 8) {
            for (int i = 0; i < count; i++) {
                List<SignInterUser> dataItem = userList.subList(start, start + subLength);
                totalArray.add(dataItem);
                start += subLength;
            }

            if ((count * 8) < userList.size()) {
                List<SignInterUser> dataItem = userList.subList(count * 8, userList.size());
                totalArray.add(dataItem);
            }
        } else {
            totalArray.add(userList);
        }

        for (int i = 0; i < totalArray.size(); i++) {
            ArrayList<ImageView> imageArray = new ArrayList<>();
            final List<SignInterUser> dataItem = totalArray.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.channel_user_six_item,
                    null, false);
            LinearLayout llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
            llRoot.setOnClickListener(mSpaceClick);
            LinearLayout header_lay1 = (LinearLayout) view.findViewById(R.id.header_lay1);
            header_lay1.setOnClickListener(mSpaceClick);
            LinearLayout header_lay2 = (LinearLayout) view.findViewById(R.id.header_lay2);
            header_lay2.setOnClickListener(mSpaceClick);
            LinearLayout header_lay3 = (LinearLayout) view.findViewById(R.id.header_lay3);
            header_lay3.setOnClickListener(mSpaceClick);
            LinearLayout header_lay4 = (LinearLayout) view.findViewById(R.id.header_lay4);
            header_lay4.setOnClickListener(mSpaceClick);
            LinearLayout header_lay5 = (LinearLayout) view.findViewById(R.id.header_lay5);
            header_lay5.setOnClickListener(mSpaceClick);
            LinearLayout header_lay6 = (LinearLayout) view.findViewById(R.id.header_lay6);
            header_lay6.setOnClickListener(mSpaceClick);

            LinearLayout header_lay7 = (LinearLayout) view.findViewById(R.id.header_lay7);
            header_lay7.setOnClickListener(mSpaceClick);

            LinearLayout header_lay8 = (LinearLayout) view.findViewById(R.id.header_lay8);
            header_lay8.setOnClickListener(mSpaceClick);
            imageArray.add((ImageView) view.findViewById(R.id.header_img1));
            imageArray.add((ImageView) view.findViewById(R.id.header_img2));
            imageArray.add((ImageView) view.findViewById(R.id.header_img3));
            imageArray.add((ImageView) view.findViewById(R.id.header_img4));
            imageArray.add((ImageView) view.findViewById(R.id.header_img5));
            imageArray.add((ImageView) view.findViewById(R.id.header_img6));
            imageArray.add((ImageView) view.findViewById(R.id.header_img7));
            imageArray.add((ImageView) view.findViewById(R.id.header_img8));

            for (int j = 0; j < dataItem.size(); j++) {
                SignInterUser signItem = dataItem.get(j);
                if (TextUtils.isEmpty(signItem.getUserThumb()) && signItem.getUid() > 0) {
                    imageArray.get(j).setImageResource(R.drawable.profilephoto_small);
                    imageArray.get(j).setBackgroundResource(R.drawable.circle_dush);
                } else if (signItem.getUid() > 0) {
                    Glide.with(mContext)
                            .load(signItem.getUserThumb())
                            .dontAnimate()
                            .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
                            .into(imageArray.get(j));
                    imageArray.get(j).setBackgroundResource(R.drawable.circle_dush);
                }
            }

            peopleViewParent.add(view);
        }

        MainHeaderPeopleAdapter peopleAdapter = new MainHeaderPeopleAdapter(mContext, peopleViewParent);
        verticalViewPager.setAdapter(peopleAdapter);
        startAd();
    }

    private BannerScrollTask scrollBannerTask = new BannerScrollTask();

    private class BannerScrollTask implements Runnable {

        public BannerScrollTask() {
        }

        @Override
        public void run() {
            synchronized (verticalViewPager) {
                bannerCurrentItem = (bannerCurrentItem + 1) % bannerList.size();
                bannerImage.setCurrentItem(bannerCurrentItem);
                startBanner();
            }
        }
    }

    private Handler mainBannerHandler = new Handler(Looper.getMainLooper());

    private void startBanner() {
        tryCancelBanner();
        mainBannerHandler.postDelayed(scrollBannerTask, 3000);
    }

    private void tryCancelBanner() {
        mainBannerHandler.removeCallbacksAndMessages(null);
    }

    private ScrollTask scrollTask = new ScrollTask();

    private class ScrollTask implements Runnable {

        public ScrollTask() {
        }

        @Override
        public void run() {
            synchronized (verticalViewPager) {
                chatPeopleCurrentItem = (chatPeopleCurrentItem + 1) % peopleViewParent.size();
                verticalViewPager.setCurrentItem(chatPeopleCurrentItem);
                startAd();
            }
        }
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private void startAd() {
        tryCancelAd();
        mainHandler.postDelayed(scrollTask, 3000);
    }

    private void tryCancelAd() {
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBizAreaResponse(RespDto responce) {
        Logger.d("load bizarea success -> " + responce);

        if (responce.isSuccess()) {
            if (!areas.isEmpty()) areas.clear();

            List<BizArea> newAreas = (List<BizArea>) responce.getData();
            if (newAreas != null && !newAreas.isEmpty()) {
                areas.addAll(newAreas);
                mIsRefreshing = true;
                adapter.notifyDataSetChanged();
                mIsRefreshing = false;
            }
            //adapter.notifyItemRangeChanged(1, adapter.getItemCount());
        }
    }

    @Override
    public void onBizAreaFailure(String msg) {
        Logger.d("load bizarea failed -> " + msg);
    }

    @Override
    public void onFeatureTagResponse(RespDto responce) {
        Logger.d("load FeatureTag success -> " + responce);
        if (responce.isSuccess()) {
            //if (!dicts.isEmpty())  dicts.clear();
            List<TagDict> tags = (List<TagDict>) responce.getData();
            if (tags != null && !tags.isEmpty()) {
                dicts.addAll(tags);
                //adapter.notifyDataSetChanged();
                mIsRefreshing = true;
                adapter.notifyItemRangeChanged(2, tags.size());
                mIsRefreshing = false;
            }

            long uid = MoreUser.getInstance().getUid() == null ? 0 : MoreUser.getInstance().getUid();
            ((MainDataLoader) mPresenter).onLoadPkgs(cityCode, uid, 0, 10);
            ((MainDataLoader) mPresenter).onLoadMerchants(cityCode, 0, 5);
        }
    }

    @Override
    public void onFeatureTagFailure(String msg) {
        Logger.d("load FeatureTag failed -> " + msg);

        long uid = MoreUser.getInstance().getUid() == null ? 0 : MoreUser.getInstance().getUid();
        ((MainDataLoader) mPresenter).onLoadPkgs(cityCode, uid, 0, 10);
    }

    @Override
    public void onMerchantsFailure(String msg) {
        Logger.d("load merchants failed -> " + msg);
    }

    @Override
    public void onPkgFailure(String msg) {

    }

    @Override
    public void onPkgResponse(RespDto response) {
        Logger.d("load pkgs success -> " + response);
        if (response.isSuccess()) {
            if (!packages.isEmpty()) packages.clear();

            PackageResp resp = (PackageResp) response.getData();

            if (resp == null) {
                Package pkg = new Package();
                pkg.setPhotos(PKG_BACKGROUND);
                pkg.setTitle("");
                pkg.setMerchantName("");
                packages.add(pkg);
                mIsRefreshing = true;
                adapter.notifyDataSetChanged();
                mIsRefreshing = false;
                return;
            }

            List<Package> pkgs = resp.getList();
            if (pkgs != null && !pkgs.isEmpty()) {
                packages.addAll(pkgs);
                mIsRefreshing = true;
                adapter.notifyDataSetChanged();
                mIsRefreshing = false;
            }
        }
    }

    @Override
    public void onMerchantsResponse(RespDto response) {
        Logger.d("load merchants success -> " + response);

        if (response.isSuccess()) {
            if (!merchants.isEmpty()) merchants.clear();
            ArrayList<MerchantItem> newMerchants = (ArrayList<MerchantItem>) response.getData();
            if (newMerchants != null && !newMerchants.isEmpty()) {
                new DistanceTask(newMerchants).execute();
            }
        }
    }

    @Override
    public void onBannerResponse(RespDto responce) {
        Logger.d("load banner success -> " + responce);
        boolean bannerIsEmpty = false;
        if (responce.isSuccess()) {
            List<BannerItem> items = (List<BannerItem>) responce.getData();
            if (items != null && !items.isEmpty()) {
                bannerIsEmpty = false;
                bannerList.addAll(items);
                setupBanner(banner, items);
            } else {
                bannerIsEmpty = true;
            }
        } else {
            bannerIsEmpty = true;
        }

        if (bannerIsEmpty) {
            bannerLayout.setVisibility(View.GONE);
            bannelHeadHeight = ScreenUtil.dp2px(mContext, 105);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                    , bannelHeadHeight);
            bannelHeadBg.setLayoutParams(lp);
        }
    }

    @Override
    public void onBannerFailure(String msg) {
        bannerLayout.setVisibility(View.GONE);
        bannelHeadHeight = ScreenUtil.dp2px(mContext, 105);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , bannelHeadHeight);
        bannelHeadBg.setLayoutParams(lp);
        Logger.d("load banner failed -> " + msg);
    }

    /**
     * zune:头条和新店的信息
     **/

    @Override
    public void onHeadlineCountResponse(RespDto response) {
        if (response != null) {
            HeadlineCount data = (HeadlineCount) response.getData();
            headlineCounts.add(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMerchantCountResponse(RespDto response) {
        if (response != null) {
            MerchantCount data = (MerchantCount) response.getData();
            if (merchantCounts.size() > 0) {
                merchantCounts.remove(0);
            }
            merchantCounts.add(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHeadlineCountFailure(String msg) {
        Logger.i("zune:", "failure = " + msg);
    }

    @Override
    public void onMerchantCountFailure(String msg) {
        Logger.i("zune:", "failure = " + msg);
    }

    @Override
    public void onGetLiveListResponse(RespDto response) {
        if (response != null && response.isSuccess() && response.getData() != null) {
            ActivityData data = (ActivityData) response.getData();
            if (data != null) {
                activities.addAll(data.getList());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGetLiveListFaiure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    /**
     * M-channel
     *
     * @param msg
     */
    @Override
    public void onGetChannelListFaiure(String msg) {
        onLoadComplete(page);
    }

    @Override
    public void onGetChannelListResponse(RespDto response) {
        onLoadComplete(page);
        ArrayList<MainChannelItem> result = (ArrayList<MainChannelItem>) response.getData();

        if (result != null && result.size() > 0) {
            channelList.addAll(result);
            adapter.notifyDataSetChanged();
        } else {
            isMoreChannel = false;
            xRecyclerView.loadMoreComplete();
        }
    }

    final class DistanceTask extends AsyncTask<Void, Void, String> {

        ArrayList<MerchantItem> oldData;

        public DistanceTask(ArrayList<MerchantItem> data) {
            oldData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            for (MerchantItem item : oldData) {
                String dist = AppUtils.getDistance(MoreUser.getInstance().
                        getUserLocationLat(), MoreUser.getInstance().
                        getUserLocationLng(), item.getLat(), item.getLng());
                item.setDistanceResult(dist);
            }
            merchants.addAll(oldData);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mIsRefreshing = true;
            adapter.notifyItemRangeChanged(3 + dicts.size(), merchants.size());
            mIsRefreshing = false;
//            footer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetSignInterFaiure(String msg) {
        chat_people_layout.setVisibility(View.GONE);
    }


    @Override
    public void onGetSignInterResponse(RespDto response) {

        ArrayList<SignInterUser> result = (ArrayList<SignInterUser>) response.getData();

        if (result != null && result.size() > 0) {
            signInterUserList.addAll(result);
            setupChatPeople(signInterUserList);
        } else {
            chat_people_layout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, View view, float radius) {
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(mContext);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(mContext.getResources(), overlay));
        rs.destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMChannelEvent(MChannelCommentEvent event) {
        int sid = event.getSid();
        int size = channelList.size();
        HeadlineComment comment = event.getComment();
        Boolean isClicked = event.isClicked();
        for (int i = 0; i < size; i++) {
            if (channelList.get(i).getInformationId() == sid) {
                if (isClicked != null) {
                    if (isClicked) {
                        MainChannelItem.LikeDto likeDto;
                        if (channelList.get(i).getLikeDto() == null) {
                            likeDto = new MainChannelItem.LikeDto();
                        } else {
                            likeDto = channelList.get(i).getLikeDto();
                        }
                        likeDto.setLikeTimes(channelList.get(i).getLikeDto().getLikeTimes() + 1);
                        ArrayList<UserLikesItem> userLikes;
                        if (likeDto.getUserLikes() == null || likeDto.getUserLikes().size() == 0) {
                            userLikes = new ArrayList<>();
                        } else {
                            userLikes = likeDto.getUserLikes();
                        }
                        UserLikesItem userItem = new UserLikesItem();
                        userItem.setUid(MoreUser.getInstance().getUid());
                        userItem.setNickName(MoreUser.getInstance().getNickname());
                        userItem.setThumb(MoreUser.getInstance().getThumb());
                        userLikes.add(0, userItem);
                        likeDto.setUserLikes(userLikes);
                        channelList.get(i).setLikeDto(likeDto);
                    } else {
                        MainChannelItem.LikeDto likeDto;
                        if (channelList.get(i).getLikeDto() == null) {
                            likeDto = new MainChannelItem.LikeDto();
                        } else {
                            likeDto = channelList.get(i).getLikeDto();
                        }
                        likeDto.setLikeTimes(channelList.get(i).getLikeDto().getLikeTimes() - 1);
                        ArrayList<UserLikesItem> userLikes = likeDto.getUserLikes();
                        if (userLikes != null) {
                            a:
                            for (int i1 = 0; i1 < userLikes.size(); i1++) {
                                if (userLikes.get(i1).getUid() == MoreUser.getInstance().getUid()) {
                                    userLikes.remove(i1);
                                    break a;
                                }
                            }
                            likeDto.setUserLikes(userLikes);
                        }
                        channelList.get(i).setLikeDto(likeDto);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            if (comment != null && channelList != null && channelList.get(i) != null &&
                    channelList.get(i).getInformationId() == sid
                    && !TextUtils.isEmpty(comment.getContent())) {
                ArrayList<Comments> comments = channelList.get(i).getComments();
                if (comments == null)
                    comments = new ArrayList<>();
                Comments new_comment = new Comments();
                new_comment.setUserId(comment.getUserId());
                new_comment.setContent(comment.getContent());
                new_comment.setCid(comment.getCid());
                new_comment.setCreateTime(comment.getCreateTime());
                new_comment.setFromNickname(comment.getFromNickname());
                new_comment.setFromUserThumb(comment.getFromUserThumb());
                if (comment.getToUserId() > 0) {
                    new_comment.setToUserId(comment.getToUserId());
                    new_comment.setToUserThumb(comment.getToUserThumb());
                    new_comment.setToNickName(comment.getToNickName());
                }
                comments.add(0, new_comment);
                channelList.get(i).setComments(comments);
                channelList.get(i).setCommentCount(channelList.get(i).getCommentCount() + 1);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
