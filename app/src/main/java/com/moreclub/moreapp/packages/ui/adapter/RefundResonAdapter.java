package com.moreclub.moreapp.packages.ui.adapter;

import android.content.Context;
import android.view.View;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.packages.model.DictionaryName;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class RefundResonAdapter extends RecyclerAdapter<DictionaryName> {
    public RefundResonAdapter(Context context, int layoutId, List<DictionaryName> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, DictionaryName dictionaryName) {
        holder.setText(R.id.apply_refund_reason, dictionaryName.getName());
        if (dictionaryName.isSelect()) {
            holder.getView(R.id.refund_item_cb).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.refund_item_cb).setVisibility(View.GONE);
        }
    }
}
