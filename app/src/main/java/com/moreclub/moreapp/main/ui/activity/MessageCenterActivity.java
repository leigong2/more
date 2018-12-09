package com.moreclub.moreapp.main.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.constant.Constant;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.MessageBase;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.RedPointerEvent;
import com.moreclub.moreapp.main.presenter.IMessageFromPushLoader;
import com.moreclub.moreapp.main.presenter.MessageFromPushLoader;
import com.moreclub.moreapp.main.ui.adapter.MessageCenterItemAdapter;
import com.moreclub.moreapp.message.ui.activity.MessageFollowListActivity;
import com.moreclub.moreapp.message.ui.activity.MessageInteractionListActivity;
import com.moreclub.moreapp.message.ui.activity.MessageSigninListActivity;
import com.moreclub.moreapp.message.ui.activity.MessageSystemListActivity;
import com.moreclub.moreapp.message.ui.activity.MessageWallListActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.main.constant.Constants.IM_RED;
import static com.moreclub.moreapp.main.constant.Constants.PUSH_RED;

/**
 * Created by Captain on 2017/4/14.
 */

public class MessageCenterActivity extends BaseActivity implements IMessageFromPushLoader.LoadMessageFromPushDataBinder {
    /**
     * zune:无法刷新
     **/

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.message_list)
    XRecyclerView xRecyclerView;

    private static final int REFRASH = 1001;
    private static final int LOADMORE = 1002;

    private ArrayList<MessageBase> dataList;
    private ArrayList<MessageBase> imDataList;
    private ArrayList<MessageBase> pushDataList;
    MessageCenterItemAdapter adapter;
    ImMessageReiceiver imReiceiver;
    private String click;

    @Override
    protected int getLayoutResource() {
        return R.layout.message_center_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);

        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        initData();
        setupViews();
    }

    @Override
    protected Class getLogicClazz() {
        return IMessageFromPushLoader.class;
    }

    private void initData() {

        /**zune:如果是推送的 直接跳到详情**/
/*        String userId = getIntent().getStringExtra("userId");
        int chatType = getIntent().getIntExtra("chatType", -1);
        if (!TextUtils.isEmpty(userId)) {
            if (chatType == Constant.CHATTYPE_SINGLE) {
                String userName = getIntent().getStringExtra("toChatNickName");
                String headerUrl = getIntent().getStringExtra("toChatHeaderUrl");
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        MessageCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(MessageCenterActivity.this, ChatActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("toChatUserID", userId);
                intent.putExtra("toChatNickName", userName);
                intent.putExtra("redPointTag", Constants.MESSAGECENTERACTIVITY);
                intent.putExtra("toChatHeaderUrl", headerUrl);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, chatType);
                ActivityCompat.startActivity(MessageCenterActivity.this, intent, compat.toBundle());
            } else {
                String userName = getIntent().getStringExtra("groupHeaderName");
                String headerUrl = getIntent().getStringExtra("groupHeaderUrl");
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        MessageCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(MessageCenterActivity.this, ChatActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("toChatUserID", userId);
                intent.putExtra("redPointTag", Constants.MESSAGECENTERACTIVITY);
                intent.putExtra("toChatNickName", userName);
                intent.putExtra("toChatHeaderUrl", "");
                intent.putExtra("groupHeaderName", userName);
                intent.putExtra("groupHeaderUrl", headerUrl);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, chatType);
                ActivityCompat.startActivity(MessageCenterActivity.this, intent, compat.toBundle());
            }
        }*/

        IntentFilter filter = new IntentFilter();
        filter.addAction("Message_Center_IM_Message");
        imReiceiver = new ImMessageReiceiver();
        registerReceiver(imReiceiver, filter);

        dataList = new ArrayList<>();
        imDataList = new ArrayList<>();
        pushDataList = new ArrayList<>();
        adapter = new MessageCenterItemAdapter(this, R.layout.message_center_item, dataList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                PrefsUtils.setInt(MessageCenterActivity.this, PUSH_RED, 0);
                PrefsUtils.setInt(MessageCenterActivity.this, IM_RED, 0);
                String token = MoreUser.getInstance().getAccess_token();
                MessageBase item = (MessageBase) o;
                if ("push".equals(item.getMessageType())) {
                    if (item.getPushType().equals("system")) {
                        click = "system";
                        AppUtils.pushLeftActivity(MessageCenterActivity.this, MessageSystemListActivity.class);
                        ((MessageFromPushLoader) mPresenter).clearRedPointPush(token, "system", MoreUser.getInstance().getUid());
                    } else if (item.getPushType().equals("sign")) {
                        click = "sign";
                        AppUtils.pushLeftActivity(MessageCenterActivity.this, MessageSigninListActivity.class);
                        ((MessageFromPushLoader) mPresenter).clearRedPointPush(token, "sign", MoreUser.getInstance().getUid());
                    } else if (item.getPushType().equals("follow")) {
                        click = "follow";
                        AppUtils.pushLeftActivity(MessageCenterActivity.this, MessageFollowListActivity.class);
                        ((MessageFromPushLoader) mPresenter).clearRedPointPush(token, "follow", MoreUser.getInstance().getUid());
                    } else if (item.getPushType().equals("wall")) {
                        click = "wall";
                        AppUtils.pushLeftActivity(MessageCenterActivity.this, MessageWallListActivity.class);
                        ((MessageFromPushLoader) mPresenter).clearRedPointPush(token, "wall", MoreUser.getInstance().getUid());
                    } else if (item.getPushType().equals("interaction")) {
                        click = "interaction";
                        AppUtils.pushLeftActivity(MessageCenterActivity.this, MessageInteractionListActivity.class);
                        ((MessageFromPushLoader) mPresenter).clearRedPointPush(token, "interaction", MoreUser.getInstance().getUid());
                    }
                } else if ("im".equals(item.getMessageType())) {
                    click = "im";
                    EMConversation conversation = item.getConversation();
                    EMMessage itemMessage = conversation.getLastMessage();
                    EMMessage fromMessage = conversation.getLatestMessageFromOthers();
                    if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                        try {
                            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                    MessageCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                            Intent intent = new Intent(MessageCenterActivity.this, ChatActivity.class);
                            intent.putExtra("userId", conversation.conversationId());
                            intent.putExtra("toChatUserID", conversation.conversationId());
                            intent.putExtra("toChatNickName", itemMessage.getStringAttribute("groupHeaderName"));
                            intent.putExtra("redPointTag", Constants.MESSAGECENTERACTIVITY);
                            intent.putExtra("toChatHeaderUrl", "");
                            intent.putExtra("groupHeaderName", itemMessage.getStringAttribute("groupHeaderName"));
                            intent.putExtra("groupHeaderUrl", itemMessage.getStringAttribute("groupHeaderUrl"));
                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                            ActivityCompat.startActivity(MessageCenterActivity.this, intent, compat.toBundle());
                        } catch (Exception e) {

                        }
                    } else {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                MessageCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                        String ext1 = null;
                        try {
                            if (fromMessage != null) {
                                Intent intent = new Intent(MessageCenterActivity.this, ChatActivity.class);
                                intent.putExtra("userId", conversation.conversationId());
                                intent.putExtra("toChatUserID", conversation.conversationId());
                                intent.putExtra("toChatNickName", fromMessage.getStringAttribute("nickName"));
                                intent.putExtra("toChatHeaderUrl", fromMessage.getStringAttribute("headerUrl"));
                                intent.putExtra("redPointTag", Constants.MESSAGECENTERACTIVITY);
                                ActivityCompat.startActivity(MessageCenterActivity.this, intent, compat.toBundle());
                            } else {
                                ext1 = itemMessage.getStringAttribute(conversation.conversationId());
                                String[] extArray = ext1.split(",");
                                if (extArray != null) {
                                    Intent intent = new Intent(MessageCenterActivity.this, ChatActivity.class);
                                    intent.putExtra("userId", conversation.conversationId());
                                    intent.putExtra("toChatUserID", conversation.conversationId());
                                    intent.putExtra("toChatNickName", extArray[0]);
                                    intent.putExtra("toChatHeaderUrl", "" + extArray[1]);
                                    intent.putExtra("redPointTag", Constants.MESSAGECENTERACTIVITY);
                                    ActivityCompat.startActivity(MessageCenterActivity.this, intent, compat.toBundle());
                                }
                            }
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        loadData();

    }

    private void loadData() {
        ((MessageFromPushLoader) mPresenter).loadMessageFromPush("" +
                MoreUser.getInstance().getUid(), "android");
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<MessageBase> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance()
                .chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    if ("系统管理员".equals(conversation.getLastMessage().getFrom())) {
                        continue;
                    } else {
                        sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage()
                                .getMsgTime(), conversation));
                    }
                }
            }
        }
