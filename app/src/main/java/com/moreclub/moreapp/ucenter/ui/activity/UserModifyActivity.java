package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserModify;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.presenter.IUserInfoModifyLoader;
import com.moreclub.moreapp.ucenter.presenter.UserInfoModifyLoader;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.moreapp.util.AliUploadUtils;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.PutObjectSamples;
import com.moreclub.moreapp.util.Utils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/13.
 */

public class UserModifyActivity extends BaseActivity implements OnItemClickListener, OnDismissListener, IUserInfoModifyLoader.UserInfoModifyDataBinder {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private static final int USER_FROM_CITY = 4;

    private static final String CROPPED_IMAGE_NAME = "UserCropImage";

    @BindView(R.id.header_img)
    ImageView headerIV;
    @BindView(R.id.sex_lay)
    LinearLayout sexLayout;
    @BindView(R.id.address_lay)
    LinearLayout addressLayout;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.user_address)
    TextView userAddressText;
    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.saveButton)
    Button saveButton;
    @BindView(R.id.user_nick)
    EditText userNickEdit;
    @BindView(R.id.user_PersonalMark)
    EditText userPersonalMark;

    private AlertView sexAlertView;
    private AlertView photoAlertView;
    private Uri imgUri;
    private String userSexSelect;
    private String userCitySelect;
    private String userThumbSelect;
    UserModify param;
    boolean isSaveUserSex;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_modify_info;
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

    @Override
    protected Class getLogicClazz() {
        return IUserInfoModifyLoader.class;
    }

    private void setupView() {

        headerIV.setOnClickListener(headerImageClickListener);
        sexLayout.setOnClickListener(sexClickListener);
        addressLayout.setOnClickListener(addressClickListener);
        naBack.setOnClickListener(goBack);
        saveButton.setOnClickListener(saveClickListener);
        activityTitle.setText("修改个人资料");


        userNickEdit.setText(MoreUser.getInstance().getNickname());
        if (!TextUtils.isEmpty(MoreUser.getInstance().getThumb())) {
            Glide.with(this)
                    .load(MoreUser.getInstance().getThumb())
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .placeholder(R.drawable.ucenter_head_profile)
                    .dontAnimate()
                    .into(headerIV);
        } else {
            Glide.with(this)
                    .load("")
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .placeholder(R.drawable.ucenter_head_profile)
                    .dontAnimate()
                    .into(headerIV);
        }

        userSexSelect = "" + MoreUser.getInstance().getSex();
        userThumbSelect = MoreUser.getInstance().getThumb();

        ((UserInfoModifyLoader) mPresenter).loadUserInfo("" + MoreUser.getInstance().getUid());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(UserModifyActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    /**
     * 性别选择
     */
    View.OnClickListener sexClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (sexAlertView == null) {
                sexAlertView = new AlertView("选择性别", null, "取消", null,
                        new String[]{" 男", "女"},
                        UserModifyActivity.this, AlertView.Style.ActionSheet, UserModifyActivity.this);
            }
            if (!sexAlertView.isShowing() && !UserModifyActivity.this.isFinishing())
                sexAlertView.show();
        }
    };


    /**
     * 地址选择
     */
    View.OnClickListener addressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyActivity.this, UserModifyAddressActivity.class);

            UserModifyActivity.this.startActivityForResult(intent, USER_FROM_CITY);
        }
    };

    /**
     * 头像选择
     */
    View.OnClickListener headerImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            photoAlertView = new AlertView("上传头像", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    UserModifyActivity.this, AlertView.Style.ActionSheet, UserModifyActivity.this);

            if (!photoAlertView.isShowing() && !UserModifyActivity.this.isFinishing())
                photoAlertView.show();
        }
    };

    /**
     * 保存
     */
    View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String tempNickName = "";
            if (userNickEdit.getText().toString().length() > 0) {
                tempNickName = userNickEdit.getText().toString();
            } else {
                tempNickName = MoreUser.getInstance().getNickname();
            }

            String tempPersonalMark = "";
            if (userPersonalMark.getText().toString().length() > 0) {
                tempPersonalMark = userPersonalMark.getText().toString();
            } else {
                tempPersonalMark = MoreUser.getInstance().getPersonalMark();
            }

            String time = TimeUtils.getTimestampSecond();
            String phoneIP = Utils.getIpAddress();

            if (phoneIP == null || phoneIP.length() == 0) {
                phoneIP = Utils.getLocalIpAddress(UserModifyActivity.this);
            }
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("appkey", Constants.MORE_APPKEY);
            map.put("appVersion", Constants.APP_version);
            map.put("uid", "" + MoreUser.getInstance().getUid());
            map.put("loginTime", time);
            map.put("loginIp", phoneIP);

            String sig = SecurityUtils.createSign(map, Constants.MORE_SECRET);
            param = new UserModify();
            param.setAppkey(Constants.MORE_APPKEY);
            param.setAppVersion(Constants.APP_version);
            param.setUid("" + MoreUser.getInstance().getUid());
            param.setLoginTime(time);
            param.setLoginIp(phoneIP);
            param.setSign(sig);
            param.setCity(userCitySelect);
            if (!isSaveUserSex)
                param.setSex(userSexSelect);
            param.setThumb(userThumbSelect);
            param.setNickName(tempNickName);
            param.setPersonalMark(tempPersonalMark);
            param.setTimestamp(time);
            ((UserInfoModifyLoader) mPresenter).updateUserInfo(param);

        }
    };

    View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserModifyActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }

    private void closeKeyboard() {
//        //关闭软键盘
//        imm.hideSoftInputFromWindow(etName.getWindowToken(),0);
//        //恢复位置
//        mAlertViewExt.setMarginBottom(0);
    }

    @Override
    public void onItemClick(Object o, int position) {
        closeKeyboard();
        if (o == photoAlertView) {
            if (position == 0) {
                File file = new File(Environment
                        .getExternalStorageDirectory(), "avatar_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".png");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                    imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else {
                    imgUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                }
            } else if (position == 1) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_FILE);
            }
        } else if (o == sexAlertView) {
            if (position == 0) {
                userSex.setText("男");
                userSexSelect = "1";
            } else if (position == 1) {
                userSex.setText("女");
                userSexSelect = "0";
            }
        }
    }

    /**
     * 回传处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("", "");

        if (requestCode == USER_FROM_CITY) {
            if (data != null) {
                userCitySelect = data.getStringExtra("selectCity");
                userAddressText.setText(userCitySelect);
                return;
            }
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (imgUri != null) {
                    startCropActivity(imgUri);
                }
                break;
            case PICK_FROM_FILE:
                startCropActivity(data.getData());
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                }
                break;
            case USER_FROM_CITY:
                userCitySelect = data.getStringExtra("selectCity");
                userAddressText.setText(userCitySelect);
                break;
            case UCrop.REQUEST_CROP:
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    Glide.with(this)
                            .load(resultUri.getPath())
                            .dontAnimate()
                            .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.ucenter_head_profile)
                            .into(headerIV);
                    String pathStr = getImageObjectKey("" + MoreUser.getInstance().getUid());
                    AliUploadUtils aliUpload = new AliUploadUtils(UserModifyActivity.this,
                            Constants.ALI_OSS_BUCKETNAME, pathStr, resultUri.getPath(),
                            MoreUser.getInstance().getUid() + "",
                            uploadResultListener);

                    EventBus.getDefault().post(new UserUpdateEvent(resultUri));
                }
                break;
            case UCrop.RESULT_ERROR:
                Toast.makeText(this, "获取图片出错", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = CROPPED_IMAGE_NAME + ".jpg";
//        switch (mRadioGroupCompressionSettings.getCheckedRadioButtonId()) {
//            case R.id.radio_png:
//                destinationFileName += ".png";
//                break;
//            case R.id.radio_jpeg:
//                destinationFileName += ".jpg";
//                break;
//        }

        File tempPic = new File(getCacheDir(), destinationFileName);
        if (tempPic.exists()) {
            tempPic.delete();
            tempPic = new File(getCacheDir(), destinationFileName);
        }

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(tempPic));

        //uCrop = basisConfig(uCrop);
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = advancedConfig(uCrop);

        uCrop.start(this);
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.black));
        options.setToolbarColor(ContextCompat.getColor(this, R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.black));
        return uCrop.withOptions(options);
    }


    //通过UserCode 加上日期组装 OSS路径
    private String getImageObjectKey(String strUserCode) {

        Date date = new Date();
        return new SimpleDateFormat("yyyy/M/d").format(date) + "/" + strUserCode + new SimpleDateFormat("yyyyMMddssSSS").format(date) + ".jpg";

    }

    PutObjectSamples.OSSUploadResultListener uploadResultListener = new PutObjectSamples.OSSUploadResultListener() {


        @Override
        public void onSuccess(String imgPath) {
            userThumbSelect = imgPath;
        }

        @Override
        public void onFailures(String error) {

        }
    };

    @Override
    public void onUserModifyResponse(RespDto<String> response) {
        MoreUser.getInstance().setUid(Long.valueOf(param.getUid()));
        MoreUser.getInstance().setThumb(param.getThumb());
        MoreUser.getInstance().setNickname(param.getNickName());
        MoreUser.getInstance().setCity(param.getCity());
        if (!TextUtils.isEmpty(param.getSex())) {
            MoreUser.getInstance().setSex(Integer.valueOf(param.getSex()));
        }
        MoreUser.getInstance().updateAccount(UserModifyActivity.this);
        PrefsUtils.setBoolean(this, Constants.KEY_SAVE_USER_SEX, true);

        finish();
    }

    @Override
    public void onUserModifyFailure(String msg) {
        if (msg != null && msg.equals("401")) {
            AppUtils.pushLeftActivity(UserModifyActivity.this, UseLoginActivity.class);
        } else {
            if (msg != null) {
                Toast.makeText(UserModifyActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserModifyActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onUserLoadInfoResponse(RespDto<UserInfo> response) {
        if (response == null || response.getData() == null) return;
        UserInfo result = response.getData();
        MoreUser.getInstance().setThumb(result.getThumb());
        MoreUser.getInstance().setCity(result.getCity());
        if (!TextUtils.isEmpty(result.getSex())) {
            sexLayout.setEnabled(false);
            userSexSelect = "" + MoreUser.getInstance().getSex();
            if (result.getSex().equals("1")) {
                userSex.setText("男");
            } else if (result.getSex().equals("0")){
                userSex.setText("女");
            }
            isSaveUserSex = true;
            MoreUser.getInstance().setSex(Integer.parseInt(result.getSex()));
        } else {
            isSaveUserSex = false;
        }
        userNickEdit.setText(MoreUser.getInstance().getNickname());
        if (!TextUtils.isEmpty(MoreUser.getInstance().getThumb())) {
            Glide.with(this)
                    .load(MoreUser.getInstance().getThumb())
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ucenter_head_profile)
                    .into(headerIV);
        }
        userPersonalMark.setText(result.getPersonalMark());
        userAddressText.setText(result.getCity());
        Log.d("d", "" + result.getSex());

    }

    @Override
    public void onUserLoadInfoFailure(String msg) {
        if (msg != null && msg.equals("401")) {
            AppUtils.pushLeftActivity(UserModifyActivity.this, UseLoginActivity.class);
        } else {
            if (msg != null) {
                Toast.makeText(UserModifyActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserModifyActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
