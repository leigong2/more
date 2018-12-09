package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.log.Logger;
import com.moreclub.common.util.CountDownTimerUtils;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.BindedUser;
import com.moreclub.moreapp.ucenter.model.UserLogin;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;
import com.moreclub.moreapp.ucenter.model.event.UserLoginEvent;
import com.moreclub.moreapp.ucenter.presenter.CheckCodeLoader;
import com.moreclub.moreapp.ucenter.presenter.ICheckCodeLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/15.
 */

public class UserRegActivity extends BaseActivity implements ICheckCodeLoader.LoadCheckCodeBinder {

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.regButton)
    Button regButton;
    @BindView(R.id.smsCodeButton)
    Button smsCodeButton;
    @BindView(R.id.regPhone)
    EditText regPhone;
    @BindView(R.id.regCode)
    EditText regCode;
    @BindView(R.id.regPassWd)
    EditText regPassWd;
    @BindView(R.id.regPhoneError)
    TextView regPhoneError;
    @BindView(R.id.regCodeError)
    TextView regCodeError;
    @BindView(R.id.regPassWdError)
    TextView regPassWdError;
    CountDownTimerUtils mCountDownTimerUtils;
    private BindedUser bindedUser;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_reg;
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
    }

    @Override
    protected Class getLogicClazz() {
        return ICheckCodeLoader.class;
    }

    private void setupViews() {
        naBack.setOnClickListener(goBack);
        activityTitle.setText("新用户注册");
        regButton.setOnClickListener(goRegHeaderNick);
        smsCodeButton.setOnClickListener(smsCodeListener);

    }

    View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserRegActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };
    View.OnClickListener goRegHeaderNick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String phone = regPhone.getText().toString();
            String vCode = regCode.getText().toString();
            String password = regPassWd.getText().toString();
            if (phone == null || phone.length() == 0) {
                Toast.makeText(UserRegActivity.this, "请输入你的手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password == null || password.length() == 0) {
                Toast.makeText(UserRegActivity.this, "请输入你的密码", Toast.LENGTH_SHORT).show();
                return;
            }

            if (vCode == null || vCode.length() == 0) {
                Toast.makeText(UserRegActivity.this, "请输入你的验证码", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!SecurityUtils.isMobileNO(phone)) {
                Toast.makeText(UserRegActivity.this, UserRegActivity.this.getResources().getString(R.string.phone_format_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(UserRegActivity.this, UserRegActivity.this.getResources().getString(R.string.password_limit), Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("phone", phone);
            map.put("vcode", vCode);
            map.put("appkey", Constants.MORE_APPKEY);
            String sig = SecurityUtils.createSign(map, "5698250a71fe54ab1ad004890229dfa75efb6d6c");
            ((CheckCodeLoader) mPresenter).userCheckSMSCode(phone, vCode, Constants.MORE_APPKEY, sig);
        }
    };

    View.OnClickListener smsCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String phone = regPhone.getText().toString();
            if (phone == null || phone.length() == 0) {
                Toast.makeText(UserRegActivity.this, "请输入你的手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("phone", phone);
            map.put("appkey", Constants.MORE_APPKEY);
            String sig = SecurityUtils.createSign(map, "5698250a71fe54ab1ad004890229dfa75efb6d6c");
            ((CheckCodeLoader) mPresenter).userGetSMSCode(phone, Constants.MORE_APPKEY, sig);
            mCountDownTimerUtils = new CountDownTimerUtils(UserRegActivity.this, smsCodeButton, 60000, 1000);
            mCountDownTimerUtils.start();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
    }

    @Override
    public void onUseGetSMSCodeResponse(RespDto response) {
        if (response == null) {
            regCodeError.setVisibility(View.VISIBLE);
            regCodeError.setText("获取验证码失败");
            if (mCountDownTimerUtils != null) {
                mCountDownTimerUtils.cancel();
                mCountDownTimerUtils.onFinish();
            }
        } else {
            regCodeError.setVisibility(View.VISIBLE);
            regCodeError.setText("验证码已发送到你的手机");
        }
    }

    @Override
    public void onUseGetSMSCodeFailure(String msg) {
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
            mCountDownTimerUtils.onFinish();
        }
        if (msg == null) {
            regCodeError.setVisibility(View.VISIBLE);
            regCodeError.setText("获取验证码失败");
        } else {
            regCodeError.setVisibility(View.VISIBLE);
            regCodeError.setText(msg);
        }
        Logger.d("UserRegActivity", msg);
    }

    @Override
    public void onUseCheckSMSCodeFailure(String msg) {
        if (msg == null) {
            Toast.makeText(UserRegActivity.this, "你输入的验证码有误", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UserRegActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
        Logger.d("UserRegActivity", msg);
    }

    @Override
    public void onUseCheckSMSCodeResponse(RespDto response) {
        String phone = regPhone.getText().toString();
        ((CheckCodeLoader) mPresenter).userCheckBindedPhone(phone);
    }

    private void regditNow(String phone, String vCode, String password) {
        String time = TimeUtils.getTimestampSecond();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("passwd", password);
        map.put("appkey", Constants.MORE_APPKEY);
        map.put("appVersion", Constants.APP_version);
        map.put("loginTime", time);
        map.put("loginType", "0");
        map.put("machine", "android");
        map.put("opSystem", "android");
        map.put("version", Constants.APP_version);
        map.put("vcode", vCode);

        UserLogin param = new UserLogin();
        param.setPhone(phone);
        param.setPasswd(password);
        param.setAppkey(Constants.MORE_APPKEY);
        param.setAppVersion(Constants.APP_version);
        param.setLoginTime(time);
        param.setLoginType("0");
        param.setMachine("android");
        param.setOpSystem("android");
        param.setVersion(Constants.APP_version);
        param.setVcode(vCode);

        String sig = SecurityUtils.createSign(map, "5698250a71fe54ab1ad004890229dfa75efb6d6c");
        param.setSign(sig);
        param.setTimestamp(time);
        ((CheckCodeLoader) mPresenter).onReg(param);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(UserRegActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onUserGetSecurityQuestionResponse(RespDto response) {

    }

    @Override
    public void onUserGetSecurityQuestionFailure(String msg) {

    }

    @Override
    public void onUserRegResponse(RespDto response) {
        if (response == null) {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        } else {
            UserLoginInfo result = (UserLoginInfo) response.getData();
            MoreUser.getInstance().setShowCouponDialog(true);
            MoreUser.getInstance().setUid(result.getUid());
            MoreUser.getInstance().setNickname(result.getNickname());
            MoreUser.getInstance().setAccess_token(result.getAccess_token());
            MoreUser.getInstance().setRefresh_token(result.getRefresh_token());
            MoreUser.getInstance().setToken_type(Constants.TOKEN_MOBILE);
            MoreUser.getInstance().setThumb(result.getThumb());
            MoreUser.getInstance().setUserPhone(result.getPhone());
            MoreUser.getInstance().setThdThumb(result.getThdThumb());
            MoreUser.getInstance().updateAccount(this);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SuperMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            String thumb;
            if (!TextUtils.isEmpty(result.getThumb())) {
                thumb = result.getThumb();
            } else {
                thumb = result.getThdThumb();
            }
            EventBus.getDefault().post(new UserLoginEvent(thumb));
            this.finish();
        }
    }

    @Override
    public void onUserRegFailure(String msg) {
        if (msg == null) {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUseCheckBindedPhoneCodeFailure(String msg) {

    }

    @Override
    public void onUseCheckBindedPhoneResponse(RespDto response) {
        if (response == null) return;
        String phone = regPhone.getText().toString();
        String vCode = regCode.getText().toString();
        String password = regPassWd.getText().toString();
        if (!response.isSuccess() && response.getErrorCode() != null && response.getErrorCode().equals("E00036")) {
            //Todo 直接注册
            regditNow(phone, vCode, password);
        } else if (response.isSuccess()) {
            //Todo 进入昵称设置 再注册
            goNickReg(phone, vCode, password);
        }
    }

    private void goNickReg(String phone, String vCode, String password) {
        Intent intent = new Intent(UserRegActivity.this, UserRegHeaderNickActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", vCode);
        intent.putExtra("password", password);
        UserRegActivity.this.startActivity(intent);
    }
}
