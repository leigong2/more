package com.moreclub.moreapp.main.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.jiecaovideoplayer.JCVideoPlayer;
import com.moreclub.common.jiecaovideoplayer.JCVideoPlayerStandard;
import com.moreclub.common.log.Logger;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.information.ui.activity.ActivitiesActivity;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.Comments;
import com.moreclub.moreapp.main.model.HeadlineCount;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantCount;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.Package;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.main.model.UserLikesItem;
import com.moreclub.moreapp.main.presenter.IMainChannelAttentionLoader;
import com.moreclub.moreapp.main.presenter.MainChannelAttentionDataBinder;
import com.moreclub.moreapp.main.ui.activity.MChannelCommentActivity;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.main.ui.activity.MChannelListActivity;
import com.moreclub.moreapp.main.ui.activity.MainActivity;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.MerchantListViewActivity;
import com.moreclub.moreapp.main.ui.activity.TotalBarsActivity;
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.packages.ui.activity.PackageSuperMainListActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.view.ViewPagerFixed;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moreclub.common.util.TimeUtils.formatSpace;

/**
 * Created by Administrator on 2017/2/25.
 */

public class MainMultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements IMainChannelAttentionLoader.LoadMainDataBinder {

    private ArrayList<MerchantCount> merchantCounts;
    private ArrayList<HeadlineCount> headlineCounts;
    public ArrayList<BizArea> areas;
    public ArrayList<TagDict> dicts;
    public ArrayList<MerchantItem> merchants;
    public ArrayList<Package> packages;
    private List<ActivityData.ActivityItem> activities;
    private ArrayList<MainChannelItem> mainChannels;
    private int mExtraCount = 5;
    private String today = formatTime(System.currentTimeMillis());
    private boolean isNewHeadline;
    private boolean isNewStore;
    private MainActivitiesAdapter activitiesAdapter;

    private BasePresenter mPresenter;
    private PopupWindow mPopupWindow;
    private View popupParentView;
    private ViewPagerFixed sceneViewPager;

    private PopupWindow mCommentWindow;
    private View commentParentView;
    private EditText commentView;
    private int attentionPositon = 0;
    private int likePosition = 0;
    private Comments mComment;
    private AlertView photoAlertView;
    @Override
    public void onAttentionChannelResponse(RespDto responce) {
        ChannelAttentionResult result = (ChannelAttentionResult) responce.getData();
        if (result != null) {
            MainChannelItem clickItem = mainChannels.get(attentionPositon);
            clickItem.setFollow(true);
        }
    }

    @Override
    public void onAttentionChannelFailure(String msg) {

    }

    @Override
    public void onLikeChannelFailure(String msg) {
        Log.i("zune:", "msg = ");
    }

    @Override
    public void onLikeChannelResponse(RespDto responce) {
        Boolean result = (Boolean) responce.getData();
        if (result) {
            MainChannelItem clickItem = mainChannels.get(likePosition);
            MainChannelItem.LikeDto likeDto = clickItem.getLikeDto();
            if (likeDto == null) {
                return;
            }
            likeDto.setRefresh(true);
            ArrayList<UserLikesItem> userLikesItem = likeDto.getUserLikes();
            if (userLikesItem == null) {
                userLikesItem = new ArrayList<UserLikesItem>();
                likeDto.setUserLikes(userLikesItem);
            }
            UserLikesItem my = new UserLikesItem();
            my.setUid(MoreUser.getInstance().getUid());
            my.setNickName(MoreUser.getInstance().getNickname());
            my.setThumb(MoreUser.getInstance().getThumb());
            userLikesItem.add(0, my);
            likeDto.setLikeTimes(likeDto.getLikeTimes() + 1);
            notifyDataSetChanged();
            Log.d("dddd", "channel scuess");
        } else {
            MainChannelItem clickItem = mainChannels.get(likePosition);
            MainChannelItem.LikeDto likeDto = clickItem.getLikeDto();
            likeDto.setRefresh(true);
            ArrayList<UserLikesItem> userLikesItem = likeDto.getUserLikes();
            if (userLikesItem != null) {
                for (int i = 0; i < userLikesItem.size(); i++) {
                    if (userLikesItem.get(i) != null && userLikesItem.get(i).getUid() > 0
                            && userLikesItem.get(i).getUid() == MoreUser.getInstance().getUid()) {
                        userLikesItem.remove(i);
                        break;
                    }
                }
            }
            likeDto.setLikeTimes(likeDto.getLikeTimes() - 1);
            notifyDataSetChanged();
            Log.d("dddd", "channel cancel scuess");
        }
    }

    private enum ITEM_TYPE {
        ITEM_TYPE_ONE,
        ITEM_TYPE_ACTIVITY,
        ITEM_TYPE_BIZAREA,
        ITEM_TYPE_TAG,
        ITEM_TYPE_ACTIVITIES,
        ITEM_TYPE_PKG,
        ITEM_TYPE_BARTITLE,
        ITEM_TYPE_BAR,
        ITEM_TYPE_CHANNELTITLE,
        ITEM_TYPE_CHANNEL
    }

    private MainBizAreaAdapter areaAdapter;
    private Context context;
    private MainPackagesAdapter pkgAdapter;
    private MainBarRecommChildAdapter barsAdapter;
    private boolean mIsRefreshing1 = false;
    private boolean mIsRefreshing2 = false;

