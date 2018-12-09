package com.moreclub.moreapp.ucenter.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.photoview.EasePhotoView;
import com.hyphenate.easeui.widget.photoview.PhotoViewAttacher;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.ui.view.tag.FlowLayout;
import com.moreclub.common.ui.view.tag.TagAdapter;
import com.moreclub.common.ui.view.tag.TagFlowLayout;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.information.model.SignInterResult;
import com.moreclub.moreapp.information.ui.activity.ActivitiesActivity;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.main.ui.activity.PublishMChannelActivity;
import com.moreclub.moreapp.main.ui.adapter.MerchantScenePagerAdapter;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.LikeUser;
import com.moreclub.moreapp.ucenter.model.UploadUserImage;
import com.moreclub.moreapp.ucenter.model.UserDetails;
import com.moreclub.moreapp.ucenter.model.UserDetailsV2;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;
import com.moreclub.moreapp.ucenter.presenter.IUserDetailsV2Loader;
import com.moreclub.moreapp.ucenter.presenter.UserDetailsV2Loader;
import com.moreclub.moreapp.ucenter.ui.adapter.RecyclerPhotoAdapter;
import com.moreclub.moreapp.ucenter.ui.view.ArcImageView;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AliUploadUtils;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.DataCleanManager;
import com.moreclub.moreapp.util.FileSaveUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.PutObjectSamples;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.iwf.photopicker.PhotoPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.Gravity.CENTER;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.BELOW;
import static com.hyphenate.easeui.EaseConstant.ISSYSTEMMESSAGE;
import static com.moreclub.moreapp.ucenter.constant.Constants.ISFIRST_SHOW_LEFT_ONESHOT;
import static com.moreclub.moreapp.ucenter.constant.Constants.ISFIRST_SHOW_UP_ONESHOT;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH_TEMP;
import static com.moreclub.moreapp.util.RandomUtil.generateString;

/**
 * A login screen that offers login via email/password.
 */
