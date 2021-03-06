package com.moreclub.moreapp.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.moreapp.app.MainApp;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Captain on 2017/3/15.
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler, OnItemClickListener {
    public static final String TAG = WXEntryActivity.class.getSimpleName();
    public static String code;
    public static BaseResp resp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean handleIntent = MainApp.api.handleIntent(getIntent(), this);
        //下面代码是判断微信分享后返回WXEnteryActivity的，如果handleIntent==false,说明没有调用IWXAPIEventHandler，则需要在这里销毁这个透明的Activity;
        if (handleIntent == false) {
            Log.d(TAG, "onCreate: " + handleIntent);
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MainApp.api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onReq: ");
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp != null) {
            resp = baseResp;
            code = ((SendAuth.Resp) baseResp).code; //即为所需的code
            Log.d(TAG, "onResp: 成功"+code);
        }
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d(TAG, "onResp: 成功");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.d(TAG, "onResp: 用户取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.d(TAG, "onResp: 发送请求被拒绝");
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(Object o, int position) {

    }



}