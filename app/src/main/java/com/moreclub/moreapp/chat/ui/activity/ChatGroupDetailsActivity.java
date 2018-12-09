package com.moreclub.moreapp.chat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.model.ChatGroupUser;
import com.moreclub.moreapp.chat.presenter.ChatGroupDetailsLoader;
import com.moreclub.moreapp.chat.presenter.IChatGroupDetailsLoader;
import com.moreclub.moreapp.chat.ui.adapter.ChatGroupUserAdapter;
import com.moreclub.moreapp.information.model.HxRoomDetails;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsReportActivity;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Captain on 2017/6/9.
 */

public class ChatGroupDetailsActivity extends BaseActivity
        implements IChatGroupDetailsLoader.LoaderChatGroupDetailsDataBinder{
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.saveImage) ImageView saveImage;
    @BindView(R.id.optionImage) ImageView optionImage;
    @BindView(R.id.exit_chat) Button exitChat;
    private ChatGroupUserAdapter adapter;
    private String roomId;
    private int chatType;
    private String toChatNickName;
    private String toChatHeaderUrl;
    private String groupHeaderName;
    private String gid;
    private ArrayList<ChatGroupUser> dataList;
    private boolean isSaveRoom;
    private boolean isShieldMessage;
    private List<Object> blockList;

    @Override
    protected int getLayoutResource() {
        return R.layout.chat_group_details_activity;
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
        return IChatGroupDetailsLoader.class;
    }

    private void initData() {
        isSaveRoom = PrefsUtils.getBoolean(this, Constants.KEY_CHAT_ROOM_SAVE, false);
        dataList = new ArrayList<>();
        blockList = new ArrayList<>();
        blockList = PrefsUtils.getDataList(this,"chat_block");
        adapter = new ChatGroupUserAdapter(this,R.layout.sign_interact_square_item,dataList);
        roomId = getIntent().getStringExtra("roomID");
        chatType = getIntent().getIntExtra("chatType",0);
        toChatNickName = getIntent().getStringExtra("toChatNickName");
        toChatHeaderUrl = getIntent().getStringExtra("toChatHeaderUrl");
        groupHeaderName = getIntent().getStringExtra("groupHeaderName");

        if (chatType== EaseConstant.CHATTYPE_GROUP) {
            ((ChatGroupDetailsLoader) mPresenter).loadRoomHxDetails(roomId);
        } else {
            if (!TextUtils.isEmpty(roomId)&&!TextUtils.isEmpty(toChatNickName)&&!TextUtils.isEmpty(toChatHeaderUrl)) {
                ChatGroupUser item = new ChatGroupUser();
                item.setUid(Long.parseLong(roomId));
                item.setNickname(toChatNickName);
                item.setThumb(toChatHeaderUrl);
                dataList.add(item);
            }
        }
    }

    private void setupViews() {
        if (chatType== EaseConstant.CHATTYPE_GROUP){
            if (!TextUtils.isEmpty(groupHeaderName)){
                activityTitle.setText(groupHeaderName);
            } else {
                activityTitle.setText(getString(R.string.chat_group_details_title));
            }
            exitChat.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(toChatNickName)){
                activityTitle.setText(toChatNickName);
            } else {
                activityTitle.setText(getString(R.string.chat_group_details_title));
            }
            exitChat.setVisibility(View.GONE);
        }

        naBack.setOnClickListener(goBackListener);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ChatGroupUser item = (ChatGroupUser) o;
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        ChatGroupDetailsActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(ChatGroupDetailsActivity.this, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", ""+item.getUid());
                ActivityCompat.startActivity(ChatGroupDetailsActivity.this, intent_merchant, compat_merchant.toBundle());

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        if (blockList!=null&&blockList.size()>0){
            for(int i=0;i<blockList.size();i++){
                String item = (String) blockList.get(i);
                if (item!=null&&item.equals(roomId)){
                    isShieldMessage = true;
                    optionImage.setImageResource(R.drawable.chatopern);
                    break;
                }
            }
        }
        if (isSaveRoom){
            saveImage.setImageResource(R.drawable.chatopern);
        } else {
            saveImage.setImageResource(R.drawable.chatclose);
        }
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChatGroupDetailsActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @OnClick(R.id.saveTo)
    void onSaveTo(){
        isSaveRoom =!isSaveRoom;
        PrefsUtils.getEditor(this)
                .putBoolean(Constants.KEY_CHAT_ROOM_SAVE,isSaveRoom)
                .commit();
        if (isSaveRoom){
            saveImage.setImageResource(R.drawable.chatopern);
        } else {
            saveImage.setImageResource(R.drawable.chatclose);
        }
    }

    @OnClick(R.id.messageOption)
    void onMessageOption(){
        isShieldMessage = !isShieldMessage;
        if (isShieldMessage) {
            optionImage.setImageResource(R.drawable.chatopern);
            for(int i =0;i<blockList.size();i++){
                if (blockList.get(i).equals(roomId)){
                    return;
                }
            }
            blockList.add(roomId);
            PrefsUtils.setDataList(this,"chat_block",blockList);
        } else {
            for(int i =0;i<blockList.size();i++){
                if (blockList.get(i).equals(roomId)){
                    blockList.remove(i);
                    PrefsUtils.setDataList(this,"chat_block",blockList);
                    break;
                }
            }
            optionImage.setImageResource(R.drawable.chatclose);
        }
    }

    @OnClick(R.id.exit_chat)
    void onExitChat(){
        if(!TextUtils.isEmpty(gid)) {
            ((ChatGroupDetailsLoader) mPresenter).onExitChatGroup("" + MoreUser.getInstance().getUid(), gid);
        }
    }

    @OnClick(R.id.report)
    void onReport(){
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                ChatGroupDetailsActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(ChatGroupDetailsActivity.this,
                UserDetailsReportActivity.class);
        intent.putExtra("relationID", roomId);
        intent.putExtra("type", "2");
        ActivityCompat.startActivity(ChatGroupDetailsActivity.this, intent, compat.toBundle());
    }

    /**
     * 成员列表
     * @param response
     */
    @Override
    public void onChatGroupUserResponse(RespDto response) {

        ArrayList<ChatGroupUser> result = (ArrayList<ChatGroupUser>) response.getData();

        if (result!=null&&result.size()>0){
            dataList.addAll(result);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChatGroupUserFailure(String msg) {

    }

    @Override
    public void onRoomHxDetailsFaiure(String msg) {

    }



    @Override
    public void onRoomHxDetailsResponse(RespDto response) {
        HxRoomDetails result = (HxRoomDetails) response.getData();

        if (!TextUtils.isEmpty(result.getGroupId())) {
            gid = result.getGroupId();
            ((ChatGroupDetailsLoader) mPresenter).loadChatGroupUser(result.getGroupId(), "0", "20");
        }
    }

    /**
     * 退群
     * @param msg
     */
    @Override
    public void onExitChatGroupFaiure(String msg) {

    }

    @Override
    public void onExitChatGroupResponse(RespDto response) {
        EMClient.getInstance().chatManager().deleteConversation(roomId,false);
        Intent intent = new Intent(this, SuperMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
