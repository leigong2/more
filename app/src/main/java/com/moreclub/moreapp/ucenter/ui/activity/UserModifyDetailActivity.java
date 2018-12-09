package com.moreclub.moreapp.ucenter.ui.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserModify;
import com.moreclub.moreapp.ucenter.model.event.UserLoginEvent;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.presenter.IUserInfoModifyLoader;
import com.moreclub.moreapp.ucenter.presenter.UserInfoModifyLoader;
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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Captain on 2017/3/13.
 */

public class UserModifyDetailActivity extends BaseActivity implements OnItemClickListener, OnDismissListener, IUserInfoModifyLoader.UserInfoModifyDataBinder {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private static final int USER_FROM_CITY = 4;
    private static final int USER_FROM_BIRTHDAY = 5;
    private static final int USER_FROM_JOB = 6;
    private static final int USER_FROM_HOBBY = 7;
    private static final int USER_FROM_BRAND = 8;
    private static final int USER_FROM_WINE = 9;
    private static final int USER_FROM_BOOK = 10;
    private static final int USER_FROM_FOOD = 11;

    private static final String CROPPED_IMAGE_NAME = "UserCropImage";
    private static final int REQUEST_CODE_READ = 1001;

    @BindView(R.id.header_img)
    ImageView headerIV;
    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.saveButton)
    TextView saveButton;
    @BindView(R.id.user_nick)
    EditText userNickEdit;

    private AlertView sexAlertView;
    private AlertView photoAlertView;
    private Uri imgUri;
    private String userSexSelect;
    private String userCitySelect;
    private String userThumbSelect;
    UserModify param;
    boolean isSaveUserSex;
    private RelativeLayout sexLayout;
    private TextView sexTag;
    private RelativeLayout addressLayout;
    private EditText userPersonalMark;
    private EditText userSex;
    private EditText userAddressText;
    private RelativeLayout signLayout;
    private RelativeLayout birthdayLayout;
    private RelativeLayout jobLayout;
    private RelativeLayout hobbyLayout;
    private RelativeLayout brandLayout;
    private RelativeLayout decorumLayout;
    private RelativeLayout bookLayout;
    private RelativeLayout foodLayout;
    private TextView signTag;
    private TextView addressTag;
    private TextView birthdayTag;
    private TextView jobTag;
    private TextView hobbyTag;
    private TextView brandTag;
    private TextView decorumTag;
    private TextView bookTag;
    private TextView foodTag;
    private EditText birthday;
    private EditText job;
    private EditText hobby;
    private EditText brand;
    private EditText decorum;
    private EditText book;
    private EditText food;
    private ImageView ivTag;
    private String myBirthday;
    private String myJob;
    private String myhobby;
    private String mybrand;
    private String mydecorum;
    private String mybook;
    private String myfood;
    private int jobId;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected int getLayoutResource() {
        return R.layout.user_modify_detail_activity;
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
        sexLayout = (RelativeLayout) findViewById(R.id.sex_layout);
        signLayout = (RelativeLayout) findViewById(R.id.sign_lay);
        addressLayout = (RelativeLayout) findViewById(R.id.hometown_lay);
        birthdayLayout = (RelativeLayout) findViewById(R.id.birthday_lay);
        jobLayout = (RelativeLayout) findViewById(R.id.job_lay);
        hobbyLayout = (RelativeLayout) findViewById(R.id.hobby_lay);
        brandLayout = (RelativeLayout) findViewById(R.id.brand_lay);
        decorumLayout = (RelativeLayout) findViewById(R.id.decorum_lay);
        bookLayout = (RelativeLayout) findViewById(R.id.book_lay);
        foodLayout = (RelativeLayout) findViewById(R.id.food_lay);

        sexTag = (TextView) sexLayout.findViewById(R.id.user_sex_tag);
        sexTag.setText("性别");
        signTag = (TextView) signLayout.findViewById(R.id.user_sex_tag);
        signTag.setText("签名");
        addressTag = (TextView) addressLayout.findViewById(R.id.user_sex_tag);
        addressTag.setText("所在地");
        birthdayTag = (TextView) birthdayLayout.findViewById(R.id.user_sex_tag);
        birthdayTag.setText("生日");
        jobTag = (TextView) jobLayout.findViewById(R.id.user_sex_tag);
        jobTag.setText("职业");
        hobbyTag = (TextView) hobbyLayout.findViewById(R.id.user_sex_tag);
        hobbyTag.setText("爱好");
        brandTag = (TextView) brandLayout.findViewById(R.id.user_sex_tag);
        brandTag.setText("品牌爱好");
        decorumTag = (TextView) decorumLayout.findViewById(R.id.user_sex_tag);
        decorumTag.setText("酒品爱好");
        bookTag = (TextView) bookLayout.findViewById(R.id.user_sex_tag);
        bookTag.setText("书籍爱好");
        foodTag = (TextView) foodLayout.findViewById(R.id.user_sex_tag);
        foodTag.setText("菜肴爱好");

        userSex = (EditText) sexLayout.findViewById(R.id.user_sex);
        ivTag = (ImageView) sexLayout.findViewById(R.id.iv_tag);
        ivTag.setVisibility(View.GONE);
        userPersonalMark = (EditText) signLayout.findViewById(R.id.user_sex);
        userPersonalMark.setHint("请输入您的个性签名");
        userPersonalMark.setFocusable(true);
        userAddressText = (EditText) addressLayout.findViewById(R.id.user_sex);
        birthday = (EditText) birthdayLayout.findViewById(R.id.user_sex);
        job = (EditText) jobLayout.findViewById(R.id.user_sex);
        hobby = (EditText) hobbyLayout.findViewById(R.id.user_sex);
        brand = (EditText) brandLayout.findViewById(R.id.user_sex);
        decorum = (EditText) decorumLayout.findViewById(R.id.user_sex);
        book = (EditText) bookLayout.findViewById(R.id.user_sex);
        food = (EditText) foodLayout.findViewById(R.id.user_sex);


        headerIV.setOnClickListener(headerImageClickListener);

        sexLayout.setOnClickListener(sexClickListener);
        userSex.setOnClickListener(sexClickListener);
        userSex.setClickable(true);
        addressLayout.setOnClickListener(addressClickListener);
        userAddressText.setOnClickListener(addressClickListener);
        birthdayLayout.setOnClickListener(birthdayClickListener);
        birthday.setOnClickListener(birthdayClickListener);
        userSex.setClickable(true);
        jobLayout.setOnClickListener(jobClickListener);
        job.setOnClickListener(jobClickListener);
        job.setClickable(true);
        hobbyLayout.setOnClickListener(hobbyClickListener);
        hobby.setOnClickListener(hobbyClickListener);
        hobby.setClickable(true);
        brandLayout.setOnClickListener(brandClickListener);
        brand.setOnClickListener(brandClickListener);
        brand.setClickable(true);
        decorumLayout.setOnClickListener(wineLikeClickListener);
        decorum.setOnClickListener(wineLikeClickListener);
        decorum.setClickable(true);
        bookLayout.setOnClickListener(bookLikeClickListener);
        book.setOnClickListener(bookLikeClickListener);
        book.setClickable(true);
        foodLayout.setOnClickListener(foodLikeClickListener);
        food.setOnClickListener(foodLikeClickListener);
        food.setClickable(true);
        naBack.setOnClickListener(goBack);
        saveButton.setOnClickListener(saveClickListener);
        activityTitle.setText("修改个人资料");

        userNickEdit.setText(MoreUser.getInstance().getNickname());
        String birthday = MoreUser.getInstance().getBirthday();
        if (birthday != null) {
            String[] birthdays = birthday.split("-");
            if (birthdays != null && birthdays.length == 3)
                this.birthday.setText(birthdays[0] + "年" + birthdays[1] + "月" + birthdays[2] + "日");
        }
        if (!TextUtils.isEmpty(MoreUser.getInstance().getThumb())) {
            Glide.with(this)
                    .load(MoreUser.getInstance().getThumb())
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .placeholder(R.drawable.profilephoto)
                    .dontAnimate()
                    .into(headerIV);
        } else {
            headerIV.setImageResource(R.drawable.profilephoto);
        }

        userSexSelect = "" + MoreUser.getInstance().getSex();

        ((UserInfoModifyLoader) mPresenter).loadUserInfo("" + MoreUser.getInstance().getUid());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(UserModifyDetailActivity.this,
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
                        UserModifyDetailActivity.this, AlertView.Style.ActionSheet, UserModifyDetailActivity.this);
            }
            if (!sexAlertView.isShowing() && !UserModifyDetailActivity.this.isFinishing() && TextUtils.isEmpty(userSex.getText().toString()))
                sexAlertView.show();
        }
    };


    /**
     * 地址选择
     */
    View.OnClickListener addressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, UserModifyAddressActivity.class);
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_CITY);
        }
    };

    /**
     * 头像选择
     */
    View.OnClickListener headerImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                requestPermission();
            } else {
                photoAlertView = new AlertView("上传头像", null, "取消", null,
                        new String[]{"拍照", "从相册中选择"},
                        UserModifyDetailActivity.this, AlertView.Style.ActionSheet, UserModifyDetailActivity.this);
                if (!photoAlertView.isShowing() && !UserModifyDetailActivity.this.isFinishing())
                    photoAlertView.show();
            }
        }
    };

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(UserModifyDetailActivity.this, permissions[0]) != PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UserModifyDetailActivity.this, permissions[1]) != PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            ActivityCompat.requestPermissions(UserModifyDetailActivity.this, permissions, REQUEST_CODE_READ);
        } else {
            photoAlertView = new AlertView("上传头像", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    UserModifyDetailActivity.this, AlertView.Style.ActionSheet, UserModifyDetailActivity.this);
            if (!photoAlertView.isShowing() && !UserModifyDetailActivity.this.isFinishing())
                photoAlertView.show();
        }
    }

    /**
     * 生日选择
     */
    View.OnClickListener birthdayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, BirthdayChoiceActivity.class);
            intent.putExtra("birthday", birthday.getText().toString());
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_BIRTHDAY);
        }
    };

    /**
     * 职业选择
     */
    View.OnClickListener jobClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, JobChoiceActivity.class);
            intent.putExtra("job", job.getText().toString());
            intent.putExtra("jobId", jobId);
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_JOB);
        }
    };

    /**
     * 爱好选择
     */
    View.OnClickListener hobbyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, HobbyChoiceActivity.class);
            intent.putExtra("hobby", hobby.getText().toString());
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_HOBBY);
        }
    };

    /**
     * 品牌喜好选择
     */
    View.OnClickListener brandClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, BrandActivity.class);
            intent.putExtra("brand", brand.getText().toString());
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_BRAND);
        }
    };

    /**
     * 酒品喜好选择
     */
    View.OnClickListener wineLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, WineLikeActivity.class);
            intent.putExtra("wineLike", decorum.getText().toString());
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_WINE);
        }
    };

    /**
     * 书籍喜好选择
     */
    View.OnClickListener bookLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, BookLikeActivity.class);
            intent.putExtra("bookLike", book.getText().toString());
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_BOOK);
        }
    };

    /**
     * 菜肴喜好选择
     */
    View.OnClickListener foodLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserModifyDetailActivity.this, FoodLikeActivity.class);
            intent.putExtra("foodLike", food.getText().toString());
            UserModifyDetailActivity.this.startActivityForResult(intent, USER_FROM_FOOD);
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
                phoneIP = Utils.getLocalIpAddress(UserModifyDetailActivity.this);
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
            param.setThumb(TextUtils.isEmpty(userThumbSelect) ? MoreUser.getInstance().getThumb() : userThumbSelect);
            param.setNickName(tempNickName);
            param.setPersonalMark(tempPersonalMark);
            param.setTimestamp(time);
            param.setHobby(hobby.getText().toString());
            String s = birthday.getText().toString();
            String year = s.replace("年", "-");
            String month = year.replace("月", "-");
            String day = month.replace("日", "");
            param.setBirthday(day);
            param.setCareerName(job.getText().toString());
            param.setCareer(jobId + "");
            param.setBrandPrefer(brand.getText().toString());
            param.setWinePrefer(decorum.getText().toString());
            param.setBookPrefer(book.getText().toString());
            param.setFoodPrefer(food.getText().toString());
            ((UserInfoModifyLoader) mPresenter).updateUserInfo(param);
        }
    };

    View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserModifyDetailActivity.this.finish();
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
            case CROP_FROM_CAMERA:
                if (null != data) {
                }
                break;
            case USER_FROM_CITY:
                if (data == null) return;
                userCitySelect = data.getStringExtra("selectCity");
                if (!TextUtils.isEmpty(userCitySelect) && !TextUtils.equals(userCitySelect, "正在定位中..."))
                    userAddressText.setText(userCitySelect);
                break;
            case USER_FROM_BIRTHDAY:
                if (data == null) return;
                myBirthday = data.getStringExtra("birthday");
                if (!TextUtils.isEmpty(myBirthday)) {
                    birthday.setText(myBirthday);
                }
                break;
            case USER_FROM_JOB:
                if (data == null) return;
                myJob = data.getStringExtra("job");
                jobId = data.getIntExtra("jobId", -1);
                if (!TextUtils.isEmpty(myJob)) {
                    job.setText(myJob);
                    job.setLines(1);
                    job.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    job.setText("");
                }
                break;
            case USER_FROM_HOBBY:
                if (data == null) return;
                myhobby = data.getStringExtra("hobby");
                if (!TextUtils.isEmpty(myhobby)) {
                    hobby.setText(myhobby);
                    hobby.setLines(1);
                    hobby.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    hobby.setText("");
                }
                break;
            case USER_FROM_BRAND:
                if (data == null) return;
                mybrand = data.getStringExtra("brand");
                if (!TextUtils.isEmpty(mybrand)) {
                    brand.setText(mybrand);
                    brand.setLines(1);
                    brand.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    brand.setText("");
                }
                break;
            case USER_FROM_WINE:
                if (data == null) return;
                mydecorum = data.getStringExtra("wineLike");
                if (!TextUtils.isEmpty(mydecorum)) {
                    decorum.setText(mydecorum);
                    decorum.setLines(1);
                    decorum.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    decorum.setText("");
                }
                break;
            case USER_FROM_BOOK:
                if (data == null) return;
                mybook = data.getStringExtra("bookLike");
                if (!TextUtils.isEmpty(mybook)) {
                    book.setText(mybook);
                    book.setLines(1);
                    book.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    book.setText("");
                }
                break;
            case USER_FROM_FOOD:
                if (data == null) return;
                myfood = data.getStringExtra("foodLike");
                if (!TextUtils.isEmpty(myfood)) {
                    food.setText(myfood);
                    food.setLines(1);
                    food.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    food.setText("");
                }
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
                            .into(headerIV);
                    String pathStr = getImageObjectKey("" + MoreUser.getInstance().getUid());
                    AliUploadUtils aliUpload = new AliUploadUtils(UserModifyDetailActivity.this,
                            Constants.ALI_OSS_BUCKETNAME, pathStr, resultUri.getPath(),
                            MoreUser.getInstance().getUid() + "",
                            uploadResultListener);

                    EventBus.getDefault().post(new UserUpdateEvent(resultUri));
                } else {
                    Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case UCrop.RESULT_ERROR:
                Toast.makeText(this, "获取图片出错", Toast.LENGTH_SHORT).show();
                break;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = CROPPED_IMAGE_NAME + ".jpg";

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
        userThumbSelect = param.getThumb();
        if (!TextUtils.isEmpty(userThumbSelect))
            MoreUser.getInstance().setThumb(userThumbSelect);
        MoreUser.getInstance().setNickname(param.getNickName());
        MoreUser.getInstance().setCity(param.getCity());
        MoreUser.getInstance().setBirthday(param.getBirthday());
        if (param.getCareer() != null) {
            int careerID = Integer.parseInt(param.getCareer());
            MoreUser.getInstance().setCareerID(careerID);
            jobId = careerID;
        } else
            MoreUser.getInstance().setCareerID(-1);
        MoreUser.getInstance().setCareerName(param.getCareerName());
        MoreUser.getInstance().setBrandPrefer(param.getBrandPrefer());
        MoreUser.getInstance().setWinePrefer(param.getWinePrefer());
        MoreUser.getInstance().setBookPrefer(param.getBookPrefer());
        MoreUser.getInstance().setFoodPrefer(param.getFoodPrefer());
        MoreUser.getInstance().setHobby(param.getHobby());
        if (!TextUtils.isEmpty(param.getSex())) {
            MoreUser.getInstance().setSex(Integer.valueOf(param.getSex()));
        }
        MoreUser.getInstance().updateAccount(UserModifyDetailActivity.this);
        PrefsUtils.setBoolean(this, Constants.KEY_SAVE_USER_SEX, true);
        UserUpdateEvent event = new UserUpdateEvent(Uri.parse(param.getThumb()));
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void onUserModifyFailure(String msg) {
        if (msg != null && msg.equals("401")) {
            AppUtils.pushLeftActivity(UserModifyDetailActivity.this, UseLoginActivity.class);
        } else {
            if (msg != null) {
                Toast.makeText(UserModifyDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserModifyDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onUserLoadInfoResponse(RespDto<UserInfo> response) {
        UserInfo result = response.getData();
        if (result == null) return;
        if (!TextUtils.isEmpty(result.getSex()))
            MoreUser.getInstance().setSex(Integer.parseInt(result.getSex()));
        if (result.getThumb() != null)
            MoreUser.getInstance().setThumb(result.getThumb());
        if (result.getThdThumb() != null)
            MoreUser.getInstance().setThdThumb(result.getThdThumb());
        if (result.getCity() != null)
            MoreUser.getInstance().setCity(result.getCity());
        if (result.getBirthday() != null)
            MoreUser.getInstance().setBirthday(result.getBirthday());
        if (result.getNickname() != null)
            MoreUser.getInstance().setNickname(result.getNickname());
        if (result.getPersonalMark() != null)
            MoreUser.getInstance().setPersonalMark(result.getPersonalMark());
        if (result.getBookPrefer() != null)
            MoreUser.getInstance().setBookPrefer(result.getBookPrefer());
        if (result.getBrandPrefer() != null)
            MoreUser.getInstance().setBrandPrefer(result.getBrandPrefer());
        if (result.getCareerName() != null)
            MoreUser.getInstance().setCareerName(result.getCareerName());
        if (result.getCareerID() > 0)
            MoreUser.getInstance().setCareerID(result.getCareerID());
        if (result.getFoodPrefer() != null)
            MoreUser.getInstance().setFoodPrefer(result.getFoodPrefer());
        if (result.getHobby() != null)
            MoreUser.getInstance().setHobby(result.getHobby());
        if (result.getWinePrefer() != null)
            MoreUser.getInstance().setWinePrefer(result.getWinePrefer());
        if (result.getPhone() != null)
            MoreUser.getInstance().setUserPhone(result.getPhone());
        MoreUser.getInstance().updateAccount(this);
        userThumbSelect = result.getThumb();
        job.setText(result.getCareerName());
        jobId = result.getCareerID();
        hobby.setText(result.getHobby());
        brand.setText(result.getBrandPrefer());
        decorum.setText(result.getWinePrefer());
        book.setText(result.getBookPrefer());
        food.setText(result.getFoodPrefer());
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
                    .placeholder(R.drawable.profilephoto)
                    .into(headerIV);
        } else if (!TextUtils.isEmpty(MoreUser.getInstance().getThdThumb())) {
            Glide.with(this)
                    .load(MoreUser.getInstance().getThdThumb())
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.profilephoto)
                    .into(headerIV);
        }
        userPersonalMark.setText(result.getPersonalMark());
        userAddressText.setText(result.getCity());
        Log.d("d", "" + result.getSex());

    }

    @Override
    public void onUserLoadInfoFailure(String msg) {
        if (msg != null && msg.equals("401")) {
            AppUtils.pushLeftActivity(UserModifyDetailActivity.this, UseLoginActivity.class);
        } else {
            if (msg != null) {
                Toast.makeText(UserModifyDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserModifyDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
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
                            UserModifyDetailActivity.this, AlertView.Style.ActionSheet, UserModifyDetailActivity.this);
                    if (!photoAlertView.isShowing() && !UserModifyDetailActivity.this.isFinishing())
                        photoAlertView.show();
                } else {
                    //用户拒绝授权
                    Toast.makeText(UserModifyDetailActivity.this, "使用外置存储卡,请手动设置", Toast.LENGTH_SHORT).show();
                    photoAlertView = new AlertView("上传头像", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"},
                            UserModifyDetailActivity.this, AlertView.Style.ActionSheet, UserModifyDetailActivity.this);
                    if (!photoAlertView.isShowing() && !UserModifyDetailActivity.this.isFinishing())
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
    protected void onDestroy() {
        super.onDestroy();
        if (MoreUser.getInstance().getThumb() != null) {
            UserUpdateEvent event = new UserUpdateEvent(Uri.parse(MoreUser.getInstance().getThumb()));
            EventBus.getDefault().post(event);
        }
    }
}
