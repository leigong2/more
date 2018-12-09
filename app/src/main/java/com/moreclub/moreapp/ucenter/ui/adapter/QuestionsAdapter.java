package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;

import java.util.List;

/**
 * Created by Administrator on 2017-05-15.
 */

public class QuestionsAdapter extends RecyclerAdapter {

    private List<String> datas;

    public QuestionsAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        this.datas = datas;
    }

    @Override
    public void convert(RecyclerViewHolder holder, Object o) {

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.setText(R.id.tv_question_name, datas.get(position));
    }
}
