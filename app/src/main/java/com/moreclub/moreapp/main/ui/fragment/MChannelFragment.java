package com.moreclub.moreapp.main.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.jiecaovideoplayer.JCVideoPlayer;
import com.moreclub.common.log.Logger;
import com.moreclub.common.magicindicator.MagicIndicator;
import com.moreclub.common.magicindicator.ViewPagerHelper;
import com.moreclub.common.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.moreclub.common.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.moreclub.common.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.Comments;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.main.model.UserLikesItem;
import com.moreclub.moreapp.main.model.event.ChannelPublishSuccessEvent;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.PublishMChannelActivity;
import com.moreclub.moreapp.main.ui.adapter.MChannelPagerAdapter;
import com.moreclub.moreapp.main.ui.view.BaseTabView;
import com.moreclub.moreapp.main.ui.view.MChannelAttentionView;
import com.moreclub.moreapp.main.ui.view.MChannelRecommendView;
import com.moreclub.moreapp.main.ui.view.MChannelSameCityView;
import com.moreclub.moreapp.main.ui.view.MChannelTopicDetailsView;
import com.moreclub.moreapp.ucenter.ui.activity.PlayMoreActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.GlideImageLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-09-08-0008.
 */

public class MChannelFragment extends BaseFragment {

    private final static String KEY_CITY_ID = "city.id";
    public static final int DISMISS_SWIPE = 1001;
    private FragmentActivity mContext;
    private ArrayList<Object> dataList;
    public int cityID;
    private MChannelPagerAdapter pagerAdapter;
    public CommonNavigatorAdapter commonNavigatorAdapter;
    public MChannelRecommendView mChannelRecommendView;
    public MChannelAttentionView mChannelAttentionView;
    public MChannelSameCityView mChannelSameCityView;
    public ArrayList<BaseTabView> viewList = new ArrayList<>();
    public ArrayList<String> tabTitles = new ArrayList<>();

