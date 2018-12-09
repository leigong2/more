package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantCard;
import com.moreclub.moreapp.main.model.MerchantCardPromoExtra;
import com.moreclub.moreapp.main.presenter.IMerchantCardPromotionLoader;
import com.moreclub.moreapp.main.presenter.MerchantCardPromotionLoader;
import com.moreclub.moreapp.main.ui.adapter.MerchantCardDetailsAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/14.
 */

public class MerchantCardInfoActivity extends BaseActivity implements IMerchantCardPromotionLoader.LoadMerchantCardPromotion {
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.cardinfo) XRecyclerView xRecyclerView;
    private View header;

    private ImageView cardImage;
    private TextView cardName;
    private TextView enCardName;
    private TextView disrateTag;
    private TextView disrate;

    private int cardLevel;
    private String mid;
    private MerchantCardDetailsAdapter adapter;

    ArrayList<MerchantCardPromoExtra> dataList;
    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_card_info_activity;
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
        return IMerchantCardPromotionLoader.class;
    }

    private void initData() {
        dataList = new ArrayList<>();
        Intent intent = getIntent();
        cardLevel = intent.getIntExtra("cardLevel",0);
        mid= intent.getStringExtra("mid");

        ((MerchantCardPromotionLoader)mPresenter).loadMerchantCardPromotion(mid,cardLevel);

    }

    private void setupViews() {
        activityTitle.setText(getString(R.string.card_details_titile));
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        header = LayoutInflater.from(this).inflate(R.layout.merchant_card_info_header,
                (ViewGroup) findViewById(android.R.id.content), false);

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.addHeaderView(header);

        cardImage = ButterKnife.findById(header, R.id.card_image);;
        cardName= ButterKnife.findById(header, R.id.card_name);
        enCardName= ButterKnife.findById(header, R.id.en_card_name);
        disrateTag= ButterKnife.findById(header, R.id.disrate_tag);
        disrate= ButterKnife.findById(header, R.id.disrate);

        if (cardLevel==0){
            cardImage.setImageResource(R.drawable.plaincard);
            cardName.setText("普通会员");
            enCardName.setText("More Plain");
            disrateTag.setVisibility(View.GONE);
            disrate.setVisibility(View.GONE);
        } else if (cardLevel==1){
            cardImage.setImageResource(R.drawable.orangecard);
            cardName.setText("橙卡会员");
            enCardName.setText("More Orange");
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
        } else if (cardLevel==2){
            cardImage.setImageResource(R.drawable.blackcard);
            cardName.setText("黑卡会员");
            enCardName.setText("More Black");
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
        }

        adapter = new MerchantCardDetailsAdapter(this,dataList);
        xRecyclerView.setAdapter(adapter);
//        PaySuccessAdapter adapter = new PaySuccessAdapter(PaySucessActivity.this,dataResoure);
//        xRecyclerView.setAdapter(adapter);




    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }


    @Override
    public void onMerchantCardPromotionResponse(RespDto responce) {

        MerchantCard result = (MerchantCard) responce.getData();

        if (result.getDiscountRate()==0||result.getDiscountRate()==1){
            disrateTag.setVisibility(View.GONE);
            disrate.setVisibility(View.GONE);
        } else {
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
            disrate.setText(""+(result.getDiscountRate()*10));
        }



        if (result.getPromoExtra()!=null&&result.getPromoExtra().size()>0){
            for(int i=0;i<result.getPromoExtra().size();i++){
                MerchantCardPromoExtra item = result.getPromoExtra().get(i);
                item.setType(1);
            }
            dataList.addAll(result.getPromoExtra());
        }

        if (result.getPromoWine()!=null&&result.getPromoWine().size()>0){
            MerchantCardPromoExtra subItem = new MerchantCardPromoExtra();
            subItem.setType(4);
            subItem.setTipStr("参与优惠酒单");
            dataList.add(subItem);

            for(int i=0;i<result.getPromoWine().size();i++){
                MerchantCardPromoExtra item = result.getPromoWine().get(i);
                item.setType(2);
            }
            dataList.addAll(result.getPromoWine());
        }

        if (result.getPromoHint()!=null&&result.getPromoHint().length()>0){
            MerchantCardPromoExtra subItem = new MerchantCardPromoExtra();
            subItem.setType(4);
            subItem.setTipStr("优惠提示");
            dataList.add(subItem);

            MerchantCardPromoExtra item1 = new MerchantCardPromoExtra();
            item1.setType(3);
            item1.setTipStr(result.getPromoHint());
            dataList.add(item1);
        }

        MerchantCardPromoExtra subItem = new MerchantCardPromoExtra();
        subItem.setType(4);
        subItem.setTipStr("温馨提示");
        dataList.add(subItem);

        dataList.addAll(getTipsArray());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMerchantCardPromotionFailure(String msg) {
        dataList.addAll(getTipsArray());
        adapter.notifyDataSetChanged();
    }

    private ArrayList<MerchantCardPromoExtra> getTipsArray(){

        ArrayList<MerchantCardPromoExtra> list = new ArrayList<>();
        MerchantCardPromoExtra item1 = new MerchantCardPromoExtra();
        item1.setType(3);
        item1.setTipStr("1.特权卡优惠内容是否可同店内其他优惠叠加使用，请咨询店内服务员");
        list.add(item1);

        MerchantCardPromoExtra item2 = new MerchantCardPromoExtra();
        item2.setType(3);
        item2.setTipStr("2.特权卡叠加优惠请查看具体使用说明，如有疑问请咨询店内服务员");
        list.add(item2);

        MerchantCardPromoExtra item3 = new MerchantCardPromoExtra();
        item3.setType(3);
        item3.setTipStr("3.如有其他问题，可致电摩儿客服：400-886-7918");
        list.add(item3);

        return list;
    }
}
