package com.moreclub.moreapp.ucenter.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.Comments;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.event.MChannelCommentEvent;
import com.moreclub.moreapp.main.ui.activity.MChannelCommentActivity;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.TopicDetailsActivity;
import com.moreclub.moreapp.main.ui.adapter.MerchantScenePagerAdapter;
import com.moreclub.moreapp.main.ui.view.ExpandableTextView;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;
import com.moreclub.moreapp.ucenter.model.UserUgc;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserUgcDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserUgcListActivity;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.SpannableClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moreclub.common.util.TimeUtils.formatSpace;

/**
 * Created by Administrator on 2017-08-28-0028.
 */

public class MyChannelAdapter extends RecyclerAdapter<UserUgc> {

    private boolean hasFollow;
    private Comments mComment;
    public PopupWindow mPopupWindow;
    private View popupParentView;
    private ViewPagerFixed sceneViewPager;
    private AlertView photoAlertView;

    public MyChannelAdapter(Context context, int layoutId, List datas, boolean hasFollow) {
        super(context, layoutId, datas);
        this.hasFollow = hasFollow;
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final UserUgc item) {

        final int curPos = holder.getAdapterPosition() - 1;
        if (item == null)
            return;

        holder.setVisible(R.id.header_tag, false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Ugc详情
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent1 = new Intent(mContext, UserUgcDetailActivity.class);
                intent1.putExtra("sid", Long.valueOf(item.getUgcId()));
                ActivityCompat.startActivity(mContext, intent1, compat1.toBundle());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                showDeleteDialog(curPos, item);
                return false;
            }
        });
        ImageView headerImage = (ImageView) holder.getView(R.id.header_img);

        holder.setOnClickListener(R.id.iv_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 删除详情
//                holder.setVisible(R.id.rl_delete, true);
//                showDeleteDialog(curPos, item);
                showDeletePop(curPos, item);
            }
        });

        holder.setOnClickListener(R.id.rl_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid().equals(item.getUid())) {
                    holder.setVisible(R.id.rl_delete, false);
                }
            }
        });
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                if (((UserUgcListActivity) mContext).ischannel) {
                    ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                    intent_merchant.putExtra("toUid", "" + item.getUid());
                    intent_merchant.putExtra("isChannel", "true");
                    MainChannelItem.ChainMerchants merchants = null;
                    if (merchants != null) {
                        intent_merchant.putExtra("mid", merchants.getMid());
                    }
                    ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
                } else {
                    ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                    intent_merchant.putExtra("toUid", "" + item.getUid());
                    ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
                }
            }
        });
        TextView headerName = (TextView) holder.getView(R.id.header_name);
        TextView headerTime = (TextView) holder.getView(R.id.header_time);
        TextView attentionBtn = (TextView) holder.getView(R.id.attention_btn);
        if (!hasFollow && !MoreUser.getInstance().getUid().equals(item.getUid())) {
            attentionBtn.setVisibility(View.VISIBLE);
        } else {
            attentionBtn.setVisibility(View.GONE);
        }
        attentionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                v.setVisibility(View.GONE);
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0
                        && !MoreUser.getInstance().getUid().equals(item.getUid())) {
                    Callback callback = new Callback<RespDto<Boolean>>() {
                        @Override
                        public void onResponse(Call<RespDto<Boolean>> call,
                                               Response<RespDto<Boolean>> response) {
                            hasFollow = !hasFollow;
                            notifyDataSetChanged();
                            ((UserUgcListActivity) mContext).from_user_detail = true;
                        }

                        @Override
                        public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                            Log.i("zune:", "t = " + t.getMessage());
                        }
                    };
                    String token = MoreUser.getInstance().getAccess_token();
                    UserFollowParam param = new UserFollowParam();
                    param.setFriendId(item.getUid() + "");
                    param.setOwnerId(MoreUser.getInstance().getUid() + "");
                    com.moreclub.moreapp.ucenter.api.ApiInterface.ApiFactory.createApi(token).onFollowAdd(param).enqueue(callback);
                }
            }
        });
        final ExpandableTextView channelDes = holder.getView(R.id.channel_des);
        TextView addresstext = (TextView) holder.getView(R.id.address);
        addresstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getMid() > 0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", "" + item.getMid());
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        });
        RelativeLayout nineLayout = (RelativeLayout) holder.getView(R.id.nine_layout);
        ImageView oneLayout = (ImageView) holder.getView(R.id.one_layout);
        ImageView one_layout_bg = (ImageView) holder.getView(R.id.one_layout_bg);
        RelativeLayout fourLayout = (RelativeLayout) holder.getView(R.id.four_layout);
        ImageView topLeftImg = (ImageView) holder.getView(R.id.top_left_img);
        topLeftImg.setOnClickListener(channelPictureClick);
        topLeftImg.setTag(R.id.top_left_img, curPos);
        ImageView topRightImg = (ImageView) holder.getView(R.id.top_right_img);
        topRightImg.setOnClickListener(channelPictureClick);
        topRightImg.setTag(R.id.top_right_img, curPos);
        ImageView bottomLeftImg = (ImageView) holder.getView(R.id.bottom_left_img);
        bottomLeftImg.setOnClickListener(channelPictureClick);
        bottomLeftImg.setTag(R.id.bottom_left_img, curPos);
        ImageView bottomRightImg = (ImageView) holder.getView(R.id.bottom_right_img);
        bottomRightImg.setOnClickListener(channelPictureClick);
        bottomRightImg.setTag(R.id.bottom_right_img, curPos);
        ImageView nine_layout1 = (ImageView) holder.getView(R.id.nine_layout1);
        nine_layout1.setOnClickListener(channelPictureClick);
        nine_layout1.setTag(R.id.nine_layout1, curPos);
        ImageView nine_layout2 = (ImageView) holder.getView(R.id.nine_layout2);
        nine_layout2.setOnClickListener(channelPictureClick);
        nine_layout2.setTag(R.id.nine_layout2, curPos);
        ImageView nine_layout3 = (ImageView) holder.getView(R.id.nine_layout3);
        nine_layout3.setOnClickListener(channelPictureClick);
        nine_layout3.setTag(R.id.nine_layout3, curPos);
        ImageView nine_layout4 = (ImageView) holder.getView(R.id.nine_layout4);
        nine_layout4.setOnClickListener(channelPictureClick);
        nine_layout4.setTag(R.id.nine_layout4, curPos);
        ImageView nine_layout5 = (ImageView) holder.getView(R.id.nine_layout5);
        nine_layout5.setOnClickListener(channelPictureClick);
        nine_layout5.setTag(R.id.nine_layout5, curPos);
        ImageView nine_layout6 = (ImageView) holder.getView(R.id.nine_layout6);
        nine_layout6.setOnClickListener(channelPictureClick);
        nine_layout6.setTag(R.id.nine_layout6, curPos);
        ImageView nine_layout7 = (ImageView) holder.getView(R.id.nine_layout7);
        nine_layout7.setOnClickListener(channelPictureClick);
        nine_layout7.setTag(R.id.nine_layout7, curPos);
        ImageView nine_layout8 = (ImageView) holder.getView(R.id.nine_layout8);
        nine_layout8.setOnClickListener(channelPictureClick);
        nine_layout8.setTag(R.id.nine_layout8, curPos);
        ImageView nine_layout9 = (ImageView) holder.getView(R.id.nine_layout9);
        nine_layout9.setOnClickListener(channelPictureClick);
        nine_layout9.setTag(R.id.nine_layout9, curPos);

        LinearLayout likeBtn = (LinearLayout) holder.getView(R.id.like_btn);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                    final int likePosition = curPos;
