package com.moreclub.moreapp.main.ui.adapter;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.model.MerchantActivities;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.util.UserCollect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017-06-02.
 */

public class MerchantActivitiesAdapter extends RecyclerAdapter<MerchantActivity> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    public MerchantActivitiesAdapter(Context context, int layoutId, ArrayList<MerchantActivity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final MerchantActivity activityItem) {
        boolean collect = activityItem.isCollected();
        holder.setText(R.id.tv_activities_title, "           " + activityItem.getTitle());
        holder.setText(R.id.tv_activities_desc, activityItem.getIntroduction());
        holder.setText(R.id.tv_activities_location, activityItem.getTitle());
        holder.setText(R.id.tv_activities_date, format(activityItem.getStartTime()));
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
        if (activityItem.getEndTime() > System.currentTimeMillis()) {
            holder.setText(R.id.tv_activities_deadline, "");
        } else {
            holder.setText(R.id.tv_activities_deadline, "已结束");
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


    private void collect(final MerchantActivity activityItem, final RecyclerViewHolder holder) {
        new UserCollect(mContext,
                activityItem.getActivityId() + "", UserCollect.COLLECT_TYPE.ACTIVITY_COLLECT.ordinal(),
                activityItem.isCollected(), new UserCollect.CollectCallBack() {
            @Override
            public void collectSuccess() {
                activityItem.setCollected(!activityItem.isCollected());
                animateHeartButton((ImageView) holder.getView(R.id.iv_activities_collect), activityItem);
            }

            @Override
            public void collectFails() {
                Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT).show();
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
}
