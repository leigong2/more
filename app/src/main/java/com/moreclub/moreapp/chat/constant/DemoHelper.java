package com.moreclub.moreapp.chat.constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.moreclub.common.log.Logger;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.chat.db.DemoDBManager;
import com.moreclub.moreapp.chat.db.InviteMessgeDao;
import com.moreclub.moreapp.chat.db.UserDao;
import com.moreclub.moreapp.chat.util.PreferenceManager;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;

import java.util.List;

public class DemoHelper {
    /**
     * data sync listener
     */
    public interface DataSyncListener {
        /**
         * sync complete
         *
         * @param success true：data sync successful，false: failed to sync data
         */
        void onSyncComplete(boolean success);
    }

    protected static final String TAG = "DemoHelper";

    private EaseUI easeUI;
    public static final String PUSH_APP_ID = "2882303761517566170";
    public static final String PUSH_APP_KEY = "5121756688170";
    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private static DemoHelper instance = null;

    private boolean isSyncingGroupsWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    private boolean isSyncingBlackListWithServer = false;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;

    private static Context appContext;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private LocalBroadcastManager broadcastManager;

    private boolean isGroupAndContactListenerRegisted;

    private DemoHelper() {
    }

    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(false);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            //initialize preference manager
            PreferenceManager.init(context);

            setEaseUIProviders();
            /**
             * This function is only meaningful when your app need recording
             * If not, remove it.
             * This function need be called before the video stream started, so we set it in onCreate function.
             * This method will set the preferred video record encoding codec.
             * Using default encoding format, recorded file may not be played by mobile player.
             */
            //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

            // resolution
            String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
            if (resolution.equals("")) {
                resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
            }
            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();
        }
    }


    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();

        options.setAutoLogin(true);
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        //you need apply & set your own id if you want to use google cloud messaging.
        options.setGCMNumber("324169311137");
        //you need apply & set your own id if you want to use Mi push notification

        options.setMipushConfig(PUSH_APP_ID, PUSH_APP_KEY);
        //you need apply & set your own id if you want to use Huawei push notification
        options.setHuaweiPushAppId("10492024");

        return options;
    }

    public static void hxLogin(String username, String password) {
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                new Thread() {
                    public void run() {
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        EMClient.getInstance().groupManager().loadAllGroups();

                        // 登录成功跳转界面
                    }
                }.start();
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
//                switch (i) {
//                    // 网络异常 2
//                    case EMError.NETWORK_ERROR:
//                        Toast.makeText(appContext, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 无效的用户名 101
//                    case EMError.INVALID_USER_NAME:
//                        Toast.makeText(appContext, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 无效的密码 102
//                    case EMError.INVALID_PASSWORD:
//                        Toast.makeText(appContext, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 用户认证失败，用户名或密码错误 202
//                    case EMError.USER_AUTHENTICATION_FAILED:
//                        Toast.makeText(appContext, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 用户不存在 204
//                    case EMError.USER_NOT_FOUND:
//                        Toast.makeText(appContext, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 无法访问到服务器 300
//                    case EMError.SERVER_NOT_REACHABLE:
//                        Toast.makeText(appContext, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 等待服务器响应超时 301
//                    case EMError.SERVER_TIMEOUT:
//                        Toast.makeText(appContext, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 服务器繁忙 302
//                    case EMError.SERVER_BUSY:
//                        Toast.makeText(appContext, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    // 未知 Server 异常 303 一般断网会出现这个错误
//                    case EMError.SERVER_UNKNOWN_ERROR:
//                        Toast.makeText(appContext, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                    default:
//                        Toast.makeText(appContext, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
//                        break;
//                }
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    protected void setEaseUIProviders() {

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return "More聊天消息";
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return R.drawable.main_more_logo;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                String nickName = "";
                String ticker = "";
                try {
                    ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                    if (message.getType() == Type.TXT) {
                        ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                    }
                    nickName = message.getStringAttribute("nickName");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (nickName == null) {
                    nickName = message.getFrom();
                }

                return nickName + ": " + ticker;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, SuperMainActivity.class);
                intent.putExtra("shouldGo", "MessageCenterFragment");
                /**zune:将推送消息传递给详情**/
                try {
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) { // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("toChatNickName", message.getStringAttribute("nickName"));
                        intent.putExtra("toChatHeaderUrl", message.getStringAttribute("headerUrl"));
                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        intent.putExtra("groupHeaderName", message.getStringAttribute("groupHeaderName"));
                        intent.putExtra("groupHeaderUrl", message.getStringAttribute("groupHeaderUrl"));
                        if (chatType == ChatType.GroupChat) {
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        }
                    }
                } catch (Exception e) {
                    Logger.i("zune:", "e = " + e);
                }
                return intent;
            }
        });
    }

    EMConnectionListener connectionListener;

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);

            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if (isGroupsSyncedWithServer && isContactsSyncedWithServer) {
                    EMLog.d(TAG, "group and contact already synced with servre");
                } else {
                    if (!isGroupsSyncedWithServer) {
                        asyncFetchGroupsFromServer(null);
                    }

                    if (!isContactsSyncedWithServer) {
                        asyncFetchContactsFromServer(null);
                    }

                    if (!isBlackListSyncedWithServer) {
                        asyncFetchBlackListFromServer(null);
                    }
                }
            }
        };

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);


    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    public void getDataFromServer() {

        asyncFetchContactsFromServer(null);

        asyncFetchBlackListFromServer(null);
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    void endCall() {

    }


    /**
     * Get group list from server
     * This method will save the sync state
     *
     * @throws HyphenateException
     */
    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
        if (isSyncingGroupsWithServer) {
            return;
        }

        isSyncingGroupsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                } catch (HyphenateException e) {

                }

            }
        }.start();
    }


    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
        if (isSyncingContactsWithServer) {
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {

                }

            }
        }.start();
    }


    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {

        if (isSyncingBlackListWithServer) {
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();

                } catch (HyphenateException e) {

                }

            }
        }.start();
    }


    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;


        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;

        isGroupAndContactListenerRegisted = false;


        DemoDBManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

}
