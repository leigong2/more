package com.moreclub.common.util;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.moreclub.common.R;

/**
 * Created by Captain on 2017/3/17.
 */

public class CountDownTimerUtils extends CountDownTimer {

    private Button mTextView;
    private Context mContext;
    public CountDownTimerUtils(Context context,Button textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        mContext = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText("( "+millisUntilFinished / 1000 + " )重新发送");  //设置倒计时时间
        mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.bgColor_alert_button_press));


        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);//重新获得点击
        mTextView.setTextColor(ContextCompat.getColor(mContext,R.color.abc_black));
    }
}
