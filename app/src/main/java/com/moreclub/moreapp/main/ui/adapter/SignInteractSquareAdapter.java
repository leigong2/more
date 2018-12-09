package com.moreclub.moreapp.main.ui.adapter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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
import android.view.Window;
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
import com.moreclub.common.ui.view.scrollview.ObservableScrollViewCallbacks;
import com.moreclub.common.ui.view.scrollview.ScrollState;
import com.moreclub.common.ui.view.tag.FlowLayout;
import com.moreclub.common.ui.view.tag.TagAdapter;
import com.moreclub.common.ui.view.tag.TagFlowLayout;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.information.model.SignInterResult;
import com.moreclub.moreapp.main.model.SignSpaceList;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.PublishMChannelActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterDetailActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterHotalActivity;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.LikeUser;
import com.moreclub.moreapp.ucenter.model.UploadUserImage;
import com.moreclub.moreapp.ucenter.model.UserDetailsV2;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;
import com.moreclub.moreapp.ucenter.ui.activity.UserUgcListActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsReportActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserGoodsActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserImagesActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserModifyDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserUgcDetailActivity;
import com.moreclub.moreapp.ucenter.ui.adapter.RecyclerPhotoAdapter;
import com.moreclub.moreapp.ucenter.ui.view.ArcImageView;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AliUploadUtils;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.DataCleanManager;
import com.moreclub.moreapp.util.FileSaveUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.PutObjectSamples;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static com.hyphenate.easeui.EaseConstant.ISSYSTEMMESSAGE;
import static com.moreclub.moreapp.main.constant.Constants.SIGNINTERACTIVITY;
import static com.moreclub.moreapp.main.constant.Constants.SUPERMAINACTIVITY;
import static com.moreclub.moreapp.ucenter.constant.Constants.ISFIRST_SHOW_LEFT_ONESHOT;
import static com.moreclub.moreapp.ucenter.constant.Constants.ISFIRST_SHOW_UP_ONESHOT;
import static com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity.getRandomList;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH_TEMP;
import static com.moreclub.moreapp.util.RandomUtil.generateString;

/**
 * Created by wangkegang on 2016/07/06 .
 */
