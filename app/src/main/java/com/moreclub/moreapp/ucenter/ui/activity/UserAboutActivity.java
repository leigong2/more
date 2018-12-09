package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/16.
 */

public class UserAboutActivity extends BaseActivity {

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.user_agreement) Button userAgreement;
    @BindView(R.id.version) TextView tvVersion;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_about;
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
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(UserAboutActivity.this.getResources().getString(R.string.settings_about));
        userAgreement.setOnClickListener(goAgreementListener);

        tvVersion.setText(Constants.APP_version);
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserAboutActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    View.OnClickListener goAgreementListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserAboutActivity.this,PlayMoreActivity.class);
            intent.putExtra("webUrl","https://www.moreclub.cn/web/open/agreement.html");
            intent.putExtra("webTitle","用户协议");

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    UserAboutActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            ActivityCompat.startActivity( UserAboutActivity.this, intent, compat.toBundle());
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }
}
