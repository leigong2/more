package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.ui.adapter.MerchantCouponSupportAdater;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.ui.view.CouponCardView;
import com.moreclub.moreapp.util.ArithmeticUtils;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/6/15.
 */

public class UserCouponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum ITEM_TYPE{
        ITEM_TYPE_ONE,
        ITEM_TYPE_TIP
    }

    private List<UserCoupon> dataArray;
    private Context mContext;
    private int mLayout;

    public enum MoreOriginateType{
        ITEM_TYPE_Norom,
        MoreOriginateTypepPlatform ,
        MoreOriginateTypepBusiness
    };

    public enum MorePromotionType{
        ITEM_TYPE_Norom,
        MorePromotionTypeCut,
        MorePrmotionTypeFullCut,
        MorePromotionTypeSale
    };

    public enum MoreCouponType{
        ITEM_TYPE_Norom,
        MoreCouponTypeMore
    };

    public enum MoreCouponStatus{
        ITEM_TYPE_Norom,
        ITEM_TYPE_one,
        ITEM_TYPE_two,
        ITEM_TYPE_three,
        MoreCouponStatusUse,
        MoreCouponStatusExpire
    };

    UseListener mUseListener;
    OnItemClickListener mItemListener;

    public UserCouponAdapter(Context context, int layout, List<UserCoupon> datas) {
        dataArray = datas;
        mContext = context;
        mLayout = layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == UserCouponAdapter.ITEM_TYPE.ITEM_TYPE_ONE.ordinal()){
            View view = LayoutInflater.from( mContext).inflate(
                    mLayout,parent,false);
            return new UserCouponAdapter.ItemOneViewHolder(view);
        }else if(viewType == UserCouponAdapter.ITEM_TYPE.ITEM_TYPE_TIP.ordinal()){
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.coupon_lock_tips_layout,parent,false);
            return new UserCouponAdapter.ItemTwoViewHolder(view);
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof UserCouponAdapter.ItemOneViewHolder){
            CouponCardView cardView = ((ItemOneViewHolder)viewHolder).cardView;
            cardView.useButton.setVisibility(View.VISIBLE);
            cardView.priceTag.setVisibility(View.VISIBLE);

            final UserCoupon userCoupon = dataArray.get(position);
            if (userCoupon.getPromotionType()==
                    MerchantCouponSupportAdater.MorePromotionType.MorePromotionTypeCut.ordinal()){

                cardView
                        .promotionTextView.setText("· 直减 ·");
                cardView
                        .priceTag.setText("元");
                int par =(int) ArithmeticUtils.round(userCoupon.getParValue(),0);
                cardView
                        .priceTextView.setText(""+ par);
            } else if (userCoupon.getPromotionType()==
                    MerchantCouponSupportAdater.MorePromotionType.MorePrmotionTypeFullCut.ordinal()){

                cardView
                        .promotionTextView.setText("· 满减 ·");
                cardView
                        .priceTag.setText("元");
                int par =(int) ArithmeticUtils.round(userCoupon.getParValue(),0);
                cardView
                        .priceTextView.setText(""+par);
            } else{

                cardView
                        .promotionTextView.setText("· 折扣 ·");
                cardView
                        .priceTag.setText("折");
                cardView
                        .priceTextView.setText(""+ ArithmeticUtils.
                        round(userCoupon.getParValue()*10.0,1));
            }

            cardView.couponNameTextView.
                    setText(userCoupon.getModalName());

            cardView.lockedImageView.setVisibility(View.GONE);

            if (userCoupon.getLocked()!=null&&(userCoupon.getLocked()||userCoupon.getStatus()==
                    MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal())){
                cardView.cardBackView.
                        setBackgroundColor(ContextCompat.getColor(mContext,R.color.coupon_bg_nouse));
                cardView.cardBgView.setAlpha(0.6f);
                if (userCoupon.getStatus()==
                        MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal()) {
                    cardView.useButton.setText("  已过期  ");
                    cardView.lockedImageView.setVisibility(View.GONE);
                } else {
                    cardView.useButton.setText("  已锁定  ");
                    cardView.lockedImageView.setVisibility(View.VISIBLE);
                }

                cardView.useButton.setTextColor
                        (ContextCompat.getColor(mContext,R.color.white));
                cardView.useButton.
                        setBackgroundResource(R.drawable.merchant_coupon_use_btn_locked);
                cardView.priceTextView.
                        setTextColor(ContextCompat.getColor(mContext,R.color.white));
                cardView.priceTag.
                        setTextColor(ContextCompat.getColor(mContext,R.color.white));
            }else {

                cardView.cardBgView.setAlpha(0.85f);
                cardView.cardBackView.
                        setBackgroundColor(ContextCompat.getColor(mContext,R.color.coupon_bg_use));
                cardView.lockedImageView.setVisibility(View.GONE);
                cardView.useButton.setText("  去使用  ");
                cardView.useButton.setTextColor
                        (ContextCompat.getColor(mContext,R.color.regButton));
                cardView.useButton.
                        setBackgroundResource(R.drawable.merchant_coupon_use_btn);
                cardView.priceTextView.
                        setTextColor(ContextCompat.getColor(mContext,R.color.regButton));
                cardView.priceTag.
                        setTextColor(ContextCompat.getColor(mContext,R.color.regButton));
                cardView.useButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mUseListener!=null){
                            mUseListener.useClick(userCoupon);
                        }
                    }
                });
            }

            cardView
                    .couponTimeTextView.setText("有效期至"+
                    TimeUtils.formatChatDate2(userCoupon.getAvlStartDateTime()*1000));

            cardView.
                    couponDesTextView.setText(userCoupon.getName());



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.onItemClick(userCoupon);
                    }
                }
            });

        }else if(viewHolder instanceof UserCouponAdapter.ItemTwoViewHolder){

        }
    }

    @Override
    public int getItemViewType(int position) {

        UserCoupon item= dataArray.get(position);

        if (item.getCid()>0){
            return UserCouponAdapter.ITEM_TYPE.ITEM_TYPE_ONE.ordinal();
        } else {
            return UserCouponAdapter.ITEM_TYPE.ITEM_TYPE_TIP.ordinal();
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    public void setOnUseListener(UseListener listener){

        mUseListener = listener;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemListener = onItemClickListener;
    }

    static class ItemOneViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardView)
        CouponCardView cardView;
        public ItemOneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemTwoViewHolder extends RecyclerView.ViewHolder{
        public ItemTwoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface UseListener{

        void useClick(UserCoupon userCoupon);

    }

    public interface OnItemClickListener {
        void onItemClick(UserCoupon item);
    }
}
