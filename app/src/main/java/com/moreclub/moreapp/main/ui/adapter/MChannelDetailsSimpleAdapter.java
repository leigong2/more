package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.main.model.MChannelGoodUser;
import com.moreclub.moreapp.main.ui.activity.MChannelCommentActivity;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-08-02-0002.
 */

public class MChannelDetailsSimpleAdapter extends RecyclerView.Adapter {

    private List<String> userName;
    private int sid;
    private Context context;
    private List<Object> datas;
    private long nowTime;
    private long oldTime;

    public MChannelDetailsSimpleAdapter(Context context, List<Object> datas, int sid, List<String> userName) {
        this.datas = datas;
        this.context = context;
        this.sid = sid;
        this.userName = userName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        RecyclerView.ViewHolder holder = null;
        if (viewType == 0) {
            inflate = LayoutInflater.from(context).inflate(R.layout.headline_comment_item, parent, false);
            holder = new CommentViewHolder(inflate);
        } else if (viewType == 1) {
            inflate = LayoutInflater.from(context).inflate(R.layout.mchannel_good_item, parent, false);
            holder = new GoodViewHolder(inflate);
        } else if (viewType == 2) {
            inflate = LayoutInflater.from(context).inflate(R.layout.mchannel_default_item, parent, false);
            holder = new DefaultViewHolder(inflate);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) return;
        if (holder instanceof CommentViewHolder) {
            final HeadlineComment headlineComment = (HeadlineComment) datas.get(position);
            if (headlineComment != null && !TextUtils.isEmpty(headlineComment.getFromUserThumb()))
                Glide.with(context)
                        .load(headlineComment.getFromUserThumb())
                        .dontAnimate()
                        .into(((CommentViewHolder) holder).civHead);
            else if (headlineComment != null)
                ((CommentViewHolder) holder).civHead.setImageResource(R.drawable.profilephoto_small);
            ((CommentViewHolder) holder).tvName.setText(headlineComment.getFromNickname());
            final StringBuilder content = new StringBuilder(headlineComment.getContent());
            StringBuilder temp = new StringBuilder();
            int endIndex = 0;
            if (headlineComment.getToUserId() > 0 && headlineComment.getToNickName() != null
                    && !headlineComment.getToNickName()
                    .equals(userName == null ? "" : userName.size() == 0 ? "" : userName.get(0))) {
                temp.append("回复 ").append(headlineComment.getToNickName()).append("：").append(content);
                endIndex = "回复 ".length() + headlineComment.getToNickName().length() + "：".length();
            } else {
                temp.append(content);
            }
            SpannableStringBuilder spannable = new SpannableStringBuilder(temp);
            int startIndex = 0;
            int color = Color.parseColor("#DDB544");
            spannable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((CommentViewHolder) holder).tvContent.setText(spannable);
            String format = getStringTime(headlineComment.getCreateTime());
            ((CommentViewHolder) holder).tvCreateTime.setText(format);
            ((CommentViewHolder) holder).like.setImageResource(headlineComment.isClicked() ? R.drawable.like2 : R.drawable.like);
            ((CommentViewHolder) holder).tvLikeCount.setText(headlineComment.getLikeTimes() > 0
                    ? headlineComment.getLikeTimes() + "" : "");
            ((CommentViewHolder) holder).tvLikeCount.setTextColor(headlineComment.isClicked()
                    ? Color.parseColor("#DDB544") : Color.parseColor("#999999"));
            ((CommentViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowTime = System.currentTimeMillis();
                    if (nowTime - oldTime > 1000) {
                        oldTime = System.currentTimeMillis();
                        setGood(holder, headlineComment);
                    }
                }
            });
            ((CommentViewHolder) holder).rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    if (context instanceof MChannelDetailsAcitivy) {
                        MChannelDetailsAcitivy acitivy = (MChannelDetailsAcitivy) context;
                        acitivy.onClickReplyListener(headlineComment);
                    } else if (context instanceof MChannelCommentActivity) {
                        MChannelCommentActivity acitivy = (MChannelCommentActivity) context;
                        acitivy.onClickReplyListener(headlineComment);
                    }
                }
            });
            ((CommentViewHolder) holder).civHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    if (headlineComment.getUserId() != 0) {
                        ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                        intent_merchant.putExtra("toUid", headlineComment.getUserId() + "");
                        ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
                    }
                }
            });
        } else if (holder instanceof GoodViewHolder) {
            MChannelGoodUser user = (MChannelGoodUser) datas.get(position);
            if (user != null && !TextUtils.isEmpty(user.getThumb()))
                Glide.with(context)
                        .load(user.getThumb())
                        .dontAnimate()
                        .into(((GoodViewHolder) holder).civUserThumb);
            else if (user != null)
                ((GoodViewHolder) holder).civUserThumb.setImageResource(R.drawable.profilephoto_small);
            ((GoodViewHolder) holder).tvUserName.setText(user.getNickName());
            ((GoodViewHolder) holder).rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (datas == null || datas.get(position) == null || ((MChannelGoodUser) datas.get(position)).getUid() <= 0)
                        return;
                    if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                            context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                    intent_merchant.putExtra("toUid", ((MChannelGoodUser) datas.get(position)).getUid() + "");
                    ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
                }
            });
        } else if (holder instanceof DefaultViewHolder) {
            int defaultHeight = ScreenUtil.getScreenHeight(context) / 2;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, defaultHeight);
            ((DefaultViewHolder) holder).itemView.setLayoutParams(lp);
            if ((Boolean) (datas.get(0))) {
                ((DefaultViewHolder) holder).tvNoComment.setVisibility(View.VISIBLE);
                Drawable topDrawable = context.getResources().getDrawable(R.drawable.comment_sofa);
                topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                ((DefaultViewHolder) holder).tvNoComment.setCompoundDrawables(null, topDrawable, null, null);
                ((DefaultViewHolder) holder).tvNoComment.setCompoundDrawablePadding(ScreenUtil.dp2px(context, 10));
                ((DefaultViewHolder) holder).tvNoComment.setText("就等你来评论");
            } else {
                ((DefaultViewHolder) holder).tvNoComment.setVisibility(View.VISIBLE);
                ((DefaultViewHolder) holder).tvNoComment.setText("有意思就点赞");
                Drawable topDrawable = context.getResources().getDrawable(R.drawable.like_first);
                topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                ((DefaultViewHolder) holder).tvNoComment.setCompoundDrawables(null, topDrawable, null, null);
                ((DefaultViewHolder) holder).tvNoComment.setCompoundDrawablePadding(ScreenUtil.dp2px(context, 10));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (datas != null)
            return datas.size();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (datas != null) {
            Object o = datas.get(position);
            if (o instanceof HeadlineComment) {
                return 0;
            } else if (o instanceof MChannelGoodUser) {
                return 1;
            } else if (o instanceof Boolean) {
                return 2;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_head)
        CircleImageView civHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.like)
        ImageView like;
        @BindView(R.id.tv_like_count)
        TextView tvLikeCount;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class GoodViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_user_thumb)
        CircleImageView civUserThumb;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        public GoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_no_comment)
        TextView tvNoComment;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void setGood(final RecyclerView.ViewHolder holder, final HeadlineComment headlineComment) {
        Long uid = MoreUser.getInstance().getUid();
        if (headlineComment.getCid() > 0 && uid != null) {
            if (uid.equals(0L)) {
                AppUtils.pushLeftActivity(context, UseLoginActivity.class);
            } else {
                String token = MoreUser.getInstance().getAccess_token();
                Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
                    @Override
                    public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                        if (response != null && response.body() != null && response.body().getData() != null) {
                            if (response.body().getData()) {
                                Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                                ((CommentViewHolder) holder).like.setImageResource(headlineComment.isClicked() ? R.drawable.like2 : R.drawable.like);
                                int count = Integer.parseInt(TextUtils.isEmpty(((CommentViewHolder) holder).tvLikeCount.getText().toString())
                                        ? "0" : ((CommentViewHolder) holder).tvLikeCount.getText().toString()) + 1;
                                ((CommentViewHolder) holder).tvLikeCount.setText(count + "");
                                ((CommentViewHolder) holder).tvLikeCount.setTextColor(Color.parseColor("#DDB544"));
                                ((CommentViewHolder) holder).like.setImageResource(R.drawable.like2);
                            } else {
                                int count = Integer.parseInt(TextUtils.isEmpty(((CommentViewHolder) holder).tvLikeCount.getText().toString())
                                        ? "0" : ((CommentViewHolder) holder).tvLikeCount.getText().toString()) - 1;
                                ((CommentViewHolder) holder).tvLikeCount.setText(count > 0 ? count + "" : "");
                                ((CommentViewHolder) holder).tvLikeCount.setTextColor(Color.parseColor("#999999"));
                                ((CommentViewHolder) holder).like.setImageResource(R.drawable.like);
                                Toast.makeText(context, "取消点赞成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                        Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                    }
                };
                ApiInterface.ApiFactory.createApi(token).onSetPersonGood(uid, headlineComment.getCid(), sid).enqueue(callback);
            }
        }
    }

    private String getStringTime(long createTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(new Date(createTime));
    }
}
