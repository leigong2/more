package com.moreclub.moreapp.ucenter.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moreclub.common.log.Logger;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.ucenter.model.AddressItem;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.ta.utdid2.android.utils.SystemUtils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BirthdayChoiceActivity extends BaseActivity {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    private RelativeLayout birthdayLayout;
    private RelativeLayout starLayout;
    private RelativeLayout ageLayout;
    private TextView birthdayTag;
    private TextView starTag;
    private TextView ageTag;
    private EditText birthday;
    private EditText star;
    private EditText age;
    private DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthday.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
            setAgeDatas(birthday.getText().toString());
        }
    };
    private View.OnClickListener birthdayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar start = Calendar.getInstance();
            int year0;
            int month0;
            int day0;
            try {
                year0 = Integer.parseInt(birthday.getText().toString().split("年")[0]);
                month0 = Integer.parseInt(birthday.getText().toString().split("年")[1].toString().split("月")[0]) - 1;
                day0 = Integer.parseInt(birthday.getText().toString().split("年")[1].toString().split("月")[1].toString().split("日")[0]);
            } catch (Exception e) {
                year0 = 1990;
                month0 = 1;
                day0 = 1;
            }
            start.set(year0, month0, day0, 0, 0, 0);
            DatePickerDialog dialog = new DatePickerDialog(BirthdayChoiceActivity.this
                    , AlertDialog.THEME_HOLO_LIGHT, callBack, year0, month0, day0);
            dialog.getDatePicker().setCalendarViewShown(false);
            dialog.show();
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.birthday_choice_activity;
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
        String birthday = getIntent().getStringExtra("birthday");
        birthdayLayout = (RelativeLayout) findViewById(R.id.birthday_lay);
        starLayout = (RelativeLayout) findViewById(R.id.star_lay);
        ageLayout = (RelativeLayout) findViewById(R.id.age_lay);
        this.birthday = (EditText) birthdayLayout.findViewById(R.id.user_sex);
        this.birthday.setOnClickListener(birthdayClick);
        birthdayLayout.setOnClickListener(birthdayClick);
        birthdayTag = (TextView) birthdayLayout.findViewById(R.id.user_sex_tag);
        birthdayTag.setText("生日");
        starTag = (TextView) starLayout.findViewById(R.id.user_sex_tag);
        starTag.setText("星座");
        ageTag = (TextView) ageLayout.findViewById(R.id.user_sex_tag);
        ageTag.setText("年龄");

        if (!TextUtils.isEmpty(birthday))
            this.birthday.setText(birthday);
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            String format = sdf.format(date);
            this.birthday.setText("1990年01月01日");
        }
        star = (EditText) starLayout.findViewById(R.id.user_sex);
        age = (EditText) ageLayout.findViewById(R.id.user_sex);
        String birthdayTime = this.birthday.getText().toString();
        setAgeDatas(birthdayTime);
        activityTitle.setText("生日");
    }

    private void setAgeDatas(String birthdayTime) {
        String starSeat;
        try {
            String year = birthdayTime.split("年")[0].toString();
            String month = birthdayTime.split("年")[1].toString().split("月")[0];
            String day = birthdayTime.split("年")[1].toString().split("月")[1].toString().split("日")[0];
            starSeat = TimeUtils.getStarSeat(Integer.parseInt(month), Integer.parseInt(day));
        } catch (Exception e) {
            Logger.i("zune:", "Integer parse exception = " + e);
            starSeat = "";
        }
        star.setText(starSeat);
        int myAge;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date parse;
        try {
            parse = sdf.parse(birthdayTime);
        } catch (ParseException e) {
            e.printStackTrace();
            parse = new Date(System.currentTimeMillis());
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf1.format(parse);
        try {
            myAge = TimeUtils.getAge(format);
        } catch (Exception e) {
            e.printStackTrace();
            myAge = 0;
        }
        age.setText(myAge + "");
    }

    @OnClick({R.id.nav_back, R.id.saveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
            case R.id.saveButton:
                saveUsers();
                break;
        }
    }

    private void saveUsers() {
        String myBirthday = birthday.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("birthday", myBirthday);
        setResult(5, intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }
}
