package com.moreclub.moreapp.main.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.information.ui.adapter.HeadlineCommentAdapter;
import com.moreclub.moreapp.main.model.MChannelGood;
import com.moreclub.moreapp.main.model.MChannelGoodUser;
import com.moreclub.moreapp.main.model.ReplyClickListener;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.presenter.IMChannelCommentLoader;
import com.moreclub.moreapp.main.presenter.MChannelCommentLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelGoodAdapter;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsActivity;
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
import butterknife.OnClick;

public class MChannelCommentActivity extends BaseActivity implements
        IMChannelCommentLoader.LoadMChannelCommentBinder, ReplyClickListener {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.iv_header_icon)
    ImageView ivHeaderIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.comment_line)
    TextView commentLine;
    @BindView(R.id.good)
    TextView good;
    @BindView(R.id.good_line)
    TextView goodLine;
    @BindView(R.id.commentList)
    XRecyclerView commentList;
    @BindView(R.id.iv_no_comment)
    ImageView ivNoComment;
    @BindView(R.id.tv_no_comment)
    TextView tvNoComment;
    @BindView(R.id.iv_good)
    ImageView ivGood;
    @BindView(R.id.bottom_good_count)
    TextView bottomGoodCount;
    @BindView(R.id.goodList)
    XRecyclerView goodList;
    @BindView(R.id.et_write_comment)
    EditText etWriteComment;
    private Headline.HeadlineDetail mDtails;
    private int sid;
    private long oldTime;
    private List<HeadlineComment> mCommentList;
    private List<MChannelGoodUser> mGoodList;
    private HeadlineSendComment mSendComment;
    private HeadlineComment headlineComment;
    private HeadlineCommentAdapter mCommentAdapter;
    private MChannelGoodAdapter mGoodAdapter;
    private int mGoodPn = 1;
    private int mPn = 1;
    private int mPs = 10;
    private int mGoodPs = 10;
    private int totalGood;
    private int pagesGood;
    private int commentCount;
    private boolean showComments;
    private boolean showGoods;
    private long nowTime;
    private String postUrl;
    private RecyclerAdapter.OnItemClickListener goodItemClick = new RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ViewGroup parent, View view, Object o, int position) {
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(MChannelCommentActivity.this, UseLoginActivity.class);
                return;
            }
            if (mGoodList == null || mGoodList.get(position) == null || mGoodList.get(position).getUid() <= 0)
                return;
            ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                    MChannelCommentActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            Intent intent_merchant = new Intent(MChannelCommentActivity.this, UserDetailV2Activity.class);
            intent_merchant.putExtra("toUid", mGoodList.get(position).getUid() + "");
            ActivityCompat.startActivity(MChannelCommentActivity.this, intent_merchant, compat_merchant.toBundle());
        }

        @Override
        public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
            return false;
        }
    };

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    etWriteComment.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(etWriteComment.getText().toString().trim())) {
                    sendComment(etWriteComment.getText().toString().trim());
                    etWriteComment.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MChannelCommentActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                }
                etWriteComment.setText("");
                return true;
            }
            return false;
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
    private String prefeerContent;
    private int type;


    @Override
    protected int getLayoutResource() {
        return R.layout.mchannel_comment_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IMChannelCommentLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupViews();
        initData();
    }

    private void initData() {
        oldTime = System.currentTimeMillis();
        if (!TextUtils.isEmpty(prefeerContent)) {
            tvTitle.setText(prefeerContent);
        }
        postUrl = getIntent().getStringExtra("postUrl");
        if (sid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelCommentLoader) mPresenter).onLoadMChannelDetails(sid, MoreUser.getInstance().getUid());
        }
    }

    private void setupViews() {
        sid = getIntent().getIntExtra("sid", this.sid);
        type = getIntent().getIntExtra("type", -1);
        prefeerContent = getIntent().getStringExtra("prefeerContent");
        activityTitle.setText("评论点赞");
        mCommentList = new ArrayList<>();
        mGoodList = new ArrayList<>();
        etWriteComment.setOnKeyListener(onKeyListener);
        etWriteComment.addTextChangedListener(textChangeListener);
        etWriteComment.setTextColor(Color.parseColor("#999999"));
        mGoodAdapter = new MChannelGoodAdapter(MChannelCommentActivity.this
                , R.layout.mchannel_good_item, mGoodList);
        mGoodAdapter.setOnItemClickListener(goodItemClick);
        mGoodAdapter.setHasHeader(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(MChannelCommentActivity.this);
        goodList.setLayoutManager(lm);
        goodList.setAdapter(mGoodAdapter);
        goodList.setPullRefreshEnabled(false);
        goodList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mGoodPn++;
                if (mGoodPn <= pagesGood && sid > 0 && MoreUser.getInstance().getUid() != null) {
                    ((MChannelCommentLoader) mPresenter).onLoadGoodList(sid, mGoodPn, mGoodPs);
                } else {
                    onLoadComplete(mGoodPn);
                }
            }
        });
        mCommentAdapter = new HeadlineCommentAdapter(MChannelCommentActivity.this
                , R.layout.headline_comment_item, mCommentList,sid);
        mCommentAdapter.setHasHeader(true);
        RecyclerView.LayoutManager lm2 = new LinearLayoutManager(MChannelCommentActivity.this);
        commentList.setLayoutManager(lm2);
        commentList.setAdapter(mCommentAdapter);
        commentList.setPullRefreshEnabled(false);
        commentList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPn++;
                mPs = 10;
                if (sid > 0 && MoreUser.getInstance().getUid() != null) {
                    ((MChannelCommentLoader) mPresenter).onLoadCommentList(sid, mPn, mPs, MoreUser.getInstance().getUid());
                } else {
                    onLoadComplete(mPn);
                }
            }
        });
    }

    @Override
    public void onGetMChannelDetailsResponse(RespDto<Headline.HeadlineDetail> response) {
        if (response != null && response.isSuccess() && response.getData() != null) {
            mDtails = response.getData();
            if (mDtails != null && TextUtils.isEmpty(prefeerContent))
                tvTitle.setText(mDtails.getTitle());
            if (TextUtils.isEmpty(mDtails.getClicked())) {
                ivGood.setImageResource(R.drawable.like);
            } else {
                ivGood.setImageResource(R.drawable.like2);
            }
            commentCount = mDtails.getConmentsCount();
            if (TextUtils.isEmpty(postUrl))
                ivHeaderIcon.setVisibility(View.GONE);
            else {
                Glide.with(this)
                        .load(postUrl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(ivHeaderIcon);
            }
            tvDetail.setText(mDtails.getUsername());
            loadComment(mDtails.getSid());
            loadGood(mDtails.getSid());
        }
    }

    @Override
    public void onGetMChannelDetailsFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
    }

    @Override
    public void onGetCommentListResponse(RespDto<List<HeadlineComment>> response) {
        if (response != null && response.getData() != null)
            initCommentList(response.getData());
    }

    @Override
    public void onGetCommentListFailure(String msg) {
        Logger.i("zune:", "comment msg = " + msg);
        onLoadComplete(mPn);
    }

    @Override
    public void onGetGoodListResponse(RespDto<MChannelGood> response) {
        if (response == null || response.getData() == null) return;
        totalGood = response.getData().getTotal();
        pagesGood = response.getData().getPages();
        initGoodList(response.getData());
    }

    @Override
    public void onGetGoodListFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
        onLoadComplete(mGoodPn);
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
            comment.setToUserThumb(mDtails.getUserthumb());
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
            showComments = true;
            if (commentLine.getVisibility() == View.VISIBLE) {
                goodList.setVisibility(View.GONE);
                commentList.setVisibility(View.VISIBLE);
                tvNoComment.setVisibility(View.GONE);
                ivNoComment.setVisibility(View.GONE);
            }
            etWriteComment.setHint("留个言评论下吧……");
            Toast.makeText(MChannelCommentActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            MChannelCommentEvent event = new MChannelCommentEvent();
            event.setComment(comment);
            event.setSid(sid);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onSendCommentFailure(String msg) {
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
            AppUtils.pushLeftActivity(MChannelCommentActivity.this, UseLoginActivity.class);
            return;
        }
        Logger.i("zune:", "comment MSg = " + msg);
    }

    @Override
    public void onSetResponse(RespDto<Boolean> response) {
        if (response == null || response.getData() == null) return;
        if (response.getData()) {
            int count = Integer.parseInt(TextUtils.isEmpty(bottomGoodCount.getText().toString())
                    ? "0" : bottomGoodCount.getText().toString());
            bottomGoodCount.setText(count + "");
            ivGood.setImageResource(R.drawable.like2);
            bottomGoodCount.setTextColor(Color.parseColor("#FFCC00"));
            Toast.makeText(MChannelCommentActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
            MChannelGoodUser user = new MChannelGoodUser();
            if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                Long uid = MoreUser.getInstance().getUid();
                user.setUid(uid);
                user.setNickName(MoreUser.getInstance().getNickname());
                user.setThumb(MoreUser.getInstance().getThumb());
            }
            if (count == 0) {
                showGoods = false;
            } else {
                showGoods = true;
            }
            if (count == 0 && goodLine.getVisibility() == View.VISIBLE) {
                goodList.setVisibility(View.GONE);
                commentList.setVisibility(View.GONE);
                tvNoComment.setVisibility(View.VISIBLE);
                ivNoComment.setVisibility(View.VISIBLE);
            } else if (count > 0 && goodLine.getVisibility() == View.VISIBLE) {
                goodList.setVisibility(View.VISIBLE);
                commentList.setVisibility(View.GONE);
                tvNoComment.setVisibility(View.GONE);
                ivNoComment.setVisibility(View.GONE);
            }
            mGoodAdapter.notifyDataSetChanged();
        } else {
            int count = Integer.parseInt(TextUtils.isEmpty(bottomGoodCount.getText().toString())
                    ? "0" : bottomGoodCount.getText().toString());
            bottomGoodCount.setText(count > 0 ? count + "" : "");
            ivGood.setImageResource(R.drawable.like);
            bottomGoodCount.setTextColor(Color.parseColor("#999999"));
            for (int i = 0; i < mGoodList.size(); i++) {
                if (mGoodList.get(i).getUid() == MoreUser.getInstance().getUid()) {
                    break;
                }
            }
            if (count == 0) {
                showGoods = false;
            } else {
                showGoods = true;
            }
            if (count == 0 && goodLine.getVisibility() == View.VISIBLE) {
                goodList.setVisibility(View.GONE);
                commentList.setVisibility(View.GONE);
                tvNoComment.setVisibility(View.VISIBLE);
                ivNoComment.setVisibility(View.VISIBLE);
            } else if (count > 0 && goodLine.getVisibility() == View.VISIBLE) {
                goodList.setVisibility(View.VISIBLE);
                commentList.setVisibility(View.GONE);
                tvNoComment.setVisibility(View.GONE);
                ivNoComment.setVisibility(View.GONE);
            }
            mGoodAdapter.notifyDataSetChanged();
            Toast.makeText(MChannelCommentActivity.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
        }
        MChannelCommentEvent event = new MChannelCommentEvent();
        event.setClicked(response.getData());
        event.setSid(sid);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onSetFailure(String msg) {
        Logger.i("zune:", "set msg = " + msg);
    }

    /**
     * zune:加载评论和点赞
     **/
    private void loadComment(Integer sid) {
        if (sid != null && sid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelCommentLoader) mPresenter).onLoadCommentList(sid, mPn, mPs, MoreUser.getInstance().getUid());
        }
    }

    private void loadGood(Integer fid) {
        if (fid != null && fid > 0 && MoreUser.getInstance().getUid() != null) {
            ((MChannelCommentLoader) mPresenter).onLoadGoodList(fid, mGoodPn, mGoodPs);
        }
    }

    /**
     * zune:初始化评论列表
     **/
    private void initCommentList(List<HeadlineComment> comments) {
        onLoadComplete(mPn);
        mCommentList.addAll(comments);
        if (!showComments && mCommentList.size() == 0) {
            tvNoComment.setVisibility(View.VISIBLE);
            ivNoComment.setVisibility(View.VISIBLE);
            commentList.setVisibility(View.GONE);
            goodList.setVisibility(View.GONE);
            showComments = false;
            return;
        } else {
            tvNoComment.setVisibility(View.GONE);
            ivNoComment.setVisibility(View.GONE);
            commentList.setVisibility(View.VISIBLE);
            goodList.setVisibility(View.GONE);
            showComments = true;
        }
        mCommentAdapter.notifyDataSetChanged();
    }

    /**
     * zune:初始化点赞列表
     **/
    private void initGoodList(MChannelGood data) {
        onLoadComplete(mGoodPn);
        mGoodList.addAll(data.getList());
        bottomGoodCount.setText(totalGood == 0 ? "" : totalGood + "");
        if (!showGoods && mGoodList.size() == 0) {
            showGoods = false;
        } else {
            showGoods = true;
        }
        mGoodAdapter.notifyDataSetChanged();
    }

    private void sendComment(String comment) {
        //Todo 发评论
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
        ((MChannelCommentLoader) mPresenter).onSendComment(MoreUser.getInstance().getAccess_token(), mSendComment);
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
                ((MChannelCommentLoader) mPresenter).onSetGood(MoreUser.getInstance().getAccess_token()
                        , MoreUser.getInstance().getUid(), sid, "likeTime1");
        }
    }

    private void onLoadComplete(int index) {
        if (index == 1) {
            if (mCommentList != null) {
                commentList.refreshComplete();
                goodList.refreshComplete();
            }
        } else {
            commentList.loadMoreComplete();
            goodList.loadMoreComplete();
        }
    }

    @OnClick({R.id.nav_back, R.id.iv_header_icon, R.id.tv_detail, R.id.comment
            , R.id.good, R.id.rl_root, R.id.rl_good})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.rl_root:
            case R.id.tv_detail:
                if (type>1) {
                    ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                            MChannelCommentActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent1 = new Intent(MChannelCommentActivity.this, MChannelDetailsAcitivy.class);
                    intent1.putExtra("sid", sid);
                    ActivityCompat.startActivity(MChannelCommentActivity.this, intent1, compat1.toBundle());
                }
                break;
            case R.id.comment:
                //Todo 切换到评论
                mCommentAdapter.notifyDataSetChanged();
                etWriteComment.setVisibility(View.VISIBLE);
                good.setTextColor(Color.parseColor("#999999"));
                comment.setTextColor(Color.parseColor("#333333"));
                commentLine.setVisibility(View.VISIBLE);
                goodLine.setVisibility(View.GONE);
                if (showComments) {
                    tvNoComment.setVisibility(View.GONE);
                    ivNoComment.setVisibility(View.GONE);
                    commentList.setVisibility(View.VISIBLE);
                } else {
                    ivNoComment.setVisibility(View.VISIBLE);
                    tvNoComment.setVisibility(View.VISIBLE);
                    ivNoComment.setImageResource(R.drawable.comment_sofa);
                    tvNoComment.setText("就等你来评论");
                    commentList.setVisibility(View.GONE);
                }
                goodList.setVisibility(View.GONE);
                break;
            case R.id.good:
                //Todo 切换到点赞
                mGoodAdapter.notifyDataSetChanged();
                etWriteComment.setVisibility(View.GONE);
                good.setTextColor(Color.parseColor("#333333"));
                comment.setTextColor(Color.parseColor("#999999"));
                commentLine.setVisibility(View.GONE);
                goodLine.setVisibility(View.VISIBLE);
                if (showGoods) {
                    tvNoComment.setVisibility(View.GONE);
                    ivNoComment.setVisibility(View.GONE);
                    goodList.setVisibility(View.VISIBLE);
                } else {
                    tvNoComment.setVisibility(View.VISIBLE);
                    ivNoComment.setVisibility(View.VISIBLE);
                    tvNoComment.setText("有意思就点赞");
                    ivNoComment.setImageResource(R.drawable.like_first);
                    goodList.setVisibility(View.GONE);
                }
                commentList.setVisibility(View.GONE);
                break;
            case R.id.rl_good:
                //Todo 点赞
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MChannelCommentActivity.this, UseLoginActivity.class);
                } else {
                    nowTime = System.currentTimeMillis();
                    if (nowTime - oldTime > 1000) {
                        oldTime = System.currentTimeMillis();
                        setGood(sid);
                    }
                }
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(MChannelCommentActivity.this,
                        v.getWindowToken());
                etWriteComment.setHint("留个言评论下吧……");
                etWriteComment.setVisibility(View.VISIBLE);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClickReplyListener(HeadlineComment headlineComment) {
        etWriteComment.setVisibility(View.VISIBLE);
        if (headlineComment.getUserId() != MoreUser.getInstance().getUid()) {
            etWriteComment.setHint("回复 " + headlineComment.getFromNickname() + "：");
            this.headlineComment = headlineComment;
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
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMChannelEvent(MChannelCommentEvent event) {
        int sid = event.getSid();
        HeadlineComment comment = event.getComment();
        Boolean isClicked = event.isClicked();
        if (sid == this.sid) {
            if (comment != null && !TextUtils.isEmpty(comment.getContent())) {
                mCommentList.add(0, comment);
                if (commentLine.getVisibility() == View.VISIBLE && mCommentList.size() > 0)
                    commentList.scrollToPosition(0);
                mCommentAdapter.notifyDataSetChanged();
            } else if (isClicked != null) {
                if (isClicked) {
                    MChannelGoodUser user = new MChannelGoodUser();
                    if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                        Long uid = MoreUser.getInstance().getUid();
                        user.setUid(uid);
                        user.setNickName(MoreUser.getInstance().getNickname());
                        user.setThumb(MoreUser.getInstance().getThumb());
                        mGoodList.add(0, user);
                        if (mGoodList.size() == 0 && goodLine.getVisibility() == View.VISIBLE) {
                            goodList.setVisibility(View.GONE);
                            commentList.setVisibility(View.GONE);
                            tvNoComment.setVisibility(View.VISIBLE);
                            ivNoComment.setVisibility(View.VISIBLE);
                            tvNoComment.setText("就等你来评论");
                        } else if (mGoodList.size() > 0 && goodLine.getVisibility() == View.VISIBLE) {
                            goodList.setVisibility(View.VISIBLE);
                            commentList.setVisibility(View.GONE);
                            tvNoComment.setVisibility(View.GONE);
                            ivNoComment.setVisibility(View.GONE);
                        }
                        if (mGoodList.size() == 0) {
                            showGoods = false;
                        } else {
                            showGoods = true;
                        }
                        int count = Integer.parseInt(TextUtils.isEmpty(bottomGoodCount.getText().toString())
                                ? "0" : bottomGoodCount.getText().toString()) + 1;
                        bottomGoodCount.setText(count + "");
                        ivGood.setImageResource(R.drawable.like2);
                        bottomGoodCount.setTextColor(Color.parseColor("#FFCC00"));
                        mGoodAdapter.notifyDataSetChanged();
                    }
                } else {
                    a:
                    for (int i1 = 0; i1 < mGoodList.size(); i1++) {
                        if (mGoodList.get(i1).getUid() == MoreUser.getInstance().getUid()) {
                            mGoodList.remove(i1);
                            if (mGoodList.size() == 0 && goodLine.getVisibility() == View.VISIBLE) {
                                goodList.setVisibility(View.GONE);
                                commentList.setVisibility(View.GONE);
                                tvNoComment.setVisibility(View.VISIBLE);
                                tvNoComment.setText("有意思就点赞");
                                ivNoComment.setVisibility(View.VISIBLE);
                            } else if (mGoodList.size() > 0 && goodLine.getVisibility() == View.VISIBLE) {
                                goodList.setVisibility(View.VISIBLE);
                                commentList.setVisibility(View.GONE);
                                tvNoComment.setVisibility(View.GONE);
                                ivNoComment.setVisibility(View.GONE);
                            }
                            if (mGoodList.size() == 0) {
                                showGoods = false;
                            } else {
                                showGoods = true;
                            }
                            int count = Integer.parseInt(TextUtils.isEmpty(bottomGoodCount.getText().toString())
                                    ? "0" : bottomGoodCount.getText().toString()) - 1;
                            bottomGoodCount.setText(count > 0 ? count + "" : "");
                            ivGood.setImageResource(R.drawable.like);
                            bottomGoodCount.setTextColor(Color.parseColor("#999999"));
                            mGoodAdapter.notifyDataSetChanged();
                            break a;
                        }
                    }
                }
            }
        }
    }
}
