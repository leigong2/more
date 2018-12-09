package com.moreclub.moreapp.information.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.MessageWallReply;
import com.moreclub.moreapp.information.model.ReplyClickListener;
import com.moreclub.moreapp.information.model.UserWriteMessageWall;
import com.moreclub.moreapp.information.presenter.IMessageWallReplyLoader;
import com.moreclub.moreapp.information.presenter.MessageWallReplyLoader;
import com.moreclub.moreapp.information.ui.adapter.MessageWallDetailAdapter;
import com.moreclub.moreapp.information.ui.view.CenterTextView;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageWallDetailActivity extends BaseActivity implements
        IMessageWallReplyLoader.LoadMessageWallReplyBinder, ReplyClickListener {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_message_wall_detail)
    XRecyclerView xrvMessageWallDetail;
    @BindView(R.id.et_reply_comment)
    EditText etReplyComment;


    private Long cid;
    private Integer pn = 0;
    private Integer ps = 20;
    private MessageWallReply.MainCommBean mainComm;
    private List<MessageWallReply.RepliesBean> replies;
    private RecyclerView.Adapter adapter;
    private CircleImageView civUserThumb;
    private TextView tvUserName;
    private TextView tvSendDate;
    private TextView replyCount;
    private CenterTextView tvComment;
    private View header;
    private boolean isFirst = true;
    private boolean isClicked;
    private boolean isLoaded;
    private int actId;
    private RecyclerView.LayoutManager lm;

    private Integer timestamp = (int) (System.currentTimeMillis() / 1000);

    private View.OnClickListener headerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Todo 弹起软键盘
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            etReplyComment.setFocusable(true);
            etReplyComment.setFocusableInTouchMode(true);
            etReplyComment.requestFocus();
        }
    };
    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                if (!TextUtils.isEmpty(etReplyComment.getText().toString().trim())) {
                    sendComment(etReplyComment.getText().toString().trim());
                } else {
                    Toast.makeText(MessageWallDetailActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                }
                etReplyComment.setText("");
                return true;
            }
            return false;
        }
    };

    private void sendComment(String comment) {
        //Todo 回复留言墙
        isClicked = true;
        if (MoreUser.getInstance().getUid() == 0) {
            AppUtils.pushLeftActivity(MessageWallDetailActivity.this, UseLoginActivity.class);
        } else {
            String token = MoreUser.getInstance().getAccess_token();
            UserWriteMessageWall user = new UserWriteMessageWall();
            user.setActivityId(actId);
            user.setAppVersion(Constants.APP_version);
            user.setComment(comment);
            int parentId = mainComm.getCommentId();
            user.setParentId(parentId);
            Long replyUid = mainComm.getUid();
            user.setReplyUid(replyUid);
            user.setUid(MoreUser.getInstance().getUid());
            user.setTimestamp(timestamp);
            ((MessageWallReplyLoader) mPresenter).onPostMessageWallReply(token, user);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.message_wall_detail_activity;
    }

    @Override
    public Class getLogicClazz() {
        return IMessageWallReplyLoader.class;
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
    }

    private void setupView() {
        activityTitle.setText("留言详情");
        mainComm = new MessageWallReply.MainCommBean();
        initRecylerView();
        cid = (long) getIntent().getIntExtra(Constants.KEY_COMMENT_ID, -1);
        actId = getIntent().getIntExtra(Constants.KEY_ACT_ID, -1);
        etReplyComment.setOnKeyListener(keyListener);
        xrvMessageWallDetail.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pn = 0;
                isFirst = true;
                ((MessageWallReplyLoader) mPresenter).onLoadMessageWallReply(cid, pn, ps);
            }

            @Override
            public void onLoadMore() {
                isLoaded = true;
                ++pn;
                ((MessageWallReplyLoader) mPresenter).onLoadMessageWallReply(cid, pn, ps);
                onLoadComplete(pn);
            }
        });
        ((MessageWallReplyLoader) mPresenter).onLoadMessageWallReply(cid, pn, ps);
    }

    private void initHeader() {
        tvUserName.setText(mainComm.getUserName());
        tvComment.setText(mainComm.getComment());
        tvSendDate.setText(format(mainComm.getCreateTime()));
        replyCount.setText("回复 " + mainComm.getReplyNum());
        Glide.with(MessageWallDetailActivity.this)
                .load(mainComm.getUserThumb())
                .dontAnimate()
                .into(civUserThumb);
    }

    private void initRecylerView() {
        lm = new LinearLayoutManager(MessageWallDetailActivity.this);
        xrvMessageWallDetail.setLayoutManager(lm);
        xrvMessageWallDetail.setPullRefreshEnabled(true);
        xrvMessageWallDetail.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvMessageWallDetail.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        replies = new ArrayList<>();
        adapter = new MessageWallDetailAdapter(MessageWallDetailActivity.this, R.layout.message_wall_reply_item, replies);
        xrvMessageWallDetail.setAdapter(adapter);
        header = LayoutInflater.from(MessageWallDetailActivity.this)
                .inflate(R.layout.reply_message_wall_header, null);
        header.setBackgroundColor(Color.parseColor("#33FCE5CD"));
        civUserThumb = (CircleImageView) header.findViewById(R.id.civ_user_thumb);
        tvUserName = (TextView) header.findViewById(R.id.tv_user_name);
        tvSendDate = (TextView) header.findViewById(R.id.tv_send_date);
        replyCount = (TextView) header.findViewById(R.id.reply_count);
        tvComment = (CenterTextView) header.findViewById(R.id.tv_comment);
        xrvMessageWallDetail.addHeaderView(header);
        header.setOnClickListener(headerClickListener);
    }

    @Override
    public void onLoadReplyResponse(RespDto<MessageWallReply> response) {
        if (isLoaded) {
            if (response == null || response.getData() == null || response.getData().getReplies() == null) {
                Toast.makeText(MessageWallDetailActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                isClicked = false;
                return;
            }
        }
        onLoadComplete(pn);
        MessageWallReply reply = response.getData();
        if (isFirst && reply != null) {
            mainComm = reply.getMainComm();
            initHeader();
        }
        if (isFirst && reply.getReplies() != null && reply.getReplies().size() > 0) {
            isFirst = false;
            replies.addAll(reply.getReplies());
            adapter.notifyDataSetChanged();
        } else if (adapter != null && isClicked && reply != null && reply.getReplies() != null) {
            ++pn;
            ((MessageWallReplyLoader) mPresenter).onLoadMessageWallReply(cid, pn, ps);
            replies.addAll(reply.getReplies());
            adapter.notifyDataSetChanged();
            if (replies != null)
                xrvMessageWallDetail.scrollToPosition(replies.size() + 2);
        } else if (adapter != null && isLoaded) {
            isLoaded = false;
            replies.addAll(reply.getReplies());
            adapter.notifyDataSetChanged();
        }
    }

    private String format(Long createTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(createTime);
        String format = dateFormat.format(date);
        return format;
    }


    private void onLoadComplete(Integer index) {
        if (index == 0 && replies != null) {
            replies.clear();
            xrvMessageWallDetail.refreshComplete();
        } else {
            xrvMessageWallDetail.loadMoreComplete();
        }
    }

    @Override
    public void onLoadReplyFailure(String msg) {
        onLoadComplete(pn);
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onPostReplyResponse(RespDto<String> body) {
        if (body != null && body.isSuccess()) {
            Toast.makeText(MessageWallDetailActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
            ((MessageWallReplyLoader) mPresenter).onLoadMessageWallReply(cid, pn, ps);
        }
    }

    @Override
    public void onPostReplyFailure(String msg) {
        Toast.makeText(MessageWallDetailActivity.this, "网络异常,稍后再试", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.nav_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                MessageWallDetailActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(MessageWallDetailActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClickReplyListener(String name, Long uid) {
        long userUid = MoreUser.getInstance().getUid();
        long toUid = uid;
        if (userUid != toUid) {
            etReplyComment.setText("回复 " + name + " : ");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            etReplyComment.setFocusable(true);
            etReplyComment.setFocusableInTouchMode(true);
            etReplyComment.requestFocus();
            etReplyComment.setSelection(etReplyComment.getText().length());
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            etReplyComment.setFocusable(true);
            etReplyComment.setFocusableInTouchMode(true);
            etReplyComment.requestFocus();
        }
    }
}
