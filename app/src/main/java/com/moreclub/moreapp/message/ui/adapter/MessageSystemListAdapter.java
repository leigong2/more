package com.moreclub.moreapp.message.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.message.model.MessageSystem;

import java.util.List;

/**
 * Created by Captain on 2017/5/4.
 */

public class MessageSystemListAdapter extends RecyclerAdapter<MessageSystem> {

    public MessageSystemListAdapter(Context context, int layoutId, List<MessageSystem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, MessageSystem messageSystem) {

        ((TextView)holder.getView(R.id.system_message_title)).setText(messageSystem.getTitle());
        ((TextView)holder.getView(R.id.system_message_time)).setText(TimeUtils.formatChatDate((messageSystem.getSendTime())));
        ((TextView)holder.getView(R.id.system_message_des)).setText(messageSystem.getContent());

        if (messageSystem.getMap()!=null) {
            if (messageSystem.getMap().getSrc()!=null&&messageSystem.getMap().getSrc().length()>0){
                holder.getView(R.id.system_message_image).setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(messageSystem.getMap().getSrc())
                        .dontAnimate()
                        .into((ImageView) holder.getView(R.id.system_message_image));
            } else {
                holder.getView(R.id.system_message_image).setVisibility(View.GONE);
            }
            if (messageSystem.getMap().getLinktext()!=null&&messageSystem.getMap().getLinktext().length()>0){
                ((TextView) holder.getView(R.id.system_message_option)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.system_message_option)).setText(messageSystem.getMap().getLinktext());
            } else {
                ((TextView) holder.getView(R.id.system_message_option)).setVisibility(View.GONE);
            }
        } else {
            ((TextView) holder.getView(R.id.system_message_option)).setVisibility(View.GONE);
             holder.getView(R.id.system_message_image).setVisibility(View.GONE);
        }

        if ("more_update".equals(messageSystem.getTemplate())){
            ((TextView) holder.getView(R.id.system_message_option)).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.system_message_option)).setText("体验升级版  MORE >");
        }
    }
}
