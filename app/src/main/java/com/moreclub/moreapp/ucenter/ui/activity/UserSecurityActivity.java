package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.event.UserBindPhoneEvent;
import com.moreclub.moreapp.ucenter.model.event.UserLogoutEvent;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSecurityActivity extends BaseActivity {

    @BindView(R.id.nav_back)
    ImageButton navBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.tv_bind_phone_num)
    TextView tvBindNum;
    @BindView(R.id.nav_share_btn)
    ImageButton navShareBtn;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightBtn;
    @BindView(R.id.exitAccountButton)
    Button exitAccountButton;
    @BindView(R.id.bind_phone_lay)
    RelativeLayout bindPhoneLay;
    @BindView(R.id.rename_pwd_lay)
    RelativeLayout renamePwdLay;
    @BindView(R.id.question_lay)
    RelativeLayout questionLay;
    private String mBindPhone;
    private boolean hasBind;
    private int count;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_security_activity;
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
        mBindPhone = MoreUser.getInstance().getUserPhone();
        activityTitle.setText("账户设置与安全");
        String token_type = MoreUser.getInstance().getToken_type();
        switch (token_type) {
            case Constants.TOKEN_MOBILE:
                bindPhoneLay.setVisibility(View.GONE);
                renamePwdLay.setVisibility(View.VISIBLE);
                questionLay.setVisibility(View.VISIBLE);
                break;
            case Constants.TOKEN_WEIBO:
                bindPhoneLay.setVisibility(View.VISIBLE);
                renamePwdLay.setVisibility(View.GONE);
                questionLay.setVisibility(View.GONE);
                break;
            case Constants.TOKEN_WEIXIN:
                bindPhoneLay.setVisibility(View.VISIBLE);
                renamePwdLay.setVisibility(View.GONE);
                questionLay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.bind_phone_lay, R.id.rename_pwd_lay, R.id.question_lay, R.id.nav_back, R.id.exitAccountButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /**zune:绑定手机号**/
            case R.id.bind_phone_lay:
                if (isNetworkConnected(UserSecurityActivity.this)) {
                    if (!hasBind && TextUtils.isEmpty(PrefsUtils.getString(UserSecurityActivity.this, Constants.KEY_BIND_NUMBER, ""))
                            && TextUtils.isEmpty(MoreUser.getInstance().getUserPhone())) {
                        AppUtils.pushLeftActivity(UserSecurityActivity.this, BindPhoneActivity.class);
                    } else {
                        //Todo
                        /**zune:这里是手机判定已经绑定之后的逻辑**/
                        Toast.makeText(UserSecurityActivity.this, "您已绑定" + getBind(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserSecurityActivity.this, "联网时才可使用该功能", Toast.LENGTH_SHORT).show();
                }
                break;
            /**zune:修改密码**/
            case R.id.rename_pwd_lay:
                AppUtils.pushLeftActivity(UserSecurityActivity.this, RenamePwdActivity.class);
                break;
            /**zune:密保问题**/
            case R.id.question_lay:
                if (isNetworkConnected(UserSecurityActivity.this)) {
                    AppUtils.pushLeftActivity(UserSecurityActivity.this, SecurityQuestionActivity.class);
                } else {
                    Toast.makeText(UserSecurityActivity.this, "联网时才可使用该功能", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_back:
                UserSecurityActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.exitAccountButton:
                MoreUser.getInstance().clearAccount(UserSecurityActivity.this);
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i("zune:", "success");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("zune:", "i  =" + i + "s = " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.i("zune:", "i  =" + i + "s = " + s);
                    }
                });
                EventBus.getDefault().post(new UserLogoutEvent());
                Intent intent = new Intent(UserSecurityActivity.this, SuperMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                UserSecurityActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                PrefsUtils.setString(this, Constants.KEY_BIND_NUMBER, "");
                break;
        }
    }

    private String getBind() {
        if (!TextUtils.isEmpty(mBindPhone) && mBindPhone.length() >= 11) {
            StringBuffer sb = new StringBuffer(mBindPhone);
            String start = sb.substring(0, 3);
            String end = sb.substring(7, 11);
            return start + "****" + end;
        }
        if (!TextUtils.isEmpty(PrefsUtils.getString(UserSecurityActivity.this, Constants.KEY_BIND_NUMBER, ""))
                && PrefsUtils.getString(UserSecurityActivity.this, Constants.KEY_BIND_NUMBER, "").length() >= 11) {
            StringBuffer sb = new StringBuffer(PrefsUtils.getString(UserSecurityActivity.this, Constants.KEY_BIND_NUMBER, ""));
            String start = sb.substring(0, 3);
            String end = sb.substring(7, 11);
            return start + "****" + end;
        }
        return "";
    }

    private boolean hasBindPhone() {
        if (!TextUtils.isEmpty(mBindPhone) && mBindPhone.length() >= 11) {
            tvBindNum.setVisibility(View.VISIBLE);
            StringBuffer sb = new StringBuffer(mBindPhone);
            String start = sb.substring(0, 3);
            String end = sb.substring(7, 11);
            tvBindNum.setText("已绑定(" + start + "****" + end + ")");
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        hasBind = hasBindPhone();
        count = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBindPhone(UserBindPhoneEvent event) {
        mBindPhone = event.getPhone();
    }
}
