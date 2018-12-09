package com.moreclub.moreapp.main.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.Constants;

import java.io.File;

import static com.moreclub.moreapp.main.constant.Constants.IS_UPDATE_MORE;

public class DialogInstallActivity extends Activity {

    private ImageView close;
    private TextView cancel;
    private TextView makeSure;
    private String filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_app);
        initView();
    }

    private void initView() {
        filePath = (new File(getExternalCacheDir(), "More" + Constants.APP_version + ".apk")).getPath();
        close = (ImageView) findViewById(R.id.closeW);
        cancel = (TextView) findViewById(R.id.cancel);
        makeSure = (TextView) findViewById(R.id.btn_make_sure);
        makeSure.setText("立即安装");
        makeSure.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        View.OnClickListener closeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsUtils.setBoolean(DialogInstallActivity.this, IS_UPDATE_MORE, false);
                finish();
            }
        };
        close.setOnClickListener(closeClickListener);
        cancel.setOnClickListener(closeClickListener);
        View.OnClickListener sureClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApk(DialogInstallActivity.this, new File(filePath));
                PrefsUtils.setBoolean(DialogInstallActivity.this, IS_UPDATE_MORE, true);
            }
        };
        makeSure.setOnClickListener(sureClickListener);
    }

    private void installApk(Context context, File apkPath) {
        Log.i("zune:", "file name = " + apkPath.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(context, "com.mydomain.fileprovider", apkPath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkPath), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    private void onHome() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