    public MainMultiTypeAdapter(Context cont,
                                ArrayList<BizArea> areas,
                                ArrayList<TagDict> dicts,
                                ArrayList<MerchantItem> merchants,
                                ArrayList<Package> pkgs,
                                ArrayList<HeadlineCount> headlineCounts,
                                ArrayList<MerchantCount> merchantCounts,
                                List<ActivityData.ActivityItem> activities,
                                ArrayList<MainChannelItem> channels) {
        this.areas = areas;
        this.dicts = dicts;
        this.context = cont;
        this.merchants = merchants;
        this.packages = pkgs;
        this.headlineCounts = headlineCounts;
        this.merchantCounts = merchantCounts;
        this.activities = activities;
        this.mainChannels = channels;
        areaAdapter = new MainBizAreaAdapter(context, areas);
        pkgAdapter = new MainPackagesAdapter(context, R.layout.main_packages_child_item, pkgs);
        activitiesAdapter = new MainActivitiesAdapter(context, R.layout.main_activities_items_item, activities);
        barsAdapter = new MainBarRecommChildAdapter(context, R.layout.main_bar_recomm_item, merchants);
        mPresenter = LogicProxy.getInstance().bind(IMainChannelAttentionLoader.class, this);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_ONE.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_barclass_item, viewGroup, false);
            return new ItemOneViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_BIZAREA.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_bizarea_item, viewGroup, false);
            return new ItemTwoViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TAG.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_bartag_item, viewGroup, false);
            return new ItemTagViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_PKG.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_packages_item, viewGroup, false);
            return new ItemPackageViewHolder(context, view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_BARTITLE.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_bartitle_item, viewGroup, false);
            return new ItemTitleViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_BAR.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_bars_item, viewGroup, false);

            return new ItemBarViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_ACTIVITY.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_activity_item, viewGroup, false);
            return new ItemActivityViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_ACTIVITIES.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_packages_item, viewGroup, false);
            return new ItemActivitiesViewHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_CHANNELTITLE.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_channel_title, viewGroup, false);
            return new ItemChannelTitleHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_CHANNEL.ordinal()) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.main_channel_item, viewGroup, false);
            return new ItemChannelHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemOneViewHolder) {

        } else if (viewHolder instanceof ItemTwoViewHolder) {
            RecyclerView recyclerView = ((ItemTwoViewHolder) viewHolder).recyclerView;
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            if (mIsRefreshing1) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
            );

            if (recyclerView.getAdapter() == null) {
                mIsRefreshing1 = true;
                recyclerView.setAdapter(areaAdapter);
                mIsRefreshing1 = false;
            }


        } else if (viewHolder instanceof ItemTagViewHolder) {
            RecyclerView recyclerView = ((ItemTagViewHolder) viewHolder).recyclerView;
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);

            final TagDict ItemTagDict = dicts.get(position - mExtraCount + 2);
            final List<TagDict> arrayData = ItemTagDict.getDics();
            MainTagAdapter tagAdapter = new MainTagAdapter(context,
                    R.layout.main_bartag_child_item,
                    arrayData);

            recyclerView.setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            if (mIsRefreshing2) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
            );

            mIsRefreshing2 = true;
            recyclerView.setAdapter(tagAdapter);
            mIsRefreshing2 = false;

            tagAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                    TagDict itemValue = arrayData.get(position);
                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeCustomAnimation(context,
                                    R.anim.slide_right_in,
                                    R.anim.fade_out);

                    Intent intent = new Intent(context, MerchantListViewActivity.class);
                    intent.putExtra("searchKey", itemValue.getName());
                    intent.putExtra("searchType", ItemTagDict.getId());
                    ActivityCompat.startActivity(context, intent, compat.toBundle());
                    Logger.d("tagAdapter", itemValue);
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                    return false;
                }
            });

            TextView textView = ((ItemTagViewHolder) viewHolder).textView;
            String name = dicts.get(position - mExtraCount + 2).getName();
            name = name.replaceAll("\r|\n", "");
            String prefix = name.substring(0, 2);
            String suffix = name.substring(2, 4);
            textView.setText(prefix + "\n" + suffix);

        } else if (viewHolder instanceof ItemPackageViewHolder) {
            RecyclerView recyclerView = ((ItemPackageViewHolder) viewHolder).recyclerView;
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getAdapter() == null)
                recyclerView.setAdapter(pkgAdapter);


            pkgAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                    Package itemValue = packages.get(position);
                    if (TextUtils.isEmpty(itemValue.getTitle())) return;

                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeCustomAnimation(context,
                                    R.anim.slide_right_in,
                                    R.anim.fade_out);

                    Intent intent = new Intent(context, PackageDetailsActivity.class);
                    intent.putExtra("pid", "" + itemValue.getPid());
                    intent.putExtra("mid", "" + itemValue.getMerchantId());
                    context.startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                    return false;
                }
            });
        } else if (viewHolder instanceof ItemTitleViewHolder) {

        } else if (viewHolder instanceof ItemChannelTitleHolder) {
            ((ItemChannelTitleHolder) viewHolder).channelGotoAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.pushLeftActivity(context, MChannelListActivity.class);
//                    AppUtils.pushLeftActivity(context, MChannelDetailsAcitivy.class);

                }
            });

        } else if (viewHolder instanceof ItemActivityViewHolder) {

            ViewGroup.LayoutParams root_lp = new ViewGroup.LayoutParams(0, 0);
            ((ItemActivityViewHolder) viewHolder).llRoot.setLayoutParams(root_lp);

//            final RelativeLayout headline = ((ItemActivityViewHolder) viewHolder).headlineLayout;
//            final RelativeLayout newStore = ((ItemActivityViewHolder) viewHolder).newStoreLayout;
//            final RelativeLayout activity = ((ItemActivityViewHolder) viewHolder).activityLayout;
//            initActivityData(Constants.HEADLINE, ((ItemActivityViewHolder) viewHolder).viewHeadline,
//                    ((ItemActivityViewHolder) viewHolder).ivEye1,
//                    ((ItemActivityViewHolder) viewHolder).redPointHeadline);
//            initActivityData(Constants.NEW_STORE, ((ItemActivityViewHolder) viewHolder).viewNewStore,
//                    ((ItemActivityViewHolder) viewHolder).tvNewStore,
//                    ((ItemActivityViewHolder) viewHolder).redPointNewStore);
//            headline.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    /**zune:头条入口**/
//                    AppUtils.pushLeftActivity(headline.getContext(), HeadlineActivity.class);
//                    if (isNewHeadline) {
//                        cancelBackground(Constants.HEADLINE, ((ItemActivityViewHolder) viewHolder).viewHeadline,
//                                ((ItemActivityViewHolder) viewHolder).redPointHeadline);
//                    }
//                }
//            });
//            newStore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    /**zune:新店入口**/
//                    AppUtils.pushLeftActivity(headline.getContext(), NewStoreActivity.class);
//                    if (isNewStore) {
//                        cancelBackground(Constants.NEW_STORE, ((ItemActivityViewHolder) viewHolder).viewNewStore,
//                                ((ItemActivityViewHolder) viewHolder).redPointNewStore);
//                    }
//                }
//            });
//            activity.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    /**zune:活动入口**/
//                    AppUtils.pushLeftActivity(headline.getContext(), ActivitiesActivity.class);
//                }
//            });
        } else if (viewHolder instanceof ItemActivitiesViewHolder) {
            ViewGroup.LayoutParams root_lp;
            if (activities == null || activities.size() == 0) {
                root_lp = new ViewGroup.LayoutParams(0, 0);
            } else {
                root_lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            ((ItemActivitiesViewHolder) viewHolder).llRoot.setLayoutParams(root_lp);
            ((ItemActivitiesViewHolder) viewHolder).titleZh.setText("近期活动");
            ((ItemActivitiesViewHolder) viewHolder).titleEn.setText("RECENT ACTIVITY");
            RecyclerView recyclerView = ((ItemActivitiesViewHolder) viewHolder).mainPackagesRv;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , ScreenUtil.dp2px(context, 318));
            lp.setMargins(ScreenUtil.dp2px(context, 16), ScreenUtil.dp2px(context, 12), 0, 0);
            recyclerView.setLayoutParams(lp);
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getAdapter() == null)
                recyclerView.setAdapter(activitiesAdapter);
            ((ItemActivitiesViewHolder) viewHolder).mainPackagesGotoAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.pushLeftActivity(context, ActivitiesActivity.class);
                }
            });
            activitiesAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                    //Todo 进入活动详情
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            parent.getContext(), R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(parent.getContext(), LiveActivity.class);
                    intent.putExtra(com.moreclub.moreapp.information.constant.Constants.KEY_ACT_ID, activities.get(position).getActivityId());
                    ActivityCompat.startActivity(parent.getContext(), intent, compat.toBundle());
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                    return false;
                }
            });
        } else if (viewHolder instanceof ItemBarViewHolder) {
            RecyclerView recyclerView = ((ItemBarViewHolder) viewHolder).recyclerView;
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getAdapter() == null)
                recyclerView.setAdapter(barsAdapter);

            barsAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                    MerchantItem item = (MerchantItem) o;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeCustomAnimation(context,
                                    R.anim.slide_right_in, R.anim.fade_out);
                    Intent intent = new Intent(context, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", "" + item.getMid());
                    ActivityCompat.startActivity(context, intent, compat.toBundle());
                }

                @Override
                public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                    return false;
                }
            });

            ((ItemBarViewHolder) viewHolder).barsGotoAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.pushAnimActivity(context, TotalBarsActivity.class,
                            R.anim.slide_right_in, R.anim.fade_out);
                }
            });

        } else if (viewHolder instanceof ItemChannelHolder) {
            String postUrl;
            final int size = mExtraCount + dicts.size();
            final int curPos = position - 10;
            if (mainChannels == null)
                return;
            final MainChannelItem item = mainChannels.get(curPos);
            if (item == null)
                return;

            JCVideoPlayer.releaseAllVideos();

            if (item.getSource() == 1) {
                ((ItemChannelHolder) viewHolder).headerTag.setVisibility(View.VISIBLE);
            } else {
                ((ItemChannelHolder) viewHolder).headerTag.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getSource() == 1) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelDetailsAcitivy.class);
                        intent.putExtra("sid", item.getInformationId());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });

            ((ItemChannelHolder) viewHolder).ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 删除详情
