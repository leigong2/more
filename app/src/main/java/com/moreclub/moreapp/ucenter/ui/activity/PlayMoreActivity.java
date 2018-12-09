package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/15.
 */

public class PlayMoreActivity extends BaseActivity {
    private final static String URL = "https://www.moreclub.cn/web/open/playmore.html";

    @BindView(R.id.playmore_wv) WebView webView;
    @BindView(R.id.activity_title) TextView tvTitle;

    @BindString(R.string.playmore_title) String title;

    @BindView(R.id.nav_back) ImageButton naBack;

    private String webUrl;
    private String webTitle;

    @Override
    protected int getLayoutResource() {
        return R.layout.playmore_activity;
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
        webUrl = getIntent().getStringExtra("webUrl");
        webTitle = getIntent().getStringExtra("webTitle");
    }

    private void setupViews() {
         naBack.setOnClickListener(goBackListener);

        if (TextUtils.isEmpty(webUrl)){
            tvTitle.setText(title);
        } else {
            tvTitle.setText(TextUtils.isEmpty(webTitle) ? "" : webTitle);
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }

        if (TextUtils.isEmpty(webUrl)){
            webUrl = URL;
        }

        webView.loadUrl(webUrl);
    }

    private  View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backClick();
        }
    };

    @Override
    public void onBackPressed() {
        backClick();
    }

    private void backClick(){
        Intent intent = getIntent();
        boolean isFromAdert = intent.getBooleanExtra("isFromAdert",false);
        if (isFromAdert){
            AppUtils.pushLeftActivity(this,SuperMainActivity.class);
            finish();
        } else {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
}
