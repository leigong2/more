package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantDetailsItem;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/3/4.
 */

public class MerchantDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private enum ITEM_TYPE{
        ITEM_TYPE_WINE,
        ITEM_TYPE_MUSIC,
        ITEM_TYPE_SCENE,
        ITEM_TYPE_FACILITIES,
        ITEM_TYPE_AREA
    }

    ArrayList<MerchantDetailsItem> wineArray;
    ArrayList<MerchantDetailsItem> musicArray;
    ArrayList<MerchantDetailsItem> sceneArray;
    ArrayList<MerchantDetailsItem> facilitiesArray;
    ArrayList<HashMap<String,String>> areaArray;

    ArrayList<String> tagArray;
    int recyclerCount;

    Context mContext;

    PopupWindow mPopupWindow;
    ViewPagerFixed sceneViewPager;


    public MerchantDetailsAdapter(Context context, ArrayList<MerchantDetailsItem> wine,
                                  ArrayList<MerchantDetailsItem> music,
                                  ArrayList<MerchantDetailsItem> scene,
                                  ArrayList<MerchantDetailsItem> facilities,
                                  ArrayList<HashMap<String,String>> area,
                                  ArrayList<String>tags, int count){
        mContext = context;
        wineArray = wine;
        musicArray = music;
        sceneArray = scene;
        facilitiesArray = facilities;
        areaArray = area;
        tagArray = tags;
        recyclerCount = count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if(viewType != MerchantDetailsAdapter.ITEM_TYPE.ITEM_TYPE_AREA.ordinal()){
            if (viewType== ITEM_TYPE.ITEM_TYPE_SCENE.ordinal()){
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.merchant_details_child_scene_recyler, viewGroup, false);
                return new MerchantDetailsAdapter.ItemRecyclerViewHolder(view);
            } else {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.merchant_details_child_recycler, viewGroup, false);
                return new MerchantDetailsAdapter.ItemRecyclerViewHolder(view);
            }
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.merchant_details_child_area, viewGroup, false);
            return new MerchantDetailsAdapter.AreaRecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof ItemRecyclerViewHolder){

            TextView titleTextView = ((ItemRecyclerViewHolder)viewHolder).titleTextView;
            RecyclerView recyclerView = ((ItemRecyclerViewHolder)viewHolder).recyclerView;
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);

            String item = tagArray.get(position);

            if (item=="wine"){
                titleTextView.setText("藏酒/主打");
                MerchantDetailsItemAdapter environmentAdapter =new MerchantDetailsItemAdapter(
                        mContext,R.layout.main_bartag_child_item,wineArray,1);
                recyclerView.setAdapter(environmentAdapter);
            } else if (item=="music"){
                titleTextView.setText("音乐调性");
                MerchantDetailsItemAdapter environmentAdapter =new MerchantDetailsItemAdapter(
                        mContext,R.layout.main_bartag_child_item,musicArray,2);
                recyclerView.setAdapter(environmentAdapter);
            }else if (item=="scene"){
                titleTextView.setText("环境风景");
                MerchantDetailsItemAdapter environmentAdapter =new MerchantDetailsItemAdapter(
                        mContext,R.layout.merchant_details_child_scene,sceneArray,3);
                recyclerView.setAdapter(environmentAdapter);

                environmentAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                        showAllSubject(sceneArray, position);
                    }

                    @Override
                    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                        return false;
                    }
                });

            }else if (item=="facilities"){
                titleTextView.setText("设施服务");
                MerchantDetailsItemAdapter environmentAdapter =new MerchantDetailsItemAdapter(
                        mContext,R.layout.main_bartag_child_item,facilitiesArray,4);
                recyclerView.setAdapter(environmentAdapter);
            }
        } else {
            TextView titleTextView = ((AreaRecyclerViewHolder)viewHolder).titleTextView;
            TextView valueTextView = ((AreaRecyclerViewHolder)viewHolder).valueTextView;
            HashMap<String,String> areaItem =areaArray.get(position-recyclerCount);
            titleTextView.setText(areaItem.get("title"));
            valueTextView.setText(areaItem.get("value"));
        }
    }

    @Override
    public int getItemViewType(int position) {
            String item = tagArray.get(position);

            if (item=="wine"){
                return MerchantDetailsAdapter.ITEM_TYPE.ITEM_TYPE_WINE.ordinal();

            } else if (item=="music"){
                return MerchantDetailsAdapter.ITEM_TYPE.ITEM_TYPE_MUSIC.ordinal();

            }else if (item=="scene"){

                return MerchantDetailsAdapter.ITEM_TYPE.ITEM_TYPE_SCENE.ordinal();
            }else if (item=="facilities"){

                return MerchantDetailsAdapter.ITEM_TYPE.ITEM_TYPE_FACILITIES.ordinal();
            }else{
                return MerchantDetailsAdapter.ITEM_TYPE.ITEM_TYPE_AREA.ordinal();
            }
    }

    @Override
    public int getItemCount() {
        return tagArray.size();
    }

    static class ItemRecyclerViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView titleTextView;
        public ItemRecyclerViewHolder(View itemView) {
            super(itemView);
            recyclerView = ButterKnife.findById(itemView, R.id.recyclerView);
            titleTextView = ButterKnife.findById(itemView, R.id.item_title);
        }
    }

    static class AreaRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        TextView valueTextView;
        public AreaRecyclerViewHolder(View itemView) {
            super(itemView);
            titleTextView = ButterKnife.findById(itemView, R.id.item_title);
            valueTextView = ButterKnife.findById(itemView, R.id.item_Value);
        }
    }

    public void showAllSubject(ArrayList<MerchantDetailsItem> pics, int clickpos) {
        if (null == mPopupWindow) {
            View layout = LayoutInflater.from(mContext).inflate(
                    R.layout.merchant_details_scene_popuwindow, null);
            layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
            int width = ScreenUtil.getScreenWidth(mContext);
            int height = ScreenUtil.getScreenHeight(mContext);

            //设置弹出部分和面积大小
            mPopupWindow = new PopupWindow(layout, width, height, true);
            //设置动画弹出效果
            mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mPopupWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);

            popupWindowSetupView(layout, pics, clickpos);
        }

        int[] pos = new int[2];

        mPopupWindow.showAtLocation(((AppCompatActivity) mContext).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP, pos[0], pos[1]);
    }

    void popupWindowSetupView(View view,ArrayList<MerchantDetailsItem> pics,int clickPos){
        int showPos=0;
        ArrayList<String> tempArray = new ArrayList<String>();
        final ArrayList<String> tipArray = new ArrayList<String>();
        for (int i=0;i<pics.size();i++){
            MerchantDetailsItem item = pics.get(i);

            if (item!=null){
                ArrayList<String> subItemArray = item.getPictures();
                if (subItemArray!=null&&subItemArray.size()>0){
                    if (i==clickPos){
                        showPos = tempArray.size();
                    }
                    tempArray.addAll(subItemArray);
                    for (int j=0;j<subItemArray.size();j++){
                        tipArray.add(item.getName()+""+(j+1)+"/"+subItemArray.size());
                    }
                }
            }
        }

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        final TextView sceneName = (TextView) view.findViewById(R.id.sceneName);

        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(mContext,tempArray);

        sceneViewPager.setAdapter(adapter);

        adapter.setOnItemClickListener(new MerchantScenePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mPopupWindow.dismiss();
            }
        });

        sceneViewPager.setCurrentItem(showPos);

        sceneViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                sceneName.setText(""+tipArray.get(position));
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
