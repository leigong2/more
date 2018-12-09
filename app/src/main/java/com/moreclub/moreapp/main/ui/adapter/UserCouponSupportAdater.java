package com.moreclub.moreapp.main.ui.adapter;
/**
 * Created by Captain on 2017/8/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.ui.activity.MerchantCouponDetailsActivity;
import com.moreclub.moreapp.main.ui.view.MerchantCouponModalView;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.ui.activity.UserCouponDetailsActivity;
import com.moreclub.moreapp.util.ArithmeticUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

/**
 * Created by Captain on 2017/6/14.
 */

public class UserCouponSupportAdater extends RecyclerAdapter<UserCoupon> {

    public enum MoreOriginateType {
        ITEM_TYPE_Norom,
        MoreOriginateTypepPlatform,
        MoreOriginateTypepBusiness
    }
    ;

    public enum MorePromotionType {
        ITEM_TYPE_Norom,
        MorePromotionTypeCut,
        MorePrmotionTypeFullCut,
        MorePromotionTypeSale
    }

    ;

    public enum MoreCouponType {
        ITEM_TYPE_Norom,
        MoreCouponTypeMore
    }

    ;

    public enum MoreCouponStatus {
        ITEM_TYPE_Norom,
        ITEM_TYPE_one,
        ITEM_TYPE_two,
        MoreCouponStatusOffline,// 下线
        MoreCouponStatusUse,// 使用
        MoreCouponStatusExpire,// 过期
        MoreCouponStatusNoUse// 不可用
    }

    public UserCouponSupportAdater(Context context, int layoutId,
                                   List<UserCoupon> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder,
                        final UserCoupon merchantSupportCoupon) {

        if (merchantSupportCoupon.getPromotionType() ==
                MorePromotionType.MorePromotionTypeCut.ordinal()) {

            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .promotionTextView.setText("· 直减 ·");
            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .priceTag.setText("元");
            int par = (int) ArithmeticUtils.round(merchantSupportCoupon.getParValue(), 0);
            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .priceTextView.setText("" + par);
        } else if (merchantSupportCoupon.getPromotionType() ==
                MorePromotionType.MorePrmotionTypeFullCut.ordinal()) {

            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .promotionTextView.setText("· 满减 ·");
            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .priceTag.setText("元");
            int par = (int) ArithmeticUtils.round(merchantSupportCoupon.getParValue(), 0);
            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .priceTextView.setText("" + par);
        } else {

            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .promotionTextView.setText("· 折扣 ·");
            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .priceTag.setText("折");
            ((MerchantCouponModalView) holder.getView(R.id.cardView))
                    .priceTextView.setText("" + ArithmeticUtils.round(merchantSupportCoupon.getParValue() * 10.0, 1));
        }

        ((MerchantCouponModalView) holder.getView(R.id.cardView))
                .couponNameTextView.setText(merchantSupportCoupon.getModalName());

        if (merchantSupportCoupon.getStatus() ==
                MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusUse.ordinal()) {

            ((MerchantCouponModalView) holder.getView(R.id.cardView)).cardBgView.
                    setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));

        } else if (merchantSupportCoupon.getStatus() ==
                MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal()) {

            ((MerchantCouponModalView) holder.getView(R.id.cardView)).cardBgView.
                    setBackgroundColor(ContextCompat.getColor(mContext, R.color.merchant_address));

        } else {

            ((MerchantCouponModalView) holder.getView(R.id.cardView)).cardBgView.
                    setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));

        }

        ((MerchantCouponModalView) holder.getView(R.id.cardView))
                .couponDesTextView.setText("" + merchantSupportCoupon.getName());


        ((MerchantCouponModalView) holder.getView(R.id.cardView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeCustomAnimation(mContext,
                                    R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext,
                            UserCouponDetailsActivity.class);
                    intent.putExtra("cid", "" + merchantSupportCoupon.getRid());
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());

            }
        });
    }
}
