package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.ReportCase;
import com.moreclub.moreapp.ucenter.model.ReportParam;
import com.moreclub.moreapp.ucenter.presenter.IUserDetailsReportLoader;
import com.moreclub.moreapp.ucenter.presenter.UserDetailsReportLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.UserDetailsReportAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/6/2.
 */

public class UserDetailsReportActivity extends BaseActivity implements IUserDetailsReportLoader.LoaderUserDetailsReport{

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.reportlist) RecyclerView recyclerView;
    @BindView(R.id.reportButton) Button reportButton;

    private ArrayList<ReportCase> dataList;
    private UserDetailsReportAdapter adapter;


    private String relationID;
    private String type;
    private String content;
    @Override
    protected int getLayoutResource() {
        return R.layout.user_details_report_activity;
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

    @Override
    protected Class getLogicClazz() {
        return IUserDetailsReportLoader.class;
    }

    private void initData() {
        relationID = getIntent().getStringExtra("relationID");
        type = getIntent().getStringExtra("type");
        content = "骚扰辱骂";
        dataList = new ArrayList<>();
        dataList.add(new ReportCase("骚扰辱骂",true));
        dataList.add(new ReportCase("淫秽色情",false));
        dataList.add(new ReportCase("垃圾广告",false));
        dataList.add(new ReportCase("欺诈(酒托)",false));
        dataList.add(new ReportCase("血腥暴力",false));
        dataList.add(new ReportCase("违法行为(反动，分裂，违禁品等)",false));
    }

    private void setupViews() {
        activityTitle.setText(R.string.report_title);
        naBack.setOnClickListener(goBackListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserDetailsReportAdapter(this,R.layout.user_details_report_item,dataList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

                for(int i=0;i<dataList.size();i++){
                    ReportCase caseItem = dataList.get(i);
                    caseItem.setSelect(false);
                }
                ReportCase item = (ReportCase) o;
                item.setSelect(true);
                content = item.getReportName();
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportParam param = new ReportParam();
                param.setContent(content);
                param.setRelationId(relationID);
                param.setType(type);
                param.setUid(""+MoreUser.getInstance().getUid());
                ((UserDetailsReportLoader)mPresenter).onReport(param);
            }
        });
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserDetailsReportActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @Override
    public void onUserDetailsReportResponse(RespDto response) {
        Toast.makeText(this,"举报成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUserDetailsReportFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(this,"举报失败",Toast.LENGTH_SHORT).show();
    }
}
