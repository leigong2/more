package com.moreclub.moreapp.information.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.ui.view.scrollview.ObservableScrollViewCallbacks;
import com.moreclub.common.ui.view.scrollview.ScrollState;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.information.presenter.HeadlineDetailLoader;
import com.moreclub.moreapp.information.presenter.IHeadlineDetailLoader;
import com.moreclub.moreapp.information.ui.adapter.HeadlineCommentAdapter;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.ShareUtils;
import com.moreclub.moreapp.util.UserCollect;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class HeadlineDetailActivity extends BaseActivity implements IHeadlineDetailLoader.LoadHeadlineDetailBinder {

    @BindView(R.id.nav_back)
    ImageButton navBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.iv_headline_detail_top)
    ImageView ivHeadlineDetailTop;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.wv_detail)
    WebView wvDetail;
    @BindView(R.id.xrv_headline_comment)
    XRecyclerView xrvHeadlineComment;
    @BindView(R.id.et_write_comment)
    EditText etWriteComment;
    @BindView(R.id.nav_share_btn)
    ImageButton btn_like;
    @BindView(R.id.nav_right_btn)
    ImageButton btn_share;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.iv_user_thumb)
    CircleImageView ivUserThumb;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.detailsScrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.pb_headline_detail)
    ProgressBar pbHeadlineDetail;
    @BindView(R.id.pb_play)
    ProgressBar pbPlay;
    @BindView(R.id.tv_no_comment)
    TextView tvNoComment;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.videoplayer)
    JCVideoPlayer videoplayer;

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private int sid;
    private SystemBarTintManager tintManager;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private ObservableScrollViewCallbacks callbacks = new ObservableScrollViewCallbacks() {
        @Override
        public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            int alpha = scrollY * 4 / 10;
            String str = Long.toHexString(alpha).toUpperCase();
            if (alpha >= 16 && alpha < 256) {
                navLayout.setBackgroundColor(Color.parseColor("#" + str + "000000"));
                tintManager.setTintColor(Color.parseColor("#" + str + "000000"));
            } else if (alpha < 16) {
                navLayout.setBackgroundColor(Color.parseColor("#0" + str + "000000"));
                tintManager.setTintColor(Color.parseColor("#0" + str + "000000"));
            }
        }

        @Override
        public void onDownMotionEvent() {

        }

        @Override
        public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        }
    };
    private boolean canScroll = false;
    private RecyclerView.Adapter mCommentAdapter;
    private List<HeadlineComment> mCommentList;
    private Headline.HeadlineDetail mDetail;
    private HeadlineSendComment mSendComment;
    private PopupWindow mShareWindow;
    private View popupShareView;

    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                if (!TextUtils.isEmpty(etWriteComment.getText().toString().trim())) {
                    sendComment(etWriteComment.getText().toString().trim());
                } else {
                    Toast.makeText(HeadlineDetailActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                }
                etWriteComment.setText("");
                return true;
            }
            return false;
        }
    };

    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbHeadlineDetail.setVisibility(View.GONE);
            rlContent.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener weixinFriendShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(HeadlineDetailActivity.this, 1, 1, 1, mDetail.getSid() + "");
            share.share();
        }
    };
    private View.OnClickListener weixinGroupShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(HeadlineDetailActivity.this, 0, 1, 1, mDetail.getSid() + "");
            share.share();
        }
    };
    private View.OnClickListener weiboShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(HeadlineDetailActivity.this, 0, 2, 1, mDetail.getSid() + "");
            share.share();
        }
    };

    private void sendComment(String content) {
        mSendComment = new HeadlineSendComment();
        String version = Constants.APP_version;
        mSendComment.setAppVersion(version);
        mSendComment.setContent(content);
        mSendComment.setPostId(sid);
        mSendComment.setToUserId(mDetail.getUid());
        mSendComment.setTimestamp((int) (System.currentTimeMillis() / 1000));
        mSendComment.setUid(MoreUser.getInstance().getUid());
        ((HeadlineDetailLoader) mPresenter).onSendComment(MoreUser.getInstance().getAccess_token(), mSendComment);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.headline_detail_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IHeadlineDetailLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#00000000"));
        setupView();
    }

    private void setupView() {
        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", -1);
        if (sid != -1) {
            ((HeadlineDetailLoader) mPresenter).onLoadHeadlineDetail(sid, MoreUser.getInstance().getUid());
        }
        navLayout.setBackgroundColor(Color.parseColor("#00000000"));
        scrollView.addScrollViewCallbacks(callbacks);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return canScroll;
            }
        });
        etWriteComment.setOnKeyListener(keyListener);
        wvDetail.setWebViewClient(client);
        if (mDetail != null) {
            String s = mDetail.toString();
            Log.i("zune:", "mDetail = " + s);
        }
    }

    /**
     * zune:html适配
     **/
    private StringBuilder initWebViewSetting(String data) {
        WebSettings settings = wvDetail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvDetail.setSaveEnabled(false);
        wvDetail.setWebChromeClient(new WebChromeClient());
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

    private void loadComment(Integer sid) {
        if (sid != -1) {
            ((HeadlineDetailLoader) mPresenter).onLoadCommentList(sid, pageIndex, pageSize,MoreUser.getInstance().getUid());
        }
    }


    private void onLoadComplete(Integer index) {
        canScroll = false;
        if (index == 1) {
            if (mCommentList != null) {
                mCommentList.clear();
                xrvHeadlineComment.refreshComplete();
            }
        } else {
            xrvHeadlineComment.loadMoreComplete();
        }
    }

    @Override
    public void onGetCommentListResponse(RespDto<List<HeadlineComment>> response) {
        onLoadComplete(pageIndex);
        mCommentList = response.getData();
        if (mCommentList.size() == 0) {
            xrvHeadlineComment.setVisibility(View.GONE);
            tvNoComment.setVisibility(View.VISIBLE);
            return;
        } else {
            xrvHeadlineComment.setVisibility(View.VISIBLE);
            tvNoComment.setVisibility(View.GONE);
        }
        mCommentAdapter = new HeadlineCommentAdapter(HeadlineDetailActivity.this, R.layout.headline_comment_item, mCommentList, sid);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(HeadlineDetailActivity.this);
        xrvHeadlineComment.setLayoutManager(lm);
        xrvHeadlineComment.setAdapter(mCommentAdapter);
        xrvHeadlineComment.setPullRefreshEnabled(false);
        xrvHeadlineComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        xrvHeadlineComment.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                canScroll = true;
                pageIndex = 1;
                ((HeadlineDetailLoader) mPresenter).onLoadCommentList(sid, pageIndex, pageSize,MoreUser.getInstance().getUid());
            }

            @Override
            public void onLoadMore() {
                canScroll = true;
                pageIndex++;
                if (pageIndex < mCommentList.size() / pageSize + 1) {
                    ((HeadlineDetailLoader) mPresenter).onLoadCommentList(sid, pageIndex, pageSize,MoreUser.getInstance().getUid());
                } else if (pageIndex == mCommentList.size() / pageSize + 1) {
                    int pageSize = mCommentList.size() % HeadlineDetailActivity.this.pageSize;
                    ((HeadlineDetailLoader) mPresenter).onLoadCommentList(sid, pageIndex, pageSize,MoreUser.getInstance().getUid());
                } else {
                    Toast.makeText(HeadlineDetailActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    onLoadComplete(pageIndex);
                }
            }
        });
    }

    @Override
    public void onGetCommentListFailure(String msg) {
        Logger.i("zune:", "msg= " + msg);
    }

    @Override
    public void onGetHeadlineDetailResponse(RespDto<Headline.HeadlineDetail> response) {
        mDetail = response.getData();
        if (mDetail.getMediaUrl() != null) {
            videoplayer.setVisibility(View.VISIBLE);
            ivHeadlineDetailTop.setVisibility(View.GONE);
            videoplayer.setUp(mDetail.getMediaUrl(),mDetail.getTitle());
            ImageView ivThumb = videoplayer.ivThumb;
            ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this)
                    .load(mDetail.getTitlePicture())
                    .dontAnimate()
                    .into(ivThumb);
        } else {
            ivHeadlineDetailTop.setVisibility(View.VISIBLE);
            videoplayer.setVisibility(View.GONE);
        }
        tvCreateTime.setText(format(mDetail.getCreateTime()));
        tvTitle.setText(mDetail.getTitle());
        btn_like.setVisibility(View.VISIBLE);
        btn_share.setVisibility(View.VISIBLE);
        btn_share.setImageResource(R.drawable.top_share);
        if (mDetail.getColloct()) {
            btn_like.setImageResource(R.drawable.collect_fav_highlight);
        } else {
            btn_like.setImageResource(R.drawable.collect_fav);
        }
        String picture = mDetail.getTitlePicture();
        Glide.with(HeadlineDetailActivity.this)
                .load(picture)
                .dontAnimate()
                .into(ivHeadlineDetailTop);
        tvUser.setText(mDetail.getUsername());
        String userThumbUrl = mDetail.getUserthumb();
        Glide.with(HeadlineDetailActivity.this)
                .load(userThumbUrl)
                .dontAnimate()
                .into(ivUserThumb);
        StringBuilder html = initWebViewSetting(mDetail.getContent());
        wvDetail.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", null);
        loadComment(mDetail.getSid());
    }

    private String format(Long createTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Date date = new Date(createTime);
        String day = simpleDateFormat.format(date);
        return day;
    }

    @Override
    public void onGetHeadlineDetailFailure(String msg) {
        Logger.i("zune:", "msg= " + msg);
    }

    @Override
    public void onSendCommentResponse(RespDto<String> response) {
        if (response.isSuccess()) {
            HeadlineComment comment = new HeadlineComment();
            comment.setToUserId(mSendComment.getToUserId());
            comment.setContent(mSendComment.getContent());
            comment.setCreateTime(System.currentTimeMillis());
            comment.setFromNickname(MoreUser.getInstance().getNickname());
            comment.setToNickName(mDetail.getUsername());
            comment.setFromUserThumb(MoreUser.getInstance().getThumb());
            comment.setToUserThumb(mDetail.getUserthumb());
            comment.setUserId(mSendComment.getUid());
            if (mCommentList != null) {
                mCommentList.add(0, comment);
                Toast.makeText(HeadlineDetailActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(HeadlineDetailActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            if (mCommentAdapter != null)
                mCommentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSendCommentFailure(String msg) {
        Toast.makeText(HeadlineDetailActivity.this, "发布失败,请尝试登录", Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.nav_back, R.id.nav_share_btn, R.id.nav_right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                HeadlineDetailActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.nav_share_btn:
                //Todo 收藏功能
                UserCollect.CollectCallBack callback = new UserCollect.CollectCallBack() {
                    @Override
                    public void collectSuccess() {
                        if (mDetail.getColloct()) {
                            mDetail.setColloct(false);
                        } else {
                            mDetail.setColloct(true);
                            btn_like.setImageResource(R.drawable.collect_fav_highlight);
                        }
                        animateHeartButton(btn_like, mDetail);
                    }

                    @Override
                    public void collectFails() {

                    }
                };
                String relationID = mDetail.getSid() + "";
                UserCollect collect = new UserCollect(HeadlineDetailActivity.this, relationID,
                        UserCollect.COLLECT_TYPE.INFORMATION_COLLECT.ordinal(), mDetail.getColloct(), callback);
                break;
            case R.id.nav_right_btn:
                //Todo 分享功能
                showShareView();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
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

            int width = ScreenUtil.getScreenWidth(HeadlineDetailActivity.this);

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


    private void animateHeartButton(final ImageView btnLike, final Headline.HeadlineDetail item) {
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
                if (item.getColloct()) {
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(HeadlineDetailActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wvDetail != null) {
            wvDetail.destroy();
            wvDetail = null;
        }
        if (keyListener != null) {
            keyListener = null;
        }
        if (callbacks != null) {
            callbacks = null;
        }
    }
}
