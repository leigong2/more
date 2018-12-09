package com.moreclub.moreapp.main.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moreclub.common.ui.BasePresenter;

/**
 * Created by Captain on 2017/7/25.
 */

public abstract class BaseTabView {
    public final static int PAGE_START = 0;
    protected final static int PAGE_SIZE = 10;
    public int page = PAGE_START;
    protected int pages = 0;
    public final static String KEY_CITY_ID = "city.id";

    public BasePresenter mPresenter;
    public RecyclerView xRecyclerView;

    public abstract View createView();

    public abstract void loadData();
}
