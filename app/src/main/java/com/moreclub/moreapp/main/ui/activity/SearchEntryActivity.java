package com.moreclub.moreapp.main.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.common.ui.view.tag.FlowLayout;
import com.moreclub.common.ui.view.tag.TagAdapter;
import com.moreclub.common.ui.view.tag.TagFlowLayout;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.main.presenter.ISearchEntryDataLoader;
import com.moreclub.moreapp.main.presenter.SearchEntryDataLoader;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Administrator on 2017/3/2.
 */

public class SearchEntryActivity extends BaseListActivity implements
        ISearchEntryDataLoader.SerchEntryDataBinder<TagDict>{

    private final static String KEY_HISTORY = "search.history";
    private LayoutInflater inflater;

    @BindView(R.id.searchentry_hotview)
    TagFlowLayout flowLayout;

    @BindView(R.id.searchentry_historyview)
    TagFlowLayout historyFlowLayout;

    @BindView(R.id.searchentry_et)
    EditText editText;

    @Override
    protected int getLayoutResource() {
        return R.layout.searchentry_activity;
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

        inflater = LayoutInflater.from(this);
        editText.requestFocus();

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                showKeyboard();
            }
        }, 500);

        ((SearchEntryDataLoader)mPresenter).onLoadSearchTag();

        initSearchHistory();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        ((SearchEntryDataLoader)mPresenter).onLoadSearchTag();
    }

    @Override
    protected Class getLogicClazz() {
        return ISearchEntryDataLoader.class;
    }

    @OnClick(R.id.searchentry_close)
    void onClickClose() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
    }

    @OnClick(R.id.searchentry_clearhistory)
    void onClickClear() {
        PrefsUtils.getEditor(this).remove(KEY_HISTORY).commit();
        setHistoryApdater(new ArrayList<String>());
    }

    @OnClick(R.id.searchentry_search_text)
    void onClickSearch() {
        String searchText = editText.getText().toString();
        if (!TextUtils.isEmpty(searchText)) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(this,
                    R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(SearchEntryActivity.this,
                    TotalBarsActivity.class);
            intent.putExtra("keyword", searchText);
            ActivityCompat.startActivity(this, intent, compat.toBundle());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
    }

    @Override
    public void onSearchTagResponse(RespDto<List<TagDict>> response) {
        List<TagDict> dicts = response.getData();
        if (dicts != null && !dicts.isEmpty()) {
            setTagAdapter(dicts);
        }
    }

    @Override
    public void onSearchTagFailure(String msg) {

    }

    private void setTagAdapter(final List<TagDict> dicts) {
        flowLayout.setAdapter(new TagAdapter<TagDict>(dicts) {
            @Override
            public View getView(FlowLayout parent, int position, TagDict tag) {
                TextView tv = (TextView) inflater.inflate(
                        R.layout.searchentry_tag_tv,
                        flowLayout, false);
                tv.setText(tag.getName());
                return tv;
            }
        });

        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(SearchEntryActivity.this,MerchantListViewActivity.class);
                intent.putExtra("searchKey", dicts.get(position).getName());
                intent.putExtra("searchType", dicts.get(position).getId());
                startActivity(intent);
                return false;
            }
        });
    }

    private void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initSearchHistory() {
        HistoryTask task = new HistoryTask(getApplication());
        task.execute();
    }

    private class HistoryTask extends AsyncTask<Void, Void, List<String>> {
        private Context context;

        public HistoryTask(Context context){
            this.context = context;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            String history = PrefsUtils.getString(context, KEY_HISTORY, "");

            if (!TextUtils.isEmpty(history)){
                List<String> list = new ArrayList<>();
                for(String item : history.split(",")) {
                    list.add(item);
                }
                return list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (strings != null) {
                setHistoryApdater(strings);
            }
        }
    }

    private void setHistoryApdater(final List<String> strings) {
        historyFlowLayout.setAdapter(new TagAdapter<String>(strings) {
            @Override
            public View getView(FlowLayout parent, int position, String tag) {
                TextView tv = (TextView) inflater.inflate(
                        R.layout.searchentry_tag_tv,
                        historyFlowLayout, false);
                tv.setText(tag);
                return tv;
            }
        });

        historyFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(SearchEntryActivity.this,
                        TotalBarsActivity.class);
                intent.putExtra("keyword", strings.get(position));
                startActivity(intent);
                return false;
            }
        });
    }
}
