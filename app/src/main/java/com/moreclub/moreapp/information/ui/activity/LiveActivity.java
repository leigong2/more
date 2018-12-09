package com.moreclub.moreapp.information.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseConstant;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.ui.view.scrollview.ObservableScrollViewCallbacks;
import com.moreclub.common.ui.view.scrollview.ScrollState;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.ActivityDetail;
import com.moreclub.moreapp.information.model.ActivityIntros;
import com.moreclub.moreapp.information.model.ChatGroupAdd;
import com.moreclub.moreapp.information.model.HxRoomDetails;
import com.moreclub.moreapp.information.model.MessageWall;
import com.moreclub.moreapp.information.presenter.ILiveDataLoader;
import com.moreclub.moreapp.information.presenter.LiveDataLoader;
import com.moreclub.moreapp.information.ui.adapter.MessageWallAdapter;
import com.moreclub.moreapp.information.ui.view.CenterTextView;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.MerchantMapActivity;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.ShareUtils;
import com.moreclub.moreapp.util.UserCollect;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

public class LiveActivity extends BaseListActivity implements ILiveDataLoader.LoadLiveDataBinder {


    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.nav_share_btn)
    ImageButton navLike;
    @BindView(R.id.nav_right_btn)
    ImageButton navShare;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_second_line)
    CenterTextView tvSecondLine;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_from)
    CenterTextView tvFrom;
    @BindView(R.id.tv_where)
    TextView tvWhere;
    @BindView(R.id.iv_where)
    ImageView ivWhere;
    @BindView(R.id.iv_from)
    ImageView ivFrom;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.civ_thumb)
    CircleImageView civThumb;
    @BindView(R.id.ll_goods)
    LinearLayout llGoods;
    @BindView(R.id.tv_club)
    TextView tvClub;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.activity_desc)
    TextView activityDesc;
    @BindView(R.id.nav_back)
    ImageButton navBack;
    @BindView(R.id.tv_enter_chat)
    TextView tvEnterChat;
    @BindView(R.id.view_add_message_wall)
    TextView addWall;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.scroll_view)
    ObservableScrollView scrollView;
    @BindView(R.id.wv_detail)
    WebView webView;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.pb_live)
    ProgressBar pbLive;
    @BindView(R.id.xrv_message_wall)
    RecyclerView xrvMessageWall;

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private ActivityIntros intros;
    private SystemBarTintManager tintManager;
    private PopupWindow mShareWindow;
    private View popupShareView;
    private Integer pn = 0;
    private Integer ps = 10;
    private List<MessageWall> mMessageWallDatas;
    private RecyclerView.Adapter adapter;
    private Integer actId;
    private int mid;
    private String roomID;

    private ObservableScrollViewCallbacks callbacks = new ObservableScrollViewCallbacks() {
        @Override
        public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            int alpha = scrollY * 8 / 10;
            String str = Long.toHexString(alpha).toUpperCase();
            if (alpha >= 16 && alpha < 256) {
                navLayout.setBackgroundColor(Color.parseColor("#" + str + "000000"));
                tintManager.setTintColor(Color.parseColor("#" + str + "000000"));
                tvEnterChat.setAlpha(1.0f);
            } else if (alpha < 16 && alpha > 0) {
                navLayout.setBackgroundColor(Color.parseColor("#0" + str + "000000"));
                tintManager.setTintColor(Color.parseColor("#0" + str + "000000"));
                tvEnterChat.setAlpha(alpha / 16);
            }
            /**zune:活动聊天室消失**/
            /**zune:地址显示一行**/
        }

        @Override
        public void onDownMotionEvent() {

        }

        @Override
        public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        }
    };
    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbLive.setVisibility(View.GONE);
            rlContent.setVisibility(View.VISIBLE);
        }
    };
    /**
     * @param context
     * @param shareType 0-朋友圈，微博,1-微信好友
     * @param channel   0-app,1-微信,2,微博,3-qq
     * @param module    0-活动,1-资讯,2-app,3-商家,4-套餐,5-拼座,6-黑卡,7-橙卡
     * @param moduleID
     */
    private View.OnClickListener weixinFriendShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(LiveActivity.this, 1, 1, 0, intros.getActivityId() + "");
            share.share();
        }
    };
    private View.OnClickListener weixinGroupShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(LiveActivity.this, 0, 1, 0, intros.getActivityId() + "");
            share.share();
        }
    };
    private View.OnClickListener weiboShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(LiveActivity.this, 0, 2, 0, intros.getActivityId() + "");
            share.share();
        }
    };
    private boolean isFirst = true;

    @Override
    protected int getLayoutResource() {
        return R.layout.live_activity;
    }

    @Override
    public Class getLogicClazz() {
        return ILiveDataLoader.class;
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
        mMessageWallDatas = new ArrayList<>();
        addWall.setVisibility(View.GONE);
        actId = getIntent().getIntExtra(Constants.KEY_ACT_ID, -1);
        ((LiveDataLoader) mPresenter).onLoadLiveDetail(actId, MoreUser.getInstance().getUid());
        ((LiveDataLoader) mPresenter).onLoadLiveIntros(actId, MoreUser.getInstance().getUid());
        ((LiveDataLoader) mPresenter).onLoadMessageWall(actId, pn, ps);
        RecyclerView.LayoutManager lm = new GridLayoutManager(LiveActivity.this, 1, LinearLayoutManager.HORIZONTAL, false);
        xrvMessageWall.setLayoutManager(lm);

        navLayout.setBackgroundColor(Color.parseColor("#00000000"));
        navLike.setVisibility(View.VISIBLE);
        navShare.setVisibility(View.VISIBLE);
        navLike.setImageResource(R.drawable.collect_fav);
        navShare.setImageResource(R.drawable.top_share);
        scrollView.addScrollViewCallbacks(callbacks);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus();
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        ((LiveDataLoader) mPresenter).onLoadLiveDetail(actId, MoreUser.getInstance().getUid());
        ((LiveDataLoader) mPresenter).onLoadLiveIntros(actId, MoreUser.getInstance().getUid());
        ((LiveDataLoader) mPresenter).onLoadMessageWall(actId, pn, ps);
    }

    /**
     * zune:设置状态栏沉浸
     **/
    @TargetApi(19)
    private void setTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = -1;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            navLayout.setTranslationY(statusBarHeight);
        }
    }

    private void setupView(ActivityIntros intros) {
        tvSecondLine.setText(intros.getTitle());
        String tags = intros.getTag();
        if (tags != null) {
            String[] tagsArr = tags.split(",");
            for (int i = 0; i < tagsArr.length; i++) {
                TextView textView = (TextView) View.inflate(LiveActivity.this, R.layout.live_activity_tag, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(8, 0, 8, 0);
                textView.setLayoutParams(layoutParams);
                textView.setText(tagsArr[i]);
                llName.addView(textView);
            }
        }
//        int offset = ScreenUtil.dp2px(this, 20);
//        StringBuilder cutString = new StringBuilder(cutString(tvWhere, intros.getAddr(), offset));
//        if (intros.getAddr().length() > cutString.length()) {
//            cutString.append("....");
//        }
//        setTexts(tvWhere, R.drawable.address_activedetail, cutString.toString());
//        setTexts(tvFrom, R.drawable.ucenter_triangle, "来自 " + intros.getMerchant() + "  ");

        tvFrom.setVisibility(View.VISIBLE);
        tvWhere.setVisibility(View.VISIBLE);
        ivFrom.setVisibility(View.VISIBLE);
        ivWhere.setVisibility(View.VISIBLE);
        tvFrom.setText("来自 " + intros.getMerchant() + "  ");
        tvWhere.setText(intros.getAddr());
        float textWidth = getTextWidth(tvFrom, "来自 " + intros.getMerchant() + "  ");
        ivFrom.setTranslationX(ScreenUtil.getScreenWidth(this) - (ScreenUtil.getScreenWidth(this) - textWidth) / 2 + ScreenUtil.dp2px(this, 0));
        float textWidth1 = getTextWidth(tvWhere, intros.getAddr());
        if (textWidth1 >= ScreenUtil.getScreenWidth(this) - ScreenUtil.dp2px(this, 32)) {
            ivWhere.setTranslationX(ScreenUtil.getScreenWidth(this) - ScreenUtil.dp2px(this, 16) + ScreenUtil.dp2px(this, 0));
        } else {
            ivWhere.setTranslationX(ScreenUtil.getScreenWidth(this) - (ScreenUtil.getScreenWidth(this) - textWidth1) / 2 + ScreenUtil.dp2px(this, 0));
        }

        tvStartTime.setText(formatDay(intros.getHoldingType(), intros.getHoldingTimes(), intros.getStartTime())
                + formatTime(intros.getStartTime()) + "-" + formatTime(intros.getEndTime()));
        tvClub.setText(intros.getSponsorName());
        tvDesc.setText(intros.getIntroduction());
        if (intros.getTextType() == null) {
            StringBuilder html = initWebViewSetting(intros.getDetails());
            webView.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", null);
            webView.setWebViewClient(client);
        } else if (intros.getTextType().equals(2)){
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            webView.loadUrl(intros.getDetails());
            pbLive.setVisibility(View.GONE);
            rlContent.setVisibility(View.VISIBLE);
        }
        if (intros.isCollected()) {
            navLike.setImageResource(R.drawable.collect_fav_highlight);
        } else {
            navLike.setImageResource(R.drawable.collect_fav);
        }
        String drinks = intros.getFeaturedDrink();
        if (drinks != null) {
            String[] drinkArr = drinks.split(",");
            /**zune:
             * 1 进口啤酒
             * 2 葡萄酒
             * 3 白兰地
             * 4 威士忌
             * 5 鸡尾酒
             * 6 精酿啤酒
             * 7 葡萄酒
             * 15 清酒
             * 16 进口啤酒
             * 17 香槟
             * 18 白兰地
             * **/
            llGoods.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i < drinkArr.length; i++) {
                switch (drinkArr[i]) {
                    case "1":
                        addView("进口啤酒", R.drawable.drink_16);
                        break;
                    case "2":
                        addView("葡萄酒", R.drawable.drink_7);
                        break;
                    case "3":
                        addView("白兰地", R.drawable.drink_18);
                        break;
                    case "4":
                        addView("威士忌", R.drawable.drink_4);
                        break;
                    case "5":
                        addView("鸡尾酒", R.drawable.drink_5);
                        break;
                    case "6":
                        addView("精酿啤酒", R.drawable.drink_6);
                        break;
                    case "7":
                        addView("葡萄酒", R.drawable.drink_7);
                        break;
                    case "15":
                        addView("清酒", R.drawable.drink_15);
                        break;
                    case "16":
                        addView("进口啤酒", R.drawable.drink_16);
                        break;
                    case "17":
                        addView("香槟", R.drawable.drink_17);
                        break;
                    case "18":
                        addView("白兰地", R.drawable.drink_18);
                        break;
                    default:
                        break;
                }
            }
        }

        int radius = 8;
        int margin = 8;
        Glide.with(LiveActivity.this)
                .load(intros.getBannerPhoto())
                .dontAnimate()
                .bitmapTransform(new BlurTransformation(this, radius, margin))
                .into(ivBg);
        Glide.with(LiveActivity.this)
                .load(intros.getSponsorThumb())
                .dontAnimate()
                .into(civThumb);
        tvEnterChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(LiveActivity.this, UseLoginActivity.class);
                    return;
                }
                if (!TextUtils.isEmpty(roomID)) {
                    ChatGroupAdd param = new ChatGroupAdd();
                    param.setGroupId(roomID);
                    param.setUserId("" + MoreUser.getInstance().getUid());
                    ((LiveDataLoader) mPresenter).onAddUserChatGroup(param);
                } else {
                    Toast.makeText(LiveActivity.this, "网络异常,请重试", Toast.LENGTH_SHORT).show();
                    ((LiveDataLoader) mPresenter).onLoadLiveDetail(actId, MoreUser.getInstance().getUid());
                }
            }
        });
    }

    /**
     * 进聊天室之前
     */
    @Override
    public void onAddUserChatGroupResponse(RespDto<Boolean> response) {

        Boolean result = response.getData();
        if (!result) {
            Toast.makeText(this, "聊天室人数已满", Toast.LENGTH_SHORT).show();
        } else {
            ((LiveDataLoader) mPresenter).loadRoomHxDetails(roomID);
        }
    }

    @Override
    public void onAddUserChatGroupFaiure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(LiveActivity.this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(this, "聊天室人数已满", Toast.LENGTH_SHORT).show();

    }

    /**
     * 进聊天室
     *
     * @param response
     */
    @Override
    public void onRoomHxDetailsResponse(RespDto<HxRoomDetails> response) {

        HxRoomDetails result = response.getData();
        if (result != null && !TextUtils.isEmpty(result.getHxId())) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    LiveActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(LiveActivity.this, ChatActivity.class);
            intent.putExtra("userId", "" + result.getHxId());
            intent.putExtra("toChatUserID", "" + result.getHxId());
            intent.putExtra("toChatNickName", result.getGroupName());
            intent.putExtra("toChatHeaderUrl", "");
            intent.putExtra("groupHeaderName", result.getGroupName());
            intent.putExtra("groupHeaderUrl", result.getThumbnail());
            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
            ActivityCompat.startActivity(LiveActivity.this, intent, compat.toBundle());
        }

    }

    @Override
    public void onRoomHxDetailsFaiure(String msg) {

    }

    /**
     * zune:将文本末尾添加图片
     **/
    private void setTexts(TextView tvWhere, int address_activedetail, String str) {
        Drawable drawable = getResources().getDrawable(address_activedetail);
        int drawableMinimumWidth = drawable.getMinimumWidth();
        int minimumHeight = drawable.getMinimumHeight();
        drawable.setBounds(0, 0, drawableMinimumWidth, minimumHeight);
        ImageSpan is = new StickerSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
        int strLength = str.length();
        SpannableString ss = new SpannableString(str);
        ss.setSpan(is, strLength - 1, strLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvWhere.setText(ss.subSequence(0, strLength));
    }

    private void addView(String text, int res) {
        View good = View.inflate(LiveActivity.this, R.layout.live_activity_goods, null);
        ImageView ivGood = (ImageView) good.findViewById(R.id.iv_good);
        TextView tvGood = (TextView) good.findViewById(R.id.tv_good);
        ivGood.setImageResource(res);
        tvGood.setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout
                .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        good.setLayoutParams(lp);
        llGoods.addView(good);
    }

    @Override
    public void onLiveDetailResponse(RespDto<ActivityDetail> body) {
        /**zune:处理富文本**/
        ActivityDetail detail = body.getData();
        mid = detail.getMid();
        roomID = detail.getRoom();
    }

    @Override
    public void onLiveDetailFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onLiveIntrosResponse(RespDto<ActivityIntros> body) {
        /**zune:处理其他数据**/
        intros = body.getData();
        rlContent.setVisibility(View.GONE);
        setupView(intros);
    }

    @Override
    public void onLiveIntrosFailure(String msg) {
        Log.i("zune:", "intros = msg = " + msg);
    }

    @Override
    public void onMessageWallResponse(RespDto<List<MessageWall>> body) {
        if (mMessageWallDatas.size() > 0) {
            mMessageWallDatas.clear();
        }
        mMessageWallDatas = body.getData();
        if (mMessageWallDatas == null || mMessageWallDatas.size() == 0) {
            mMessageWallDatas = new ArrayList<>();
            addWall.setVisibility(View.VISIBLE);
        }
        if (adapter == null && isFirst) {
            isFirst = false;
            adapter = new MessageWallAdapter(LiveActivity.this, R.layout.message_wall_item, mMessageWallDatas, "out");
            xrvMessageWall.setAdapter(adapter);
        } else if (adapter != null && mMessageWallDatas != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageWallFailure(String msg) {
        Log.i("zune:", "walls = msg = " + msg);
    }


    @OnClick({R.id.civ_thumb, R.id.tv_from, R.id.iv_from, R.id.tv_where, R.id.iv_where
            , R.id.nav_back, R.id.nav_share_btn, R.id.nav_right_btn
            , R.id.xrv_message_wall, R.id.view_add_message_wall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_add_message_wall:
            case R.id.xrv_message_wall:
                if (MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(LiveActivity.this, UseLoginActivity.class);
                } else {
                    //Todo 进入留言墙
                    ActivityOptionsCompat compat_write = ActivityOptionsCompat.makeCustomAnimation(
                            LiveActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_write = new Intent(LiveActivity.this, MessageWallActivity.class);
                    intent_write.putExtra(Constants.KEY_ACT_ID, actId);
                    ActivityCompat.startActivity(LiveActivity.this, intent_write, compat_write.toBundle());
                }
                break;
            case R.id.civ_thumb:
                //Todo 商家主页入口
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        LiveActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(LiveActivity.this, UserDetailV2Activity.class);
                intent_merchant.putExtra("mid", mid);
                intent_merchant.putExtra("toUid", intros.getSponsor() + "");
                ActivityCompat.startActivity(LiveActivity.this, intent_merchant, compat_merchant.toBundle());
                break;
            case R.id.tv_from:
            case R.id.iv_from:
                int type = intros.getType();
                if (type == 5 || type == 6)
                    break;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        LiveActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(LiveActivity.this, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", mid + "");
                ActivityCompat.startActivity(LiveActivity.this, intent, compat.toBundle());
                break;
            case R.id.tv_where:
            case R.id.iv_where:
                if (intros != null) {
                    Intent intent_where = new Intent(LiveActivity.this,
                            MerchantMapActivity.class);
                    intent_where.putExtra("toLat", Double.valueOf(intros.getLat()));
                    intent_where.putExtra("toLng", Double.valueOf(intros.getLng()));
                    intent_where.putExtra("merchantName", intros.getMerchant());
                    intent_where.putExtra("merchantAddress", intros.getAddr());
                    LiveActivity.this.startActivity(intent_where);
                }
                break;
            case R.id.nav_back:
                backClick();
                break;
            case R.id.nav_share_btn:
                //Todo 收藏功能
                UserCollect.CollectCallBack callback = new UserCollect.CollectCallBack() {
                    @Override
                    public void collectSuccess() {
                        if (intros.isCollected()) {
                            intros.setCollected(false);
                        } else {
                            intros.setCollected(true);
                            navLike.setImageResource(R.drawable.collect_fav_highlight);
                        }
                        animateHeartButton(navLike, intros);
                    }

                    @Override
                    public void collectFails() {

                    }
                };
                String relationID = intros.getActivityId() + "";
                new UserCollect(LiveActivity.this, relationID,
                        UserCollect.COLLECT_TYPE.ACTIVITY_COLLECT.ordinal(), intros.isCollected(), callback);
                break;
            case R.id.nav_right_btn:
                //Todo 分享功能
                showShareView();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    private void backClick() {
        Intent intent = getIntent();
        boolean isFromAdert = intent.getBooleanExtra("isFromAdert", false);
        if (isFromAdert) {
            AppUtils.pushLeftActivity(this, SuperMainActivity.class);
            finish();
        } else {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    }

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

            int width = ScreenUtil.getScreenWidth(LiveActivity.this);

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

    /**
     * zune:html适配
     **/
    private StringBuilder initWebViewSetting(String data) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setSaveEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
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
        html.append(data);
        html.append("</div>");
        html.append("</body>");
        html.append("<script type=\"text/javascript\"> window.onload = function imgcenter() " +
                "{ var box = document.getElementsByClassName(\"box3\"); " +
                "var img = box[0].getElementsByTagName(\"img\"); " +
                "for(i = 0; i < img.length; i++) { " +
                "var imgParentNodes = img[i].parentNode;console.log(imgParentNodes) " +
                "imgParentNodes.style.textIndent = 'inherit';}}</script>");
        html.append("</html>");
        return html;
    }

    private String formatTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        String minute = format.format(date);
        return minute;
    }

    private String formatDay(int holdingType, String holdingTimes, Long time) {
        if (holdingType == 2) {
            String[] split = holdingTimes.split(",");
            StringBuffer sb = new StringBuffer("每");
            for (int i = 0; i < split.length; i++) {
                if (i < split.length - 1)
                    sb.append("周" + split[i] + ",");
                else
                    sb.append("周" + split[i]);
            }
            return sb.toString();
        } else if (holdingType == 1) {
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日 EE ");
            Date date = new Date(time);
            String day = format.format(date);
            return day;
        }
        return "";
    }

    private void animateHeartButton(final ImageView btnLike, final ActivityIntros item) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(btnLike, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(btnLike, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(btnLike, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (item.isCollected()) {
                    btnLike.setImageResource(R.drawable.collect_fav_highlight);
                } else {
                    btnLike.setImageResource(R.drawable.collect_fav);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null)
            client = null;
        if (callbacks != null)
            callbacks = null;
    }

    private class StickerSpan extends ImageSpan {
        public StickerSpan(Drawable b, int verticalAlignment) {
            super(b, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();
            int transY = (int) (bottom - b.getBounds().bottom - ScreenUtil.dp2px(LiveActivity.this, 4));// 减去行间距
            if (mVerticalAlignment == ALIGN_BASELINE) {
                int textLength = text.length();
                for (int i = 0; i < textLength; i++) {
                    if (Character.isLetterOrDigit(text.charAt(i))) {
                        transY -= paint.getFontMetricsInt().descent;
                        break;
                    }
                }
            }
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }

    private String cutString(TextView view, String text, int offset) {
        String subString;
        int textWidth = (int) getTextWidth(view, text.toString());
        if (textWidth >= ScreenUtil.getScreenWidth(this) - offset) {
            String cutString = cutString(view, text.substring(0, text.length() - 1), offset);
            return cutString;
        } else {
            subString = text;
            return subString;
        }
    }

    public float getTextWidth(TextView view, String text) {
        TextPaint paint = view.getPaint();
        if (text == null)
            return -1;
        return paint.measureText(text);
    }
}
