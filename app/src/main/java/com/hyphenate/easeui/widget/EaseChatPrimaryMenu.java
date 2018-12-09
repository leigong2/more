package com.hyphenate.easeui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moreclub.moreapp.R;
import com.hyphenate.util.EMLog;

/**
 * primary menu
 */
public class EaseChatPrimaryMenu extends EaseChatPrimaryMenuBase implements OnClickListener {
    private EditText editText;
    //    private View buttonSetModeKeyboard;
    private RelativeLayout edittext_layout;
    //    private View buttonSetModeVoice;
    private View buttonSend;
    private LinearLayout smilesButton;
    private LinearLayout voiceButton;
    private LinearLayout picButton;
    private LinearLayout vedioButton;
    private LinearLayout locationButton;

    //    private View buttonPressToSpeak;
//    private ImageView faceNormal;
//    private ImageView faceChecked;
//    private Button buttonMore;
    private boolean ctrlPress = false;

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EaseChatPrimaryMenu(Context context) {
        super(context);
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        Context context1 = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_primary_menu, this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
//        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
//        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
//        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
//        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
//        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
//        RelativeLayout faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
//        buttonMore = (Button) findViewById(R.id.btn_more);
//        edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);

        smilesButton = (LinearLayout) findViewById(R.id.smilesButton);
        voiceButton = (LinearLayout) findViewById(R.id.voiceButton);
        picButton = (LinearLayout) findViewById(R.id.picButton);
        vedioButton = (LinearLayout) findViewById(R.id.vedioButton);
        locationButton = (LinearLayout) findViewById(R.id.locationButton);

        buttonSend.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        picButton.setOnClickListener(this);
        vedioButton.setOnClickListener(this);
        smilesButton.setOnClickListener(this);
        editText.setOnClickListener(this);

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        // listen the text change
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
//                    buttonMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
//                    buttonMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EMLog.d("key", "keyCode:" + keyCode + " action:" + event.getAction());

                // test on Mac virtual machine: ctrl map to KEYCODE_UNKNOWN
                if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        ctrlPress = true;
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        ctrlPress = false;
                    }
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                EMLog.d("key", "keyCode:" + event.getKeyCode() + " action" + event.getAction() + " ctrl:" + ctrlPress);
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                ctrlPress == true)) {
                    String s = editText.getText().toString();
                    editText.setText("");
                    listener.onSendBtnClicked(s);
                    return true;
                } else {
                    return false;
                }
            }
        });


//        buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(listener != null){
//                    return listener.onPressToSpeakBtnTouch(v, event);
//                }
//                return false;
//            }
//        });
    }

    /**
     * set recorder view when speak icon is touched
     *
     * @param voiceRecorderView
     */
    public void setPressToSpeakRecorderView(EaseVoiceRecorderView voiceRecorderView) {
        EaseVoiceRecorderView voiceRecorderView1 = voiceRecorderView;
    }

    /**
     * append emoji icon to editText
     *
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent) {
        editText.append(emojiContent);
    }

    /**
     * delete emojicon
     */
    public void onEmojiconDeleteEvent() {
        if (!TextUtils.isEmpty(editText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }

    /**
     * on clicked event
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_send) {
            if (listener != null) {
                String s = editText.getText().toString();
                editText.setText("");
                listener.onSendBtnClicked(s);
            }
        } else if (id == R.id.voiceButton) {
            setModeVoice();
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleExtendClicked();
        } else if (id == R.id.et_sendmessage) {
            if (listener != null)
                listener.onEditTextClicked();
        } else if (id == R.id.smilesButton) {
            toggleFaceImage();
            if (listener != null) {
                listener.onToggleEmojiconClicked();
            }
        } else if(id==R.id.picButton){
            if (listener != null) {
                listener.onTogglePictureClicked();
            }
        } else if (id==R.id.vedioButton){
            if (listener != null) {
                listener.onToggleVedioClicked();
            }
        } else if (id==R.id.locationButton){
            if (listener != null) {
                listener.onToggleAddressClicked();
            }
        }
    }


    /**
     * show voice icon when speak bar is touched
     */
    protected void setModeVoice() {
        hideKeyboard();
//        edittext_layout.setVisibility(View.GONE);
//        buttonSetModeVoice.setVisibility(View.GONE);
//        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
//        buttonMore.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(View.VISIBLE);
//        faceNormal.setVisibility(View.VISIBLE);
//        faceChecked.setVisibility(View.INVISIBLE);

    }

    /**
     * show keyboard
     */
    protected void setModeKeyboard() {
        edittext_layout.setVisibility(View.VISIBLE);
//        buttonSetModeKeyboard.setVisibility(View.GONE);
//        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        editText.requestFocus();
        buttonSend.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(View.GONE);
//        if (TextUtils.isEmpty(editText.getText())) {
//            buttonMore.setVisibility(View.VISIBLE);
//            buttonSend.setVisibility(View.GONE);
//        } else {
//            buttonMore.setVisibility(View.GONE);
//            buttonSend.setVisibility(View.VISIBLE);
//        }

    }


    protected void toggleFaceImage() {
//        if(faceNormal.getVisibility() == View.VISIBLE){
//            showSelectedFaceImage();
//        }else{
//            showNormalFaceImage();
//        }
    }

    private void showNormalFaceImage() {
//        faceNormal.setVisibility(View.VISIBLE);
//        faceChecked.setVisibility(View.INVISIBLE);
    }

    private void showSelectedFaceImage() {
//        faceNormal.setVisibility(View.INVISIBLE);
//        faceChecked.setVisibility(View.VISIBLE);
    }


    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();
    }

    @Override
    public void onTextInsert(CharSequence text) {
        int start = editText.getSelectionStart();
        Editable editable = editText.getEditableText();
        editable.insert(start, text);
        setModeKeyboard();
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

}
