package com.moreclub.moreapp.ucenter.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.moreclub.moreapp.ucenter.model.event.UserLogoutEvent;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.DataCleanManager;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH;

/**
 * Created by Captain on 2017/3/16.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.exitAccountButton)
    Button exitAccountButton;

    @BindView(R.id.account_security_lay)
    RelativeLayout accountSecuritylay;
    @BindView(R.id.account_secret_lay)
    RelativeLayout accountSecretlay;
    @BindView(R.id.question_lay)
    RelativeLayout questionLay;
    @BindView(R.id.clear_lay)
    RelativeLayout clearLay;
    @BindView(R.id.about_lay)
    RelativeLayout aboutLay;
    @BindView(R.id.aboutName)
    TextView aboutName;
    @BindView(R.id.tv_setting_cache)
    TextView tvSettingCache;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_setting;
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
        activityTitle.setText(SettingActivity.this.getResources().getString(R.string.setting_title));
        exitAccountButton.setOnClickListener(exitListener);
        accountSecuritylay.setOnClickListener(accountSecurityListener);
        accountSecretlay.setOnClickListener(accountSecretListener);
        questionLay.setOnClickListener(questionListener);
        aboutLay.setOnClickListener(aboutListener);
        clearLay.setOnClickListener(clearListener);
    }


    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SettingActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    View.OnClickListener exitListener = new View.OnClickListener() {

        @TargetApi(Build.VERSION_CODES.ECLAIR)
        @Override
        public void onClick(View v) {
            MoreUser.getInstance().clearAccount(SettingActivity.this);
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
            Intent intent = new Intent(SettingActivity.this, SuperMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SettingActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            PrefsUtils.setString(SettingActivity.this, Constants.KEY_PHONE_USER, "");
            PrefsUtils.setString(SettingActivity.this, Constants.KEY_BIND_NUMBER, "");
        }
    };

    View.OnClickListener accountSecurityListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AppUtils.pushLeftActivity(SettingActivity.this, UserSecurityActivity.class);
        }
    };
    View.OnClickListener accountSecretListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.pushLeftActivity(SettingActivity.this, UserSecretActivity.class);
        }
    };
    View.OnClickListener questionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.pushLeftActivity(SettingActivity.this, QuestionFeedbackActivity.class);
        }
    };

    View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File filesDir = getFilesDir();
            File cacheDir = getCacheDir();
            File filesDb = new File("/data/data/" + getPackageName() + "/databases");
            File externalCacheDir = getExternalCacheDir();
            File pictureDir = new File(SAVE_REAL_PATH);
            long start = getDirSize(filesDir) + getDirSize(cacheDir) + getDirSize(filesDb) + getDirSize(externalCacheDir);
            DataCleanManager.cleanSharedPreference(SettingActivity.this);
            DataCleanManager.cleanDatabases(SettingActivity.this);
            DataCleanManager.cleanFiles(SettingActivity.this);
            DataCleanManager.cleanInternalCache(SettingActivity.this);
            DataCleanManager.cleanExternalCache(SettingActivity.this);
            DataCleanManager.cleanCustomCache(SAVE_REAL_PATH);
            long end = getDirSize(filesDir) + getDirSize(cacheDir) + getDirSize(filesDb)
                    + getDirSize(externalCacheDir) + getDirSize(pictureDir);
            String size = format(start - end);
            Toast.makeText(SettingActivity.this, "共清理" + size + "垃圾", Toast.LENGTH_SHORT).show();
        }
    };

    private String format(long size) {
        if (size > 1024) {
            return size / 1024 + "K";
        } else if (size > 1024 * 1024) {
            return size / 1024 / 1024 + "M";
        }
        return size + "B";
    }

    View.OnClickListener aboutListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AppUtils.pushLeftActivity(SettingActivity.this, UserAboutActivity.class);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    private long getCache() {
        File filesDir = getFilesDir();
        File cacheDir = getCacheDir();
        File filesDb = new File("/data/data/" + getPackageName() + "/databases");
        File externalCacheDir = getExternalCacheDir();
        long size = getDirSize(filesDir) + getDirSize(cacheDir) + getDirSize(filesDb) + getDirSize(externalCacheDir);
        return size;
    }

    private long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }
}
