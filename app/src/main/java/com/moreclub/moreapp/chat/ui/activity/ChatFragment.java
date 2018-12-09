package com.moreclub.moreapp.chat.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;
import com.moreclub.moreapp.chat.constant.Constant;
import com.moreclub.moreapp.chat.widget.ChatRowVoiceCall;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {

    /**
     * if it is chatBot 
     */
    private boolean isRobot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {

        }
        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), SuperMainActivity.class);
                    intent.putExtra("shouldGo","MessageCenterFragment");
                    startActivity(intent);
                }
                onBackPressed();
            }
        });
//        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if(chatType == EaseConstant.CHATTYPE_GROUP){
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    
                }
                @Override
                public void afterTextChanged(Editable s) {
                    
                } 
            });
        }
    }
    
    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        inputMenu.registerExtendMenuItem( extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
        //end of red packet code
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
            case REQUEST_CODE_SELECT_VIDEO: //send the video
                if (data != null) {

                }
                break;
            case REQUEST_CODE_SELECT_FILE: //send the file
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFileByUri(uri);
                    }
                }
                break;
            case REQUEST_CODE_SELECT_AT_USER:
                if(data != null){
                    String username = data.getStringExtra("username");
                    inputAtUsername(username, false);
                }
                break;
            default:
                break;
            }
        }
        
    }
    
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if(isRobot){
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
    }
    
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }
  

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar

    }
    
    @Override
    public void onAvatarLongClick(EMMessage message) {

    }
    
    
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        //red packet code : 拆红包页面

        //end of red packet code
        return false;
    }
    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //red packet code : 处理红包回执透传消息
        //end of red packet code
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
        case ITEM_VIDEO:
            Intent intent = new Intent(getActivity(), ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
            break;
        case ITEM_FILE: //file
            selectFileFromLocal();
            break;
        case ITEM_VOICE_CALL:
            startVoiceCall();
            break;
        case ITEM_VIDEO_CALL:
            startVideoCall();
            break;
        default:
            break;
        }
        //keep exist extend menu
        return false;
    }
    
    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
    
    /**
     * make a voice call
     */
    protected void startVoiceCall() {

    }
    
    /**
     * make a video call
     */
    protected void startVideoCall() {

    }

    @Override
    public void onDismiss(Object o) {

    }

    /**
     * chat row provider 
     *
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
        	//which is used to count the number of different chat row
            return 10;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                    message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
                //end of red packet code
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void takeVideo(String path, int dur) {

        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(path, 3);
            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.close();
            sendVideoMessage(path, file.getAbsolutePath(), dur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
