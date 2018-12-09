package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.AddressItem;
import com.moreclub.moreapp.ucenter.ui.adapter.UserAddressSelectorAdapter;
import com.moreclub.moreapp.util.AddressLoadTask;
import com.moreclub.moreapp.util.MapLocation;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/13.
 */

public class UserModifyAddressActivity extends BaseActivity {

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.main_rv)
    XRecyclerView xRecyclerView;

    View header;
    View subView;

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
        setupView();
    }

    private void setupView() {
        naBack.setOnClickListener(goBack);
        activityTitle.setText("所在地");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        header = LayoutInflater.from(this).inflate(R.layout.user_modify_address_header,
                (ViewGroup) findViewById(android.R.id.content), false);

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.addHeaderView(header);

        final TextView autoText = (TextView) header.findViewById(R.id.autoAddressText);
        LinearLayout autoLay = (LinearLayout) header.findViewById(R.id.addressAutoLay);
        if (MoreUser.getInstance().getCity() == null) {
            MapLocation.getLocation(UserModifyAddressActivity.this.getApplicationContext(), new MapLocation.MapCallback() {
                @Override
                public void dbMapCallback(double lat, double lng, String locationCity, String province, String city) {
                    autoText.setText(city);
                }

                @Override
                public void dbMapCallbackFails() {
                    Log.i("zune:","failure");
                }
            });
        } else
            autoText.setText(MoreUser.getInstance().getLocationCity());
        autoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectCity", MoreUser.getInstance().getLocationCity());
                UserModifyAddressActivity.this.setResult(4, intent);
                UserModifyAddressActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
        AddressLoadTask addressLoadTask = new AddressLoadTask(UserModifyAddressActivity.this, parseAddress);
        addressLoadTask.execute();

    }

    View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserModifyAddressActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    AddressLoadTask.ParseAddressListener parseAddress = new AddressLoadTask.ParseAddressListener() {

        @Override
        public void parseAddressSuccess(final ArrayList<AddressItem> list) {

            UserAddressSelectorAdapter adapter = new UserAddressSelectorAdapter(UserModifyAddressActivity.this, R.layout.user_modify_address_item, list);
            adapter.setHasHeader(true);
            xRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, Object o, int position) {

                    position--;
                    if (position < 0)
                        position = 0;
                    AddressItem itemAddress = list.get(position);
                    final ArrayList<AddressItem> clickList = itemAddress.getSubAddressArray();
                    if (clickList != null && clickList.size() > 0) {
                        subView = LayoutInflater.from(UserModifyAddressActivity.this).inflate(R.layout.user_modify_sub_address,
                                (ViewGroup) findViewById(android.R.id.content), false);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        UserModifyAddressActivity.this.getWindow().addContentView(subView, layoutParams);

                        RecyclerView subAddressList = (RecyclerView) subView.findViewById(R.id.sub_address_list);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(UserModifyAddressActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        subAddressList.setLayoutManager(layoutManager);
                        UserAddressSelectorAdapter subAdapter = new UserAddressSelectorAdapter(UserModifyAddressActivity.this, R.layout.user_modify_address_item, clickList);
                        subAddressList.setAdapter(subAdapter);
                        subAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                                AddressItem itemAddress = clickList.get(position);
                                Intent intent = new Intent();
                                intent.putExtra("selectCity", itemAddress.getAddressName());
                                UserModifyAddressActivity.this.setResult(4, intent);
                                UserModifyAddressActivity.this.finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                            }

                            @Override
                            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                                return false;
                            }
                        });
                    }

//                    View subAddressView = new View(UserModifyAddressActivity.this);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//                     subAddressView.setBackgroundColor(ContextCompat.getColor(UserModifyAddressActivity.this,R.color.abc_red));
//

//                    Intent intent = new Intent(UserModifyAddressActivity.this,UserModifyAddressSubActivity.class);
//                    intent.putExtra("addressPostion",position);
//                    UserModifyAddressActivity.this.startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                    return false;
                }
            });
        }


        @Override
        public void parseAddressFails() {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
    }
}
