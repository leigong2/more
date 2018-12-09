package com.moreclub.moreapp.main.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.model.MChannelGood;
import com.moreclub.moreapp.main.model.MChannelGoodUser;
import com.moreclub.moreapp.main.model.ReplyClickListener;
import com.moreclub.moreapp.main.model.UGCChannel;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.presenter.IMChannelDetailsLoader;
import com.moreclub.moreapp.main.presenter.MChannelDetailsLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelDetailsSimpleAdapter;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.ShareUtils;
import com.moreclub.moreapp.util.UserCollect;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelDetailsAcitivy extends BaseListActivity implements
        IMChannelDetailsLoader.LoadMChannelDetailsBinder, ReplyClickListener {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    @BindView(R.id.nav_back)
    ImageButton navBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.channel_header_img)
    CircleImageView channelHeaderImg;
    @BindView(R.id.channel_name)
    TextView channelName;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    @BindView(R.id.mchannel_lay)
    LinearLayout mchannelLay;
    @BindView(R.id.bottom_comment_count)
    TextView bottomCommentCount;
    @BindView(R.id.comment_lay)
    LinearLayout commentLay;
    @BindView(R.id.iv_good)
    ImageView ivGood;
    @BindView(R.id.bottom_good_count)
    TextView bottomGoodCount;
    @BindView(R.id.like_lay)
    LinearLayout likeLay;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.collect_lay)
    LinearLayout collectLay;
    @BindView(R.id.btn_share)
    ImageView btnShare;
    @BindView(R.id.share_lay)
    LinearLayout shareLay;
    @BindView(R.id.et_write_comment)
    EditText etWriteComment;
    @BindView(R.id.commentList)
    XRecyclerView commentList;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.ll_foot)
    LinearLayout llFoot;
    @BindView(R.id.divider)
    TextView divider;
    private Headline.HeadlineDetail mDetail;
    private int mPn = 1;
    private int mGoodPn = 1;
    private int mPs = 10;
    private int mGoodPs = 10;
    private int sid;
    private View popupShareView;
    private PopupWindow mShareWindow;
    private List<HeadlineComment> mCommentList;
    private List<MChannelGoodUser> mGoodList;
    private boolean showComments;
    private boolean showGoods;
    private long oldTime;
    private long nowTime;
    private HeadlineSendComment mSendComment;
    private int totalGood;
    private int webHeight;
    private HeadlineComment headlineComment;
    private int commentCount;
    private LinearLayout header;
    private JCVideoPlayer videoplayer;
    private FrameLayout flVideo;
    private WebView channelWebview;
    private ImageView ivHeadlineDetailTop;
    private TextView tvTitle;
    private TextView comment;
    private TextView commentLine;
    private TextView good;
    private TextView goodLine;
    private List<Object> mDatas;
    private MChannelDetailsSimpleAdapter mAdapter;
    private int pagesGood;
    private List<String> userName;

    /**
     * zune:网页加载监听
     **/
    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbLoading.setVisibility(View.GONE);
            commentList.setVisibility(View.VISIBLE);
            llFoot.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            webHeight = getViewWidthAndHeight(channelWebview)[1];
            Log.i("zune:", "webHeight = " + webHeight);

        }
    };
    private View.OnClickListener weixinFriendShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                return;
            }
            ShareUtils share = new ShareUtils(MChannelDetailsAcitivy.this, 1, 1, 1, mDetail.getSid() + "");
            share.share();
        }
    };
    private View.OnClickListener weixinGroupShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                return;
            }
            ShareUtils share = new ShareUtils(MChannelDetailsAcitivy.this, 0, 1, 1, mDetail.getSid() + "");
            share.share();
        }
    };
    private View.OnClickListener weiboShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                return;
            }
            ShareUtils share = new ShareUtils(MChannelDetailsAcitivy.this, 0, 2, 1, mDetail.getSid() + "");
            share.share();
        }
    };
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!TextUtils.isEmpty(etWriteComment.getText().toString().trim())) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    sendComment(etWriteComment.getText().toString().trim());
                    etWriteComment.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MChannelDetailsAcitivy.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    inputMethodManager.showSoftInput(etWriteComment, InputMethodManager.RESULT_SHOWN);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                    etWriteComment.setFocusable(true);
                    etWriteComment.setFocusableInTouchMode(true);
                    etWriteComment.requestFocus();
                }
                etWriteComment.setText("");
                return true;
            }
            return false;
        }
    };
    private View.OnClickListener commentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Todo 切换评论
            mDatas.clear();
            mDatas.addAll(mCommentList);
            good.setTextColor(Color.parseColor("#999999"));
            comment.setTextColor(Color.parseColor("#333333"));
            commentLine.setVisibility(View.VISIBLE);
            goodLine.setVisibility(View.GONE);
            if (!showComments) {
                mDatas.clear();
                mDatas.add(true);
            }
            mAdapter.notifyDataSetChanged();
        }
    };
    private View.OnClickListener goodClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Todo 切换到点赞
            mDatas.clear();
            mDatas.addAll(mGoodList);
            etWriteComment.setVisibility(View.GONE);
            good.setTextColor(Color.parseColor("#333333"));
            comment.setTextColor(Color.parseColor("#999999"));
            commentLine.setVisibility(View.GONE);
            goodLine.setVisibility(View.VISIBLE);
            if (!showGoods) {
                mDatas.clear();
                mDatas.add(false);
            }
            mAdapter.notifyDataSetChanged();
        }
    };
    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null && s.toString().split("：") != null && s.toString().split("：").length > 1) {
                etWriteComment.setTextColor(Color.parseColor("#333333"));
            } else if (s != null && !s.toString().contains("：")) {
                etWriteComment.setTextColor(Color.parseColor("#333333"));
            } else {
                etWriteComment.setTextColor(Color.parseColor("#999999"));
            }
            etWriteComment.setSelection(etWriteComment.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.mchannel_details_two_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IMChannelDetailsLoader.class;
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
        setupViews();
        initData();
    }

    @Override
    protected void onReloadData() {
        if (sid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelDetailsLoader) mPresenter).onLoadMChannelDetails(sid, MoreUser.getInstance().getUid());
        }
    }

    private void initData() {
        oldTime = System.currentTimeMillis();
        sid = getIntent().getIntExtra("sid", -1);
        if (sid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelDetailsLoader) mPresenter).onLoadMChannelDetails(sid, MoreUser.getInstance().getUid());
        }
    }

    private void setupViews() {
        mCommentList = new ArrayList<>();
        mGoodList = new ArrayList<>();
        mDatas = new ArrayList<>();
        userName = new ArrayList<>();
        pbLoading.setVisibility(View.VISIBLE);
        commentList.setVisibility(View.GONE);
        llFoot.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
        pbLoading.setTranslationY(ScreenUtil.getScreenHeight(this) / 2 - ScreenUtil.dp2px(this, 56));
        etWriteComment.setOnKeyListener(onKeyListener);
        etWriteComment.addTextChangedListener(textChangeListener);
        etWriteComment.setTextColor(Color.parseColor("#999999"));
        initHeader();
        mAdapter = new MChannelDetailsSimpleAdapter(this, mDatas, sid, userName);
        LinearLayoutManager lm = new LinearLayoutManager(MChannelDetailsAcitivy.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        commentList.setLayoutManager(lm);
        commentList.setAdapter(mAdapter);
        commentList.setPullRefreshEnabled(false);
        commentList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (commentLine.getVisibility() == View.VISIBLE) {
                    mPn++;
                    if (sid > 0 && MoreUser.getInstance().getUid() != null) {
                        ((MChannelDetailsLoader) mPresenter).onLoadCommentList(sid, mPn, mPs
                                , MoreUser.getInstance().getUid());
                    } else {
                        onLoadComplete(mPn);
                    }
                } else if (goodLine.getVisibility() == View.VISIBLE) {
                    mGoodPn++;
                    if (mGoodPn <= pagesGood && sid > 0 && MoreUser.getInstance().getUid() != null) {
                        ((MChannelDetailsLoader) mPresenter).onLoadGoodList(sid, mGoodPn, mGoodPs);
                    } else {
                        onLoadComplete(mGoodPn);
                    }
                }
            }
        });
    }

    private void initHeader() {
        header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.channel_detail_header, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(lp);
        flVideo = (FrameLayout) header.findViewById(R.id.fl_video);
        videoplayer = (JCVideoPlayer) header.findViewById(R.id.videoplayer);
        ivHeadlineDetailTop = (ImageView) header.findViewById(R.id.iv_headline_detail_top);
        tvTitle = (TextView) header.findViewById(R.id.tv_title);
        channelWebview = (WebView) header.findViewById(R.id.channelWebview);
        channelWebview.setWebViewClient(client);
        comment = (TextView) header.findViewById(R.id.comment);
        comment.setOnClickListener(commentClickListener);
        commentLine = (TextView) header.findViewById(R.id.comment_line);
        good = (TextView) header.findViewById(R.id.good);
        good.setOnClickListener(goodClickListener);
        goodLine = (TextView) header.findViewById(R.id.good_line);
        commentList.addHeaderView(header);
    }

    @Override
    public void onGetMChannelDetailsResponse(RespDto<Headline.HeadlineDetail> response) {
        if (response.getData() == null) return;
        mDetail = response.getData();
        if (userName != null && userName.size() == 0)
            userName.add(mDetail.getUsername());
        commentCount = mDetail.getConmentsCount();
        if (!TextUtils.isEmpty(mDetail.getMediaUrl())) {
            videoplayer.setVisibility(View.VISIBLE);
            ivHeadlineDetailTop.setVisibility(View.GONE);
            videoplayer.setUp(mDetail.getMediaUrl(), mDetail.getTitle());
            ImageView ivThumb = videoplayer.ivThumb;
            ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this)
                    .load(mDetail.getTitlePicture())
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivThumb);
        } else {
            ivHeadlineDetailTop.setVisibility(View.VISIBLE);
            videoplayer.setVisibility(View.GONE);
        }
        if (mDetail == null) return;
        if (mDetail.getTextType() == null) {
            StringBuilder html = initWebViewSetting(mDetail.getContent());
            channelWebview.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", null);
        } else if (mDetail.getTextType().equals(2)) {
            WebSettings settings = channelWebview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            channelWebview.loadUrl(mDetail.getContent());
            flVideo.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        } else {
            WebSettings settings = channelWebview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            channelWebview.loadUrl(mDetail.getContent());
            flVideo.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        }
        tvTitle.setText(mDetail.getTitle());
        if (mDetail.getColloct()) {
            btnCollect.setImageResource(R.drawable.collect_fav_highlight);
        } else {
            btnCollect.setImageResource(R.drawable.channel_fav_black);
        }
        if (TextUtils.isEmpty(mDetail.getClicked())) {
            ivGood.setImageResource(R.drawable.like);
        } else {
            ivGood.setImageResource(R.drawable.like2);
        }
        channelName.setText(mDetail.getUsername());
        String userThumbUrl = mDetail.getUserthumb();
        if (userThumbUrl != null)
            ivGo.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(userThumbUrl)
                .dontAnimate()
                .into(channelHeaderImg);
        channelHeaderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        MChannelDetailsAcitivy.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(MChannelDetailsAcitivy.this, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", "" + mDetail.getUid());
                ActivityCompat.startActivity(MChannelDetailsAcitivy.this, intent_merchant, compat_merchant.toBundle());
            }
        });
        Glide.with(this)
                .load(mDetail.getTitlePicture())
                .dontAnimate()
                .into(ivHeadlineDetailTop);
        loadComment(mDetail.getSid());
        loadGood(mDetail.getSid());
    }

    @Override
    public void onGetMChannelDetailsFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
        onLoadComplete(mPn);
    }

    @Override
    public void onGetUgcDetailsResponse(RespDto<UGCChannel> response) {

    }

    @Override
    public void onGetUgcDetailsFailure(String msg) {

    }

    @Override
    public void onGetCommentListResponse(RespDto<List<HeadlineComment>> response) {
        if (response == null || response.getData() == null) return;
        initCommentList(response.getData());
    }

    @Override
    public void onGetCommentListFailure(String msg) {
        Log.i("zune:", "onGetCommentListFailure msg = " + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        onLoadComplete(mPn);
    }

    @Override
    public void onGetGoodListResponse(RespDto<MChannelGood> response) {
        if (response == null || !response.isSuccess()) return;
        MChannelGood data = response.getData();
        if (data == null || data.getList() == null) {
            return;
        } else {
            totalGood = response.getData().getTotal();
            pagesGood = response.getData().getPages();
            initGoodList(response.getData());
        }
    }

    @Override
    public void onGetGoodListFailure(String msg) {
        Logger.i("zune:", "goodlist msg = " + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        onLoadComplete(mGoodPn);
    }

    @Override
    public void onSetResponse(RespDto<Boolean> response) {
        if (response == null || response.getData() == null) return;
        if (response.getData()) {
            int count = Integer.parseInt(TextUtils.isEmpty(bottomGoodCount.getText().toString())
                    ? "0" : bottomGoodCount.getText().toString()) + 1;
            bottomGoodCount.setText(count + "");
            ivGood.setImageResource(R.drawable.like2);
            bottomGoodCount.setTextColor(Color.parseColor("#FFCC00"));
            Toast.makeText(MChannelDetailsAcitivy.this, "点赞成功", Toast.LENGTH_SHORT).show();
            MChannelGoodUser user = new MChannelGoodUser();
            if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                Long uid = MoreUser.getInstance().getUid();
                user.setUid(uid);
                user.setNickName(MoreUser.getInstance().getNickname());
                user.setThumb(MoreUser.getInstance().getThumb());
                mGoodList.add(0, user);
            }
            if (count == 0) {
                showGoods = false;
            } else {
                showGoods = true;
            }
            mDatas.clear();
            mDatas.addAll(mGoodList);
            mAdapter.notifyDataSetChanged();
        } else {
            int count = Integer.parseInt(TextUtils.isEmpty(bottomGoodCount.getText().toString())
                    ? "0" : bottomGoodCount.getText().toString()) - 1;
            bottomGoodCount.setText(count > 0 ? count + "" : "");
            ivGood.setImageResource(R.drawable.like);
            bottomGoodCount.setTextColor(Color.parseColor("#999999"));
            for (int i = 0; i < mGoodList.size(); i++) {
                if (mGoodList.get(i).getUid() == MoreUser.getInstance().getUid()) {
                    mGoodList.remove(i);
                    break;
                }
            }
            if (count == 0) {
                showGoods = false;
            } else {
                showGoods = true;
            }
            if (mGoodList.size() == 0) {
                mDatas.clear();
                mDatas.add(false);
            } else {
                mDatas.clear();
                mDatas.addAll(mGoodList);
            }
            mAdapter.notifyDataSetChanged();
            Toast.makeText(MChannelDetailsAcitivy.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
        }
        MChannelCommentEvent event = new MChannelCommentEvent();
        event.setClicked(response.getData());
        event.setSid(sid);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onSetFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
        Toast.makeText(MChannelDetailsAcitivy.this, "点赞失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetPersonGoodResponse(RespDto<Boolean> response) {

    }

    @Override
    public void onSetPersonGoodFailure(String msg) {

    }

    @Override
    public void onSendCommentResponse(RespDto<String> response) {
        if (response != null && response.getData() != null && response.isSuccess()) {
            HeadlineComment comment = new HeadlineComment();
            if (mSendComment.getToUserId() > 0)
                comment.setToUserId(mSendComment.getToUserId());
            comment.setContent(mSendComment.getContent());
            comment.setCreateTime(System.currentTimeMillis());
            comment.setFromNickname(MoreUser.getInstance().getNickname());
            comment.setToNickName(mSendComment.getToNickName());
            comment.setFromUserThumb(MoreUser.getInstance().getThumb());
            comment.setToUserThumb(mDetail.getUserthumb());
            comment.setUserId(mSendComment.getUid());
            comment.setClicked(false);
            int cid;
            try {
                cid = Integer.parseInt(response.getData());
            } catch (Exception e) {
                Logger.i("zune:", "e = " + e);
                cid = 0;
            }
            comment.setCid(cid);
            mCommentList.add(0, comment);
            bottomCommentCount.setText(TextUtils.isEmpty(bottomCommentCount.getText().toString()) ?
                    "1" : Integer.parseInt(bottomCommentCount.getText().toString()) + 1 + "");
            showComments = true;
            mDatas.clear();
            mDatas.addAll(mCommentList);
            Toast.makeText(MChannelDetailsAcitivy.this, "发布成功", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
            if (commentLine.getVisibility() == View.VISIBLE && mCommentList.size() > 0)
                commentList.scrollToPosition(2);
            MChannelCommentEvent event = new MChannelCommentEvent();
            event.setComment(comment);
            event.setSid(sid);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onSendCommentFailure(String msg) {
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        Logger.i("zune:", "onSendCommentFailure msg = " + msg);
    }

    /**
     * zune:加载评论
     **/
    private void loadComment(Integer sid) {
        if (sid != null && sid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelDetailsLoader) mPresenter).onLoadCommentList(sid, mPn, mPs, MoreUser.getInstance().getUid());
        }
    }

    private void loadGood(Integer fid) {
        if (fid != null && fid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelDetailsLoader) mPresenter).onLoadGoodList(fid, mGoodPn, mGoodPs);
        }
    }

    /**
     * zune:点赞活动
     **/
    private void setGood(Integer sid) {
        Long uid = MoreUser.getInstance().getUid();
        if (sid > 0 && uid != null) {
            if (uid.equals(0L)) {
                AppUtils.pushLeftActivity(getApplicationContext(), UseLoginActivity.class);
            } else
                ((MChannelDetailsLoader) mPresenter).onSetGood(MoreUser.getInstance().getAccess_token()
                        , MoreUser.getInstance().getUid(), sid, "likeTime1");
        }
    }

    /**
     * zune:初始化评论列表
     **/
    private void initCommentList(List<HeadlineComment> comments) {
        onLoadComplete(mPn);
        mCommentList.addAll(comments);
        if (commentLine.getVisibility() == View.VISIBLE) {
            mDatas.clear();
            if (mCommentList.size() == 0) {
                mDatas.add(true);
            } else
                mDatas.addAll(mCommentList);
        }
        if (commentCount == 0) {
            bottomCommentCount.setText("");
        } else
            bottomCommentCount.setText("" + commentCount);
        if (mCommentList.size() == 0) {
            showComments = false;
        } else {
            showComments = true;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void sendComment(String comment) {
        //Todo 发评论
        if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
            AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
            return;
        }
        mSendComment = new HeadlineSendComment();
        String version = Constants.APP_version;
        mSendComment.setAppVersion(version);
        mSendComment.setTimestamp((int) (System.currentTimeMillis() / 1000));
        mSendComment.setUid(MoreUser.getInstance().getUid());
        String hint = etWriteComment.getHint().toString();
        if (TextUtils.equals(hint, "留个言评论下吧……")) {
            mSendComment.setContent(comment);
            mSendComment.setPostId(sid);
            mSendComment.setToNickName("");
        } else {
            try {
                mSendComment.setContent(comment);
                mSendComment.setPostId(sid);
                mSendComment.setToNickName(headlineComment.getFromNickname());
                mSendComment.setToUserId(headlineComment.getUserId());
            } catch (Exception e) {
                mSendComment.setContent(comment);
                mSendComment.setPostId(sid);
            }
        }
        ((MChannelDetailsLoader) mPresenter).onSendComment(MoreUser.getInstance().getAccess_token(), mSendComment);
    }

    /**
     * zune:初始化点赞列表
     **/
    private void initGoodList(MChannelGood data) {
        onLoadComplete(mGoodPn);
        mGoodList.addAll(data.getList());
        if (goodLine.getVisibility() == View.VISIBLE) {
            mDatas.clear();
            if (mGoodList.size() == 0) {
                mDatas.add(false);
            } else
                mDatas.addAll(mGoodList);
        }
        bottomGoodCount.setText(totalGood == 0 ? "" : totalGood + "");
        if (mGoodList.size() == 0) {
            showGoods = false;
        } else {
            showGoods = true;
        }
        if (goodLine.getVisibility() == View.GONE) return;
        mAdapter.notifyDataSetChanged();
    }

    private void onLoadComplete(int index) {
        if (index == 1) {
            if (mCommentList != null) {
                commentList.refreshComplete();
            }
        } else {
            commentList.loadMoreComplete();
        }
    }

    private void animateHeartButton(final ImageView btnCollect, final Headline.HeadlineDetail item) {
        if (mDetail == null) return;
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(btnCollect, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(btnCollect, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(btnCollect, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (item.getColloct()) {
                    btnCollect.setImageResource(R.drawable.collect_fav_highlight);
                } else {
                    btnCollect.setImageResource(R.drawable.channel_fav_black);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
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

            int width = ScreenUtil.getScreenWidth(MChannelDetailsAcitivy.this);

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(MChannelDetailsAcitivy.this,
                        v.getWindowToken());
                etWriteComment.setHint("留个言评论下吧……");
                etWriteComment.setVisibility(View.GONE);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * zune:html适配
     **/
    private StringBuilder initWebViewSetting(String data) {
        WebSettings settings = channelWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        channelWebview.setSaveEnabled(false);
        channelWebview.setWebChromeClient(new WebChromeClient());
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

    private int[] getViewWidthAndHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(width, height);

        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    @Override
    public void onClickReplyListener(HeadlineComment comment) {
        etWriteComment.setVisibility(View.VISIBLE);
        if (comment.getUserId() != MoreUser.getInstance().getUid()) {
            etWriteComment.setHint("回复 " + comment.getFromNickname() + "：");
            headlineComment = comment;
        } else {
            etWriteComment.setHint("留个言评论下吧……");
            this.headlineComment = null;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        etWriteComment.setFocusable(true);
        etWriteComment.setFocusableInTouchMode(true);
        etWriteComment.requestFocus();
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

    @OnClick({R.id.nav_back, R.id.iv_go, R.id.mchannel_lay, R.id.et_write_comment
            , R.id.comment_lay, R.id.like_lay, R.id.collect_lay, R.id.share_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                backClick();
                break;
            case R.id.et_write_comment:
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                    return;
                }
                break;
            case R.id.mchannel_lay:
                //Todo 进入主页
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                    return;
                }
                if (mDetail == null || mDetail.getUid() <= 0) break;
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        MChannelDetailsAcitivy.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(MChannelDetailsAcitivy.this, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", mDetail.getUid() + "");
                intent_merchant.putExtra("isChannel", "true");
                ActivityCompat.startActivity(MChannelDetailsAcitivy.this, intent_merchant, compat_merchant.toBundle());
                break;
            case R.id.comment_lay:
                //Todo 评论
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                } else {
                    if (commentLine.getVisibility() == View.GONE) {
                        mDatas.clear();
                        mDatas.addAll(mCommentList);
                        good.setTextColor(Color.parseColor("#999999"));
                        comment.setTextColor(Color.parseColor("#333333"));
                        commentLine.setVisibility(View.VISIBLE);
                        goodLine.setVisibility(View.GONE);
                        if (!showComments) {
                            mDatas.clear();
                            mDatas.add(true);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    etWriteComment.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    etWriteComment.setFocusable(true);
                    etWriteComment.setFocusableInTouchMode(true);
                    etWriteComment.requestFocus();
                }
                break;
            case R.id.like_lay:
                //Todo 点赞
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                } else {
                    if (goodLine.getVisibility() == View.GONE) {
                        mDatas.clear();
                        mDatas.addAll(mGoodList);
                        etWriteComment.setVisibility(View.GONE);
                        good.setTextColor(Color.parseColor("#333333"));
                        comment.setTextColor(Color.parseColor("#999999"));
                        commentLine.setVisibility(View.GONE);
                        goodLine.setVisibility(View.VISIBLE);
                        if (!showGoods) {
                            mDatas.clear();
                            mDatas.add(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    nowTime = System.currentTimeMillis();
                    if (nowTime - oldTime > 1000) {
                        oldTime = System.currentTimeMillis();
                        setGood(sid);
                    }
                }
                break;
            case R.id.share_lay:
                //Todo 分享
                showShareView();
                break;
            case R.id.collect_lay:
                //Todo 收藏
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelDetailsAcitivy.this, UseLoginActivity.class);
                    break;
                }
                UserCollect.CollectCallBack callback = new UserCollect.CollectCallBack() {
                    @Override
                    public void collectSuccess() {
                        if (mDetail.getColloct()) {
                            mDetail.setColloct(false);
                        } else {
                            mDetail.setColloct(true);
                            btnCollect.setImageResource(R.drawable.collect_fav_highlight);
                        }
                        animateHeartButton(btnCollect, mDetail);
                    }

                    @Override
                    public void collectFails() {

                    }
                };
                String relationID = mDetail.getSid() + "";
                new UserCollect(MChannelDetailsAcitivy.this, relationID,
                        UserCollect.COLLECT_TYPE.INFORMATION_COLLECT.ordinal(), mDetail.getColloct(), callback);
                break;
        }
    }
}
