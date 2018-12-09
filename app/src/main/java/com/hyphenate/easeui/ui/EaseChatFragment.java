package com.hyphenate.easeui.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu.ChatInputMenuListener;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.ui.activity.ChatGroupDetailsActivity;
import com.moreclub.moreapp.chat.ui.activity.RecorderVideoActivity;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.InterInvites;
import com.moreclub.moreapp.main.model.event.ImRefreshEvent;
import com.moreclub.moreapp.main.presenter.AllowInteraction;
import com.moreclub.moreapp.main.presenter.IAllowInteraction;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.SignInterDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.util.List;

/**
 * you can new an EaseChatFragment to use or you can inherit it to expand.
 * You need call setArguments to pass chatType and userId
 * <br/>
 * <br/>
 * you can see ChatActivity in demo for your reference
 */
public abstract class EaseChatFragment extends EaseBaseFragment implements OnItemClickListener, OnDismissListener, EMMessageListener, IAllowInteraction.AllowInteractionBinder {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    // constant start from 11 to avoid conflict with constant in base class
    protected static final int ITEM_VIDEO = 11;
    protected static final int ITEM_FILE = 12;
    protected static final int ITEM_VOICE_CALL = 13;
    protected static final int ITEM_VIDEO_CALL = 14;

    protected static final int REQUEST_CODE_SELECT_VIDEO = 11;
    protected static final int REQUEST_CODE_SELECT_FILE = 12;
    protected static final int REQUEST_CODE_GROUP_DETAIL = 13;
    protected static final int REQUEST_CODE_CONTEXT_MENU = 14;
    protected static final int REQUEST_CODE_SELECT_AT_USER = 15;


    protected static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    protected static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    protected static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    protected static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    //red packet code : 红包功能使用的常量
    protected static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    protected static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    protected static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    protected static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    protected static final int MESSAGE_TYPE_RECV_RANDOM = 11;
    protected static final int MESSAGE_TYPE_SEND_RANDOM = 12;
    protected static final int ITEM_RED_PACKET = 16;
    //end of red packet code
    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    protected String toChatNickName;
    protected String toChatHeaderUrl;
    protected String toChatUserID;
    protected String groupHeaderName;
    protected String groupHeaderUrl;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;
    protected EMConversation conversation;
    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;
    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;
    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;

    protected EMMessage contextMenuMessage;

    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;
    static final int ITEM_VEDIO = 4;

    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;

    private AlertView photoAlertView;
    private String tag;
    private BasePresenter mPresenter;
    private RelativeLayout interestLayout;
    private TextView tv_allow;
    private TextView tv_answer;
    private ImageView allowed;
    private int vid;
    private int status;
    private TextView tv_answer_dead;
    private TextView tv_allow_dead;
    private TextView tv_old;
    private TextView tv_name;
    private int mid;
    private int isfightseat;
    private int isSystemMessage;
    private String merchant;
    private TextView tv_address;
    private AlertView messageMenuAlertView;