public class SignInteractSquareAdapter extends RecyclerView.Adapter<SignInteractSquareAdapter.ViewHolder> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final int REQUEST_CODE_READ = 1003;
    /**
     * 展示数据
     */
    private ArrayList<SignSpaceList.SininMoreSpacesBean> arrayList;
    private String mid;
    private int sid = -1;
    private List<SignSpaceList.SininMoreSpacesBean.SignInteractionDtoBean> newSignInterses;

    private int[] resTypeSelectorIds = {R.drawable.pinzuo_bg_pgz, R.drawable.pinzuo_bg_llt, R.drawable.pinzuo_bg_wyx
            , R.drawable.pinzuo_bg_qhd, R.drawable.pinzuo_bg_bgm, R.drawable.pinzuo_bg_bsh};

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private String invite = com.moreclub.moreapp.main.constant.Constants.TITLE_ANSWER;
//    拼个座 聊聊天 玩游戏 求回答 帮个忙 别说话
//type 5      3      6     4      1     2

    private int[] resIds = {R.drawable.buyuadrink_white, R.drawable.cocktail_white, R.drawable.beerbrew_white
            , R.drawable.wine_white, R.drawable.whiskey_white, R.drawable.importbeer_white,
            R.drawable.champagne_white, R.drawable.brandy_white, R.drawable.saki_white};
    private Context mContext;
    private int mySid;
    public PopupWindow mUserPopupWindow;
    private View popupParentView;
    private boolean isSelf;
    private boolean isOtherPeople;
    private View view_weight;
    private ImageButton nav_back;
    private ImageButton navRightBtn;
    private TextView tvDefaultNewsCamera;
    private TextView tvDefaultCamera;
    private RelativeLayout llBottomLayout;
    private LinearLayout llUpshowOneshot;
    private LinearLayout llRightshowOneshot;
    private TextView invite_someone;
    private ObservableScrollView detailsScrollView;
    public boolean hasGone;
    private boolean canScroll;
    private PopupWindow bubblePopWindow;
    private LinearLayout llNews;
    private LinearLayout llNewsLay;
    private TextView tvNewsCount;
    private RelativeLayout rlDefaultNesPhoto;
    private LinearLayout llFirstLay;
    private LinearLayout llSecondLay;
    private View viewDivider1;
    private TextView tvGoodsCount;
    private TextView tvFollow;
    private boolean isBar;
    private LinearLayout llActivitiesLay;
    private RelativeLayout llFirstActivitiesLay;
    private RelativeLayout llFirstActivitiesLay2;
    private View viewDivider;
    private TextView tvUserDetail;
    private TagFlowLayout flTags;
    private CircleImageView civUserThumb;
    private ArcImageView aivTop;
    private TextView tvUserName;
    private TextView tvYearsOld;
    private ImageView ivSex;
    private LinearLayout llSex;
    private TextView userPersonalMark;
    private TextView tvCity;
    private TextView tvNoContent;
    private int screenWidth;
    private int screenHeight;
    private View viewDivider2;
    private LinearLayout llGoodsLay;
    private TextView tvPhotosCount;
    private RelativeLayout rlDefaultPhoto;
    private View viewDivider0;
    private RecyclerView rvPhotos;
    private ArrayList<String> imagePaths;
    private ArrayList<String> imageBigUrls;
    private ArrayList<String> upLoadUrls;
    private ArrayList<String> selectPath;
    private ArrayList<String> tags;
    private ArrayList<UserDetailsV2.ProfileLikeBean.LikesBean> likes;
    private RecyclerPhotoAdapter adapter;
    private ImageView ivDefaultGood;
    private View viewDivider3;
    private LinearLayout llGoods;
    private LinearLayout llFirstWithoutPhoto;
    private TextView tvNoPhoto;
    private ImageView ivNewsIcon;
    private TextView tvNewsTitle;
    private LinearLayout llSecondWithoutPhoto;
    private TextView tvNoPhoto2;
    private TextView tvNewsTitle2;
    private ImageView ivNewsIcon2;
    private View popupBigImageView;
    public PopupWindow mBigPopupWindow;
    private ViewPagerFixed sceneViewPager;
    private boolean canLikeClick;
    private boolean isLiked;
    private ImageView ivCool;
    private View navLay;
    private TextView interest;
    private TextView tvIKnow;
    private FrameLayout reviewPicLayout;
    private EasePhotoView reviewPic;
    private static UserDetailsV2 userDetails;
    private LinearLayout llPhotoLay;
    private int selectCount;
    private boolean followClickable = true;
    private TextView tvChat;
    private RelativeLayout rlCoor;
    private int chatType;
    private int upLoadCount;
    private static Long toUid;
    private Window window;

    public SignInteractSquareAdapter(Context context, ArrayList<SignSpaceList.SininMoreSpacesBean> dataList
            , List<SignSpaceList.SininMoreSpacesBean.SignInteractionDtoBean> signInterses, String mid) {
        this.arrayList = dataList;
        this.newSignInterses = signInterses;
        this.mContext = context;
        this.mid = mid;
        if (UpdateUser.getInstance().getAdapters() == null ||
                UpdateUser.getInstance().getAdapters().size() == 0) {
            Map<String, SignInteractSquareAdapter> adapters = new HashMap<>();
            adapters.put("0", this);
            UpdateUser.getInstance().setAdapters(adapters);
        }
        if (mContext instanceof AppCompatActivity) {
            window = ((AppCompatActivity) mContext).getWindow();
        } else if (mContext instanceof Activity) {
            window = ((Activity) mContext).getWindow();
        }
    }

    public void updateData(ArrayList<SignSpaceList.SininMoreSpacesBean> data) {
        this.arrayList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 瀑布流外部设置spanCount为2，在这列设置两个不同的item type,以区分不同的布局
        if (position == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public SignInteractSquareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sign_interact_square_item_white_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sign_interact_square_item, parent, false);
        }

        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SignInteractSquareAdapter.ViewHolder holder, final int position) {
        // 绑定数据

        if (position == 0) {
            if (newSignInterses != null && newSignInterses.size() > 0)
                for (int i = newSignInterses.size() - 1; i >= 0; i--) {
                    if (newSignInterses.get(i) != null && newSignInterses.get(i).getUid() != null
                            && newSignInterses.get(i).getUid().equals(MoreUser.getInstance().getUid())
                            && newSignInterses.get(i).getExprise() == 2) {
                        this.mySid = newSignInterses.get(i).getSid();
                    }
                }
        }

        if (position == 1) {
            return;
        }


        final SignSpaceList.SininMoreSpacesBean item = arrayList.get(position);
        if (TextUtils.isEmpty(mid) && !TextUtils.isEmpty(item.getMerchantName())) {
            holder.addrName.setVisibility(View.VISIBLE);
            holder.addrLay.setVisibility(View.VISIBLE);
            holder.addrName.setText(item.getMerchantName());
            holder.addrLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.getMid();
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", item.getMid() + "");
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            });
        } else {
            holder.addrName.setVisibility(View.GONE);
            holder.addrLay.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.getUserAvatar()))
            Glide.with(mContext)
                    .load(item.getUserAvatar())
                    .dontAnimate()
                    .placeholder(R.drawable.profilephoto)
                    .into(holder.mIcon);
        else
            holder.mIcon.setImageResource(R.drawable.profilephoto);

        holder.mName.setText(item.getUserName());
        holder.signInterLayout.setVisibility(View.GONE);
        for (int i = 0; i < newSignInterses.size(); i++) {
            if (newSignInterses.get(i) != null && TextUtils.equals(item.getUid() + "", newSignInterses.get(i).getUid() + "")) {
                final int finalI = i;
                int exprise = newSignInterses.get(finalI).getExprise();
                switch (exprise) {
                    case 1:
                        //已结束
                    case 3:
                        //过期
                        break;
                    case 2:
                        //在线
                        holder.signInterLayout.setVisibility(View.VISIBLE);
                        holder.addrName.setVisibility(VISIBLE);
                        holder.signInterLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Todo 进入签到互动详情
                                if (MoreUser.getInstance().getUid() == 0) {
                                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                                } else {
                                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                            mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                                    Intent intent = new Intent(mContext, SignInterDetailActivity.class);
                                    if (mContext instanceof SignInterHotalActivity) {
                                        intent.putExtra("isFromMSpace", true);
                                        intent.putExtra("tag", SIGNINTERACTIVITY);
                                    } else if (mContext instanceof SuperMainActivity) {
                                        intent.putExtra("tag", SUPERMAINACTIVITY);
                                    }
                                    intent.putExtra("mid", item.getMid());
                                    intent.putExtra("sid", newSignInterses.get(finalI).getSid());
                                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                                }
                            }
                        });
                        holder.contentInter.setText(newSignInterses.get(i).getContent());
                        int gift = newSignInterses.get(i).getGift();
                        int type = newSignInterses.get(i).getType();
                        switch (type) {
                            case 5:
                                holder.baseImageType.setImageResource(resTypeSelectorIds[0]);
                                break;
                            case 3:
                                holder.baseImageType.setImageResource(resTypeSelectorIds[1]);
                                break;
                            case 6:
                                holder.baseImageType.setImageResource(resTypeSelectorIds[2]);
                                break;
                            case 4:
                                holder.baseImageType.setImageResource(resTypeSelectorIds[3]);
                                break;
                            case 1:
                                holder.baseImageType.setImageResource(resTypeSelectorIds[4]);
                                break;
                            case 2:
                                holder.baseImageType.setImageResource(resTypeSelectorIds[5]);
                                break;
                        }
                        if (gift > 0)
                            holder.baseImageDrink.setImageResource(resIds[gift - 1]);
                        else
                            holder.baseImageDrink.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }
        if (item.getGender() != null && item.getGender().equals("0")) {
            holder.mSexImg.setVisibility(View.VISIBLE);
            holder.mSexImg.setImageResource(R.drawable.femenine);
        } else if (item.getGender() != null && item.getGender().equals("1")) {
            holder.mSexImg.setVisibility(View.VISIBLE);
            holder.mSexImg.setImageResource(R.drawable.masculine);
        } else {
            holder.mSexImg.setVisibility(View.GONE);
        }

        try {
            if (item.getBirthday() == null) {
                holder.mAge.setText("保密");
            } else {
                int ageint = TimeUtils.getAge(item.getBirthday());
                if (ageint == 0) {
                    holder.mAge.setText("保密");
                } else {
                    holder.mAge.setText("" + ageint);
                }
            }
        } catch (Exception e) {
            holder.mAge.setText("保密");
            e.printStackTrace();
        }
        long now = Long.valueOf(TimeUtils.getTimestampSecond());
        long time = now - (item.getSigninTime() / 1000);
        SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日");
        java.sql.Date date = new java.sql.Date(item.getSigninTime());
        String format = sdr.format(date);
        if (time < (10 * 60)) {
            holder.mTime.setText("刚刚");
        } else if (time > (10 * 60) && time < (60 * 60)) {
            holder.mTime.setText((time / (60)) + "分钟");
        } else if (time > (60 * 60) && time < (24 * 60 * 60)) {
            holder.mTime.setText((time / (60 * 60)) + "小时");
        } else if (time > (24 * 60 * 60) && time < (3 * 24 * 60 * 60)) {
            holder.mTime.setText((time / (24 * 60 * 60)) + "天前");
        } else {
            holder.mTime.setText(format);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() != null
                        && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                } else {
                    toUid = item.getUid();
                    loadPopDatas(toUid);
                }
