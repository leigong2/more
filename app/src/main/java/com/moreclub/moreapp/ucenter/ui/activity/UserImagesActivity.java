package com.moreclub.moreapp.ucenter.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.ucenter.model.UserLoadImage;
import com.moreclub.moreapp.ucenter.model.UserSmallImage;
import com.moreclub.moreapp.ucenter.presenter.IUserImagesLoader;
import com.moreclub.moreapp.ucenter.presenter.UserImagesLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.RecyclerImagePhotoAdapter;
import com.moreclub.moreapp.ucenter.ui.adapter.UserImagesAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.FileSaveUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CUSTOM;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH;

public class UserImagesActivity extends BaseActivity implements IUserImagesLoader.LoaderUserImagesDataBinder {

    @BindView(R.id.nav_back)
    ImageButton navBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.rv_photos)
    XRecyclerView rvPhotos;
    @BindView(R.id.reviewPicLayout)
    FrameLayout reviewPicLayout;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightBtn;
    @BindView(R.id.nav_share_btn)
    ImageButton navShareBtn;
    @BindView(R.id.vp_preview)
    ViewPager vpPreview;
    @BindView(R.id.choice_all)
    TextView choiceAll;
    @BindView(R.id.select_count)
    TextView selectCount;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private ArrayList<String> imageBigUrls;
    private ArrayList<UserSmallImage> imageThumbUrls;
    private ArrayList<Long> ids;
    private int pn;
    private int ps = 32;
    private RecyclerImagePhotoAdapter adapter;
    private long uid;
    private SystemBarTintManager tintManager;
    private LinearLayout navLay;
    private String from;
    private String toUid;
    private UserImagesAdapter pagerAdapter;
    private List<View> views;
    private boolean deleteAll;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_images_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IUserImagesLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupView();
        loadData();
    }

    private void loadData() {
        if (TextUtils.isEmpty(toUid))
            toUid = "0";
        ((UserImagesLoader) mPresenter).onloadImages(Long.parseLong(toUid), pn, ps);
    }

    private void setupView() {
        toUid = getIntent().getStringExtra("toUid");
        from = "";
        if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
            choiceAll.setVisibility(View.GONE);
        } else {
            choiceAll.setVisibility(View.GONE);
        }
        activityTitle.setText("相册");
        navShareBtn.setImageResource(R.drawable.delete);
        imageBigUrls = new ArrayList<>();
        imageThumbUrls = new ArrayList<>();
        ids = new ArrayList<>();
        adapter = new RecyclerImagePhotoAdapter(UserImagesActivity.this
                , R.layout.simple_imageview, imageThumbUrls);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 4
                , GridLayoutManager.VERTICAL, false);
        adapter.setHasHeader(true);
        rvPhotos.setLayoutManager(lm);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLoadingMoreEnabled(true);
        rvPhotos.setPullRefreshEnabled(false);
        rvPhotos.setRefreshProgressStyle(ProgressStyle.SysProgress);
        rvPhotos.setArrowImageView(R.drawable.abc_icon_down_arrow);
        rvPhotos.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        initViewPager();
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                setupViewPager(position);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        rvPhotos.setLoadingListener(new com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pn = 0;
                loadData();
            }

            @Override
            public void onLoadMore() {
                pn++;
                loadData();
            }
        });
        navLay = (LinearLayout) findViewById(R.id.nav_layout);
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    private void initViewPager() {
        views = new ArrayList<>();
        for (int i = 0; i < imageBigUrls.size(); i++) {
            addPhotoView(i);
        }
        pagerAdapter = new UserImagesAdapter(views);
        vpPreview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectCount.setText((position + 1) + " / " + views.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpPreview.setAdapter(pagerAdapter);
    }

    private void addPhotoView(final int i) {
        final SubsamplingScaleImageView photoView = new SubsamplingScaleImageView(UserImagesActivity.this);
        ViewGroup.LayoutParams lp = new LinearLayoutCompat
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.setLayoutParams(lp);
        photoView.setMaxScale(15);
        photoView.setZoomEnabled(true);
        photoView.setMinimumScaleType(SCALE_TYPE_CUSTOM);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewPicLayout.setVisibility(View.GONE);
                selectCount.setVisibility(View.GONE);
                if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
                    choiceAll.setVisibility(View.GONE);
                }
                showTitleBar();
            }
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSaveDialog(i);
                return false;
            }
        });
        Glide.with(this)
                .load(imageBigUrls.get(i)).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                ImageSource imageSource = ImageSource.uri(Uri.fromFile(resource));
                int  sWidth = BitmapFactory.decodeFile(resource.getAbsolutePath()).getWidth();
                int sHeight = BitmapFactory.decodeFile(resource.getAbsolutePath()).getHeight();
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                //float centerX = SystemUtil.displaySize.x / 2;
                // imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(2.0F, new PointF(0, 0), 0));
                //imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(2.0F, new PointF(0, 0), 0));
                if (sHeight >= height
                        && sHeight / sWidth >=3) {
                    photoView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                    photoView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(2.0F, new PointF(0, 0), 0));
                }else {
                    photoView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                    photoView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                    photoView.setDoubleTapZoomStyle(ZOOM_FOCUS_CENTER_IMMEDIATE);
                }
            }});

        views.add(photoView);
    }

    private void setupViewPager(int position) {
        reviewPicLayout.setVisibility(View.VISIBLE);
        if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
            choiceAll.setVisibility(View.GONE);
        }
        selectCount.setVisibility(View.VISIBLE);
        if (MoreUser.getInstance().getUid().equals(uid))
            navShareBtn.setVisibility(View.VISIBLE);
        vpPreview.setCurrentItem(position);
        selectCount.setText((position + 1) + " / " + views.size());
        pagerAdapter.notifyDataSetChanged();
        hintTitleBar();
    }

    private void hintTitleBar() {
        navLay.setBackgroundColor(Color.parseColor("#00000000"));
        if (Build.VERSION.SDK_INT > 19)
            tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    private void showTitleBar() {
        navLay.setBackgroundColor(Color.parseColor("#000000"));
        if (Build.VERSION.SDK_INT > 19)
            tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    /**
     * zune:设置状态栏沉浸
     **/
    @TargetApi(19)
    private void setTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = -1;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            navLay.setTranslationY(statusBarHeight);
        }
    }

    private void setUnTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = -1;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            navLay.setTranslationY(statusBarHeight);
        }
    }

    @Override
    public void onloadImagesResponse(RespDto<List<UserLoadImage>> responce) {
        deleteAll = false;
        onLoadComplete(pn);
        if (responce == null || responce.getData() == null) {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
            return;
        }
        List<UserLoadImage> data = responce.getData();
        int size = views.size();
        for (int i = 0; i < data.size(); i++) {
            uid = data.get(i).getUid();
            String bigUrl = data.get(i).getUrl();
            String smallUrl = data.get(i).getThumbUrl();
            imageBigUrls.add(bigUrl);
            ids.add(data.get(i).getId());
            UserSmallImage image = new UserSmallImage();
            image.setThumb(smallUrl);
            imageThumbUrls.add(image);
            addPhotoView(i + size);
        }
        adapter.notifyDataSetChanged();
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onloadImagesFailure(String msg) {
        onLoadComplete(pn);
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onDeleteImagesResponse(RespDto<Boolean> responce) {
        if (responce == null) return;
        Boolean data = responce.getData();
        if (data != null && data) {
            if (!deleteAll)
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(from))
                from = getIntent().getStringExtra("from");
            showTitleBar();
            int currentItem = vpPreview.getCurrentItem();
            imageBigUrls.remove(currentItem);
            imageThumbUrls.remove(currentItem);
            ids.remove(currentItem);
            views.remove(currentItem);
            adapter.notifyDataSetChanged();
            if (imageBigUrls.size() == 0) {
                reviewPicLayout.setVisibility(View.GONE);
                if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
                    choiceAll.setVisibility(View.GONE);
                }
                selectCount.setVisibility(View.GONE);
                navShareBtn.setVisibility(View.GONE);
            }
            pagerAdapter.notifyDataSetChanged();
            if (currentItem > views.size() - 1) {
                selectCount.setText(currentItem + " / " + views.size());
            } else {
                selectCount.setText((currentItem + 1) + " / " + views.size());
            }
        }
    }

    @Override
    public void onDeleteImagesFailure(String msg) {

    }


    private void onLoadComplete(Integer index) {
        if (index == 0) {
            imageThumbUrls.clear();
            views.clear();
            imageBigUrls.clear();
            rvPhotos.refreshComplete();
        } else {
            rvPhotos.loadMoreComplete();
        }
    }

    @OnClick({R.id.nav_back, R.id.nav_share_btn, R.id.choice_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                if (reviewPicLayout.getVisibility() == View.VISIBLE) {
                    selectCount.setVisibility(View.GONE);
                    reviewPicLayout.setVisibility(View.GONE);
                    if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
                        choiceAll.setVisibility(View.GONE);
                    }
                    navShareBtn.setVisibility(View.GONE);
                    showTitleBar();
                } else {
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                }
                break;
            case R.id.nav_share_btn:
                if (deleteAll) {
                    if (ids != null)
                        for (int i = 0; i < ids.size(); i++) {
                            ((UserImagesLoader) mPresenter).onDeleteImages(MoreUser.getInstance().getAccess_token()
                                    , ids.get(i), uid);
                        }
                    pn = 0;
                    loadData();
                } else {
                    View dialogLayout = View.inflate(UserImagesActivity.this, R.layout.sign_inter_delete_dialog, null);
                    TextView title = (TextView) dialogLayout.findViewById(R.id.title);
                    title.setText("确定要删除这张图片吗？");
                    TextView delete = (TextView) dialogLayout.findViewById(R.id.delete);
                    TextView cancel = (TextView) dialogLayout.findViewById(R.id.cancel);
                    final AlertDialog dialog = new AlertDialog.Builder(this)
                            .setView(dialogLayout)
                            .show();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Todo 删除图片
                            deletePicture();
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.choice_all:
                for (int i = 0; i < imageThumbUrls.size(); i++) {
                    imageThumbUrls.get(i).setSelect(true);
                }
                adapter.notifyDataSetChanged();
                navShareBtn.setVisibility(View.VISIBLE);
                deleteAll = true;
                break;
        }
    }

    private void deletePicture() {
        if (ids != null && ids.size() > vpPreview.getCurrentItem())
            ((UserImagesLoader) mPresenter).onDeleteImages(MoreUser.getInstance().getAccess_token()
                    , ids.get(vpPreview.getCurrentItem()), uid);
    }

    private void showSaveDialog(final int pos) {
        View inflate = View.inflate(this, R.layout.sign_inter_delete_dialog, null);
        TextView title = (TextView) inflate.findViewById(R.id.title);
        TextView save = (TextView) inflate.findViewById(R.id.delete);
        TextView cancel = (TextView) inflate.findViewById(R.id.cancel);
        title.setText("是否保存图片?");
        save.setText("保存");
        cancel.setText("取消");
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(inflate)
                .show();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture(pos);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void savePicture(int position) {
        new FileSaveUtils().getBitMBitmap(imageBigUrls.get(position), new FileSaveUtils.OnSaveListener() {
            @Override
            public void getFilesResponse(Bitmap map) {
                FileSaveUtils.saveImageToGallery(UserImagesActivity.this, map, SAVE_REAL_PATH);
            }

            @Override
            public void getFilesFail() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (reviewPicLayout.getVisibility() == View.VISIBLE) {
            selectCount.setVisibility(View.GONE);
            reviewPicLayout.setVisibility(View.GONE);
            if ((MoreUser.getInstance().getUid() + "").equals(toUid)) {
                choiceAll.setVisibility(View.GONE);
            }
            navShareBtn.setVisibility(View.GONE);
            showTitleBar();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from.equals("UserDetailV2Activity") &&
                UpdateUser.getInstance().getAdapters() != null) {
            Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
            SignInteractSquareAdapter signInteractSquareAdapter = adapters.get("0");
            if (signInteractSquareAdapter != null) {
                signInteractSquareAdapter.upDatePopupWindow();
            } else {
                AppUtils.pushLeftActivity(this, UserDetailV2Activity.class);
            }
        }
    }
}
