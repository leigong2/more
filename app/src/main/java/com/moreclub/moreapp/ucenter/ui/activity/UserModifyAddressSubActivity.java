package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.AddressItem;
import com.moreclub.moreapp.ucenter.ui.adapter.UserAddressSelectorAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/14.
 */

public class UserModifyAddressSubActivity extends BaseActivity {

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.main_rv)
    XRecyclerView xRecyclerView;

    ArrayList<AddressItem> arrayList;
    @Override
    protected int getLayoutResource() {
        return R.layout.user_modify_address;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        
        initData();
        setupView();

    }

    private void initData() {



    }

    private void setupView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);

        activityTitle.setText("所在地");
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModifyAddressSubActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        if (arrayList!=null) {
            UserAddressSelectorAdapter adapter = new UserAddressSelectorAdapter(UserModifyAddressSubActivity.this, R.layout.user_modify_address_item, arrayList);
            xRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
    }
}
