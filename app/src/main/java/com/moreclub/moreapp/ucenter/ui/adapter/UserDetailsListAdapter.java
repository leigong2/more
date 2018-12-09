package com.moreclub.moreapp.ucenter.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.util.DateUtils;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.log.Logger;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.Comments;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.main.model.UserLikesItem;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.presenter.IMainChannelAttentionLoader;
import com.moreclub.moreapp.main.presenter.MainChannelAttentionDataBinder;
import com.moreclub.moreapp.main.ui.activity.MChannelCommentActivity;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.adapter.MerchantScenePagerAdapter;
import com.moreclub.moreapp.ucenter.model.BaseUserDetails;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.UserCollect;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Captain on 2017/8/2.
 */

public class UserDetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements IMainChannelAttentionLoader.LoadMainDataBinder {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private enum USER_DETAILS_ITEM_TYPE {
        ITEM_TYPE_NORM,
        ITEM_TYPE_ONE,
        ITEM_TYPE_ACTIVITY
    }

    private ArrayList<BaseUserDetails> dataList;
    private Context context;

    private BasePresenter mPresenter;
    private PopupWindow mPopupWindow;
    private View popupParentView;
    private ViewPagerFixed sceneViewPager;

    private PopupWindow mCommentWindow;
    private View commentParentView;
    private EditText commentView;
    private int attentionPositon = 0;
    private int likePosition = 0;
    private Comments mComment;

    public UserDetailsListAdapter(Context c, ArrayList<BaseUserDetails> list) {
        dataList = list;
        context = c;
        mPresenter = LogicProxy.getInstance().bind(IMainChannelAttentionLoader.class, this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == USER_DETAILS_ITEM_TYPE.ITEM_TYPE_ONE.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_channel_item, viewGroup, false);
            return new UserDetailsListAdapter.ItemChannelHolder(view);
        } else if (viewType == USER_DETAILS_ITEM_TYPE.ITEM_TYPE_ACTIVITY.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activities_item, viewGroup, false);
            return new UserDetailsListAdapter.ItemActivityViewHolder(view);
        } else if (viewType == USER_DETAILS_ITEM_TYPE.ITEM_TYPE_NORM.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.no_data_view, viewGroup, false);
            return new UserDetailsListAdapter.ItemNoDataViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseUserDetails item = dataList.get(position);
        if (item.getListType() == 1) {
            return USER_DETAILS_ITEM_TYPE.ITEM_TYPE_ONE.ordinal();
        } else if (item.getListType() == 2) {
            return USER_DETAILS_ITEM_TYPE.ITEM_TYPE_ACTIVITY.ordinal();
        } else {
            return USER_DETAILS_ITEM_TYPE.ITEM_TYPE_NORM.ordinal();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof UserDetailsListAdapter.ItemChannelHolder) {
            String postUrl;
            final MainChannelItem item = (MainChannelItem) dataList.get(position);
            if (item == null)
                return;

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getSource() == 1) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelDetailsAcitivy.class);
                        intent.putExtra("sid", item.getInformationId());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });

            ImageView headerImage = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).headerImage;
            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    if (item.getType() == 4) {
                        ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                        intent_merchant.putExtra("toUid", "" + item.getUid());
                        intent_merchant.putExtra("isChannel", "true");
                        MainChannelItem.ChainMerchants merchants = null;
                        if (item.getChainMerchants() != null && item.getChainMerchants().size() > 0) {
                            merchants = item.getChainMerchants().get(0);
                        }
                        if (merchants != null) {
                            intent_merchant.putExtra("mid", merchants.getMid());
                        }
                        ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
                    } else {
                        ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                        intent_merchant.putExtra("toUid", "" + item.getUid());
                        ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
                    }
                }
            });
            TextView headerName = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).headerName;
            TextView headerTime = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).headerTime;
            TextView attentionBtn = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).attentionBtn;
            if (item.getType() == 4 && !item.isFollow()) {
                attentionBtn.setVisibility(View.VISIBLE);
            } else {
                attentionBtn.setVisibility(View.GONE);
            }
            attentionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    v.setVisibility(View.GONE);
                    if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                        attentionPositon = position;
                        ChannelAttentionParam param = new ChannelAttentionParam();
                        MainChannelItem.SubType subType = item.getSubType();
                        if (subType == null) {
                            return;
                        }
                        param.setFriendId(subType.getStid());
                        param.setOwnerId(MoreUser.getInstance().getUid());
                        param.setRemark("1");
                        ((MainChannelAttentionDataBinder) mPresenter).onAttentionChannel(param);
                    } else {

                    }
                }
            });
            TextView channelDes = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).channelDes;
            RelativeLayout picgridView = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).picgridView;
            TextView addresstext = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).address;
            addresstext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainChannelItem.ChainMerchants merchants = null;
                    if (item.getChainMerchants() != null && item.getChainMerchants().size() > 0) {
                        merchants = item.getChainMerchants().get(0);
                    }
                    if (merchants != null) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MerchantDetailsViewActivity.class);
                        intent.putExtra("mid", "" + merchants.getMid());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });
            RelativeLayout nineLayout = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nineLayout;
            ImageView oneLayout = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).oneLayout;
            RelativeLayout fourLayout = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).fourLayout;
            ImageView topLeftImg = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).topLeftImg;
            topLeftImg.setOnClickListener(channelPictureClick);
            topLeftImg.setTag(R.id.top_left_img, position);
            ImageView topRightImg = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).topRightImg;
            topRightImg.setOnClickListener(channelPictureClick);
            topRightImg.setTag(R.id.top_right_img, position);
            ImageView bottomLeftImg = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).bottomLeftImg;
            bottomLeftImg.setOnClickListener(channelPictureClick);
            bottomLeftImg.setTag(R.id.bottom_left_img, position);
            ImageView bottomRightImg = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).bottomRightImg;
            bottomRightImg.setOnClickListener(channelPictureClick);
            bottomRightImg.setTag(R.id.bottom_right_img, position);
            ImageView nine_layout1 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout1;
            nine_layout1.setOnClickListener(channelPictureClick);
            nine_layout1.setTag(R.id.nine_layout1, position);
            ImageView nine_layout2 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout2;
            nine_layout2.setOnClickListener(channelPictureClick);
            nine_layout2.setTag(R.id.nine_layout2, position);
            ImageView nine_layout3 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout3;
            nine_layout3.setOnClickListener(channelPictureClick);
            nine_layout3.setTag(R.id.nine_layout3, position);
            ImageView nine_layout4 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout4;
            nine_layout4.setOnClickListener(channelPictureClick);
            nine_layout4.setTag(R.id.nine_layout4, position);
            ImageView nine_layout5 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout5;
            nine_layout5.setOnClickListener(channelPictureClick);
            nine_layout5.setTag(R.id.nine_layout5, position);
            ImageView nine_layout6 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout6;
            nine_layout6.setOnClickListener(channelPictureClick);
            nine_layout6.setTag(R.id.nine_layout6, position);
            ImageView nine_layout7 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout7;
            nine_layout7.setOnClickListener(channelPictureClick);
            nine_layout7.setTag(R.id.nine_layout7, position);
            ImageView nine_layout8 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout8;
            nine_layout8.setOnClickListener(channelPictureClick);
            nine_layout8.setTag(R.id.nine_layout8, position);
            ImageView nine_layout9 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).nine_layout9;
            nine_layout9.setOnClickListener(channelPictureClick);
            nine_layout9.setTag(R.id.nine_layout9, position);

            LinearLayout likeBtn = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).likeBtn;
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                        likePosition = position;
                        ((MainChannelAttentionDataBinder) mPresenter).
                                onLikeChannel(item.getInformationId(),
                                        MoreUser.getInstance().getUid(), "likeTime1");
                    } else {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    }
                }
            });

            int likeHeaderWidth = ScreenUtil.dp2px(context, 24);
            int likeHeaderTop = ScreenUtil.dp2px(context, 8);
            LinearLayout likePeopleLayout = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).likePeopleLayout;
            MainChannelItem.LikeDto likeDto = item.getLikeDto();
            ArrayList<UserLikesItem> userLikes = likeDto.getUserLikes();