    private OnClickListener allowListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ((AllowInteraction) mPresenter).allowInteraction(MoreUser.getInstance().getAccess_token(), vid);
        }
    };
    private OnClickListener answerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ((AllowInteraction) mPresenter).allowInteraction(MoreUser.getInstance().getAccess_token(), vid);
        }
    };
    private OnClickListener merchantClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    getContext(), R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(getContext(), MerchantDetailsViewActivity.class);
            if (EaseChatFragment.this.mid != 0)
                intent.putExtra("mid", EaseChatFragment.this.mid + "");
            ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
        }
    };
    private OnClickListener inviteListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    getContext(), R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            Intent intent = new Intent(getContext(), SignInterDetailActivity.class);
            intent.putExtra("sid", sid);
            ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
        }
    };
    private int sid;

    public abstract void takeVideo(String path, int dur);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.ease_fragment_chat, container, false);
        interestLayout = (RelativeLayout) inflate.findViewById(R.id.interest_layout);
        tv_allow = (TextView) inflate.findViewById(R.id.tv_allow);
        tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        tv_allow_dead = (TextView) inflate.findViewById(R.id.tv_allow_dead);
        tv_answer = (TextView) inflate.findViewById(R.id.tv_answer);
        tv_answer_dead = (TextView) inflate.findViewById(R.id.tv_answer_dead);
        tv_address = (TextView) inflate.findViewById(R.id.tv_address);
        allowed = (ImageView) inflate.findViewById(R.id.allowed);
        tv_old = (TextView) inflate.findViewById(R.id.tv_old);
        mPresenter = LogicProxy.getInstance().bind(IAllowInteraction.class, EaseChatFragment.this);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        fragmentArgs = getArguments();
        // check if single chat or group chat
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
        if (TextUtils.isEmpty(toChatUsername)) {
            toChatUsername = fragmentArgs.getString("toChatUserID");
        }
        toChatNickName = fragmentArgs.getString("toChatNickName");
        toChatHeaderUrl = fragmentArgs.getString("toChatHeaderUrl");
        isfightseat = fragmentArgs.getInt("ISFIGHTSEAT");
        toChatUserID = fragmentArgs.getString("toChatUserID");
        groupHeaderName = fragmentArgs.getString("groupHeaderName");
        groupHeaderUrl = fragmentArgs.getString("groupHeaderUrl");
        tag = fragmentArgs.getString("tag");
        merchant = fragmentArgs.getString("merchant");
        vid = fragmentArgs.getInt("vid");
        status = fragmentArgs.getInt("status");
        mid = fragmentArgs.getInt("mid");
        if (toChatUserID != null)
            ((AllowInteraction) mPresenter).onLoadInterInvitesDetail(MoreUser.getInstance().getAccess_token(), Long.parseLong(toChatUserID), MoreUser.getInstance().getUid());
        super.onActivityCreated(savedInstanceState);

        if (messageMenuAlertView==null){
            messageMenuAlertView = new AlertView("消息菜单", null, "取消", null,
                    new String[]{"复制", "删除"},
                    getActivity(), AlertView.Style.ActionSheet,EaseChatFragment.this);

        }
    }

    /**
     * init view
     */
    protected void initView() {
        // hold to record voice
        //noinspection ConstantConditions
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);

        // message list layout
        messageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }

            @Override
            public void onPressToSpeakBtnTouchFinish(String voiceFilePath, int voiceTimeLength) {
                sendVoiceMessage(voiceFilePath, voiceTimeLength);
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void setUpView() {
        titleBar.setTitle(toChatNickName);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
                if (user != null) {
                    titleBar.setTitle(user.getNick());
                }
            }
            titleBar.setRightImageResource(R.drawable.ease_to_group_details_normal);
            titleBar.setRightLayoutVisibility(View.VISIBLE);
        } else {
            titleBar.setRightImageResource(R.drawable.ease_to_group_details_normal);
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    titleBar.setTitle(group.getGroupName());
                // listen the event that user moved out group or group is dismissed
                groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
            }
        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }

        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    toGroupDetails();
                } else {
                    toGroupDetails();
                }
            }
        });

        setRefreshLayoutListener();

        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        inputMenu.registerExtendMenuItem(extendMenuItemClickListener);
    }


    protected void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }

    }

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null, toChatNickName, toChatHeaderUrl);
        setListItemClickListener();

        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {

                    ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                            getContext(), R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_merchant = new Intent(getContext(), UserDetailV2Activity.class);
                    intent_merchant.putExtra("toUid", username);
                    ActivityCompat.startActivity(getContext(), intent_merchant, compat_merchant.toBundle());

//                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(final EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(message);
                }
                if (message.getType()== EMMessage.Type.TXT){
                    if (messageMenuAlertView!=null) {
                        messageMenuAlertView.show();
                    } else {
                        messageMenuAlertView = new AlertView("消息菜单", null, "取消", null,
                                new String[]{"复制", "删除"},
                                getActivity(), AlertView.Style.ActionSheet,EaseChatFragment.this);
                        messageMenuAlertView.show();
                    }
                } else {
                    new EaseAlertDialog(getActivity(), R.string.delete_message, R.string.comfirm_delete_message, null, new AlertDialogUser() {
                        @Override
                        public void onResult(boolean confirmed, Bundle bundle) {
                            if (!confirmed) {
                                return;
                            }
                            conversation.removeMessage(message.getMsgId());
                            messageList.refresh();
                        }
                    }, true).show();
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public void onBubbleLongClick(final EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }

                if (message.getType()== EMMessage.Type.TXT){
                    if (messageMenuAlertView!=null) {
                        messageMenuAlertView.show();
                    } else {
                        messageMenuAlertView = new AlertView("消息菜单", null, "取消", null,
                                new String[]{"复制", "删除"},
                                getActivity(), AlertView.Style.ActionSheet,EaseChatFragment.this);
                        messageMenuAlertView.show();
                    }
                } else {
                    new EaseAlertDialog(getActivity(), R.string.delete_message, R.string.comfirm_delete_message, null, new AlertDialogUser() {
                        @Override
                        public void onResult(boolean confirmed, Bundle bundle) {
                            if (!confirmed) {
                                return;
                            }
                            conversation.removeMessage(message.getMsgId());
                            messageList.refresh();
                        }
                    }, true).show();
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                messageList.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    final Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        new EaseAlertDialog(getActivity(), R.string.confirm_send_pic_title, R.string.confirm_send_pic_des, null, new AlertDialogUser() {
                            @Override
                            public void onResult(boolean confirmed, Bundle bundle) {
                                if (confirmed) {
                                    sendPicByUri(selectedImage);
                                }
                            }
                        }, true).show();
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == 100) {
                Uri uri = data.getParcelableExtra("uri");
                String[] projects = new String[]{MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION};
                Cursor cursor = getActivity().getContentResolver().query(
                        uri, projects, null,
                        null, null);
                int duration = 0;
                String filePath = null;

                if (cursor.moveToFirst()) {
                    // path：MediaStore.Audio.Media.DATA
                    filePath = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    // duration：MediaStore.Audio.Media.DURATION
                    duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    EMLog.d(TAG, "duration:" + duration);
                }
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                takeVideo(filePath, duration);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

    }

    public void onBackPressed() {
        hideKeyboard();
        EventBus.getDefault().post(new ImRefreshEvent());
        if (inputMenu.onBackPressed()) {
            getActivity().finish();
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity().isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
                            titleBar.setTitle(room.getName());
                            EMLog.d(TAG, "join room success : " + room.getName());
                        } else {
                            titleBar.setTitle(toChatUsername);
                        }
                        addChatRoomChangeListenr();
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                getActivity().finish();
            }
        });
    }

    protected void addChatRoomChangeListenr() {
        /*
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(toChatUsername)) {
                    showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    getActivity().finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showChatroomToast("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onRemovedFromChatRoom(String roomId, String roomName, String participant) {
                if (roomId.equals(toChatUsername)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                    	EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
                        getActivity().finish();
                    }else{
                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }


            // ============================= group_reform new add api begin
            @Override
            public void onMuteListAdded(String chatRoomId, Map<String, Long> mutes) {}

            @Override
            public void onMuteListRemoved(String chatRoomId, List<String> mutes) {}

            @Override
            public void onAdminAdded(String chatRoomId, String admin) {}

            @Override
            public void onAdminRemoved(String chatRoomId, String admin) {}

            @Override
            public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {}

            // ============================= group_reform new add api end

        };
        
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
        */
    }

    protected void showChatroomToast(final String toastContent) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), toastContent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion>=23){
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, 321);
                    return;
                }
            }

            if (chatFragmentHelper != null && view != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    if (photoAlertView == null) {
                        photoAlertView = new AlertView("上传头像", null, "取消", null,
                                new String[]{"拍照", "从相册中选择"},
                                getContext(), AlertView.Style.ActionSheet, EaseChatFragment.this);
                    }
                    if (!photoAlertView.isShowing() && !getActivity().isFinishing())
                        photoAlertView.show();
                    break;
                case ITEM_PICTURE:
                    if (photoAlertView == null) {
                        photoAlertView = new AlertView("上传头像", null, "取消", null,
                                new String[]{"拍照", "从相册中选择"},
                                getContext(), AlertView.Style.ActionSheet, EaseChatFragment.this);
                    }
                    if (!photoAlertView.isShowing() && !getActivity().isFinishing())
                        photoAlertView.show();
                    break;

                case ITEM_LOCATION:
                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                case ITEM_VEDIO:
//                    Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), RecorderVideoActivity.class);
                    startActivityForResult(intent, 100);
                default:
                    break;
            }
        }
    }


    @Override
    public void onItemClick(Object o, int position) {
        if (o == photoAlertView) {
            if (position == 0) {
                selectPicFromCamera();
            } else if (position == 1) {
                selectPicFromLocal();
            }
        } else if (o==messageMenuAlertView){
            if (position == 0) {
            //copy
                clipboard.setPrimaryClip(ClipData.newPlainText(null,
                        ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
            } else if (position == 1) {
            //delete
                if (contextMenuMessage!=null) {
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                }
            }
        }
    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }


    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }


    //send message
    protected void sendTextMessage(String content) {
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(message);
        }
    }

    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);

    }


    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }


    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
            message.setAttribute("groupHeaderName", groupHeaderName);
            message.setAttribute("groupHeaderUrl", groupHeaderUrl);
            message.setAttribute("roomID", "" + toChatUserID);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(ChatType.ChatRoom);
        }

        message.setAttribute("nickName", MoreUser.getInstance().getNickname());
        message.setAttribute("headerUrl", MoreUser.getInstance().getThumb());
        message.setAttribute(toChatUserID, toChatNickName + "," + toChatHeaderUrl);
        message.setAttribute("fromUID", "" + MoreUser.getInstance().getUid());

        if (isfightseat == 1) {
            message.setAttribute("ISFIGHTSEAT", 1);
        } else {
            message.setAttribute("ISFIGHTSEAT", 0);
        }

        if (isSystemMessage == 1) {
            message.setAttribute(EaseConstant.ISSYSTEMMESSAGE, 1);
        } else {
            message.setAttribute(EaseConstant.ISSYSTEMMESSAGE, 0);
        }


