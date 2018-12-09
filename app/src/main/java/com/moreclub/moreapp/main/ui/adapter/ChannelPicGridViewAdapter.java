package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.moreapp.R;


/**
 * Created by Captain on 2017/7/26.
 */


public class ChannelPicGridViewAdapter extends BaseAdapter {
    String[] arrayList;
    private LayoutInflater mInflater;
    Context mContext;

    public ChannelPicGridViewAdapter(Context context, String[] list) {
        arrayList = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        if (arrayList!=null) {
            return arrayList.length;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return arrayList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_channel_pic_item, null);

            // construct an item tag
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.pic_item));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }

        String item = arrayList[position];
        Glide.with(mContext).load(item)
                .into(viewTag.mIcon);
        return convertView;
    }

    class ItemViewTag {
        protected ImageView mIcon;
        public ItemViewTag(ImageView icon) {
            this.mIcon = icon;
        }
    }
}