package com.moreclub.moreapp.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;

/**
 * Created by Administrator on 2017/5/4.
 */

public class ShareHandler {

    private Activity context;
    private PopupWindow mShareWindow;
    private View popupShareView;
    private int module;
    private String moduleId;
    private ShareUtils.IShareCallBackLinstener shareCallBack;
    public TextView shareTitle;
    public TextView shareContent;

    public ShareHandler(Activity context, int module,  String moduleId) {
        this.context = context;
        this.module = module;
        this.moduleId = moduleId;
    }

    public void showShareView() {
        if (null == mShareWindow) {
            popupShareView = LayoutInflater.from(context).inflate(
                    R.layout.share_view, null);
            popupShareView.findViewById(R.id.view_weight).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mShareWindow != null && mShareWindow.isShowing()) {
                        mShareWindow.dismiss();
                    }
                }
            });
            LinearLayout weixinFriendLay = (LinearLayout) popupShareView.findViewById(R.id.wx_friend_lay);
            LinearLayout weixinGroupLay = (LinearLayout) popupShareView.findViewById(R.id.wx_chat_lay);
            LinearLayout weiboLay = (LinearLayout) popupShareView.findViewById(R.id.wb_lay);
            shareTitle = (TextView) popupShareView.findViewById(R.id.share_title);
            shareContent = (TextView) popupShareView.findViewById(R.id.share_content);
            weixinFriendLay.setOnClickListener(weixinFriendShareListener);
            weixinGroupLay.setOnClickListener(weixinGroupShareListener);
            weiboLay.setOnClickListener(weiboShareListener);

            int width = ScreenUtil.getScreenWidth(context);

            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            popupShareView.measure(spec,spec);
            int height=popupShareView.getMeasuredHeight();

            //设置弹出部分和面积大小
            mShareWindow = new PopupWindow(popupShareView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            //设置动画弹出效果
            mShareWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mShareWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
            mShareWindow.setTouchable(true);
            mShareWindow.setFocusable(true);
            mShareWindow.setOutsideTouchable(true);

        }
        int[] pos = new int[2];

        mShareWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], pos[1]);
    }

    View.OnClickListener weixinFriendShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(context,1, 1, module, moduleId);
            if (shareCallBack!=null){
                share.setOnShareCallBack(shareCallBack);
            }
            share.share();
        }
    };

    View.OnClickListener weixinGroupShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(context,0, 1, module, moduleId);
            if (shareCallBack!=null){
                share.setOnShareCallBack(shareCallBack);
            }
            share.share();
        }
    };

    View.OnClickListener weiboShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShareUtils share = new ShareUtils(context, 0, 2, module, moduleId);
            if (shareCallBack!=null){
                share.setOnShareCallBack(shareCallBack);
            }
            share.share();
        }
    };

    /**
     * 分享回调
     * @param back
     */
    public void setOnShareCallBack(ShareUtils.IShareCallBackLinstener back){
        shareCallBack = back;
    }
}
