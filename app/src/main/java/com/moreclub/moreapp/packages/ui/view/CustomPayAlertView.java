package com.moreclub.moreapp.packages.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.moreapp.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Captain on 2017/8/15.
 */

public class CustomPayAlertView {

    public enum Style{
        ActionSheet,
        Alert
    }
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );
    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    public static final int CANCELPOSITION = -1;//点击取消按钮返回 －1，其他按钮从0开始算

    private String title;
    private String msg;
    private String[] destructive;
    private String[] others;
    private List<String> mDestructive;
    private List<String> mOthers;
    private String cancel;
    private String confirm;
    private ArrayList<String> mDatas = new ArrayList<String>();

    private WeakReference<Context> contextWeak;
    private ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//AlertView 的 根View
    private ViewGroup loAlertHeader;//窗口headerView

    private CustomPayAlertView.Style style = CustomPayAlertView.Style.Alert;

    private OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;
    private boolean isShowing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;

    public CustomPayAlertView(CustomPayAlertView.Builder builder) {
        this.contextWeak = new WeakReference<>(builder.context);
        this.style = builder.style;
        this.title = builder.title;
        this.msg = builder.msg;
        this.cancel = builder.cancel;
        this.destructive = builder.destructive;
        this.others = builder.others;
        this.onItemClickListener = builder.onItemClickListener;

        initData(title, msg, cancel, destructive, others);
        initViews();
        init();
        initEvents();
    }

    public CustomPayAlertView(String title, String msg, String cancel, String[] destructive, String[] others, Context context, CustomPayAlertView.Style style, OnItemClickListener onItemClickListener){
        this.contextWeak = new WeakReference<>(context);
        if(style != null)this.style = style;
        this.onItemClickListener = onItemClickListener;
        this.cancel = cancel;
        if (destructive.length>0)
            this.confirm = destructive[0];
        else {
            this.confirm = "确定";
        }
        initData(title, msg, cancel, destructive, others);
        initViews();
        init();
        initEvents();
    }

    /**
     * 获取数据
     */
    protected void initData(String title, String msg, String cancel, String[] destructive, String[] others) {

        this.title = title;
        this.msg = msg;
        if (destructive != null){
            this.mDestructive = Arrays.asList(destructive);
            this.mDatas.addAll(mDestructive);
        }
        if (others != null){
            this.mOthers = Arrays.asList(others);
            this.mDatas.addAll(mOthers);
        }
        if (cancel != null){
            this.cancel = cancel;
            if(style == CustomPayAlertView.Style.Alert && mDatas.size() < HORIZONTAL_BUTTONS_MAXCOUNT){
                this.mDatas.add(0,cancel);
            }
        }
    }
    protected void initViews(){
        Context context = contextWeak.get();
        if(context == null) return;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(com.moreclub.common.R.layout.layout_alertview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(com.moreclub.common.R.id.content_container);
        int margin_alert_left_right = 0;
        switch (style){
            case ActionSheet:
                params.gravity = Gravity.BOTTOM;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(com.moreclub.common.R.dimen.margin_actionsheet_left_right);
                params.setMargins(margin_alert_left_right,0,margin_alert_left_right,margin_alert_left_right);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.BOTTOM;
                initActionSheetViews(layoutInflater);
                break;
            case Alert:
                params.gravity = Gravity.CENTER;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(com.moreclub.common.R.dimen.margin_alert_left_right);
                params.setMargins(margin_alert_left_right,0,margin_alert_left_right,0);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.CENTER;
                initAlertViews(layoutInflater);
                break;
        }
    }
    protected void initHeaderView(ViewGroup viewGroup){
        loAlertHeader = (ViewGroup) viewGroup.findViewById(com.moreclub.common.R.id.loAlertHeader);
        //标题和消息
        TextView tvAlertTitle = (TextView) viewGroup.findViewById(com.moreclub.common.R.id.tvAlertTitle);
        TextView tvAlertMsg = (TextView) viewGroup.findViewById(com.moreclub.common.R.id.tvAlertMsg);
        if(title != null) {
            tvAlertTitle.setText(title);
        }else{
            tvAlertTitle.setVisibility(View.GONE);
        }
        if(msg != null) {
            tvAlertMsg.setText(msg);
        }else{
            tvAlertMsg.setVisibility(View.GONE);
        }
    }
    protected void initListView(){
        Context context = contextWeak.get();
        if(context == null) return;

        ListView alertButtonListView = (ListView) contentContainer.findViewById(com.moreclub.common.R.id.alertButtonListView);
        //把cancel作为footerView
        if(cancel != null && style == CustomPayAlertView.Style.Alert){
            View itemView = LayoutInflater.from(context).inflate(com.moreclub.common.R.layout.item_alertbutton, null);
            TextView tvAlert = (TextView) itemView.findViewById(com.moreclub.common.R.id.tvAlert);
            tvAlert.setText(cancel);
            tvAlert.setClickable(true);
            tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
            tvAlert.setTextColor(context.getResources().getColor(com.moreclub.common.R.color.textColor_alert_button_cancel));
            tvAlert.setBackgroundResource(com.moreclub.common.R.drawable.bg_alertbutton_bottom);
            tvAlert.setOnClickListener(new CustomPayAlertView.OnTextClickListener(CANCELPOSITION));
            alertButtonListView.addFooterView(itemView);
        }
        AlertViewAdapter adapter = new AlertViewAdapter(mDatas,mDestructive);
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(onItemClickListener != null)onItemClickListener.onItemClick(CustomPayAlertView.this,position);
                dismiss();
            }
        });
    }
    protected void initActionSheetViews(LayoutInflater layoutInflater) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(com.moreclub.common.R.layout.layout_alertview_actionsheet,contentContainer);
        initHeaderView(viewGroup);

        initListView();
        TextView tvAlertCancel = (TextView) contentContainer.findViewById(com.moreclub.common.R.id.tvAlertCancel);
        if(cancel != null){
            tvAlertCancel.setVisibility(View.VISIBLE);
            tvAlertCancel.setText(cancel);
        }
        tvAlertCancel.setOnClickListener(new CustomPayAlertView.OnTextClickListener(CANCELPOSITION));
    }
    protected void initAlertViews(LayoutInflater layoutInflater) {
        Context context = contextWeak.get();
        if(context == null) return;

        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.custom_pay_alertview, contentContainer);
        initHeaderView(viewGroup);

        TextView cancelBtn = (TextView) viewGroup.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new CustomPayAlertView.OnTextClickListener(CANCELPOSITION));
        cancelBtn.setText(cancel);
        TextView confirmTxt = (TextView) viewGroup.findViewById(R.id.confimBtn);
        confirmTxt.setOnClickListener(new CustomPayAlertView.OnTextClickListener(0));
        confirmTxt.setText(confirm);

        ImageView closeDialog = (ImageView) viewGroup.findViewById(R.id.closeDialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        int position = 0;
//        //如果总数据小于等于HORIZONTAL_BUTTONS_MAXCOUNT，则是横向button
//        if(mDatas.size()<=HORIZONTAL_BUTTONS_MAXCOUNT){
//            ViewStub viewStub = (ViewStub) contentContainer.findViewById(com.moreclub.common.R.id.viewStubHorizontal);
//            viewStub.inflate();
//            LinearLayout loAlertButtons = (LinearLayout) contentContainer.findViewById(com.moreclub.common.R.id.loAlertButtons);
//            for (int i = 0; i < mDatas.size(); i ++) {
//                //如果不是第一个按钮
//                if (i != 0){
//                    //添加上按钮之间的分割线
//                    View divier = new View(context);
//                    divier.setBackgroundColor(context.getResources().getColor(com.moreclub.common.R.color.bgColor_divier));
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)context.getResources().getDimension(com.moreclub.common.R.dimen.size_divier), LinearLayout.LayoutParams.MATCH_PARENT);
//                    loAlertButtons.addView(divier,params);
//                }
//                View itemView = LayoutInflater.from(context).inflate(com.moreclub.common.R.layout.item_alertbutton, null);
//                TextView tvAlert = (TextView) itemView.findViewById(com.moreclub.common.R.id.tvAlert);
//                tvAlert.setClickable(true);
//
//                //设置点击效果
//                if(mDatas.size() == 1){
//                    tvAlert.setBackgroundResource(com.moreclub.common.R.drawable.bg_alertbutton_bottom);
//                }
//                else if(i == 0){//设置最左边的按钮效果
//                    tvAlert.setBackgroundResource(com.moreclub.common.R.drawable.bg_alertbutton_left);
//                }
//                else if(i == mDatas.size() - 1){//设置最右边的按钮效果
//                    tvAlert.setBackgroundResource(com.moreclub.common.R.drawable.bg_alertbutton_right);
//                }
//                String data = mDatas.get(i);
//                tvAlert.setText(data);
//
//                //取消按钮的样式
//                if (data == cancel){
//                    tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
//                    tvAlert.setTextColor(context.getResources().getColor(com.moreclub.common.R.color.textColor_alert_button_cancel));
//                    tvAlert.setOnClickListener(new CustomPayAlertView.OnTextClickListener(CANCELPOSITION));
//                    position = position - 1;
//                }
//                //高亮按钮的样式
//                else if (mDestructive!= null && mDestructive.contains(data)){
//                    tvAlert.setTextColor(context.getResources().getColor(com.moreclub.common.R.color.textColor_alert_button_destructive));
//                }
//
//                tvAlert.setOnClickListener(new CustomPayAlertView.OnTextClickListener(position));
//                position++;
//                loAlertButtons.addView(itemView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//            }
//        }
//        else{
//            ViewStub viewStub = (ViewStub) contentContainer.findViewById(com.moreclub.common.R.id.viewStubVertical);
//            viewStub.inflate();
//            initListView();
//        }
    }
    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    protected void initEvents() {
    }
    public CustomPayAlertView addExtView(View extView){
        loAlertHeader.addView(extView);
        return this;
    }
    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        isShowing = true;
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }
    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }
    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null && isShowing;
    }

    public void dismiss() {
        //消失动画
        outAnim.setAnimationListener(outAnimListener);
        contentContainer.startAnimation(outAnim);
    }

    public void dismissImmediately() {
        decorView.removeView(rootView);
        isShowing = false;
        if(onDismissListener != null){
            onDismissListener.onDismiss(this);
        }

    }

    public Animation getInAnimation() {
        Context context = contextWeak.get();
        if(context == null) return null;

        int res = AlertAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        Context context = contextWeak.get();
        if(context == null) return null;

        int res = AlertAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public CustomPayAlertView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    class OnTextClickListener implements View.OnClickListener{

        private int position;
        public OnTextClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            if(onItemClickListener != null)onItemClickListener.onItemClick(CustomPayAlertView.this,position);
            dismiss();
        }
    }
    private Animation.AnimationListener outAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dismissImmediately();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 主要用于拓展View的时候有输入框，键盘弹出则设置MarginBottom往上顶，避免输入法挡住界面
     */
    public void setMarginBottom(int marginBottom){
        Context context = contextWeak.get();
        if(context == null) return;

        int margin_alert_left_right = context.getResources().getDimensionPixelSize(com.moreclub.common.R.dimen.margin_alert_left_right);
        params.setMargins(margin_alert_left_right,0,margin_alert_left_right,marginBottom);
        contentContainer.setLayoutParams(params);
    }
    public CustomPayAlertView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(com.moreclub.common.R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        }
        else{
            view.setOnTouchListener(null);
        }
        return this;
    }
    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    /**
     * Builder for arguments
     */
    public static class Builder {
        private Context context;
        private CustomPayAlertView.Style style;
        private String title;
        private String msg;
        private String cancel;
        private String[] destructive;
        private String[] others;
        private OnItemClickListener onItemClickListener;

        public CustomPayAlertView.Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public CustomPayAlertView.Builder setStyle(CustomPayAlertView.Style style) {
            if(style != null) {
                this.style = style;
            }
            return this;
        }

        public CustomPayAlertView.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public CustomPayAlertView.Builder setMessage(String msg) {
            this.msg = msg;
            return this;
        }

        public CustomPayAlertView.Builder setCancelText(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public CustomPayAlertView.Builder setDestructive(String... destructive) {
            this.destructive = destructive;
            return this;
        }

        public CustomPayAlertView.Builder setOthers(String[] others) {
            this.others = others;
            return this;
        }

        public CustomPayAlertView.Builder setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public CustomPayAlertView build() {
            return new CustomPayAlertView(this);
        }
    }

}
