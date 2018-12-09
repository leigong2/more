package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.List;

/**
 * Created by Captain on 2017/5/25.
 */

public class ModifyAutoSignAdapter extends RecyclerAdapter<MerchantItem> {


    public ModifyAutoSignAdapter(Context context, int layoutId, List<MerchantItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MerchantItem messageBase) {

        ((TextView) holder.getView(R.id.merchantName)).setText(messageBase.getName());

        if (messageBase.isSelected()){
            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.VISIBLE);
        } else {
            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.GONE);
        }
    }
}