//                startUserDetail(item);
            }
        });
    }

    public void loadPopDatas(final Long uid) {
        Long suid = MoreUser.getInstance().getUid();
        Callback userDetail = new Callback<RespDto<UserDetailsV2>>() {
            @Override
            public void onResponse(Call<RespDto<UserDetailsV2>> call,
                                   Response<RespDto<UserDetailsV2>> response) {
                if (response != null && response.body() != null && response.body().getData() != null) {
                    userDetails = response.body().getData();
                    hasGone = false;
                    startUserPop(userDetails, uid);
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserDetailsV2>> call, Throwable t) {

            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadUserV2Details(uid, suid).enqueue(userDetail);
    }

    private void startUserPop(UserDetailsV2 data, Long uid) {
        Long suid = MoreUser.getInstance().getUid();
        screenWidth = ScreenUtil.getScreenWidth(mContext);
        screenHeight = ScreenUtil.getScreenHeight(mContext);
        if (popupParentView == null || mUserPopupWindow == null ||
                !mUserPopupWindow.isShowing())
            popupParentView = LayoutInflater.from(mContext).inflate(
                    R.layout.user_detail_v2_activity, null);
        setupPopView(uid, suid);
        setupPopData(data, uid);
        //设置弹出部分和面积大小
        if (mUserPopupWindow == null || !mUserPopupWindow.isShowing()) {
            mUserPopupWindow = new PopupWindow(popupParentView, MATCH_PARENT
                    , MATCH_PARENT, true);
            //设置动画弹出效果
            mUserPopupWindow.setFocusable(false);
            mUserPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mUserPopupWindow.setAnimationStyle(R.style.coupon_popup_animation);
            mUserPopupWindow.showAtLocation(window.getDecorView(), Gravity.BOTTOM,
                    0, 0);
            MainApp.getInstance().spacePops.add(mUserPopupWindow);
        }
        Callback<RespDto<Boolean>> followState = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> responce) {
                canLikeClick = true;
                if (responce != null && responce.body() != null
                        && responce.body().isSuccess() && responce.body().getData()) {
                    ivCool.setImageResource(R.drawable.like_highlight_btn);
                    isLiked = true;
                } else {
                    ivCool.setImageResource(R.drawable.like);
                    isLiked = false;
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadLikeState(uid, suid).enqueue(followState);
    }

    private void setupPopView(Long uid, Long suid) {
        view_weight = popupParentView.findViewById(R.id.view_weight);
        nav_back = (ImageButton) popupParentView.findViewById(R.id.nav_back);
        navRightBtn = (ImageButton) popupParentView.findViewById(R.id.nav_right_btn);
        tvDefaultNewsCamera = (TextView) popupParentView.findViewById(R.id.tv_default_news_camera);
        tvDefaultCamera = (TextView) popupParentView.findViewById(R.id.tv_default_camera);
        llBottomLayout = (RelativeLayout) popupParentView.findViewById(R.id.ll_bottom_layout);
        llUpshowOneshot = (LinearLayout) popupParentView.findViewById(R.id.ll_upshow_oneshot);
        llRightshowOneshot = (LinearLayout) popupParentView.findViewById(R.id.ll_rightshow_oneshot);
        invite_someone = (TextView) popupParentView.findViewById(R.id.invite_someone);
        detailsScrollView = (ObservableScrollView) popupParentView.findViewById(R.id.detailsScrollView);
        llNews = (LinearLayout) popupParentView.findViewById(R.id.ll_news);
        llNewsLay = (LinearLayout) popupParentView.findViewById(R.id.ll_news_lay);
        tvNewsCount = (TextView) popupParentView.findViewById(R.id.tv_news_count);
        tvGoodsCount = (TextView) popupParentView.findViewById(R.id.tv_goods_count);
        tvFollow = (TextView) popupParentView.findViewById(R.id.tv_follow);
        tvUserDetail = (TextView) popupParentView.findViewById(R.id.tv_user_detail);
        rlDefaultNesPhoto = (RelativeLayout) popupParentView.findViewById(R.id.rl_default_news_photo);
        llFirstLay = (LinearLayout) popupParentView.findViewById(R.id.ll_first_lay);
        llSecondLay = (LinearLayout) popupParentView.findViewById(R.id.ll_second_lay);
        viewDivider1 = popupParentView.findViewById(R.id.view_divider1);
        llActivitiesLay = (LinearLayout) popupParentView.findViewById(R.id.ll_activities_lay);
        llFirstActivitiesLay = (RelativeLayout) popupParentView.findViewById(R.id.ll_first_activities_lay);
        llFirstActivitiesLay2 = (RelativeLayout) popupParentView.findViewById(R.id.ll_first_activities_lay2);
        flTags = (TagFlowLayout) popupParentView.findViewById(R.id.fl_tags);
        civUserThumb = (CircleImageView) popupParentView.findViewById(R.id.civ_user_thumb);
        aivTop = (ArcImageView) popupParentView.findViewById(R.id.aiv_top);
        tvUserName = (TextView) popupParentView.findViewById(R.id.tv_user_name);
        tvYearsOld = (TextView) popupParentView.findViewById(R.id.tv_years_old);
        userPersonalMark = (TextView) popupParentView.findViewById(R.id.user_personal_mark);
        tvCity = (TextView) popupParentView.findViewById(R.id.tv_city);
        ivSex = (ImageView) popupParentView.findViewById(R.id.iv_sex);
        tvNoContent = (TextView) popupParentView.findViewById(R.id.tv_no_content);
        llSex = (LinearLayout) popupParentView.findViewById(R.id.ll_sex);
        llGoodsLay = (LinearLayout) popupParentView.findViewById(R.id.ll_goods_lay);
        viewDivider = popupParentView.findViewById(R.id.view_divider);
        viewDivider2 = popupParentView.findViewById(R.id.view_divider2);
        tvPhotosCount = (TextView) popupParentView.findViewById(R.id.tv_photos_count);
        rlDefaultPhoto = (RelativeLayout) popupParentView.findViewById(R.id.rl_default_photo);
        rvPhotos = (RecyclerView) popupParentView.findViewById(R.id.rv_photos);
        viewDivider0 = popupParentView.findViewById(R.id.view_divider0);
        ivDefaultGood = (ImageView) popupParentView.findViewById(R.id.iv_default_good);
        viewDivider3 = popupParentView.findViewById(R.id.view_divider3);
        llGoods = (LinearLayout) popupParentView.findViewById(R.id.ll_goods);
        llFirstWithoutPhoto = (LinearLayout) popupParentView.findViewById(R.id.ll_first_lay_without_photo);
        tvNoPhoto = (TextView) popupParentView.findViewById(R.id.tv_news_title_no_photo);
        ivNewsIcon = (ImageView) popupParentView.findViewById(R.id.iv_news_icon);
        tvNewsTitle = (TextView) popupParentView.findViewById(R.id.tv_news_title);
        llSecondWithoutPhoto = (LinearLayout) popupParentView.findViewById(R.id.ll_second_lay_without_photo);
        tvNoPhoto2 = (TextView) popupParentView.findViewById(R.id.tv_news_title2_no_photo);
        tvNewsTitle2 = (TextView) popupParentView.findViewById(R.id.tv_news_title2);
        ivNewsIcon2 = (ImageView) popupParentView.findViewById(R.id.iv_news_icon2);
        ivCool = (ImageView) popupParentView.findViewById(R.id.iv_cool);
        navLay = popupParentView.findViewById(R.id.nav_layout);
        tvIKnow = (TextView) popupParentView.findViewById(R.id.tv_i_know_up);
        reviewPicLayout = (FrameLayout) popupParentView.findViewById(R.id.reviewPicLayout);
        reviewPic = (EasePhotoView) popupParentView.findViewById(R.id.reviewPic);
        llPhotoLay = (LinearLayout) popupParentView.findViewById(R.id.ll_photo_lay);
        tvChat = (TextView) popupParentView.findViewById(R.id.tv_chat);
        rlCoor = (RelativeLayout) popupParentView.findViewById(R.id.rl_cool);
        ViewGroup.MarginLayoutParams ivDefaultGoodLp
                = (ViewGroup.MarginLayoutParams) ivDefaultGood.getLayoutParams();
        int margin = (int) ((screenWidth - ScreenUtil.dp2px(mContext, 24) * 9.0f - ScreenUtil.dp2px(mContext, 16)) / 18.0f);
        ivDefaultGoodLp.leftMargin = margin;
        ivDefaultGood.setLayoutParams(ivDefaultGoodLp);
        navLay.setBackgroundColor(Color.parseColor("#00000000"));
        RelativeLayout.LayoutParams navLp = (RelativeLayout.LayoutParams) navLay.getLayoutParams();
        navLp.addRule(BELOW, R.id.view_weight);
        navLay.setLayoutParams(navLp);
        nav_back.setImageResource(R.drawable.close);
        nav_back.setVisibility(View.GONE);
        if (TextUtils.equals(suid + "", uid + "")) {
            isSelf = true;
            isOtherPeople = false;
            navRightBtn.setImageResource(R.drawable.write_user_info);
        } else {
            isOtherPeople = true;
            isSelf = false;
            navRightBtn.setImageResource(R.drawable.user_details_menu);
        }
        navRightBtn.setVisibility(View.GONE);
        if (isSelf) {
            tvDefaultCamera.setText("上传照片");
            tvDefaultNewsCamera.setText("发动态");
            llBottomLayout.setVisibility(View.GONE);
        } else {
            llBottomLayout.setVisibility(VISIBLE);
            tvDefaultCamera.setText("邀请TA");
            tvDefaultNewsCamera.setText("邀请TA");
        }
        RelativeLayout.LayoutParams llBottomLayoutLP = new RelativeLayout.LayoutParams(WRAP_CONTENT
                , ScreenUtil.dp2px(mContext, 48));
        llBottomLayoutLP.setMargins(0, screenHeight - ScreenUtil.dp2px(mContext, 48 + 15 + 16), 0, 0);
        llBottomLayoutLP.addRule(CENTER_HORIZONTAL);
        llBottomLayout.setLayoutParams(llBottomLayoutLP);
        if (mySid > 0 && TextUtils.isEmpty(mid)) {
            invite_someone.setVisibility(View.VISIBLE);
        } else {
            invite_someone.setVisibility(View.GONE);
        }

        imagePaths = new ArrayList<>();
        imageBigUrls = new ArrayList<>();
        upLoadUrls = new ArrayList<>();
        selectPath = new ArrayList<>();
        tags = new ArrayList<>();
        likes = new ArrayList<>();
        adapter = new RecyclerPhotoAdapter(mContext
                , R.layout.simple_imageview, imagePaths);
        RecyclerView.LayoutManager lm = new GridLayoutManager(mContext, 4
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
        if (!hasGone) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    screenWidth, (int) (screenHeight * 0.35f));
            view_weight.setLayoutParams(lp);
        }
        if (PrefsUtils.getBoolean(mContext, ISFIRST_SHOW_UP_ONESHOT, true)) {
            llUpshowOneshot.setVisibility(View.VISIBLE);
        }
        if (PrefsUtils.getBoolean(mContext, ISFIRST_SHOW_LEFT_ONESHOT, false)) {
            llRightshowOneshot.setVisibility(View.GONE);
        }
        setupScrollView(uid);
        RelativeLayout.LayoutParams rlDefaultPhotoLP = (RelativeLayout.LayoutParams) rlDefaultPhoto.getLayoutParams();
        rlDefaultPhotoLP.width = (screenWidth - ScreenUtil.dp2px(mContext, 32)) / 4;
        rlDefaultPhotoLP.height = (screenWidth - ScreenUtil.dp2px(mContext, 32)) / 4;
        rlDefaultPhotoLP.setMargins(ScreenUtil.dp2px(mContext, 16), 2, 2, 2);
        rlDefaultPhoto.setLayoutParams(rlDefaultPhotoLP);

//        ViewGroup.MarginLayoutParams rlDefaultNewsPhotoLP = (ViewGroup.MarginLayoutParams) rlDefaultPhoto.getLayoutParams();
//        rlDefaultNewsPhotoLP.width = (screenWidth - ScreenUtil.dp2px(mContext, 32)) / 4;
//        rlDefaultNewsPhotoLP.height = (screenWidth - ScreenUtil.dp2px(mContext, 32)) / 4;
//        rlDefaultNewsPhotoLP.setMargins(ScreenUtil.dp2px(mContext, 16), 2, 2, 2);
//        rlDefaultNesPhoto.setLayoutParams(rlDefaultNewsPhotoLP);

        if (rlDefaultPhoto.getVisibility() == View.VISIBLE) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
            layoutParams.addRule(BELOW, R.id.rl_default_photo);
            viewDivider0.setLayoutParams(layoutParams);
        }
        initClick(uid);
    }

    private void initClick(final Long uid) {
        View.OnClickListener aivTopClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelf)
                    AppUtils.pushLeftActivity(mContext, UserModifyDetailActivity.class);
            }
        };
        View.OnClickListener backClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApp.getInstance().spacePops.size() > 0)
                    MainApp.getInstance().spacePops.remove(mUserPopupWindow);
                mUserPopupWindow.dismiss();
                DataCleanManager.cleanCustomCache(SAVE_REAL_PATH_TEMP);
                mUserPopupWindow = null;
                hasGone = false;
                followClickable = true;
                canLikeClick = true;
                llGoods.removeAllViews();
                imageBigUrls.clear();
                imagePaths.clear();
                Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
                if (adapters != null && adapters.size() > 0) {
                    adapters.clear();
                }
            }
        };
        View.OnClickListener inviteClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 邀请他拼座互动
                SignInterResult params = new SignInterResult();
                params.setFromUid(MoreUser.getInstance().getUid());
                params.setUid(uid);
                params.setTitle(com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_INTEREST);
                params.setSid(mySid);
                replyInvite(params, uid + "");
            }
        };
        View.OnClickListener rlDefaultNesPhotoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelf) {
                    //Todo 发起动态
                    ActivityOptionsCompat compat5 = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.push_down_in, R.anim.push_down_out);
                    Intent intent5 = new Intent(mContext, PublishMChannelActivity.class);
                    intent5.putExtra("from", "UserDetailV2Activity");
                    ActivityCompat.startActivity(mContext, intent5, compat5.toBundle());
                } else {
                    String str = "btn_invite_add_picture";
                    Map<String, String> map = new HashMap<>();
                    map.put("muid", uid + "");
                    map.put("suid", MoreUser.getInstance().getUid() + "");
                    if (Build.VERSION.SDK_INT > 22) {
                        int du = 2000000000;
                        MobclickAgent.onEventValue(mContext, str, map, du);
                    } else {
                        MobclickAgent.onEvent(mContext, str, map);
                    }
                    Toast.makeText(mContext, "您的邀请已发送", Toast.LENGTH_SHORT).show();
                }
            }
        };
        View.OnClickListener tvIKnowClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsUtils.setBoolean(mContext, ISFIRST_SHOW_UP_ONESHOT, false);
                PrefsUtils.setBoolean(mContext, ISFIRST_SHOW_LEFT_ONESHOT, false);
                llUpshowOneshot.setVisibility(View.GONE);
                llRightshowOneshot.setVisibility(View.GONE);
            }
        };
        View.OnClickListener ivMenuClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext != null) {
                    if (isSelf) {
                        AppUtils.pushLeftActivity(mContext, UserModifyDetailActivity.class);
                    } else {
                        int[] location = new int[2];
                        v.getLocationInWindow(location);
                        bubblePopWindow.showAtLocation(window.getDecorView()
                                , Gravity.NO_GRAVITY, location[0], ScreenUtil.dp2px(mContext, 56)
                                        + location[1]);
                    }
                }
            }
        };
        View.OnClickListener civUserThumbClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewPicLayout.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(userDetails.getThumb())
                        .dontAnimate()
                        .into(reviewPic);
            }
        };
        View.OnClickListener llPhotoLayClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat0 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent0 = new Intent(mContext, UserImagesActivity.class);
                intent0.putExtra("toUid", uid + "");
                intent0.putExtra("from", "UserDetailV2Activity");
                ActivityCompat.startActivity(mContext, intent0, compat0.toBundle());
            }
        };
        View.OnClickListener rlDefaultPhotoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelf) {
                    selectPath.clear();
                    selectCount = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                        requestPermission();
                    } else {
                        //Todo 相册选择
                        if (mContext instanceof SignInterHotalActivity) {
                            PhotoPicker.builder()
                                    .setPhotoCount(9)
                                    .setShowCamera(true)
                                    .setShowGif(true)
                                    .setPreviewEnabled(true)
                                    .start(((SignInterHotalActivity) mContext), PhotoPicker.REQUEST_CODE);
                        } else if (mContext instanceof SignInterActivity) {
                            PhotoPicker.builder()
                                    .setPhotoCount(9)
                                    .setShowCamera(true)
                                    .setShowGif(true)
                                    .setPreviewEnabled(true)
                                    .start(((SignInterActivity) mContext), PhotoPicker.REQUEST_CODE);
                        } else if (mContext instanceof SuperMainActivity) {
                            PhotoPicker.builder()
                                    .setPhotoCount(9)
                                    .setShowCamera(true)
                                    .setShowGif(true)
                                    .setPreviewEnabled(true)
                                    .start(((SuperMainActivity) mContext), PhotoPicker.REQUEST_CODE);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "您的邀请已发送", Toast.LENGTH_SHORT).show();
                }
            }
        };
        View.OnClickListener llNewsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat4 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent4 = new Intent(mContext, UserUgcListActivity.class);
                intent4.putExtra("type", 1);
                intent4.putExtra("friendUid", uid + "");
                if (userDetails != null)
                    intent4.putExtra("hasFollow", userDetails.getHasFollow());
                ActivityCompat.startActivity(mContext, intent4, compat4.toBundle());
            }
        };
        View.OnClickListener llGoodsLayClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent2 = new Intent(mContext, UserGoodsActivity.class);
                intent2.putExtra("toUid", uid + "");
                intent2.putExtra("from", "UserDetailV2Activity");
                ActivityCompat.startActivity(mContext, intent2, compat2.toBundle());
            }
        };
        View.OnClickListener tvFollowClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("zune", "关注");
                if (followClickable) {
                    followClickable = false;
                    if (!userDetails.getHasFollow()) {
                        addFollow(uid);
                    } else {
                        deleteFollow(uid);
                    }
                }
            }

        };
        View.OnClickListener tvChatClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zune", "发消息");
                if (userDetails != null && uid > 0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("userId", "" + uid);
                    intent.putExtra("mid", "" + mid);
                    intent.putExtra("toChatUserID", "" + uid);
                    intent.putExtra("toChatNickName", "" + userDetails.getNickName());
                    intent.putExtra("toChatHeaderUrl", "" + userDetails.getThumb());
                    intent.putExtra("ISFIGHTSEAT", 1);
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        };
        View.OnClickListener rlCoorClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canLikeClick && uid > 0) {
                    if (!isLiked) {
                        LikeUser user = new LikeUser();
                        user.setmUid(uid);
                        user.setsUid(MoreUser.getInstance().getUid());
                        user.setState(true);
                        likeGoodResp(user);
                    } else {
                        LikeUser user = new LikeUser();
                        user.setmUid(uid);
                        user.setsUid(MoreUser.getInstance().getUid());
                        user.setState(false);
                        likeGoodResp(user);
                    }
                }
            }
        };
        View.OnLongClickListener reviewPicLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSaveDialog(0);
                return false;
            }
        };

        PhotoViewAttacher.OnPhotoTapListener reviewPicClick = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View v, float x, float y) {
                if (reviewPicLayout.getVisibility() == View.VISIBLE) {
                    reviewPicLayout.setVisibility(View.GONE);
                }
            }
        };
        View.OnClickListener llFirstNewsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent1 = new Intent(mContext, UserUgcDetailActivity.class);
                intent1.putExtra("sid", userDetails.getUgcSimpleDtos().get(0).getSid());
                ActivityCompat.startActivity(mContext, intent1, compat1.toBundle());
            }
        };
        View.OnClickListener llSecondNewsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent1 = new Intent(mContext, UserUgcDetailActivity.class);
                intent1.putExtra("sid", userDetails.getUgcSimpleDtos().get(1).getSid());
                ActivityCompat.startActivity(mContext, intent1, compat1.toBundle());
            }
        };
        reviewPic.setOnLongClickListener(reviewPicLongClick);
        reviewPic.setOnPhotoTapListener(reviewPicClick);
        rlCoor.setOnClickListener(rlCoorClick);
        tvChat.setOnClickListener(tvChatClick);
        tvFollow.setOnClickListener(tvFollowClick);
        llGoodsLay.setOnClickListener(llGoodsLayClick);
        llNews.setOnClickListener(llNewsClick);
        llNewsLay.setOnClickListener(llNewsClick);
        llFirstLay.setOnClickListener(llFirstNewsClick);
        llFirstWithoutPhoto.setOnClickListener(llFirstNewsClick);
        llSecondLay.setOnClickListener(llSecondNewsClick);
        llSecondWithoutPhoto.setOnClickListener(llSecondNewsClick);
        ivNewsIcon.setOnClickListener(llFirstNewsClick);
        ivNewsIcon2.setOnClickListener(llSecondNewsClick);
        rlDefaultPhoto.setOnClickListener(rlDefaultPhotoClick);
        llPhotoLay.setOnClickListener(llPhotoLayClick);
        civUserThumb.setOnClickListener(civUserThumbClick);
        navRightBtn.setOnClickListener(ivMenuClick);
        tvIKnow.setOnClickListener(tvIKnowClick);
        rlDefaultNesPhoto.setOnClickListener(rlDefaultNesPhotoClick);
        invite_someone.setOnClickListener(inviteClick);
        nav_back.setOnClickListener(backClick);
        view_weight.setOnClickListener(backClick);
        aivTop.setOnClickListener(aivTopClick);
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
                    Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                    ivCool.setImageResource(R.drawable.like_highlight_btn);
                    UserDetailsV2.ProfileLikeBean.LikesBean like = new UserDetailsV2.ProfileLikeBean.LikesBean();
                    like.setUid(MoreUser.getInstance().getUid());
                    like.setNickName(MoreUser.getInstance().getNickname());
                    if (!TextUtils.isEmpty(MoreUser.getInstance().getThumb()))
                        like.setThumb(MoreUser.getInstance().getThumb());
                    likes.add(like);
                } else {
                    Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
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

    private void deleteFollow(Long uid) {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                followClickable = true;
                if (userDetails.getHasFollow()) {
                    userDetails.setHasFollow(false);
                    tvFollow.setText("关注");
                    Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
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
        param.setFriend(uid + "");
        ApiInterface.ApiFactory.createApi(token).onFollowDel(MoreUser.getInstance().getUid() + ""
                , uid + "", param).enqueue(callback);
    }

    private void addFollow(Long uid) {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                followClickable = true;
                if (!userDetails.getHasFollow()) {
                    userDetails.setHasFollow(true);
                    tvFollow.setText("关注中");
                    Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                followClickable = true;
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        UserFollowParam param = new UserFollowParam();
        param.setFriendId(uid + "");
        param.setOwnerId(MoreUser.getInstance().getUid() + "");
        ApiInterface.ApiFactory.createApi(token).onFollowAdd(param).enqueue(callback);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(mContext, permissions[0]) != PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, permissions[1]) != PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            ActivityCompat.requestPermissions((Activity) mContext, permissions, REQUEST_CODE_READ);
        } else {
            //Todo 打开相册
            PhotoPicker.builder()
                    .setPhotoCount(9)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start((Activity) mContext, PhotoPicker.REQUEST_CODE);
        }
    }

    private void replyInvite(SignInterResult params, final String uid) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                //Todo 拼座邀请成功
                if (response != null && response.body() != null) {
                    Boolean data = response.body().getData();
                    if (data != null && data) {
                        Toast.makeText(mContext, "已发送邀请成功", Toast.LENGTH_SHORT).show();
                        //Todo 进入会话框
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("userId", uid);
                        intent.putExtra("toChatUserID", uid);
                        intent.putExtra("toChatNickName", userDetails.getNickName());
                        intent.putExtra("toChatHeaderUrl", userDetails.getThumb());
                        intent.putExtra("invite", invite);
                        intent.putExtra("ISFIGHTSEAT", 1);
                        ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                        EMMessage message = EMMessage.createTxtSendMessage
                                (com.moreclub.moreapp.main.constant.Constants.TITLE_SEND_REPLY, uid);
                        sendMessage(message, uid);
                    } else
                        Toast.makeText(mContext, response.body().getErrorDesc(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

            }
        };
        ApiInterface.ApiFactory.createApi(MoreUser.getInstance().getAccess_token())
                .onReplyInvite(params).enqueue(callback);
    }

    private void sendMessage(EMMessage message, String uid) {
        if (message == null) {
            return;
        }
        //Todo Type类型
        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        message.setAttribute("nickName", MoreUser.getInstance().getNickname());
        message.setAttribute("headerUrl", MoreUser.getInstance().getThumb());
        message.setAttribute(uid, userDetails.getNickName() + "," + userDetails.getThumb());
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

    private void setupPopData(UserDetailsV2 userDetails, Long uid) {
        if (userDetails == null) return;
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
        setBirthdayStar(userDetails);
        if (tags.size() != getTags(userDetails).size()) {
            tags.addAll(getTags(userDetails));
            setTagAdapter(flTags, tags);
        }
        if (profileAlbum != null) {
            List<UserDetailsV2.ProfileAlbumBean.AlbumsBean> albums = profileAlbum.getAlbums();
            if (albums == null)
                albums = new ArrayList<>();
            setPhotosLay(profileAlbum.getCount(), albums);
        }
        Glide.with(mContext)
                .load(userDetails.getThumb())
                .dontAnimate()
                .placeholder(R.drawable.profilephoto_small)
                .into(civUserThumb);
        int radius = 6;
        int margin = 4;
        Glide.with(mContext)
                .load(userDetails.getThumb())
                .dontAnimate()
                .bitmapTransform(new BlurTransformation(mContext, radius, margin))
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
            if (uid != null && !isSelf) {
                hintUserInfo();
            } else if (uid != null) {
                tvNoContent.setVisibility(View.VISIBLE);
            }
        } else {
            tvNoContent.setVisibility(View.GONE);
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

    private void setPhotosLay(int count, List<UserDetailsV2.ProfileAlbumBean.AlbumsBean> albums) {
        if (albums == null) {
            tvPhotosCount.setText("0");
            return;
        }
        imagePaths.clear();
        imageBigUrls.clear();
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
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewDivider0.getLayoutParams();
            layoutParams.addRule(BELOW, R.id.rl_default_photo);
            viewDivider0.setLayoutParams(layoutParams);
        }
        if (mUserPopupWindow != null && window != null)
            mUserPopupWindow.setContentView(window.getDecorView());
    }

    private TagAdapter setTagAdapter(final TagFlowLayout layout, final List<String> userTags) {
        TagAdapter adapter = new TagAdapter<String>(userTags) {
            @Override
            public View getView(FlowLayout parent, int position, String userTag) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(
                        R.layout.user_detail_tag, layout, false);

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
                if (getTextWidth(tv, userTag) > ScreenUtil.getScreenWidth(mContext)
                        - ScreenUtil.dp2px(mContext, 32) - 12) {
                    layoutParams.width = ScreenUtil.getScreenWidth(mContext)
                            - ScreenUtil.dp2px(mContext, 32) - 12;
                } else {
                    layoutParams.width = WRAP_CONTENT;
                }
                layoutParams.height = WRAP_CONTENT;
                layoutParams.setMargins(6, 6, 0, 6);
                int padding = ScreenUtil.dp2px(mContext, 6);
                tv.setPadding(padding, padding, padding, padding);
                String name = userTags.get(position);
                tv.setText(name);
                int color = randomColor();
                tv.setTextColor(color);
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(ScreenUtil.dp2px(mContext, 2));
                shape.setStroke(ScreenUtil.dp2px(mContext, 1), color);
                tv.setBackgroundDrawable(shape);
                return tv;
            }
        };
        layout.setAdapter(adapter);
        return adapter;
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

    private float getTextWidth(TextView view, String text) {
        TextPaint paint = view.getPaint();
        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * 12);
        return paint.measureText(text);
    }

    private List getTags(UserDetailsV2 userDetails) {
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

    private void setBirthdayStar(UserDetailsV2 userDetails) {
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
            int margin = (int) ((screenWidth - ScreenUtil.dp2px(mContext, 24) * 9.0f - ScreenUtil.dp2px(mContext, 16)) / 18.0f);
            for (int i = 0; i < 9; i++) {
                CircleImageView image = new CircleImageView(mContext);
                LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                        ScreenUtil.dp2px(mContext, 24), ScreenUtil.dp2px(mContext, 24));
                lp.leftMargin = margin;
                lp.rightMargin = margin;
                image.setLayoutParams(lp);
                if (users.size() >= 8) {
                    if (i == 8) {
                        TextView textView = new TextView(mContext);
                        LinearLayoutCompat.LayoutParams lp2 = new LinearLayoutCompat.LayoutParams(
                                ScreenUtil.dp2px(mContext, 24), ScreenUtil.dp2px(mContext, 24));
                        lp2.setMargins(margin, 0, margin, 0);
                        textView.setLayoutParams(lp2);
                        textView.setBackgroundResource(R.drawable.circle_blue);
                        textView.setGravity(CENTER);
                        textView.setTextColor(Color.WHITE);
                        textView.setText("8+");
                        llGoods.addView(textView);
                    } else {
                        Glide.with(mContext)
                                .load(users.get(i).getThumb())
                                .dontAnimate()
                                .placeholder(R.drawable.profilephoto_small)
                                .into(image);
                        llGoods.addView(image);
                    }
                } else {
                    if (i == users.size()) {
                        TextView textView = new TextView(mContext);
                        LinearLayoutCompat.LayoutParams lp2 = new LinearLayoutCompat.LayoutParams(
                                ScreenUtil.dp2px(mContext, 24), ScreenUtil.dp2px(mContext, 24));
                        lp2.setMargins(margin, 0, margin, 0);
                        textView.setLayoutParams(lp2);
                        textView.setBackgroundResource(R.drawable.circle_blue);
                        textView.setText(users.size() + "");
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(CENTER);
                        llGoods.addView(textView);
                    } else if (i < users.size()) {
                        Glide.with(mContext)
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
                    Glide.with(mContext)
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
                } else if (isOtherPeople) {
                    rlDefaultNesPhoto.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(ugcSimpleDtos.get(0).getTitlePictrues())) {
                    llFirstLay.setVisibility(View.GONE);
                    llFirstWithoutPhoto.setVisibility(VISIBLE);
                    tvNoPhoto.setText(ugcSimpleDtos.get(0).getContent());
                } else {
                    Glide.with(mContext)
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
                        Glide.with(mContext)
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

    private void showAllSubject(ArrayList<String> pics, int clickpos) {
        popupBigImageView = LayoutInflater.from(mContext).inflate(
                R.layout.merchant_details_scene_popuwindow, null);
        popupBigImageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));

        //设置弹出部分和面积大小
        mBigPopupWindow = new PopupWindow(popupBigImageView, screenWidth, screenHeight, true);
        //设置动画弹出效果
        mBigPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        // 设置半透明灰色
        ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
        mBigPopupWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
        mBigPopupWindow.setTouchable(true);
        mBigPopupWindow.setFocusable(true);
        popupWindowSetupView(popupBigImageView, pics, clickpos);
        int[] pos = new int[2];
        mBigPopupWindow.showAtLocation(window.getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
        MainApp.getInstance().spacePops.add(mBigPopupWindow);
    }

    private void popupWindowSetupView(View view, final ArrayList<String> pics, int clickpos) {

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        final TextView sceneName = (TextView) view.findViewById(R.id.sceneName);
        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(mContext, pics);

        sceneViewPager.setAdapter(adapter);

        adapter.setOnItemClickListener(new MerchantScenePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (MainApp.getInstance().spacePops.size() > 0)
                    MainApp.getInstance().spacePops.remove(mBigPopupWindow);
                mBigPopupWindow.dismiss();
                mBigPopupWindow = null;
            }
        });

        sceneViewPager.setCurrentItem(clickpos);
        sceneName.setText((clickpos + 1) + " / " + pics.size());
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

    private void setupScrollView(Long uid) {
        final float[] firstX = {0};
        final float[] firstY = {0};
        final float[] lastX = {0};
        final float[] lastY = {0};
        setupMenu(uid);
        detailsScrollView.addScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                if (scrollState != null)
                    switch (scrollState) {
                        case UP:
                            if (!hasGone)
                                hindViewWeight();
                            break;
                        case STOP:
                            Log.i("zune:", "stop");
                            break;
                        case DOWN:
                            Log.i("zune:", "down");
                            break;
                    }
            }
        });
        detailsScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (llUpshowOneshot.getVisibility() == VISIBLE) {
                    PrefsUtils.setBoolean(mContext, ISFIRST_SHOW_UP_ONESHOT, false);
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

    private void setupMenu(final Long uid) {
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(mContext).
                inflate(R.layout.user_details_menu, null);
        bubblePopWindow = BubblePopupHelper.create(mContext, bubbleLayout);
        TextView menu_report = (TextView) bubbleLayout.findViewById(R.id.menu_report);
        menu_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext,
                        UserDetailsReportActivity.class);
                intent.putExtra("relationID", uid);
                intent.putExtra("type", "3");
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        });
    }

    @SuppressLint("NewApi")
    private void hindViewWeight() {
        canScroll = true;
        try {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view_weight, "translationY"
                    , 0, -(int) (screenHeight * 0.35f));
            animator.setDuration(300);
            animator.setInterpolator(ACCELERATE_INTERPOLATOR);
            animator.start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float animatedValue = (Float) animation.getAnimatedValue();
                    int height = (int) (screenHeight * 0.35f);
                    height += animatedValue;
                    if (height < 0) {
                        height = 0;
                    }
                    if (Build.VERSION.SDK_INT <= 19) {
                        height = ScreenUtil.dp2px(mContext, 8);
                    }
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, height);
                    view_weight.setLayoutParams(lp);
                    view_weight.setVisibility(View.INVISIBLE);
                    nav_back.setVisibility(VISIBLE);
                    navRightBtn.setVisibility(VISIBLE);
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

    private void showSaveDialog(final int pos) {
        AppCompatActivity activity = (AppCompatActivity) mContext;
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
                FileSaveUtils.saveImageToGallery(mContext, map, SAVE_REAL_PATH);
            }

            @Override
            public void getFilesFail() {

            }
        });
    }

    private void startUserDetail(SignSpaceList.SininMoreSpacesBean item) {
        if (MoreUser.getInstance().getUid() == 0) {
            AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
        } else {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    mContext, R.anim.push_down_in, R.anim.push_down_out);
            Intent intent = new Intent(mContext, UserDetailV2Activity.class);
//                    Intent intent = new Intent(mContext, UserDetailsActivity.class);
            intent.putExtra("isFromSpace", true);
            intent.putExtra("toUid", "" + item.getUid());
            for (int i = 0; i < newSignInterses.size(); i++) {
                if (newSignInterses.get(i) != null && newSignInterses.get(i).getUid() != null
                        && newSignInterses.get(i).getUid().equals(item.getUid())) {
                    sid = newSignInterses.get(i).getSid();
                }
            }
            intent.putExtra("mySid", mySid);
            ActivityCompat.startActivity(mContext, intent, compat.toBundle());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public void upDatePopupWindow() {
        if (mUserPopupWindow != null && mUserPopupWindow.isShowing()) {
            if (view_weight.getVisibility() == VISIBLE) {
                hasGone = false;
            } else {
                hasGone = true;
            }
            followClickable = true;
            canLikeClick = true;
            imageBigUrls.clear();
            imagePaths.clear();
            upLoadUrls.clear();
            selectPath.clear();
            likes.clear();
            llGoods.removeAllViews();
            loadPopDatas(toUid);
        }
    }

    public void refreshAdpater(ArrayList<String> paths) {
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
                        uploadNetImages(images);
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
                        uploadNetImages(images);
                    }
                    Log.i("zune:", "error = " + error);
                }
            };
            new AliUploadUtils(mContext, Constants.ALI_OSS_BUCKETNAME
                    , getImageObjectKey("" + MoreUser.getInstance().getUid()), paths.get(i),
                    MoreUser.getInstance().getUid() + "",
                    uploadResultListener);
        }
    }

    private void uploadNetImages(UploadUserImage images) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (response == null || response.body() == null) return;
                Boolean data = response.body().getData();
                tvPhotosCount.setText(upLoadCount + (TextUtils.isEmpty(tvPhotosCount.getText().toString())
                        ? 0 : Integer.parseInt(tvPhotosCount.getText().toString())) + "");
                upLoadCount = 0;
                upLoadUrls.clear();
                if (data != null && data) {
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                Log.i("zune:", "t = " + t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(MoreUser.getInstance().getAccess_token())
                .onUpLoadImages(images).enqueue(callback);
    }

    //通过UserCode 加上日期组装 OSS路径
    private String getImageObjectKey(String strUserCode) {
        Date date = new Date();
        return new SimpleDateFormat("yyyy/M/d").format(date) + "/" + strUserCode
                + new SimpleDateFormat("yyyyMMddssSSS").format(date) + generateString(12) + ".jpg";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout addrLay;
        private TextView addrName;
        private ImageView baseImageDrink;
        private TextView contentInter;
        private ImageView baseImageType;
        public RelativeLayout signInterLayout;
        public CircleImageView mIcon;
        public ImageView mSexImg;
        public TextView mName;
        public TextView mAge;
        public TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.nameText);
            mIcon = (CircleImageView) itemView.findViewById(R.id.header_img);
            mSexImg = (ImageView) itemView.findViewById(R.id.sex_img);
            mAge = (TextView) itemView.findViewById(R.id.age);
            mTime = (TextView) itemView.findViewById(R.id.time);
            signInterLayout = (RelativeLayout) itemView.findViewById(R.id.sign_inter_item);
            baseImageType = (ImageView) itemView.findViewById(R.id.base_image_type);
            baseImageDrink = (ImageView) itemView.findViewById(R.id.base_image_drink);
            contentInter = (TextView) itemView.findViewById(R.id.content_sign_inter);
            addrName = (TextView) itemView.findViewById(R.id.addr_name);
            addrLay = (LinearLayout) itemView.findViewById(R.id.ll_address_lay);
        }
    }


    private int[] getViewWidthAndHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(width, height);

        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }
}
