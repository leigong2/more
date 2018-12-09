package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 2017/2/27.
 */

public class MerchantAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<MerchantItem> listData;
    private Context mContext;

    public  MerchantAdapter(ArrayList<MerchantItem> data, Context context){
        this.listData = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.merchant_list_item,parent,false);
        return new MerchantAdapter.MerchantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MerchantItem item = this.listData.get(position);

        ((MerchantViewHolder)holder).merchantName.setText(item.getBusName());
//        ((MerchantViewHolder)holder).thumb.setText(item.getBusName());
        Glide.with(mContext).load(item.getThumb())
                .dontAnimate()
                .into(((MerchantViewHolder)holder).thumb);
        ((MerchantViewHolder)holder).sellPoint.setText(item.getRecommendReason());
        ((MerchantViewHolder)holder).busName.setText(item.getBusName());
        ((MerchantViewHolder)holder).busTag.setText(item.getTags());
        ((MerchantViewHolder)holder).distance.setText(item.getDistanceResult());

    }

    @Override
    public int getItemCount() {
        return this.listData.size();
    }

    public class MerchantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView merchantName;
        ImageView thumb;
        TextView sellPoint;
        TextView busName;
        TextView busTag;
        TextView distance;

        View parentView;
        public MerchantViewHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.merchantIV);
            merchantName = (TextView) itemView.findViewById(R.id.merchantName);
            sellPoint = (TextView) itemView.findViewById(R.id.sellPointerTV);
            busName = (TextView) itemView.findViewById(R.id.busName);
            busTag = (TextView) itemView.findViewById(R.id.busTag);
            distance = (TextView) itemView.findViewById(R.id.distDanceTV);
            parentView = itemView.findViewById(R.id.merchantItemParentView);
            parentView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            pos--;
            if (pos<0){
                pos=0;
            }
            MerchantItem item = listData.get(pos);

            Intent intent = new Intent(mContext,MerchantDetailsViewActivity.class);
            intent.putExtra("mid",""+item.getMid());
            mContext.startActivity(intent);
        }
    }
}
