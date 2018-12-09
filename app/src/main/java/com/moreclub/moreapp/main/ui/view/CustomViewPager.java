package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent arg0) {
//		return true;
//	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (listener != null) {
				listener.onTouchDown();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (listener != null) {
				listener.onTouchUp();
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private OnViewPagerTouchEvent listener;
	
	public void setOnViewPagerTouchEventListener(OnViewPagerTouchEvent l){
		listener = l;
	}
	
	public interface OnViewPagerTouchEvent{
		void onTouchDown();
		void onTouchUp();
	}
}
