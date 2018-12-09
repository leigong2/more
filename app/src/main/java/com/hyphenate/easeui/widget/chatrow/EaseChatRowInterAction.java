package com.hyphenate.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.ui.view.CenterTextView;
import com.moreclub.moreapp.main.constant.Constants;

import java.util.Date;


public class EaseChatRowInterAction extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected Activity activity;

    public String nickName;
    private CenterTextView contentView;

    public EaseChatRowInterAction(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        onInflateView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        onFindViewById();
    }

    /**
     * set property according message and postion
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position) {
        this.message = message;
        this.position = position;
        setUpBaseView();
        onSetUpView();
    }

    private void setUpBaseView() {
        // set nickname, avatar and background of bubble
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (message == null)
                    return;
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        //set nickname and avatar
        if (message.direct() == Direct.SEND) {
            String text = ((EMTextMessageBody) message.getBody()).getMessage();
            contentView.setText(text);
        } else {
            String message = ((EMTextMessageBody) this.message.getBody()).getMessage();
            switch (message) {
                case Constants.TITLE_INTEREST:
                    contentView.setText(Constants.TITLE_INTEREST);
                    break;
                case Constants.TITLE_REPLY:
                    contentView.setText(Constants.TITLE_REPLY);
                    break;
                case Constants.TITLE_ANSWER_REPLY_SELF:
                    contentView.setText(Constants.TITLE_ANSWER_REPLY);
                    break;
                case Constants.TITLE_ANSWER_INTEREST_SELF:
                    contentView.setText(Constants.TITLE_ANSWER_INTEREST);
                    break;
            }
        }
    }


    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_interaction_message, this);
    }

    /**
     * find view by id
     */
    protected void onFindViewById() {
        contentView = (CenterTextView) findViewById(R.id.tv_chatcontent);
    }


    /**
     * setup view
     */
    protected void onSetUpView() {
        // 设置内容
        if (message == null)
            return;
    }

    /**
     * on bubble clicked
     */
    protected void onBubbleClick() {

    }

}
