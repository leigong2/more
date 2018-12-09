package com.moreclub.moreapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.ShareParam;
import com.moreclub.moreapp.main.model.ShareRequestResult;
import com.moreclub.moreapp.main.presenter.IShareDataLoader;
import com.moreclub.moreapp.main.presenter.LoaderShareData;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.concurrent.ExecutionException;

/**
 * Created by Captain on 2017/4/19.
 */

public class ShareUtils implements IShareDataLoader.LoadShareDataBinder, IWeiboHandler.Response {

    private Context mContext;
    private int mShareType;
    private int mChannel;
    private int mModule;
    private String mModuleID;

    private BasePresenter mPresenter;

    private WechatShareManager mShareManager;

    private IShareCallBackLinstener shareCallBack;


    /**
     * @param context
     * @param shareType 0-朋友圈，微博,1-微信好友
     * @param channel   0-app,1-微信,2,微博,3-qq
     * @param module    0-活动,1-资讯,2-app,3-商家,4-套餐,5-拼座,6-黑卡,7-橙卡,8-Ugc
     * @param moduleID
     */
    public ShareUtils(Context context, int shareType, int channel, int module, String moduleID) {

        mContext = context;
        mShareType = shareType;
        mChannel = channel;
        mModule = module;
        mModuleID = moduleID;
        mPresenter = LogicProxy.getInstance().bind(IShareDataLoader.class, this);

        mShareManager = WechatShareManager.getInstance(mContext);
    }

    public void share() {

        if (mShareType == 1 || mShareType == 0) {
            if (!isWebchatAvaliable()) {
                return;
            }
        }

        String time = TimeUtils.getTimestampSecond();
        ShareParam param = new ShareParam();
        param.setAppVersion("1.0");
        param.setChannel(mChannel);
        param.setShareType(mShareType);
        param.setUid("" + MoreUser.getInstance().getUid());
        param.setTimestamp(time);
        param.setModule(mModule);
        param.setModuleId(mModuleID);

        ((LoaderShareData) mPresenter).share(param);
    }

    @Override
    public void onShareResponse(RespDto response) {

        ShareRequestResult result = (ShareRequestResult) response.getData();

        if (mChannel == 1) {
            WechatShareManager.ShareContentWebpage mShareContent = (WechatShareManager.ShareContentWebpage)
                    mShareManager.getShareContentWebpag(result.getTitle(), result.getDescription(),
                            result.getShareUrl(), result.getPicUrl());

            new WXShareTask(mShareContent, mShareType).execute();
        } else if (mChannel == 2) {
            new WBShareTask(result.getTitle(), result.getDescription(), result.getPicUrl(), result.getShareUrl()).execute();
        }

        if (shareCallBack!=null){
            shareCallBack.shareComlete(true);
        }
    }

    @Override
    public void onShareFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
            return;
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    class WXShareTask extends AsyncTask<Void, Void, String> {

        WechatShareManager.ShareContentWebpage shareParam;
        int type;

        public WXShareTask(WechatShareManager.ShareContentWebpage param, int t) {
            shareParam = param;
            type = t;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (type == 1) {
                    mShareManager.shareByWebchat(shareParam, WechatShareManager.WECHAT_SHARE_TYPE_TALK);
                } else {
                    mShareManager.shareByWebchat(shareParam, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                }
                return "1";
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
//                Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

    class WBShareTask extends AsyncTask<Void, Void, String> {

        String wbTitle;
        String wbContent;
        String wbImgPath;
        String wbWebUrl;

        public WBShareTask(String title, String content, String imgPath, String webUrl) {
            wbTitle = title;
            wbContent = content;
            wbImgPath = imgPath;
            wbWebUrl = webUrl;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                // 1. 初始化微博的分享消息
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.textObject = getWBTextObj(wbContent + wbWebUrl);
                weiboMessage.textObject.actionUrl = wbWebUrl;
                weiboMessage.textObject.title = wbTitle;
                weiboMessage.imageObject = getWBImageObj(wbImgPath);

                // 2. 初始化从第三方到微博的消息请求
                SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                // 用transaction唯一标识一个请求
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.multiMessage = weiboMessage;
                // 3. 发送请求消息到微博，唤起微博分享界面
                Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mContext);
                String token = "";
                if (accessToken != null) {
                    token = accessToken.getToken();
                }
                MainApp.mWeiboShareAPI.sendRequest((Activity) mContext, request, MainApp.mAuthInfo, token, new WeiboAuthListener() {

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        // TODO Auto-generated method stub

//                        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
//                        AccessTokenKeeper.writeAccessToken(mContext, newToken);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                return "1";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
//                Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(mContext, "成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(mContext, "取消", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(mContext,
                            "失败" + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private boolean isWebchatAvaliable() {
        //检测手机上是否安装了微信
        try {
            mContext.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getWBImageObj(String imagePath) throws ExecutionException, InterruptedException {
        ImageObject imageObject = new ImageObject();

        Bitmap bitmap = Glide.with(mContext)
                .load(imagePath)
                .asBitmap()
                .centerCrop()
                .into(100, 100)
                .get();

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.main_more_logo);
        }

        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        imageObject.setImageObject(bitmap);

        return imageObject;
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getWBTextObj(String c) {
        TextObject textObject = new TextObject();
        textObject.text = c;

        return textObject;
    }

    /**
     * 分享回调
     * @param back
     */
    public void setOnShareCallBack(IShareCallBackLinstener back){
        shareCallBack = back;
    }

    public interface IShareCallBackLinstener{

        void shareComlete(boolean sucess);

    }

}
