package com.moreclub.moreapp.information.ui.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017-06-30.
 */

public class XRecyclerView extends com.jcodecraeer.xrecyclerview.XRecyclerView {
    public XRecyclerView(Context context) {
        super(context);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onScrollStateChanged(int newState) {
        super.onScrollStateChanged(newState);
        switch (newState){
            case SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                //当屏幕停止滚动，加载图片
                try {
                    if(getContext() != null) Glide.with(getContext()).resumeRequests();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                try {
                    if(getContext() != null) Glide.with(getContext()).pauseRequests();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                try {
                    if(getContext() != null) Glide.with(getContext()).pauseRequests();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.refreshComplete();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if(p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if(child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout)child;
                    break;
                }
            }
            if(appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                    }
                });
            }
        }
    }

}