public class UserDetailV2Activity extends BaseActivity implements IUserDetailsV2Loader.LoaderUserDetailsV2DataBinder {
    private static final int REQUEST_CODE_READ = 1003;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    @BindView(R.id.tv_activities_count)
    TextView tvActivitiesCount;
    @BindView(R.id.ll_activities_lay)
    LinearLayout llActivitiesLay;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    @BindView(R.id.view_divider1)
    View viewDivider1;
    @BindView(R.id.iv_activities_icon)
    ImageView ivActivitiesIcon;
    @BindView(R.id.tv_activities_title)
    TextView tvActivitiesTitle;
    @BindView(R.id.tv_activities_time)
    TextView tvActivitiesTime;
    @BindView(R.id.ll_first_activities_lay)
    RelativeLayout llFirstActivitiesLay;
    @BindView(R.id.iv_activities_icon2)
    ImageView ivActivitiesIcon2;
    @BindView(R.id.tv_activities_title2)
    TextView tvActivitiesTitle2;
    @BindView(R.id.tv_activities_time2)
    TextView tvActivitiesTime2;
    @BindView(R.id.ll_first_activities_lay2)
    RelativeLayout llFirstActivitiesLay2;
    @BindView(R.id.ll_activities)
    LinearLayout llActivities;
    @BindView(R.id.ll_upshow_oneshot)
    LinearLayout llUpshowOneshot;
    @BindView(R.id.ll_rightshow_oneshot)
    LinearLayout llRightshowOneshot;
    @BindView(R.id.aiv_top)
    ArcImageView aivTop;
    @BindView(R.id.civ_user_thumb)
    CircleImageView civUserThumb;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_photo_lay)
    LinearLayout llPhotoLay;
    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;
    @BindView(R.id.ll_news_lay)
    LinearLayout llNewsLay;
    @BindView(R.id.iv_news_icon)
    ImageView ivNewsIcon;
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.ll_first_lay)
    LinearLayout llFirstLay;
    @BindView(R.id.iv_news_icon2)
    ImageView ivNewsIcon2;
    @BindView(R.id.tv_news_title2)
    TextView tvNewsTitle2;
    @BindView(R.id.ll_second_lay)
    LinearLayout llSecondLay;
    @BindView(R.id.ll_news)
    LinearLayout llNews;
    @BindView(R.id.view_divider)
    View viewDivider;
    @BindView(R.id.tv_user_detail)
    TextView tvUserDetail;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.user_personal_mark)
    TextView userPersonalMark;
    @BindView(R.id.fl_tags)
    TagFlowLayout flTags;
    @BindView(R.id.view_divider2)
    View viewDivider2;
    @BindView(R.id.ll_goods_lay)
    LinearLayout llGoodsLay;
    @BindView(R.id.ll_goods)
    LinearLayout llGoods;
    @BindView(R.id.view_divider3)
    View viewDivider3;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.detailsScrollView)
    ObservableScrollView detailsScrollView;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.fl_root)
    RelativeLayout flRoot;
    @BindView(R.id.tv_photos_count)
    TextView tvPhotosCount;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.rl_default_photo)
    RelativeLayout rlDefaultPhoto;
    @BindView(R.id.view_divider0)
    View viewDivider0;
    @BindView(R.id.tv_news_count)
    TextView tvNewsCount;
    @BindView(R.id.iv_camera_news)
    ImageView ivCameraNews;
    @BindView(R.id.rl_default_news)
    RelativeLayout rlDefaultNews;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.iv_default_good)
    ImageView ivDefaultGood;
    @BindView(R.id.reviewPicLayout)
    FrameLayout reviewPicLayout;
    @BindView(R.id.reviewPic)
    EasePhotoView reviewPic;
    @BindView(R.id.view_weight)
    View view_weight;
    @BindView(R.id.tv_no_content)
    TextView tvNoContent;
    @BindView(R.id.tv_default_camera)
    TextView tvDefaultCamera;
    @BindView(R.id.tv_default_news_camera)
    TextView tvDefaultNewsCamera;
    @BindView(R.id.rl_default_news_photo)
    RelativeLayout rlDefaultNesPhoto;
    @BindView(R.id.iv_cool)
    ImageView ivCool;
    @BindView(R.id.view_weight_chat)
    View view_weight_chat;
    @BindView(R.id.invite_someone)
    TextView invite_someone;
    @BindView(R.id.interest)
    TextView interest;
    @BindView(R.id.tv_news_title_no_photo)
    TextView tvNoPhoto;
    @BindView(R.id.tv_news_title2_no_photo)
    TextView tvNoPhoto2;
    @BindView(R.id.ll_bottom_layout)
    RelativeLayout llBottomLayout;
    @BindView(R.id.ll_first_lay_without_photo)
    LinearLayout llFirstWithoutPhoto;
    @BindView(R.id.ll_second_lay_without_photo)
    LinearLayout llSecondWithoutPhoto;
    @BindView(R.id.nav_back)
    ImageButton nav_back;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightBtn;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.ll_sex)
    LinearLayout llSex;
    @BindView(R.id.tv_years_old)
    TextView tvYearsOld;
    @BindView(R.id.rl_cool)
    RelativeLayout rlCool;

    private UserDetailsV2 userDetails;
    private String toUid;
    private int mid;
    private int sid;
    private int mySid;
    private String isChannel;
    private int page = 0;
    private int pageSize = 20;
    private TagAdapter tagAdapter;
    private ArrayList<String> imagePaths;
    private ArrayList<String> imageBigUrls;
    private RecyclerPhotoAdapter adapter;
    private boolean isSelf;
    private boolean isOtherPeople;
    private int selectCount;
    private int upLoadCount;
    private ArrayList<String> upLoadUrls;
    private ArrayList<String> selectPath;
    private int pn = 0;
    private int ps = 99;
    private boolean hasGone = true;
    private PopupWindow popupWindow;
    private List<String> tags;
    private boolean isLiked;
    private boolean canLikeClick;
    private String invite = com.moreclub.moreapp.main.constant.Constants.TITLE_ANSWER;
    private int chatType;
    private String userThumb;
    private String nickName;
    private String inter = com.moreclub.moreapp.main.constant.Constants.TITLE_ALLOW;
    private boolean isFromSpace;
    private boolean isBar;
    private boolean followClickable = true;
    private View popupParentView;
    public PopupWindow mPopupWindow;
    public ViewPagerFixed sceneViewPager;
    private boolean canScroll;
    private List<UserDetailsV2.ProfileLikeBean.LikesBean> likes;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_detail_v2_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IUserDetailsV2Loader.class;
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
        loadData();
    }

    @SuppressLint("NewApi")
    private void setupView() {
        LinearLayout navLayout = (LinearLayout) findViewById(R.id.nav_layout);
        navLayout.setBackgroundColor(Color.parseColor("#00000000"));
        nav_back.setImageResource(R.drawable.close);
        nav_back.setVisibility(View.VISIBLE);
        toUid = getIntent().getStringExtra("toUid");
        if (TextUtils.isEmpty(toUid))
            toUid = "0";
        if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
            isSelf = true;
            navRightBtn.setImageResource(R.drawable.write_user_info);
        } else {
            isOtherPeople = true;
            navRightBtn.setImageResource(R.drawable.user_details_menu);
        }
        navRightBtn.setVisibility(VISIBLE);
        mid = getIntent().getIntExtra("mid", -1);
        sid = getIntent().getIntExtra("sid", -1);
        mySid = getIntent().getIntExtra("mySid", -1);
        isChannel = getIntent().getStringExtra("isChannel");
        isFromSpace = getIntent().getBooleanExtra("isFromSpace", false);
        if (toUid.equals(MoreUser.getInstance().getUid() + "")) {
            tvDefaultCamera.setText("上传照片");
            tvDefaultNewsCamera.setText("发动态");
            llBottomLayout.setVisibility(View.GONE);
        } else {
            llBottomLayout.setVisibility(VISIBLE);
            tvDefaultCamera.setText("邀请TA");
            tvDefaultNewsCamera.setText("邀请TA");
        }
        if (mySid <= 0 || mid > 0 || "true".equals(isChannel)) {
            invite_someone.setVisibility(View.GONE);
        } else {
            invite_someone.setVisibility(View.VISIBLE);
        }
        imagePaths = new ArrayList<>();
        imageBigUrls = new ArrayList<>();
        upLoadUrls = new ArrayList<>();
        selectPath = new ArrayList<>();
        tags = new ArrayList<>();
        likes = new ArrayList<>();
        adapter = new RecyclerPhotoAdapter(UserDetailV2Activity.this
                , R.layout.simple_imageview, imagePaths);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 4
                , GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvPhotos.setLayoutManager(lm);
        rvPhotos.setAdapter(adapter);
        adapter.setHasHeader(false);
        rvPhotos.setVisibility(View.GONE);
        rlDefaultPhoto.setVisibility(View.VISIBLE);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (isSelf) {
                    position--;
                }
                if (imageBigUrls.size() > position && position >= 0) {
                    showAllSubject(imageBigUrls, position);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        reviewPic.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View v,float x,float y) {
                if (reviewPicLayout.getVisibility() == View.VISIBLE) {
                    reviewPicLayout.setVisibility(View.GONE);
                }
            }
        });
        reviewPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSaveDialog(0);
                return false;
            }
        });
        if (isFromSpace) {
            int height = ScreenUtil.getScreenHeight(this);
            int width = ScreenUtil.getScreenWidth(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            view_weight.setLayoutParams(lp);
            view_weight.setVisibility(View.GONE);
        }
        if (isFromSpace && PrefsUtils.getBoolean(this, ISFIRST_SHOW_UP_ONESHOT, true)) {
            llUpshowOneshot.setVisibility(View.GONE);
        }
        if (isFromSpace && PrefsUtils.getBoolean(this, ISFIRST_SHOW_LEFT_ONESHOT, false)) {
            llRightshowOneshot.setVisibility(View.GONE);
        }
        final float[] firstX = {0};
        final float[] firstY = {0};
        final float[] lastX = {0};
        final float[] lastY = {0};
        setupMenu();
        detailsScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (llUpshowOneshot.getVisibility() == VISIBLE) {
                    PrefsUtils.setBoolean(UserDetailV2Activity.this, ISFIRST_SHOW_UP_ONESHOT, false);
                    llUpshowOneshot.setVisibility(View.GONE);
                }
                switch (event.getAction()) {
                    case ACTION_DOWN:
                        firstX[0] = event.getX();
                        firstY[0] = event.getY();
                        break;
                    case ACTION_MOVE:
                        if (firstX[0] <= 0f)
                            firstX[0] = event.getX();
                        else
                            lastX[0] = event.getX();
                        if (firstY[0] <= 0f)
                            firstY[0] = event.getY();
                        else
                            lastY[0] = event.getY();
                        float scrollY = detailsScrollView.getScrollY();
                        Log.i("zune:", "scrollY = " + scrollY);
                        boolean horizonal = Math.abs(lastX[0] - firstX[0]) > Math.abs(lastY[0] - firstY[0]);
                        if (!hasGone && scrollY > 0 && !horizonal) {
                            hindViewWeight();
                        }
                        break;
                    case ACTION_UP:
                        lastY[0] = event.getY();
                        break;
                }
                lastX[0] = event.getX();
                lastY[0] = event.getY();
                float currentScrollY = detailsScrollView.getScrollY();
                Log.i("zune:", "firstY = " + firstY[0]);
                Log.i("zune:", "lastY = " + lastY[0]);
                Log.i("zune:", "currentScrollY = " + currentScrollY);
                if (Math.abs(lastX[0] - firstX[0]) > Math.abs(lastY[0] - firstY[0])) {
                    //Todo 横向滑动

                } else if (lastY[0] - firstY[0] < 0 || currentScrollY > 0) {
                    //Todo 向上滑动
                    if (!hasGone) {
                        hindViewWeight();
                    } else {
                        //Todo 向下滑动
                    }
                }
                firstY[0] = 0;
                return canScroll;
            }
        });
    }

    private void showSaveDialog(final int pos) {
        AppCompatActivity activity = this;
        View inflate = View.inflate(activity, R.layout.sign_inter_delete_dialog, null);
        TextView title = (TextView) inflate.findViewById(R.id.title);
        TextView save = (TextView) inflate.findViewById(R.id.delete);
        TextView cancel = (TextView) inflate.findViewById(R.id.cancel);
        title.setText("是否保存图片?");
        save.setText("保存");
        cancel.setText("取消");
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(inflate)
                .show();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture(pos);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void savePicture(int position) {
        new FileSaveUtils().getBitMBitmap(userDetails.getThumb(), new FileSaveUtils.OnSaveListener() {
            @Override
            public void getFilesResponse(Bitmap map) {
                FileSaveUtils.saveImageToGallery(UserDetailV2Activity.this, map, SAVE_REAL_PATH);
            }

            @Override
            public void getFilesFail() {

            }
        });
    }

    @SuppressLint("NewApi")
    private void hindViewWeight() {
        canScroll = true;
        try {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view_weight, "translationY"
                    , 0, -(int) (ScreenUtil.getScreenHeight(UserDetailV2Activity.this) * 0.35f));
            animator.setDuration(100);
            animator.setInterpolator(ACCELERATE_INTERPOLATOR);
            animator.start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float animatedValue = (Float) animation.getAnimatedValue();
                    int height = (int) (ScreenUtil.getScreenHeight(UserDetailV2Activity.this) * 0.35f);
                    height += animatedValue;
                    int width = ScreenUtil.getScreenWidth(UserDetailV2Activity.this);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
                    view_weight.setLayoutParams(lp);
                    view_weight.setVisibility(View.GONE);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    detailsScrollView.smoothScrollTo(0, 0);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    canScroll = false;
                    detailsScrollView.smoothScrollTo(0, 0);
                }
            });
            nav_back.setVisibility(View.VISIBLE);
            navRightBtn.setVisibility(VISIBLE);
        } catch (Exception e) {
            Log.i("zune:", "e = " + e);
        }
        hasGone = true;
    }

    /**
     * 图片预览popupWindow
     *
     * @param pics
     * @param clickpos
     */
    public void showAllSubject(ArrayList<String> pics, int clickpos) {
        if (null == mPopupWindow) {
            popupParentView = LayoutInflater.from(this).inflate(
                    R.layout.merchant_details_scene_popuwindow, null);
            popupParentView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            int width = ScreenUtil.getScreenWidth(this);
            int height = ScreenUtil.getScreenHeight(this);

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


        mPopupWindow.showAtLocation(getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
    }

    void popupWindowSetupView(View view, final ArrayList<String> pics, int clickPos) {

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        final TextView sceneName = (TextView) view.findViewById(R.id.sceneName);

        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(this, pics);

        sceneViewPager.setAdapter(adapter);

        adapter.setOnItemClickListener(new MerchantScenePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }
        });

        sceneViewPager.setCurrentItem(clickPos);
        sceneName.setText((clickPos + 1) + " / " + pics.size());
        sceneViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sceneName.setText((position + 1) + " / " + pics.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupMenu() {
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).
                inflate(R.layout.user_details_menu, null);
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);
        TextView menu_report = (TextView) bubbleLayout.findViewById(R.id.menu_report);
        menu_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailV2Activity.this,
                        UserDetailsReportActivity.class);
                intent.putExtra("relationID", toUid);
                intent.putExtra("type", "3");
                ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
            }
        });
    }

    private void loadData() {
        if ("true".equals(isChannel)) {
            ((UserDetailsV2Loader) mPresenter).onUserActiveDetails(MoreUser.getInstance().getUid(), Long.parseLong(toUid));
//            ((UserDetailsV2Loader) mPresenter).loadUserActiveList(MoreUser.getInstance().getUid(), Long.parseLong(toUid), 0, 2);
            if (!TextUtils.isEmpty(toUid))
                ((UserDetailsV2Loader) mPresenter).onLoadState(MoreUser.getInstance().getAccess_token(), Long.parseLong(toUid), MoreUser.getInstance().getUid());
            llGoods.setVisibility(View.GONE);
            llGoodsLay.setVisibility(View.GONE);
            ivDefaultGood.setVisibility(View.GONE);
            viewDivider3.setVisibility(View.GONE);
            rlCool.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(toUid)) {
                ((UserDetailsV2Loader) mPresenter).loadUserDetails(Long.parseLong(toUid), MoreUser.getInstance().getUid());
                ((UserDetailsV2Loader) mPresenter).onLoadState(MoreUser.getInstance().getAccess_token(), Long.parseLong(toUid), MoreUser.getInstance().getUid());
            }
            if (mid > 0) {
                ((UserDetailsV2Loader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), page, pageSize);
            }
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rlDefaultPhoto.getLayoutParams();
        lp.width = (ScreenUtil.getScreenWidth(UserDetailV2Activity.this) - ScreenUtil.dp2px(this, 32)) / 4;
        lp.height = (ScreenUtil.getScreenWidth(UserDetailV2Activity.this) - ScreenUtil.dp2px(this, 32)) / 4;
        lp.setMargins(ScreenUtil.dp2px(this, 16), 2, 2, 2);
        rlDefaultPhoto.setLayoutParams(lp);

//        ViewGroup.MarginLayoutParams rlDefaultNewsPhotoLP = (ViewGroup.MarginLayoutParams) rlDefaultPhoto.getLayoutParams();
//        rlDefaultNewsPhotoLP.width = (ScreenUtil.getScreenWidth(this) - ScreenUtil.dp2px(this, 32)) / 4;
//        rlDefaultNewsPhotoLP.height = (ScreenUtil.getScreenWidth(this) - ScreenUtil.dp2px(this, 32)) / 4;
//        rlDefaultNewsPhotoLP.setMargins(ScreenUtil.dp2px(this, 16), 2, 2, 2);
//        rlDefaultNesPhoto.setLayoutParams(rlDefaultNewsPhotoLP);

        if (rlDefaultPhoto.getVisibility() == View.VISIBLE) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
            layoutParams.addRule(BELOW, R.id.rl_default_photo);
            viewDivider0.setLayoutParams(layoutParams);
        }
    }

    private void hintUserInfo() {
        tvUserDetail.setVisibility(View.GONE);
        tvCity.setVisibility(View.GONE);
        userPersonalMark.setVisibility(View.GONE);
        viewDivider2.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llGoodsLay.getLayoutParams();
        if (viewDivider.getVisibility() == VISIBLE)
            layoutParams.addRule(BELOW, R.id.view_divider);
        else if (viewDivider1.getVisibility() == VISIBLE)
            layoutParams.addRule(BELOW, R.id.view_divider1);
        llGoodsLay.setLayoutParams(layoutParams);
    }

    @SuppressLint("NewApi")
    @OnClick({R.id.aiv_top, R.id.nav_back, R.id.civ_user_thumb, R.id.ll_photo_lay, R.id.ll_news_lay, R.id.view_weight,
            R.id.iv_news_icon, R.id.ll_first_lay, R.id.iv_news_icon2, R.id.ll_second_lay, R.id.rl_default_photo,
            R.id.ll_news, R.id.ll_goods_lay, R.id.ll_goods, R.id.tv_follow, R.id.tv_chat, R.id.rl_cool
            , R.id.tv_i_know_left, R.id.tv_i_know_up, R.id.nav_right_btn, R.id.rl_default_news_photo
            , R.id.invite_someone, R.id.interest, R.id.ll_activities_lay, R.id.rl_root, R.id.ll_first_lay_without_photo,
            R.id.ll_second_lay_without_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aiv_top:
                if (isSelf)
                    AppUtils.pushLeftActivity(UserDetailV2Activity.this, UserModifyDetailActivity.class);
                break;
            case R.id.rl_root:
                break;
            case R.id.invite_someone:
                //Todo 邀请他拼座互动
                SignInterResult params = new SignInterResult();
                params.setFromUid(MoreUser.getInstance().getUid());
                params.setUid(Long.parseLong(toUid));
                params.setTitle(com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_INTEREST);
                params.setSid(mySid);
                replyInvite(params);
                break;
            case R.id.interest:
                //Todo 我感兴趣
                SignInter inter = new SignInter();
                inter.setUid(Long.parseLong(toUid));
                inter.setFromUid(MoreUser.getInstance().getUid());
                inter.setSid(sid);
                inter.setTitle(com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_INTEREST);
                ((UserDetailsV2Loader) mPresenter).onPostSignInter(MoreUser.getInstance().getAccess_token(), inter);
                break;
            case R.id.ll_activities_lay:
                ActivityOptionsCompat compat_activities = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent_activities = new Intent(this, ActivitiesActivity.class);
                intent_activities.putExtra("mid", mid);
                ActivityCompat.startActivity(this, intent_activities, compat_activities.toBundle());
                break;
            case R.id.rl_default_news_photo:
                if (toUid.equals(MoreUser.getInstance().getUid() + "")) {
                    //Todo 发起动态
                    ActivityOptionsCompat compat5 = ActivityOptionsCompat.makeCustomAnimation(
                            UserDetailV2Activity.this, R.anim.push_down_in, R.anim.push_down_out);
                    Intent intent5 = new Intent(UserDetailV2Activity.this, PublishMChannelActivity.class);
                    intent5.putExtra("from", "UserDetailV2Activity");
                    ActivityCompat.startActivity(UserDetailV2Activity.this, intent5, compat5.toBundle());
                } else {
                    Context context = UserDetailV2Activity.this;
                    String str = "btn_invite_add_picture";
                    Map<String, String> map = new HashMap<>();
                    map.put("muid", toUid + "");
                    map.put("suid", MoreUser.getInstance().getUid() + "");
                    if (Build.VERSION.SDK_INT > 22) {
                        int du = 2000000000;
                        MobclickAgent.onEventValue(context, str, map, du);
                    } else {
                        MobclickAgent.onEvent(context, str, map);
                    }
                    Toast.makeText(this, "您的邀请已发送", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_i_know_left:
            case R.id.tv_i_know_up:
                PrefsUtils.setBoolean(UserDetailV2Activity.this, ISFIRST_SHOW_UP_ONESHOT, false);
                PrefsUtils.setBoolean(UserDetailV2Activity.this, ISFIRST_SHOW_LEFT_ONESHOT, false);
                llUpshowOneshot.setVisibility(View.GONE);
                llRightshowOneshot.setVisibility(View.GONE);
                break;
            case R.id.nav_right_btn:
                if (isSelf) {
                    AppUtils.pushLeftActivity(UserDetailV2Activity.this, UserModifyDetailActivity.class);
                } else {
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], view.getHeight() + location[1]);
                }
                break;
            case R.id.view_weight:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
                break;
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
                break;
            case R.id.civ_user_thumb:
                reviewPicLayout.setVisibility(View.VISIBLE);
                Glide.with(UserDetailV2Activity.this)
                        .load(userDetails.getThumb())
                        .dontAnimate()
                        .into(reviewPic);
                break;
            case R.id.ll_photo_lay:
                ActivityOptionsCompat compat0 = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent0 = new Intent(UserDetailV2Activity.this, UserImagesActivity.class);
                intent0.putExtra("toUid", toUid);
                intent0.putExtra("from", "UserDetailV2Activity");
                ActivityCompat.startActivity(UserDetailV2Activity.this, intent0, compat0.toBundle());
                break;
            case R.id.rl_default_photo:
                if (toUid != null && toUid.equals(MoreUser.getInstance().getUid() + "")) {
                    selectPath.clear();
                    selectCount = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                        requestPermission();
                    } else {
                        //Todo 相册选择
                        PhotoPicker.builder()
                                .setPhotoCount(9)
                                .setShowCamera(true)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(this, PhotoPicker.REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(this, "您的邀请已发送", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_news_icon:
            case R.id.iv_news_icon2:
            case R.id.ll_first_lay:
            case R.id.ll_first_lay_without_photo:
            case R.id.ll_second_lay:
            case R.id.ll_second_lay_without_photo:
            case R.id.ll_news_lay:
            case R.id.ll_news:
                ActivityOptionsCompat compat4 = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent4 = new Intent(this, UserUgcListActivity.class);
                int type;
                if ("true".equals(isChannel)) {
                    type = 4;
                } else {
                    type = 1;
                }
                intent4.putExtra("type", type);
                intent4.putExtra("friendUid", toUid);
                if ("true".equals(isChannel)) {
                    intent4.putExtra("isChannel", true);
                }
                if (userDetails != null)
                    intent4.putExtra("hasFollow", userDetails.getHasFollow());
                ActivityCompat.startActivity(this, intent4, compat4.toBundle());
                break;
            case R.id.ll_goods_lay:
                ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent2 = new Intent(UserDetailV2Activity.this, UserGoodsActivity.class);
                intent2.putExtra("toUid", toUid);
                intent2.putExtra("from", "UserDetailV2Activity");
                ActivityCompat.startActivity(UserDetailV2Activity.this, intent2, compat2.toBundle());
                break;
            case R.id.ll_goods:
                break;
            case R.id.tv_follow:
                Log.d("zune", "关注");
                if (followClickable) {
                    if (!userDetails.getHasFollow()) {
                        if ("true".equals(isChannel)) {
                            ChannelAttentionParam param = new ChannelAttentionParam();
                            param.setFriendId(Long.valueOf(toUid));
                            param.setOwnerId(MoreUser.getInstance().getUid());
                            param.setRemark("1");
                            addMChannel(param);
                        } else {
                            addFollow();
                        }
                    } else {
                        deleteFollow();
                    }
                    followClickable = false;
                }
                break;
            case R.id.tv_chat:
                Log.d("zune", "发消息");
                if (userDetails != null && !TextUtils.isEmpty(toUid)) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(UserDetailV2Activity.this, ChatActivity.class);
                    intent.putExtra("userId", "" + toUid);
                    intent.putExtra("mid", "" + mid);
                    intent.putExtra("toChatUserID", "" + toUid);
                    intent.putExtra("toChatNickName", "" + userDetails.getNickName());
                    intent.putExtra("toChatHeaderUrl", "" + userDetails.getThumb());
                    intent.putExtra("ISFIGHTSEAT", 1);
                    ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
                }
                break;
            case R.id.rl_cool:
                if (canLikeClick && toUid != null) {
                    if (!isLiked) {
                        LikeUser user = new LikeUser();
                        user.setmUid(Long.parseLong(toUid));
                        user.setsUid(MoreUser.getInstance().getUid());
                        user.setState(true);
                        likeGoodResp(user);
                    } else {
                        LikeUser user = new LikeUser();
                        user.setmUid(Long.parseLong(toUid));
                        user.setsUid(MoreUser.getInstance().getUid());
                        user.setState(false);
                        likeGoodResp(user);
                    }
                }
                break;
        }
    }

    private void replyInvite(SignInterResult params) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                //Todo 拼座邀请成功
                if (response != null && response.body() != null) {
                    Boolean data = response.body().getData();
                    if (data != null && data) {
                        Toast.makeText(UserDetailV2Activity.this, "已发送邀请成功", Toast.LENGTH_SHORT).show();
                        //Todo 进入会话框
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(UserDetailV2Activity.this, ChatActivity.class);
                        intent.putExtra("userId", toUid + "");
                        intent.putExtra("toChatUserID", toUid);
                        intent.putExtra("toChatNickName", userDetails.getNickName());
                        intent.putExtra("toChatHeaderUrl", userDetails.getThumb());
                        intent.putExtra("invite", invite);
                        intent.putExtra("ISFIGHTSEAT", 1);
                        ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
                        EMMessage message = EMMessage.createTxtSendMessage
                                (com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_REPLY, toUid + "");
                        sendMessage(message);
                    } else
                        Toast.makeText(UserDetailV2Activity.this, response.body().getErrorDesc(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

            }
        };
        ApiInterface.ApiFactory.createApi(MoreUser.getInstance().getAccess_token())
                .onReplyInvite(params).enqueue(callback);
    }

    private void addMChannel(ChannelAttentionParam param) {
        Callback<RespDto<ChannelAttentionResult>> callback
                = new Callback<RespDto<ChannelAttentionResult>>() {
            @Override
            public void onResponse(Call<RespDto<ChannelAttentionResult>> call,
                                   Response<RespDto<ChannelAttentionResult>> response) {
                followClickable = true;
                if (!userDetails.getHasFollow()) {
                    userDetails.setHasFollow(true);
                    tvFollow.setText("关注中");
                }
            }

            @Override
            public void onFailure(Call<RespDto<ChannelAttentionResult>> call, Throwable t) {
                followClickable = true;
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.main.api.ApiInterface.ApiFactory.createApi(token).onChannelAttention(param).enqueue(callback);
    }

    private void deleteFollow() {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                followClickable = true;
                if (userDetails.getHasFollow()) {
                    userDetails.setHasFollow(false);
                    tvFollow.setText("关注");
                    Toast.makeText(UserDetailV2Activity.this, "取消成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                followClickable = true;
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        UserFollowParam param = new UserFollowParam();
        param.setUid(MoreUser.getInstance().getUid() + "");
        param.setFriend(toUid);
        ApiInterface.ApiFactory.createApi(token).onFollowDel(MoreUser.getInstance().getUid() + ""
                , toUid, param).enqueue(callback);
    }

    private void addFollow() {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                followClickable = true;
                if (!userDetails.getHasFollow()) {
                    userDetails.setHasFollow(true);
                    tvFollow.setText("关注中");
                    Toast.makeText(UserDetailV2Activity.this, "关注成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                followClickable = true;
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        UserFollowParam param = new UserFollowParam();
        param.setFriendId(toUid);
        param.setOwnerId(MoreUser.getInstance().getUid() + "");
        ApiInterface.ApiFactory.createApi(token).onFollowAdd(param).enqueue(callback);
    }

    private void likeGoodResp(LikeUser user) {
        Callback<RespDto<LikeUser>> callback = new Callback<RespDto<LikeUser>>() {
            @Override
            public void onResponse(Call<RespDto<LikeUser>> call, Response<RespDto<LikeUser>> response) {
                canLikeClick = true;
                if (response == null && response.body() != null) return;
                LikeUser data = response.body().getData();
                isLiked = data.getState();
                if (isLiked) {
                    Toast.makeText(UserDetailV2Activity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                    ivCool.setImageResource(R.drawable.like_highlight_btn);
                    UserDetailsV2.ProfileLikeBean.LikesBean like = new UserDetailsV2.ProfileLikeBean.LikesBean();
                    like.setUid(MoreUser.getInstance().getUid());
                    like.setNickName(MoreUser.getInstance().getNickname());
                    if (!TextUtils.isEmpty(MoreUser.getInstance().getThumb()))
                        like.setThumb(MoreUser.getInstance().getThumb());
                    likes.add(like);
                } else {
                    Toast.makeText(UserDetailV2Activity.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
                    ivCool.setImageResource(R.drawable.like);
                    for (int i = 0; i < likes.size(); i++) {
                        if (likes.get(i).getUid() == MoreUser.getInstance().getUid()) {
                            likes.remove(i);
                            break;
                        }
                    }
                }
                llGoods.removeAllViews();
                tvGoodsCount.setText(likes.size() + "");
                setGoodsLay(likes);
            }

            @Override
            public void onFailure(Call<RespDto<LikeUser>> call, Throwable t) {
                canLikeClick = true;
            }
        };
        ApiInterface.ApiFactory.createApi(MoreUser.getInstance().getAccess_token()).onAddLikeGood(user).enqueue(callback);
    }

    public void onUserDetailsResponse(RespDto response) {
        if (response == null || response.getData() == null) return;
        userDetails = (UserDetailsV2) response.getData();
        if (userDetails.getUgcCount() > 0) {
            //Todo ugc
            llNews.setVisibility(View.VISIBLE);
            llNewsLay.setVisibility(View.VISIBLE);
            tvNewsCount.setText(userDetails.getUgcCount() + "");
            setupUgc(userDetails.getUgcSimpleDtos());
        } else {
            tvNewsCount.setText("0");
            llNews.setVisibility(View.VISIBLE);
            rlDefaultNesPhoto.setVisibility(View.VISIBLE);
            llNewsLay.setVisibility(View.VISIBLE);
            llFirstLay.setVisibility(View.GONE);
            llSecondLay.setVisibility(View.GONE);
            viewDivider1.setVisibility(View.VISIBLE);
        }
        UserDetailsV2.ProfileLikeBean profileLike = userDetails.getProfileLike();
        int count = profileLike.getCount();
        tvGoodsCount.setText(count + "");
        if (profileLike != null && profileLike.getLikes() != null)
            likes.addAll(profileLike.getLikes());
        setGoodsLay(likes);
        UserDetailsV2.ProfileAlbumBean profileAlbum = userDetails.getProfileAlbum();
        if (userDetails.getHasFollow()) {
            tvFollow.setText("关注中");
        } else {
            tvFollow.setText("关注");
        }
        if (userDetails.getSex() != null && userDetails.getSex().equals("-1")
                && mid > 0) {
            //Todo 商家主页
            isBar = true;
            loadActivities(userDetails.getProfileAcitivity());
        } else {
            llActivitiesLay.setVisibility(View.GONE);
            llFirstActivitiesLay.setVisibility(View.GONE);
            llFirstActivitiesLay2.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp_user = (RelativeLayout.LayoutParams) tvUserDetail.getLayoutParams();
            if (llNews.getVisibility() == View.VISIBLE) {
                lp_user.addRule(BELOW, R.id.view_divider1);
            } else {
                lp_user.addRule(BELOW, R.id.view_divider0);
            }
            tvUserDetail.setLayoutParams(lp_user);
            if (!TextUtils.isEmpty(userDetails.getBirthday())) {
                String[] birthdays = userDetails.getBirthday().split("-");
                if (birthdays != null && birthdays.length == 3) {
                    String s = birthdays[0] + "年" + birthdays[1] + "月" + birthdays[2] + "日";
                    getAgeDatas(s);
                }
            }
            setBirthdayStar();
            if (tags.size() != getTags().size()) {
                tags.addAll(getTags());
                tagAdapter = setTagAdapter(flTags, tags);
            }
        }
        if (profileAlbum != null) {
            List<UserDetailsV2.ProfileAlbumBean.AlbumsBean> albums = profileAlbum.getAlbums();
            if (albums == null)
                albums = new ArrayList<>();
            setPhotosLay(profileAlbum.getCount(), albums);
        }
        Glide.with(this)
                .load(userDetails.getThumb())
                .dontAnimate()
                .placeholder(R.drawable.profilephoto_small)
                .into(civUserThumb);
        int radius = 6;
        int margin = 4;
        Glide.with(this)
                .load(userDetails.getThumb())
                .dontAnimate()
                .bitmapTransform(new BlurTransformation(this, radius, margin))
                .into(aivTop);
        tvUserName.setText(userDetails.getNickName());
        String birthday = userDetails.getBirthday();
        if (birthday != null) {
            try {
                int age = TimeUtils.getAge(birthday);
                tvYearsOld.setText(age + "");
            } catch (Exception e) {
                Log.i("zune:", "e = " + e);
            }
        }
        if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("0")) {
            ivSex.setImageResource(R.drawable.femenine);
            tvYearsOld.setTextColor(Color.parseColor("#ed2929"));
        } else if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("1")) {
            ivSex.setImageResource(R.drawable.masculine);
            tvYearsOld.setTextColor(Color.parseColor("#2194ef"));
        } else {
            llSex.setVisibility(View.GONE);
        }
        userPersonalMark.setText(userDetails.getPersonalMark());
        if (TextUtils.isEmpty(userPersonalMark.getText().toString() + tvCity.getText().toString())
                && (tags == null || tags.size() == 0)) {
            if (toUid != null && !toUid.equals(MoreUser.getInstance().getUid() + "")) {
                hintUserInfo();
            } else if (toUid != null) {
                tvNoContent.setVisibility(View.VISIBLE);
            }
        } else {
            tvNoContent.setVisibility(View.GONE);
        }
        if (this.isFromSpace) {
            int height = (int) (ScreenUtil.getScreenHeight(this) * 0.35f);
            int width = ScreenUtil.getScreenWidth(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            view_weight.setLayoutParams(lp);
            view_weight.setVisibility(View.GONE);
        } else {
            hasGone = true;
            nav_back.setVisibility(View.VISIBLE);
        }
    }

    /**
     * zune:加载商家活动
     *
     * @param profileAcitivity
     */
    private void loadActivities(UserDetailsV2.ProfileAcitivityBean profileAcitivity) {
        if (profileAcitivity != null && profileAcitivity.getCount() > 0) {
            setupActivities(profileAcitivity.getRespList());
            tvActivitiesCount.setText(profileAcitivity.getCount() + "");
        } else {
            setupActivities(new ArrayList<UserDetailsV2.ProfileAcitivityBean.RespListBean>());
        }
    }

    private void setGoodsLay(List<UserDetailsV2.ProfileLikeBean.LikesBean> users) {
        if (users == null || users.size() == 0) {
            llGoodsLay.setVisibility(View.GONE);
            ivDefaultGood.setVisibility(View.GONE);
            viewDivider3.setVisibility(View.INVISIBLE);
            llGoods.setVisibility(View.GONE);
            return;
        }
        llGoods.setVisibility(VISIBLE);
        llGoodsLay.setVisibility(VISIBLE);
        viewDivider3.setVisibility(VISIBLE);
        RelativeLayout.LayoutParams lp0 = (RelativeLayout.LayoutParams) viewDivider3.getLayoutParams();
        lp0.addRule(BELOW, R.id.ll_goods);
        viewDivider3.setLayoutParams(lp0);
        if (users.size() > 0) {
            int margin = (int) ((ScreenUtil.getScreenWidth(this) - ScreenUtil.dp2px(this, 24) * 9.0f - ScreenUtil.dp2px(this, 16)) / 18.0f);
            for (int i = 0; i < 9; i++) {
                CircleImageView image = new CircleImageView(this);
                LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                        ScreenUtil.dp2px(this, 24), ScreenUtil.dp2px(this, 24));
                lp.leftMargin = margin;
                lp.rightMargin = margin;
                image.setLayoutParams(lp);
                if (users.size() >= 8) {
                    if (i == 8) {
                        TextView textView = new TextView(this);
                        LinearLayoutCompat.LayoutParams lp2 = new LinearLayoutCompat.LayoutParams(
                                ScreenUtil.dp2px(this, 24), ScreenUtil.dp2px(this, 24));
                        lp2.setMargins(margin, 0, margin, 0);
                        textView.setLayoutParams(lp2);
                        textView.setBackgroundResource(R.drawable.circle_blue);
                        textView.setGravity(CENTER);
                        textView.setTextColor(Color.WHITE);
                        textView.setText("8+");
                        llGoods.addView(textView);
                    } else {
                        Glide.with(this)
                                .load(users.get(i).getThumb())
                                .dontAnimate()
                                .placeholder(R.drawable.profilephoto_small)
                                .into(image);
                        llGoods.addView(image);
                    }
                } else {
                    if (i == users.size()) {
                        TextView textView = new TextView(this);
                        LinearLayoutCompat.LayoutParams lp2 = new LinearLayoutCompat.LayoutParams(
                                ScreenUtil.dp2px(this, 24), ScreenUtil.dp2px(this, 24));
                        lp2.setMargins(margin, 0, margin, 0);
                        textView.setLayoutParams(lp2);
                        textView.setBackgroundResource(R.drawable.circle_blue);
                        textView.setText(users.size() + "");
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(CENTER);
                        llGoods.addView(textView);
                    } else if (i < users.size()) {
                        Glide.with(this)
                                .load(users.get(i).getThumb())
                                .dontAnimate()
                                .placeholder(R.drawable.profilephoto_small)
                                .into(image);
                        llGoods.addView(image);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void setPhotosLay(int count, List<UserDetailsV2.ProfileAlbumBean.AlbumsBean> albums) {
        if (albums == null) {
            tvPhotosCount.setText("0");
            return;
        }
        int size = albums.size();
        tvPhotosCount.setText(count + "");
        for (int i = 0; i < size; i++) {
            String url = albums.get(i).getThumbUrl();
            String bigUrl = albums.get(i).getUrl();
            imageBigUrls.add(bigUrl);
            imagePaths.add(url);
        }
        if (size >= 8) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                temp.add(imagePaths.get(i));
            }
            imagePaths.clear();
            imagePaths.addAll(temp);
            temp.clear();
            if (isOtherPeople) {
                rlDefaultPhoto.setVisibility(View.GONE);
            } else if (isSelf) {
                rlDefaultPhoto.setVisibility(View.VISIBLE);
                imagePaths.add(0, imagePaths.get(0));
                imagePaths.remove(8);
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
            layoutParams.addRule(BELOW, R.id.rv_photos);
            viewDivider0.setLayoutParams(layoutParams);
            rvPhotos.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else if (size > 0) {
            if (isOtherPeople) {
                rlDefaultPhoto.setVisibility(View.GONE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
                layoutParams.addRule(BELOW, R.id.rv_photos);
                viewDivider0.setLayoutParams(layoutParams);
            } else if (isSelf) {
                rlDefaultPhoto.setVisibility(View.VISIBLE);
                imagePaths.add(0, imagePaths.get(0));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
                layoutParams.addRule(BELOW, R.id.rv_photos);
                viewDivider0.setLayoutParams(layoutParams);
            }
            adapter.notifyDataSetChanged();
            rvPhotos.setVisibility(View.VISIBLE);
        } else if (size == 0) {
            rvPhotos.setVisibility(View.GONE);
            rlDefaultPhoto.setVisibility(View.VISIBLE);
        }
    }

    private void setupActivities(final List<UserDetailsV2.ProfileAcitivityBean.RespListBean> activities) {
        if (activities == null) return;
        View.OnClickListener firstActivityClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**zune:进入活动详情**/
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailV2Activity.this, LiveActivity.class);
                intent.putExtra(com.moreclub.moreapp.information.constant.Constants.KEY_ACT_ID, activities.get(0).getActivityId());
                ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
            }
        };
        View.OnClickListener secondActivityClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**zune:进入活动详情**/
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailV2Activity.this, LiveActivity.class);
                intent.putExtra(com.moreclub.moreapp.information.constant.Constants.KEY_ACT_ID, activities.get(1).getActivityId());
                ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
            }
        };
        switch (activities.size()) {
            case 0:
                llActivitiesLay.setVisibility(View.GONE);
                llFirstActivitiesLay.setVisibility(View.GONE);
                llFirstActivitiesLay2.setVisibility(View.GONE);
                viewDivider.setVisibility(View.GONE);
                RelativeLayout.LayoutParams lp_user = (RelativeLayout.LayoutParams) tvUserDetail.getLayoutParams();
                if (llNews.getVisibility() == View.VISIBLE) {
                    lp_user.addRule(BELOW, R.id.view_divider1);
                } else {
                    lp_user.addRule(BELOW, R.id.view_divider0);
                }
                tvUserDetail.setLayoutParams(lp_user);
                break;
            case 1:
                llFirstActivitiesLay.setVisibility(View.VISIBLE);
                llFirstActivitiesLay2.setVisibility(View.GONE);
                Glide.with(this)
                        .load(activities.get(0).getBannerPhoto())
                        .dontAnimate()
                        .into(ivActivitiesIcon);
                tvActivitiesTitle.setText(activities.get(0).getTitle());
                String time = formatDay(activities.get(0).getHoldingType(), activities.get(0).getHoldingTimes(), activities.get(0).getStartTime())
                        + formatTime(activities.get(0).getStartTime()) + "-" + formatTime(activities.get(0).getEndTime());
                tvActivitiesTime.setText(time);
                llFirstActivitiesLay.setOnClickListener(firstActivityClick);
                break;
            case 2:
                llFirstActivitiesLay.setVisibility(View.VISIBLE);
                llFirstActivitiesLay2.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(activities.get(0).getBannerPhoto())
                        .dontAnimate()
                        .into(ivActivitiesIcon);
                tvActivitiesTitle.setText(activities.get(0).getTitle());
                String time0 = formatDay(activities.get(0).getHoldingType(), activities.get(0).getHoldingTimes(), activities.get(0).getStartTime())
                        + formatTime(activities.get(0).getStartTime()) + "-" + formatTime(activities.get(0).getEndTime());
                tvActivitiesTime.setText(time0);
                Glide.with(this)
                        .load(activities.get(1).getBannerPhoto())
                        .dontAnimate()
                        .into(ivActivitiesIcon2);
                tvActivitiesTitle2.setText(activities.get(1).getTitle());
                String time2 = formatDay(activities.get(1).getHoldingType(), activities.get(1).getHoldingTimes(), activities.get(1).getStartTime())
                        + formatTime(activities.get(1).getStartTime()) + "-" + formatTime(activities.get(1).getEndTime());
                tvActivitiesTime2.setText(time2);
                llFirstActivitiesLay.setOnClickListener(firstActivityClick);
                llFirstActivitiesLay2.setOnClickListener(secondActivityClick);
                break;
        }
    }

    private void setupUgc(List<UserDetailsV2.UgcSimpledtosBean> ugcSimpleDtos) {
        if (ugcSimpleDtos == null) return;
        switch (ugcSimpleDtos.size()) {
            case 0:
                llNews.setVisibility(View.VISIBLE);
                rlDefaultNesPhoto.setVisibility(View.VISIBLE);
                llNewsLay.setVisibility(View.VISIBLE);
                llFirstLay.setVisibility(View.GONE);
                llSecondLay.setVisibility(View.GONE);
                viewDivider1.setVisibility(View.VISIBLE);
                tvNewsCount.setText("0");
                break;
            case 1:
                if (TextUtils.isEmpty(ugcSimpleDtos.get(0).getTitlePictrues())) {
                    llFirstWithoutPhoto.setVisibility(VISIBLE);
                    llFirstLay.setVisibility(View.GONE);
                    tvNoPhoto.setText(ugcSimpleDtos.get(0).getContent());
                } else {
                    Glide.with(this)
                            .load(ugcSimpleDtos.get(0).getTitlePictrues())
                            .dontAnimate()
                            .into(ivNewsIcon);
                    tvNewsTitle.setText(ugcSimpleDtos.get(0).getContent());
                    llFirstLay.setVisibility(VISIBLE);
                    llSecondLay.setVisibility(View.GONE);
                }
                if (isOtherPeople) {
                    rlDefaultNesPhoto.setVisibility(View.GONE);
                } else if (isSelf) {
                    rlDefaultNesPhoto.setVisibility(View.VISIBLE);
                    ugcSimpleDtos.add(0, ugcSimpleDtos.get(0));
                }
                break;
            case 2:
                if (isSelf) {
                    ugcSimpleDtos.remove(1);
                    rlDefaultNesPhoto.setVisibility(View.VISIBLE);
                    ugcSimpleDtos.add(0, ugcSimpleDtos.get(0));
                }
                if (TextUtils.isEmpty(ugcSimpleDtos.get(0).getTitlePictrues())) {
                    llFirstLay.setVisibility(View.GONE);
                    llFirstWithoutPhoto.setVisibility(VISIBLE);
                    tvNoPhoto.setText(ugcSimpleDtos.get(0).getContent());
                } else {
                    Glide.with(this)
                            .load(ugcSimpleDtos.get(0).getTitlePictrues())
                            .dontAnimate()
                            .into(ivNewsIcon);
                    llFirstLay.setVisibility(View.VISIBLE);
                    llFirstWithoutPhoto.setVisibility(View.GONE);
                    tvNewsTitle.setText(ugcSimpleDtos.get(0).getContent());
                }
                if (isOtherPeople) {
                    if (TextUtils.isEmpty(ugcSimpleDtos.get(1).getTitlePictrues())) {
                        llSecondLay.setVisibility(View.GONE);
                        llSecondWithoutPhoto.setVisibility(VISIBLE);
                        tvNoPhoto2.setText(ugcSimpleDtos.get(1).getContent());
                    } else {
                        Glide.with(this)
                                .load(ugcSimpleDtos.get(1).getTitlePictrues())
                                .dontAnimate()
                                .into(ivNewsIcon2);
                        llSecondWithoutPhoto.setVisibility(View.GONE);
                        tvNewsTitle2.setText(ugcSimpleDtos.get(1).getContent());
                        llSecondLay.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onUploadImageResponse(RespDto responce) {
        Boolean data = (Boolean) responce.getData();
        tvPhotosCount.setText(upLoadCount + (TextUtils.isEmpty(tvPhotosCount.getText().toString())
                ? 0 : Integer.parseInt(tvPhotosCount.getText().toString())) + "");
        upLoadCount = 0;
        upLoadUrls.clear();
        if (data != null && data) {
            Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLikeUserResponse(RespDto responce) {
        canLikeClick = true;
        if (responce == null) return;
        LikeUser data = (LikeUser) responce.getData();
        isLiked = data.getState();
        if (isLiked) {
            ivCool.setImageResource(R.drawable.like_highlight_btn);
        } else
            ivCool.setImageResource(R.drawable.like);
        AppUtils.pushLeftActivity(this, UserDetailV2Activity.class);
    }

    @Override
    public void onLoadStateResponse(RespDto responce) {
        canLikeClick = true;
        if (responce != null && responce.isSuccess() && (Boolean) responce.getData()) {
            ivCool.setImageResource(R.drawable.like_highlight_btn);
            isLiked = true;
        } else {
            ivCool.setImageResource(R.drawable.like);
            isLiked = false;
        }
    }

    @Override
    public void onAttentionChannelResponse(RespDto responce) {
        followClickable = true;
        if (!userDetails.getHasFollow()) {
            userDetails.setHasFollow(true);
            tvFollow.setText("关注中");
        }
    }

    @Override
    public void onUserActiveListResponse(RespDto body) {
        if (body == null || body.getData() == null) return;
        ArrayList<MainChannelItem> result = (ArrayList<MainChannelItem>) body.getData();
        ArrayList<UserDetailsV2.UgcSimpledtosBean> datas = new ArrayList<>();
        int size = result.size();
        for (int i = 0; i < size; i++) {
            if (i < 2) {
                UserDetailsV2.UgcSimpledtosBean bean = new UserDetailsV2.UgcSimpledtosBean();
                bean.setContent(result.get(i).getPrefeerContent());
                bean.setMediaType(result.get(i).getMediaType());
                String prefeerPictrues = result.get(i).getPrefeerPictrues();
                if (prefeerPictrues != null) {
                    String[] split = prefeerPictrues.split(",");
                    if (split != null && split.length > 0) {
                        bean.setTitlePictrues(split[0]);
                    }
                }
                bean.setMediaUrl(result.get(i).getMediaUrl());
                datas.add(bean);
            } else
                break;
        }
        tvNewsCount.setText(size + "");
        setupUgc(datas);
    }

    @Override
    public void onUserActiveDetailsResponse(RespDto body) {
        UserDetails userDetails = (UserDetails) body.getData();
        int ugcCount = userDetails.getUgcCount();
        tvNewsCount.setText(ugcCount + "");
        List<UserDetailsV2.UgcSimpledtosBean> ugcSimpleDtos = userDetails.getUgcSimpleDtos();
        if (ugcSimpleDtos != null)
            setupUgc(ugcSimpleDtos);
        replaceUsers(userDetails);
        Glide.with(this)
                .load(userDetails.getThumb())
                .dontAnimate()
                .into(civUserThumb);
        tvUserName.setText(userDetails.getNickName());
        if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("0")) {
            ivSex.setImageResource(R.drawable.femenine);
        } else if (!TextUtils.isEmpty(userDetails.getSex()) && userDetails.getSex().equals("1")) {
            ivSex.setImageResource(R.drawable.masculine);
        } else {
            ivSex.setVisibility(View.GONE);
        }
        userPersonalMark.setText(userDetails.getPersonalMark());
        if (userDetails.getHasFollow()) {
            tvFollow.setText("关注中");
        } else {
            tvFollow.setText("关注");
        }
        if (TextUtils.isEmpty(userPersonalMark.getText().toString() + tvCity.getText().toString())) {
            hintUserInfo();
        }
        setPhotosLay(0, new ArrayList<UserDetailsV2.ProfileAlbumBean.AlbumsBean>());
        setupActivities(new ArrayList<UserDetailsV2.ProfileAcitivityBean.RespListBean>());
    }

    private void replaceUsers(UserDetails user) {
        userDetails = new UserDetailsV2();
        userDetails.setNickName(user.getNickName());
        userDetails.setThumb(user.getThumb());
        userDetails.setHasFollow(user.getHasFollow());
    }

    @Override
    public void onReplyInviteResponse(RespDto body) {
        //Todo 拼座邀请成功
        if (body != null) {
            Boolean data = (Boolean) body.getData();
            if (data != null && data) {
                Toast.makeText(UserDetailV2Activity.this, "已发送邀请成功", Toast.LENGTH_SHORT).show();
                //Todo 进入会话框
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserDetailV2Activity.this, ChatActivity.class);
                intent.putExtra("userId", toUid + "");
                intent.putExtra("toChatUserID", toUid);
                intent.putExtra("toChatNickName", userDetails.getNickName());
                intent.putExtra("toChatHeaderUrl", userDetails.getThumb());
                intent.putExtra("invite", invite);
                intent.putExtra("ISFIGHTSEAT", 1);
                ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
                EMMessage message = EMMessage.createTxtSendMessage(com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_REPLY, toUid + "");
                sendMessage(message);
            } else
                Toast.makeText(UserDetailV2Activity.this, body.getErrorDesc(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPostSignInterResponse(RespDto body) {
        //Todo 发送我感兴趣的拼座
        Boolean data = (Boolean) body.getData();
        if (body == null || data == null) {
            Toast.makeText(this, body.getErrorDesc(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (body != null && data != null && data) {
            Toast.makeText(this, body.getErrorDesc(), Toast.LENGTH_SHORT).show();
        }
        userThumb = userDetails.getThumb();
        nickName = userDetails.getNickName();

        //Todo 进入会话框
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                UserDetailV2Activity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(UserDetailV2Activity.this, ChatActivity.class);
        intent.putExtra("userId", toUid + "");
        intent.putExtra("toChatUserID", toUid);
        intent.putExtra("toChatNickName", nickName);
        intent.putExtra("toChatHeaderUrl", userThumb);
        intent.putExtra("inter", inter);
        intent.putExtra("ISFIGHTSEAT", 1);
        ActivityCompat.startActivity(UserDetailV2Activity.this, intent, compat.toBundle());
        EMMessage message = EMMessage.createTxtSendMessage(com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_INTEREST, toUid + "");
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        //Todo Type类型
        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        message.setAttribute("nickName", MoreUser.getInstance().getNickname());
        message.setAttribute("headerUrl", MoreUser.getInstance().getThumb());
        message.setAttribute(toUid + "", userDetails.getNickName() + "," + userDetails.getThumb());
        message.setAttribute("fromUID", "" + MoreUser.getInstance().getUid());
        message.setAttribute("invite", invite);
        message.setAttribute(ISSYSTEMMESSAGE, 0);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("", "");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("", "");
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("", "");
            }
        });
    }

    @Override
    public void onMerchantActivityResponse(RespDto body) {
        if (body == null || body.getData() == null) return;
        final List<MerchantActivity> activities = (List<MerchantActivity>) body.getData();
//        setupActivities(activities);
    }

    @Override
    public void onFollowDelResponse(RespDto response) {
        followClickable = true;
        if (userDetails.getHasFollow()) {
            userDetails.setHasFollow(false);
            tvFollow.setText("关注");
            Toast.makeText(this, "取消成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFollowAddResponse(RespDto response) {
        followClickable = true;
        if (!userDetails.getHasFollow()) {
            userDetails.setHasFollow(true);
            tvFollow.setText("关注中");
            Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserDetailsFailure(String msg) {
        followClickable = true;
    }

    @Override
    public void onFollowAddFailure(String msg) {
        followClickable = true;
    }

    @Override
    public void onFollowDelFailure(String msg) {

    }

    @Override
    public void onMerchantActivityFailure(String msg) {

    }

    @Override
    public void onReplyInviteFailure(String msg) {
        //Todo 拼座邀请失败
        Log.i("zune:", "msg = " + msg);
        Toast.makeText(UserDetailV2Activity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserActiveDetailsFailure(String msg) {

    }

    @Override
    public void onUserActiveListFailure(String msg) {

    }

    @Override
    public void onAttentionChannelFailure(String msg) {

    }

    @Override
    public void onUploadImageFailure(String msg) {

    }

    @Override
    public void onLikeUserFailure(String msg) {
        canLikeClick = true;
    }

    @Override
    public void onLoadStateFailure(String msg) {
        canLikeClick = true;
    }

    @Override
    public void onPostSignInterFailure(String msg) {

    }

    private List<String> getTags() {
        List<String> strings = new ArrayList<>();
        if (!TextUtils.isEmpty(userDetails.getWinePrefer())) {
            String[] split = userDetails.getWinePrefer().split(" ");
            for (int i = 0; i < split.length; i++) {
                strings.add(split[i]);
            }
        }
        if (!TextUtils.isEmpty(userDetails.getBookPrefer())) {
            String[] split = userDetails.getBookPrefer().split(" ");
            for (int i = 0; i < split.length; i++) {
                strings.add(split[i]);
            }
        }
        if (!TextUtils.isEmpty(userDetails.getBrandPrefer())) {
            String[] split = userDetails.getBrandPrefer().split(" ");
            for (int i = 0; i < split.length; i++) {
                strings.add(split[i]);
            }
        }
        if (!TextUtils.isEmpty(userDetails.getHobby())) {
            String[] split = userDetails.getHobby().split(" ");
            for (int i = 0; i < split.length; i++) {
                strings.add(split[i]);
            }
        }
        if (!TextUtils.isEmpty(userDetails.getFoodPrefer())) {
            String[] split = userDetails.getFoodPrefer().split(" ");
            for (int i = 0; i < split.length; i++) {
                strings.add(split[i]);
            }
        }
        List randomList = getRandomList(strings);
        return randomList;
    }

    private void setBirthdayStar() {
        List<String> strings = new ArrayList<>();
        if (!TextUtils.isEmpty(userDetails.getCity())) {
            strings.add(userDetails.getCity());
        }
        if (!TextUtils.isEmpty(userDetails.getCareerName())) {
            strings.add(userDetails.getCareerName());
        }
        if (!TextUtils.isEmpty(userDetails.getBirthday())) {
            String[] birthdays = userDetails.getBirthday().split("-");
            if (birthdays != null && birthdays.length == 3) {
                String s = birthdays[0] + "年" + birthdays[1] + "月" + birthdays[2] + "日";
                String ageDatas = getAgeDatas(s);
                strings.add(ageDatas);
            }
        }
        switch (strings.size()) {
            case 1:
                tvCity.setText(strings.get(0));
                break;
            case 2:
                tvCity.setText(strings.get(0) + "  ·  " + strings.get(1));
                break;
            case 3:
                tvCity.setText(strings.get(0) + "  ·  " + strings.get(1) + "  ·  " + strings.get(2));
                break;
            default:
                tvCity.setVisibility(View.GONE);
                break;
        }
    }

    private String getAgeDatas(String birthdayTime) {
        String starSeat;
        try {
            String year = birthdayTime.split("年")[0].toString();
            String month = birthdayTime.split("年")[1].toString().split("月")[0];
            String day = birthdayTime.split("年")[1].toString().split("月")[1].toString().split("日")[0];
            starSeat = TimeUtils.getStarSeat(Integer.parseInt(month), Integer.parseInt(day));
        } catch (Exception e) {
            Logger.i("zune:", "Integer parse exception = " + e);
            starSeat = "";
        }
        return starSeat;
    }

    public static List getRandomList(List sourceList) {
        if (sourceList == null || sourceList.size() == 0) {
            return sourceList;
        }
        List randomList = new ArrayList(sourceList.size());
        do {
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
        } while (sourceList.size() > 0);
        return randomList;
    }

    private int randomColor() {
        int[] colors = {
                Color.parseColor("#f2ad3c"),
                Color.parseColor("#ad6de6"),
                Color.parseColor("#2999f1"),
                Color.parseColor("#84be4a"),
                Color.parseColor("#de6e6e"),
        };
        return colors[new Random().nextInt(5)];
    }

    private TagAdapter setTagAdapter(final TagFlowLayout layout, final List<String> userTags) {
        TagAdapter adapter = new TagAdapter<String>(userTags) {
            @Override
            public View getView(FlowLayout parent, int position, String userTag) {
                TextView tv = (TextView) getLayoutInflater().inflate(
                        R.layout.user_detail_tag, layout, false);

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
                if (getTextWidth(tv, userTag) > ScreenUtil.getScreenWidth(UserDetailV2Activity.this)
                        - ScreenUtil.dp2px(UserDetailV2Activity.this, 32) - 12) {
                    layoutParams.width = ScreenUtil.getScreenWidth(UserDetailV2Activity.this)
                            - ScreenUtil.dp2px(UserDetailV2Activity.this, 32) - 12;
                } else {
                    layoutParams.width = WRAP_CONTENT;
                }
                layoutParams.height = WRAP_CONTENT;
                layoutParams.setMargins(6, 6, 0, 6);
                int padding = ScreenUtil.dp2px(UserDetailV2Activity.this, 6);
                tv.setPadding(padding, padding, padding, padding);
                String name = userTags.get(position);
                tv.setText(name);
                int color = randomColor();
                tv.setTextColor(color);
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(ScreenUtil.dp2px(UserDetailV2Activity.this, 2));
                shape.setStroke(ScreenUtil.dp2px(UserDetailV2Activity.this, 1), color);
                tv.setBackgroundDrawable(shape);
                return tv;
            }
        };
        layout.setAdapter(adapter);
        return adapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(UserDetailV2Activity.this, permissions[0]) != PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UserDetailV2Activity.this, permissions[1]) != PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            ActivityCompat.requestPermissions(UserDetailV2Activity.this, permissions, REQUEST_CODE_READ);
        } else {
            //Todo 打开相册
            PhotoPicker.builder()
                    .setPhotoCount(9)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(this, PhotoPicker.REQUEST_CODE);
        }
    }

    private void refreshAdpater(ArrayList<String> paths) {
        // 处理返回照片地址
        selectCount = paths.size();
        for (int i = 0; i < paths.size(); i++) {
            imagePaths.add(i, paths.get(i));
        }
        if (imagePaths.size() > 0) {
            if (isOtherPeople) {
                rvPhotos.setVisibility(View.VISIBLE);
                rlDefaultPhoto.setVisibility(View.GONE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
                layoutParams.addRule(BELOW, R.id.rv_photos);
                viewDivider0.setLayoutParams(layoutParams);
            } else if (isSelf) {
                rvPhotos.setVisibility(View.VISIBLE);
                rlDefaultPhoto.setVisibility(View.VISIBLE);
                imagePaths.add(0, imagePaths.get(0));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
                layoutParams.addRule(BELOW, R.id.rv_photos);
                viewDivider0.setLayoutParams(layoutParams);
            }
        }
        if (imagePaths.size() > 8) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                temp.add(imagePaths.get(i));
            }
            imagePaths.clear();
            imagePaths.addAll(temp);
            temp.clear();
            adapter.notifyDataSetChanged();
        } else
            adapter.notifyDataSetChanged();
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
                        ((UserDetailsV2Loader) mPresenter).onUploadImages(MoreUser.getInstance().getAccess_token()
                                , images);
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
            new AliUploadUtils(UserDetailV2Activity.this,
                    Constants.ALI_OSS_BUCKETNAME, getImageObjectKey("" + MoreUser.getInstance().getUid()), paths.get(i),
                    MoreUser.getInstance().getUid() + "",
                    uploadResultListener);
        }
    }

    private String formatDay(int holdingType, String holdingTimes, Long time) {
        if (holdingType == 2) {
            String[] split = holdingTimes.split(",");
            StringBuffer sb = new StringBuffer("每");
            for (int i = 0; i < split.length; i++) {
                if (i < split.length - 1)
                    sb.append("周" + split[i] + ",");
                else
                    sb.append("周" + split[i]);
            }
            return sb.toString();
        } else if (holdingType == 1) {
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日 EE ");
            Date date = new Date(time);
            String day = format.format(date);
            return day;
        }
        return "";
    }

    private String formatTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        String minute = format.format(date);
        return minute;
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
                    //Todo 打开相册
                    PhotoPicker.builder()
                            .setPhotoCount(9)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(true)
                            .start(this, PhotoPicker.REQUEST_CODE);
                } else {
                    //用户拒绝授权
                    try {
                        //Todo 打开相册
                        PhotoPicker.builder()
                                .setPhotoCount(9)
                                .setShowCamera(true)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(this, PhotoPicker.REQUEST_CODE);
                    } catch (Exception e) {
                        Log.i("zune:", "e = " + e);
                        Toast.makeText(UserDetailV2Activity.this, "使用外置存储卡,请手动设置", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Todo 打开相册
                    PhotoPicker.builder()
                            .setPhotoCount(9)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(true)
                            .start(this, PhotoPicker.REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (reviewPicLayout.getVisibility() == View.VISIBLE) {
            reviewPicLayout.setVisibility(View.GONE);
        } else {
            overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        followClickable = true;
        canLikeClick = true;
        imageBigUrls.clear();
        imagePaths.clear();
        upLoadUrls.clear();
        selectPath.clear();
        likes.clear();
        llGoods.removeAllViews();
        loadData();
    }

    @Override
    protected void onDestroy() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        super.onDestroy();
        DataCleanManager.cleanCustomCache(SAVE_REAL_PATH_TEMP);
    }

    public float getTextWidth(TextView view, String text) {
        TextPaint paint = view.getPaint();
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * 12);
        return paint.measureText(text);
    }
}