/*                    ((MChannelRecommAttentionLoader) mPresenter).
                            onLikeChannel(item.getInformationId(),
                                    MoreUser.getInstance().getUid(), "likeTime1");*/
                    Callback<RespDto<Boolean>> callback
                            = new Callback<RespDto<Boolean>>() {
                        @Override
                        public void onResponse(Call<RespDto<Boolean>> call,
                                               Response<RespDto<Boolean>> response) {
                            if (response == null || response.body() == null) return;
                            Boolean result = (Boolean) response.body().getData();
                            MChannelCommentEvent event = new MChannelCommentEvent();
                            event.setClicked(result);
                            if (mDatas != null && mDatas.size() > 0 && mDatas.get(0) != null)
                                event.setSid(mDatas.get(likePosition).getUgcId());
                            EventBus.getDefault().post(event);
                            if (!result) {
                                mDatas.get(likePosition).getLikeDto().setClicked(false);
                                mDatas.get(likePosition).getLikeDto().setLikeTimes(mDatas
                                        .get(likePosition).getLikeDto().getLikeTimes() - 1);
                                List<UserUgc.LikeDtoBean.UserLikesBean> userLikes = mDatas.get(likePosition).getLikeDto().getUserLikes();
                                for (int i = 0; i < userLikes.size(); i++) {
                                    if (MoreUser.getInstance().getUid() != null &&
                                            MoreUser.getInstance().getUid().equals(userLikes.get(i).getUid())) {
                                        userLikes.remove(i);
                                        break;
                                    }
                                }
                                mDatas.get(likePosition).getLikeDto().setUserLikes(userLikes);
                            } else {
                                mDatas.get(likePosition).getLikeDto().setClicked(true);
                                mDatas.get(likePosition).getLikeDto().setLikeTimes(mDatas
                                        .get(likePosition).getLikeDto().getLikeTimes() + 1);
                                List<UserUgc.LikeDtoBean.UserLikesBean> userLikes = mDatas.get(likePosition).getLikeDto().getUserLikes();
                                if (userLikes == null) {
                                    userLikes = new ArrayList<>();
                                }
                                UserUgc.LikeDtoBean.UserLikesBean userLikesBean = new UserUgc.LikeDtoBean.UserLikesBean();
                                userLikesBean.setNickName(MoreUser.getInstance().getNickname());
                                userLikesBean.setThumb(MoreUser.getInstance().getThumb());
                                userLikesBean.setUid(MoreUser.getInstance().getUid());
                                userLikes.add(0, userLikesBean);
                                mDatas.get(likePosition).getLikeDto().setUserLikes(userLikes);
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

                        }
                    };
                    String token = MoreUser.getInstance().getAccess_token();
                    ApiInterface.ApiFactory.createApi(token).onLikeChannel(item.getUgcId()
                            , MoreUser.getInstance().getUid(), "likeTime1").enqueue(callback);
                    ((UserUgcListActivity) mContext).from_user_detail = true;
                } else {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                }
            }
        });

        int likeHeaderWidth = ScreenUtil.dp2px(mContext, 24);
        int likeHeaderTop = ScreenUtil.dp2px(mContext, 8);
        LinearLayout likePeopleLayout = (LinearLayout) holder.getView(R.id.like_people_layout);

        UserUgc.LikeDtoBean likeDto = item.getLikeDto();
        List<UserUgc.LikeDtoBean.UserLikesBean> userLikes = likeDto.getUserLikes();
