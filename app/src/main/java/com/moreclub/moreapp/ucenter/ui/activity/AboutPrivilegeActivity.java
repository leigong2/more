package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.ucenter.model.PrivilegeAboutItem;
import com.moreclub.moreapp.ucenter.ui.adapter.PrivilegeAboutAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/11.
 */

public class AboutPrivilegeActivity extends BaseActivity {
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.privilege_list) XRecyclerView privilegeList;

    private ArrayList<PrivilegeAboutItem> dataList;
    @Override
    protected int getLayoutResource() {
        return R.layout.about_privilege;
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
        setupViews();
    }

    private void initData() {
        dataList = new ArrayList<>();

        PrivilegeAboutItem item1 = new PrivilegeAboutItem();
        item1.setTitle("注册");
        item1.setDes("1天＝1里程，自注册之日起，每增加1天系统自动为你增加1里程，越早注册积累的里程越多");
        item1.setIcon(R.drawable.register_privilege);
        dataList.add(item1);

        PrivilegeAboutItem item2 = new PrivilegeAboutItem();
        item2.setTitle("消费");
        item2.setDes("1元＝1里程，您在More的每笔消费都会被换算成里程，消费越多里程越多，消费1元积累1里程");
        item2.setIcon(R.drawable.consume_privilege);
        dataList.add(item2);

        PrivilegeAboutItem item3 = new PrivilegeAboutItem();
        item3.setTitle("登录");
        item3.setDes("1天＝1里程，成功登录打开More,即可积累1里程，每日上限1里程");
        item3.setIcon(R.drawable.login_privilege);
        dataList.add(item3);

        PrivilegeAboutItem item4 = new PrivilegeAboutItem();
        item4.setTitle("分享");
        item4.setDes("1次＝20里程，在个人中心分享More给你的朋友，即可一次性获得20里程，每日限1次");
        item4.setIcon(R.drawable.share_privilege);
        dataList.add(item4);

        PrivilegeAboutItem item5 = new PrivilegeAboutItem();
        item5.setTitle("签到");
        item5.setDes("1次＝10里程，任意签到一家酒吧，一次性获得10里程，每日上限10里程");
        item5.setIcon(R.drawable.signin_licheng);
        dataList.add(item5);


    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(AboutPrivilegeActivity.this.getResources().getString(R.string.get_privilege_title));

        LinearLayoutManager privilegeManager = new LinearLayoutManager(this);
        privilegeManager.setOrientation(LinearLayoutManager.VERTICAL);

        privilegeList.setLayoutManager(privilegeManager);

        PrivilegeAboutAdapter adapter = new PrivilegeAboutAdapter(this,
                R.layout.about_privilege_item, dataList);

        privilegeList.setAdapter(adapter);
    }
    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AboutPrivilegeActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };
}
