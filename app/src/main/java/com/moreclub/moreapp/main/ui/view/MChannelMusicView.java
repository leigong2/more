package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.moreclub.moreapp.R;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelMusicView extends BaseTabView{

    private Context mContext;
    private int cityID;
    public MChannelMusicView(Context cxt){
        mContext = cxt;

    }

    public View createView(){

        LinearLayout parentView = new LinearLayout(mContext);

        parentView.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        parentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_normal));
        return parentView;
    }

    public void loadData(){
        Log.d("dddd","MChannelMusicView");
    }

}
