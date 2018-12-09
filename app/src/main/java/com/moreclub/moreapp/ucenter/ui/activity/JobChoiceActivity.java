package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.tag.FlowLayout;
import com.moreclub.common.ui.view.tag.TagAdapter;
import com.moreclub.common.ui.view.tag.TagFlowLayout;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserTag;
import com.moreclub.moreapp.ucenter.presenter.IJobTagLoader;
import com.moreclub.moreapp.ucenter.presenter.JobTagDataLoader;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.ucenter.constant.Constants.MODAL_ARRAY;

public class JobChoiceActivity extends BaseActivity implements IJobTagLoader.JobTagDataBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.job_choice_lay)
    TagFlowLayout jobChoiceLay;
    @BindView(R.id.jobs_lay)
    TagFlowLayout jobsLay;
    private List<UserTag> userChoiceTags;
    private TagAdapter tagAdapter;
    private int jobId;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_job_choice;
    }

    @Override
    protected Class getLogicClazz() {
        return IJobTagLoader.class;
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
        userChoiceTags = new ArrayList<>();
        String jobs = getIntent().getStringExtra("job");
        jobId = getIntent().getIntExtra("jobId",-1);
        if (!TextUtils.isEmpty(jobs)) {
            String[] jobArr = jobs.split(" ");
            for (int i = 0; i < jobArr.length; i++) {
                UserTag userTag = new UserTag();
                userTag.setName(jobArr[i]);
                userTag.setId(jobId);
                userChoiceTags.add(userTag);
            }
        }
        activityTitle.setText("职业");
        String modal = MODAL_ARRAY[2];
        tagAdapter = setTagAdapter(jobChoiceLay, userChoiceTags, true);
        ((JobTagDataLoader) mPresenter).loadTag(modal);
    }

    @Override
    public void onTagResponse(RespDto<Map<String, List<UserTag>>> res) {
        Logger.i("zune:", "res = " + res);
        Map<String, List<UserTag>> data = res.getData();
        List<UserTag> userTags = data.get(MODAL_ARRAY[2]);
        setTagAdapter(jobsLay, userTags, false);
    }

    @Override
    public void onTagFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
    }

    private TagAdapter setTagAdapter(final TagFlowLayout jobsLay, final List<UserTag> userTags, final boolean isChoice) {
        TagAdapter adapter = new TagAdapter<UserTag>(userTags) {
            @Override
            public View getView(FlowLayout parent, int position, UserTag userTag) {
                TextView tv = (TextView) getLayoutInflater().inflate(
                        R.layout.searchentry_tag_tv, jobsLay, false);
                int padding = ScreenUtil.dp2px(JobChoiceActivity.this, 9);
                tv.setPadding(padding,ScreenUtil.dp2px(JobChoiceActivity.this, 6),padding,padding);
                ViewGroup.MarginLayoutParams
                        layoutParams = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
                layoutParams.setMargins(3,3,3,3);
                tv.setLayoutParams(layoutParams);
                if (isChoice) {
                    String name = userTags.get(position).getName() + "   ";
                    Drawable drawable = getResources().getDrawable(R.drawable.cross_profiltag);
                    int drawableMinimumWidth = drawable.getMinimumWidth();
                    int minimumHeight = drawable.getMinimumHeight();
                    int height = 12 + ScreenUtil.dp2px(JobChoiceActivity.this, 12);
                    drawable.setBounds(0, 0, drawableMinimumWidth * height / minimumHeight, height);
                    ImageSpan is = new StickerSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
                    int strLength = name.length();
                    SpannableString ss = new SpannableString(name);
                    ss.setSpan(is, strLength - 1, strLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tv.setText(ss.subSequence(0, strLength));
                    tv.setBackgroundResource(R.drawable.job_choice_background);
                } else {
                    tv.setText(userTags.get(position).getName());
                }
                return tv;
            }
        };
        jobsLay.setAdapter(adapter);
        jobsLay.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                UserTag userTag = userTags.get(position);
                if (isChoice) {
                    userChoiceTags.remove(position);
                } else if (userChoiceTags.size() == 0) {
                    userChoiceTags.add(userTag);
                } else {
                    Toast.makeText(getApplicationContext(), "您最多只可以选择1个标签", Toast.LENGTH_SHORT).show();
                }
                tagAdapter.notifyDataChanged();
                return false;
            }
        });
        return adapter;
    }

    @OnClick({R.id.nav_back, R.id.saveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.saveButton:
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent();
                if (userChoiceTags != null && userChoiceTags.size() > 0) {
                    intent.putExtra("job", userChoiceTags.get(0).getName());
                    intent.putExtra("jobId", userChoiceTags.get(0).getId());
                }
                JobChoiceActivity.this.setResult(6, intent);
                JobChoiceActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
        }
    }

    private class StickerSpan extends ImageSpan {
        public StickerSpan(Drawable b, int verticalAlignment) {
            super(b, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();
            int transY = (int) (bottom - b.getBounds().bottom - ScreenUtil.dp2px(JobChoiceActivity.this, 0));// 减去行间距
            if (mVerticalAlignment == ALIGN_BASELINE) {
                int textLength = text.length();
                for (int i = 0; i < textLength; i++) {
                    if (Character.isLetterOrDigit(text.charAt(i))) {
                        transY -= paint.getFontMetricsInt().descent;
                        break;
                    }
                }
            }
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }
}