//                holder.setVisible(R.id.rl_delete, true);
//                showDeleteDialog(curPos, item);
                    showDeletePop(curPos, item);
                }
            });

            if (item.getType() == 4) {
                ((ItemChannelHolder) viewHolder).tvAllContent.setVisibility(View.VISIBLE);
            } else {
                ((ItemChannelHolder) viewHolder).tvAllContent.setVisibility(View.GONE);
            }
            ImageView headerImage = ((ItemChannelHolder) viewHolder).headerImage;
            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    if (item.getType() == 4) {
                        ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                        intent_merchant.putExtra("toUid", "" + item.getUid());
                        intent_merchant.putExtra("isChannel", "true");
                        MainChannelItem.ChainMerchants merchants = null;
                        if (item.getChainMerchants() != null && item.getChainMerchants().size() > 0) {
                            merchants = item.getChainMerchants().get(0);
                        }
                        if (merchants != null) {
                            intent_merchant.putExtra("mid", merchants.getMid());
                        }
                        ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
                    } else {
                        ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                        intent_merchant.putExtra("toUid", "" + item.getUid());
                        ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
                    }
                }
            });
            TextView headerName = ((ItemChannelHolder) viewHolder).headerName;
            TextView headerTime = ((ItemChannelHolder) viewHolder).headerTime;
            TextView attentionBtn = ((ItemChannelHolder) viewHolder).attentionBtn;
            if (!item.isFollow()) {
                attentionBtn.setVisibility(View.VISIBLE);
            } else {
                attentionBtn.setVisibility(View.GONE);
            }
            attentionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    v.setVisibility(View.GONE);
                    if (item.getType() == 1) {
                        attentionPositon = curPos;
                        ChannelAttentionParam param = new ChannelAttentionParam();
                        param.setFriendId(item.getUid());
                        param.setOwnerId(MoreUser.getInstance().getUid());
                        param.setRemark("1");
                        ((MainChannelAttentionDataBinder) mPresenter).onAttentionChannel(param);
                    }
                }
            });
            TextView channelDes = ((ItemChannelHolder) viewHolder).channelDes;
            RelativeLayout picgridView = ((ItemChannelHolder) viewHolder).picgridView;
            TextView addresstext = ((ItemChannelHolder) viewHolder).address;
            addresstext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainChannelItem.ChainMerchants merchants = null;
                    if (item.getChainMerchants() != null && item.getChainMerchants().size() > 0) {
                        merchants = item.getChainMerchants().get(0);
                    }
                    if (merchants != null) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MerchantDetailsViewActivity.class);
                        intent.putExtra("mid", "" + merchants.getMid());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });

            RelativeLayout mediaLayout = ((ItemChannelHolder) viewHolder).mediaLayout;
            JCVideoPlayerStandard playerView = ((ItemChannelHolder) viewHolder).videoplayer;
            RelativeLayout nineLayout = ((ItemChannelHolder) viewHolder).nineLayout;
            ImageView oneLayout = ((ItemChannelHolder) viewHolder).oneLayout;
            RelativeLayout fourLayout = ((ItemChannelHolder) viewHolder).fourLayout;
            ImageView topLeftImg = ((ItemChannelHolder) viewHolder).topLeftImg;
            topLeftImg.setOnClickListener(channelPictureClick);
            topLeftImg.setTag(R.id.top_left_img, curPos);
            ImageView topRightImg = ((ItemChannelHolder) viewHolder).topRightImg;
            topRightImg.setOnClickListener(channelPictureClick);
            topRightImg.setTag(R.id.top_right_img, curPos);
            ImageView bottomLeftImg = ((ItemChannelHolder) viewHolder).bottomLeftImg;
            bottomLeftImg.setOnClickListener(channelPictureClick);
            bottomLeftImg.setTag(R.id.bottom_left_img, curPos);
            ImageView bottomRightImg = ((ItemChannelHolder) viewHolder).bottomRightImg;
            bottomRightImg.setOnClickListener(channelPictureClick);
            bottomRightImg.setTag(R.id.bottom_right_img, curPos);
            ImageView nine_layout1 = ((ItemChannelHolder) viewHolder).nine_layout1;
            nine_layout1.setOnClickListener(channelPictureClick);
            nine_layout1.setTag(R.id.nine_layout1, curPos);
            ImageView nine_layout2 = ((ItemChannelHolder) viewHolder).nine_layout2;
            nine_layout2.setOnClickListener(channelPictureClick);
            nine_layout2.setTag(R.id.nine_layout2, curPos);
            ImageView nine_layout3 = ((ItemChannelHolder) viewHolder).nine_layout3;
            nine_layout3.setOnClickListener(channelPictureClick);
            nine_layout3.setTag(R.id.nine_layout3, curPos);
            ImageView nine_layout4 = ((ItemChannelHolder) viewHolder).nine_layout4;
            nine_layout4.setOnClickListener(channelPictureClick);
            nine_layout4.setTag(R.id.nine_layout4, curPos);
            ImageView nine_layout5 = ((ItemChannelHolder) viewHolder).nine_layout5;
            nine_layout5.setOnClickListener(channelPictureClick);
            nine_layout5.setTag(R.id.nine_layout5, curPos);
            ImageView nine_layout6 = ((ItemChannelHolder) viewHolder).nine_layout6;
            nine_layout6.setOnClickListener(channelPictureClick);
            nine_layout6.setTag(R.id.nine_layout6, curPos);
            ImageView nine_layout7 = ((ItemChannelHolder) viewHolder).nine_layout7;
            nine_layout7.setOnClickListener(channelPictureClick);
            nine_layout7.setTag(R.id.nine_layout7, curPos);
            ImageView nine_layout8 = ((ItemChannelHolder) viewHolder).nine_layout8;
            nine_layout8.setOnClickListener(channelPictureClick);
            nine_layout8.setTag(R.id.nine_layout8, curPos);
            ImageView nine_layout9 = ((ItemChannelHolder) viewHolder).nine_layout9;
            nine_layout9.setOnClickListener(channelPictureClick);
            nine_layout9.setTag(R.id.nine_layout9, curPos);

            LinearLayout likeBtn = ((ItemChannelHolder) viewHolder).likeBtn;
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                        likePosition = curPos;
/*                        ((MainChannelAttentionDataBinder) mPresenter).
                                onLikeChannel(item.getInformationId(),
                                        MoreUser.getInstance().getUid(), "likeTime1");*/
                        Callback<RespDto<Boolean>> callback
                                = new Callback<RespDto<Boolean>>() {
                            @Override
                            public void onResponse(Call<RespDto<Boolean>> call,
                                                   Response<RespDto<Boolean>> response) {
                                if (response == null || response.body() == null) return;
                                Boolean result = (Boolean) response.body().getData();
                                if (result) {
                                    MainChannelItem clickItem = mainChannels.get(likePosition);
                                    MainChannelItem.LikeDto likeDto = clickItem.getLikeDto();
                                    if (likeDto == null) {
                                        return;
                                    }
                                    likeDto.setRefresh(true);
                                    ArrayList<UserLikesItem> userLikesItem = likeDto.getUserLikes();
                                    if (userLikesItem == null) {
                                        userLikesItem = new ArrayList<UserLikesItem>();
                                        likeDto.setUserLikes(userLikesItem);
                                    }
                                    UserLikesItem my = new UserLikesItem();
                                    my.setUid(MoreUser.getInstance().getUid());
                                    my.setNickName(MoreUser.getInstance().getNickname());
                                    my.setThumb(MoreUser.getInstance().getThumb());
                                    userLikesItem.add(0, my);
                                    likeDto.setLikeTimes(likeDto.getLikeTimes() + 1);
                                    notifyDataSetChanged();
                                    Log.d("dddd", "channel scuess");
                                } else {
                                    MainChannelItem clickItem = mainChannels.get(likePosition);
                                    MainChannelItem.LikeDto likeDto = clickItem.getLikeDto();
                                    likeDto.setRefresh(true);
                                    ArrayList<UserLikesItem> userLikesItem = likeDto.getUserLikes();
                                    if (userLikesItem != null) {
                                        for (int i = 0; i < userLikesItem.size(); i++) {
                                            if (userLikesItem.get(i) != null && userLikesItem.get(i).getUid() > 0
                                                    && userLikesItem.get(i).getUid() == MoreUser.getInstance().getUid()) {
                                                userLikesItem.remove(i);
                                                break;
                                            }
                                        }
                                    }
                                    likeDto.setLikeTimes(likeDto.getLikeTimes() - 1);
                                    notifyDataSetChanged();
                                    Log.d("dddd", "channel cancel scuess");
                                }
                            }

                            @Override
                            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

                            }
                        };

                        String token = MoreUser.getInstance().getAccess_token();
                        ApiInterface.ApiFactory.createApi(token).onLikeChannel(item.getInformationId()
                                , MoreUser.getInstance().getUid(), "likeTime1").enqueue(callback);


                    } else {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    }

                }
            });

            int likeHeaderWidth = ScreenUtil.dp2px(context, 24);
            int likeHeaderTop = ScreenUtil.dp2px(context, 8);
            LinearLayout likePeopleLayout = ((ItemChannelHolder) viewHolder).likePeopleLayout;
            MainChannelItem.LikeDto likeDto = item.getLikeDto();
            ArrayList<UserLikesItem> userLikes = likeDto.getUserLikes();
