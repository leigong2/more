package com.moreclub.moreapp.ucenter.ui.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PhoneUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.CheckPackageParam;
import com.moreclub.moreapp.ucenter.model.PackageCheck;
import com.moreclub.moreapp.ucenter.model.PackageCheckItem;
import com.moreclub.moreapp.ucenter.presenter.IPackageCheckListLoader;
import com.moreclub.moreapp.ucenter.presenter.PackageCheckListLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.PackageCheckListAdapter;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.app.Constants.MORE_PHONE;
import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/7/18.
 */

public class PackageCheckActivity extends BaseListActivity implements IPackageCheckListLoader.LoaderPackageCheckListDataBinder, View.OnTouchListener{
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.input_focus_text) EditText inputFocusText;
    @BindView(R.id.recyclerView) XRecyclerView recyclerView;
    @BindView(R.id.check_tag) TextView checkTag;

    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;

    private ArrayList<TextView> inputArray = new ArrayList<>();
    private ArrayList<TextView> cursorArray = new ArrayList<>();
    private ArrayList<PackageCheckItem> dataList = new ArrayList<>();
    private String mid;
    private PackageCheckListAdapter adapter;
    private static final int MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_MERCHANT_PHONE = 2;
    private String inputString;
    @Override
    protected int getLayoutResource() {
        return R.layout.package_check_activity;
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

    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        loadData();
    }

    @Override
    protected Class getLogicClazz() {
        return IPackageCheckListLoader.class;
    }

    private void initData() {
        mid = getIntent().getStringExtra("mid");
        loadData();
    }

    private void loadData(){
        ((PackageCheckListLoader)mPresenter).packageCheckList(mid,"1",""+page,""+PAGE_SIZE);
    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(getResources().getString(R.string.package_check));
        inputArray.add((TextView) findViewById(R.id.input1));
        inputArray.add((TextView) findViewById(R.id.input2));
        inputArray.add((TextView) findViewById(R.id.input3));
        inputArray.add((TextView) findViewById(R.id.input4));
        inputArray.add((TextView) findViewById(R.id.input5));
        inputArray.add((TextView) findViewById(R.id.input6));
        inputArray.add((TextView) findViewById(R.id.input7));
        inputArray.add((TextView) findViewById(R.id.input8));

        cursorArray.add((TextView) findViewById(R.id.cursor1));
        cursorArray.add((TextView) findViewById(R.id.cursor2));
        cursorArray.add((TextView) findViewById(R.id.cursor3));
        cursorArray.add((TextView) findViewById(R.id.cursor4));
        cursorArray.add((TextView) findViewById(R.id.cursor5));
        cursorArray.add((TextView) findViewById(R.id.cursor6));
        cursorArray.add((TextView) findViewById(R.id.cursor7));
        cursorArray.add((TextView) findViewById(R.id.cursor8));

        for(int i=0;i<cursorArray.size();i++){
            TextView cursorItem = cursorArray.get(i);
            if (i==0){
                cursorItem.setVisibility(View.VISIBLE);
            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(cursorItem, "alpha", 0.1f, 1.0f);
            animator.setDuration(1000);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }

        inputFocusText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence!=null&&charSequence.length()>0){
                    String tempString = charSequence.toString();
                    if (tempString.length()>=8){
                        tempString= tempString.substring(0,8);
                    }
                    if (tempString!=null&&tempString.startsWith(".")){
                        resetInputNumber("");
                        return;
                    }
                    inputString = tempString;
                    if (inputString!=null&&inputString.length()==8){
                        CheckPackageParam param = new CheckPackageParam();
                        param.setAppVersion(MainApp.getInstance().mVersion);
                        param.setMerchantId(mid);
                        param.setDeviceId(PhoneUtil.getImsi2(PackageCheckActivity.this));
                        param.setMachine("android");
                        param.setUid(""+MoreUser.getInstance().getUid());
                        param.setMachineStyle("android");
                        param.setOpSystem("android");
                        param.setOpVersion("5.0");
                        param.setCouponCode(inputString);
                        ((PackageCheckListLoader)mPresenter).onCheckPackage(param);
                    }
                    resetInputNumber(tempString);
                } else{
                    resetInputNumber("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                recyclerView.reset();
                loadData();
            }

            @Override
            public void onLoadMore() {
                if (page < pages){
                    page++;
                    loadData();
                } else {
                    recyclerView.loadMoreComplete();
                }
            }
        });
        adapter = new PackageCheckListAdapter(this,R.layout.package_check_item,dataList);
        adapter.setHasHeader(true);
        recyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.call_phone)
    void onClickCallPhone(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE);
        } else {
            callPhone(MORE_PHONE);
        }

    }

    private void callPhone(String ph) {
        if (ph == null || ph.length() == 0) {
            return;
        }
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + ph));
        startActivity(phoneIntent);
    }

    private void resetInputNumber(String value){

        for(int i=0;i<inputArray.size();i++){
            TextView item= inputArray.get(i);
            item.setText("");
        }

        for(int i=0;i<cursorArray.size();i++){
            TextView item= cursorArray.get(i);
            item.setVisibility(View.GONE);
        }

        int inputLength = value.length();
        if (inputLength<8) {
            TextView cursorItem = cursorArray.get(inputLength);
            cursorItem.setVisibility(View.VISIBLE);
        }

        if (inputLength>0) {
            for (int i = 0; i < inputLength; i++) {
                if (i < 8) {
                    char itemValue = value.charAt(i);
                    TextView item = inputArray.get(i);
                    item.setText("" + itemValue);
                }
            }
        }
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PackageCheckActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(PackageCheckActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 核销列表
     * @param response
     */
    @Override
    public void onPackageCheckResponse(RespDto<PackageCheck> response) {
        onLoadComplete(page);
        PackageCheck result = response.getData();
        pages = result.getPages();
        if (pages==0){
            pages=1;
        }
        if (result!=null&&result.getList()!=null&&result.getList().size()>0){
            dataList.addAll(result.getList());
            adapter.notifyDataSetChanged();
        } else {
            if (pages!=0) {
                recyclerView.loadMoreComplete();
            }
        }

    }


    @Override
    public void onPackageCheckFailure(String msg) {
        onLoadComplete(page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    /**
     * 核销
     * @param response
     */
    @Override
    public void onCheckResponse(RespDto<PackageCheckItem> response) {

        PackageCheckItem result = response.getData();
        if (result!=null){
            dataList.add(0,result);
            adapter.notifyDataSetChanged();
        }
        checkTag.setText("请输入套餐验证码");
        checkTag.setTextColor(ContextCompat.getColor(this,R.color.black));
        inputString = "";
        inputFocusText.setText("");
        resetInputNumber("");
    }

    @Override
    public void onCheckFailure(String msg) {
        if (msg!=null&&msg.length()>0) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        checkTag.setText("验证失败,请重新输入套餐验证码");
        checkTag.setTextColor(ContextCompat.getColor(this,R.color.check_package_error));
        inputString = "";
        inputFocusText.setText("");
        resetInputNumber("");
    }
}
