package com.moreclub.moreapp.ucenter.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserLogin;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.presenter.IUserRegDataLoader;
import com.moreclub.moreapp.ucenter.presenter.UserRegDataLoader;
import com.moreclub.moreapp.util.AliUploadUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.PutObjectSamples;
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
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.moreclub.moreapp.util.RandomUtil.generateString;

/**
 * Created by Captain on 2017/3/16.
 */

public class UserRegHeaderNickActivity extends BaseActivity implements IUserRegDataLoader.UserRegDataBinder {

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.goHome)
    Button goHome;
    @BindView(R.id.nickEditText)
    EditText nickEditText;
    @BindView(R.id.header_img)
    ImageView headerImg;
    @BindView(R.id.iv_man_select)
    ImageView ivManSelect;
    @BindView(R.id.iv_women_select)
    ImageView ivWomenSelect;

    String phoneString;
    String codeString;
    String passwordString;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final String CROPPED_IMAGE_NAME = "UserCropImage";
    private static final int REQUEST_CODE_READ = 1001;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private AlertView photoAlertView;
    private Uri imgUri;
    private String userThumbSelect;
    private AlertView sexAlertView;
    private String userSexSelect;
    private OnItemClickListener clickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(Object o, int position) {
            try {
                if (o == photoAlertView) {
                    if (position == 0) {
                        File file = new File(Environment
                                .getExternalStorageDirectory(), "avatar_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".png");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            ContentValues contentValues = new ContentValues(1);
                            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                            imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
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
            } catch (Exception e) {
                Log.i("zune:", "e = " + e);
            }
        }
    };
    private PutObjectSamples.OSSUploadResultListener uploadResultListener = new PutObjectSamples.OSSUploadResultListener() {
        @Override
        public void onSuccess(String imgPath) {
            userThumbSelect = imgPath;
        }

        @Override
        public void onFailures(String error) {
            Log.i("zune:", "error = " + error);
        }
    };
    private RelativeLayout sexLayout;
    private TextView sexTag;
    private EditText userSex;
    private View.OnClickListener sexClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (sexAlertView == null) {
                sexAlertView = new AlertView("选择性别", null, "取消", null,
                        new String[]{" 男", "女"},
                        UserRegHeaderNickActivity.this, AlertView.Style.ActionSheet, clickListener);
            }
            if (!sexAlertView.isShowing() && !UserRegHeaderNickActivity.this.isFinishing())
                sexAlertView.show();
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.reg_header_nick;
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

    @Override
    protected Class getLogicClazz() {
        return IUserRegDataLoader.class;
    }

    private void initData() {
        phoneString = getIntent().getStringExtra("phone");
        codeString = getIntent().getStringExtra("code");
        passwordString = getIntent().getStringExtra("password");
    }

    private void setupViews() {
//        initSex();
        userThumbSelect = MoreUser.getInstance().getThumb();
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(UserRegHeaderNickActivity.this.getResources().getString(R.string.reg_finish));
        goHome.setOnClickListener(goHomeListener);
        if (!TextUtils.isEmpty(userThumbSelect))
            Glide.with(this)
                    .load(userThumbSelect)
                    .placeholder(R.drawable.profilephoto)
                    .dontAnimate()
                    .into(headerImg);
    }

    private void initSex() {
        sexLayout = (RelativeLayout) findViewById(R.id.sex_layout);
        sexTag = (TextView) sexLayout.findViewById(R.id.user_sex_tag);
        sexTag.setTextColor(Color.parseColor("#333333"));
        sexTag.setText("性别");
        userSex = (EditText) sexLayout.findViewById(R.id.user_sex);
        userSex.setClickable(true);
        sexLayout.setOnClickListener(sexClickListener);
        userSex.setOnClickListener(sexClickListener);
        userSex.setClickable(true);
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserRegHeaderNickActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
    }

    View.OnClickListener goHomeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nickString = nickEditText.getText().toString();
            if (nickString == null || nickString.length() == 0) {
                Toast.makeText(UserRegHeaderNickActivity.this, "请输入您的别名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(userThumbSelect)) {
                Toast.makeText(UserRegHeaderNickActivity.this, "请上传您的头像", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(userSexSelect)) {
                Toast.makeText(UserRegHeaderNickActivity.this, "请填写您的性别", Toast.LENGTH_SHORT).show();
                return;
            }
            String time = TimeUtils.getTimestampSecond();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("phone", phoneString);
            map.put("passwd", passwordString);
            map.put("appkey", Constants.MORE_APPKEY);
            map.put("appVersion", Constants.APP_version);
            map.put("loginTime", time);
            map.put("loginType", "0");
            map.put("machine", "android");
            map.put("opSystem", "android");
            map.put("version", Constants.APP_version);
            map.put("vcode", codeString);


            UserLogin param = new UserLogin();
            param.setPhone(phoneString);
            param.setPasswd(passwordString);
            param.setAppkey(Constants.MORE_APPKEY);
            param.setAppVersion(Constants.APP_version);
            param.setLoginTime(time);
            param.setLoginType("0");
            param.setMachine("android");
            param.setOpSystem("android");
            param.setVersion(Constants.APP_version);
            param.setVcode(codeString);
            param.setThumb(userThumbSelect);
            param.setSex(userSexSelect);

            String sig = SecurityUtils.createSign(map, "5698250a71fe54ab1ad004890229dfa75efb6d6c");
            param.setSign(sig);
            param.setTimestamp(time);
            param.setNickname(nickString);
            ((UserRegDataLoader) mPresenter).onReg(param);

        }
    };

    @Override
    public void onUserRegResponse(RespDto response) {
        if (response == null) {
            Toast.makeText(UserRegHeaderNickActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
        } else {
            UserLoginInfo result = (UserLoginInfo) response.getData();
            MoreUser.getInstance().setShowCouponDialog(true);
            MoreUser.getInstance().setUid(result.getUid());
            MoreUser.getInstance().setNickname(result.getNickname());
            MoreUser.getInstance().setAccess_token(result.getAccess_token());
            MoreUser.getInstance().setRefresh_token(result.getRefresh_token());
            MoreUser.getInstance().setToken_type(Constants.TOKEN_MOBILE);
            MoreUser.getInstance().setThumb(userThumbSelect);
            if (!TextUtils.isEmpty(userSexSelect))
                MoreUser.getInstance().setSex(Integer.parseInt(userSexSelect));
            MoreUser.getInstance().updateAccount(this);
            Toast.makeText(UserRegHeaderNickActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserRegHeaderNickActivity.this, SuperMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            UserRegHeaderNickActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onUserRegFailure(String msg) {
        if (msg == null) {
            Toast.makeText(UserRegHeaderNickActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UserRegHeaderNickActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(UserRegHeaderNickActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showDialogPermission() {
        View dialogView = View.inflate(this, R.layout.sign_inter_delete_dialog, null);
        TextView delete = (TextView) dialogView.findViewById(R.id.delete);
        delete.setText("确定");
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        cancel.setText("");
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("您必须允许存储权限才能注册");
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .show();
        dialog.setCancelable(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                dialog.dismiss();
            }
        });
    }

    private void requestPermission() {
        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
        if (ContextCompat.checkSelfPermission(UserRegHeaderNickActivity.this, permissions[0]) != PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UserRegHeaderNickActivity.this, permissions[1]) != PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            ActivityCompat.requestPermissions(UserRegHeaderNickActivity.this, permissions, REQUEST_CODE_READ);
        } else {
            photoAlertView = new AlertView("上传头像", null, "取消", null, new String[]{"拍照", "从相册中选择"},
                    UserRegHeaderNickActivity.this, AlertView.Style.ActionSheet, clickListener);
            if (!photoAlertView.isShowing() && !UserRegHeaderNickActivity.this.isFinishing())
                photoAlertView.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    //用户同意授权
                    photoAlertView = new AlertView("上传头像", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"},
                            UserRegHeaderNickActivity.this, AlertView.Style.ActionSheet, clickListener);
                    if (!photoAlertView.isShowing() && !UserRegHeaderNickActivity.this.isFinishing())
                        photoAlertView.show();
                } else {
                    //用户拒绝授权
                    Toast.makeText(UserRegHeaderNickActivity.this, "使用外置存储卡,请手动设置", Toast.LENGTH_SHORT).show();
                    photoAlertView = new AlertView("上传头像", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"},
                            UserRegHeaderNickActivity.this, AlertView.Style.ActionSheet, clickListener);
                    if (!photoAlertView.isShowing() && !UserRegHeaderNickActivity.this.isFinishing())
                        photoAlertView.show();
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (imgUri != null) {
                    startCropActivity(imgUri);
                }
                break;
            case PICK_FROM_FILE:
                if (data == null) return;
                startCropActivity(data.getData());
                break;
            case UCrop.REQUEST_CROP:
                if (data == null) return;
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    Glide.with(this)
                            .load(resultUri.getPath())
                            .dontAnimate()
                            .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.profilephoto)
                            .into(headerImg);
                    String randomUid;
                    if (MoreUser.getInstance() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                        randomUid = MoreUser.getInstance().getUid() + "";
                    } else {
                        randomUid = generateString(12);
                    }
                    String pathStr = getImageObjectKey(randomUid);
                    AliUploadUtils aliUpload = new AliUploadUtils(UserRegHeaderNickActivity.this,
                            Constants.ALI_OSS_BUCKETNAME, pathStr, resultUri.getPath(),
                            uploadResultListener);
                    EventBus.getDefault().post(new UserUpdateEvent(resultUri));
                } else {
                    Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case UCrop.RESULT_ERROR:
                Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCropActivity(Uri uri) {
        String destinationFileName = CROPPED_IMAGE_NAME + ".jpg";
        File tempPic = new File(getCacheDir(), destinationFileName);
        if (tempPic.exists()) {
            tempPic.delete();
            tempPic = new File(getCacheDir(), destinationFileName);
        }
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(tempPic));
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = advancedConfig(uCrop);
        uCrop.start(this);
    }

    private UCrop advancedConfig(UCrop uCrop) {
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

    @OnClick({R.id.header_img, R.id.iv_man_select, R.id.tv_man, R.id.iv_women_select, R.id.tv_women})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_img:
                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                } else {
                    photoAlertView = new AlertView("上传头像", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"},
                            UserRegHeaderNickActivity.this, AlertView.Style.ActionSheet, clickListener);
                    if (!photoAlertView.isShowing() && !UserRegHeaderNickActivity.this.isFinishing())
                        photoAlertView.show();
                }
                break;
            case R.id.iv_man_select:
            case R.id.tv_man:
                ivManSelect.setSelected(true);
                ivWomenSelect.setSelected(false);
                userSexSelect = "1";
                break;
            case R.id.iv_women_select:
            case R.id.tv_women:
                ivManSelect.setSelected(false);
                ivWomenSelect.setSelected(true);
                userSexSelect = "0";
                break;
        }
    }
}