    public MagicIndicator channelTab;
    public ViewPager viewPager;
    public View header;
    public Toolbar title_bar;
    private Banner image_banner;
    private ArrayList<BannerItem> bannerList;
    private int bannelH;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private TabLayout tl_main;
    private ImageView ivCamera;
    private TextView activityTitle;
    private CollapsingToolbarLayoutState state;
    private TextView activityTitle2;
    private ArrayList<View> pagerViews;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private View.OnClickListener publishChannelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(getContext(), UseLoginActivity.class);
                return;
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    getContext(), R.anim.push_down_in, R.anim.push_down_out);
            Intent intent = new Intent(getContext(), PublishMChannelActivity.class);
            intent.putExtra("from", "MChannelFragment");
            ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
        }
    };
    private AppBarLayout.OnOffsetChangedListener appBarScrollListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset == 0) {
                if (state != CollapsingToolbarLayoutState.EXPANDED) {
                    state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    mCollapsingToolbarLayout.setTitle("");//设置title为EXPANDED
                    activityTitle.setVisibility(View.INVISIBLE);
                    ivCamera.setVisibility(View.VISIBLE);
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                    mCollapsingToolbarLayout.setTitle("频道");//设置title不显示
                    activityTitle.setVisibility(View.VISIBLE);//隐藏播放按钮ss
                    state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    ivCamera.setVisibility(View.VISIBLE);
                }
            } else {
                if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                        activityTitle.setVisibility(View.INVISIBLE);//由折叠变为中间状态时隐藏播放按钮
                    }
                    mCollapsingToolbarLayout.setTitle("");//设置title为INTERNEDIATE
                    state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    ivCamera.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.mchannel_fragment;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mContext = getActivity();
        bannerList = new ArrayList<>();
        dataList = new ArrayList<>();
        pagerViews = new ArrayList<>();
        pagerAdapter = new MChannelPagerAdapter(mContext, pagerViews);
        setupViews(rootView);
        loadTab();
        initData();
    }

    public void reLoadData() {
        if (dataList != null)
            dataList.clear();
        if (bannerList != null)
            bannerList.clear();
        if (viewList != null)
            viewList.clear();
        if (tabTitles != null)
            tabTitles.clear();
        if (pagerViews != null)
            pagerViews.clear();
        if (tl_main != null)
            tl_main.removeAllTabs();
        setupViews(rootView);
        loadTab();
        initData();
    }

    private void initData() {
        /**zune:getBinder出现过类型强转异常**/
        Callback callback = new Callback<RespDto<List<BannerItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<BannerItem>>> call,
                                   Response<RespDto<List<BannerItem>>> response) {
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    Logger.d("load banner success -> " + response.body());
                    boolean bannerIsEmpty;
                    if (response.body().isSuccess()) {
                        List<BannerItem> items = response.body().getData();
                        if (items != null && !items.isEmpty()) {
                            bannerIsEmpty = false;
                            bannerList.addAll(items);
                            setupBanner(image_banner, items);
                        } else {
                            bannerIsEmpty = true;
                            setupBanner(image_banner, null);
                        }
                    } else {
                        bannerIsEmpty = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<BannerItem>>> call, Throwable t) {
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadBanner(cityID, "spage", 0).enqueue(callback);
    }

    private void setupViews(final View view) {
        cityID = PrefsUtils.getInt(mContext, KEY_CITY_ID, 2);
        header = view.findViewById(R.id.header);
        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.apl_main);
        title_bar = (Toolbar) view.findViewById(R.id.title_bar);
        image_banner = (Banner) view.findViewById(R.id.image_banner);
        tl_main = (TabLayout) view.findViewById(R.id.tl_main);
        viewPager = (ViewPager) view.findViewById(R.id.vp_main);
        ivCamera = (ImageView) view.findViewById(R.id.iv_camera_news);
        activityTitle = (TextView) view.findViewById(R.id.activity_title);
        activityTitle2 = (TextView) view.findViewById(R.id.activity_title2);
        pagerAdapter = new MChannelPagerAdapter(mContext, pagerViews);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.ctl_main);
        viewPager.setCurrentItem(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCollapsingToolbarLayout.setNestedScrollingEnabled(false);
        }
        activityTitle.setText("频道");
        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
        //设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
        //收缩后字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        title_bar.getBackground().setAlpha(0);//toolbar透明度初始化为0
        viewPager.setAdapter(pagerAdapter);
        title_bar.setOverScrollMode(TabLayout.MODE_SCROLLABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title_bar.setBackgroundTintMode(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title_bar.setForegroundTintMode(null);
            }
        }
        ivCamera.setOnClickListener(publishChannelClick);
        mAppBarLayout.addOnOffsetChangedListener(appBarScrollListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1 && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(getContext(), UseLoginActivity.class);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                JCVideoPlayer.releaseAllVideos();
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_main));
        tl_main.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        //设置样式刷新显示的位置
    }
