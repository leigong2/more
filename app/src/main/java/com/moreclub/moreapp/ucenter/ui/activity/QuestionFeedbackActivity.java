package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserFeedback;
import com.moreclub.moreapp.ucenter.presenter.FeedbackLoader;
import com.moreclub.moreapp.ucenter.presenter.IFeedbackLoader;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/16.
 */

public class QuestionFeedbackActivity extends BaseActivity implements
        IFeedbackLoader.LoaderFeedbackDataBinder<UserFeedback>,
        TextView.OnEditorActionListener{

    /**zune:文字不换行**/
    private final static int TEXT_LIMIT = 500;

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.feedback_et) EditText editText;
    @BindView(R.id.feenack_inputNumber) TextView textNumber;

    @BindString(R.string.feedback_title) String title;
    @BindString(R.string.feedback_content_empty) String empty;
    @BindString(R.string.feedback_resp_success) String success;
    @BindString(R.string.feedback_resp_failed) String failed;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_question_feedback;
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
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(title);

        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        editText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textNumber.setText(i2 + "/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart = editText.getSelectionStart();
                editEnd = editText.getSelectionEnd();
                if (temp.length() > TEXT_LIMIT) {//限制长度
                    TastyToast.makeText(getApplicationContext(),
                            "输入的字数已经超过了限制！",
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                    editable.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    editText.setText(editable);
                    editText.setSelection(tempSelection);
                    textNumber.setText("500/500");
                }
            }
        });
        editText.setOnEditorActionListener(this);

    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            QuestionFeedbackActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @OnClick(R.id.feedbackButton)
    void onClickSubmit() {
        submitFeedback();
    }

    @Override
    protected Class getLogicClazz() {
        return IFeedbackLoader.class;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onFeedbackResponse(Response<RespDto<Boolean>> response) {
        if (response.code() == 401) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }

        if (response.body().getData()) {
            editText.setText("");
            TastyToast.makeText(getApplicationContext(), success,
                    TastyToast.LENGTH_SHORT, TastyToast.INFO);
        }
    }

    @Override
    public void onFeedbackFailure(String msg) {
        TastyToast.makeText(getApplicationContext(), failed,
                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                submitFeedback();
                InputMethodUtils.closeSoftKeyboard(QuestionFeedbackActivity.this);
                return true;
        }
        return false;
    }

    private void submitFeedback() {
        String feedbackText = editText.getText().toString();
        if (!TextUtils.isEmpty(feedbackText)) {
            UserFeedback feedback = new UserFeedback();
            feedback.setUid(MoreUser.getInstance().getUid());
            feedback.setContent(feedbackText);
            ((FeedbackLoader)mPresenter).userPostFeedback(feedback);
        } else {
            TastyToast.makeText(getApplicationContext(), empty,
                    TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }
}
