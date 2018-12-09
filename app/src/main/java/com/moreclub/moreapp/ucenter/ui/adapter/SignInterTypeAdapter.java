package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.ui.activity.AddSignInterActivity;

import java.util.List;

/**
 * Created by Administrator on 2017-06-20.
 */

public class SignInterTypeAdapter extends RecyclerAdapter<Integer> {

    private int[] resTypeSelectorIds = {R.drawable.pinzuo_pgz, R.drawable.pinzuo_llt, R.drawable.pinzuo_wyx
            , R.drawable.pinzuo_qhd, R.drawable.pinzuo_bgm, R.drawable.pinzuo_bsh};

    private int[] resTypeIds = {R.drawable.pinzuo_pgz_grey, R.drawable.pinzuo_llt_grey, R.drawable.pinzuo_wyx_grey
            , R.drawable.pinzuo_qhd_grey, R.drawable.pinzuo_bgm_grey, R.drawable.pinzuo_bsh_grey};

    public boolean[] selects = {false, false, false, false, true, false};
    private final AddSignInterActivity activity;

    public SignInterTypeAdapter(Context context, int layoutId, List<Integer> datas) {
        super(context, layoutId, datas);
        activity = (AddSignInterActivity) this.mContext;
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final Integer type) {
        holder.setIsRecyclable(false);
        final int position = getPosition(holder);
        final ImageView ivType = holder.getView(R.id.iv_type_root);
        ivType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selects.length; i++) {
                    for (int i1 = 0; i1 < selects.length; i1++) {
                        selects[i1] = false;
                    }
                    selects[type - 1] = true;
                }
                ivType.setSelected(true);
                notifyDataSetChanged();
                if (position > -1)
                    activity.etContent.setHint(activity.hints[position]);
            }
        });
        if (selects[type - 1]) {
            ivType.setImageResource(resTypeSelectorIds[position]);
        } else {
            ivType.setImageResource(resTypeIds[position]);
        }
    }
}
