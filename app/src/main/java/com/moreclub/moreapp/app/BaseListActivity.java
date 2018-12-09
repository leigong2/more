package com.moreclub.moreapp.app;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moreclub.common.log.Logger;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.R;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseListActivity extends AppCompatActivity implements DataBinder {

    protected BasePresenter mPresenter;

    protected abstract int getLayoutResource();

    protected abstract void onInitialization(Bundle bundle);

    protected abstract void onReloadData();

    protected Class getLogicClazz() {
        return null;
    }

    private View noDataView;
    private ImageView noDataImg;
    private TextView noDataText;
    private Handler listHander = new Handler();

    protected void onInitData2Remote() {
        if (getLogicClazz() != null)
            mPresenter = getLogicImpl();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("name (%s.java:0)", getClass().getSimpleName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int layoutResoource = getLayoutResource();
        if ( layoutResoource != 0)
            setContentView(layoutResoource);
        this.onInitData2Remote();
        this.onInitialization(savedInstanceState);
    }

    //获得该页面的实例
    public <T> T getLogicImpl() {
        return LogicProxy.getInstance().bind(getLogicClazz(), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null && !mPresenter.isViewBind()) {
            LogicProxy.getInstance().bind(getLogicClazz(), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogicProxy.getInstance().unbind(getLogicClazz(), this);
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void showNoData(boolean isNoWifi){
        if (noDataView==null){
            noDataView = LayoutInflater.from(this).inflate(R.layout.all_activity_nodata_view, null);
            FrameLayout.LayoutParams ll =
                    new FrameLayout.LayoutParams
                            (FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT);
            ll.gravity = Gravity.BOTTOM;
            noDataView.setLayoutParams(ll);
            addContentView(noDataView,ll);

            ImageButton nav = (ImageButton) noDataView.findViewById(R.id.nav_back);
            final ProgressBar pbNoWifi = (ProgressBar) noDataView.findViewById(R.id.pb_no_wifi);
            nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            final LinearLayout lay = (LinearLayout) noDataView.findViewById(R.id.no_data_l);
            lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pbNoWifi.setVisibility(View.VISIBLE);
                    lay.setVisibility(View.GONE);
                    listHander.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pbNoWifi.setVisibility(View.GONE);
                            lay.setVisibility(View.VISIBLE);
                        }
                    },500);
                    onReloadData();
                }
            });
            noDataImg = (ImageView) noDataView.findViewById(R.id.no_data_img);
            noDataText = (TextView) noDataView.findViewById(R.id.no_data_text);

            if (isNoWifi){
                noDataImg.setImageResource(R.drawable.nowifi);
                noDataText.setText("网路去哪里了？");
            }
        }
        noDataView.setVisibility(View.VISIBLE);
    }

    public void hideNoData(){
        if (noDataView!=null) {
            noDataView.setVisibility(View.GONE);
        }
    }

}
