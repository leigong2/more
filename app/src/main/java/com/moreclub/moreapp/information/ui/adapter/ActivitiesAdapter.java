package com.moreclub.moreapp.information.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.util.UserCollect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-05-18.
 */

public class ActivitiesAdapter extends RecyclerAdapter<ActivityData.ActivityItem> {
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    public ActivitiesAdapter(Context context, int layoutId, List<ActivityData.ActivityItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final ActivityData.ActivityItem activityItem) {
        boolean collect = activityItem.isCollect();
        holder.setText(R.id.tv_activities_title, "           " + activityItem.getTitle());
        holder.setText(R.id.tv_activities_desc, activityItem.getIntroduction());
        holder.setText(R.id.tv_activities_location, activityItem.getMerchantName());
        holder.setText(R.id.tv_activities_date, formatDay(activityItem.getHoldingType(), activityItem.getHoldingTimes(), activityItem.getStartTime())
                + formatTime(activityItem.getStartTime()) + "-" + formatTime(activityItem.getEndTime()));
        if (activityItem.getType() == 1 || activityItem.getType() == 5) {
            holder.setImageResource(R.id.iv_activity_logo, R.drawable.activity_list_live);
        } else if (activityItem.getType() == 2 || activityItem.getType() == 6) {
            holder.setImageResource(R.id.iv_activity_logo, R.drawable.activity_list_party);
        }
        if (collect) {
            ((ImageView) holder.getView(R.id.iv_activities_collect)).setImageResource(R.drawable.collect_fav_highlight);
        } else {
            ((ImageView) holder.getView(R.id.iv_activities_collect)).setImageResource(R.drawable.collect_fav);
        }
        TextView tvDeadLine = holder.getView(R.id.tv_activities_deadline);
        if (activityItem.getEndTime() > System.currentTimeMillis()) {
            holder.setText(R.id.tv_activities_deadline, "");
            tvDeadLine.setVisibility(View.GONE);
        } else {
            holder.setText(R.id.tv_activities_deadline, "已结束");
            tvDeadLine.setVisibility(View.VISIBLE);
        }

        Glide.with(mContext)
                .load(activityItem.getBannerPhoto())
                .dontAnimate()
                .into((ImageView) holder.getView(R.id.iv_activities_picture));
        View.OnClickListener collectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**zune:收藏活动**/
                collect(activityItem, holder);
            }
        };
        holder.setOnClickListener(R.id.iv_activities_collect, collectListener);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**zune:进入活动详情**/
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, LiveActivity.class);
                intent.putExtra(Constants.KEY_ACT_ID, activityItem.getActivityId());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        };
        holder.setOnClickListener(R.id.root_view_activities, listener);
    }

    private void collect(final ActivityData.ActivityItem activityItem, final RecyclerViewHolder holder) {
        new UserCollect(mContext,
                activityItem.getActivityId() + "", UserCollect.COLLECT_TYPE.ACTIVITY_COLLECT.ordinal(),
                activityItem.isCollect(), new UserCollect.CollectCallBack() {
            @Override
            public void collectSuccess() {
                activityItem.setCollect(!activityItem.isCollect());
                animateHeartButton((ImageView) holder.getView(R.id.iv_activities_collect), activityItem);
            }

            @Override
            public void collectFails() {
                Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateHeartButton(final ImageView btnLike, final ActivityData.ActivityItem item) {
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
                if (item.isCollect()) {
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