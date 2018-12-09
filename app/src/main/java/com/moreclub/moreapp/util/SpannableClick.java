package com.moreclub.moreapp.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.moreclub.moreapp.app.MainApp;

/**
 * Created by Administrator on 2017-09-05-0005.
 */

public class SpannableClick extends ClickableSpan {
    private View.OnClickListener mListener;
    private int color;

    public SpannableClick(int color, View.OnClickListener l) {
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
