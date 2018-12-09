package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
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
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.SecurityQuestions;
import com.moreclub.moreapp.ucenter.model.UserCheckAnswer;
import com.moreclub.moreapp.ucenter.model.UserCheckAnswerResp;
import com.moreclub.moreapp.ucenter.presenter.CheckAnswerLoader;
import com.moreclub.moreapp.ucenter.presenter.ICheckAnswerLoader;
import com.moreclub.moreapp.ucenter.presenter.IQuestionLoader;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCheckAnswerActivity extends BaseActivity implements ICheckAnswerLoader.LoadCheckAnswerBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.ll_questions)
    LinearLayout llQuestions;
    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.iv_spread)
    ImageView ivSpread;
    @BindView(R.id.divider)
    View divider;
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
    private String question;

    @Override
    protected int getLayoutResource() {
        return R.layout.security_question_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return ICheckAnswerLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupViews();
    }

    private void setupViews() {
        activityTitle.setText("密保问题");
        question = getIntent().getStringExtra("question");
        ivSpread.setRotation(90);
        etPhoneNumber.addTextChangedListener(listener);
        ((CheckAnswerLoader) mPresenter).loadQuestions();
    }

    @Override
    public void onCheckAnswerResponse(RespDto<String> response) {
        boolean isSuccess = false;
        if (llQuestions.getChildCount() == 0 || TextUtils.isEmpty(question)) {
            Toast.makeText(UserCheckAnswerActivity.this, "密保问题答案不匹配", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < llQuestions.getChildCount(); i++) {
                if (llQuestions.getChildAt(i).getVisibility() == View.VISIBLE) {
                    LinearLayout childAt = (LinearLayout) llQuestions.getChildAt(i);
                    for (int i1 = 0; i1 < childAt.getChildCount(); i1++) {
                        if (childAt.getChildAt(i1) instanceof TextView) {
                            if (!TextUtils.isEmpty(question) &&
                                    ((TextView) (childAt.getChildAt(i1))).getText() != null &&
                                    ((TextView) (childAt.getChildAt(i1))).getText().toString().equals(question)) {
                                isSuccess = true;
                            }
                        }
                    }
                }
            }
            if (response.isSuccess() && isSuccess) {
                Intent intent = new Intent(UserCheckAnswerActivity.this, ConfirmPasswordActivity.class);
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                UserCheckAnswerActivity.this.startActivity(intent);
            } else {
                Toast.makeText(UserCheckAnswerActivity.this, "密保问题答案不匹配", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCheckAnswerFailure(String msg) {
        Toast.makeText(UserCheckAnswerActivity.this, "校验失败msg = " + msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.nav_back, R.id.rename_make_sure, R.id.iv_spread})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                UserCheckAnswerActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.rename_make_sure:
                String phone = getIntent().getStringExtra("phone");
                UserCheckAnswer answer = new UserCheckAnswer();
                int timestamp = getIntent().getIntExtra("timestamp", -1);
                String etAnswer = etPhoneNumber.getText().toString().trim();
                HashMap<String, String> map = new HashMap<>();
                map.put("fp", "1");
                map.put("timestamp", timestamp + "");
                map.put("phone", phone);
                map.put("appkey", Constants.MORE_APPKEY);
                map.put("appVersion", Constants.APP_version);
                String sign = SecurityUtils.createSign(map, "5698250a71fe54ab1ad004890229dfa75efb6d6c");
                answer.setUid(0);
                answer.setTimestamp(timestamp);
                answer.setSign(sign);
                answer.setPhone(phone);
                answer.setAnswer(etAnswer);
                answer.setAppkey(Constants.MORE_APPKEY);
                answer.setAppVersion(Constants.APP_version);
                ((CheckAnswerLoader) mPresenter).onCheckAnswerLoader("open", answer);
                break;
            case R.id.iv_spread:
                isSpread = !isSpread;
                switchTranslate(ScreenUtil.dp2px(UserCheckAnswerActivity.this, 46 * (questions.size() - 1)), isSpread);
                switchVisible(0, isSpread);
                break;
        }
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
            View child = View.inflate(UserCheckAnswerActivity.this, R.layout.security_questions, null);
            TextView tvQuestionName = (TextView) child.findViewById(R.id.tv_question_name);
            tvQuestionName.setText(questions.get(i));
            final int finalI = i;
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSpread = !isSpread;
                    switchTranslate(ScreenUtil.dp2px(UserCheckAnswerActivity.this, 46 * (questions.size() - 1)), isSpread);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(UserCheckAnswerActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
