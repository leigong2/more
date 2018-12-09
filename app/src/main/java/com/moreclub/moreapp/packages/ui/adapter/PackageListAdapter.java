package com.moreclub.moreapp.packages.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.packages.model.PackageBaseInfo;
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.util.UserCollect;

import java.util.List;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageListAdapter extends RecyclerAdapter<PackageBaseInfo> {
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);


    public PackageListAdapter(Context context, int layoutId, List<PackageBaseInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final PackageBaseInfo packageBaseInfo) {

        String photos = packageBaseInfo.getPhotos();
        final boolean collected = packageBaseInfo.getCollected();
        float oldPrice = packageBaseInfo.getOldPrice();
        float price = packageBaseInfo.getPrice();
        String merchantName = packageBaseInfo.getMerchantName();
        int personNum = packageBaseInfo.getPersonNum();
        String title = packageBaseInfo.getTitle();
        String[] photoArray = photos.split(";");

        if (photoArray!=null&&photoArray.length>0){
            Glide.with(mContext)
                    .load(photoArray[0])
                    .dontAnimate()
                    .into((ImageView) holder.getView(R.id.package_image_banner_iv));

        }

        holder.setText(R.id.package_title, title);
        holder.setText(R.id.package_tag, merchantName);
        holder.setText(R.id.package_price, price+"/"+personNum+"人");
        if (collected) {
            holder.setImageResource(R.id.package_collect_button, R.drawable.collect_fav_highlight);
        } else {
            holder.setImageResource(R.id.package_collect_button, R.drawable.collect_fav);
        }

        holder.getView(R.id.package_collect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCollect(view, packageBaseInfo);
            }
        });
    }

    private void onCollect(final View view, final PackageBaseInfo info) {
        new UserCollect(mContext, info.getPid() + "",
                UserCollect.COLLECT_TYPE.PACKAGE_COLLECT.ordinal(),
                info.getCollected(),
                new UserCollect.CollectCallBack(){

            @Override
            public void collectSuccess() {
                info.setCollected(!info.getCollected());
                animateHeartButton((ImageButton) view, info);
            }

            @Override
            public void collectFails() {
            }
        });
    }


    private void animateHeartButton(final ImageButton btnLike, final PackageBaseInfo info) {
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
                if (info.getCollected()) {
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
}
