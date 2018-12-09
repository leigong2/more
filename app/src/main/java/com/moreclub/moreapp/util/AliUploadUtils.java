package com.moreclub.moreapp.util;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.AliYunAuth;
import com.moreclub.moreapp.ucenter.model.AliYunAuthParm;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH_TEMP;

/**
 * Created by Captain on 2017/3/18.
 */

public class AliUploadUtils {

    private String accessKeyId;
    private String accessKeySecret;
    private OSS oss;
    private OSSCredentialProvider credentialProvider;
    private Context context;
    private String mBucketName, mUploadObject, mUploadFilePath;
    private String userID;
    private PutObjectSamples.OSSUploadResultListener resultListener;

    public AliUploadUtils(Context context, String bucketName, String uploadObject, String uploadFilePath, String uid, PutObjectSamples.OSSUploadResultListener listener) {
        this.context = context;
        mUploadFilePath = uploadFilePath;
        initLuBanAliYunAuth(bucketName, uploadObject, uid, listener);
    }

    private void initLuBanAliYunAuth(final String bucketName, final String uploadObject, final String uid
            , final PutObjectSamples.OSSUploadResultListener listener) {
        File file = new File(SAVE_REAL_PATH_TEMP);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        Luban.with(context)
                .load(mUploadFilePath)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(SAVE_REAL_PATH_TEMP)             // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        mUploadFilePath = file.getPath();
                        mBucketName = bucketName;
                        mUploadObject = uploadObject;
                        userID = uid;
                        aliYunAuth(userID);
                        resultListener = listener;
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Log.i("zune:", "e = " + e.getMessage());
                    }

                }).launch();    //启动压缩
    }

    public AliUploadUtils(Context context, String bucketName, String uploadObject, String uploadFilePath, PutObjectSamples.OSSUploadResultListener listener) {
        this.context = context;
        mUploadFilePath = uploadFilePath;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", 0 + "");
        String sig = SecurityUtils.createSign(map, Constants.MORE_SECRET);
        AliYunAuthParm param = new AliYunAuthParm();
        param.setSign(sig);
        initLuBanPostImg(param, listener, bucketName, uploadObject);
    }

    private void initLuBanPostImg(final AliYunAuthParm param
            , final PutObjectSamples.OSSUploadResultListener listener, final String bucketName, final String uploadObject) {
        File file = new File(SAVE_REAL_PATH_TEMP);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        Luban.with(context)
                .load(mUploadFilePath)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(SAVE_REAL_PATH_TEMP)             // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        resultListener = listener;
                        mBucketName = bucketName;
                        mUploadObject = uploadObject;
                        postImg(param);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Log.i("zune:", "e = " + e.getMessage());
                    }

                }).launch();    //启动压缩
    }

    private void aliYunAuth(String uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", uid);
        String sig = SecurityUtils.createSign(map, Constants.MORE_SECRET);
        AliYunAuthParm param = new AliYunAuthParm();
        param.setUid(uid);
        param.setSign(sig);
        Callback callback = new Callback<RespDto<AliYunAuth>>() {
            @Override
            public void onResponse(Call<RespDto<AliYunAuth>> call,
                                   Response<RespDto<AliYunAuth>> response) {
                if (response != null && response.body() != null && response.body().getData() != null) {
                    AliYunAuth result = response.body().getData();
                    accessKeyId = result.getAccessKeyId();
                    accessKeySecret = result.getAccessKeySecret();
                    credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIvuH0L4iRT9wE", "BcNFpRkba9n777qyd1FTGmQUM3Vl62");

                    ClientConfiguration conf = new ClientConfiguration();
                    conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                    conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                    conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                    conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                    OSSLog.enableLog();
                    oss = new OSSClient(context.getApplicationContext(), "http://" + Constants.ALI_OSS_END_POOINT, credentialProvider, conf);
                    uploadFile();
                }
            }

            @Override
            public void onFailure(Call<RespDto<AliYunAuth>> call, Throwable t) {
                if ("401".equals(t.getMessage())) {
                    AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    return;
                }
                Logger.d("AliUploadUtils", t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onAliYunAuth(param).enqueue(callback);
    }

    public void uploadFile() {
        new PutObjectSamples(oss, mBucketName, mUploadObject, mUploadFilePath, resultListener).asyncPutObjectFromLocalFile();
    }

    public void postImg(AliYunAuthParm param) {
        Callback callback = new Callback<RespDto<AliYunAuth>>() {
            @Override
            public void onResponse(Call<RespDto<AliYunAuth>> call,
                                   Response<RespDto<AliYunAuth>> response) {
                if (response != null && response.body() != null && response.body().getData() != null) {
                    AliYunAuth result = (AliYunAuth) response.body().getData();
                    accessKeyId = result.getAccessKeyId();
                    accessKeySecret = result.getAccessKeySecret();
                    credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIvuH0L4iRT9wE", "BcNFpRkba9n777qyd1FTGmQUM3Vl62");

                    ClientConfiguration conf = new ClientConfiguration();
                    conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                    conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                    conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                    conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                    OSSLog.enableLog();
                    oss = new OSSClient(context.getApplicationContext(), "http://" + Constants.ALI_OSS_END_POOINT, credentialProvider, conf);
                    uploadFile();
                }
            }

            @Override
            public void onFailure(Call<RespDto<AliYunAuth>> call, Throwable t) {
                if ("401".equals(t.getMessage())) {
                    AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    return;
                }
                Logger.d("AliUploadUtils", t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onAliYunAuthOpen(param).enqueue(callback);
    }
}
