package com.moreclub.moreapp.main.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.jiecaovideoplayer.JCVideoPlayer;
import com.moreclub.common.magicindicator.MagicIndicator;
import com.moreclub.common.magicindicator.ViewPagerHelper;
import com.moreclub.common.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.moreclub.common.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.moreclub.common.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.Comments;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.main.model.UserLikesItem;
import com.moreclub.moreapp.main.model.event.ChannelPublishSuccessEvent;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.ui.adapter.MChannelPagerAdapter;
import com.moreclub.moreapp.main.ui.view.BaseTabView;
import com.moreclub.moreapp.main.ui.view.MChannelAttentionView;
import com.moreclub.moreapp.main.ui.view.MChannelRecommendView;
import com.moreclub.moreapp.main.ui.view.MChannelSameCityView;
import com.moreclub.moreapp.main.ui.view.MChannelTopicDetailsView;
import com.moreclub.moreapp.main.ui.view.MainChannelViewV3;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelListActivity extends BaseListActivity {
    private final static String KEY_CITY_ID = "city.id";
    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.magic_indicator)
    MagicIndicator channelTab;
    @BindView(R.id.mychannel_container)
    ViewPager viewPager;
    @BindView(R.id.nav_publish_btn)
    ImageButton navPublishBtn;
    private int cityID;
    private MChannelPagerAdapter pagerAdapter;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    public MChannelRecommendView mChannelRecommendView;
    public MChannelAttentionView mChannelAttentionView;
    public MChannelSameCityView mChannelSameCityView;
    public BaseTabView mChannelViewV3;
    ArrayList<BaseTabView> viewList = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    private ArrayList<View> pagerViews;

    private boolean isChannelAttentionSelect;
    private boolean isChannelSameCitySelect;
    private boolean isChannelUploadSuccess;
    private boolean isChannelV3Select;

    @Override
    protected int getLayoutResource() {
        return R.layout.channel_list_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        initData();
        setupViews();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
    }

    private void initData() {
        cityID = PrefsUtils.getInt(this, KEY_CITY_ID, 2);
    }

    private void setupViews() {
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JCVideoPlayer.backPress()) {
                    return;
                }
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
        activityTitle.setText("M-channel");

        mChannelRecommendView = new MChannelRecommendView(null, this);
        viewList.add(mChannelRecommendView);
        mChannelRecommendView.loadData();

        mChannelAttentionView = new MChannelAttentionView(null,this);
        viewList.add(mChannelAttentionView);

        mChannelSameCityView = new MChannelSameCityView(null, this);
        viewList.add(mChannelSameCityView);

        mChannelViewV3 = new MainChannelViewV3(this);
        viewList.add(mChannelViewV3);

        tabTitles.add("   推荐   ");
        tabTitles.add("   我关注的   ");
        tabTitles.add("   同城   ");
        tabTitles.add("   测试   ");

        for (int i = 0; i < viewList.size(); i++) {
            View temp = viewList.get(i).createView();
            pagerViews.add(temp);
        }
        pagerViews = new ArrayList<>();
        pagerAdapter = new MChannelPagerAdapter(MChannelListActivity.this, pagerViews);
        viewPager.setAdapter(pagerAdapter);
//        viewPager.setOffscreenPageLimit(3);
        loadTab();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                JCVideoPlayer.releaseAllVideos();
                switch (position) {
                    case 0:
                        if (isChannelUploadSuccess) {
                            mChannelRecommendView.reloadData();
                            mChannelRecommendView.loadData();
                        }
                        break;
                    case 1:
                        if (MoreUser.getInstance().getUid() != null
                                && MoreUser.getInstance().getUid().equals(0L)) {
                            AppUtils.pushLeftActivity(MChannelListActivity.this, UseLoginActivity.class);
                        }
                        if (!isChannelAttentionSelect) {
                            isChannelAttentionSelect = true;
                            mChannelAttentionView.loadData();
                        }
                        break;
                    case 2:
                        if (!isChannelSameCitySelect) {
                            isChannelSameCitySelect = true;
                            mChannelSameCityView.loadData();
                        }
                        break;
                    case 3:
                        if (!isChannelV3Select) {
                            isChannelV3Select = true;
                            mChannelViewV3.loadData();
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelListActivity.this, UseLoginActivity.class);
                    return;
                }
                AppUtils.pushDownActivity(MChannelListActivity.this, PublishMChannelActivity.class);
            }
        });
        initMagicIndicator();
    }

    private void initMagicIndicator() {

        channelTab.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
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
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean fromPublish = intent.getBooleanExtra("fromPublish", false);
        if (fromPublish) {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMChannelEvent(MChannelCommentEvent event) {
        int sid = event.getSid();
        HeadlineComment comment = event.getComment();
        Boolean isClicked = event.isClicked();
        dispatchAdater(sid, mChannelRecommendView.dataList, mChannelRecommendView.adapter, comment, isClicked);
        dispatchAdater(sid, mChannelAttentionView.dataList, mChannelAttentionView.adapter, comment, isClicked);
        dispatchAdater(sid, mChannelSameCityView.dataList, mChannelSameCityView.adapter, comment, isClicked);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChannelPublishEvent(ChannelPublishSuccessEvent event) {
        isChannelUploadSuccess = event.isChannelPublishSuccess();
    }

    private void dispatchAdater(int sid, ArrayList<MainChannelItem> channelList
            , RecyclerAdapter adapter, HeadlineComment comment, Boolean isClicked) {
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (mChannelRecommendView.adapter.photoAlertView != null &&
//                    mChannelRecommendView.adapter.photoAlertView.isShowing()) {
//                mChannelRecommendView.adapter.photoAlertView.dismiss();
//            }
//            if (mChannelAttentionView.adapter.photoAlertView != null &&
//                    mChannelAttentionView.adapter.photoAlertView.isShowing()) {
//                mChannelAttentionView.adapter.photoAlertView.dismiss();
//            }
//            if (mChannelSameCityView.adapter.photoAlertView != null &&
//                    mChannelSameCityView.adapter.photoAlertView.isShowing()) {
//                mChannelSameCityView.adapter.photoAlertView.dismiss();
//            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void loadTab() {

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
                        MChannelTopicDetailsView viewItem = new MChannelTopicDetailsView(null,MChannelListActivity.this, item.getName(), "", item.getTid());
                        viewList.add(viewItem);
                    }

//                    viewPager.setOffscreenPageLimit(tabTitles.size());
                    pagerAdapter.notifyDataSetChanged();
                    commonNavigatorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<Topic>>> call, Throwable t) {

            }
        };
        ApiInterface.ApiFactory.createApi().onLoadtopics(cityID).enqueue(callback);

    }
}
