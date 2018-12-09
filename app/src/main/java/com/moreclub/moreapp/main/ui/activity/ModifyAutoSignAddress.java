package com.moreclub.moreapp.main.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.CorrectSignin;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.presenter.INearbyMerchantLoader;
import com.moreclub.moreapp.main.presenter.NearbyMerchantLoader;
import com.moreclub.moreapp.main.ui.adapter.ModifyAutoSignAdapter;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Captain on 2017/5/24.
 */

public class ModifyAutoSignAddress extends BaseActivity implements INearbyMerchantLoader.LoaderNearbyMerchantDataBinder {
    private final static String KEY_CITY_CODE = "city.code";
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    ModifyAutoSignAdapter adapter;
    ArrayList<MerchantItem> dataList;

    String oldMid;
    String oldMerchantName;
    MerchantItem selectMerchant;

    @Override
    protected int getLayoutResource() {
        return R.layout.modify_auto_sign_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {

        ButterKnife.bind(this);
        initData();
        setupView();

    }

    @Override
    protected Class getLogicClazz() {
        return INearbyMerchantLoader.class;
    }

    private void initData() {

        oldMid = getIntent().getStringExtra("mid");
        oldMerchantName = getIntent().getStringExtra("merchantName");

        dataList = new ArrayList<MerchantItem>();
        ((NearbyMerchantLoader) mPresenter).loadNearbyMerchant(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"),
                0, 5, MoreUser.getInstance().getUserLocationLng()
                        + "," + MoreUser.getInstance().getUserLocationLat());

    }

    private void setupView() {
        ImageButton backButton = (ImageButton) findViewById(R.id.nav_back);
        TextView titleView = (TextView) findViewById(R.id.activity_title);
        titleView.setText("位置纠错");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View header = LayoutInflater.from(this).inflate(R.layout.modify_sign_address_header,
                (ViewGroup) findViewById(android.R.id.content), false);

        if (oldMerchantName != null) {
            TextView errorSignMerchant = (TextView) header.findViewById(R.id.errorSignMerchant);
            errorSignMerchant.setText(oldMerchantName);
        }

        xRecyclerView.addHeaderView(header);
        xRecyclerView.setPullRefreshEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);

        adapter = new ModifyAutoSignAdapter(ModifyAutoSignAddress.this, R.layout.modify_auto_sign_item, dataList);
        xRecyclerView.setAdapter(adapter);
        adapter.setAddHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

                for (int i = 0; i < dataList.size(); i++) {
                    MerchantItem item = (MerchantItem) dataList.get(i);
                    item.setSelected(false);
                }
//                if (position - 1 < 0) {
//                    position = 0;
//                }
                MerchantItem item2 = (MerchantItem) o;
                selectMerchant = item2;
                item2.setSelected(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    @OnClick(R.id.modifyButton)
    void modifySignin() {
        CorrectSignin param = new CorrectSignin();
        param.setNewMid("" + selectMerchant.getMid());
        param.setNewMname(selectMerchant.getName());
        param.setOldMid(oldMid);
        param.setUid("" + MoreUser.getInstance().getUid());
        ((NearbyMerchantLoader) mPresenter).onCorrectSign(param);
    }

    @OnClick(R.id.noModify)
    void noModifyClick() {
        finish();
    }


    @Override
    public void onNearbyMerchantsResponse(RespDto response) {
        if (response.isSuccess()) {
            ArrayList<MerchantItem> newMerchants = (ArrayList<MerchantItem>) response.getData();
            if (newMerchants != null && !newMerchants.isEmpty() && newMerchants.size() > 0) {
                MerchantItem item = newMerchants.get(0);
                item.setSelected(true);
                selectMerchant = item;
                dataList.addAll(newMerchants);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNearbyMerchantsFailure(String msg) {

    }

    /**
     * 修改签到
     *
     * @param msg
     */
    @Override
    public void onCorrectSignFailure(String msg) {
        Toast.makeText(this, "修改失败,请重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTopicListFailure(String msg) {

    }

    @Override
    public void onTopicListResponse(RespDto response) {

    }

    @Override
    public void onCorrectSignResponse(RespDto response) {
        boolean result = (boolean) response.getData();
        if (result) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "修改失败,请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
