package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.log.Logger;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.SecurityQuestions;
import com.moreclub.moreapp.ucenter.model.UserAnswer;
import com.moreclub.moreapp.ucenter.presenter.IQuestionLoader;
import com.moreclub.moreapp.ucenter.presenter.QuestionLoader;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecurityQuestionActivity extends BaseActivity implements IQuestionLoader.SecurityQuestionBinder<List<String>> {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.view_error_line2)
    View viewErrorLine2;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.iv_spread)
    ImageView ivSpread;
    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.ll_questions)
    LinearLayout llQuestions;
    private boolean isSpread;
    private List<String> questions = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();
    private TextWatcher listener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.security_question_activity;
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

    @Override
    protected Class getLogicClazz() {
        return IQuestionLoader.class;
    }


    private void setupView() {
        ((QuestionLoader) mPresenter).loadQuestions();
        activityTitle.setText("密保问题");
        ivSpread.setRotation(90);
        etPhoneNumber.addTextChangedListener(listener);
    }


    @OnClick({R.id.nav_back, R.id.rename_make_sure, R.id.iv_spread})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                SecurityQuestionActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.rename_make_sure:
                if (!TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())) {
                    UserAnswer answer = new UserAnswer();
                    int position = getVisibleViewPosition(llQuestions);
                    answer.setAnswer(etPhoneNumber.getText().toString().trim());
                    answer.setCode(ids.get(position));
                    answer.setUid(MoreUser.getInstance().getUid());
                    ((QuestionLoader) mPresenter).postSecurityAnswer(answer, MoreUser.getInstance().getAccess_token());
                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("答案不能为空");
                }
                break;
            case R.id.iv_spread:
                isSpread = !isSpread;
                switchTranslate(ScreenUtil.dp2px(SecurityQuestionActivity.this, 46 * (questions.size() - 1)), isSpread);
                switchVisible(0, isSpread);
                break;

        }
    }

    private void initVisible(LinearLayout views) {
        if (views.getChildCount() > 1) {
            views.getChildAt(0).setVisibility(View.VISIBLE);
            for (int i = 0; i < views.getChildCount(); i++) {
                if (i == 0)
                    views.getChildAt(i).setVisibility(View.VISIBLE);
                else
                    views.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    private void switchTranslate(int distance, boolean isSpread) {
        if (isSpread) {
            tvAnswer.setTranslationY(distance);
            etPhoneNumber.setTranslationY(distance);
            divider.setTranslationY(distance);
            ivSpread.setRotation(-90);
        } else {
            tvAnswer.setTranslationY(0);
            etPhoneNumber.setTranslationY(0);
            divider.setTranslationY(0);
            ivSpread.setRotation(90);
        }
    }

    private void switchVisible(int position, boolean isSpread) {
        if (isSpread) {
            for (int i = 0; i < llQuestions.getChildCount(); i++) {
                llQuestions.getChildAt(i).setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = 0; i < llQuestions.getChildCount(); i++) {
                if (position == i)
                    llQuestions.getChildAt(i).setVisibility(View.VISIBLE);
                else
                    llQuestions.getChildAt(i).setVisibility(View.GONE);
            }
        }

    }

    private int getVisibleViewPosition(LinearLayout llQuestions) {
        if (!isSpread) {
            for (int i = 0; i < llQuestions.getChildCount(); i++) {
                if (llQuestions.getChildAt(i).getVisibility() == View.VISIBLE) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onQuestionLoadResponse(RespDto<SecurityQuestions> response) {
        Logger.i("zune:", "response = " + response.getData().toString());
        SecurityQuestions data = response.getData();
        List<SecurityQuestions.SecuritySubjectBean> security_subject = data.getSecurity_subject();
        for (SecurityQuestions.SecuritySubjectBean securitySubjectBean : security_subject) {
            String name = securitySubjectBean.getName();
            int id = securitySubjectBean.getId();
            questions.add(name);
            ids.add(id);
        }
        for (int i = 0; i < questions.size(); i++) {
            View child = View.inflate(SecurityQuestionActivity.this, R.layout.security_questions, null);
            TextView tvQuestionName = (TextView) child.findViewById(R.id.tv_question_name);
            tvQuestionName.setText(questions.get(i));
            final int finalI = i;
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSpread = !isSpread;
                    switchTranslate(ScreenUtil.dp2px(SecurityQuestionActivity.this, 46 * (questions.size() - 1)), isSpread);
                    switchVisible(finalI, isSpread);
                }
            };
            tvQuestionName.setOnClickListener(listener);
            llQuestions.addView(child);
            initVisible(llQuestions);
        }

    }

    @Override
    public void onQuestionLoadFailure(String msg) {
        Logger.i("zune:", "获取失败msg = " + msg);
    }

    @Override
    public void onPostAnswerResponse(RespDto<?> response) {
        Toast.makeText(SecurityQuestionActivity.this, "密保问题设置成功", Toast.LENGTH_SHORT).show();
        SecurityQuestionActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onPostAnswerFailure(String msg) {
        Toast.makeText(SecurityQuestionActivity.this, "密保问题设置失败msg = " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(SecurityQuestionActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
