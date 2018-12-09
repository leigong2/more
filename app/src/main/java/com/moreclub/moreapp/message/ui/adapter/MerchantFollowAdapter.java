package com.moreclub.moreapp.message.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.FollowsUser;
import com.moreclub.moreapp.ucenter.presenter.IUserDetailsLoader;
import com.moreclub.moreapp.ucenter.presenter.UserDetailsLoader;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Captain on 2017/4/10.
 */

public class MerchantFollowAdapter extends RecyclerAdapter<FollowsUser> implements IUserDetailsLoader.LoaderUserDetailsDataBinder{

    private BasePresenter mPresenter;
    public MerchantFollowAdapter(Context context, int layoutId, List<FollowsUser> datas) {
        super(context, layoutId, datas);
        mPresenter = LogicProxy.getInstance().bind(IUserDetailsLoader.class, this);
    }

    @Override
    public void convert(RecyclerViewHolder holder,final FollowsUser followsUser) {

        holder.setText(R.id.user_name, followsUser.getNickname());
        Glide.with(mContext)
                .load(followsUser.getThumb())
                .dontAnimate()
                .into((CircleImageView) holder.getView(R.id.header_img));

        if (followsUser.getSex()==0){
            holder.setImageResource(R.id.sex_img,R.drawable.femenine);
        } else {
            holder.setImageResource(R.id.sex_img,R.drawable.masculine);
        }

        String star="";
        try {
            int month = TimeUtils.getDateMonth(followsUser.getBirthday());
            int day = TimeUtils.getDateDay(followsUser.getBirthday());
            star = TimeUtils.getStarSeat(month,day);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int ageint = TimeUtils.getAge(followsUser.getBirthday());
            if (ageint == 0) {
                holder.setText(R.id.age, "");
            } else {
                holder.setText(R.id.age, ""+ageint);
            }
        }catch (Exception e){
            e.printStackTrace();
            holder.setText(R.id.age, "");
        }

        StringBuilder followDes = new StringBuilder();
        if (followsUser.getCareer()!=null){
            followDes.append(followsUser.getCareer());
            if (star!=null&&star.length()>0){
                followDes.append(" | ");
                followDes.append(star);
            }
            followDes.append(" | ");
            followDes.append("历史派对 "+followsUser.getParties());
        } else {
            if (star!=null&&star.length()>0){
                followDes.append(star);
                followDes.append(" | ");
            }
            followDes.append("历史派对 "+followsUser.getParties());
        }
        holder.setText(R.id.follow_des, followDes.toString());

        if (followsUser.getFollowType()==0){
            holder.getView(R.id.focus_button).setVisibility(View.VISIBLE);
            if (followsUser.isForeach()) {
                holder.setText(R.id.focus_word, mContext.getString(R.string.follow_foreach_word));
                holder.setImageResource(R.id.focus_status,R.drawable.focused_x);
            } else {
                holder.setText(R.id.focus_word, mContext.getString(R.string.follow_ta));
                holder.setImageResource(R.id.focus_status,R.drawable.focus_x);
            }
        } else {
            if (followsUser.isForeach()) {
                holder.getView(R.id.focus_button).setVisibility(View.VISIBLE);
                holder.setText(R.id.focus_word, mContext.getString(R.string.follow_foreach_word));
                holder.setImageResource(R.id.focus_status,R.drawable.focused_x);
            } else {
                holder.getView(R.id.focus_button).setVisibility(View.GONE);
            }
        }

        holder.getView(R.id.focus_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followsUser.getFollowType()==0) {
                    if (followsUser.isForeach()) {

                    } else {
                        ((UserDetailsLoader) mPresenter).onFollowAdd("" + MoreUser.getInstance().getUid(), "" + followsUser.getUid());
                    }
                } else {
                    if (followsUser.isForeach()) {

                    } else {
                        ((UserDetailsLoader) mPresenter).onFollowDel("" + MoreUser.getInstance().getUid(), "" + followsUser.getUid());
                    }
                }
            }
        });
    }

    /**
     * 关注
     * @param response
     */
    @Override
    public void onUserDetailsResponse(RespDto response) {

    }

    @Override
    public void onUserDetailsFailure(String msg) {

    }

    @Override
    public void onFollowAddFailure(String msg) {

    }

    @Override
    public void onFollowDelFailure(String msg) {

    }

    @Override
    public void onMerchantActivityFailure(String message) {

    }

    @Override
    public void onReplyInviteFailure(String msg) {

    }

    @Override
    public void onUserActiveDetailsFailure(String msg) {

    }

    @Override
    public void onUserActiveListFailure(String msg) {

    }

    @Override
    public void onAttentionChannelFailure(String msg) {

    }

    @Override
    public void onAttentionChannelResponse(RespDto responce) {

    }

    @Override
    public void onUserActiveListResponse(RespDto body) {

    }

    @Override
    public void onUserActiveDetailsResponse(RespDto body) {

    }

    @Override
    public void onReplyInviteResponse(RespDto body) {

    }

    @Override
    public void onMerchantActivityResponse(RespDto body) {

    }

    @Override
    public void onFollowDelResponse(RespDto response) {
        Toast.makeText(mContext,"取消关注成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowAddResponse(RespDto response) {
        Toast.makeText(mContext,"关注成功",Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }
}
