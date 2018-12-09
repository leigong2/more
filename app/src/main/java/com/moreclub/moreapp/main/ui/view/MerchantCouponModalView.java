package com.moreclub.moreapp.main.ui.view;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;

/**
 * Created by Captain on 2017/6/19.
 */

public class MerchantCouponModalView  extends RelativeLayout{

    public ImageView cardBgView;
    public ImageView leftDevideView;
    public TextView promotionTextView;
    public TextView priceTextView;
    public TextView priceTag;
    public TextView couponNameTextView;
    public TextView couponDesTextView;

    RelativeLayout leftView;
    RelativeLayout rightView;

    public MerchantCouponModalView(Context context) {
        super(context);
        init(context);
    }

    public MerchantCouponModalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public MerchantCouponModalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        int screenWidth = ScreenUtil.getScreenWidth(context);
        int oldHeightPX = ScreenUtil.dp2px(context, 105);
        int oldWidthPX = ScreenUtil.dp2px(context, 328);
        int oldMargins = ScreenUtil.dp2px(context, 16);
        int oldLeftWidthPX = ScreenUtil.dp2px(context, 105);

        cardBgView = new ImageView(context);
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(
                screenWidth-(oldMargins*2),
                ((screenWidth-(oldMargins*2)) * oldHeightPX)/oldWidthPX);
        bgParams.setMargins(oldMargins, 0, 0, 0);
        cardBgView.setLayoutParams(bgParams);
        cardBgView.setImageResource(R.drawable.coupon_model_pattern);
        cardBgView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(cardBgView);

        leftView = new RelativeLayout(context);
        RelativeLayout.LayoutParams leftViewParams = new RelativeLayout.LayoutParams(
                oldLeftWidthPX*(screenWidth-(oldMargins*2))/oldWidthPX,
                ((screenWidth-(oldMargins*2)) * oldHeightPX)/oldWidthPX);
        leftView.setLayoutParams(leftViewParams);
        this.addView(leftView);

        rightView = new RelativeLayout(context);
        RelativeLayout.LayoutParams rightViewParams = new RelativeLayout.LayoutParams(
                screenWidth-(oldMargins*2)-oldLeftWidthPX*(screenWidth-(oldMargins*2))/oldWidthPX-10,
                ((screenWidth-(oldMargins*2)) * oldHeightPX)/oldWidthPX);
        rightViewParams.setMargins(oldLeftWidthPX*(screenWidth-(oldMargins*2))/oldWidthPX+10, 0, 0, 0);
        rightView.setLayoutParams(rightViewParams);
        this.addView(rightView);

        leftDevideView = new ImageView(context);
        RelativeLayout.LayoutParams devideParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ((screenWidth-(oldMargins*2)) * oldHeightPX)/oldWidthPX);
        devideParams.setMargins(oldLeftWidthPX*(screenWidth-(oldMargins*2))/oldWidthPX,0,0,0);
        leftDevideView.setLayoutParams(devideParams);
        leftDevideView.setImageResource(R.drawable.coupon_pattern_up);
        leftDevideView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(leftDevideView);

        promotionTextView = new TextView(context);
        RelativeLayout.LayoutParams promotionParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        promotionParams.setMargins(0,oldMargins,0,0);
        promotionParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        promotionTextView.setLayoutParams(promotionParams);
        promotionTextView.setTextColor(ContextCompat.getColor(context,R.color.white));
        promotionTextView.setTextSize(14);
        leftView.addView(promotionTextView);


        LinearLayout priceLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams priceLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        priceLayoutParams.setMargins(0,0,0,oldMargins);
        priceLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        priceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        priceLayout.setLayoutParams(priceLayoutParams);
        leftView.addView(priceLayout);
        priceLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        priceTextView = new TextView(context);
        RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        priceTextView.setLayoutParams(priceParams);
        priceTextView.setTextColor(ContextCompat.getColor(context,R.color.mainItemPkgBar));
        priceTextView.setTextSize(30);
        priceLayout.addView(priceTextView);

        priceTag = new TextView(context);
        RelativeLayout.LayoutParams priceTagParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        priceTag.setLayoutParams(priceTagParams);
        priceTag.setTextColor(ContextCompat.getColor(context,R.color.mainItemPkgBar));
        priceTag.setTextSize(12);
        priceLayout.addView(priceTag);

        couponNameTextView = new TextView(context);
        RelativeLayout.LayoutParams couponNameParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        couponNameParams.setMargins(oldMargins,oldMargins,oldMargins,0);
        couponNameTextView.setLayoutParams(couponNameParams);
        couponNameTextView.setTextColor(ContextCompat.getColor(context,R.color.white));
        couponNameTextView.setTextSize(18);
        rightView.addView(couponNameTextView);

        couponDesTextView = new TextView(context);
        RelativeLayout.LayoutParams couponDesParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        couponDesParams.setMargins(oldMargins,0,0,oldMargins);
        couponDesParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        couponDesTextView.setLayoutParams(couponDesParams);
        rightView.addView(couponDesTextView);
        couponDesTextView.setTextColor(ContextCompat.getColor(context,R.color.white));
        couponDesTextView.setTextSize(13);
        couponDesTextView.setMaxLines(2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
