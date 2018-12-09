package com.moreclub.moreapp.message.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.ui.activity.MainActivity;
import com.moreclub.moreapp.main.ui.activity.ModifyAutoSignAddress;
import com.moreclub.moreapp.message.model.MessageSignin;
import com.moreclub.moreapp.ucenter.ui.activity.UserSecretActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by Captain on 2017/5/26.
 */

public class MessageSigninListAdapter extends RecyclerAdapter<MessageSignin> {

    public MessageSigninListAdapter(Context context, int layoutId, List<MessageSignin> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder,final MessageSignin messageSignin) {

//    auto = 1;
//    privacy = 0;
//    setting = 1;
//    timestamp = 1495781236000;
//    uid = 3097832504509440;
        ((TextView) holder.getView(R.id.signin_setting)).setText(mContext.getString(R.string.messgae_signin_list_item_setting));
        ((TextView) holder.getView(R.id.signin_correct)).setText(mContext.getString(R.string.message_signin_list_item_error_address));
        ((TextView) holder.getView(R.id.system_message_time)).setText(DateUtils.getTimestampString(new Date(messageSignin.getTimestamp())));
        if (messageSignin.isSetting()){
            if (messageSignin.isAuto()){
                ((TextView) holder.getView(R.id.system_message_title)).setText(mContext.getString(R.string.messgae_signin_list_item_open));
                ((TextView) holder.getView(R.id.system_message_des)).setText(mContext.getString(R.string.messgae_signin_list_item_open_des));
            } else {
                ((TextView) holder.getView(R.id.system_message_title)).setText(mContext.getString(R.string.messgae_signin_list_item_close));
                ((TextView) holder.getView(R.id.system_message_des)).setText(mContext.getString(R.string.messgae_signin_list_item_close_des));
            }
            ((TextView) holder.getView(R.id.signin_setting)).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.signin_correct)).setVisibility(View.GONE);
        } else {
            if (messageSignin.isAuto()){
                ((TextView) holder.getView(R.id.system_message_title)).setText(mContext.getString(R.string.message_signin_list_item_autosuccess));
            } else {
                ((TextView) holder.getView(R.id.system_message_title)).setText(mContext.getString(R.string.message_signin_list_item_success));
            }
            if (messageSignin.getPersonCount()==0){
                String success = mContext.getString(R.string.message_signin_list_item_sign_des_tomerchant, messageSignin.getMerchantName());
                SpannableStringBuilder spannable = new SpannableStringBuilder(success);
                int startIndex = 3;
                int endIndex = 3 + messageSignin.getMerchantName().length();
                int color = Color.parseColor("#FFCC00");
                spannable.setSpan(new ForegroundColorSpan(color),startIndex, endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) holder.getView(R.id.system_message_des)).setText(spannable);
            } else {
                String string = mContext.getString(R.string.message_signin_list_item_sign_des, messageSignin.getMerchantName(), "" + messageSignin.getPersonCount());
                SpannableStringBuilder spannable = new SpannableStringBuilder(string);
                int startIndex = 3;
                int endIndex = 3 + messageSignin.getMerchantName().length();
                int color = Color.parseColor("#FFCC00");
                spannable.setSpan(new ForegroundColorSpan(color),startIndex, endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) holder.getView(R.id.system_message_des)).setText(spannable);
            }
            ((TextView) holder.getView(R.id.signin_setting)).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.signin_correct)).setVisibility(View.VISIBLE);
        }
        if (messageSignin.getPrivacy()!=null){
            if (messageSignin.getPrivacy()) {
                ((TextView) holder.getView(R.id.system_message_title)).setText(mContext.getString(R.string.message_signin_list_item_see_open));
                ((TextView) holder.getView(R.id.system_message_des)).setText(mContext.getString(R.string.message_signin_list_item_see_open_des));
            }else{
                ((TextView) holder.getView(R.id.system_message_title)).setText(mContext.getString(R.string.message_signin_list_item_see_close));
                ((TextView) holder.getView(R.id.system_message_des)).setText(mContext.getString(R.string.message_signin_list_item_see_close_des));
            }
            ((TextView) holder.getView(R.id.signin_setting)).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.signin_correct)).setVisibility(View.GONE);
        }
        ((TextView) holder.getView(R.id.signin_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageSignin != null) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, UserSecretActivity.class);
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        });
        ((TextView) holder.getView(R.id.signin_correct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageSignin != null) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, ModifyAutoSignAddress.class);
                    intent.putExtra("mid", ""+messageSignin.getMid());
                    intent.putExtra("merchantName", messageSignin.getMerchantName());
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                }
            }
        });

    }
}
