package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantDetailsItem;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/3/6.
 */

public class MerchantGridViewAdapter extends BaseAdapter {
    ArrayList<MerchantDetailsItem> arrayList;
    private LayoutInflater mInflater;
    Context mContext;

    public MerchantGridViewAdapter(Context context, ArrayList<MerchantDetailsItem> list) {
        arrayList = list;
        mContext= context;
        mInflater =  LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.merchant_child_tag_item, null);

            // construct an item tag
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.main_bartag_logo),
                    (TextView) convertView.findViewById(R.id.main_bartag_name));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }

        MerchantDetailsItem item = arrayList.get(position);
        // set name
        viewTag.mName.setText(item.getName());
        Glide.with(mContext).load(item.getExt())
                .into(viewTag.mIcon);
        return convertView;
    }

    class ItemViewTag {
        protected ImageView mIcon;
        protected TextView mName;

        public ItemViewTag(ImageView icon, TextView name) {
            this.mName = name;
            this.mIcon = icon;
        }
    }
}