//        toChatNickName = fragmentArgs.getString("toChatNickName");
//        toChatHeaderUrl = fragmentArgs.getString("toChatHeaderUrl");
//        toChatUserID = fragmentArgs.getString("toChatUserID");

//        message.setAttribute(""+MoreUser.getInstance().getUid(),MoreUser.getInstance().getNickname()+","+MoreUser.getInstance().getThumb());
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //对系统消息和拼座消息进行复位
        isfightseat = 0;
        isSystemMessage = 0;

        message.setMessageStatusCallback(new EMCallBack() {


            @Override
            public void onSuccess() {
                Log.d("", "");
            }

            @Override
            public void onError(int i, String s) {
                if (i==201){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(),"会话已过期，请重新登录",Toast.LENGTH_SHORT).show();
                            AppUtils.pushLeftActivity(getContext(), UseLoginActivity.class);
                        }
                    });

                }
                Log.d("", "");
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("", "");
            }
        });
        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }


    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();
    }

    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        //limit the size < 10M
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getActivity(), R.string.The_file_is_not_greater_than_10_m, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Log.e("currentapiVersion","currentapiVersion====>"+currentapiVersion);
        if (currentapiVersion<24){
            startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                    REQUEST_CODE_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, cameraFile.getAbsolutePath());
            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }

    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(), null, msg, null, new AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    messageList.refresh();
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
//            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
//            if (group == null) {
//                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (chatFragmentHelper != null) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(getContext(), ChatGroupDetailsActivity.class);
                intent.putExtra("roomID", toChatUsername);
                intent.putExtra("chatType", chatType);
                intent.putExtra("groupHeaderName", groupHeaderName);
                ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    getContext(), R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(getContext(), ChatGroupDetailsActivity.class);
            intent.putExtra("roomID", toChatUsername);
            intent.putExtra("chatType", chatType);
            intent.putExtra("toChatNickName", toChatNickName);
            intent.putExtra("toChatHeaderUrl", toChatHeaderUrl);
            ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // get the content and send it
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     *
     */
    class GroupListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

    protected EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentListener(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param
         */
        void onAvatarLongClick(EMMessage message);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();

    }

    @Override
    public void onAllowInteractionResponse(RespDto<Boolean> object) {
        if (object.getData()) {
            tv_allow.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);
            allowed.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                interestLayout.setVisibility(View.GONE);
            }
        }, 2000);
        switch (tag) {
            case Constants.ALLOW:  //通过我感兴趣
                isSystemMessage = 1;
                isfightseat = 1;
                EMMessage message = EMMessage.createTxtSendMessage(Constants.TITLE_ANSWER_INTEREST_SELF, toChatUsername);
                message.setAttribute("inter", Constants.TITLE_ALLOW);
                message.setAttribute(EaseConstant.ISSYSTEMMESSAGE, 1);
                sendMessage(message);
                break;
            case Constants.ANSWER:  //接受拼座邀请
                isSystemMessage = 1;
                isfightseat = 1;
                EMMessage message2 = EMMessage.createTxtSendMessage(Constants.TITLE_ANSWER_REPLY_SELF, toChatUsername);
                message2.setAttribute("invite", Constants.TITLE_ANSWER);
                message2.setAttribute(EaseConstant.ISSYSTEMMESSAGE, 1);
                sendMessage(message2);
                break;
            default:
                interestLayout.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onAllowInteractionFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onLoadInterInvitesResponse(RespDto<List<InterInvites>> respDto) {
        List<InterInvites> data = respDto.getData();
        if (data == null || data.size() == 0)
            return;
        if (data.get(data.size() - 1).getMerchantName() != null) {
            tv_address.setText(data.get(data.size() - 1).getMerchantName());
            tv_address.setOnClickListener(merchantClick);
        }
        sid = data.get(data.size() - 1).getSid();
        vid = data.get(data.size() - 1).getVid();
        tv_name.setOnClickListener(inviteListener);
        if (!data.get(data.size() - 1).isInviteOrInter()) {
            tag = Constants.ANSWER;  //接受拼座邀请
            interestLayout.setVisibility(View.VISIBLE);
            tv_allow.setVisibility(View.VISIBLE);
            tv_answer.setVisibility(View.GONE);
            allowed.setVisibility(View.GONE);
            tv_allow.setOnClickListener(allowListener);
            tv_name.setText("TA对您的拼座感兴趣");
        } else {
            tag = Constants.ALLOW;  //通过我感兴趣
            interestLayout.setVisibility(View.VISIBLE);
            tv_answer.setVisibility(View.VISIBLE);
            tv_allow.setVisibility(View.GONE);
            allowed.setVisibility(View.GONE);
            tv_answer.setOnClickListener(answerListener);
            tv_name.setText("TA邀请您加入拼座");
        }
        // 当前状态  0 初始   1 接受  2拒绝 3 过期  4 关闭 （3,4 为活动状态） 5 邀请过期（暂未使用） 6 已阅读
        switch (data.get(data.size() - 1).getStatus()) {
            case 0:
                break;
            case 1:
                interestLayout.setVisibility(View.GONE);
                break;
            case 2:
                interestLayout.setVisibility(View.GONE);
                break;
            case 3:
                tv_allow.setVisibility(View.GONE);
                tv_answer.setVisibility(View.GONE);
                tv_old.setVisibility(View.VISIBLE);
                tv_old.setText("已过期");
                if (data.get(data.size() - 1).isInviteOrInter())
                    tv_answer_dead.setVisibility(View.VISIBLE);
                else
                    tv_allow_dead.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interestLayout.setVisibility(View.GONE);
                    }
                }, 2000);
                break;
            case 4:
                tv_allow.setVisibility(View.GONE);
                tv_answer.setVisibility(View.GONE);
                tv_old.setVisibility(View.VISIBLE);
                tv_old.setText("已关闭");
                if (data.get(data.size() - 1).isInviteOrInter())
                    tv_answer_dead.setVisibility(View.VISIBLE);
                else
                    tv_allow_dead.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interestLayout.setVisibility(View.GONE);
                    }
                }, 2000);
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    @Override
    public void onLoadInterInvitesFailure(String object) {
        Log.i("zune:", "msg = " + object);
    }
}
