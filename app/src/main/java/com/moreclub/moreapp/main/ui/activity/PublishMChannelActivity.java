package com.moreclub.moreapp.main.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.widget.photoview.EasePhotoView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.AddUGCParam;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.main.model.event.ChannelAddressEvent;
import com.moreclub.moreapp.main.model.event.ChannelPublishSuccessEvent;
import com.moreclub.moreapp.main.model.event.ChannelTopicEvent;
import com.moreclub.moreapp.main.presenter.IPublishUGCLoader;
import com.moreclub.moreapp.main.presenter.PublishUGCLoader;
import com.moreclub.moreapp.main.ui.adapter.MerchantScenePagerAdapter;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UploadUserImage;
import com.moreclub.moreapp.ucenter.model.UploadedUGCChannel;
import com.moreclub.moreapp.ucenter.presenter.UserDetailsV2Loader;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.adapter.RecyclerPhotoAdapter;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AliUploadUtils;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.DataCleanManager;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.PutObjectSamples;
import com.moreclub.moreapp.util.SpannableClick;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH_TEMP;
import static com.moreclub.moreapp.util.RandomUtil.generateString;

/**
 * Created by Captain on 2017/8/24.
 */

public class PublishMChannelActivity extends BaseActivity implements IPublishUGCLoader.LoaderPublishUGCDataBinder {
    private static final int REQUEST_CAMERA_CODE = 1001;
    private static final int REQUEST_PREVIEW_CODE = 1002;
    private static final int REQUEST_CODE_READ = 1003;
    private static final int SEND_UGC = 1004;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final static String KEY_CITY_CODE = "city.code";
    private final static String KEY_CITY_ID = "city.id";

    @BindView(R.id.nav_back)
    TextView nav_back;
    @BindView(R.id.publishBtn)
    TextView publishBtn;
    @BindView(R.id.publishdes)
    EditText publishdes;
    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;
    @BindView(R.id.reviewPicLayout)
    FrameLayout reviewPicLayout;
    @BindView(R.id.reviewPic)
    EasePhotoView reviewPic;
    @BindView(R.id.address_tag)
    TextView selectAddress;
    @BindView(R.id.topic_tag)
    TextView selectTopic;

    @BindView(R.id.address_name)
    TextView addressName;
    @BindView(R.id.topic_name)
    TextView topicName;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;

    private RecyclerPhotoAdapter adapter;
    private ArrayList<String> imagePaths;
    private ArrayList<String> imageBigUrls;
    private ArrayList<String> upLoadUrls;
    private ArrayList<String> selectPath;
    private ArrayList<String> photoTemp;
    private HashMap<String, String> uploadMap;
    private PopupWindow mPopupWindow;
    private View popupParentView;
    private ViewPagerFixed sceneViewPager;