//        try {
//            // Internal is TimSort algorithm, has bug
//            sortConversationByLastChatTime(sortList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        List<MessageBase> list = new ArrayList<MessageBase>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            EMConversation second = sortItem.second;
            MessageBase item = new MessageBase();
            item.setMessageType("im");
            item.setUnReadCount(second.getUnreadMsgCount());
            item.setConversation(sortItem.second);
            item.setPushTime(sortItem.first);
            list.add(item);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    private void setupViews() {
        activityTitle.setText(getString(R.string.message_activity_title));
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RedPointerEvent(false));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
//                sortDataList();
//                adapter.notifyDataSetChanged();
            }
        });
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new RedPointerEvent(false));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        super.onBackPressed();
    }

    @Override
    public void onMessageFromPushResponse(RespDto response) {
        onLoadComplete(REFRASH);
        MessageCenterPush result = (MessageCenterPush) response.getData();
        List<MessageBase> im = loadConversationList();
        deleteFirstIM(im);
        if (im != null && im.size() > 0) {
            imDataList.addAll(im);
        }

        MessageBase systemItem = new MessageBase();
        systemItem.setPushTitle(getString(R.string.message_system_title));
        if (result.getSystemContent() == null) {
            systemItem.setPushDes(getString(R.string.message_system_no_content));
        } else {
            systemItem.setPushDes(result.getSystemContent());
        }
        systemItem.setUnReadCount(result.getSystemCount());
        systemItem.setMessageType("push");
        systemItem.setPushType("system");
        systemItem.setPushTime(result.getSystemSendTime());
        pushDataList.add(systemItem);

        MessageBase signItem = new MessageBase();
        signItem.setPushTitle(getString(R.string.message_sign_title));
        if (result.getSignCount() != 0) {
            signItem.setPushDes(result.getSign());
        } else {
            signItem.setPushDes("暂无最新签到消息");
        }
        signItem.setUnReadCount(result.getSignCount());
        signItem.setMessageType("push");
        signItem.setPushType("sign");
        signItem.setPushTime(result.getSignSendTime());
        pushDataList.add(signItem);

        MessageBase followItem = new MessageBase();
        followItem.setPushTitle(getString(R.string.message_attention_title));
        if (result.getFollowCount() > 0) {
            followItem.setPushDes(getString(R.string.message_follow_item_des, result.getFollowCount() + ""));
        } else {
            followItem.setPushDes("最近还没有人关注你哟");
        }
        followItem.setUnReadCount(result.getFollowCount());
        followItem.setMessageType("push");
        followItem.setPushType("follow");
        followItem.setPushTime(result.getFollowSendTime());
        pushDataList.add(followItem);


        MessageBase wallItem = new MessageBase();
        String wall = result.getWall();
        if (wall != null) {
            wallItem.setPushDes(wall);
        } else {
            wallItem.setPushDes("暂无留言墙消息");
        }
        wallItem.setUnReadCount(result.getWallCount());
        wallItem.setPushTitle(getString(R.string.message_wall_title));
        wallItem.setMessageType("push");
        wallItem.setPushType("wall");
        wallItem.setPushTime(result.getWallSendTime());
        pushDataList.add(wallItem);

        MessageBase interactionItem = new MessageBase();
        if (result.getInteractionCount() > 0) {
            String content = result.getInteraction();
            switch (content) {
                case Constants.TITLE_INTEREST:
                    break;
                case Constants.TITLE_REPLY:
                    break;
                case Constants.TITLE_ANSWER_REPLY_SELF:
                    interactionItem.setPushDes(Constants.TITLE_ANSWER_REPLY);
                    break;
                case Constants.TITLE_ANSWER_INTEREST_SELF:
                    interactionItem.setPushDes(Constants.TITLE_ANSWER_INTEREST);
                    break;
                default:
                    interactionItem.setPushDes(content);
                    break;
            }
        } else {
            interactionItem.setPushDes("");
        }
        interactionItem.setUnReadCount(result.getInteractionCount());
        interactionItem.setPushTitle(getString(R.string.message_seat_title));
        interactionItem.setMessageType("push");
        interactionItem.setPushType("interaction");
        if (result != null && !TextUtils.isEmpty(result.getInteraction()))
            interactionItem.setPushDes(result.getInteraction());
        else if (result != null) {
            interactionItem.setPushDes("暂无拼座消息");
        }
        interactionItem.setPushTime(result.getInteractionSendTime());
        pushDataList.add(interactionItem);

        dataList.addAll(imDataList);
        dataList.addAll(pushDataList);
        sortData();
        adapter.notifyDataSetChanged();
        Log.d("", "");
    }

    @Override
    public void onMessageFromPushFailure(String msg) {
        onLoadComplete(LOADMORE);
        onLoadComplete(REFRASH);
        MessageCenterPush result = new MessageCenterPush();
        MessageBase systemItem = new MessageBase();
        systemItem.setPushTitle(getString(R.string.message_system_title));
        if (result.getSystemContent() == null) {
            systemItem.setPushDes(getString(R.string.message_system_no_content));
        } else {
            systemItem.setPushDes(result.getSystemContent());
        }
        systemItem.setUnReadCount(result.getSystemCount());
        systemItem.setMessageType("push");
        systemItem.setPushType("system");
        systemItem.setPushTime(result.getSystemSendTime());
        pushDataList.add(systemItem);

        MessageBase signItem = new MessageBase();
        signItem.setPushTitle(getString(R.string.message_sign_title));
        if (result.getSignCount() != 0) {
            signItem.setPushDes(result.getSign());
        } else {
            signItem.setPushDes("暂无最新签到消息");
        }
        signItem.setUnReadCount(result.getSignCount());
        signItem.setMessageType("push");
        signItem.setPushType("sign");
        signItem.setPushTime(0);
        pushDataList.add(signItem);

        MessageBase followItem = new MessageBase();
        followItem.setPushTitle(getString(R.string.message_attention_title));
        if (result.getFollowCount() > 0) {
            followItem.setPushDes(getString(R.string.message_follow_item_des, result.getFollowCount() + ""));
        } else {
            followItem.setPushDes("最近还没有人关注你哟");
        }
        followItem.setUnReadCount(result.getFollowCount());
        followItem.setMessageType("push");
        followItem.setPushType("follow");
        followItem.setPushTime(result.getFollowSendTime());
        pushDataList.add(followItem);


        MessageBase wallItem = new MessageBase();
        String wall = result.getWall();
        if (wall != null) {
            wallItem.setPushDes(wall);
        } else {
            wallItem.setPushDes("");
        }
        wallItem.setUnReadCount(result.getWallCount());
        wallItem.setPushTitle(getString(R.string.message_wall_title));
        wallItem.setMessageType("push");
        wallItem.setPushType("wall");
        if (result != null && !TextUtils.isEmpty(result.getWall()))
            wallItem.setPushDes(result.getWall());
        else
            wallItem.setPushDes("暂无留言墙消息");
        wallItem.setPushTime(result.getWallSendTime());
        pushDataList.add(wallItem);

        MessageBase interactionItem = new MessageBase();
        if (result.getInteractionCount() > 0) {
            String content = result.getInteraction();
            switch (content) {
                case Constants.TITLE_INTEREST:
                    break;
                case Constants.TITLE_REPLY:
                    break;
                case Constants.TITLE_ANSWER_REPLY_SELF:
                    interactionItem.setPushDes(Constants.TITLE_ANSWER_REPLY);
                    break;
                case Constants.TITLE_ANSWER_INTEREST_SELF:
                    interactionItem.setPushDes(Constants.TITLE_ANSWER_INTEREST);
                    break;
                default:
                    interactionItem.setPushDes(content);
                    break;
            }
        } else {
            interactionItem.setPushDes("");
        }
        interactionItem.setUnReadCount(result.getInteractionCount());
        interactionItem.setPushTitle(getString(R.string.message_seat_title));
        interactionItem.setMessageType("push");
        interactionItem.setPushType("interaction");
        if (result != null && !TextUtils.isEmpty(result.getInteraction()))
            interactionItem.setPushDes(result.getInteraction());
        else if (result != null) {
            interactionItem.setPushDes("暂无拼座消息");
        }
        interactionItem.setPushTime(result.getInteractionSendTime());
        pushDataList.add(interactionItem);
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }

        List<MessageBase> im = loadConversationList();
        deleteFirstIM(im);
        if (im != null && im.size() > 0) {
            imDataList.addAll(im);
        }

        dataList.addAll(pushDataList);
        dataList.addAll(imDataList);
        sortData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearRedPointPushResponse() {
        Log.i("zune:", "response =");
        for (int i = 0; i < dataList.size(); i++) {
            MessageBase messageBase = dataList.get(i);
            if (messageBase != null && messageBase.getPushType() != null &&
                    messageBase.getPushType().equals(click)) {
                messageBase.setUnReadCount(0);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearRedPointPushFailure(String msg) {
        Log.i("zune:", "clearRedPointPushFailure = " + msg);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void closePushFailure(String msg) {

    }

    @Override
    public void closePushResponse(RespDto body) {

    }

    private void sortData() {
        Collections.sort(dataList, new Comparator<MessageBase>() {
            @Override
            public int compare(final MessageBase con1, final MessageBase con2) {

                if (con1.getPushTime() == con2.getPushTime()) {
                    return 0;
                } else if (con2.getPushTime() > con1.getPushTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public void refreshIM() {
        xRecyclerView.refreshComplete();

        List<MessageBase> im = loadConversationList();
        if (im != null && im.size() > 0) {
            imDataList.clear();
            imDataList.addAll(im);
        }
        dataList.clear();
        deleteFirstIM(imDataList);
        dataList.addAll(imDataList);
        dataList.addAll(pushDataList);
        sortData();
        adapter.notifyDataSetChanged();
    }


    private void deleteFirstIM(List<MessageBase> imDataList) {
        for (int i = 0; i < imDataList.size(); i++) {
            MessageBase messageBase = imDataList.get(i);
            EMMessage itemMessage = messageBase.getConversation().getLastMessage();
            String message;
            try {
                message = ((EMTextMessageBody) itemMessage.getBody()).getMessage();
            } catch (Exception e) {
                message = "";
            }
            if (itemMessage.direct() == EMMessage.Direct.SEND) {

            } else
                switch (message) {
                    case Constants.TITLE_SEND_INTEREST:
                    case Constants.TITLE_SEND_REPLY:
                        if (itemMessage.isUnread()) {
                            imDataList.remove(i);
                        }
                        break;
                    default:
                        break;
                }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("zune:", "onNewIntent");
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(imReiceiver);
        super.onDestroy();
    }

    public class ImMessageReiceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if ("hx".equals(type)) {
                Message msg = refreshHandler.obtainMessage();
                msg.what = 0; //消息标识
                refreshHandler.sendMessage(msg); //发送消息
            }
        }
    }

    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    refreshIM();
                    break;
                default:
                    break;
            }
        }
    };

    private void onLoadComplete(int page) {
        if (page == REFRASH) {
            xRecyclerView.refreshComplete();
            pushDataList.clear();
            dataList.clear();
            imDataList.clear();
        } else
            xRecyclerView.loadMoreComplete();
    }

    private class CollectByCount implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            MessageBase messageBase1 = (MessageBase) o1;
            MessageBase messageBase2 = (MessageBase) o2;
            return 0;
//            return messageBase2.getUnReadCount().compareTo(messageBase1.getUnReadCount());
        }
    }
}