//        if ((userLikes != null && userLikes.size() > 0 && likePeopleLayout.getChildCount() == 0) || (likeDto.isRefresh() && userLikes != null)) {
        likePeopleLayout.removeAllViews();
        if (userLikes != null && userLikes.size() > 0) {
            for (int i = 0; i < userLikes.size(); i++) {
                UserUgc.LikeDtoBean.UserLikesBean likesItem = userLikes.get(i);
                ImageView likeUserItemView = new ImageView(mContext);
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
                    Glide.with(mContext)
                            .load(likesItem.getThumb())
                            .dontAnimate()
                            .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
                            .placeholder(R.drawable.profilephoto_small)
                            .into(likeUserItemView);
                }
            }
        }


        TextView commentCount = (TextView) holder.getView(R.id.comment_count);
        commentCount.setText("" + item.getCommentCount());

        holder.getView(R.id.comment_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                } else {
                    showCommentPopuWindow(item.getUgcId(), MoreUser.getInstance().getNickname(), -1, curPos);
                    mComment = new Comments();
                }
            }
        });

        TextView goAllCommentBtn = holder.getView(R.id.go_all_comment_btn);
        TextView likeCount = (TextView) holder.getView(R.id.like_count);
        ImageView like = holder.getView(R.id.like);
        like.setImageResource(R.drawable.like);
        if (item.getLikeDto().getUserLikes() != null) {
            int likesSize = item.getLikeDto().getUserLikes().size();
            for (int i = 0; i < likesSize; i++) {
                if (item.getLikeDto().getUserLikes().get(i).getUid() == MoreUser.getInstance().getUid()) {
                    like.setImageResource(R.drawable.like2);
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

        TextView commentText1 = (TextView) holder.getView(R.id.comment_text_1);
        TextView commentText2 = (TextView) holder.getView(R.id.comment_text_2);
        TextView commentText3 = (TextView) holder.getView(R.id.comment_text_3);
        commentText1.setVisibility(View.GONE);
        commentText2.setVisibility(View.GONE);
        commentText3.setVisibility(View.GONE);
        LinearLayout ll_comments = (LinearLayout) holder.getView(R.id.comment_list_layout);
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

        Glide.with(mContext)
                .load(item.getThumb())
                .dontAnimate()
                .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
                .into(headerImage);

        headerName.setText(item.getNickName());
        headerTime.setText(formatSpace(item.getCreateTime()));
        if (!TextUtils.isEmpty(item.getContent())) {
            setChannelDetailExpandText(item, channelDes,item.getContent());
        }
        holder.setVisible(R.id.tv_all_content, false);
        List<String> prefeerPictrue = item.getPictures();
        int size;
        if (prefeerPictrue != null) {
            size = prefeerPictrue.size();
        } else {
            size = 0;
        }
        String prefeerPictrues[] = new String[size];
        String postUrl;
        if (prefeerPictrue != null) {
            for (int i = 0; i < prefeerPictrue.size(); i++) {
                prefeerPictrues[i] = prefeerPictrue.get(i);
            }
            postUrl = prefeerPictrues[0];
            if (prefeerPictrues.length == 0) {
                holder.setVisible(R.id.picgridView, false);
                oneLayout.setVisibility(View.GONE);
                one_layout_bg.setVisibility(View.GONE);
                nineLayout.setVisibility(View.GONE);
                fourLayout.setVisibility(View.GONE);
            } else if (prefeerPictrues.length == 1) {
                holder.setVisible(R.id.picgridView, true);
                oneLayout.setVisibility(View.VISIBLE);
                one_layout_bg.setVisibility(View.VISIBLE);
                nineLayout.setVisibility(View.GONE);
                fourLayout.setVisibility(View.GONE);
                final String onePic = prefeerPictrues[0];
                Glide.with(mContext)
                        .load(prefeerPictrues[0])
                        .dontAnimate()
                        .into(oneLayout);
                oneLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> pic = new ArrayList<String>();
                        pic.add(onePic);
                        showAllSubject(pic, 0);
                    }
                });
            } else if (prefeerPictrues.length == 4) {
                holder.setVisible(R.id.picgridView, true);
                oneLayout.setVisibility(View.GONE);
                one_layout_bg.setVisibility(View.GONE);
                nineLayout.setVisibility(View.GONE);
                fourLayout.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(prefeerPictrues[0])
                        .dontAnimate()
                        .placeholder(R.drawable.default_more)
                        .into(topLeftImg);
                Glide.with(mContext)
                        .load(prefeerPictrues[1])
                        .dontAnimate()
                        .placeholder(R.drawable.default_more)
                        .into(topRightImg);
                Glide.with(mContext)
                        .load(prefeerPictrues[2])
                        .dontAnimate()
                        .placeholder(R.drawable.default_more)
                        .into(bottomLeftImg);
                Glide.with(mContext)
                        .load(prefeerPictrues[3])
                        .dontAnimate()
                        .placeholder(R.drawable.default_more)
                        .into(bottomRightImg);
            } else {
                holder.setVisible(R.id.picgridView, true);
                oneLayout.setVisibility(View.GONE);
                one_layout_bg.setVisibility(View.GONE);
                nineLayout.setVisibility(View.VISIBLE);
                fourLayout.setVisibility(View.GONE);

                for (int i = 0; i < prefeerPictrues.length; i++) {
                    if (i < 9) {
                        ImageView itemNine = nineArray.get(i);
                        itemNine.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(prefeerPictrues[i])
                                .dontAnimate()
                                .placeholder(R.drawable.default_more)
                                .into(itemNine);
                    }
                }
            }
        } else {
            postUrl = "";
            holder.setVisible(R.id.picgridView, false);
            oneLayout.setVisibility(View.GONE);
            one_layout_bg.setVisibility(View.GONE);
            nineLayout.setVisibility(View.GONE);
            fourLayout.setVisibility(View.GONE);
        }
        final String finalPostUrl = postUrl;
        likePeopleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to do...评论
                if (item != null && item.getUgcId() > 0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, MChannelCommentActivity.class);
                    intent.putExtra("sid", item.getUgcId());
                    intent.putExtra("type", 1);
                    intent.putExtra("prefeerContent", item.getContent());
                    intent.putExtra("postUrl", finalPostUrl);
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        });
        ll_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to do...评论
                if (item != null && item.getUgcId() > 0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, MChannelCommentActivity.class);
                    intent.putExtra("sid", item.getUgcId());
                    intent.putExtra("type", 1);
                    intent.putExtra("prefeerContent", item.getContent());
                    intent.putExtra("postUrl", finalPostUrl);
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        });
        goAllCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item != null && item.getUgcId() > 0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, MChannelCommentActivity.class);
                    intent.putExtra("sid", item.getUgcId());
                    intent.putExtra("type", 1);
                    intent.putExtra("prefeerContent", item.getContent());
                    intent.putExtra("postUrl", finalPostUrl);
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        });
        MainChannelItem.ChainMerchants chainMerchants = null;
        if (!TextUtils.isEmpty(item.getMerchantName())) {
            addresstext.setVisibility(View.VISIBLE);
            addresstext.setText(item.getMerchantName());
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
                setupCommentText(commentText1, comentItem, item, curPos);
            }
            if (item.getComments().size() >= 2) {
                Comments comentItem = item.getComments().get(1);
                commentText2.setVisibility(View.VISIBLE);
                setupCommentText(commentText2, comentItem, item, curPos);
            }
            if (item.getComments().size() >= 3) {
                Comments comentItem = item.getComments().get(2);
                commentText3.setVisibility(View.VISIBLE);
                setupCommentText(commentText3, comentItem, item, curPos);
            }
        }
    }

    private void setChannelDetailExpandText(final UserUgc item, final ExpandableTextView channelDes, String content) {
        final int color = R.color.shit_yellow;
        final int colorGray = R.color.mainTagName;
        final int colorContent = R.color.channel_item_content;
        final View.OnClickListener webClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent1 = new Intent(mContext, UserUgcDetailActivity.class);
                intent1.putExtra("sid", Long.parseLong(item.getUgcId() + ""));
                ActivityCompat.startActivity(mContext, intent1, compat1.toBundle());
            }
        };
        final View.OnClickListener topicClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUgc.ChainIntersBean chainInteritem = null;
                if (item.getChainInters() != null && item.getChainInters().size() > 0) {
                    chainInteritem = item.getChainInters().get(0);
                }
                String chainInterTitle = "";
                if (chainInteritem != null)
                    chainInterTitle = chainInteritem.getTitle();

                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, TopicDetailsActivity.class);
                intent.putExtra("topicTitle", chainInterTitle);
                intent.putExtra("topicDes", item.getContent());
                if (chainInteritem == null) {
                    intent.putExtra("topicId", 0);
                } else {
                    intent.putExtra("topicId", chainInteritem.getInterId());
                }
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        };
        final View.OnClickListener contentClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent1 = new Intent(mContext, UserUgcDetailActivity.class);
                intent1.putExtra("sid", Long.parseLong(item.getUgcId() + ""));
                ActivityCompat.startActivity(mContext, intent1, compat1.toBundle());
            }
        };
        if (item.getChainInters() == null || item.getChainInters().size() == 0
                || item.getChainInters().get(0) == null || item.getChainInters().get(0)
                .getTitle() == null) {
            final SpannableStringBuilder spannable = new SpannableStringBuilder();
            spannable.append(content).append(" ");
            final int startIndex = 0;
            final int secondIndex = content.length() + 1;
            Drawable d = mContext.getResources().getDrawable(R.drawable.link);
            d.setBounds(0, 0, ScreenUtil.dp2px(mContext, 14), ScreenUtil.dp2px(mContext, 14));
            final ScreenUtil.StickerSpan imageSpan = new ScreenUtil.StickerSpan(d, DynamicDrawableSpan.ALIGN_BOTTOM);
            final SpannableClick webSpan = new SpannableClick(colorGray, webClick);
            final SpannableClick contentSpan = new SpannableClick(colorContent, contentClick);
            spannable.setSpan(contentSpan, startIndex, secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            channelDes.setText(spannable);
            channelDes.setMovementMethod(LinkMovementMethod.getInstance());
            channelDes.setmSpanListener(new ExpandableTextView.ExpandSpannableListener() {

                @Override
                public void loadExpandSpan(SpannableStringBuilder ssbExpand) {
                    if (ssbExpand.toString().contains(" 链接")) {
                        if (ssbExpand.length() < secondIndex + 2) {
                            ssbExpand.setSpan(imageSpan, ssbExpand.length() - 2, ssbExpand.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ssbExpand.setSpan(webSpan, ssbExpand.length() - 1, ssbExpand.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            ssbExpand.setSpan(imageSpan, secondIndex, secondIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ssbExpand.setSpan(webSpan, secondIndex + 1, secondIndex + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }

                @Override
                public void loadShrinkSpan(SpannableStringBuilder ssbExpand) {

                }
            });
        } else {
            String title = item.getChainInters().get(0).getTitle();
            final SpannableStringBuilder spannable = new SpannableStringBuilder();
            spannable.append("#").append(title).append("#  ").append(content).append(" ");
            final int startIndex = 0;
            final int secondIndex = title.length() + 4;
            final int thirdIndex = title.length() + 5 + content.length();
            Drawable d = mContext.getResources().getDrawable(R.drawable.link);
            d.setBounds(0, 0, ScreenUtil.dp2px(mContext, 14), ScreenUtil.dp2px(mContext, 14));
            final ScreenUtil.StickerSpan imageSpan = new ScreenUtil.StickerSpan(d, DynamicDrawableSpan.ALIGN_BOTTOM);
            final SpannableClick webSpan = new SpannableClick(colorGray, webClick);
            final SpannableClick topicSpan = new SpannableClick(color, topicClick);
            final SpannableClick contentSpan = new SpannableClick(colorContent, contentClick);
            spannable.setSpan(topicSpan, startIndex, secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(contentSpan, secondIndex, thirdIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            channelDes.updateForRecyclerView(spannable, ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dp2px(mContext, 32),
//                    channelDes.getExpandState());
            channelDes.setText(spannable);
            channelDes.setMovementMethod(LinkMovementMethod.getInstance());
            Log.i("zune:", "text = " + channelDes.getText().toString());
            channelDes.setmSpanListener(new ExpandableTextView.ExpandSpannableListener() {
                @Override
                public void loadExpandSpan(SpannableStringBuilder ssbExpand) {
                    ssbExpand.setSpan(topicSpan, startIndex, secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (ssbExpand.toString().contains("链接")) {
                        if (ssbExpand.length() < thirdIndex + 2) {
                            ssbExpand.setSpan(imageSpan, ssbExpand.length() - 2, ssbExpand.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ssbExpand.setSpan(webSpan, ssbExpand.length() - 1, ssbExpand.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            ssbExpand.setSpan(imageSpan, thirdIndex, thirdIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ssbExpand.setSpan(webSpan, thirdIndex + 1, thirdIndex + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }

                @Override
                public void loadShrinkSpan(SpannableStringBuilder ssbExpand) {
                    ssbExpand.setSpan(topicSpan, startIndex, secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            });
        }
    }

    private void setExpandableText(final UserUgc item, ExpandableTextView channelDes) {
        UserUgc.ChainIntersBean chainInteritem = null;
        if (item.getChainInters() != null && item.getChainInters().size() > 0) {
            chainInteritem = item.getChainInters().get(0);
        }
        String chainInterTitle = "";
        String chainInter = "";
        if (chainInteritem != null)
            chainInterTitle = chainInteritem.getTitle();
        if (chainInterTitle != null && chainInterTitle.length() > 0) {
            chainInter = "#" + chainInterTitle + "#  " + item.getContent();
            SpannableStringBuilder spannable = new SpannableStringBuilder(chainInter);
            int startIndex = 0;
            int endIndex = chainInterTitle.length() + 2;
            int color = R.color.shit_yellow;
            View.OnClickListener topicClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserUgc.ChainIntersBean chainInteritem = null;
                    if (item.getChainInters() != null && item.getChainInters().size() > 0) {
                        chainInteritem = item.getChainInters().get(0);
                    }
                    String chainInterTitle = "";
                    if (chainInteritem != null)
                        chainInterTitle = chainInteritem.getTitle();

                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, TopicDetailsActivity.class);
                    intent.putExtra("topicTitle", chainInterTitle);
                    intent.putExtra("topicDes", item.getContent());
                    intent.putExtra("topicId", chainInteritem.getInterId());
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            };
            spannable.setSpan(new SpannableClick(color, topicClick), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            channelDes.setText(spannable);
            channelDes.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            channelDes.setText(item.getContent());
        }
    }

    private void showDeletePop(final int curPos, final UserUgc item) {
        String[] others;
        final boolean isSelf = MoreUser.getInstance().getUid().equals(item.getUid());
        if (isSelf) {
            others = new String[]{"删除", "举报"};
        } else {
            others = new String[]{"举报"};
        }
        photoAlertView = new AlertView(null, null, "取消", null, others,
                mContext, AlertView.Style.ActionSheet, new com.moreclub.common.ui.view.alertview.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (o == photoAlertView) {
                    if (position == 0) {
                        if (isSelf) {
                            deleteMyUgc(curPos, item);
                        } else {
                            Toast.makeText(mContext, "举报成功", Toast.LENGTH_SHORT).show();
                        }
                    } else if (position == 1) {
                        Toast.makeText(mContext, "举报成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (!photoAlertView.isShowing() && !((UserUgcListActivity) mContext).isFinishing())
            photoAlertView.show();
    }

    private void showDeleteDialog(final int curPos, final UserUgc item) {
        //Todo 删除详情
        View dialogLayout = View.inflate(mContext, R.layout.sign_inter_delete_dialog, null);
        TextView title = (TextView) dialogLayout.findViewById(R.id.title);
        title.setText("确定要删除这条动态吗？");
        TextView delete = (TextView) dialogLayout.findViewById(R.id.delete);
        TextView cancel = (TextView) dialogLayout.findViewById(R.id.cancel);
        final AlertDialog dialog = new AlertDialog.Builder(((UserUgcListActivity) mContext))
                .setView(dialogLayout)
                .show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 删除图片
                deleteMyUgc(curPos, item);
                dialog.dismiss();
            }
        });
    }

    private void deleteMyUgc(final int curPos, UserUgc item) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                    mDatas.remove(curPos);
                    notifyDataSetChanged();
                    ((UserUgcListActivity) mContext).from_user_detail = true;
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

            }
        };
        Long id = Long.valueOf(item.getUgcId());
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.ucenter.api.ApiInterface.ApiFactory.createApi(token).onDeleteMyUgcList(id).enqueue(callback);
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
            UserUgc positionItem = mDatas.get(curp);

            List<String> prefeerPictrue = positionItem.getPictures();
            String prefeerPictrues[] = new String[prefeerPictrue.size()];
            if (prefeerPictrue != null) {
                for (int i = 0; i < prefeerPictrue.size(); i++) {
                    prefeerPictrues[i] = prefeerPictrue.get(i);
                }
            }
            if (prefeerPictrues != null && prefeerPictrues.length > 0) {
                ArrayList<String> picArray = new ArrayList<>();
                for (int i = 0; i < prefeerPictrues.length; i++) {
                    picArray.add(prefeerPictrues[i]);
                }
                showAllSubject(picArray, picIndex);
            }
            Log.d("dddd", "" + picIndex);
        }
    };


    /**
     * 图片预览popupWindow
     *
     * @param pics
     * @param clickpos
     */
    public void showAllSubject(ArrayList<String> pics, int clickpos) {
        if (null == mPopupWindow) {
            popupParentView = LayoutInflater.from(mContext).inflate(
                    R.layout.merchant_details_scene_popuwindow, null);
            popupParentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
            int width = ScreenUtil.getScreenWidth(mContext);
            int height = ScreenUtil.getScreenHeight(mContext);

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


        mPopupWindow.showAtLocation(((Activity) mContext).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
    }

    void popupWindowSetupView(View view, ArrayList<String> pics, int clickPos) {

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(mContext, pics);

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


    private int postID;
    private String fromNickName;
    private long toUid;
    private int commentRefreshPosition;
    private String myCommentContent;
    private PopupWindow mCommentWindow;
    private View commentParentView;
    private EditText commentView;

    /**
     * channel评论
     */
    public void showCommentPopuWindow(int post, String nick, long tou, int p) {
        postID = post;
        fromNickName = nick;
        toUid = tou;
        commentRefreshPosition = p;
        if (null == mCommentWindow) {
            commentParentView = LayoutInflater.from(mContext).inflate(
                    R.layout.main_activity_comment_popu, null, false);
            commentView = (EditText) commentParentView.findViewById(R.id.et_write_comment);
            commentView.setOnKeyListener(onKeyListener);
            commentView.addTextChangedListener(textChangeListener);
            View noEditView = commentParentView.findViewById(R.id.no_edit_view);
            noEditView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCommentWindow != null && mCommentWindow.isShowing()) {
                        mCommentWindow.dismiss();
                        mCommentWindow = null;
                        commentParentView = null;
                        commentView = null;
                    }
                }
            });

            commentView.setVisibility(View.VISIBLE);
            if (toUid > 0 && toUid != MoreUser.getInstance().getUid()) {
                commentView.setHint("回复 " + fromNickName + "：");
            } else {
                commentView.setHint("留个言评论下吧……");
            }
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        mCommentWindow.showAtLocation(((Activity) mContext).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);

    }


    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!TextUtils.isEmpty(commentView.getText().toString().trim())) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    myCommentContent = commentView.getText().toString().trim();
                    sendComment(commentView.getText().toString().trim());
                    commentView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mContext, "请输入评论内容", Toast.LENGTH_SHORT).show();
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

        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (response == null || response.body() == null) return;
                if (mCommentWindow != null && mCommentWindow.isShowing()) {
                    mCommentWindow.dismiss();
                    mCommentWindow = null;
                }
                if (myCommentContent != null && myCommentContent.length() > 0) {
                    UserUgc clickItem = mDatas.get(commentRefreshPosition);
                    Comments myComments = new Comments();
                    myComments.setToUserId(mComment.getToUserId());
                    int cid;
                    try {
                        cid = Integer.parseInt(response.body().getData().toString());
                    } catch (Exception e) {
                        Logger.i("zune:", "e = " + e);
                        cid = 0;
                    }
                    myComments.setCid(cid);
                    myComments.setToNickName(mComment.getToNickName());
                    myComments.setToUserThumb(mComment.getToUserThumb());
                    myComments.setContent(myCommentContent);
                    myComments.setFromNickname(MoreUser.getInstance().getNickname());
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
                    List<Comments> comments = mDatas.get(commentRefreshPosition).getComments();
                    if (comments == null) {
                        comments = new ArrayList<>();
                    }
                    comments.add(0, myComments);
                    mDatas.get(commentRefreshPosition).setCommentCount(mDatas.get(commentRefreshPosition).getCommentCount() + 1);
                    mDatas.get(commentRefreshPosition).setComments(comments);
                    event.setComment(comment);
                    event.setSid(clickItem.getUgcId());
                    EventBus.getDefault().post(event);
                    notifyDataSetChanged();
                    ((UserUgcListActivity) mContext).from_user_detail = true;
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi(token).onSendComment(mSendComment).enqueue(callback);

    }

    private void setupCommentText(TextView commentT, final Comments comentItem, final UserUgc item, final int position) {
        final StringBuilder commentContent = new StringBuilder();
        if (comentItem != null && comentItem.getFromNickname() != null) {
            commentContent.append(comentItem.getFromNickname());
            if (comentItem.getToNickName() != null && !comentItem.getToNickName().equals(MoreUser.getInstance().getNickname())) {
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
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", "" + comentItem.getUserId());
                ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
            }
        };
        try {
            spannable.setSpan(new Clickable(color, listener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                showCommentPopuWindow(item.getUgcId(), comentItem.getFromNickname(), comentItem.getUserId(), position);
                mComment = new Comments();
                mComment.setToNickName(comentItem.getFromNickname());
                mComment.setToUserId(comentItem.getUserId());
                mComment.setToUserThumb(comentItem.getFromUserThumb());
            }
        };
        try {
            spannable.setSpan(new Clickable(R.color.searchEntryTagText, listner), endIndex, commentContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        if (comentItem.getToNickName() != null) {
            toStartIndex = endIndex;
            toEndIndex = 0;
            if (comentItem.getToNickName() != null && !comentItem.getToNickName().equals(MoreUser.getInstance().getNickname())) {
                toEndIndex = toStartIndex + comentItem.getToNickName().length() + 2;
            }
            int toColor = R.color.shit_yellow;
            View.OnClickListener reCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 回复某人
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                        return;
                    }
                    showCommentPopuWindow(item.getUgcId(), comentItem.getFromNickname(), comentItem.getUserId(), position);
                    mComment = new Comments();
                    mComment.setToNickName(comentItem.getFromNickname());
                    mComment.setToUserId(comentItem.getUserId());
                    mComment.setToUserThumb(comentItem.getFromUserThumb());
                }
            };
            try {
                spannable.setSpan(new Clickable(toColor, reCommentListener), toStartIndex, toEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
}
