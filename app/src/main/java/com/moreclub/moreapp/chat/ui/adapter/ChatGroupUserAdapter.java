package com.moreclub.moreapp.chat.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.chat.model.ChatGroupUser;
import com.moreclub.moreapp.main.model.SignPersion;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Captain on 2017/6/9.
 */

public class ChatGroupUserAdapter  extends RecyclerAdapter<ChatGroupUser> {

    public ChatGroupUserAdapter(Context context, int layoutId, List<ChatGroupUser> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, ChatGroupUser collectlistInfo) {

        Glide.with(mContext)
                .load(collectlistInfo.getThumb())
                .dontAnimate()
                .into((ImageView) holder.getView(R.id.header_img));

        ((TextView)holder.getView(R.id.nameText)).setTextColor(ContextCompat.getColor(mContext,R.color.black));
        holder.setText(R.id.nameText,collectlistInfo.getNickname());

        if (collectlistInfo.getSex()==null){
            holder.getView(R.id.sex_img).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.sex_img).setVisibility(View.VISIBLE);
            if ("1".equals(collectlistInfo.getSex())) {
                holder.setImageResource(R.id.sex_img, R.drawable.masculine);
            } else {
                holder.setImageResource(R.id.sex_img, R.drawable.femenine);
            }
        }

        try {
            if (collectlistInfo.getBirthday()==null){
                holder.setText(R.id.age,"保密");
            } else {
                int ageint = TimeUtils.getAge(collectlistInfo.getBirthday());
                if (ageint == 0) {
                    holder.setText(R.id.age,"保密");
                } else {
                    holder.setText(R.id.age,ageint+"");
                }
            }
        } catch ( Exception e){
            holder.setText(R.id.age,"保密");
            e.printStackTrace();
        }
    }
}