    private int upLoadCount;
    private int selectCount;
    private int cityID;
    private int mid;
    private int topicID;
    private int photo_select_count = 9;
    private boolean doNothing;
    private boolean ready;
    private boolean uploaded = true;
    private ArrayList<MerchantItem> addresses;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SEND_UGC) {
                Toast.makeText(PublishMChannelActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                rl_progress.setVisibility(View.GONE);
                uploaded = true;
                upLoadCount = 0;
                if (ready) {
                    sendMyUgc();
                }
                ready = false;
            }
        }
    };
    private String from;
    private int size;

    @Override
    protected int getLayoutResource() {
        return R.layout.publish_mchannel_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        initData();
        loadData();
        setupViews();
    }

    private void loadData() {
        ((PublishUGCLoader) mPresenter).loadNearbyMerchant(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"),
                0, 5, MoreUser.getInstance().getUserLocationLng()
                        + "," + MoreUser.getInstance().getUserLocationLat());
        ((PublishUGCLoader) mPresenter).loadTopicList(cityID);
    }

    @Override
    protected Class getLogicClazz() {
        return IPublishUGCLoader.class;
    }

    private void initData() {
        imagePaths = new ArrayList<>();
        imageBigUrls = new ArrayList<>();
        upLoadUrls = new ArrayList<>();
        selectPath = new ArrayList<>();
        photoTemp = new ArrayList<>();
        uploadMap = new HashMap<>();
        imagePaths.add("+");
        cityID = PrefsUtils.getInt(this, KEY_CITY_ID, 2);
    }

    @SuppressLint("NewApi")
    private void setupViews() {
//        from_user_detail = getIntent().getBooleanExtra("from_user_detail", false);
        from = getIntent().getStringExtra("from");
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(publishdes.getText().toString()) ||
                        imagePaths.size() > 1) {
                    View inflate = View.inflate(PublishMChannelActivity.this, R.layout.sign_inter_delete_dialog, null);
                    TextView title = (TextView) inflate.findViewById(R.id.title);
                    TextView save = (TextView) inflate.findViewById(R.id.delete);
                    TextView cancel = (TextView) inflate.findViewById(R.id.cancel);
                    title.setText("你确定放弃发布吗?");
                    save.setText("确定");
                    cancel.setText("取消");
                    final AlertDialog dialog = new AlertDialog.Builder(PublishMChannelActivity.this)
                            .setView(inflate)
                            .show();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            doNothing = true;
                            finish();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    doNothing = true;
                    finish();
                }
            }
        });
        ready = false;
        adapter = new RecyclerPhotoAdapter(PublishMChannelActivity.this
                , R.layout.simple_imageview, imagePaths);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 4
                , GridLayoutManager.VERTICAL, false);
        rvPhotos.setLayoutManager(lm);
        rvPhotos.setAdapter(adapter);
        adapter.setHasHeader(false);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (!ready && uploaded) {
                    String itemString = (String) o;
                    if ("+".equals(itemString)) {
                        //Todo 上传照片
                        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                        selectPath.clear();
                        selectCount = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                            requestPermission();
                        } else {
                            clearAddTag();
                            PhotoPicker.builder()
                                    .setPhotoCount(photo_select_count)
                                    .setShowCamera(true)
                                    .setShowGif(true)
                                    .setPreviewEnabled(false)
                                    .start(PublishMChannelActivity.this, PhotoPicker.REQUEST_CODE);
                        }
                    } else {
                        if (imagePaths.size() > position && position >= 0) {
                            uploaded = true;
                            if (imagePaths.size() == 9) {
                                showAllSubject(imagePaths, position);
                            } else {
                                List<String> clickArr = imagePaths.subList(0, imagePaths.size() - 1);
                                showAllSubject(clickArr, position);
                            }
                        } else {
                            uploaded = true;
                        }
                    }
                } else {
                    Toast.makeText(PublishMChannelActivity.this, "图片正在上传中", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publishdes.getText().toString() == null || publishdes.getText().toString().length() == 0) {
                    Toast.makeText(PublishMChannelActivity.this, "请输入动态内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uploaded) {
                    publishBtn.setClickable(false);
                    sendMyUgc();
                } else if (imagePaths.size() > 1) {
                    ready = true;
                    publishBtn.setClickable(false);
                    rl_progress.setVisibility(View.VISIBLE);
                }
            }
        });
        publishdes.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence != null && charSequence.length() > 0) {
                    publishBtn.setBackgroundResource(R.drawable.publish_channel_used);
                    publishBtn.setEnabled(true);
                } else {
                    publishBtn.setBackgroundResource(R.drawable.publish_channel_noused);
                    publishBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void sendMyUgc() {
        AddUGCParam param = new AddUGCParam();
        param.setUid(MoreUser.getInstance().getUid());
        if (upLoadUrls != null && upLoadUrls.size() > 0) {
            param.setTitlePicture(upLoadUrls.get(0));
            if (upLoadUrls.size() > 9) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    temp.add(upLoadUrls.get(i));
                }
                upLoadUrls.clear();
                upLoadUrls.addAll(temp);
            }
            param.setPictures(upLoadUrls);
        }
        param.setContent(publishdes.getText().toString());
        param.setMediaType(0);
        param.setMediaUrl("");
        if (mid > 0)
            param.setMid(mid);
        param.setCity(cityID);
        ArrayList<Integer> chainInters = new ArrayList<>();
        chainInters.add(topicID);
        param.setChainInters(chainInters);
        ((PublishUGCLoader) mPresenter).uploadUGC(param);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            clearAddTag();
            imagePaths.add("+");
            photoTemp.add("+");
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
            uploaded = true;
            return;
        }
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                refreshAdpater(photos);
            }
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(PublishMChannelActivity.this, permissions[0]) != PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(PublishMChannelActivity.this, permissions[1]) != PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            ActivityCompat.requestPermissions(PublishMChannelActivity.this, permissions, REQUEST_CODE_READ);
        } else {
            clearAddTag();
            PhotoPicker.builder()
                    .setPhotoCount(photo_select_count)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .start(this, PhotoPicker.REQUEST_CODE);
        }
    }

    private void clearAddTag() {

        if (imagePaths.size() > 0) {
            for (int i = 0; i < imagePaths.size(); i++) {
                String item = imagePaths.get(i);
                if ("+".equals(item)) {
                    imagePaths.remove(i);
                }
            }
        }

        if (photoTemp.size() > 0) {
            for (int i = 0; i < photoTemp.size(); i++) {
                String item = photoTemp.get(i);
                if ("+".equals(item)) {
                    photoTemp.remove(i);
                }
            }
        }
    }

    private void refreshAdpater(ArrayList<String> paths) {
        // 处理返回照片地址
        if (paths.size() > 0) {
            uploaded = false;
            for (int i = 0; i < paths.size(); i++) {
                uploadMap.put(paths.get(i), getImageObjectKey("" + MoreUser.getInstance().getUid()));
            }
        }
        selectCount += paths.size();
        for (int i = 0; i < paths.size(); i++) {
            photoTemp.add(paths.get(i));
        }
        imagePaths.clear();
        if (photoTemp.size() >= 9) {
            for (int i = 0; i < 9; i++) {
                imagePaths.add(photoTemp.get(i));
            }
            photoTemp.clear();
            photoTemp.addAll(imagePaths);
            adapter.notifyDataSetChanged();
        } else {
            photo_select_count = 9 - (photoTemp.size());
            imagePaths.addAll(photoTemp);
            imagePaths.add("+");
            adapter.notifyDataSetChanged();
        }
        uploadImages(paths);
    }

    private void uploadImages(ArrayList<String> paths) {
        if (paths == null || paths.size() == 0) return;
        for (int i = 0; i < paths.size(); i++) {
            PutObjectSamples.OSSUploadResultListener uploadResultListener = new PutObjectSamples.OSSUploadResultListener() {
                @Override
                public void onSuccess(String imgPath) {
                    upLoadCount++;
                    upLoadUrls.add(imgPath);
                    if (upLoadCount == selectCount) {
                        UploadUserImage images = new UploadUserImage();
                        if (MoreUser.getInstance().getUid() != null)
                            images.setUid(MoreUser.getInstance().getUid());
                        images.setUrls(upLoadUrls);

                        handler.sendEmptyMessage(SEND_UGC);


//                        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
//                            @Override
//                            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
//                                if (response != null && response.body() != null && response.body().getData()) {
//                                    rl_progress.setVisibility(View.GONE);
//                                    Toast.makeText(PublishMChannelActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//                                    uploaded = true;
//                                    ready = false;
//                                    upLoadCount = 0;
//                                    if (ready) {
//                                        sendMyUgc();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
//                                rl_progress.setVisibility(View.GONE);
//                                Toast.makeText(PublishMChannelActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
//                                uploaded = true;
//                                upLoadCount = 0;
//                            }
//                        };
//                        ApiInterface.ApiFactory.createApi(MoreUser.getInstance().getAccess_token())
//                                .onUpLoadImages(images).enqueue(callback);
                    }
                    Log.i("zune:", "imgPath = " + imgPath);
                }

                @Override
                public void onFailures(String error) {
                    upLoadCount++;
                    if (upLoadCount == selectCount) {
                        UploadUserImage images = new UploadUserImage();
                        if (MoreUser.getInstance().getUid() != null)
                            images.setUid(MoreUser.getInstance().getUid());
                        images.setUrls(upLoadUrls);
                        ((UserDetailsV2Loader) mPresenter).onUploadImages(MoreUser.getInstance().getAccess_token()
                                , images);
                    }
                    Log.i("zune:", "error = " + error);
                }
            };
            new AliUploadUtils(PublishMChannelActivity.this,
                    Constants.ALI_OSS_BUCKETNAME, uploadMap.get(paths.get(i)), paths.get(i),
                    MoreUser.getInstance().getUid() + "",
                    uploadResultListener);
        }
    }

    //通过UserCode 加上日期组装 OSS路径
    private String getImageObjectKey(String strUserCode) {
        Date date = new Date();
        return new SimpleDateFormat("yyyy/M/d").format(date) + "/" + strUserCode + new SimpleDateFormat("yyyyMMddssSSS").format(date) + generateString(12) + ".jpg";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    //用户同意授权
                    clearAddTag();
                    PhotoPicker.builder()
                            .setPhotoCount(photo_select_count)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start(this, PhotoPicker.REQUEST_CODE);
                } else {
                    //用户拒绝授权
                    try {
                        clearAddTag();
                        PhotoPicker.builder()
                                .setPhotoCount(photo_select_count)
                                .setShowCamera(true)
                                .setShowGif(true)
                                .setPreviewEnabled(false)
                                .start(this, PhotoPicker.REQUEST_CODE);
                    } catch (Exception e) {
                        Log.i("zune:", "e = " + e);
                        Toast.makeText(PublishMChannelActivity.this, "使用外置存储卡,请手动设置", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                }
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @OnClick(R.id.address_lay)
    public void addressLayClick() {
        AppUtils.pushLeftActivity(this, FindAndSelectMerchantActivity.class);
    }

    @OnClick(R.id.topic_lay)
    public void topicLayClick() {
        AppUtils.pushLeftActivity(this, ChannelSelectTopicsActivity.class);

    }

    @Override
    public void onNearbyMerchantsResponse(RespDto response) {
        final StringBuilder merchants = new StringBuilder();
        if (response.isSuccess()) {
            ArrayList<MerchantItem> newMerchants = (ArrayList<MerchantItem>) response.getData();
            if (newMerchants != null && !newMerchants.isEmpty() && newMerchants.size() > 0) {
                addresses = new ArrayList<>();
                for (int i = 0; i < newMerchants.size(); i++) {
                    if (i == 2) {
                        break;
                    }
                    MerchantItem item = newMerchants.get(i);
                    merchants.append(item.getName());
                    merchants.append(" ");
                    addresses.add(item);
                }
            }
        }
        int startIndex = 0;
        int endIndex = 0;
        SpannableStringBuilder spannable = new SpannableStringBuilder(merchants.toString());
        for (int i = 0; i < 2; i++) {
            endIndex += addresses.get(i).getName().length() + 1;
            final int finalI = i;
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 选择酒吧
                    selectAddress.setText(addresses.get(finalI).getName());
                    mid = addresses.get(finalI).getMid();
                }
            };
            try {
                int color = R.color.star_location_change;
                spannable.setSpan(new SpannableClick(color, listener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = endIndex;
            } catch (Exception e) {

            }
        }
        addressName.setText(spannable);
        addressName.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onNearbyMerchantsFailure(String msg) {

    }

    @Override
    public void onTopicListFailure(String msg) {

    }

    @Override
    public void onUploadUGCFailure(String msg) {
        publishBtn.setClickable(true);
    }

    @Override
    public void onUploadUGCResponse(RespDto response) {
        publishBtn.setClickable(true);
        Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
        UploadedUGCChannel result = (UploadedUGCChannel) response.getData();
        if (result != null) {
            EventBus.getDefault().post(new ChannelPublishSuccessEvent(true));
        }
        finish();
    }

    @Override
    public void onTopicListResponse(RespDto response) {
        StringBuilder topics = new StringBuilder();
        final ArrayList<Topic> data = (ArrayList<Topic>) response.getData();
        final ArrayList<Topic> topicses = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (i == 2) {
                    break;
                }
                Topic item = data.get(i);
                topics.append(item.getName());
                topics.append(" ");
                topicses.add(data.get(i));
            }
        }
        int startIndex = 0;
        int endIndex = 0;
        SpannableStringBuilder spannable = new SpannableStringBuilder(topics.toString());
        for (int i = 0; i < data.size(); i++) {
            endIndex += data.get(i).getName().length() + 1;
            final int finalI = i;
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 选择话题
                    selectTopic.setText(data.get(finalI).getName());
                    topicID = data.get(finalI).getTid();
                }
            };
            try {
                int color = R.color.star_location_change;
                spannable.setSpan(new SpannableClick(color, listener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = endIndex;
            } catch (Exception e) {

            }
        }
        topicName.setText(spannable);
        topicName.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 图片预览popupWindow
     *
     * @param pics
     * @param clickpos
     */
    public void showAllSubject(List<String> pics, int clickpos) {
        if (null == mPopupWindow) {
            popupParentView = LayoutInflater.from(this).inflate(
                    R.layout.channel_photo_review_popuwindow, null);
            popupParentView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            int width = ScreenUtil.getScreenWidth(this);
            int height = ScreenUtil.getScreenHeight(this);

            ImageButton nav_back = (ImageButton) popupParentView.findViewById(R.id.nav_back);
            nav_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupParentView = null;
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
            });

            TextView delete_photo = (TextView) popupParentView.findViewById(R.id.delete_photo);
            delete_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = sceneViewPager.getCurrentItem();
                    String key = imagePaths.get(pos);
                    String aliKey = "http://" + Constants.ALI_OSS_BUCKETNAME + "." + Constants.ALI_OSS_END_POOINT + "/" + uploadMap.get(key);
                    for (int i = 0; i < upLoadUrls.size(); i++) {
                        String item = upLoadUrls.get(i);
                        if (item != null && aliKey != null && item.equals(aliKey)) {
                            upLoadUrls.remove(i);
                            break;
                        }
                    }

                    imagePaths.remove(pos);
                    photoTemp.remove(pos);
                    String addStr = imagePaths.get(imagePaths.size() - 1);
                    popupParentView = null;
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                    adapter.notifyDataSetChanged();

                    if ("+".equals(addStr)) {
                        photo_select_count = 9 - (photoTemp.size() - 1);
                    } else {
                        photo_select_count = 9 - (photoTemp.size());
                        imagePaths.add("+");
                        photoTemp.add("+");
                    }
                    size--;
                }
            });

            //设置弹出部分和面积大小
            mPopupWindow = new PopupWindow(popupParentView, width, height, true);
            //设置动画弹出效果
            mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mPopupWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);

        }
        popupWindowSetupView(popupParentView, pics, clickpos);
        int[] pos = new int[2];

        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
    }

    void popupWindowSetupView(View view, final List<String> pics, final int clickPos) {

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        final TextView sceneName = (TextView) view.findViewById(R.id.sceneName);
        ArrayList<String> showArr = new ArrayList<>();
        size = pics.size();
        for (int i = 0; i < size; i++) {
            String tempItem = pics.get(i);
            if ("+".equals(tempItem)) {
                continue;
            } else {
                showArr.add(tempItem);
            }
        }
        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(this, showArr);
        sceneViewPager.setAdapter(adapter);
        adapter.setOnItemClickListener(new MerchantScenePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }
        });

        sceneViewPager.setCurrentItem(clickPos);

        sceneViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                sceneName.setText((position + 1) + "/" + size);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (rl_progress.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "图片正在上传中", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(publishdes.getText().toString()) ||
                    imagePaths.size() > 1) {
                View inflate = View.inflate(PublishMChannelActivity.this, R.layout.sign_inter_delete_dialog, null);
                TextView title = (TextView) inflate.findViewById(R.id.title);
                TextView save = (TextView) inflate.findViewById(R.id.delete);
                TextView cancel = (TextView) inflate.findViewById(R.id.cancel);
                title.setText("你确定放弃发布吗?");
                save.setText("确定");
                cancel.setText("取消");
                final AlertDialog dialog = new AlertDialog.Builder(PublishMChannelActivity.this)
                        .setView(inflate)
                        .show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        doNothing = true;
                        finish();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                doNothing = true;
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        DataCleanManager.cleanCustomCache(SAVE_REAL_PATH_TEMP);
        if (!doNothing) {
            if ("UserDetailV2Activity".equals(from)) {
                Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
                if (adapters != null && adapters.get("0") != null) {
                    SignInteractSquareAdapter signInteractSquareAdapter = adapters.get("0");
                    signInteractSquareAdapter.upDatePopupWindow();
                } else {
                    AppUtils.pushLeftActivity(this, UserDetailV2Activity.class);
                }
            } else if ("MChannelFragment".equals(from)) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(this, SuperMainActivity.class);
                intent.putExtra("shouldGo", "MChannelFragment");
                intent.putExtra("from", "PublishMChannelActivity");
                ActivityCompat.startActivity(this, intent, compat.toBundle());
            } else if ("SpaceFragment".equals(from)) {
                //Todo Nothing
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressEvent(ChannelAddressEvent event) {
        MerchantItem item = event.getMerchantItem();
        if (item.getMid() == -1) {
            selectAddress.setText("所在位置");
            mid = 0;
        } else {
            selectAddress.setText(item.getName());
            mid = item.getMid();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicEvent(ChannelTopicEvent event) {
        Topic item = event.getTopic();
        if (item.getTid() == -1) {
            selectTopic.setText("添加话题");
            topicID = 0;
        } else {
            selectTopic.setText("#" + item.getName() + "#");
            topicID = item.getTid();
        }
    }
}
