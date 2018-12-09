package com.moreclub.moreapp.ucenter.ui.activity;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.information.model.SignInterResult;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.message.ui.activity.MessageFollowListActivity;
import com.moreclub.moreapp.ucenter.model.BaseUserDetails;
import com.moreclub.moreapp.ucenter.model.UserDetails;
import com.moreclub.moreapp.ucenter.presenter.IUserDetailsLoader;
import com.moreclub.moreapp.ucenter.presenter.UserDetailsLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.UserDetailsListAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hyphenate.easeui.EaseConstant.ISSYSTEMMESSAGE;

/**
 * Created by Captain on 2017/5/2.
 */

public class UserDetailsActivity extends BaseActivity implements IUserDetailsLoader.LoaderUserDetailsDataBinder {

    private static final int FROM_LEFT = 1001;
    private static final int FROM_TOP = 1002;
    private static final int FROM_RIGHT = 1003;
    private static final int FROM_BOTTOM = 1004;

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightbtn;
    @BindView(R.id.user_details_rv)
    XRecyclerView xRecyclerView;

    @BindView(R.id.reviewPicLayout)
    LinearLayout reviewPicLayout;
    @BindView(R.id.reviewPic)
    ImageView reviewPic;


    private View header;
    private RelativeLayout activity_party_layout;
    private TextView party_tab;
    private TextView party_line;
    private RelativeLayout activity_channel_layout;
    private TextView channel_tab;
    private TextView channel_line;
    private CircleImageView headerImg;
    private LinearLayout headerClickLayout;
    private TextView user_name;
    private ImageView user_sex;
    private TextView specific_mask;
    private TextView attention_count;
    private TextView follower_count;
    private TextView attention_btn;
    private TextView message_btn;
    private ImageView inner_img;
    private TextView civCity;
    private TextView civDrink;
    private TextView civLife;
    private TextView civNight;
    private TextView civSelf;
    private TextView civStar;
    private PopupWindow popupWindow;
    private RelativeLayout modifyUserLay;
    private TextView modifyUserInfoTv;
    private LinearLayout optionLay;
    private String toUid;
    private String isChannel;
    UserDetails userDetails;
    private UserDetailsListAdapter wineAdapter;
    private ArrayList<BaseUserDetails> dataList = new ArrayList<>();
    private ArrayList<MerchantActivity> activities = new ArrayList<>();
    private ArrayList<MainChannelItem> channelArray = new ArrayList<>();
    private Integer pn = 0;
    private Integer ps = 20;

    private int page = 0;
    private int pageSize = 20;

    private Integer mid;
    private boolean isFirst = true;
    private int screenHeight;
    private int screenWidth;
    private int leftTopX;
    private int leftTopY;
    private int rightTopX;
    private int rightTopY;
    private int leftBottomX;
    private int leftBottomY;
    private int rightBottomX;
    private int rightBottomY;
    private int locationTopX;
    private int locationTopY;
    private int locationBottomX;
    private int locationBottomY;
    private int locationLeftX;
    private int locationLeftY;
    private int locationRightX;
    private int locationRightY;
    private int bleftTopX;
    private int bleftTopY;
    private int brightTopX;
    private int brightTopY;
    private int bleftBottomX;
    private int bleftBottomY;
    private int brightBottomX;
    private int brightBottomY;
    private int blocationTopX;
    private int blocationTopY;
    private int blocationBottomX;
    private int blocationBottomY;
    private int blocationLeftX;
    private int blocationLeftY;
    private int blocationRightX;
    private int blocationRightY;
    private TextView follower_tag;
    private TextView attention_tag;
    private View weight;
    private View reply_weight;
    private LinearLayout reply_lay;
    private ImageView reply_iv;
    private TextView reply_tv;
    private int sid;
    private int mySid;
    private int chatType;
    private int isfightseat = 1;
    private String invite = Constants.TITLE_ANSWER;
    private LinearLayout ll_root;
    private boolean canScroll = true;
    private int selectTab = 1;
    private View weight1;
    private View weight2;


