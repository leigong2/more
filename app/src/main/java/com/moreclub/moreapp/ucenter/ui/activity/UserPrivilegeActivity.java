package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.ucenter.model.UserPrivilege;
import com.moreclub.moreapp.ucenter.presenter.IUserPrivilegeLoader;
import com.moreclub.moreapp.ucenter.presenter.UserPrivilegeLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.PrivilegeMerchantHeaderAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.ShareHandler;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/4/9.
 */

public class UserPrivilegeActivity extends BaseListActivity implements IUserPrivilegeLoader.UserPrivilegeDataBinder{
    private final static String KEY_CITY_CODE = "city.code";
    @BindView(R.id.user_privilege_scrollview)
    ObservableScrollView scrollView;

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.card_type) ImageView cardTypeIV;
    @BindView(R.id.left_pointer) ImageView leftPointer;
    @BindView(R.id.midd_pointer) ImageView middPointer;
    @BindView(R.id.right_pointer) ImageView rightPointer;
    @BindView(R.id.level_value) TextView levelValue;
    @BindView(R.id.open_value) TextView openValue;
    @BindView(R.id.discount_value) TextView discountValue;
    @BindView(R.id.cooperation_value) TextView cooperationValue;

    @BindView(R.id.cooperation_merchant_header)
    RecyclerView cooperationMerchantHeader;

    @BindView(R.id.card_name) TextView cardName;
    @BindView(R.id.en_card_name) TextView enCard_Name;
    @BindView(R.id.disrate_tag) TextView disrateTag;
    @BindView(R.id.disrate) TextView disrate;
    @BindView(R.id.orange_line) TextView orangeLine;

    @BindView(R.id.user_privilege_division_unit)
    TextView tvDivisionUnit;

    @BindView(R.id.user_privilege_invite_btn)
    Button inviteBtn;

    @BindColor(R.color.user_privilege_division_expire)
    int expireColor;

    @BindView(R.id.user_privilege_division_card_layout)
    LinearLayout divisionLayout;

    @BindView(R.id.user_privilege_division_card_title)
    TextView tvDivisionTitle;

    @BindView(R.id.user_privilege_division_card_num)
    TextView tvDivisionCount;

    @BindView(R.id.user_privilege_division_desc)
    TextView tvDivisionDesc;

    @BindView(R.id.user_privilege_division_time)
    TextView tvDivisionExpireTime;

    private int cardLevel;
    private int curMiles;
    private int totalMiles;

    private int cooperationCard;
    private int mainScreenWidth =1000 ;
    private UserPrivilege result;

    private ArrayList<MerchantItem> dataList = new ArrayList<>();
    PrivilegeMerchantHeaderAdapter adapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.user_privilege_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        initData();
        setupViews();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        loadData(""+cardLevel);
    }

    @Override
    protected Class getLogicClazz() {
        return IUserPrivilegeLoader.class;
    }

    private void initData() {

        mainScreenWidth= ScreenUtil.getScreenWidth(this);

        Intent intent = getIntent();
        cardLevel = intent.getIntExtra("cardLevel",0);
        curMiles= intent.getIntExtra("curMiles",0);
        totalMiles= intent.getIntExtra("totalMiles",0);
        cooperationCard = cardLevel;
        loadData(""+cardLevel);
    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(UserPrivilegeActivity.this.getResources().getString(R.string.user_privilege_title));

        int totalLine = mainScreenWidth - ScreenUtil.dp2px(this,132);

        int orangeWidth = totalLine/2;

        if (cardLevel==0){
            cardTypeIV.setImageResource(R.drawable.plaincard);
            leftPointer.setImageResource(R.drawable.privilege_plain_big);
            middPointer.setImageResource(R.drawable.privilege_lock);
            rightPointer.setImageResource(R.drawable.privilege_lock);

            double plaincardWidth =(int) ((float)curMiles/999.0)*orangeWidth;
            orangeLine.setWidth((int)plaincardWidth);

        } else if (cardLevel==1){
            cardTypeIV.setImageResource(R.drawable.orangecard);
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_orange_big);
            rightPointer.setImageResource(R.drawable.privilege_lock);
            double plaincardWidth = orangeWidth+((float)curMiles/9999.0)*orangeWidth;
            orangeLine.setWidth((int)plaincardWidth);
        } else if (cardLevel==2){
            cardTypeIV.setImageResource(R.drawable.blackcard);
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_orange);
            rightPointer.setImageResource(R.drawable.privilege_black_big);
            orangeLine.setWidth(mainScreenWidth);
        }
        levelValue.setText(curMiles+"/"+totalMiles);
        setCardInfo(cardLevel);

        LinearLayoutManager privilegeManager = new LinearLayoutManager(this);
        privilegeManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cooperationMerchantHeader.setLayoutManager(privilegeManager);

        adapter = new PrivilegeMerchantHeaderAdapter(this,
                R.layout.user_privilege_header_item, dataList);
        cooperationMerchantHeader.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserPrivilegeActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserPrivilegeActivity.this, CooperationMerchantActivity.class);
                intent.putExtra("cardtype",cooperationCard);
                ActivityCompat.startActivity( UserPrivilegeActivity.this, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollBy(0, ScreenUtil.dp2px(UserPrivilegeActivity.this, 217));
                scrollView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }
        }, 50);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void loadData(String type){
        ((UserPrivilegeLoader)mPresenter).loadPrivilege(type, PrefsUtils.getString(this, KEY_CITY_CODE, "cd"));
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserPrivilegeActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };


    @OnClick(R.id.left_pointer)
    void leftPointerClick(){
        cardTypeIV.setImageResource(R.drawable.plaincard);
        if (cardLevel==0){

            leftPointer.setImageResource(R.drawable.privilege_plain_big);
            middPointer.setImageResource(R.drawable.privilege_lock);
            rightPointer.setImageResource(R.drawable.privilege_lock);
        } else if (cardLevel==1){
            leftPointer.setImageResource(R.drawable.privilege_plain_big);
            middPointer.setImageResource(R.drawable.privilege_orange);
            rightPointer.setImageResource(R.drawable.privilege_lock);
        } else if (cardLevel==2){
            leftPointer.setImageResource(R.drawable.privilege_plain_big);
            middPointer.setImageResource(R.drawable.privilege_orange);
            rightPointer.setImageResource(R.drawable.privilege_black);
        }
        setCardInfo(0);
        loadData("0");
        cooperationCard = 0;
    }

    @OnClick(R.id.midd_pointer)
    void middPointerClick(){
        cardTypeIV.setImageResource(R.drawable.orangecard);
        if (cardLevel==0){
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_lock_big);
            rightPointer.setImageResource(R.drawable.privilege_lock);
        } else if (cardLevel==1){
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_orange_big);
            rightPointer.setImageResource(R.drawable.privilege_lock);
        } else if (cardLevel==2){
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_orange_big);
            rightPointer.setImageResource(R.drawable.privilege_black);
        }
        setCardInfo(1);
        loadData("1");
        cooperationCard = 1;
    }

    @OnClick(R.id.right_pointer)
    void rightPointerClick(){
        cardTypeIV.setImageResource(R.drawable.blackcard);
        if (cardLevel==0){
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_lock);
            rightPointer.setImageResource(R.drawable.privilege_lock_big);
        } else if (cardLevel==1){
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_orange);
            rightPointer.setImageResource(R.drawable.privilege_lock_big);
        } else if (cardLevel==2){
            leftPointer.setImageResource(R.drawable.privilege_plain);
            middPointer.setImageResource(R.drawable.privilege_orange);
            rightPointer.setImageResource(R.drawable.privilege_black_big);
        }
        setCardInfo(2);
        loadData("2");
        cooperationCard = 2;
    }

    @OnClick(R.id.user_privilege_invite_btn)
    void onCliCKInviteFriends() {
        if (result != null) {
            if (result.getBlackDivision() != null) {
                if (result.getBlackDivision().getExprise()) {
                    Toast.makeText(this, "抱歉，该分享已过期", Toast.LENGTH_SHORT).show();
                } else{
                    new ShareHandler(this, 6, "" + result.getBlackDivision().getDid()).showShareView();
                }
            } else if (result.getOrangeDivision() != null) {
                if (result.getOrangeDivision().getExprise()) {
                    Toast.makeText(this, "抱歉，该分享已过期", Toast.LENGTH_SHORT).show();
                } else {
                    new ShareHandler(this, 7, "" + result.getOrangeDivision().getDid()).showShareView();
                }
            }
        }
    }

    void setCardInfo(int type){
        if (type==0){
            cardName.setText("普通会员");
            enCard_Name.setText("More Plain");
            disrateTag.setVisibility(View.GONE);
            disrate.setVisibility(View.GONE);
        } else if (type==1){
            cardName.setText("橙卡会员");
            enCard_Name.setText("More Orange");
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
        } else if (type==2){
            cardName.setText("黑卡会员");
            enCard_Name.setText("More Black");
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserPrivilegeResponse(RespDto response) {

        UserPrivilege result = (UserPrivilege) response.getData();
        this.result = result;
        Log.d("ddd","success");

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        int mileageLimit = result.getMileageLimit();
        String mileageLimitStr = "";

        if (mileageLimit==0){
            mileageLimitStr = "总里程9999,如何获取里程";
            levelValue.setText(curMiles+"/9999");
        } else {
            mileageLimitStr = "总里程"+mileageLimit+",如何获取里程";
            levelValue.setText(curMiles+"/"+mileageLimit);
        }

        if (result.getBlackDivision() != null) {
            long time = result.getBlackDivision().getExpriseTime()*1000L;
            Logger.d("=====format.timetime(date)=======" + time );
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Logger.d("=====format.format(date)=======" + date +", "+ format.format(date));
            //result.getBlackDivision().setExprise(true);  for test

            if (result.getBlackDivision().getExprise()) {
                updateExpireColor();
            }

            tvDivisionTitle.setText("黑卡邀请券");
            tvDivisionCount.setText(""+ result.getBlackDivision().getAcTime());
            tvDivisionDesc.setText("使用您的特权身份，邀请好友成为「More黑卡会员」");
            tvDivisionExpireTime.setText("有效期至 " + format.format(date));
        } else if (result.getOrangeDivision() != null) {
            long time = result.getOrangeDivision().getExpriseTime()*1000L;
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (result.getOrangeDivision().getExprise()) {
                updateExpireColor();
            }

            tvDivisionTitle.setText("橙卡邀请券");
            tvDivisionCount.setText(""+ result.getOrangeDivision().getAcTime());
            tvDivisionDesc.setText("使用您的特权身份，邀请好友成为「More橙卡会员」");
            tvDivisionExpireTime.setText("有效期至 "+format.format(date));
        } else {
            divisionLayout.setVisibility(View.GONE);
            scrollView.fullScroll(View.FOCUS_UP);
        }

        timer.start();

        openValue.setText(mileageLimitStr);
        StringBuilder discountStr = new StringBuilder();
        if (result.getRemarks()!=null&&result.getRemarks().size()>0){
            for(int i=0;i<result.getRemarks().size();i++){
                discountStr.append((i+1)+".");
                discountStr.append(result.getRemarks().get(i));
                if (i!=(result.getRemarks().size()-1)) {
                    discountStr.append("\n\n");
                }
            }
        }
        if (discountStr.length()>0) {
            discountValue.setText(discountStr.toString());
        } else {
            discountValue.setText("暂无说明");
        }
        cooperationValue.setText("所在地区合作酒吧共"+result.getTotalMerchants()+"家");
        if (result.getDiscountRate()==0||result.getDiscountRate()==1){
            disrateTag.setVisibility(View.GONE);
            disrate.setVisibility(View.GONE);
        } else {
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
            disrate.setText(""+(result.getDiscountRate()*10));
        }

        dataList.clear();
        dataList.addAll(result.getMerchants());
        adapter.notifyDataSetChanged();
    }

    private void updateExpireColor() {
        tvDivisionTitle.setTextColor(expireColor);
        tvDivisionUnit.setTextColor(expireColor);
        tvDivisionCount.setTextColor(expireColor);
        tvDivisionDesc.setTextColor(expireColor);
        tvDivisionExpireTime.setTextColor(expireColor);
        inviteBtn.setBackgroundResource(R.drawable.user_privilege_invite_expire_btn);
        inviteBtn.setTextColor(expireColor);
    }

    @Override
    public void onUserPrivilegeFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(UserPrivilegeActivity.this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(UserPrivilegeActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.open_lay)
    void onClickOpenLay(){
        AppUtils.pushLeftActivity(this, AboutPrivilegeActivity.class);
    }

    @OnClick(R.id.cooperation_lay)
    void onClickCooperationLay(){
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(this, CooperationMerchantActivity.class);
        intent.putExtra("cardtype",cooperationCard);
        ActivityCompat.startActivity( this, intent, compat.toBundle());
    }


    private CountDownTimer timer = new CountDownTimer(5000, 1000){

        @Override
        public void onTick(long tick) {
            if (tick/1000 == 2) {
                Logger.d("===============tick" + tick);
                if (result != null && (result.getBlackDivision() != null
                        || result.getOrangeDivision() != null)) {
                    scrollView.smoothScrollBy(0, -divisionLayout.getHeight());
                }
            }
        }

        @Override
        public void onFinish() {
            if (result == null) return;

            if (result.getBlackDivision() != null) {
                if (result.getBlackDivision().getExprise()) {
                    scrollView.smoothScrollBy(0, divisionLayout.getHeight());
                }
            }else if (result.getOrangeDivision() != null) {
                if (result.getOrangeDivision().getExprise()) {
                    scrollView.smoothScrollBy(0, divisionLayout.getHeight());
                }
            }
        }
    };
}
