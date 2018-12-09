package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.ui.activity.MySignInterListActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterDetailActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterTotalActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.moreclub.moreapp.main.constant.Constants.MYSIGNINTERLISTACTIVITY;
import static com.moreclub.moreapp.main.constant.Constants.SIGNINTERACTIVITY;
import static com.moreclub.moreapp.main.constant.Constants.SIGNINTERTOTALACTIVITY;

/**
 * Created by Administrator on 2017-06-21.
 */

public class SignInterLiveAdapter extends RecyclerAdapter<MerchantsSignInters> {

    private String mid;
    private int mySid = -1;

    //    436512
    private int[] resTypeSelectorIds = {R.drawable.pinzuo_pgz, R.drawable.pinzuo_llt, R.drawable.pinzuo_wyx
            , R.drawable.pinzuo_qhd, R.drawable.pinzuo_bgm, R.drawable.pinzuo_bsh};

    private int[] resTypeIds = {R.drawable.pinzuo_pgz_grey, R.drawable.pinzuo_llt_grey, R.drawable.pinzuo_wyx_grey
            , R.drawable.pinzuo_qhd_grey, R.drawable.pinzuo_bgm_grey, R.drawable.pinzuo_bsh_grey};


    private int[] resIds = {R.drawable.buyuadrink_white, R.drawable.cocktail_white, R.drawable.beerbrew_white
            , R.drawable.wine_white, R.drawable.whiskey_white, R.drawable.importbeer_white,
            R.drawable.champagne_white, R.drawable.brandy_white, R.drawable.saki_white};
    private String[] str = {"", "鸡尾酒", "精酿啤酒", "威士忌", "鸡尾酒", "进口啤酒", "香槟", "白兰地", "清酒"};

    private int[] drawables = {R.drawable.interaction_sit, R.drawable.interaction_talk, R.drawable.interaction_play,
            R.drawable.interaction_qa, R.drawable.interaction_help, R.drawable.interaction_dont_talk};


    public SignInterLiveAdapter(Context context, int layoutId, List<MerchantsSignInters> datas, String mid) {
        super(context, layoutId, datas);
        this.mid = mid;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MerchantsSignInters merchantsSignInters) {
        if (getPosition(holder) == 1) {
            for (int i = mDatas.size() - 1; i >= 0; i--) {
                if (mDatas.get(i).getUid().equals(MoreUser.getInstance().getUid()) && mDatas.get(i).getExprise() == 2) {
                    mySid = mDatas.get(i).getSid();
                }
            }
        }
        View view_weight = holder.getView(R.id.view_weight);
        int exprise = merchantsSignInters.getExprise();
        switch (exprise) {
            case 1:
                //已结束
            case 3:
                //过期
                switch (merchantsSignInters.getType()) {
                    case 5:
                        holder.setImageResource(R.id.iv_type, resTypeIds[0]);
                        break;
                    case 3:
                        holder.setImageResource(R.id.iv_type, resTypeIds[1]);
                        break;
                    case 6:
                        holder.setImageResource(R.id.iv_type, resTypeIds[2]);
                        break;
                    case 4:
                        holder.setImageResource(R.id.iv_type, resTypeIds[3]);
                        break;
                    case 1:
                        holder.setImageResource(R.id.iv_type, resTypeIds[4]);
                        break;
                    case 2:
                        holder.setImageResource(R.id.iv_type, resTypeIds[5]);
                        break;
                }
                view_weight.setBackgroundResource(R.drawable.sign_detail_content);
                break;
            case 2:
                //在线
                switch (merchantsSignInters.getType()) {
                    case 5:
                        holder.setImageResource(R.id.iv_type, resTypeSelectorIds[0]);
                        view_weight.setBackgroundResource(drawables[0]);
                        break;
                    case 3:
                        holder.setImageResource(R.id.iv_type, resTypeSelectorIds[1]);
                        view_weight.setBackgroundResource(drawables[1]);
                        break;
                    case 6:
                        holder.setImageResource(R.id.iv_type, resTypeSelectorIds[2]);
                        view_weight.setBackgroundResource(drawables[2]);
                        break;
                    case 4:
                        holder.setImageResource(R.id.iv_type, resTypeSelectorIds[3]);
                        view_weight.setBackgroundResource(drawables[3]);
                        break;
                    case 1:
                        holder.setImageResource(R.id.iv_type, resTypeSelectorIds[4]);
                        view_weight.setBackgroundResource(drawables[4]);
                        break;
                    case 2:
                        holder.setImageResource(R.id.iv_type, resTypeSelectorIds[5]);
                        view_weight.setBackgroundResource(drawables[5]);
                        break;
                }
                break;
        }
        Glide.with(mContext)
                .load(merchantsSignInters.getUserThumb())
                .placeholder(R.drawable.profilephoto_small)
                .dontAnimate()
                .into((CircleImageView) holder.getView(R.id.civ_user_thumb));
        holder.setOnClickListener(R.id.civ_user_thumb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", "" + merchantsSignInters.getUid());
                ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
            }
        });
        holder.setText(R.id.tv_user_name, merchantsSignInters.getNickName());
        holder.setText(R.id.age, merchantsSignInters.getAge() + "");
        if (merchantsSignInters.getGender() != null && merchantsSignInters.getGender().equals("0")) {
            holder.setImageResource(R.id.iv_gender, R.drawable.femenine);
        } else {
            holder.setImageResource(R.id.iv_gender, R.drawable.masculine);
        }
        holder.setText(R.id.tv_content, merchantsSignInters.getContent());
        if (merchantsSignInters.getGift() == 0) {
            holder.getView(R.id.drinks).setVisibility(View.GONE);
            holder.getView(R.id.tv_drinks).setVisibility(View.GONE);
        } else {
            holder.setImageResource(R.id.drinks, resIds[merchantsSignInters.getGift() - 1]);
            holder.setText(R.id.tv_drinks, "请你喝一杯" + str[merchantsSignInters.getGift() - 1]);
        }
        String time = getTime(merchantsSignInters.getTimestamp());
        holder.setText(R.id.time, time);
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 进入签到互动详情
                if (MoreUser.getInstance().getUid() != null
                        && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    return;
                }
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent = new Intent(mContext, SignInterDetailActivity.class);
                intent.putExtra("sid", merchantsSignInters.getSid());
                if (mContext instanceof SignInterActivity) {
                    intent.putExtra("tag", SIGNINTERACTIVITY);
                } else if (mContext instanceof SignInterTotalActivity) {
                    intent.putExtra("tag", SIGNINTERTOTALACTIVITY);
                } else if (mContext instanceof MySignInterListActivity) {
                    intent.putExtra("tag", MYSIGNINTERLISTACTIVITY);
                }
                intent.putExtra("mySid", mySid);
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        });
    }

    private String getTime(long timestamp) {
        long now = Long.valueOf(TimeUtils.getTimestampSecond());
        SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日");
        java.sql.Date date = new java.sql.Date(timestamp * 1000);
        String format = sdr.format(date);
        long time = now - timestamp;
        if (time < (10 * 60)) {
            return "刚刚";
        } else if (time > (10 * 60) && time < (60 * 60)) {
            return (time / (60)) + "分钟";
        } else if (time > (60 * 60) && time < (24 * 60 * 60)) {
            return (time / (60 * 60)) + "小时";
        } else if (time > (24 * 60 * 60) && time < (3 * 24 * 60 * 60)) {
            return (time / (24 * 60 * 60)) + "天前";
        } else {
            return format;
        }
    }
}
