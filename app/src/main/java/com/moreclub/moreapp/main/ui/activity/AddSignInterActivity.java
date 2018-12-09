package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.AddSignInter;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.model.event.ChannelAddressEvent;
import com.moreclub.moreapp.main.presenter.AddSignInterLoader;
import com.moreclub.moreapp.main.presenter.IAddSignInterLoader;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.adapter.SignInterDrinkAdapter;
import com.moreclub.moreapp.ucenter.ui.adapter.SignInterTypeAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.SpannableClick;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_CODE;

public class AddSignInterActivity extends BaseActivity implements IAddSignInterLoader.AddSignInterDataBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_type)
    RecyclerView xrvType;
    @BindView(R.id.et_content)
    public EditText etContent;
    @BindView(R.id.tv_for_editing)
    TextView tvForEditing;
    @BindView(R.id.xrv_drink)
    RecyclerView xrvDrink;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.address_img)
    ImageView addressImg;
    @BindView(R.id.address_tag)
    TextView addressTag;
    @BindView(R.id.caicai)
    TextView caicai;
    @BindView(R.id.address_name)
    TextView addressName;
    @BindView(R.id.address_lay)
    RelativeLayout addressLay;
    @BindView(R.id.divider_line)
    View dividerLine;
    @BindView(R.id.ll_weight)
    LinearLayout llWeight;
    private SignInterDrinkAdapter adapter_drink;
    private SignInterTypeAdapter adapter_type;

    public String[] hints = {"一个人喝酒超级无聊，想找好玩朋友拼个座，带我飞。", "聊音乐，电影，酒…认识有趣的你！"
            , "急！玩游戏缺人，求旁边的帅哥靓女加入～", "城市小白，求土著支招好吃的好玩的去处！"
            , "遇见生活小难题，求大神支招！", "开车堵，上班老板不酷，回家生活不酷，憋着难受，这里说点酷的！"};

    private String mid;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            tvForEditing.setText(length + "/100");
            if (length > 100) {
                tvForEditing.setText(100 + "/100");
                etContent.setText(s.subSequence(0, 100));
            }
        }
    };
    private ArrayList<MerchantItem> addresses;
    private String from;

    @Override
    protected int getLayoutResource() {
        return R.layout.add_sign_inter_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IAddSignInterLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupView();
    }

    private void setupView() {
        activityTitle.setText("约玩");
        from = getIntent().getStringExtra("from");
        etContent.setHint(hints[0]);
        etContent.addTextChangedListener(watcher);
        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");
        if (TextUtils.isEmpty(mid)) {
            addressLay.setVisibility(View.VISIBLE);
            dividerLine.setVisibility(View.VISIBLE);
            addressLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            AddSignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(AddSignInterActivity.this, FindAndSelectMerchantActivity.class);
                    intent.putExtra("from", "AddSignInterActivity");
                    ActivityCompat.startActivity(AddSignInterActivity.this, intent, compat.toBundle());
                }
            });
            ((AddSignInterLoader) mPresenter).loadNearbyMerchant(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"),
                    0, 3, MoreUser.getInstance().getUserLocationLng()
                            + "," + MoreUser.getInstance().getUserLocationLat());
        }
        WrapContentLinearLayoutManager lm = new WrapContentLinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        xrvType.setLayoutManager(lm);
        List<Integer> types = new ArrayList<>();
        //    436512
        for (int i = 1; i <= 6; i++) {
            switch (i) {
                case 1:
                    types.add(5);
                    break;
                case 2:
                    types.add(3);
                    break;
                case 3:
                    types.add(6);
                    break;
                case 4:
                    types.add(4);
                    break;
                case 5:
                    types.add(1);
                    break;
                case 6:
                    types.add(2);
                    break;
            }
        }
        adapter_type = new SignInterTypeAdapter(this, R.layout.sign_inter_type_item, types);
        xrvType.setAdapter(adapter_type);

        WrapContentLinearLayoutManager lm_drink = new WrapContentLinearLayoutManager(this);
        lm_drink.setOrientation(LinearLayoutManager.HORIZONTAL);
        xrvDrink.setLayoutManager(lm_drink);
        List<Integer> drinks = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            drinks.add(i);
        }
        adapter_drink = new SignInterDrinkAdapter(this, R.layout.sign_inter_drink_item, drinks);
        xrvDrink.setAdapter(adapter_drink);
    }

    @Override
    public void onAddSignResponse(RespDto response) {
        if (response != null && response.getData() != null) {
            MerchantsSignInters data = (MerchantsSignInters) response.getData();
            Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
            finish();
            if ("SignInterActivity".equals(from)) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AddSignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(AddSignInterActivity.this, SignInterActivity.class);
                intent.putExtra("mid", mid);
                ActivityCompat.startActivity(AddSignInterActivity.this, intent, compat.toBundle());
            } else if ("SuperMainActivity".equals(from)) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AddSignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(AddSignInterActivity.this, SuperMainActivity.class);
                intent.putExtra("shouldGo", "SpaceFragment");
                ActivityCompat.startActivity(AddSignInterActivity.this, intent, compat.toBundle());
            } else if ("SignInterTotalActivity".equals(from)) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AddSignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(AddSignInterActivity.this, SignInterTotalActivity.class);
                ActivityCompat.startActivity(AddSignInterActivity.this, intent, compat.toBundle());
            } else if ("MySignInterListActivity".equals(from)) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AddSignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(AddSignInterActivity.this, MySignInterListActivity.class);
                ActivityCompat.startActivity(AddSignInterActivity.this, intent, compat.toBundle());
            }
        } else {
            Toast.makeText(getApplicationContext(), "您在同一家酒吧只能有一个约酒公告，请关闭后再试", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAddSignFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
        }
    }

    @Override
    public void onNearbyMerchantsFailure(String msg) {

    }

    @Override
    public void onNearbyMerchantsResponse(RespDto response) {
        final StringBuilder merchants = new StringBuilder();
        if (response.isSuccess()) {
            ArrayList<MerchantItem> newMerchants = (ArrayList<MerchantItem>) response.getData();
            if (newMerchants != null && !newMerchants.isEmpty() && newMerchants.size() > 0) {
                addresses = new ArrayList<>();
                for (int i = 0; i < newMerchants.size(); i++) {
                    if (i == 2) {
                        break;
                    }
                    MerchantItem item = newMerchants.get(i);
                    merchants.append(item.getName());
                    merchants.append(" ");
                    addresses.add(item);
                }
            }
        }
        int startIndex = 0;
        int endIndex = 0;
        SpannableStringBuilder spannable = new SpannableStringBuilder(merchants.toString());
        for (int i = 0; i < 2; i++) {
            endIndex += addresses.get(i).getName().length() + 1;
            final int finalI = i;
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 选择酒吧
                    addressTag.setText(addresses.get(finalI).getName());
                    mid = addresses.get(finalI).getMid().toString();
                }
            };
            try {
                int color = R.color.mainTagName;
                spannable.setSpan(new SpannableClick(color, listener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = endIndex;
            } catch (Exception e) {

            }
        }
        addressName.setText(spannable);
        addressName.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @OnClick({R.id.nav_back, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.tv_send:
                if (MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(getApplicationContext(), UseLoginActivity.class);
                    return;
                }
                if (TextUtils.isEmpty(mid)) {
                    Toast.makeText(this, "请选择约玩的位置,遇见同好朋友", Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = MoreUser.getInstance().getAccess_token();
                AddSignInter inter = new AddSignInter();
                String content;
                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    content = etContent.getHint().toString().trim();
                } else {
                    content = etContent.getText().toString().trim();
                }
                inter.setContent(content);
                String gift = getGigt();
                if (gift != null)
                    inter.setGift(gift);
                if (!TextUtils.isEmpty(mid)) {
                    inter.setMid(Integer.parseInt(mid));
                }
                int type = getType();
                inter.setType(type);
                inter.setUid(MoreUser.getInstance().getUid());
                ((AddSignInterLoader) mPresenter).onAddSignInter(token, inter);
                break;
        }
    }

    private int getType() {
        for (int i = 0; i < adapter_type.selects.length; i++) {
            if (adapter_type.selects[i]) {
                return i + 1;
            }
        }
        return 5;
    }

    private String getGigt() {
        for (int i = 0; i < adapter_drink.selects.length; i++) {
            if (adapter_drink.selects[i])
                return i + 1 + "";
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressEvent(ChannelAddressEvent event) {
        MerchantItem item = event.getMerchantItem();
        if (item.getMid() == -1) {
            addressTag.setText("所在位置");
            mid = 0 + "";
        } else {
            addressTag.setText(item.getName());
            mid = item.getMid() + "";
        }
    }
}