//            if ((userLikes != null && userLikes.size() > 0 && likePeopleLayout.getChildCount() == 0)
//                    || (likeDto.isRefresh() && userLikes != null)) {
            likePeopleLayout.removeAllViews();
            if (userLikes != null && userLikes.size() > 0) {

                for (int i = 0; i < userLikes.size(); i++) {
                    UserLikesItem likesItem = userLikes.get(i);
                    ImageView likeUserItemView = new ImageView(context);
                    LinearLayout.LayoutParams bgParams = new LinearLayout.LayoutParams(
                            likeHeaderWidth,
                            likeHeaderWidth);
                    bgParams.setMargins(8, 8, 8, 8);
                    likeUserItemView.setLayoutParams(bgParams);
                    likeUserItemView.setBackgroundResource(R.drawable.circle_gray_dush);
                    likePeopleLayout.addView(likeUserItemView);
                    if (i >= 6) {
                        likeUserItemView.setImageResource(R.drawable.more_likes);
                        break;
                    } else {
                        Glide.with(context)
                                .load(likesItem.getThumb())
                                .dontAnimate()
                                .transform(new GlideCircleTransform(context, 2, 0xF0F0F0))
                                .placeholder(R.drawable.profilephoto_small)
                                .into(likeUserItemView);
                    }
                }
                likeDto.setRefresh(false);
            }


            TextView commentCount = ((ItemChannelHolder) viewHolder).commentCount;
            commentCount.setText("" + item.getCommentCount());
            ((ItemChannelHolder) viewHolder).commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid() == null || MoreUser.getInstance().getUid().equals(0L)) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    } else {
                        showCommentPopuWindow(item.getInformationId(), MoreUser.getInstance().getNickname(), -1, curPos);
                        mComment = new Comments();
                    }
                }
            });

            TextView goAllCommentBtn = ((ItemChannelHolder) viewHolder).goAllCommentBtn;
            TextView likeCount = ((ItemChannelHolder) viewHolder).likeCount;
            ((ItemChannelHolder) viewHolder).like.setImageResource(R.drawable.like);
            if (item.getLikeDto().getUserLikes() != null) {
                int likesSize = item.getLikeDto().getUserLikes().size();
                for (int i = 0; i < likesSize; i++) {
                    if (item.getLikeDto().getUserLikes().get(i).getUid() == MoreUser.getInstance().getUid()) {
                        ((ItemChannelHolder) viewHolder).like.setImageResource(R.drawable.like2);
                        break;
                    }
                }
            }
            if (item.getLikeDto() != null && item.getLikeDto().getLikeTimes() > 0) {
                int likeCountValue = item.getLikeDto().getLikeTimes();
                if (likeCountValue > 1000) {
                    likeCount.setText("" + (likeCountValue / 1000) + "k");
                } else {
                    likeCount.setText("" + likeCountValue);
                }
            } else {
                likeCount.setText("0");
            }

            TextView commentText1 = ((ItemChannelHolder) viewHolder).comment_text_1;
            TextView commentText2 = ((ItemChannelHolder) viewHolder).comment_text_2;
            TextView commentText3 = ((ItemChannelHolder) viewHolder).comment_text_3;
            commentText1.setVisibility(View.GONE);
            commentText2.setVisibility(View.GONE);
            commentText3.setVisibility(View.GONE);
            LinearLayout ll_comment = ((ItemChannelHolder) viewHolder).ll_comment;

            ArrayList<ImageView> nineArray = new ArrayList<>();
            nine_layout1.setVisibility(View.GONE);
            nineArray.add(nine_layout1);
            nine_layout2.setVisibility(View.GONE);
            nineArray.add(nine_layout2);
            nine_layout3.setVisibility(View.GONE);
            nineArray.add(nine_layout3);
            nine_layout4.setVisibility(View.GONE);
            nineArray.add(nine_layout4);
            nine_layout5.setVisibility(View.GONE);
            nineArray.add(nine_layout5);
            nine_layout6.setVisibility(View.GONE);
            nineArray.add(nine_layout6);
            nine_layout7.setVisibility(View.GONE);
            nineArray.add(nine_layout7);
            nine_layout8.setVisibility(View.GONE);
            nineArray.add(nine_layout8);
            nine_layout9.setVisibility(View.GONE);
            nineArray.add(nine_layout9);

            Glide.with(context)
                    .load(item.getFromThumb())
                    .dontAnimate()
                    .placeholder(R.drawable.profilephoto_small)
                    .override(ScreenUtil.dp2px(context, 40), ScreenUtil.dp2px(context, 40))
                    .transform(new GlideCircleTransform(context, 2, 0xF0F0F0))
                    .into(headerImage);

            headerName.setText(item.getNickName());
            headerTime.setText(formatSpace(item.getPrefeerTime()));
            MainChannelItem.ChainInter chainInteritem = null;
            if (item.getChainInters() != null && item.getChainInters().size() > 0) {
                chainInteritem = item.getChainInters().get(0);
            }
            String chainInterTitle = "";
            if (chainInteritem != null)
                chainInterTitle = chainInteritem.getTitle();
            String chainInter = "";
            if (chainInterTitle != null && chainInterTitle.length() > 0) {
                chainInter = "#" + chainInterTitle + "#  " + item.getPrefeerContent();
                SpannableStringBuilder spannable = new SpannableStringBuilder(chainInter);
                int startIndex = 0;
                int endIndex = chainInterTitle.length() + 2;
                int color = Color.parseColor("#DDB544");
                spannable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                channelDes.setText(spannable);
                channelDes.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                channelDes.setText(item.getPrefeerContent());
            }

