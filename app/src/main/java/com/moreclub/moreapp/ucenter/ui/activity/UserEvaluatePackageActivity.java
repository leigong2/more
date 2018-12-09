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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.view.CustomGridView;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserPackageEvaluate;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.presenter.IUserEvaluatePackageLoader;
import com.moreclub.moreapp.ucenter.presenter.UserEvaluatePackageLoader;
import com.moreclub.moreapp.ucenter.ui.view.ColoredRatingBar;
import com.moreclub.moreapp.ucenter.ui.view.EvaluateGridViewAdapter;
import com.moreclub.moreapp.util.AliUploadUtils;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.PutObjectSamples;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.sdsmdg.tastytoast.TastyToast;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/31.
 */

public class UserEvaluatePackageActivity extends BaseActivity implements IUserEvaluatePackageLoader.LoaderUserEvaluatePackage,OnItemClickListener, OnDismissListener ,TextView.OnEditorActionListener{

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private static final int USER_FROM_CITY = 4;

    private static final String CROPPED_IMAGE_NAME = "UserCropImage";

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.package_title) TextView packageTitle;
    @BindView(R.id.package_evaluate) ColoredRatingBar packageEvaluate;
    @BindView(R.id.music_evaluate) ColoredRatingBar musicEvaluate;
    @BindView(R.id.environment_evaluate) ColoredRatingBar environmentEvaluate;
    @BindView(R.id.server_evaluate) ColoredRatingBar serverEvaluate;
    @BindView(R.id.evaluate_content) EditText evaluateContent;
    @BindView(R.id.word_number) TextView wordNumber;
    @BindView(R.id.evaluateButton) Button evaluateButton;
    @BindView(R.id.picgridView) CustomGridView picgridView;
    private int pid;
    private long orderId;
    private String mid;
    private String merchantName;
    private String packageName;
    private Uri imgUri;

    private ArrayList<String> pictures = new ArrayList<String>();
    private StringBuilder picturesString = new StringBuilder();
    private int currentDelectIndex=0;
    private AlertView photoAlertView;
    private EvaluateGridViewAdapter adapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.user_evaluate_package_activity;
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
        return IUserEvaluatePackageLoader.class;
    }

    private void initData() {

        Intent intent = getIntent();
        orderId = intent.getLongExtra("orderId",0);
        pid = intent.getIntExtra("pid",0);
        mid = intent.getStringExtra("mid");
        merchantName = intent.getStringExtra("merchantName");
        packageName = intent.getStringExtra("packageName");

        pictures.add("add");
    }

    private void setupViews() {
        activityTitle.setText(R.string.evaluate_package_title);
        naBack.setOnClickListener(goBackListener);
        evaluateButton.setOnClickListener(evaluateListener);
        packageTitle.setText(packageName);

        adapter = new EvaluateGridViewAdapter(UserEvaluatePackageActivity.this,pictures,picItemClick);
        picgridView.setAdapter(adapter);
        evaluateContent.setImeOptions(EditorInfo.IME_ACTION_DONE);
        evaluateContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0) {
                    wordNumber.setTextColor(ContextCompat.getColor(UserEvaluatePackageActivity.this,R.color.abc_red));
                } else {
                    wordNumber.setTextColor(ContextCompat.getColor(UserEvaluatePackageActivity.this,R.color.merchant_item_distance));
                }
                wordNumber.setText(charSequence.length() + "/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart = evaluateContent.getSelectionStart();
                editEnd = evaluateContent.getSelectionEnd();
                if (temp.length() > 50) {//限制长度
                    TastyToast.makeText(getApplicationContext(),
                           UserEvaluatePackageActivity.this.getString(R.string.input_excess_word),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                    editable.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    evaluateContent.setText(editable);
                    evaluateContent.setSelection(tempSelection);
                    wordNumber.setText("50/50");
                }
            }
        });
        evaluateContent.setOnEditorActionListener(this);
    }


    View.OnClickListener evaluateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            evaluatePackage();
        }
    };


    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserEvaluatePackageActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    /**
     * 提交评价
     *
     */
    private void evaluatePackage(){

        float packageEvaluateF = packageEvaluate.getRating();
        float musicEvaluateF = musicEvaluate.getRating();
        float environmentEvaluateF = environmentEvaluate.getRating();
        float serverEvaluateF = serverEvaluate.getRating();

        if (packageEvaluateF==0){
            Toast.makeText(UserEvaluatePackageActivity.this,getString(R.string.no_evaluate_package),Toast.LENGTH_SHORT).show();
            return;
        }
        if (musicEvaluateF==0){
            Toast.makeText(UserEvaluatePackageActivity.this,getString(R.string.no_evaluate_music),Toast.LENGTH_SHORT).show();
            return;
        }
        if (environmentEvaluateF==0){
            Toast.makeText(UserEvaluatePackageActivity.this,getString(R.string.no_evaluate_environment),Toast.LENGTH_SHORT).show();
            return;
        }
        if (serverEvaluateF==0){
            Toast.makeText(UserEvaluatePackageActivity.this,getString(R.string.no_evaluate_server),Toast.LENGTH_SHORT).show();
            return;
        }

        String evaluateContentString = evaluateContent.getText().toString();

        String picS = "";
        if (pictures!=null&&pictures.size()>0){
            for(int i=0;i<pictures.size();i++){
                String picStr = pictures.get(i);
                if (!picStr.equals("add")) {
                    if (i == (pictures.size() - 1)) {
                        picturesString.append(picStr);
                    } else {
                        picturesString.append(picStr);
                        picturesString.append(";");
                    }
                }
            }
        }
        picS = picturesString.toString();
        UserPackageEvaluate param = new UserPackageEvaluate();
        param.setBuyerPhotos(picS);
        param.setFacilityStar(serverEvaluateF);
        param.setMusicStar(musicEvaluateF);
        param.setSceneStar(environmentEvaluateF);
        param.setpStar(packageEvaluateF);
        param.setOrderId(orderId);
        param.setUid(MoreUser.getInstance().getUid());
        param.setPid(pid);
        param.setOtherComment(evaluateContentString);

        ((UserEvaluatePackageLoader) mPresenter).userEvaluatePackage(param);
    }

    @Override
    public void onUserEvaluatePackageResponse(RespDto response) {
        Toast.makeText(UserEvaluatePackageActivity.this,getString(R.string.evaluate_package_tip),Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUserEvaluatePackageFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 图片选择
     * @param o
     */
    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o==photoAlertView){
            if (position==0){
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
            } else if (position==1){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_FILE);
            } else if (position==2){
                if (pictures.size()==1){
                    pictures.remove(currentDelectIndex);
                    pictures.add("add");
                } else {
                    pictures.remove(currentDelectIndex);
                    String addStr = pictures.get(pictures.size()-1);
                    if (!addStr.equals("add")){
                        pictures.add("add");
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 回传处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("","");
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (imgUri!=null){
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
            case UCrop.REQUEST_CROP:
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri!=null){
                    String pathStr =getImageObjectKey(""+ MoreUser.getInstance().getUid());
                    AliUploadUtils aliUpload = new AliUploadUtils(UserEvaluatePackageActivity.this,
                            Constants.ALI_OSS_BUCKETNAME, pathStr,resultUri.getPath(),
                            MoreUser.getInstance().getUid() + "",
                            uploadResultListener);

                    EventBus.getDefault().post(new UserUpdateEvent(resultUri));
                }
                break;
            case UCrop.RESULT_ERROR:
                Toast.makeText(this,getString(R.string.error_get_picture),Toast.LENGTH_SHORT).show();
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
        if (tempPic.exists()){
            tempPic.delete();
            tempPic =  new File(getCacheDir(), destinationFileName);
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
    private String getImageObjectKey (String strUserCode){

        Date date = new Date();
        return new SimpleDateFormat("yyyy/M/d").format(date)+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";

    }

    EvaluateGridViewAdapter.ItemClick picItemClick = new EvaluateGridViewAdapter.ItemClick(){

        @Override
        public void itemClick(int pos) {
            currentDelectIndex = pos;
            photoAlertView = new AlertView(UserEvaluatePackageActivity.this.getString(R.string.upload_header_title),
                    null,UserEvaluatePackageActivity.this.getString(R.string.dialog_confirm_cancel), null,
                    new String[]{UserEvaluatePackageActivity.this.getString(R.string.take_picture),
                            UserEvaluatePackageActivity.this.getString(R.string.picture_from_exist),"删除"},
                    UserEvaluatePackageActivity.this, AlertView.Style.ActionSheet, UserEvaluatePackageActivity.this);

            if (!photoAlertView.isShowing()&&!UserEvaluatePackageActivity.this.isFinishing())
                photoAlertView.show();
        }
    };

    PutObjectSamples.OSSUploadResultListener uploadResultListener = new PutObjectSamples.OSSUploadResultListener(){


        @Override
        public void onSuccess(final String imgPath) {

            UserEvaluatePackageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pictures.remove(pictures.size()-1);
                    pictures.add(imgPath);
                    if (pictures.size()==8){
                        Toast.makeText(UserEvaluatePackageActivity.this,UserEvaluatePackageActivity.this.getString(R.string.picture_excess_upload),Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    pictures.add("add");
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailures(String error) {

        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        return false;
    }
}
