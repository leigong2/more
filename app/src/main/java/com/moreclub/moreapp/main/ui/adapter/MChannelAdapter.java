package com.moreclub.moreapp.main.ui.adapter;
import android.content.Context;
import android.view.View;

import com.moreclub.moreapp.main.model.MainChannelItem;
import java.util.List;

/**
 * Created by Captain on 2017/7/28.
 */

public class MChannelAdapter extends MChannelBaseAdapter {
    public MChannelAdapter(Context context, int layoutId, List<MainChannelItem> datas, View header) {
        super(context, layoutId, datas, header);
    }
}
