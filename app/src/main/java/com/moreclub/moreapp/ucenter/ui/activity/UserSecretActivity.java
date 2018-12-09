package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.UserSignSetting;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;
import com.moreclub.moreapp.ucenter.presenter.IUserSecretLoader;
import com.moreclub.moreapp.ucenter.presenter.UserSecretLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSecretActivity extends BaseActivity implements IUserSecretLoader.LoaderUserSecretDataBinder{

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.tb_toggle_auto)
    ImageView tbToggleAuto;
    @BindView(R.id.tb_toggle_secret)
    ImageView tbToggleSecret;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_secret_activity;
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
        if (PrefsUtils.getBoolean(UserSecretActivity.this,Constants.KEY_AUTO_SIGN,true)) {
            tbToggleAuto.setSelected(true);
        } else {
            tbToggleAuto.setSelected(false);
        }
        if (PrefsUtils.getBoolean(UserSecretActivity.this,Constants.KEY_SECRET_SIGN,true)) {
            tbToggleSecret.setSelected(true);
        } else {
            tbToggleSecret.setSelected(false);
        }
        tbToggleAuto.setClickable(false);
        tbToggleSecret.setClickable(false);
        loadData();
    }

    private void loadData(){
        ((UserSecretLoader)mPresenter).loadSignSetting(""+MoreUser.getInstance().getUid());
    }

    @Override
    protected Class getLogicClazz() {
        return IUserSecretLoader.class;
    }

    private void setupView() {
        activityTitle.setText("隐私");
    }

    @OnClick({R.id.nav_back, R.id.auto_lay,R.id.account_security_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                UserSecretActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.auto_lay:
                tbToggleAuto.setSelected(!tbToggleAuto.isSelected());
                if (tbToggleAuto.isSelected()) {
                    //Todo
                    /**zune:处理自动签到逻辑**/
                    PrefsUtils.setBoolean(UserSecretActivity.this, Constants.KEY_AUTO_SIGN,true);
                    MoreUser.getInstance().setAutoSignSuccess(false);
                    PrefsUtils.setBoolean(this, com.moreclub.moreapp.ucenter.constant.Constants.KEY_AUTO_SIGN_POP,false);
                    AutoSigninSettings param = new AutoSigninSettings();
                    param.setUid(""+MoreUser.getInstance().getUid());
                    param.setBarVisiable(null);
                    param.setAutoSignin(true);
                    ((UserSecretLoader)mPresenter).onSecretSign(""+MoreUser.getInstance().getUid(),param);
                } else {
                    AutoSigninSettings param = new AutoSigninSettings();
                    param.setUid(""+MoreUser.getInstance().getUid());
                    param.setAutoSignin(false);
                    param.setBarVisiable(null);
                    PrefsUtils.setBoolean(UserSecretActivity.this, Constants.KEY_AUTO_SIGN,false);
                    MoreUser.getInstance().setAutoSignSuccess(true);
                    PrefsUtils.setBoolean(this, com.moreclub.moreapp.ucenter.constant.Constants.KEY_AUTO_SIGN_POP,true);
                    ((UserSecretLoader)mPresenter).onSecretSign(""+MoreUser.getInstance().getUid(),param);
                }
                break;
            case R.id.account_security_lay:
                tbToggleSecret.setSelected(!tbToggleSecret.isSelected());
                if (tbToggleSecret.isSelected()) {
                    //Todo
                    /**zune:处理签到仅酒吧可见逻辑**/
                    AutoSigninSettings param = new AutoSigninSettings();
                    param.setUid(""+MoreUser.getInstance().getUid());
                    param.setAutoSignin(null);
                    param.setBarVisiable(true);
                    PrefsUtils.setBoolean(UserSecretActivity.this, Constants.KEY_SECRET_SIGN,true);
                    ((UserSecretLoader)mPresenter).onSecretSign(""+MoreUser.getInstance().getUid(),param);
                } else {
                    AutoSigninSettings param = new AutoSigninSettings();
                    param.setUid(""+MoreUser.getInstance().getUid());
                    param.setBarVisiable(false);
                    param.setAutoSignin(null);
                    PrefsUtils.setBoolean(UserSecretActivity.this, Constants.KEY_SECRET_SIGN,false);
                    ((UserSecretLoader)mPresenter).onSecretSign(""+MoreUser.getInstance().getUid(),param);
                }
                break;
        }
    }

    @Override
    public void onSecretSignResponse(RespDto<String> response) {

    }

    @Override
    public void onSecretSignFailure(String msg) {

    }

    @Override
    public void onSignSettingResponse(RespDto<UserSignSetting> response) {
        UserSignSetting result = response.getData();
        if (result.isAutoSignin()) {
            tbToggleAuto.setSelected(true);
        } else {
            tbToggleAuto.setSelected(false);
        }
        if (result.isBarVisiable()) {
            tbToggleSecret.setSelected(true);
        } else {
            tbToggleSecret.setSelected(false);
        }
    }

    @Override
    public void onSignSettingFailure(String msg) {

    }
}