//        initMagicIndicator();

    private void initMagicIndicator() {
        channelTab.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setText(tabTitles.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                linePagerIndicator.setColors(Color.BLACK);
                return linePagerIndicator;
            }
        };
        commonNavigator.setAdapter(commonNavigatorAdapter);
        channelTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(channelTab, viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMChannelEvent(MChannelCommentEvent event) {
        int sid = event.getSid();
        HeadlineComment comment = event.getComment();
        Boolean isClicked = event.isClicked();
        if (mChannelRecommendView != null)
            dispatchAdater(sid, mChannelRecommendView.dataList, mChannelRecommendView.adapter, comment, isClicked);
        if (mChannelAttentionView != null)
            dispatchAdater(sid, mChannelAttentionView.dataList, mChannelAttentionView.adapter, comment, isClicked);
        if (mChannelSameCityView != null)
            dispatchAdater(sid, mChannelSameCityView.dataList, mChannelSameCityView.adapter, comment, isClicked);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChannelPublishEvent(ChannelPublishSuccessEvent event) {
    }

    private void dispatchAdater(int sid, ArrayList<MainChannelItem> channelList
            , RecyclerAdapter adapter, HeadlineComment comment, Boolean isClicked) {
        if (channelList == null || adapter == null) return;
        int size = channelList.size();
        for (int i = 0; i < size; i++) {
            if (channelList.get(i).getInformationId() == sid && isClicked != null) {
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
                    ArrayList<UserLikesItem> userLikes = channelList.get(i).getLikeDto().getUserLikes();
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

    public void loadTab() {

        mChannelRecommendView = new MChannelRecommendView(this, mContext);
        viewList.add(mChannelRecommendView);

        mChannelAttentionView = new MChannelAttentionView(this, mContext);
        viewList.add(mChannelAttentionView);

        mChannelSameCityView = new MChannelSameCityView(this, mContext);
        viewList.add(mChannelSameCityView);
        for (int i = 0; i < viewList.size(); i++) {
            View temp = viewList.get(i).createView();
            pagerViews.add(temp);
        }
        tabTitles.add("   推荐   ");
        tabTitles.add("   我关注的   ");
        tabTitles.add("   同城   ");
        pagerAdapter.notifyDataSetChanged();
        Callback<RespDto<ArrayList<Topic>>> callback
                = new Callback<RespDto<ArrayList<Topic>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<Topic>>> call,
                                   Response<RespDto<ArrayList<Topic>>> response) {
                if (response == null || response.body() == null) return;
                List<Topic> result = response.body().getData();
                if (result != null && result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        Topic item = result.get(i);
                        tabTitles.add("   " + item.getName() + "   ");
                        MChannelTopicDetailsView viewItem = new MChannelTopicDetailsView(
                                MChannelFragment.this, mContext, item.getName(), "", item.getTid());
                        viewList.add(viewItem);
                        pagerViews.add(viewItem.createView());
                    }

                    for (int i = 0; i < tabTitles.size(); i++) {
                        tl_main.addTab(tl_main.newTab().setText(tabTitles.get(i)));
                    }
                    title_bar.setOverScrollMode(TabLayout.MODE_SCROLLABLE);
                    pagerAdapter.notifyDataSetChanged();
//                    commonNavigatorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<Topic>>> call, Throwable t) {

            }
        };
        ApiInterface.ApiFactory.createApi().onLoadtopics(cityID).enqueue(callback);
    }

    private void setupBanner(Banner banner, final List<BannerItem> items) {
        final ArrayList<String> images = new ArrayList<>();
        if (items == null) {
            banner.setVisibility(View.GONE);
            activityTitle2.setVisibility(View.VISIBLE);
            mCollapsingToolbarLayout.setTitle("频道");//设置title不显示
            activityTitle.setVisibility(View.VISIBLE);//隐藏播放按钮ss
            state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
            ivCamera.setVisibility(View.VISIBLE);
            return;
        } else {
            state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
            mCollapsingToolbarLayout.setTitle("");//设置title为EXPANDED
            activityTitle.setVisibility(View.INVISIBLE);
            activityTitle2.setVisibility(View.GONE);
            banner.setVisibility(View.VISIBLE);
        }
        for (BannerItem item : items) {
            images.add(item.getBannerPhoto());
        }
        int sw = ScreenUtil.getScreenWidth(mContext);
        bannelH = (int) (((float) ScreenUtil.dp2px(mContext, 169) / ScreenUtil.dp2px(mContext, 288)) * sw);
        if (bannelH == 0) {
            bannelH = ScreenUtil.dp2px(mContext, 170);
        }
        LinearLayout.LayoutParams paramb = new LinearLayout.LayoutParams(sw, bannelH);
        banner.setLayoutParams(paramb);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
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
                    } else if (!TextUtils.isEmpty(item.getClickLink())) {
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
                intent.putExtra("mid", "" + item.getRelationId());
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

    public static int[] getViewWidthAndHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(width, height);

        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    /**
     * RecyclerView 滚动到底部 最后一条完全显示
     *
     * @param recyclerView
     * @return
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
