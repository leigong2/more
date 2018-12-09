package com.moreclub.moreapp.main.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.presenter.ISignInterDetailLoader;
import com.moreclub.moreapp.main.presenter.SignInterDetailLoader;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.ShareUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hyphenate.easeui.EaseConstant.ISSYSTEMMESSAGE;
import static com.moreclub.moreapp.main.constant.Constants.MYSIGNINTERLISTACTIVITY;
import static com.moreclub.moreapp.main.constant.Constants.SIGNINTERACTIVITY;
import static com.moreclub.moreapp.main.constant.Constants.SIGNINTERTOTALACTIVITY;
import static com.moreclub.moreapp.main.constant.Constants.SUPERMAINACTIVITY;

public class SignInterDetailActivity extends BaseActivity implements ISignInterDetailLoader.LoadSignInterDetailBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.nav_share_btn)
    ImageButton navShareBtn;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightBtn;
    @BindView(R.id.civ_user_thumb)
    CircleImageView civUserThumb;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.drinks)
    ImageView ivDrink;
    @BindView(R.id.iv_type)
    ImageView ivType;
    @BindView(R.id.gender)
    ImageView ivGender;
    @BindView(R.id.tv_drinks)
    TextView tvDrinks;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_me_location)
    TextView tvMeLocation;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.end)
    TextView end;
    @BindView(R.id.iv_drink_bottom)
    ImageView ivDrinkBottom;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.view_weight)
    View view_weight;
    private View popupShareView;
    private PopupWindow mShareWindow;
    private int sid;
    private int chatType;
    private MerchantsSignInters merchantsSignInters;
    private int isfightseat = 1;
    private String inter = Constants.TITLE_ALLOW;
    private int tag;

    private int[] resTypeSelectorIds = {R.drawable.pinzuo_pgz, R.drawable.pinzuo_llt, R.drawable.pinzuo_wyx
            , R.drawable.pinzuo_qhd, R.drawable.pinzuo_bgm, R.drawable.pinzuo_bsh};

    private int[] resIds = {R.drawable.buyuadrink_white, R.drawable.cocktail_white, R.drawable.beerbrew_white
            , R.drawable.wine_white, R.drawable.whiskey_white, R.drawable.importbeer_white,
            R.drawable.champagne_white, R.drawable.brandy_white, R.drawable.saki_white};

    private String[] str = {"", "鸡尾酒", "精酿啤酒", "威士忌", "鸡尾酒", "进口啤酒", "香槟", "白兰地", "清酒"};

    private int[] drawables = {R.drawable.interaction_sit, R.drawable.interaction_talk, R.drawable.interaction_play,
            R.drawable.interaction_qa, R.drawable.interaction_help, R.drawable.interaction_dont_talk};

    private View.OnClickListener weixinFriendShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(SignInterDetailActivity.this, 1, 1, 5, sid + "");
            share.share();
        }
    };
    private View.OnClickListener weixinGroupShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(SignInterDetailActivity.this, 0, 1, 5, sid + "");
            share.share();
        }
    };
    private View.OnClickListener weiboShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(SignInterDetailActivity.this, 0, 2, 5, sid + "");
            share.share();
        }
    };
    private Long toUid;
    private String nickName;
    private String userThumb;
    private int mySid;
    private int mid;
    private boolean isFromMSpace;

    @Override
    public Class getLogicClazz() {
        return ISignInterDetailLoader.class;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_inter_detail_activity;
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
        sid = getIntent().getIntExtra("sid", -1);
        mySid = getIntent().getIntExtra("mySid", -1);
        mid = getIntent().getIntExtra("mid", -1);
        tag = getIntent().getIntExtra("tag", -1);
        isFromMSpace = getIntent().getBooleanExtra("isFromMSpace", false);
        ((SignInterDetailLoader) mPresenter).onLoadSignInterDetail(MoreUser.getInstance().getAccess_token(), sid);
        activityTitle.setText("M-space");
        navShareBtn.setImageResource(R.drawable.delete);
        navRightBtn.setImageResource(R.drawable.top_share);

    }

    private void initData() {
        if (merchantsSignInters == null) return;
        Glide.with(this)
                .load(merchantsSignInters.getUserThumb())
                .placeholder(R.drawable.profilephoto_small)
                .dontAnimate()
                .into(civUserThumb);
        tvUserName.setText(merchantsSignInters.getNickName());
        if (merchantsSignInters.getGender().equals("0")) {
            ivGender.setImageResource(R.drawable.femenine);
        } else {
            ivGender.setImageResource(R.drawable.masculine);
        }
        if (merchantsSignInters.getGift() > 0) {
            ivDrink.setImageResource(resIds[merchantsSignInters.getGift() - 1]);
            tvDrinks.setText("请你喝一杯" + str[merchantsSignInters.getGift() - 1]);
        } else {
            ivDrink.setVisibility(View.INVISIBLE);
            tvDrinks.setVisibility(View.INVISIBLE);
        }
        switch (merchantsSignInters.getType()) {
            case 5:
                view_weight.setBackgroundResource(drawables[0]);
                ivType.setImageResource(resTypeSelectorIds[0]);
                break;
            case 3:
                view_weight.setBackgroundResource(drawables[1]);
                ivType.setImageResource(resTypeSelectorIds[1]);
                break;
            case 6:
                view_weight.setBackgroundResource(drawables[2]);
                ivType.setImageResource(resTypeSelectorIds[2]);
                break;
            case 4:
                view_weight.setBackgroundResource(drawables[3]);
                ivType.setImageResource(resTypeSelectorIds[3]);
                break;
            case 1:
                view_weight.setBackgroundResource(drawables[4]);
                ivType.setImageResource(resTypeSelectorIds[4]);
                break;
            case 2:
                view_weight.setBackgroundResource(drawables[5]);
                ivType.setImageResource(resTypeSelectorIds[5]);
                break;
        }
        tvContent.setText(merchantsSignInters.getContent());
        tvMeLocation.setText(merchantsSignInters.getMerchantName());
        int exprise = merchantsSignInters.getExprise();
        switch (exprise) {
            case 1:
                end.setText("已 结 束");
                navRightBtn.setVisibility(View.GONE);
                navShareBtn.setVisibility(View.GONE);
                tvSend.setVisibility(View.GONE);
                ivDrinkBottom.setVisibility(View.GONE);
                end.setVisibility(View.VISIBLE);
                if (merchantsSignInters.getUid().equals(MoreUser.getInstance().getUid())) {
                    navRightBtn.setVisibility(View.VISIBLE);
                    navShareBtn.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                end.setText("结 束");
                if (merchantsSignInters.getUid().equals(MoreUser.getInstance().getUid())) {
                    end.setVisibility(View.VISIBLE);
                    tvSend.setVisibility(View.GONE);
                    ivDrinkBottom.setVisibility(View.GONE);
                    navRightBtn.setVisibility(View.VISIBLE);
                    navShareBtn.setVisibility(View.VISIBLE);
                } else {
                    end.setVisibility(View.GONE);
                    tvSend.setVisibility(View.VISIBLE);
                    ivDrinkBottom.setVisibility(View.VISIBLE);
                    navRightBtn.setVisibility(View.GONE);
                    navShareBtn.setVisibility(View.GONE);
                }
                break;
            case 3:
                end.setText("已 过 期");
                navRightBtn.setVisibility(View.GONE);
                navShareBtn.setVisibility(View.GONE);
                tvSend.setVisibility(View.GONE);
                ivDrinkBottom.setVisibility(View.GONE);
                end.setVisibility(View.VISIBLE);
                if (merchantsSignInters.getUid().equals(MoreUser.getInstance().getUid())) {
                    navRightBtn.setVisibility(View.VISIBLE);
                    navShareBtn.setVisibility(View.VISIBLE);
                }
                break;
        }
        String time = getTime(merchantsSignInters.getTimestamp());
        this.time.setText(time);
    }

    private String getTime(long timestamp) {
        long now = Long.valueOf(TimeUtils.getTimestampSecond());
        long time = now - timestamp;
        if (time < (10 * 60)) {
            return "刚刚";
        } else if (time > (10 * 60) && time < (60 * 60)) {
            return (time / (60)) + "分钟";
        } else if (time > (60 * 60) && time < (24 * 60 * 60)) {
            return (time / (60 * 60)) + "小时";
        } else if (time > (24 * 60 * 60) && time < (30 * 24 * 60 * 60)) {
            return (time / (24 * 60 * 60)) + "天";
        } else if (time > (30 * 24 * 60 * 60)) {
            return (time / (30 * 24 * 60 * 60)) + "月";
        } else {
            return "";
        }
    }


    @OnClick({R.id.nav_back, R.id.nav_share_btn, R.id.nav_right_btn, R.id.tv_send
            , R.id.civ_user_thumb, R.id.end, R.id.tv_me_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                break;
            case R.id.tv_me_location:
                ActivityOptionsCompat compat0 = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent0 = new Intent(this, MerchantDetailsViewActivity.class);
                if (mid > 0)
                    intent0.putExtra("mid", mid + "");
                else if (merchantsSignInters != null && merchantsSignInters.getMid() > 0) {
                    intent0.putExtra("mid", merchantsSignInters.getMid()+"");
                }
                ActivityCompat.startActivity(this, intent0, compat0.toBundle());
                break;
            case R.id.nav_share_btn:
                //Todo 删除互动
                View dialogLayout = View.inflate(SignInterDetailActivity.this, R.layout.sign_inter_delete_dialog, null);
                TextView delete = (TextView) dialogLayout.findViewById(R.id.delete);
                TextView cancel = (TextView) dialogLayout.findViewById(R.id.cancel);
                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(dialogLayout)
                        .show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SignInterDetailLoader) mPresenter).onDeleteSignInterDetail(MoreUser.getInstance().getAccess_token(), merchantsSignInters.getSid());
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.end:
                //Todo 结束互动
                if (end.getText().toString().equals("结 束"))
                    ((SignInterDetailLoader) mPresenter).endSignInter(MoreUser.getInstance().getAccess_token(), merchantsSignInters.getSid());
                break;
            case R.id.nav_right_btn:
                //Todo 分享互动
                if (MoreUser.getInstance().getUid() != null
                        && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(SignInterDetailActivity.this, UseLoginActivity.class);
                    return;
                }
                showShareView();
                break;
            case R.id.tv_send:
                //Todo 我感兴趣
                if (MoreUser.getInstance().getUid() != null
                        && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(SignInterDetailActivity.this, UseLoginActivity.class);
                    return;
                }
                SignInter inter = new SignInter();
                inter.setUid(merchantsSignInters.getUid());
                inter.setFromUid(MoreUser.getInstance().getUid());
                inter.setSid(merchantsSignInters.getSid());
                inter.setTitle(Constants.TITLE_SEND_INTEREST);
                ((SignInterDetailLoader) mPresenter).onPostSignInter(MoreUser.getInstance().getAccess_token(), inter);
                break;
            case R.id.civ_user_thumb:
                //Todo 个人主页
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(SignInterDetailActivity.this, UseLoginActivity.class);
                    return;
                } else {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            SignInterDetailActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent = new Intent(SignInterDetailActivity.this, UserDetailV2Activity.class);
                    intent.putExtra("toUid", "" + merchantsSignInters.getUid());
                    intent.putExtra("sid", merchantsSignInters.getSid());
                    intent.putExtra("mySid", mySid);
                    ActivityCompat.startActivity(SignInterDetailActivity.this, intent, compat.toBundle());
                }
                break;
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

            int width = ScreenUtil.getScreenWidth(SignInterDetailActivity.this);

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
    public void onEndResponse(RespDto<Boolean> body) {
        //1关闭 2上线 3过期
        if (body.isSuccess())
            end.setText("已 结 束");
    }

    @Override
    public void onEndFailure(String msg) {
        Toast.makeText(this, "结束失败", Toast.LENGTH_SHORT).show();
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onLoadInterDetailResponse(RespDto<MerchantsSignInters> body) {
        merchantsSignInters = body.getData();
        initData();
    }

    @Override
    public void onLoadInterDetailFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onDeleteInterDetailResponse(RespDto<Object> body) {
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        finish();
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                SignInterDetailActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent;
        if (isFromMSpace) {
            intent = new Intent(SignInterDetailActivity.this, SignInterHotalActivity.class);
        } else if (tag == SIGNINTERACTIVITY) {
            intent = new Intent(SignInterDetailActivity.this, SignInterActivity.class);
        } else if (tag == SIGNINTERTOTALACTIVITY) {
            intent = new Intent(SignInterDetailActivity.this, SignInterTotalActivity.class);
        } else if (tag == MYSIGNINTERLISTACTIVITY) {
            intent = new Intent(SignInterDetailActivity.this, MySignInterListActivity.class);
        } else if (tag == SUPERMAINACTIVITY) {
            intent = new Intent(SignInterDetailActivity.this, SuperMainActivity.class);
            intent.putExtra("shouldGo", "SpaceFragment");
        } else {
            intent = new Intent(SignInterDetailActivity.this, SuperMainActivity.class);
            intent.putExtra("shouldGo", "SpaceFragment");
        }
        intent.putExtra("mid", merchantsSignInters.getMid() + "");
        ActivityCompat.startActivity(SignInterDetailActivity.this, intent, compat.toBundle());
    }

    @Override
    public void onDeleteInterDetailFailure(String msg) {
        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onPostSignInterResponse(RespDto<Boolean> body) {
        //Todo 发送我感兴趣的拼座
        Boolean data = body.getData();
        if (body == null || data == null) {
            Toast.makeText(this, body.getErrorDesc(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (body != null && data != null && data) {
            Toast.makeText(this, body.getErrorDesc(), Toast.LENGTH_SHORT).show();
        }
        userThumb = merchantsSignInters.getUserThumb();
        nickName = merchantsSignInters.getNickName();
        toUid = merchantsSignInters.getUid();

        //Todo 进入会话框
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                SignInterDetailActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(SignInterDetailActivity.this, ChatActivity.class);
        intent.putExtra("userId", toUid + "");
        intent.putExtra("mid", merchantsSignInters.getMid());
        intent.putExtra("merchant", merchantsSignInters.getMerchantName());
        intent.putExtra("toChatUserID", toUid);
        intent.putExtra("toChatNickName", nickName);
        intent.putExtra("toChatHeaderUrl", userThumb);
        intent.putExtra("inter", inter);
        intent.putExtra("ISFIGHTSEAT", 1);
        ActivityCompat.startActivity(SignInterDetailActivity.this, intent, compat.toBundle());
        EMMessage message = EMMessage.createTxtSendMessage(Constants.TITLE_SEND_INTEREST, toUid + "");
        sendMessage(message);
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
        message.setAttribute(toUid + "", nickName + "," + userThumb);
        message.setAttribute("fromUID", "" + MoreUser.getInstance().getUid());
        message.setAttribute("inter", inter);
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
    public void onPostSignInterFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
