package com.moreclub.moreapp.information.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.main.ui.activity.MChannelCommentActivity;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.main.ui.adapter.MChannelDetailsSimpleAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-17.
 */

public class HeadlineCommentAdapter extends RecyclerAdapter<HeadlineComment> {

    private int sid;
    private long nowTime;
    private long oldTime;

    public HeadlineCommentAdapter(Context context, int layoutId, List<HeadlineComment> datas, int sid) {
        super(context, layoutId, datas);
        this.sid = sid;
        oldTime = System.currentTimeMillis();
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final HeadlineComment headlineComment) {
        CircleImageView civHead = holder.getView(R.id.civ_head);
        if (headlineComment != null && !TextUtils.isEmpty(headlineComment.getFromUserThumb()))
            Glide.with(mContext)
                    .load(headlineComment.getFromUserThumb())
                    .dontAnimate()
                    .into(civHead);
        else if (headlineComment != null)
            civHead.setImageResource(R.drawable.profilephoto_small);
        holder.setText(R.id.tv_name, headlineComment.getFromNickname());
        StringBuilder content = new StringBuilder(headlineComment.getContent());
        StringBuilder temp = new StringBuilder();
        int endIndex = 0;
        if (headlineComment.getToUserId() > 0 && headlineComment.getToNickName() != null) {
            temp.append("回复 ").append(headlineComment.getToNickName()).append("：").append(content);
            endIndex = "回复 ".length() + headlineComment.getToNickName().length() + "：".length();
        } else {
            temp.append(content);
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(temp);
        int startIndex = 0;
        int color = Color.parseColor("#DDB544");
        spannable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) holder.getView(R.id.tv_content)).setText(spannable);
        String format = getStringTime(headlineComment.getCreateTime());
        holder.setText(R.id.tv_create_time, format);
        holder.setImageResource(R.id.like
                , headlineComment.isClicked() ? R.drawable.like2 : R.drawable.like);
        holder.setText(R.id.tv_like_count, headlineComment.getLikeTimes() > 0
                ? headlineComment.getLikeTimes() + "" : "");
        holder.setTextColor(R.id.tv_like_count, headlineComment.isClicked()
                ? Color.parseColor("#DDB544") : Color.parseColor("#999999"));
        holder.setOnClickListener(R.id.like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowTime = System.currentTimeMillis();
                if (nowTime - oldTime > 1000) {
                    oldTime = System.currentTimeMillis();
                    setGood(holder, headlineComment.getCid());
                }
            }
        });
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                if (mContext instanceof MChannelDetailsAcitivy) {
                    MChannelDetailsAcitivy acitivy = (MChannelDetailsAcitivy) mContext;
                    acitivy.onClickReplyListener(headlineComment);
                } else if (mContext instanceof MChannelCommentActivity) {
                    MChannelCommentActivity acitivy = (MChannelCommentActivity) mContext;
                    acitivy.onClickReplyListener(headlineComment);
                }
            }
        });
        holder.setOnClickListener(R.id.civ_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                if (headlineComment.getUserId() != 0) {
                    ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                    intent_merchant.putExtra("toUid", headlineComment.getUserId() + "");
                    ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
                }
            }
        });
    }

    private void setGood(final RecyclerViewHolder holder, int cid) {
        Long uid = MoreUser.getInstance().getUid();
        if (cid > 0 && uid != null) {
            if (uid.equals(0L)) {
                AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
            } else {
                String token = MoreUser.getInstance().getAccess_token();
                Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
                    @Override
                    public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                        if (response != null && response.body() != null && response.body().getData() != null) {
                            if (response.body().getData()) {
                                Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                                int count = Integer.parseInt(TextUtils.isEmpty(((TextView) holder.getView(R.id.tv_like_count)).getText().toString())
                                        ? "0" : ((TextView) holder.getView(R.id.tv_like_count)).getText().toString()) + 1;
                                ((TextView) holder.getView(R.id.tv_like_count)).setText(count + "");
                                holder.setTextColor(R.id.tv_like_count, Color.parseColor("#DDB544"));
                                ((ImageView) holder.getView(R.id.like)).setImageResource(R.drawable.like2);
                            } else {
                                int count = Integer.parseInt(TextUtils.isEmpty(((TextView) holder.getView(R.id.tv_like_count)).getText().toString())
                                        ? "0" : ((TextView) holder.getView(R.id.tv_like_count)).getText().toString()) - 1;
                                ((TextView) holder.getView(R.id.tv_like_count)).setText(count > 0 ? count + "" : "");
                                holder.setTextColor(R.id.tv_like_count, Color.parseColor("#999999"));
                                ((ImageView) holder.getView(R.id.like)).setImageResource(R.drawable.like);
                                Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                        Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
                    }
                };
                ApiInterface.ApiFactory.createApi(token).onSetPersonGood(uid, cid,sid).enqueue(callback);
            }
        }
    }

    private String getStringTime(long createTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(new Date(createTime));
    }
}
