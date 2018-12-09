package com.hyphenate.easeui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.moreclub.moreapp.R;
import com.hyphenate.easeui.model.EaseVoiceRecorder;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.moreclub.moreapp.util.ClickUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Voice recorder view
 *
 */
public class EaseVoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected EaseVoiceRecorder voiceRecorder;
    protected PowerManager.WakeLock wakeLock;

    protected TextView recorderTip;
    protected ImageView recordButton;
    private boolean upClearRecord;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (!upClearRecord) {
                if (msg.what > 0) {
                    recorderTip.setText("上拉取消 (" + msg.what + "')");
                }
            }
        }
    };

    public EaseVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        upClearRecord = false;
        View view =LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_recorder, this);
        recorderTip = (TextView) view.findViewById(R.id.recorder_tip);
        recordButton = (ImageView) view.findViewById(R.id.record_button);

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    /**
     * on speak button touched
     * 
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            try {
                if (EaseChatRowVoicePlayClickListener.isPlaying)
                    EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                startRecording();
            } catch (Exception e) {
                e.printStackTrace();
            }

            recordButton.setImageResource(R.drawable.record_step2);
            return true;
        case MotionEvent.ACTION_MOVE:
            if (event.getY() < 0) {
                showReleaseToCancelHint();
                recordButton.setImageResource(R.drawable.record_step3);
            } else {
                showMoveUpToCancelHint();
                recordButton.setImageResource(R.drawable.record_step2);
            }
            return true;
        case MotionEvent.ACTION_UP:
            if (event.getY() < 0) {
                // discard the recorded audio.
                discardRecording();
            } else {
                // stop recording and send voice file
                try {
                    int length = stopRecoding();
                    if (length > 0) {
                        if (recorderCallback != null) {
                            recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            recorderTip.setText("长按录音");
                        }
                    } else if (length == EMError.FILE_INVALID) {
                        recorderTip.setText(context.getString(R.string.Recording_without_permission));
                    } else {
                        recorderTip.setText(context.getString(R.string.The_recording_time_is_too_short));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    recorderTip.setText(context.getString(R.string.Recording_without_permission));
                }
            }
            recordButton.setImageResource(R.drawable.record_step1);
            return true;
        default:
            discardRecording();
            recordButton.setImageResource(R.drawable.record_step1);
            return false;
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         * 
         * @param voiceFilePath
         *            录音完毕后的文件路径
         * @param voiceTimeLength
         *            录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!EaseCommonUtils.isSdcardExist()) {
            recorderTip.setText(context.getString(R.string.Send_voice_need_sdcard_support));
            return;
        }
        if (ClickUtils.isRecordClick()) {
            return ;
        }
        try {
            wakeLock.acquire();
            upClearRecord = false;
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            recorderTip.setText(context.getString(R.string.Recording_without_permission));
            return;
        }
    }

    public void showReleaseToCancelHint() {
        upClearRecord = true;
        recorderTip.setText("松开手指，取消发送");
    }

    public void showMoveUpToCancelHint() {

    }

    public void discardRecording() {
        recorderTip.setText("长按录音");
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
