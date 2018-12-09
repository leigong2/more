package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.main.ui.adapter.SignInterAdapter;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.ui.fragment.SignInterFragment;
import com.moreclub.moreapp.main.ui.fragment.SignSquareFragment;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.ucenter.ui.activity.PlayMoreActivity;
import com.moreclub.moreapp.util.DataCleanManager;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.app.Constants.BASE_URL;
import static com.moreclub.moreapp.main.constant.Constants.SIGNINTERACTIVITY;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH_TEMP;

public class SignInterActivity extends BaseActivity {

    @BindView(R.id.sign_inter_pager)
    ViewPager signInterPager;
    @BindView(R.id.sign_title)
    NavigationTabStrip signTitle;
    @BindView(R.id.iv_addpinzuo)
    ImageView ivSignInter;
    @BindView(R.id.ib_report)
    ImageButton ibReport;
    private String mid;
    public SignSquareFragment mSignSquareFragment;
    public SignInterFragment mSignInterFragment;
    private PopupWindow popupWindow;

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mSignInterFragment.signInterses.size(); i++) {
                if (mSignInterFragment.signInterses.get(i).getUid().equals(MoreUser.getInstance().getUid())) {
                    if (mSignInterFragment.signInterses.get(i).getExprise() == 2) {
                        Toast.makeText(getApplicationContext(), "您在此只能有一个约酒公告，请关闭后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    SignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(SignInterActivity.this, AddSignInterActivity.class);
            intent.putExtra("mid", mid);
            ActivityCompat.startActivity(SignInterActivity.this, intent, compat.toBundle());
        }
    };

    private View.OnClickListener myInterClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //Todo 进入签到互动详情
            boolean hasLoaded = false;
            int sid = -1;
            for (int i = 0; i < mSignInterFragment.signInterses.size(); i++) {
                if (MoreUser.getInstance() != null &&
                        mSignInterFragment != null && mSignInterFragment.signInterses != null &&
                        mSignInterFragment.signInterses.get(i) != null &&
                        MoreUser.getInstance().getUid().equals(mSignInterFragment.signInterses.get(i).getUid())) {
                    int exprise = mSignInterFragment.signInterses.get(i).getExprise();
                    if (exprise == 2) {
                        hasLoaded = true;
                        sid = mSignInterFragment.signInterses.get(i).getSid();
                    }
                }
            }
            if (!hasLoaded) {
                Toast.makeText(SignInterActivity.this, "您在当前酒吧还没有正在进行的拼座互动噢!", Toast.LENGTH_SHORT).show();
                return;
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    SignInterActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            Intent intent = new Intent(SignInterActivity.this, SignInterDetailActivity.class);
            intent.putExtra("sid", sid);
            intent.putExtra("tag", SIGNINTERACTIVITY);
            ActivityCompat.startActivity(SignInterActivity.this, intent, compat.toBundle());
        }
    };
    private View.OnClickListener howToPlayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = BASE_URL + "/web/open/h2p";
            Intent intent2 = new Intent(SignInterActivity.this, PlayMoreActivity.class);
            intent2.putExtra("webUrl", url);
            intent2.putExtra("webTitle", "怎么玩");
            ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeCustomAnimation(
                    SignInterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            ActivityCompat.startActivity(SignInterActivity.this, intent2, compat2.toBundle());
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mid = intent.getStringExtra("mid");
        if (mSignSquareFragment.xRecyclerView != null)
            mSignSquareFragment.xRecyclerView.refresh();
        if (mSignInterFragment.xRecyclerView != null)
            mSignInterFragment.xRecyclerView.refresh();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_inter_activity;
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
        Intent intent = getIntent();
        if (mid == null)
            mid = intent.getStringExtra("mid");
        String logo = intent.getStringExtra("logo");
        String desc = intent.getStringExtra("desc");
        String name = intent.getStringExtra("name");
        ArrayList<Fragment> fragments = new ArrayList<>();
        mSignSquareFragment = new SignSquareFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mid", mid);
        bundle.putString("logo", logo);
        bundle.putString("desc", desc);
        bundle.putString("name", name);
        mSignSquareFragment.setArguments(bundle);
        mSignInterFragment = new SignInterFragment();
        mSignInterFragment.setArguments(bundle);
        fragments.add(mSignSquareFragment);
        fragments.add(mSignInterFragment);
        PagerAdapter signInterAdapter = new SignInterAdapter(getSupportFragmentManager(), fragments);
        signInterPager.setAdapter(signInterAdapter);
        signInterPager.setOffscreenPageLimit(2);
        signTitle.setViewPager(signInterPager);
        ivSignInter.setOnClickListener(addListener);
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).
                inflate(R.layout.sign_inter_menu, null);
        TextView myInter = (TextView) bubbleLayout.findViewById(R.id.my_sign_inter);
        myInter.setOnClickListener(myInterClick);
        TextView howToPlay = (TextView) bubbleLayout.findViewById(R.id.how_to_play);
        howToPlay.setOnClickListener(howToPlayClick);
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);

    }

    @OnClick({R.id.nav_back, R.id.ib_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                SignInterActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.ib_report:
                //Todo 弹出
                int[] location = new int[2];
                view.getLocationInWindow(location);
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], view.getHeight() + location[1]);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mSignSquareFragment.adapter.mUserPopupWindow != null
                && mSignSquareFragment.adapter.mUserPopupWindow.isShowing()) {
            mSignSquareFragment.adapter.mUserPopupWindow.dismiss();
            DataCleanManager.cleanCustomCache(SAVE_REAL_PATH_TEMP);
            Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
            if (adapters != null && adapters.size() > 0) {
                adapters.clear();
            }
            mSignSquareFragment.adapter.hasGone = false;
        } else {
            super.onBackPressed();
            UpdateUser.getInstance().setAdapters(null);
        }
    }
}
