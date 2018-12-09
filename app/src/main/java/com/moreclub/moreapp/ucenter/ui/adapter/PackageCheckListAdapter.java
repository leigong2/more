package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.PackageCheckItem;
import com.moreclub.moreapp.util.ArithmeticUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Captain on 2017/7/18.
 */

public class PackageCheckListAdapter extends RecyclerAdapter<PackageCheckItem> {

    public PackageCheckListAdapter(Context context, int layoutId, List<PackageCheckItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, PackageCheckItem item) {

        ((TextView)holder.getView(R.id.package_name)).setText(item.getTitle());

        ((TextView)holder.getView(R.id.package_price)).
                setText("¥ "+ ArithmeticUtils.round((item.getTotalPrice()/item.getItemNum()),1));
        ((TextView)holder.getView(R.id.code_value)).setText(item.getConsumCode());
        ((TextView)holder.getView(R.id.package_time)).setText(TimeUtils.getSDF2Time(item.getUsedTime()));

        String phone = item.getContactPhone();
        if (phone!=null&&phone.length()>8){
            ((TextView)holder.getView(R.id.phone_value)).setText(phone.substring(0,3)+"*****"+phone.substring(phone.length()-3));
        } else {
            ((TextView)holder.getView(R.id.phone_value)).setText(item.getContactPhone());
        }
    }
}
