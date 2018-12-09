package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserPassword;
import com.moreclub.moreapp.ucenter.model.event.UserLogoutEvent;
import com.moreclub.moreapp.ucenter.presenter.IRenameLoader;
import com.moreclub.moreapp.ucenter.presenter.RenameLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenamePwdActivity extends BaseActivity implements IRenameLoader.RenameBinder {


    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_check_number)
    EditText etCheckNumber;
    @BindView(R.id.view_error_line1)
    View viewErrorLine1;
    @BindView(R.id.view_error_line2)
    View viewErrorLine2;
    @BindView(R.id.tv_old_phone)
    TextView tvOldPhone;
    @BindView(R.id.et_old_phone_number)
    EditText etOldPhoneNumber;
    @BindView(R.id.tv_error1)
    TextView tvError1;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_error2)
    TextView tvError2;
    @BindView(R.id.check)
    TextView check;
    @BindView(R.id.view_error_line3)
    View viewErrorLine3;
    @BindView(R.id.tv_error3)
    TextView tvError3;
    @BindView(R.id.rename_make_sure)
    Button renameMakeSure;
    private UserPassword mUserPassword = new UserPassword();
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvError1.setVisibility(View.INVISIBLE);
            tvError2.setVisibility(View.INVISIBLE);
            tvError3.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected Class getLogicClazz() {
        return IRenameLoader.class;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.rename_pwd_activity;
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
        activityTitle.setText("修改密码");
        etPhoneNumber.addTextChangedListener(watcher);
        etCheckNumber.addTextChangedListener(watcher);
        etOldPhoneNumber.addTextChangedListener(watcher);
    }

    @OnClick({R.id.nav_back, R.id.rename_make_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                RenamePwdActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.rename_make_sure:
                if (TextUtils.isEmpty(etCheckNumber.getText().toString().trim())
                        || TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())
                        || TextUtils.isEmpty(etOldPhoneNumber.getText().toString().trim())) {
                    tvError1.setVisibility(View.VISIBLE);
                    tvError1.setText("密码不能为空");
                    tvError2.setVisibility(View.VISIBLE);
                    tvError2.setText("密码不能为空");
                    tvError3.setVisibility(View.VISIBLE);
                    tvError3.setText("密码不能为空");
                } else if (!etCheckNumber.getText().toString().trim().equals(etPhoneNumber.getText().toString().trim())) {
                    tvError3.setVisibility(View.VISIBLE);
                    tvError3.setText("两次输入新密码不一致");
                } else if (etOldPhoneNumber.getText().toString().trim().equals(etPhoneNumber.getText().toString().trim())) {
                    tvError2.setVisibility(View.VISIBLE);
                    tvError2.setText("新旧密码一致");
                } else if (etCheckNumber.getText().toString().length() < 6) {
                    tvError2.setVisibility(View.VISIBLE);
                    tvError2.setText("密码长度至少为6");
                } else if (!isNetworkConnected(RenamePwdActivity.this)) {
                    Toast.makeText(RenamePwdActivity.this, "无网络连接,请稍后再试", Toast.LENGTH_SHORT).show();
                } else {
                    initUserPassword();
                    ((RenameLoader) mPresenter).onRenamePwd(mUserPassword, MoreUser.getInstance().getAccess_token());
                }
                break;
        }
    }

    private void initUserPassword() {
        String oldpwd = etOldPhoneNumber.getText().toString().trim();
        String newpwd = etCheckNumber.getText().toString().trim();
        String phone = PrefsUtils.getString(RenamePwdActivity.this, Constants.KEY_PHONE_USER, "");
        mUserPassword.setOldpwd(oldpwd);
        mUserPassword.setAppkey(Constants.MORE_APPKEY);
        mUserPassword.setNewpwd(newpwd);
        mUserPassword.setPhone(phone);
    }


    public String generateModifyPasswdSign(UserPassword questreq, boolean fp) {
        HashMap<String, String> map = new HashMap<String, String>();
        String appkey = questreq.getAppkey();

        map.put("appkey", appkey);
        map.put("appVersion", questreq.getAppVersion());
        map.put("timestamp", questreq.getTimestamp() + "");
        map.put("newpwd", questreq.getNewpwd());
        if (!fp) {
            map.put("uid", questreq.getUid() + "");
            map.put("fp", "0");
            map.put("oldpwd", questreq.getOldpwd());
        } else {
            map.put("fp", "splash1");
            map.put("phone", questreq.getPhone());
        }
        return SecurityUtils.createSign(map, Constants.MORE_SECRET);
    }

    @Override
    public void onRenameResponse(RespDto response) {

        Toast.makeText(RenamePwdActivity.this, "密码修改成功,请重新登录", Toast.LENGTH_SHORT).show();
        MoreUser.getInstance().clearAccount(RenamePwdActivity.this);
        EventBus.getDefault().post(new UserLogoutEvent());
        Intent intent = new Intent(RenamePwdActivity.this, SuperMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        RenamePwdActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        viewErrorLine3.setVisibility(View.VISIBLE);
        if (response == null) {
            tvError3.setVisibility(View.VISIBLE);
            tvError3.setText("密码修改失败");
        } else {
            tvError3.setVisibility(View.VISIBLE);
            tvError3.setText("密码修改成功");
        }
    }

    @Override
    public void onRenameFailure(String msg) {
        if (msg == null) {
            viewErrorLine3.setVisibility(View.VISIBLE);
            tvError3.setVisibility(View.VISIBLE);
            tvError3.setText("密码修改失败");
        } else if (msg.equals("密码不正确")) {
            viewErrorLine1.setVisibility(View.VISIBLE);
            tvError1.setVisibility(View.VISIBLE);
            tvError1.setText("原" + msg);
        } else {
            viewErrorLine3.setVisibility(View.VISIBLE);
            tvError3.setVisibility(View.VISIBLE);
            tvError3.setText("msg");
        }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(RenamePwdActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