//            channelDes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MainChannelItem.ChainInter chainInteritem = null;
//                    if (item.getChainInters() != null && item.getChainInters().size() > 0) {
//                        chainInteritem = item.getChainInters().get(0);
//                    }
//                    String chainInterTitle = "";
//                    if (chainInteritem != null)
//                        chainInterTitle = chainInteritem.getTitle();
//
//                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
//                            context, R.anim.slide_right_in, R.anim.slide_right_out);
//                    Intent intent = new Intent(context, TopicDetailsActivity.class);
//                    intent.putExtra("topicTitle", chainInterTitle);
//                    intent.putExtra("topicDes", item.getPrefeerContent());
//                    ActivityCompat.startActivity(context, intent, compat.toBundle());
//                }
//            });

            String prefeerPictrue = item.getPrefeerPictrues();
            String prefeerPictrues[] = null;
            if (prefeerPictrue != null) {
                prefeerPictrues = prefeerPictrue.split(",");
                postUrl = prefeerPictrues[0];

                if (prefeerPictrues.length == 1) {
                    oneLayout.setVisibility(View.VISIBLE);
                    nineLayout.setVisibility(View.GONE);
                    fourLayout.setVisibility(View.GONE);
                    final String onePic = prefeerPictrues[0];
                    Glide.with(context)
                            .load(onePic)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(oneLayout);
                    oneLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> pic = new ArrayList<String>();
                            pic.add(onePic);
                            showAllSubject(pic, 0);
                        }
                    });
                } else if (prefeerPictrues.length == 4) {
                    oneLayout.setVisibility(View.GONE);
                    nineLayout.setVisibility(View.GONE);
                    fourLayout.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(prefeerPictrues[0])
                            .dontAnimate()
                            .placeholder(R.drawable.default_more)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override(ScreenUtil.dp2px(context, 106), ScreenUtil.dp2px(context, 106))
                            .into(topLeftImg);
                    Glide.with(context)
                            .load(prefeerPictrues[1])
                            .placeholder(R.drawable.default_more)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override(ScreenUtil.dp2px(context, 106), ScreenUtil.dp2px(context, 106))
                            .dontAnimate()
                            .into(topRightImg);
                    Glide.with(context)
                            .load(prefeerPictrues[2])
                            .placeholder(R.drawable.default_more)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override(ScreenUtil.dp2px(context, 106), ScreenUtil.dp2px(context, 106))
                            .dontAnimate()
                            .into(bottomLeftImg);
                    Glide.with(context)
                            .load(prefeerPictrues[3])
                            .placeholder(R.drawable.default_more)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override(ScreenUtil.dp2px(context, 106), ScreenUtil.dp2px(context, 106))
                            .dontAnimate()
                            .into(bottomRightImg);
                } else {
                    oneLayout.setVisibility(View.GONE);
                    nineLayout.setVisibility(View.VISIBLE);
                    fourLayout.setVisibility(View.GONE);

                    for (int i = 0; i < prefeerPictrues.length; i++) {
                        if (i < 9) {
                            ImageView itemNine = nineArray.get(i);
                            itemNine.setVisibility(View.VISIBLE);
                            Glide.with(context)
                                    .load(prefeerPictrues[i])
                                    .dontAnimate()
                                    .placeholder(R.drawable.default_more)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .override(ScreenUtil.dp2px(context, 106), ScreenUtil.dp2px(context, 106))
                                    .into(itemNine);
                        }
                    }
                }
            } else {
                postUrl = "";
            }

            if (item.getMediaType()==3&&item.getMediaUrl()!=null&&item.getMediaUrl().length()>0){
                mediaLayout.setVisibility(View.VISIBLE);
                oneLayout.setVisibility(View.GONE);
                nineLayout.setVisibility(View.GONE);
                fourLayout.setVisibility(View.GONE);
                if (prefeerPictrues.length>0){
//                    Glide.with(context)
//                            .load(prefeerPictrues[0])
//                            .dontAnimate()
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(item_video_iv_cover);
                    playerView.setUp(
                            item.getMediaUrl().trim(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                            "");
                    Picasso.with(context)
                            .load(prefeerPictrues[0])
                            .into(playerView.thumbImageView);
                } else {

                }
            } else {
                mediaLayout.setVisibility(View.GONE);
            }
            final String finalPostUrl = postUrl;
            likePeopleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do...评论
                    if (item != null && item.getInformationId() > 0) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelCommentActivity.class);
                        intent.putExtra("prefeerContent", item.getPrefeerContent());
                        intent.putExtra("postUrl", finalPostUrl);
                        intent.putExtra("type", item.getType());
                        intent.putExtra("sid", item.getInformationId());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });
            ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do...评论
                    if (item != null && item.getInformationId() > 0) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelCommentActivity.class);
                        intent.putExtra("postUrl", finalPostUrl);
                        intent.putExtra("type", item.getType());
                        intent.putExtra("prefeerContent", item.getPrefeerContent());
                        intent.putExtra("sid", item.getInformationId());
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });
            ((ItemChannelHolder) viewHolder).goAllCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null && item.getInformationId() > 0) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                context, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(context, MChannelCommentActivity.class);
                        intent.putExtra("sid", item.getInformationId());
                        intent.putExtra("type", item.getType());
                        intent.putExtra("prefeerContent", item.getPrefeerContent());
                        if (finalPostUrl != null)
                            intent.putExtra("postUrl", finalPostUrl);
                        ActivityCompat.startActivity(context, intent, compat.toBundle());
                    }
                }
            });

            MainChannelItem.ChainMerchants chainMerchants = null;
            if (item.getChainMerchants() != null && item.getChainMerchants().size() > 0) {
                addresstext.setVisibility(View.VISIBLE);
                chainMerchants = item.getChainMerchants().get(0);
                StringBuilder addressBuilder = new StringBuilder();
                if (chainMerchants.getCityCode() != null) {
                    addressBuilder.append("  ");
                    if (MainApp.cityMap != null) {
                        addressBuilder.append(MainApp.cityMap.get(chainMerchants.getCityCode()));
                    }
                    if (chainMerchants.getName() != null) {
                        addressBuilder.append(".");
                        addressBuilder.append(chainMerchants.getName());
                    }
                    addresstext.setText(addressBuilder.toString());
                } else {
                    addresstext.setText("");
                }
            } else {
                addresstext.setVisibility(View.GONE);
            }

            if (item.getCommentCount() == 0) {
                goAllCommentBtn.setVisibility(View.GONE);
            } else {
                goAllCommentBtn.setVisibility(View.VISIBLE);
                goAllCommentBtn.setText("查看全部" + item.getCommentCount() + "条评论");
            }
            if (item.getComments() != null && item.getComments().size() > 0) {
                if (item.getComments().size() >= 1) {
                    Comments comentItem = item.getComments().get(0);
                    commentText1.setVisibility(View.VISIBLE);
                    setupCommentText(commentText1, comentItem, item, curPos);
                }
                if (item.getComments().size() >= 2) {
                    Comments comentItem = item.getComments().get(1);
                    commentText2.setVisibility(View.VISIBLE);
                    setupCommentText(commentText2, comentItem, item, curPos);
                }
                if (item.getComments().size() >= 3) {
                    Comments comentItem = item.getComments().get(2);
                    commentText3.setVisibility(View.VISIBLE);
                    setupCommentText(commentText3, comentItem, item, curPos);
                }
            }
        } else {


        }
    }

    private void showDeletePop(final int curPos, final MainChannelItem item) {
        String[] others;
        final boolean isSelf = MoreUser.getInstance().getUid().equals(item.getUid());
        if (isSelf) {
            others = new String[]{"删除", "举报"};
        } else {
            others = new String[]{"举报"};
        }
        photoAlertView = new AlertView(null, null, "取消", null, others,
                context, AlertView.Style.ActionSheet, new com.moreclub.common.ui.view.alertview.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (o == photoAlertView) {
                    if (position == 0) {
                        if (isSelf) {
                            deleteMyUgc(curPos, item);
                        } else {
                            Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                        }
                    } else if (position == 1) {
                        Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (!photoAlertView.isShowing() && !((MainActivity) context).isFinishing())
            photoAlertView.show();
    }

    private void deleteMyUgc(final int curPos, MainChannelItem item) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    mainChannels.remove(curPos);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {

            }
        };
        Long id = Long.valueOf(item.getInformationId());
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.ucenter.api.ApiInterface.ApiFactory.createApi(token).onDeleteMyUgcList(id).enqueue(callback);
    }


    private View.OnClickListener channelPictureClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewid = v.getId();
            int curp = (int) v.getTag(viewid);
            int picIndex = 0;
            if (viewid == R.id.top_left_img) {
                picIndex = 0;
            } else if (viewid == R.id.top_right_img) {
                picIndex = 1;
            } else if (viewid == R.id.bottom_left_img) {
                picIndex = 2;
            } else if (viewid == R.id.bottom_right_img) {
                picIndex = 3;
            } else if (viewid == R.id.nine_layout1) {
                picIndex = 0;
            } else if (viewid == R.id.nine_layout2) {
                picIndex = 1;
            } else if (viewid == R.id.nine_layout3) {
                picIndex = 2;
            } else if (viewid == R.id.nine_layout4) {
                picIndex = 3;
            } else if (viewid == R.id.nine_layout5) {
                picIndex = 4;
            } else if (viewid == R.id.nine_layout6) {
                picIndex = 5;
            } else if (viewid == R.id.nine_layout7) {
                picIndex = 6;
            } else if (viewid == R.id.nine_layout8) {
                picIndex = 7;
            } else if (viewid == R.id.nine_layout9) {
                picIndex = 8;
            }
            MainChannelItem positionItem = mainChannels.get(curp);

            String prefeerPictrue = positionItem.getPrefeerPictrues();
            String prefeerPictrues[] = null;
            if (prefeerPictrue != null) {
                prefeerPictrues = prefeerPictrue.split(",");
            }
            if (prefeerPictrues != null && prefeerPictrues.length > 0) {
                ArrayList<String> picArray = new ArrayList<>();
                for (int i = 0; i < prefeerPictrues.length; i++) {
                    picArray.add(prefeerPictrues[i]);
                }
                showAllSubject(picArray, picIndex);
            }
            Log.d("dddd", "" + picIndex);
        }
    };

    private int postID;
    private String fromNickName;
    private long toUid;
    private int commentRefreshPosition;
    private String myCommentContent;

    /**
     * channel评论
     */
    public void showCommentPopuWindow(int post, String nick, long tou, int p) {
        postID = post;
        fromNickName = nick;
        toUid = tou;
        commentRefreshPosition = p;
        if (null == mCommentWindow) {
            commentParentView = LayoutInflater.from(context).inflate(
                    R.layout.main_activity_comment_popu, null, false);
            commentView = (EditText) commentParentView.findViewById(R.id.et_write_comment);
            commentView.setOnKeyListener(onKeyListener);
            commentView.addTextChangedListener(textChangeListener);
            View noEditView = commentParentView.findViewById(R.id.no_edit_view);
            noEditView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentWindow.dismiss();
                    mCommentWindow = null;
                    commentParentView = null;
                    commentView = null;
                }
            });

            commentView.setVisibility(View.VISIBLE);
            if (toUid > 0 && toUid != MoreUser.getInstance().getUid()) {
                commentView.setHint("回复 " + fromNickName + "：");
            } else {
                commentView.setHint("留个言评论下吧……");
            }
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            commentView.setFocusable(true);
            commentView.setFocusableInTouchMode(true);
            commentView.requestFocus();

            //设置弹出部分和面积大小
            mCommentWindow = new PopupWindow(commentParentView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

            mCommentWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mCommentWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            //设置动画弹出效果
            mCommentWindow.setAnimationStyle(R.style.PopupAnimation);

            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x66000000);
            mCommentWindow.setBackgroundDrawable(dw);

            mCommentWindow.setTouchable(true);
            mCommentWindow.setFocusable(true);

        }
        int[] pos = new int[2];
        mCommentWindow.showAtLocation(((Activity) context).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);

    }


    /**
     * 图片预览popupWindow
     *
     * @param pics
     * @param clickpos
     */
    public void showAllSubject(ArrayList<String> pics, int clickpos) {
        if (null == mPopupWindow) {
            popupParentView = LayoutInflater.from(context).inflate(
                    R.layout.merchant_details_scene_popuwindow, null);
            popupParentView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            int width = ScreenUtil.getScreenWidth(context);
            int height = ScreenUtil.getScreenHeight(context);

            //设置弹出部分和面积大小
            mPopupWindow = new PopupWindow(popupParentView, width, height, true);
            //设置动画弹出效果
            mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mPopupWindow.setBackgroundDrawable(dw);

//            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);

        }
        popupWindowSetupView(popupParentView, pics, clickpos);
        int[] pos = new int[2];


        mPopupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(),
                Gravity.RIGHT | Gravity.TOP,
                pos[0], pos[1]);
    }

    void popupWindowSetupView(View view, ArrayList<String> pics, int clickPos) {

        sceneViewPager = (ViewPagerFixed) view.findViewById(R.id.sceneViewPager);
        MerchantScenePagerAdapter adapter = new MerchantScenePagerAdapter(context, pics);

        sceneViewPager.setAdapter(adapter);

        adapter.setOnItemClickListener(new MerchantScenePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }
        });

        sceneViewPager.setCurrentItem(clickPos);

        sceneViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onSendCommentFailure(String msg) {

    }

    @Override
    public void onSendCommentResponse(RespDto response) {
        if (mCommentWindow != null && mCommentWindow.isShowing()) {
            mCommentWindow.dismiss();
            mCommentWindow = null;
        }
        if (myCommentContent != null && myCommentContent.length() > 0) {
            MainChannelItem clickItem = mainChannels.get(commentRefreshPosition);
            ArrayList<Comments> comArray =
                    clickItem.getComments();
            if (comArray == null) {
                comArray = new ArrayList<Comments>();
            }
            Comments myComments = new Comments();
            myComments.setToUserId(mComment.getToUserId());
            myComments.setCid(Integer.parseInt(response.getData().toString()));
            myComments.setToNickName(mComment.getToNickName());
            myComments.setToUserThumb(mComment.getToUserThumb());
            myComments.setContent(myCommentContent);
            myComments.setFromNickname(MoreUser.getInstance().getNickname());
            comArray.add(0, myComments);
            clickItem.setComments(comArray);
            clickItem.setCommentCount(clickItem.getCommentCount() <= 0 ? 1 : clickItem.getCommentCount() + 1);
            mainChannels.set(commentRefreshPosition, clickItem);
            notifyDataSetChanged();
        }
    }


    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!TextUtils.isEmpty(commentView.getText().toString().trim())) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    myCommentContent = commentView.getText().toString().trim();
                    sendComment(commentView.getText().toString().trim());
                    commentView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    inputMethodManager.showSoftInput(commentView, InputMethodManager.RESULT_SHOWN);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                    commentView.setFocusable(true);
                    commentView.setFocusableInTouchMode(true);
                    commentView.requestFocus();
                }
                commentView.setText("");
                return true;
            }
            return false;
        }
    };

    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null && s.toString().split("：") != null && s.toString().split("：").length > 1) {
                commentView.setTextColor(Color.parseColor("#333333"));
            } else if (s != null && !s.toString().contains("：")) {
                commentView.setTextColor(Color.parseColor("#333333"));
            } else {
                commentView.setTextColor(Color.parseColor("#999999"));
            }
            commentView.setSelection(commentView.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void sendComment(String comment) {

        //Todo 发评论
        HeadlineSendComment mSendComment = new HeadlineSendComment();
        String version = com.moreclub.moreapp.ucenter.constant.Constants.APP_version;
        mSendComment.setAppVersion(version);
        mSendComment.setTimestamp((int) (System.currentTimeMillis() / 1000));
        mSendComment.setUid(MoreUser.getInstance().getUid());
        String hint = commentView.getHint().toString();
        if (TextUtils.equals(hint, "留个言评论下吧……")) {
            mSendComment.setContent(comment);
            mSendComment.setPostId(postID);
            mSendComment.setToNickName("");
        } else {
            try {
                mSendComment.setContent(comment);
                mSendComment.setPostId(postID);
                mSendComment.setToNickName(fromNickName);
                mSendComment.setToUserId(toUid);
            } catch (Exception e) {
                mSendComment.setContent(comment);
                mSendComment.setPostId(postID);
            }
        }
//        ((MainChannelAttentionDataBinder) mPresenter).onSendComment(mSendComment);

        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (response == null || response.body() == null) return;
                if (mCommentWindow != null && mCommentWindow.isShowing()) {
                    mCommentWindow.dismiss();
                    mCommentWindow = null;
                }
                if (myCommentContent != null && myCommentContent.length() > 0) {
                    MainChannelItem clickItem = mainChannels.get(commentRefreshPosition);
                    ArrayList<Comments> comArray =
                            clickItem.getComments();
                    if (comArray == null) {
                        comArray = new ArrayList<Comments>();
                    }
                    Comments myComments = new Comments();
                    myComments.setToUserId(mComment.getToUserId());
                    myComments.setCid(Integer.parseInt(response.body().getData().toString()));
                    myComments.setToNickName(mComment.getToNickName());
                    myComments.setToUserThumb(mComment.getToUserThumb());
                    myComments.setContent(myCommentContent);
                    myComments.setFromNickname(MoreUser.getInstance().getNickname());
                    comArray.add(0, myComments);
                    clickItem.setComments(comArray);
                    clickItem.setCommentCount(clickItem.getCommentCount() <= 0 ? 1 : clickItem.getCommentCount() + 1);
                    mainChannels.set(commentRefreshPosition, clickItem);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {

            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi(token).onSendComment(mSendComment).enqueue(callback);

    }

    private void setupCommentText(TextView commentT, final Comments comentItem, final MainChannelItem item, final int position) {
        final StringBuilder commentContent = new StringBuilder();
        if (comentItem != null && comentItem.getFromNickname() != null) {
            commentContent.append(comentItem.getFromNickname());
            if (comentItem.getToNickName() != null
                    && !comentItem.getToNickName().equals(MoreUser.getInstance().getNickname())) {
                commentContent.append("  @");
                commentContent.append(comentItem.getToNickName());
            }
            commentContent.append("  ");
            commentContent.append(comentItem.getContent());
        } else {
            commentContent.append(comentItem.getContent());
        }

        SpannableStringBuilder spannable = new SpannableStringBuilder(commentContent.toString());
        int startIndex = 0;
        int endIndex = 0;
        if (comentItem.getFromNickname() != null) {
            endIndex = comentItem.getFromNickname().length() + 2;
        }
        int color = R.color.mainItemTagTitle;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 个人主页
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    return;
                }
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        context, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(context, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", "" + comentItem.getUserId());
                ActivityCompat.startActivity(context, intent_merchant, compat_merchant.toBundle());
            }
        };
        try {
            spannable.setSpan(new Clickable(color, listener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        try {
            spannable.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        int toStartIndex = endIndex;
        int toEndIndex = 0;
        View.OnClickListener listner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 回复某人
                if (MoreUser.getInstance().getUid() == 0) {
                    AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                    return;
                }
                showCommentPopuWindow(item.getInformationId(), comentItem.getFromNickname(), comentItem.getUserId(), position);
                mComment = new Comments();
                mComment.setToNickName(comentItem.getFromNickname());
                mComment.setToUserId(comentItem.getUserId());
                mComment.setToUserThumb(comentItem.getFromUserThumb());
            }
        };
        try {
            spannable.setSpan(new Clickable(R.color.searchEntryTagText, listner), endIndex, commentContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        if (comentItem.getToNickName() != null) {
            toStartIndex = endIndex;
            toEndIndex = 0;
            if (comentItem.getToNickName() != null && !comentItem.getToNickName().equals(MoreUser.getInstance().getNickname())) {
                toEndIndex = toStartIndex + comentItem.getToNickName().length() + 2;
            }
            int toColor = R.color.shit_yellow;
            View.OnClickListener reCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 回复某人
                    if (MoreUser.getInstance().getUid() == 0) {
                        AppUtils.pushLeftActivity(context, UseLoginActivity.class);
                        return;
                    }
                    showCommentPopuWindow(item.getInformationId(), comentItem.getFromNickname(), comentItem.getUserId(), position);
                    mComment = new Comments();
                    mComment.setToNickName(comentItem.getFromNickname());
                    mComment.setToUserId(comentItem.getUserId());
                    mComment.setToUserThumb(comentItem.getFromUserThumb());
                }
            };
            try {
                spannable.setSpan(new Clickable(toColor, reCommentListener), toStartIndex, toEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {

            }
        }
        commentT.setText(spannable);
        commentT.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void cancelBackground(String tag, View backGround, View redPoint) {
        backGround.setBackgroundResource(R.drawable.circle_activity_normal);
        redPoint.setVisibility(View.GONE);
        today = formatTime(System.currentTimeMillis());
        String cityCode = PrefsUtils.getString(backGround.getContext(), Constants.KEY_CITY_CODE, "cd");
        PrefsUtils.setString(backGround.getContext(), Constants.KEY_CANCEL_REDPOINT + tag + cityCode, cityCode + today);
    }

    private String formatTime(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Date date = new Date(millis);
        String today = simpleDateFormat.format(date);
        return today;
    }

    private void initActivityData(String tag, View background, View view, View redPoint) {
        String cityCode = PrefsUtils.getString(background.getContext(), Constants.KEY_CITY_CODE, "cd");
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(R.drawable.frame_eyes);
            AnimationDrawable animationDrawable = (AnimationDrawable) ((ImageView) view).getDrawable();
            animationDrawable.start();
            String save = PrefsUtils.getString(background.getContext(), Constants.KEY_CANCEL_REDPOINT + tag + cityCode, "");
            String now = cityCode + today;
            if (headlineCounts != null && headlineCounts.size() > 0
                    && !TextUtils.equals(save
                    , now)) {
                isNewHeadline = true;
                int drawable = R.drawable.circle_activity_select;
                background.setBackgroundResource(drawable);
                redPoint.setVisibility(View.VISIBLE);
            } else {
                int drawable = R.drawable.circle_activity_normal;
                background.setBackgroundResource(drawable);
                redPoint.setVisibility(View.GONE);
            }
        }
        if (view instanceof TextView) {
            if (merchantCounts != null && merchantCounts.size() > 0)
                ((TextView) view).setText(merchantCounts.get(0).getMerchantCount() + "");
            String save = PrefsUtils.getString(background.getContext(), Constants.KEY_CANCEL_REDPOINT + tag + cityCode, "");
            String now = cityCode + today;
            if (merchantCounts != null && merchantCounts.size() > 0 &&
                    merchantCounts.get(0) != null && merchantCounts.get(0).getMerchantCount() > 0
                    && !TextUtils.equals(save
                    , now)) {
                isNewStore = true;
                int drawable = R.drawable.circle_activity_select;
                background.setBackgroundResource(drawable);
                redPoint.setVisibility(View.VISIBLE);
            } else {
                int drawable = R.drawable.circle_activity_normal;
                background.setBackgroundResource(drawable);
                redPoint.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.ITEM_TYPE_ACTIVITY.ordinal();
        } else if (position == 1) {
            return ITEM_TYPE.ITEM_TYPE_ONE.ordinal();
        } else if (position == 2) {
            return ITEM_TYPE.ITEM_TYPE_BIZAREA.ordinal();
        } else if (position <= dicts.size() + 2) {
            return ITEM_TYPE.ITEM_TYPE_TAG.ordinal();
        } else if (position == dicts.size() + 3) {
            return ITEM_TYPE.ITEM_TYPE_ACTIVITIES.ordinal();
        } else if (position == dicts.size() + 4) {
            return ITEM_TYPE.ITEM_TYPE_PKG.ordinal();
        } else if (position == dicts.size() + 5) {
            return ITEM_TYPE.ITEM_TYPE_BAR.ordinal();
        } else if (position == dicts.size() + 6) {
            return ITEM_TYPE.ITEM_TYPE_CHANNELTITLE.ordinal();
        } else if (position >= dicts.size() + 7) {
            return ITEM_TYPE.ITEM_TYPE_CHANNEL.ordinal();
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        if (dicts == null) {
            dicts = new ArrayList<>();
        }
        if (mainChannels == null) {
            mainChannels = new ArrayList<>();
        }
        return 7 + dicts.size() + mainChannels.size();
    }

    static class ItemOneViewHolder extends RecyclerView.ViewHolder {
        public ItemOneViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ItemTwoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public ItemTwoViewHolder(View itemView) {
            super(itemView);
            recyclerView = ButterKnife.findById(itemView, R.id.main_bizare_item_rv);
        }
    }

    class ItemPackageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_packages_rv)
        RecyclerView recyclerView;
        @BindView(R.id.main_packages_goto_all_lay)
        LinearLayout imageButton;

        @OnClick({R.id.main_pkg_header_item})
        void onClickAllPkgs() {
            AppUtils.pushLeftActivity(context, PackageSuperMainListActivity.class);
        }

        public ItemPackageViewHolder(final Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemTagViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_bartag_item_rv)
        RecyclerView recyclerView;
        @BindView(R.id.main_tag_title)
        TextView textView;

        public ItemTagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemTitleViewHolder extends RecyclerView.ViewHolder {
        public ItemTitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ItemChannelTitleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_channel_goto_all)
        ImageView channelGotoAll;

        public ItemChannelTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class ItemChannelHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_img)
        ImageView headerImage;
        @BindView(R.id.header_name)
        TextView headerName;
        @BindView(R.id.header_tag)
        ImageView headerTag;
        @BindView(R.id.header_time)
        TextView headerTime;
        @BindView(R.id.attention_btn)
        TextView attentionBtn;
//        @BindView(R.id.channel_des)
//        ExpandableTextView channelDes;

        @BindView(R.id.channel_des)
        TextView channelDes;
        //////////////////////////////////////////////////////////
        @BindView(R.id.picgridView)
        RelativeLayout picgridView;
        @BindView(R.id.nine_layout)
        RelativeLayout nineLayout;
        @BindView(R.id.one_layout)
        ImageView oneLayout;
        @BindView(R.id.media_layout)
        RelativeLayout mediaLayout;
        @BindView(R.id.videoplayer)
        JCVideoPlayerStandard videoplayer;
        @BindView(R.id.four_layout)
        RelativeLayout fourLayout;
        @BindView(R.id.top_left_img)
        ImageView topLeftImg;
        @BindView(R.id.top_right_img)
        ImageView topRightImg;
        @BindView(R.id.bottom_left_img)
        ImageView bottomLeftImg;
        @BindView(R.id.bottom_right_img)
        ImageView bottomRightImg;
        @BindView(R.id.nine_layout1)
        ImageView nine_layout1;
        @BindView(R.id.nine_layout2)
        ImageView nine_layout2;
        @BindView(R.id.nine_layout3)
        ImageView nine_layout3;
        @BindView(R.id.nine_layout4)
        ImageView nine_layout4;
        @BindView(R.id.nine_layout5)
        ImageView nine_layout5;
        @BindView(R.id.nine_layout6)
        ImageView nine_layout6;
        @BindView(R.id.nine_layout7)
        ImageView nine_layout7;
        @BindView(R.id.nine_layout8)
        ImageView nine_layout8;
        @BindView(R.id.nine_layout9)
        ImageView nine_layout9;
        ////////////////////////////////////////
        @BindView(R.id.comment_text_1)
        TextView comment_text_1;
        @BindView(R.id.comment_text_2)
        TextView comment_text_2;
        @BindView(R.id.comment_text_3)
        TextView comment_text_3;
        //////////////////////////////////////////////////
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.comment_btn)
        LinearLayout commentBtn;
        @BindView(R.id.comment_list_layout)
        LinearLayout ll_comment;
        @BindView(R.id.comment_count)
        TextView commentCount;
        @BindView(R.id.like_btn)
        LinearLayout likeBtn;
        @BindView(R.id.like_count)
        TextView likeCount;
        @BindView(R.id.like)
        ImageView like;
        @BindView(R.id.like_people_layout)
        LinearLayout likePeopleLayout;
        @BindView(R.id.go_all_comment_btn)
        TextView goAllCommentBtn;
        @BindView(R.id.tv_all_content)
        TextView tvAllContent;
        @BindView(R.id.iv_menu)
        ImageView ivMenu;

        public ItemChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemBarViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_bars_rv)
        RecyclerView recyclerView;

        @BindView(R.id.main_bars_goto_all)
        ImageView barsGotoAll;

        public ItemBarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemActivityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_headline)
        View viewHeadline;
        @BindView(R.id.red_point_headline)
        View redPointHeadline;
        @BindView(R.id.headline_layout)
        RelativeLayout headlineLayout;
        @BindView(R.id.view_new_store)
        View viewNewStore;
        @BindView(R.id.red_point_new_store)
        View redPointNewStore;
        @BindView(R.id.new_store_layout)
        RelativeLayout newStoreLayout;
        @BindView(R.id.view_activity)
        View viewActivity;
        @BindView(R.id.red_point_activity)
        View redPointActivity;
        @BindView(R.id.activity_layout)
        RelativeLayout activityLayout;
        @BindView(R.id.iv_eye1)
        ImageView ivEye1;
        @BindView(R.id.tv_new_store)
        TextView tvNewStore;
        @BindView(R.id.ll_root)
        FrameLayout llRoot;

        public ItemActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemActivitiesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_bartag_divider)
        LinearLayout mainBartagDivider;
        @BindView(R.id.title_zh)
        TextView titleZh;
        @BindView(R.id.main_packages_goto_all)
        ImageView mainPackagesGotoAll;
        @BindView(R.id.main_pkg_header_item)
        RelativeLayout mainPkgHeaderItem;
        @BindView(R.id.title_en)
        TextView titleEn;
        @BindView(R.id.main_packages_goto_all_lay)
        LinearLayout mainPackagesGotoAllLay;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;
        @BindView(R.id.main_packages_rv)
        RecyclerView mainPackagesRv;

        public ItemActivitiesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class Clickable extends ClickableSpan {
        private View.OnClickListener mListener;
        private int color;

        public Clickable(int color, View.OnClickListener l) {
            mListener = l;
            this.color = color;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(MainApp.getContext().getResources().getColor(color));
        }

    }
}