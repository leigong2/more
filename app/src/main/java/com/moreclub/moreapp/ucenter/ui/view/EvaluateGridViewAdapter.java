package com.moreclub.moreapp.ucenter.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantDetailsItem;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/3/6.
 */

public class EvaluateGridViewAdapter extends BaseAdapter {
    ArrayList<String> arrayList;
    private LayoutInflater mInflater;
    Context mContext;
    ItemClick itemClickListener;
    public EvaluateGridViewAdapter(Context context, ArrayList<String> list,ItemClick lis) {
        arrayList = list;
        mContext= context;
        mInflater =  LayoutInflater.from(context);
        itemClickListener = lis;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        PictureHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.evaluate_pic_grid_item, null);
            holder = new PictureHolder();
            holder.picture = (ImageView) convertView.findViewById(R.id.pic_item);
            convertView.setTag(holder);
        } else {
            holder = (PictureHolder) convertView.getTag();
        }

        String item = arrayList.get(position);
        if (!item.equals("add")){
            Glide.with(mContext).load(item)
                    .placeholder(R.drawable.addphoto)
                    .into(holder.picture);
        } else {
            Glide.with(mContext).load("")
                    .placeholder(R.drawable.addphoto)
                    .into(holder.picture);
        }
        // set name
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener!=null){
                    itemClickListener.itemClick(position);
                }
            }
        });

        return convertView;
    }

    public interface ItemClick{

       void itemClick(int pos);

    }

    static class PictureHolder{
        ImageView picture;
    }

}