    private View.OnClickListener followerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    UserDetailsActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(UserDetailsActivity.this, MessageFollowListActivity.class);
            intent.putExtra("followType", 0);
            ActivityCompat.startActivity(UserDetailsActivity.this, intent, compat.toBundle());
        }
    };
    private View.OnClickListener attentionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    UserDetailsActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(UserDetailsActivity.this, MessageFollowListActivity.class);
            intent.putExtra("followType", 1);
            ActivityCompat.startActivity(UserDetailsActivity.this, intent, compat.toBundle());
        }
    };
    private View.OnClickListener replyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Todo 邀请他拼座互动
            SignInterResult params = new SignInterResult();
            params.setFromUid(MoreUser.getInstance().getUid());
            params.setUid(userDetails.getUid());
            params.setTitle(Constants.TITLE_SEND_INTEREST);
            params.setSid(mySid);
            ((UserDetailsLoader) mPresenter).onReplyInvite(MoreUser.getInstance().getAccess_token(), params);
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.user_details_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        initData();
        setupViews();
    }

    @Override
    protected Class getLogicClazz() {
        return IUserDetailsLoader.class;
    }

    private void initData() {
        toUid = getIntent().getStringExtra("toUid");
        mid = getIntent().getIntExtra("mid", -1);
        sid = getIntent().getIntExtra("sid", -1);
        mySid = getIntent().getIntExtra("mySid", -1);
        isChannel = getIntent().getStringExtra("isChannel");

        MerchantActivity noActivity = new MerchantActivity();
        noActivity.setListType(-1);
        activities.add(noActivity);

        MainChannelItem noChannel = new MainChannelItem();
        noChannel.setListType(-1);
        channelArray.add(noChannel);

        selectTab = 1;
        loadData();
    }

    private void initTab() {
        channel_tab.setTextColor(ContextCompat.getColor(this, R.color.black));
        channel_line.setVisibility(View.VISIBLE);
        party_tab.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
        party_line.setVisibility(View.GONE);
    }

    private void loadData() {
        screenHeight = ScreenUtil.getScreenHeight(UserDetailsActivity.this);
        screenWidth = ScreenUtil.getScreenWidth(UserDetailsActivity.this);
        leftTopX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 130);
        leftTopY = ScreenUtil.dp2px(UserDetailsActivity.this, 50);
        rightTopX = screenWidth / 2 + ScreenUtil.dp2px(UserDetailsActivity.this, 85);
        rightTopY = ScreenUtil.dp2px(UserDetailsActivity.this, 50);
        leftBottomX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 130);
        leftBottomY = ScreenUtil.dp2px(UserDetailsActivity.this, 260);
        rightBottomX = screenWidth / 2 + ScreenUtil.dp2px(UserDetailsActivity.this, 85);
        rightBottomY = ScreenUtil.dp2px(UserDetailsActivity.this, 260);

        locationTopX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 25);
        locationTopY = ScreenUtil.dp2px(UserDetailsActivity.this, 75);
        locationBottomX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 25);
        locationBottomY = ScreenUtil.dp2px(UserDetailsActivity.this, 235);
        locationLeftX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 105);
        locationLeftY = ScreenUtil.dp2px(UserDetailsActivity.this, 158);
        locationRightX = screenWidth / 2 + ScreenUtil.dp2px(UserDetailsActivity.this, 60);
        locationRightY = ScreenUtil.dp2px(UserDetailsActivity.this, 158);

        bleftTopX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 30);
        bleftTopY = ScreenUtil.dp2px(UserDetailsActivity.this, -72);
        brightTopX = screenWidth / 2 + ScreenUtil.dp2px(UserDetailsActivity.this, 200);
        brightTopY = ScreenUtil.dp2px(UserDetailsActivity.this, 158);
        bleftBottomX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 230);
        bleftBottomY = ScreenUtil.dp2px(UserDetailsActivity.this, 158);
        brightBottomX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 30);
        brightBottomY = ScreenUtil.dp2px(UserDetailsActivity.this, 358);

        blocationTopX = screenWidth / 2 + ScreenUtil.dp2px(UserDetailsActivity.this, 65);
        blocationTopY = ScreenUtil.dp2px(UserDetailsActivity.this, 80);
        blocationBottomX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 110);
        blocationBottomY = ScreenUtil.dp2px(UserDetailsActivity.this, 240);
        blocationLeftX = screenWidth / 2 - ScreenUtil.dp2px(UserDetailsActivity.this, 110);
        blocationLeftY = ScreenUtil.dp2px(UserDetailsActivity.this, 80);
        blocationRightX = screenWidth / 2 + ScreenUtil.dp2px(UserDetailsActivity.this, 65);
        blocationRightY = ScreenUtil.dp2px(UserDetailsActivity.this, 240);

        if ("true".equals(isChannel)) {
            ((UserDetailsLoader) mPresenter).onUserActiveDetails(MoreUser.getInstance().getUid(), Long.parseLong(toUid));
            if (mid > 0)
                ((UserDetailsLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), page, pageSize);
            ((UserDetailsLoader) mPresenter).loadUserActiveList(MoreUser.getInstance().getUid(), Long.parseLong(toUid), pn, ps);
        } else {
            if (!TextUtils.isEmpty(toUid))
                ((UserDetailsLoader) mPresenter).loadUserDetails(MoreUser.getInstance().getUid(), Long.parseLong(toUid));
            if (mid > 0) {
                ((UserDetailsLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), page, pageSize);
            } else {
                xRecyclerView.loadMoreComplete();
            }
        }
    }

    private void setupViews() {
        navRightbtn.setVisibility(View.VISIBLE);
        navRightbtn.setImageResource(R.drawable.user_details_menu);
        activityTitle.setText("");
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).
                inflate(R.layout.user_details_menu, null);
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);
        TextView menu_report = (TextView) bubbleLayout.findViewById(R.id.menu_report);
        menu_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailsActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailsActivity.this,
                        UserDetailsReportActivity.class);
                intent.putExtra("relationID", toUid);
                intent.putExtra("type", "3");
                ActivityCompat.startActivity(UserDetailsActivity.this, intent, compat.toBundle());

            }
        });

        navRightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationInWindow(location);
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);
            }
        });
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        header = LayoutInflater.from(this).inflate(R.layout.user_details_header,
                (ViewGroup) findViewById(android.R.id.content), false);

        activity_party_layout = (RelativeLayout) header.findViewById(R.id.activity_party_layout);
        party_tab = (TextView) header.findViewById(R.id.party_tab);
        party_line = (TextView) header.findViewById(R.id.party_line);
        activity_channel_layout = (RelativeLayout) header.findViewById(R.id.activity_channel_layout);
        channel_tab = (TextView) header.findViewById(R.id.channel_tab);
        channel_line = (TextView) header.findViewById(R.id.channel_line);

        activity_party_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channel_tab.setTextColor(ContextCompat.getColor(UserDetailsActivity.this, R.color.merchant_item_distance));
                channel_line.setVisibility(View.GONE);
                party_tab.setTextColor(ContextCompat.getColor(UserDetailsActivity.this, R.color.black));
                party_line.setVisibility(View.VISIBLE);

                dataList.clear();
                dataList.addAll(activities);
                wineAdapter.notifyDataSetChanged();
                selectTab = 2;
            }
        });

        activity_channel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channel_tab.setTextColor(ContextCompat.getColor(UserDetailsActivity.this, R.color.black));
                channel_line.setVisibility(View.VISIBLE);
                party_tab.setTextColor(ContextCompat.getColor(UserDetailsActivity.this, R.color.merchant_item_distance));
                party_line.setVisibility(View.GONE);

                dataList.clear();
                dataList.addAll(channelArray);
                wineAdapter.notifyDataSetChanged();
                selectTab = 1;
            }
        });
        headerClickLayout = (LinearLayout) header.findViewById(R.id.header_click_layout);
        headerClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails != null && userDetails.getThumb() != null && userDetails.getThumb().length() > 0) {
                    reviewPicLayout.setVisibility(View.VISIBLE);
                    Glide.with(UserDetailsActivity.this)
                            .load(userDetails.getThumb())
                            .dontAnimate()
                            .into(reviewPic);
                }
            }
        });

        reviewPicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewPicLayout.setVisibility(View.GONE);
            }
        });

        headerImg = (CircleImageView) header.findViewById(R.id.header_img);
        user_name = (TextView) header.findViewById(R.id.user_name);
        user_sex = (ImageView) header.findViewById(R.id.user_sex);
        specific_mask = (TextView) header.findViewById(R.id.specific_mask);
        attention_btn = (TextView) header.findViewById(R.id.attention_btn);
        reply_weight = (View) header.findViewById(R.id.reply_weight);
        reply_lay = (LinearLayout) header.findViewById(R.id.reply_lay);
        reply_iv = (ImageView) header.findViewById(R.id.reply_iv);
        reply_tv = (TextView) header.findViewById(R.id.reply_tv);
        weight = (View) header.findViewById(R.id.weight);
        weight1 = (View) header.findViewById(R.id.weight1);
        weight2 = (View) header.findViewById(R.id.weight2);

        /**zune:粉丝和关注列表**/
        /**zune:自己的资料 关注他和发消息 修改为修改资料**/
        /**zune:Toast关注成功,关注失败**/
        /**zune控件转动闪烁 速度减半**/
        follower_tag = (TextView) header.findViewById(R.id.follower_tag);
        follower_count = (TextView) header.findViewById(R.id.follower_count);
        attention_tag = (TextView) header.findViewById(R.id.attention_tag);
        attention_count = (TextView) header.findViewById(R.id.attention_count);


        message_btn = (TextView) header.findViewById(R.id.message_btn);
        inner_img = (ImageView) header.findViewById(R.id.inner_img);
        modifyUserLay = (RelativeLayout) header.findViewById(R.id.modify_user_lay);
        modifyUserInfoTv = (TextView) header.findViewById(R.id.modify_user_info);
        optionLay = (LinearLayout) header.findViewById(R.id.option_lay);
        /**zune:处理位置**/
        civCity = (TextView) header.findViewById(R.id.civ_city);
        civSelf = (TextView) header.findViewById(R.id.civ_self);
        civStar = (TextView) header.findViewById(R.id.civ_star);
        civDrink = (TextView) header.findViewById(R.id.civ_drink);
        civLife = (TextView) header.findViewById(R.id.civ_life);
        civNight = (TextView) header.findViewById(R.id.civ_night);

        if (toUid != null && toUid.equals("" + MoreUser.getInstance().getUid())) {
            optionLay.setVisibility(View.GONE);
            modifyUserLay.setVisibility(View.VISIBLE);
        } else {
            optionLay.setVisibility(View.VISIBLE);
            modifyUserLay.setVisibility(View.GONE);
        }

        ValueAnimator animatorCity1 = dispatch(civCity, FROM_TOP, locationTopX, locationTopY, locationBottomX, locationBottomY, true, false);
        animatorCity1.start();
        ValueAnimator animatorCity2 = dispatch(civCity, FROM_BOTTOM, locationBottomX, locationBottomY, locationTopX, locationTopY, false, false);
        animatorCity2.start();
        ValueAnimator animatorSelf1 = dispatch(civSelf, FROM_LEFT, locationLeftX, locationLeftY, locationRightX, locationRightY, true, false);
        animatorSelf1.start();
        ValueAnimator animatorSelf2 = dispatch(civSelf, FROM_RIGHT, locationRightX, locationRightY, locationLeftX, locationLeftY, false, false);
        animatorSelf2.start();
        ValueAnimator animatorStar1 = dispatch(civStar, FROM_RIGHT, locationRightX, locationRightY, locationLeftX, locationLeftY, true, false);
        animatorStar1.start();
        ValueAnimator animatorStar2 = dispatch(civStar, FROM_LEFT, locationLeftX, locationLeftY, locationRightX, locationRightY, false, false);
        animatorStar2.start();


        ValueAnimator animatorNight1 = dispatch(civNight, FROM_TOP, blocationTopX, blocationTopY, blocationBottomX, blocationBottomY, true, true);
        animatorNight1.start();
        ValueAnimator animatorNight2 = dispatch(civNight, FROM_BOTTOM, blocationBottomX, blocationBottomY, blocationTopX, blocationTopY, false, true);
        animatorNight2.start();
        ValueAnimator animatorDrink1 = dispatch(civDrink, FROM_LEFT, blocationLeftX, blocationLeftY, blocationRightX, blocationRightY, true, true);
        animatorDrink1.start();
        ValueAnimator animatorDrink2 = dispatch(civDrink, FROM_RIGHT, blocationRightX, blocationRightY, blocationLeftX, blocationLeftY, false, true);
        animatorDrink2.start();
        ValueAnimator animatorLife1 = dispatch(civLife, FROM_RIGHT, blocationRightX, blocationRightY, blocationLeftX, blocationLeftY, true, true);
        animatorLife1.start();
        ValueAnimator animatorLife2 = dispatch(civLife, FROM_LEFT, blocationLeftX, blocationLeftY, blocationRightX, blocationRightY, false, true);
        animatorLife2.start();

        attention_btn.setOnClickListener(attentionClick);
        message_btn.setOnClickListener(sendMessageClick);
        modifyUserInfoTv.setOnClickListener(gotoModifyInfoClick);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.addHeaderView(header);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if ("true".equals(isChannel) && selectTab == 1) {
                    ++pn;
                    ((UserDetailsLoader) mPresenter).loadUserActiveList(MoreUser.getInstance().getUid(), Long.parseLong(toUid), pn, ps);
                } else if (selectTab == 2) {
                    if (mid > 0) {
                        page++;
                        ((UserDetailsLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), page, pageSize);
                    } else {
                        xRecyclerView.loadMoreComplete();
                    }
                } else {
                    xRecyclerView.loadMoreComplete();
                }
            }
        });

        initTab();
        dataList.addAll(channelArray);
        wineAdapter = new UserDetailsListAdapter(this, dataList);
        xRecyclerView.setAdapter(wineAdapter);
    }

    private ValueAnimator dispatch(final TextView view, int location, int startX, int startY, int endX, int endY, boolean isFirst, boolean isBig) {
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(location, isBig), new PointF(startX, startY), new PointF(endX, endY));
        valueAnimator.setDuration(20000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
                view.invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator.setStartDelay(20000);
                valueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setTarget(view);
        if (!isFirst) {
            valueAnimator.setStartDelay(20000);
        }
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setInterpolator(new LinearInterpolator());
        return valueAnimator;
    }

    private class BezierEvaluator implements TypeEvaluator<PointF> {


        private float firstX;
        private float firstY;
        private float secondX;
        private float secondY;

        public BezierEvaluator(int location, boolean isBig) {
            switch (location) {
                case FROM_LEFT:
                    if (!isBig) {
                        firstX = leftBottomX;
                        firstY = leftBottomY;
                        secondX = rightBottomX;
                        secondY = rightBottomY;
                    } else {
                        firstX = bleftTopX;
                        firstY = bleftTopY;
                        secondX = brightTopX;
                        secondY = brightTopY;
                    }
                    break;
                case FROM_TOP:
                    if (!isBig) {
                        firstX = leftTopX;
                        firstY = leftTopY;
                        secondX = leftBottomX;
                        secondY = leftBottomY;
                    } else {
                        firstX = brightTopX;
                        firstY = brightTopY;
                        secondX = brightBottomX;
                        secondY = brightBottomY;
                    }
                    break;
                case FROM_RIGHT:
                    if (!isBig) {
                        firstX = rightTopX;
                        firstY = rightTopY;
                        secondX = leftTopX;
                        secondY = leftTopY;
                    } else {
                        firstX = brightBottomX;
                        firstY = brightBottomY;
                        secondX = bleftBottomX;
                        secondY = bleftBottomY;
                    }
                    break;
                case FROM_BOTTOM:
                    if (!isBig) {
                        firstX = rightBottomX;
                        firstY = rightBottomY;
                        secondX = rightTopX;
                        secondY = rightTopY;
                    } else {
                        firstX = bleftBottomX;
                        firstY = bleftBottomY;
                        secondX = bleftTopX;
                        secondY = bleftTopY;
                    }
                    break;
            }
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue,
                               PointF endValue) {
            final float t = fraction;
            float oneMinusT = 1.0f - t;
            PointF point = new PointF();

            PointF point0 = (PointF) startValue;

            PointF point1 = new PointF();
            point1.set(firstX, firstY);

            PointF point2 = new PointF();
            point2.set(secondX, secondY);

            PointF point3 = (PointF) endValue;

            point.x = oneMinusT * oneMinusT * oneMinusT * (point0.x)
                    + 3 * oneMinusT * oneMinusT * t * (point1.x)
                    + 3 * oneMinusT * t * t * (point2.x)
                    + t * t * t * (point3.x);

            point.y = oneMinusT * oneMinusT * oneMinusT * (point0.y)
                    + 3 * oneMinusT * oneMinusT * t * (point1.y)
                    + 3 * oneMinusT * t * t * (point2.y)
                    + t * t * t * (point3.y);
            return point;
        }
    }

    private void onLoadComplete(Integer index) {
        if (selectTab == 1) {
            if (index == 0) {
                channelArray.clear();
                xRecyclerView.refreshComplete();
            } else {
                xRecyclerView.loadMoreComplete();
            }
        } else {
            if (index == 0) {
                activities.clear();
                xRecyclerView.refreshComplete();
            } else {
                xRecyclerView.loadMoreComplete();
            }
        }
    }


    View.OnClickListener attentionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("ddd", "关注");
            if (userDetails.getUid() != (long) MoreUser.getInstance().getUid()) {
                if ("true".equals(isChannel)) {
                    message_btn.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    weight1.setVisibility(View.GONE);
                    weight2.setVisibility(View.GONE);
                } else {
                    message_btn.setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    weight1.setVisibility(View.VISIBLE);
                    weight2.setVisibility(View.VISIBLE);
                }
                if (!userDetails.getHasFollow()) {

                    if ("true".equals(isChannel)) {
                        ChannelAttentionParam param = new ChannelAttentionParam();
                        param.setFriendId(Long.valueOf(toUid));
                        param.setOwnerId(MoreUser.getInstance().getUid());
                        param.setRemark("1");
                        ((UserDetailsLoader) mPresenter).onAttentionChannel(param);
                    } else {
                        ((UserDetailsLoader) mPresenter).onFollowAdd("" + MoreUser.getInstance().getUid(), toUid);
                    }
                } else {
                    ((UserDetailsLoader) mPresenter).onFollowDel("" + MoreUser.getInstance().getUid(), toUid);
                }
            }
        }
    };

    View.OnClickListener sendMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("ddd", "发消息");
            if (userDetails != null) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailsActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailsActivity.this, ChatActivity.class);
                intent.putExtra("userId", "" + toUid);
                intent.putExtra("mid", "" + mid);
                intent.putExtra("toChatUserID", "" + toUid);
                intent.putExtra("toChatNickName", "" + userDetails.getNickName());
                intent.putExtra("toChatHeaderUrl", "" + userDetails.getThumb());
                intent.putExtra("ISFIGHTSEAT", 1);
                ActivityCompat.startActivity(UserDetailsActivity.this, intent, compat.toBundle());
            }
        }
    };

    View.OnClickListener gotoModifyInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.pushLeftActivity(UserDetailsActivity.this, UserModifyDetailActivity.class);

        }
    };


    @Override
    public void onUserDetailsResponse(RespDto response) {
        userDetails = (UserDetails) response.getData();
        if (TextUtils.isEmpty(userDetails.getSex()) || (userDetails.getSex().equals("-1")
                && userDetails.getParties() > 0)) {
            civCity.setVisibility(View.GONE);
            civDrink.setVisibility(View.GONE);
            civLife.setVisibility(View.GONE);
            civNight.setVisibility(View.GONE);
            civSelf.setVisibility(View.GONE);
            civStar.setVisibility(View.GONE);
        } else {
            civCity.setVisibility(View.VISIBLE);
            civDrink.setVisibility(View.VISIBLE);
            civLife.setVisibility(View.VISIBLE);
            civNight.setVisibility(View.VISIBLE);
            civSelf.setVisibility(View.VISIBLE);
            civStar.setVisibility(View.VISIBLE);
            canScroll = false;
        }
        if (mySid > 0 && !MoreUser.getInstance().getUid().equals(userDetails.getUid())) {
            reply_iv.setVisibility(View.VISIBLE);
            reply_lay.setVisibility(View.VISIBLE);
            reply_weight.setVisibility(View.VISIBLE);
            reply_lay.setOnClickListener(replyListener);
        }
        if (!TextUtils.isEmpty(userDetails.getCity()))
            civCity.setText(format(userDetails.getCity()));
        else
            civCity.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getWinePrefer()))
            civDrink.setText(format(userDetails.getWinePrefer()));
        else
            civDrink.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getPersonalMark()))
            civLife.setText(format(userDetails.getPersonalMark()));
        else
            civLife.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getBookPrefer()))
            civNight.setText(format(userDetails.getBookPrefer()));
        else
            civNight.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getBrandPrefer()))
            civSelf.setText(format(userDetails.getBrandPrefer()));
        else
            civSelf.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getHobby()))
            civStar.setText(format(userDetails.getHobby()));
        else
            civStar.setVisibility(View.GONE);
        Glide.with(this)
                .load(userDetails.getThumb())
                .dontAnimate()
                .into(headerImg);
        user_name.setText(userDetails.getNickName());
        if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("0")) {
            user_sex.setImageResource(R.drawable.femenine);
        } else if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("1")) {
            user_sex.setImageResource(R.drawable.masculine);
        } else {
            user_sex.setVisibility(View.GONE);
        }

        specific_mask.setText(userDetails.getPersonalMark());
        attention_count.setText("" + userDetails.getFollow());
        follower_count.setText("" + userDetails.getFollower());

        if (userDetails.getHasFollow()) {
            attention_btn.setText("关注中");
        } else {
            attention_btn.setText("关注 TA");
        }

        if (userDetails.getUid() != (long) MoreUser.getInstance().getUid()) {
            message_btn.setVisibility(View.VISIBLE);
            weight.setVisibility(View.VISIBLE);
            weight1.setVisibility(View.VISIBLE);
            weight2.setVisibility(View.VISIBLE);
        } else {
            attention_btn.setText("修改个人资料");
            message_btn.setVisibility(View.GONE);
            weight.setVisibility(View.GONE);
            weight1.setVisibility(View.GONE);
            weight2.setVisibility(View.GONE);
            reply_iv.setVisibility(View.GONE);
            reply_lay.setVisibility(View.GONE);
            reply_weight.setVisibility(View.GONE);
        }

        if ("true".equals(isChannel)) {
            message_btn.setVisibility(View.GONE);
            weight.setVisibility(View.GONE);
            weight1.setVisibility(View.GONE);
            weight2.setVisibility(View.GONE);
        }
    }

    private String format(String city) {
        if (city != null && city.length() > 5) {
            return city.substring(0, 5) + "...";
        } else if (city == null) {
            return "";
        }
        return city;
    }

    @Override
    public void onUserDetailsFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            finish();
            return;
        }
    }

    /**
     * 关注
     *
     * @param msg
     */


    @Override
    public void onFollowDelFailure(String msg) {

    }

    @Override
    public void onReplyInviteResponse(RespDto body) {
        //Todo 拼座邀请成功
        if (body != null) {
            Boolean data = (Boolean) body.getData();
            if (data != null && data) {
                Toast.makeText(UserDetailsActivity.this, "已发送邀请成功", Toast.LENGTH_SHORT).show();
                //Todo 进入会话框
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailsActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailsActivity.this, ChatActivity.class);
                intent.putExtra("userId", toUid + "");
                intent.putExtra("toChatUserID", toUid);
                intent.putExtra("toChatNickName", userDetails.getNickName());
                intent.putExtra("toChatHeaderUrl", userDetails.getThumb());
                intent.putExtra("invite", invite);
                intent.putExtra("ISFIGHTSEAT", 1);
                ActivityCompat.startActivity(UserDetailsActivity.this, intent, compat.toBundle());
                EMMessage message = EMMessage.createTxtSendMessage(Constants.TITLE_SEND_REPLY, toUid + "");
                sendMessage(message);
            } else
                Toast.makeText(UserDetailsActivity.this, body.getErrorDesc(), Toast.LENGTH_SHORT).show();
        }

    }

    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        //Todo Type类型
        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        message.setAttribute("nickName", MoreUser.getInstance().getNickname());
        message.setAttribute("headerUrl", MoreUser.getInstance().getThumb());
        message.setAttribute(toUid + "", userDetails.getNickName() + "," + userDetails.getThumb());
        message.setAttribute("fromUID", "" + MoreUser.getInstance().getUid());
        message.setAttribute("invite", invite);
        message.setAttribute(ISSYSTEMMESSAGE, 0);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("", "");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("", "");
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("", "");
            }
        });
    }

    @Override
    public void onReplyInviteFailure(String msg) {
        //Todo 拼座邀请失败
        Log.i("zune:", "msg = " + msg);
        Toast.makeText(UserDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserActiveListFailure(String msg) {
        Log.d("ddd", "");
        onLoadComplete(pn);
    }

    @Override
    public void onUserActiveListResponse(RespDto body) {
        onLoadComplete(pn);
        ArrayList<MainChannelItem> result = (ArrayList<MainChannelItem>) body.getData();
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                MainChannelItem item = result.get(i);
                item.setListType(1);
            }
            if (pn == 0) {
                channelArray.clear();
                channelArray.addAll(result);
            }
            dataList.clear();
            dataList.addAll(channelArray);
            wineAdapter.notifyDataSetChanged();
        } else {
            xRecyclerView.loadMoreComplete();
        }
        Log.d("ddd", "");
    }

    @Override
    public void onUserActiveDetailsFailure(String msg) {

    }


    @Override
    public void onUserActiveDetailsResponse(RespDto body) {
        userDetails = (UserDetails) body.getData();

        if (TextUtils.isEmpty(userDetails.getSex()) || (userDetails.getSex().equals("-1") && userDetails.getParties() > 0)) {
            civCity.setVisibility(View.GONE);
            civDrink.setVisibility(View.GONE);
            civLife.setVisibility(View.GONE);
            civNight.setVisibility(View.GONE);
            civSelf.setVisibility(View.GONE);
            civStar.setVisibility(View.GONE);
        } else {
            canScroll = false;
        }
        if (mySid > 0 && !MoreUser.getInstance().getUid().equals(userDetails.getUid())) {
            reply_iv.setVisibility(View.VISIBLE);
            reply_lay.setVisibility(View.VISIBLE);
            reply_weight.setVisibility(View.VISIBLE);
            reply_lay.setOnClickListener(replyListener);
        }
        if (!TextUtils.isEmpty(userDetails.getCity()))
            civCity.setText(format(userDetails.getCity()));
        else
            civCity.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getWinePrefer()))
            civDrink.setText(format(userDetails.getWinePrefer()));
        else
            civDrink.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getPersonalMark()))
            civLife.setText(format(userDetails.getPersonalMark()));
        else
            civLife.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getBookPrefer()))
            civNight.setText(format(userDetails.getBookPrefer()));
        else
            civNight.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getBrandPrefer()))
            civSelf.setText(format(userDetails.getBrandPrefer()));
        else
            civSelf.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userDetails.getHobby()))
            civStar.setText(format(userDetails.getHobby()));
        else
            civStar.setVisibility(View.GONE);
        Glide.with(this)
                .load(userDetails.getThumb())
                .dontAnimate()
                .into(headerImg);
        user_name.setText(userDetails.getNickName());
        if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("0")) {
            user_sex.setImageResource(R.drawable.femenine);
        } else if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("1")) {
            user_sex.setImageResource(R.drawable.masculine);
        } else {
            user_sex.setVisibility(View.GONE);
        }

        specific_mask.setText(userDetails.getPersonalMark());
        attention_count.setText("" + userDetails.getFollow());
        follower_count.setText("" + userDetails.getFollower());

        if (userDetails.getHasFollow()) {
            attention_btn.setText("关注中");
        } else {
            attention_btn.setText("关注 TA");
        }

        if (userDetails.getUid() != (long) MoreUser.getInstance().getUid()) {
            message_btn.setVisibility(View.VISIBLE);
            weight.setVisibility(View.VISIBLE);
            weight1.setVisibility(View.VISIBLE);
            weight2.setVisibility(View.VISIBLE);
        } else {
            attention_btn.setText("修改个人资料");
            message_btn.setVisibility(View.GONE);
            weight.setVisibility(View.GONE);
            weight1.setVisibility(View.GONE);
            weight2.setVisibility(View.GONE);
            reply_iv.setVisibility(View.GONE);
            reply_lay.setVisibility(View.GONE);
            reply_weight.setVisibility(View.GONE);
        }

        if ("true".equals(isChannel)) {
            message_btn.setVisibility(View.GONE);
            weight.setVisibility(View.GONE);
            weight1.setVisibility(View.GONE);
            weight2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMerchantActivityResponse(RespDto body) {
        onLoadComplete(page);
        List<MerchantActivity> data = (List<MerchantActivity>) body.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                MerchantActivity item = data.get(i);
                item.setListType(2);
            }

            if (page == 0) {
                activities.clear();
            }
            activities.addAll(data);
        } else {
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onMerchantActivityFailure(String message) {
        Log.i("zune:", "msg = " + message);
        onLoadComplete(page);
    }

    @Override
    public void onFollowDelResponse(RespDto response) {
        if (userDetails.getHasFollow()) {
            userDetails.setHasFollow(false);
            attention_btn.setText("关注 TA");
        }
    }

    @Override
    public void onFollowAddFailure(String msg) {
        Toast.makeText(this, "关注失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowAddResponse(RespDto response) {
        if (!userDetails.getHasFollow()) {
            userDetails.setHasFollow(true);
            attention_btn.setText("关注中");
        }
    }

    @Override
    public void onAttentionChannelFailure(String msg) {

    }

    @Override
    public void onAttentionChannelResponse(RespDto responce) {
        ChannelAttentionResult result = (ChannelAttentionResult) responce.getData();
        if (result != null) {
            userDetails.setHasFollow(true);
            attention_btn.setText("关注中");
        }
    }

    @Override
    public void onBackPressed() {
        if (reviewPicLayout.getVisibility() == View.VISIBLE) {
            reviewPicLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
