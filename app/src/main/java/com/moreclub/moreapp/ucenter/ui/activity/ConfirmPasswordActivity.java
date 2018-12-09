package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserPassword;
import com.moreclub.moreapp.ucenter.presenter.IUserForgetModifyPasswordLoader;
import com.moreclub.moreapp.ucenter.presenter.UserForgetModifyPasswordLoader;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/16.
 */

public class ConfirmPasswordActivity extends BaseActivity implements IUserForgetModifyPasswordLoader.LoaderUserModifyPassword{

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.comfirm_new_password) EditText comfirmNewPassword;
    @BindView(R.id.reconfirmPassword) EditText reconfirmPassword;
    @BindView(R.id.comfirmPasswordButton) Button comfirmPasswordButton;

    String phone;
    @Override
    protected int getLayoutResource() {
        return R.layout.user_comfirm_password;
    }

    @Override
    protected Class getLogicClazz() {
        return IUserForgetModifyPasswordLoader.class;
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

    private void initData() {
        phone = getIntent().getStringExtra("phone");
    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(ConfirmPasswordActivity.this.getResources().getString(R.string.passwordfind));
        comfirmPasswordButton.setOnClickListener(modifyPasswordListener);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(ConfirmPasswordActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConfirmPasswordActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    View.OnClickListener modifyPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String password1 = comfirmNewPassword.getText().toString();
            String password2 = reconfirmPassword.getText().toString();
            if (password1==null||password1.length()==0){
                Toast.makeText(ConfirmPasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            if (password2==null||password2.length()==0){
                Toast.makeText(ConfirmPasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password1.equals(password2)){
                Toast.makeText(ConfirmPasswordActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                return;
            }

            if (password1.length()<6){
                Toast.makeText(ConfirmPasswordActivity.this,ConfirmPasswordActivity.this.getResources().getString(R.string.password_limit),Toast.LENGTH_SHORT).show();
                return;
            }

            String time = TimeUtils.getTimestampSecond();
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("newpwd",password1);
            map.put("phone", phone);
            map.put("appkey",Constants.MORE_APPKEY);
            map.put("appVersion", Constants.APP_version);
            map.put("fp","1");
            map.put("timestamp",time);


            UserPassword param = new UserPassword();
            param.setNewpwd(password1);
            param.setPhone(phone);
            param.setAppkey(Constants.MORE_APPKEY);
            param.setAppVersion(Constants.APP_version);
            param.setFp("1");
            param.setTimestamp(time);
            String sig = SecurityUtils.createSign(map,"5698250a71fe54ab1ad004890229dfa75efb6d6c");
            param.setSign(sig);

            ((UserForgetModifyPasswordLoader) mPresenter).onModifyPassword(param);
        }
    };

    @Override
    public void onUserModifyPasswordResponse(RespDto<String> response) {

        if (response==null){
            Toast.makeText(ConfirmPasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ConfirmPasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ConfirmPasswordActivity.this, UseLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ConfirmPasswordActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onUserModifyPasswordFailure(String msg) {
        if (msg==null){
            Toast.makeText(ConfirmPasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ConfirmPasswordActivity.this,msg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }
}