//            if ((userLikes != null && userLikes.size() > 0 && likePeopleLayout.getChildCount() == 0) || (likeDto.isRefresh() && userLikes != null)) {
            likePeopleLayout.removeAllViews();
            if (userLikes != null && userLikes.size() > 0){
                for (int i = 0; i < userLikes.size(); i++) {
                    UserLikesItem likesItem = userLikes.get(i);
                    ImageView likeUserItemView = new ImageView(context);
                    LinearLayout.LayoutParams bgParams = new LinearLayout.LayoutParams(
                            likeHeaderWidth,
                            likeHeaderWidth);
                    bgParams.setMargins(8, 8, 8, 8);
                    likeUserItemView.setLayoutParams(bgParams);
                    likeUserItemView.setBackgroundResource(R.drawable.circle_gray_dush);
                    likePeopleLayout.addView(likeUserItemView);
                    if (i >= 6) {
                        likeUserItemView.setImageResource(R.drawable.more_likes);
                        break;
                    } else {
                        Glide.with(context)
                                .load(likesItem.getThumb())
                                .dontAnimate()
                                .transform(new GlideCircleTransform(context, 2, 0xF0F0F0))
                                .placeholder(R.drawable.profilephoto_small)
                                .into(likeUserItemView);
                    }
                }
                likeDto.setRefresh(false);
            }


            TextView commentCount = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).commentCount;
            commentCount.setText("" + item.getCommentCount());
            ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    } else {
                        showCommentPopuWindow(item.getInformationId(), MoreUser.getInstance().getNickname(), -1, position);
                        mComment = new Comments();
                    }
                }
            });

            TextView goAllCommentBtn = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).goAllCommentBtn;
            TextView likeCount = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).likeCount;
            ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).like.setImageResource(R.drawable.like);
            if (item.getLikeDto().getUserLikes() != null) {
                int likesSize = item.getLikeDto().getUserLikes().size();
                for (int i = 0; i < likesSize; i++) {
                    if (item.getLikeDto().getUserLikes().get(i).getUid() == MoreUser.getInstance().getUid()) {
                        ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).like.setImageResource(R.drawable.like2);
                        break;
                    }
                }
            }
            if (item.getLikeDto() != null && item.getLikeDto().getLikeTimes() > 0) {
                int likeCountValue = item.getLikeDto().getLikeTimes();
                if (likeCountValue > 1000) {
                    likeCount.setText("" + (likeCountValue / 1000) + "k");
                } else {
                    likeCount.setText("" + likeCountValue);
                }
            } else {
                likeCount.setText("0");
            }

            TextView commentText1 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).comment_text_1;
            TextView commentText2 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).comment_text_2;
            TextView commentText3 = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).comment_text_3;
            commentText1.setVisibility(View.GONE);
            commentText2.setVisibility(View.GONE);
            commentText3.setVisibility(View.GONE);
            LinearLayout ll_comment = ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).ll_comment;

            ArrayList<ImageView> nineArray = new ArrayList<>();
            nine_layout1.setVisibility(View.GONE);
            nineArray.add(nine_layout1);
            nine_layout2.setVisibility(View.GONE);
            nineArray.add(nine_layout2);
            nine_layout3.setVisibility(View.GONE);
            nineArray.add(nine_layout3);
            nine_layout4.setVisibility(View.GONE);
            nineArray.add(nine_layout4);
            nine_layout5.setVisibility(View.GONE);
            nineArray.add(nine_layout5);
            nine_layout6.setVisibility(View.GONE);
            nineArray.add(nine_layout6);
            nine_layout7.setVisibility(View.GONE);
            nineArray.add(nine_layout7);
            nine_layout8.setVisibility(View.GONE);
            nineArray.add(nine_layout8);
            nine_layout9.setVisibility(View.GONE);
            nineArray.add(nine_layout9);

            Glide.with(context)
                    .load(item.getFromThumb())
                    .dontAnimate()
                    .transform(new GlideCircleTransform(context, 2, 0xF0F0F0))
                    .into(headerImage);

            headerName.setText(item.getNickName());
            headerTime.setText(DateUtils.getTimestampString(new Date(item.getPrefeerTime())));
            MainChannelItem.ChainInter chainInteritem = null;
            if (item.getChainInters() != null && item.getChainInters().size() > 0) {
                chainInteritem = item.getChainInters().get(0);
            }
            String chainInterTitle = "";
            if (chainInteritem != null)
                chainInterTitle = chainInteritem.getTitle();
            String chainInter = "";
            if (chainInterTitle != null && chainInterTitle.length() > 0) {
                chainInter = "#" + chainInterTitle + "#  " + item.getPrefeerContent();
                SpannableStringBuilder spannable = new SpannableStringBuilder(chainInter);
                int startIndex = 0;
                int endIndex = chainInterTitle.length() + 2;
                int color = Color.parseColor("#DDB544");
                spannable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                channelDes.setText(spannable);
            } else {
                channelDes.setText(item.getPrefeerContent());
            }

            String prefeerPictrue = item.getPrefeerPictrues();
            String prefeerPictrues[] = null;
            if (prefeerPictrue != null) {
                prefeerPictrues = prefeerPictrue.split(",");
                postUrl = prefeerPictrues[0];
                if (prefeerPictrues.length == 1) {
                    oneLayout.setVisibility(View.VISIBLE);
                    nineLayout.setVisibility(View.GONE);
                    fourLayout.setVisibility(View.GONE);
                    final String onePic = prefeerPictrues[0];
                    Glide.with(context)
                            .load(prefeerPictrues[0])
                            .dontAnimate()
                            .into(oneLayout);
                    oneLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> pic =new ArrayList<String>();
                            pic.add(onePic);
                            showAllSubject(pic, 0);
                        }
                    });
                } else if (prefeerPictrues.length == 4) {
                    oneLayout.setVisibility(View.GONE);
                    nineLayout.setVisibility(View.GONE);
                    fourLayout.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(prefeerPictrues[0])
                            .dontAnimate()
                            .into(topLeftImg);
                    Glide.with(context)
                            .load(prefeerPictrues[1])
                            .dontAnimate()
                            .into(topRightImg);
                    Glide.with(context)
                            .load(prefeerPictrues[2])
                            .dontAnimate()
                            .into(bottomLeftImg);
                    Glide.with(context)
                            .load(prefeerPictrues[3])
                            .dontAnimate()
                            .into(bottomRightImg);
                } else {
                    oneLayout.setVisibility(View.GONE);
                    nineLayout.setVisibility(View.VISIBLE);
                    fourLayout.setVisibility(View.GONE);

                    for (int i = 0; i < prefeerPictrues.length; i++) {
                        if (i < 9) {
                            ImageView itemNine = nineArray.get(i);
                            itemNine.setVisibility(View.VISIBLE);
                            Glide.with(context)
                                    .load(prefeerPictrues[i])
                                    .dontAnimate()
                                    .into(itemNine);
                        }
                    }
                }
            } else {
                postUrl = "";
            }
            final String finalPostUrl = postUrl;
            likePeopleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do...评论

                    if (item != null && item.getInformationId() > 0 && item.getSource() == 1) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelCommentActivity.class);
                        intent.putExtra("postUrl", finalPostUrl);
                        intent.putExtra("type", item.getType());
                        intent.putExtra("prefeerContent",item.getPrefeerContent());
                        intent.putExtra("sid", item.getInformationId());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });
            ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do...评论
                    if (item != null && item.getInformationId() > 0 && item.getSource() == 1) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelCommentActivity.class);
                        intent.putExtra("postUrl", finalPostUrl);
                        intent.putExtra("type", item.getType());
                        intent.putExtra("prefeerContent",item.getPrefeerContent());
                        intent.putExtra("sid", item.getInformationId());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });
            ((UserDetailsListAdapter.ItemChannelHolder) viewHolder).goAllCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null && item.getInformationId() > 0 && item.getSource() == 1) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelCommentActivity.class);
                        intent.putExtra("sid", item.getInformationId());
                        intent.putExtra("type", item.getType());
                        intent.putExtra("prefeerContent",item.getPrefeerContent());
                        if (finalPostUrl != null)
                            intent.putExtra("postUrl", finalPostUrl);
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });

            MainChannelItem.ChainMerchants chainMerchants = null;
            if (item.getChainMerchants() != null && item.getChainMerchants().size() > 0) {
                chainMerchants = item.getChainMerchants().get(0);
                StringBuilder addressBuilder = new StringBuilder();
                if (chainMerchants.getCityCode() != null) {
                    addressBuilder.append("  ");
                    if (MainApp.cityMap != null) {
                        addressBuilder.append(MainApp.cityMap.get(chainMerchants.getCityCode()));
                    }
                    if (chainMerchants.getName() != null) {
                        addressBuilder.append(".");
                        addressBuilder.append(chainMerchants.getName());
                    }
                    addresstext.setText(addressBuilder.toString());
                } else {
                    addresstext.setText("");
                }
            } else {
                addresstext.setVisibility(View.GONE);
            }

            if (item.getCommentCount() == 0) {
                goAllCommentBtn.setVisibility(View.GONE);
            } else {
                goAllCommentBtn.setVisibility(View.VISIBLE);
                goAllCommentBtn.setText("查看全部" + item.getCommentCount() + "条评论");
            }
            if (item.getComments() != null && item.getComments().size() > 0) {
                if (item.getComments().size() >= 1) {
                    Comments comentItem = item.getComments().get(0);
                    commentText1.setVisibility(View.VISIBLE);
                    setupCommentText(commentText1, comentItem, item, position);
                }
                if (item.getComments().size() >= 2) {
                    Comments comentItem = item.getComments().get(1);
                    commentText2.setVisibility(View.VISIBLE);
                    setupCommentText(commentText2, comentItem, item, position);
                }
                if (item.getComments().size() >= 3) {
                    Comments comentItem = item.getComments().get(2);
                    commentText3.setVisibility(View.VISIBLE);
                    setupCommentText(commentText3, comentItem, item, position);
                }
            }
        } else if (viewHolder instanceof UserDetailsListAdapter.ItemActivityViewHolder) {
            final MerchantActivity activityItem = (MerchantActivity) dataList.get(position);
            boolean collect = activityItem.isCollected();
            ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder).tv_activities_title.setText("           " + activityItem.getTitle());
            ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder).tv_activities_desc.setText(activityItem.getIntroduction());
            ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder).tv_activities_location.setText(activityItem.getIntroduction());
            ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder).tv_activities_date
                    .setText(formatDay(activityItem.getHoldingType(), activityItem.getHoldingTimes(), activityItem.getStartTime())
                    + formatTime(activityItem.getStartTime()) + "-" + formatTime(activityItem.getEndTime()));
            if (activityItem.getType() == 1 || activityItem.getType() == 5) {
                ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder)
                        .iv_activity_logo.setImageResource(R.drawable.activity_list_live);
            } else if (activityItem.getType() == 2 || activityItem.getType() == 6) {
                ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder)
                        .iv_activity_logo.setImageResource(R.drawable.activity_list_party);
            }
            if (collect) {
                ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder)
                        .iv_activities_collect.setImageResource(R.drawable.collect_fav_highlight);
            } else {
                ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder)
                        .iv_activities_collect.setImageResource(R.drawable.collect_fav);
            }

            if (activityItem.getEndTime() > System.currentTimeMillis()) {
                ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder)
                        .tv_activities_deadline.setText("");
            } else {
                ((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder)
                        .tv_activities_deadline.setText("已结束");
            }

            Glide.with(context)
                    .load(activityItem.getBannerPhoto())
                    .dontAnimate()
                    .placeholder(R.drawable.default_more)
                    .into(((UserDetailsListAdapter.ItemActivityViewHolder) viewHolder).iv_activities_picture);
            View.OnClickListener collectListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**zune:收藏活动**/
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    collect(activityItem, null);
                }
            };
            ((ItemActivityViewHolder) viewHolder).iv_activities_collect.setOnClickListener(collectListener);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**zune:进入活动详情**/
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            context, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(context, LiveActivity.class);
                    intent.putExtra(Constants.KEY_ACT_ID, activityItem.getActivityId());
                    ActivityCompat.startActivity(context, intent, compat.toBundle());
                }
            };
            ((ItemActivityViewHolder) viewHolder).root_view_activities.setOnClickListener(listener);
        }
    }

    private void collect(final MerchantActivity activityItem, final RecyclerViewHolder holder) {
        new UserCollect(context,
                activityItem.getActivityId() + "", UserCollect.COLLECT_TYPE.ACTIVITY_COLLECT.ordinal(),
                activityItem.isCollected(), new UserCollect.CollectCallBack() {
            @Override
            public void collectSuccess() {
                activityItem.setCollected(!activityItem.isCollected());
//                animateHeartButton((ImageView) holder.getView(R.id.iv_activities_collect), activityItem);
            }

            @Override
            public void collectFails() {
                Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateHeartButton(final ImageView btnLike, final MerchantActivity item) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(btnLike, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(btnLike, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(btnLike, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (item.isCollected()) {
                    btnLike.setImageResource(R.drawable.collect_fav_highlight);
                } else {
                    btnLike.setImageResource(R.drawable.collect_fav);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
    }

    private String format(long startTime) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 EE HH:mm");
        Date date = new Date(startTime);
        String formatDate = format.format(date);
        return formatDate;
    }

    private View.OnClickListener channelPictureClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewid = v.getId();
            int curp = (int) v.getTag(viewid);
            int picIndex = 0;
            if (viewid == R.id.top_left_img) {
                picIndex = 0;
            } else if (viewid == R.id.top_right_img) {
                picIndex = 1;
            } else if (viewid == R.id.bottom_left_img) {
                picIndex = 2;
            } else if (viewid == R.id.bottom_right_img) {
                picIndex = 3;
            } else if (viewid == R.id.nine_layout1) {
                picIndex = 0;
            } else if (viewid == R.id.nine_layout2) {
                picIndex = 1;
            } else if (viewid == R.id.nine_layout3) {
                picIndex = 2;
            } else if (viewid == R.id.nine_layout4) {
                picIndex = 3;
            } else if (viewid == R.id.nine_layout5) {
                picIndex = 4;
            } else if (viewid == R.id.nine_layout6) {
                picIndex = 5;
            } else if (viewid == R.id.nine_layout7) {
                picIndex = 6;
            } else if (viewid == R.id.nine_layout8) {
                picIndex = 7;
            } else if (viewid == R.id.nine_layout9) {
                picIndex = 8;
            }
            MainChannelItem positionItem = (MainChannelItem) dataList.get(curp);

            String prefeerPictrue = positionItem.getPrefeerPictrues();
            String prefeerPictrues[] = null;
            if (prefeerPictrue != null) {
                prefeerPictrues = prefeerPictrue.split(",");
            }
            if (prefeerPictrues != null && prefeerPictrues.length > 0) {
                ArrayList<String> picArray = new ArrayList<>();
                for (int i = 0; i < prefeerPictrues.length; i++) {
                    picArray.add(prefeerPictrues[i]);
                }
//                picArray.add("http://more-activity.oss-cn-beijing.aliyuncs.com/2016/12/02/7vQQRIcV7PmKJoyXqcXusJhuS.jpg");
//                picArray.add("http://more-activity.oss-cn-beijing.aliyuncs.com/2016/12/02/ROkD3oyh58gP592OgjZJnP2mW.jpg");
//                picArray.add("http://more-activity.oss-cn-beijing.aliyuncs.com/2016/12/02/5aV240pGh9nZbvpolG4Gg0pjs.jpg");
//                picArray.add("http://more-activity.oss-cn-beijing.aliyuncs.com/2016/12/02/gD1wQ4uSBqDG0uELnhA15rrBR.jpg");
                showAllSubject(picArray, picIndex);
            }
            Log.d("dddd", "" + picIndex);
        }
    };

    private int postID;
    private String fromNickName;
    private long toUid;
    private int commentRefreshPosition;
    private String myCommentContent;

    /**
     * channel评论
     */
    public void showCommentPopuWindow(int post, String nick, long tou, int p) {
        postID = post;
        fromNickName = nick;
        toUid = tou;
        commentRefreshPosition = p;
        if (null == mCommentWindow) {
            commentParentView = LayoutInflater.from(context).inflate(
                    R.layout.main_activity_comment_popu, null, false);
            commentView = (EditText) commentParentView.findViewById(R.id.et_write_comment);
            commentView.setOnKeyListener(onKeyListener);
            commentView.addTextChangedListener(textChangeListener);
            View noEditView = commentParentView.findViewById(R.id.no_edit_view);
            noEditView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentWindow.dismiss();
                    mCommentWindow = null;
                    commentParentView = null;
                    commentView = null;
                }
            });

            commentView.setVisibility(View.VISIBLE);
            if (toUid > 0 && toUid != MoreUser.getInstance().getUid()) {
                commentView.setHint("回复 " + fromNickName + "：");
            } else {
                commentView.setHint("留个言评论下吧……");
            }
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            commentView.setFocusable(true);
            commentView.setFocusableInTouchMode(true);
            commentView.requestFocus();

            //设置弹出部分和面积大小
            mCommentWindow = new PopupWindow(commentParentView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

            mCommentWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mCommentWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            //设置动画弹出效果
            mCommentWindow.setAnimationStyle(R.style.PopupAnimation);

            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x66000000);
            mCommentWindow.setBackgroundDrawable(dw);

            mCommentWindow.setTouchable(true);
            mCommentWindow.setFocusable(true);

        }
        int[] pos = new int[2];
        mCommentWindow.showAtLocation(((Activity) context).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);

    }


    /**
     * 图片预览popupWindow
     *
     * @param pics
     * @param clickpos
     */
    public void showAllSubject(ArrayList<String> pics, int clickpos) {
        if (null == mPopupWindow) {
            popupParentView = LayoutInflater.from(context).inflate(
                    R.layout.merchant_details_scene_popuwindow, null);
            popupParentView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            int width = ScreenUtil.getScreenWidth(context);
            int height = ScreenUtil.getScreenHeight(context);

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


        mPopupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
    }

    void popupWindowSetupView(View view, ArrayList<String> pics, int clickPos) {

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(context, pics);

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
    public void onAttentionChannelResponse(RespDto responce) {
        ChannelAttentionResult result = (ChannelAttentionResult) responce.getData();
        if (result != null && dataList != null && dataList.size() > 0) {
            MainChannelItem clickItem = (MainChannelItem) dataList.get(attentionPositon);
            clickItem.setFollow(true);
        }
    }

    @Override
    public void onAttentionChannelFailure(String msg) {

    }

    @Override
    public void onLikeChannelFailure(String msg) {

    }

    @Override
    public void onLikeChannelResponse(RespDto responce) {
        Boolean result = (Boolean) responce.getData();
        if (result) {
            if (dataList != null && dataList.size() > 0) {
                MainChannelItem clickItem = (MainChannelItem) dataList.get(likePosition);
                MainChannelItem.LikeDto likeDto = clickItem.getLikeDto();
                if (likeDto == null) {
                    return;
                }
                likeDto.setRefresh(true);
                ArrayList<UserLikesItem> userLikesItem = likeDto.getUserLikes();
                if (userLikesItem == null) {
                    userLikesItem = new ArrayList<UserLikesItem>();
                    likeDto.setUserLikes(userLikesItem);
                }
                UserLikesItem my = new UserLikesItem();
                my.setUid(MoreUser.getInstance().getUid());
                my.setNickName(MoreUser.getInstance().getNickname());
                my.setThumb(MoreUser.getInstance().getThumb());
                userLikesItem.add(0, my);
                likeDto.setLikeTimes(likeDto.getLikeTimes() + 1);
                notifyDataSetChanged();
            }
            Log.d("dddd", "channel scuess");
        } else if (dataList != null && dataList.size() > 0) {
            MainChannelItem clickItem = (MainChannelItem) dataList.get(likePosition);
            MainChannelItem.LikeDto likeDto = clickItem.getLikeDto();
            likeDto.setRefresh(true);
            ArrayList<UserLikesItem> userLikesItem = likeDto.getUserLikes();
            if (userLikesItem != null) {
                for (int i = 0; i < userLikesItem.size(); i++) {
                    if (userLikesItem.get(i) != null && userLikesItem.get(i).getUid() > 0
                            && userLikesItem.get(i).getUid() == MoreUser.getInstance().getUid()) {
                        userLikesItem.remove(i);
                        break;
                    }
                }
            }
            likeDto.setLikeTimes(likeDto.getLikeTimes() - 1);
            notifyDataSetChanged();
            Log.d("dddd", "channel cancel scuess");
        }
        MChannelCommentEvent event = new MChannelCommentEvent();
        event.setClicked(result);
        if (dataList != null && dataList.size() > 0 && dataList.get(likePosition) != null) {
            MainChannelItem item = (MainChannelItem) dataList.get(likePosition);
            event.setSid(item.getInformationId());
        }
        EventBus.getDefault().post(event);
    }

    @Override
    public void onSendCommentFailure(String msg) {
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
            AppUtils.pushLeftActivity(context, UseLoginActivity.class);
            return;
        }
    }

    @Override
    public void onSendCommentResponse(RespDto response) {
        if (mCommentWindow!=null&&mCommentWindow.isShowing()) {
            mCommentWindow.dismiss();
            mCommentWindow = null;
        }
        if (myCommentContent != null && myCommentContent.length() > 0) {
            MainChannelItem clickItem = (MainChannelItem) dataList.get(commentRefreshPosition);
            ArrayList<Comments> comArray =
                    clickItem.getComments();
            if (comArray == null) {
                comArray = new ArrayList<Comments>();
            }
            Comments myComments = new Comments();
            myComments.setToUserId(mComment.getToUserId());
            int cid;
            try {
                cid = Integer.parseInt(response.getData().toString());
            } catch (Exception e) {
                Logger.i("zune:", "e = " + e);
                cid = 0;
            }
            myComments.setCid(cid);
            myComments.setToNickName(mComment.getToNickName());
            myComments.setToUserThumb(mComment.getToUserThumb());
            myComments.setContent(myCommentContent);
            myComments.setFromNickname(MoreUser.getInstance().getNickname());
            comArray.add(0, myComments);
            clickItem.setComments(comArray);
            clickItem.setCommentCount(clickItem.getCommentCount() <= 0 ? 1 : clickItem.getCommentCount() + 1);
            dataList.set(commentRefreshPosition, clickItem);
            MChannelCommentEvent event = new MChannelCommentEvent();
            HeadlineComment comment = new HeadlineComment();
            if (myComments.getToUserId() > 0)
                comment.setToUserId(myComments.getToUserId());
            comment.setContent(myComments.getContent());
            comment.setCreateTime(System.currentTimeMillis());
            comment.setFromNickname(MoreUser.getInstance().getNickname());
            comment.setToNickName(myComments.getToNickName());
            comment.setFromUserThumb(MoreUser.getInstance().getThumb());
            comment.setToUserThumb(myComments.getToUserThumb());
            comment.setUserId(myComments.getUserId());
            comment.setClicked(false);
            comment.setCid(cid);
            event.setComment(comment);
            event.setSid(clickItem.getInformationId());
            EventBus.getDefault().post(event);
            notifyDataSetChanged();
        }
    }


    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!TextUtils.isEmpty(commentView.getText().toString().trim())) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    myCommentContent = commentView.getText().toString().trim();
                    sendComment(commentView.getText().toString().trim());
                    commentView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    inputMethodManager.showSoftInput(commentView, InputMethodManager.RESULT_SHOWN);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                    commentView.setFocusable(true);
                    commentView.setFocusableInTouchMode(true);
                    commentView.requestFocus();
                }
                commentView.setText("");
                return true;
            }
            return false;
        }
    };

    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null && s.toString().split("：") != null && s.toString().split("：").length > 1) {
                commentView.setTextColor(Color.parseColor("#333333"));
            } else if (s != null && !s.toString().contains("：")) {
                commentView.setTextColor(Color.parseColor("#333333"));
            } else {
                commentView.setTextColor(Color.parseColor("#999999"));
            }
            commentView.setSelection(commentView.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void sendComment(String comment) {

        //Todo 发评论
        HeadlineSendComment mSendComment = new HeadlineSendComment();
        String version = com.moreclub.moreapp.ucenter.constant.Constants.APP_version;
        mSendComment.setAppVersion(version);
        mSendComment.setTimestamp((int) (System.currentTimeMillis() / 1000));
        mSendComment.setUid(MoreUser.getInstance().getUid());
        String hint = commentView.getHint().toString();
        if (TextUtils.equals(hint, "留个言评论下吧……")) {
            mSendComment.setContent(comment);
            mSendComment.setPostId(postID);
            mSendComment.setToNickName("");
        } else {
            try {
                mSendComment.setContent(comment);
                mSendComment.setPostId(postID);
                mSendComment.setToNickName(fromNickName);
                mSendComment.setToUserId(toUid);
            } catch (Exception e) {
                mSendComment.setContent(comment);
                mSendComment.setPostId(postID);
            }
        }
        ((MainChannelAttentionDataBinder) mPresenter).onSendComment(mSendComment);

    }

    private void setupCommentText(TextView commentT, final Comments comentItem, final MainChannelItem item, final int position) {
        final StringBuilder commentContent = new StringBuilder();
        if (comentItem != null && comentItem.getFromNickname() != null) {
            commentContent.append(comentItem.getFromNickname());
            if (comentItem.getToNickName() != null) {
                commentContent.append("  @");
                commentContent.append(comentItem.getToNickName());
            }
            commentContent.append("  ");
            commentContent.append(comentItem.getContent());
        } else {
            commentContent.append(comentItem.getContent());
        }

        SpannableStringBuilder spannable = new SpannableStringBuilder(commentContent.toString());
        int startIndex = 0;
        int endIndex = 0;
        if (comentItem.getFromNickname() != null) {
            endIndex = comentItem.getFromNickname().length() + 2;
        }
        int color = R.color.mainItemTagTitle;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 个人主页
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    return;
                }
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", "" + comentItem.getUserId());
                ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
            }
        };
        try {
            spannable.setSpan(new UserDetailsListAdapter.Clickable(color, listener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        try {
            spannable.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        int toStartIndex = endIndex;
        int toEndIndex = 0;
        View.OnClickListener listner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 回复某人
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    return;
                }
                showCommentPopuWindow(item.getInformationId(), comentItem.getFromNickname(), comentItem.getUserId(), position);
                mComment = new Comments();
                mComment.setToNickName(comentItem.getFromNickname());
                mComment.setToUserId(comentItem.getUserId());
                mComment.setToUserThumb(comentItem.getFromUserThumb());
            }
        };
        try {
            spannable.setSpan(new UserDetailsListAdapter.Clickable(R.color.searchEntryTagText, listner), endIndex, commentContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        if (comentItem.getToNickName() != null) {
            toStartIndex = endIndex;
            toEndIndex = 0;
            if (comentItem.getToNickName() != null) {
                toEndIndex = toStartIndex + comentItem.getToNickName().length() + 2;
            }
            int toColor = R.color.shit_yellow;
            View.OnClickListener reCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 回复某人
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    showCommentPopuWindow(item.getInformationId(), comentItem.getFromNickname(), comentItem.getUserId(), position);
                    mComment = new Comments();
                    mComment.setToNickName(comentItem.getFromNickname());
                    mComment.setToUserId(comentItem.getUserId());
                    mComment.setToUserThumb(comentItem.getFromUserThumb());
                }
            };
            try {
                spannable.setSpan(new UserDetailsListAdapter.Clickable(toColor, reCommentListener), toStartIndex, toEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {

            }
        }
        commentT.setText(spannable);
        commentT.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class Clickable extends ClickableSpan {
        private View.OnClickListener mListener;
        private int color;

        public Clickable(int color, View.OnClickListener l) {
            mListener = l;
            this.color = color;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(MainApp.getContext().getResources().getColor(color));
        }

    }


    static class ItemChannelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header_img)
        ImageView headerImage;
        @BindView(R.id.header_name)
        TextView headerName;
        @BindView(R.id.header_tag)
        ImageView headerTag;
        @BindView(R.id.header_time)
        TextView headerTime;
        @BindView(R.id.attention_btn)
        TextView attentionBtn;
        @BindView(R.id.channel_des)
        TextView channelDes;
        //////////////////////////////////////////////////////////
        @BindView(R.id.picgridView)
        RelativeLayout picgridView;
        @BindView(R.id.nine_layout)
        RelativeLayout nineLayout;
        @BindView(R.id.one_layout)
        ImageView oneLayout;
        @BindView(R.id.four_layout)
        RelativeLayout fourLayout;
        @BindView(R.id.top_left_img)
        ImageView topLeftImg;
        @BindView(R.id.top_right_img)
        ImageView topRightImg;
        @BindView(R.id.bottom_left_img)
        ImageView bottomLeftImg;
        @BindView(R.id.bottom_right_img)
        ImageView bottomRightImg;
        @BindView(R.id.nine_layout1)
        ImageView nine_layout1;
        @BindView(R.id.nine_layout2)
        ImageView nine_layout2;
        @BindView(R.id.nine_layout3)
        ImageView nine_layout3;
        @BindView(R.id.nine_layout4)
        ImageView nine_layout4;
        @BindView(R.id.nine_layout5)
        ImageView nine_layout5;
        @BindView(R.id.nine_layout6)
        ImageView nine_layout6;
        @BindView(R.id.nine_layout7)
        ImageView nine_layout7;
        @BindView(R.id.nine_layout8)
        ImageView nine_layout8;
        @BindView(R.id.nine_layout9)
        ImageView nine_layout9;
        ////////////////////////////////////////
        @BindView(R.id.comment_text_1)
        TextView comment_text_1;
        @BindView(R.id.comment_text_2)
        TextView comment_text_2;
        @BindView(R.id.comment_text_3)
        TextView comment_text_3;
        //////////////////////////////////////////////////
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.comment_btn)
        LinearLayout commentBtn;
        @BindView(R.id.comment_list_layout)
        LinearLayout ll_comment;
        @BindView(R.id.comment_count)
        TextView commentCount;
        @BindView(R.id.like_btn)
        LinearLayout likeBtn;
        @BindView(R.id.like_count)
        TextView likeCount;
        @BindView(R.id.like)
        ImageView like;
        @BindView(R.id.like_people_layout)
        LinearLayout likePeopleLayout;
        @BindView(R.id.go_all_comment_btn)
        TextView goAllCommentBtn;

        public ItemChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemActivityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_activities_title)
        TextView tv_activities_title;

        @BindView(R.id.tv_activities_desc)
        TextView tv_activities_desc;

        @BindView(R.id.tv_activities_location)
        TextView tv_activities_location;

        @BindView(R.id.tv_activities_date)
        TextView tv_activities_date;

        @BindView(R.id.iv_activity_logo)
        ImageView iv_activity_logo;

        @BindView(R.id.iv_activities_collect)
        ImageView iv_activities_collect;


        @BindView(R.id.tv_activities_deadline)
        TextView tv_activities_deadline;

        @BindView(R.id.iv_activities_picture)
        ImageView iv_activities_picture;

        @BindView(R.id.root_view_activities)
        RelativeLayout root_view_activities;

        public ItemActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemNoDataViewHolder extends RecyclerView.ViewHolder {

        public ItemNoDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String formatTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        String minute = format.format(date);
        return minute;
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

}
