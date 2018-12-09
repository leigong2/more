package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelPagerAdapter extends PagerAdapter {

    private Context mContext;
//    private ArrayList<BaseTabView> dataList;
    private ArrayList<View> dataList;

    public MChannelPagerAdapter(Context cxt, ArrayList<View> list){
        mContext = cxt;
        dataList = list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = dataList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container != null) {
            container.removeView((View) object);
        }
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }


    @Override
    public Parcelable saveState() {
        return super.saveState();
    }
}
