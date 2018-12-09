package com.moreclub.moreapp.main.ui.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.main.utils.TestWakeLock;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.Build.VERSION_CODES.N;
import static java.lang.System.currentTimeMillis;

/**
 * Created by Administrator on 2017-07-20-0020.
 */

public class UpdateService extends Service {

    private String downLoadUrl;
    private String filePath;
    private int downloadId;
    private double progress;
    private NotificationManager manager;
    private Notification notification;
    private int totalBytes;
    private TestWakeLock twl;
    public static final String ONCLICK = "com.app.onclick";
    private boolean isContinue;
    private BaseDownloadTask downloadTask;
    private boolean isNetBad;
    private boolean downLoadComplete;
    private boolean shouldConcle;
    protected FileDownloadLargeFileListener downloadSampleListener = new FileDownloadLargeFileListener() {

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, long soFarBytes, long totalBytes) {
            Log.i("zune:", "connect");
        }

        @Override
        protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            Log.i("zune:", "pending = " + soFarBytes + "..." + totalBytes);
            if (notification == null)
                setNotificationProgrss();
        }

        @Override
        protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            Log.i("zune:", "pause = " + soFarBytes + ".." + totalBytes);
        }

        @Override
        protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, long soFarBytes) {
            Log.i("zune:", "retry = " + soFarBytes);
        }

        @Override
        protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            isNetBad = false;
            DecimalFormat df = new DecimalFormat("######0.00");
            progress = soFarBytes * 1.0 / UpdateService.this.totalBytes * 1.0;
            Log.i("zune:", "sofar = " + soFarBytes);
            Log.i("zune:", "totalBytes = " + totalBytes);
            Log.i("zune:", "progress = " + progress);
            Log.i("zune:", "getSofar = " + task.getLargeFileSoFarBytes());
            Log.i("zune:", "getTotal = " + task.getLargeFileTotalBytes());
            String formatProgress = df.format(progress * 100);
            // 更新进度
            if (notification == null && !shouldConcle)
                setNotificationProgrss();
            else if (notification != null && !downLoadComplete && !shouldConcle) {
                RemoteViews contentView = notification.contentView;
                contentView.setTextViewText(R.id.rate, formatProgress + "%");
                contentView.setProgressBar(R.id.progress, 100, (int) Double.parseDouble(formatProgress), false);
                contentView.setViewVisibility(R.id.tv_continue, View.GONE);
                IntentFilter filter_click = new IntentFilter();
                filter_click.addAction(ONCLICK);
                //注册广播
                registerReceiver(receiver_onclick, filter_click);
                Intent intent_click = new Intent(ONCLICK);
                PendingIntent continue_click = PendingIntent.getBroadcast(UpdateService.this, 0, intent_click, 0);
                contentView.setOnClickPendingIntent(R.id.tv_continue, continue_click);

                Intent intent_close_click = new Intent(ONCLICK);
                PendingIntent close_click = PendingIntent.getBroadcast(UpdateService.this, 1, intent_close_click, 0);
                contentView.setOnClickPendingIntent(R.id.close, close_click);
                startForeground(downloadId, notification);
            } else if (notification != null) {
                Log.i("zune:", "我要取消");
                FileDownloader.getImpl().pause(downloadSampleListener);
                stopSelf();
                RemoteViews contentView = notification.contentView;
                contentView.setTextViewText(R.id.fileName, "More" + Constants.APP_version + ".apk");
                contentView.setTextViewText(R.id.rate, 100 + "%");
                contentView.setProgressBar(R.id.progress, 100, 100, false);
                manager.cancel(downloadId);
            }
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) throws Throwable {
            /**zune:需要重新下载的时候传入true**/
            task.setForceReDownload(false);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            Log.i("zune:", "completed = " + task.getLargeFileTotalBytes());
            // 下载完成
//            showInstallDialog();
            installApk(UpdateService.this, new File(filePath));
            downLoadComplete = true;
            stopSelf();
            if (notification != null) {
                RemoteViews contentView = notification.contentView;
                contentView.setTextViewText(R.id.fileName, "More" + Constants.APP_version + ".apk");
                contentView.setTextViewText(R.id.rate, 100 + "%");
                contentView.setProgressBar(R.id.progress, 100, 100, false);
                manager.cancel(downloadId);
            }
            Toast.makeText(UpdateService.this, "More" + Constants.APP_version + ".apk已为您下载完毕", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void warn(BaseDownloadTask task) {
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            Log.i("zune:", "e = " + e.getMessage());
            if (notification != null) {
                isNetBad = true;
                RemoteViews contentView = notification.contentView;
                contentView.setTextViewText(R.id.fileName, "More" + Constants.APP_version + ".apk");
                contentView.setTextViewText(R.id.tv_continue, "重试");
                contentView.setViewVisibility(R.id.tv_continue, View.VISIBLE);
                isContinue = true;
                IntentFilter filter_click = new IntentFilter();
                filter_click.addAction(ONCLICK);
                //注册广播
                registerReceiver(receiver_onclick, filter_click);
                Intent intent_click = new Intent(ONCLICK);
                PendingIntent continue_click = PendingIntent.getBroadcast(UpdateService.this, 0, intent_click, 0);
                contentView.setOnClickPendingIntent(R.id.tv_continue, continue_click);
                startForeground(downloadId, notification);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zune:", "onCreat");
    }

    private void initDownLoad() {
        if (Build.VERSION.SDK_INT <= 20) {
            filePath = (new File(getDir("download", MODE_WORLD_READABLE | MODE_WORLD_READABLE), "More" + Constants.APP_version + ".apk")).getPath();
//            String[] command = {"/system/bin/sh","-c","chmod 777",filePath};
            String[] command = {"chmod", "777", filePath};
            ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("zune:", "filePath = " + filePath);
            try {
                openFileOutput("More" + Constants.APP_version + ".apk", Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
                Log.i("zune:", "设置了权限");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            filePath = (new File(getExternalCacheDir(), "More" + Constants.APP_version + ".apk")).getPath();
        }
        downloadTask = createDownloadTask(1);
        downloadId = downloadTask.start();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zune:", "onStart");
        downLoadUrl = intent.getStringExtra("downloadUrl");
        testWakeLock();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("zune:", "onTrimMem, level = " + level);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        twl.releaseWakeLock();
//        if (receiver_onclick != null)
//            unregisterReceiver(receiver_onclick);
        Log.i("zune:", "serviceDestroy");
    }

    private void installApk(Service context, File apkPath) {
        Log.i("zune:", "file name = " + apkPath.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= N) { // 7.0+以上版本
            String authority = getApplicationInfo().packageName + ".provider";
            Uri apkUri = FileProvider.getUriForFile(context, authority, apkPath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkPath), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    private BaseDownloadTask createDownloadTask(final int position) {
        String url = downLoadUrl;
        Log.i("zune:", "url = " + url);
        try {
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            totalBytes = conn.getContentLength();
            Log.i("zune:", "totleBytes = " + totalBytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("zune:", "e = " + e);
        }
        boolean isDir = false;
        final String path = filePath;
        Log.i("zune:", "secucess = task");
        return FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setAutoRetryTimes(3)
                .setSyncCallback(true)
                .setListener(downloadSampleListener);
    }

    private String getTotalSize(int total) {
        if (total > 1024) {
            return total / 1024 + "K";
        } else if (total > 1024 * 1024) {
            return total / 1024 / 1024 + "M";
        }
        return total + "B";
    }

    private void showInstallDialog() {
        Intent intent = new Intent(this, SuperMainActivity.class);
        intent.putExtra("INSTALL_NOW", true);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    protected void setNotificationProgrss() {
        Intent intent = new Intent(this, SuperMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new Notification(R.mipmap.ic_launcher, "正在下载", currentTimeMillis());
        notification.flags |= Notification.FLAG_NO_CLEAR;
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
        if (Build.VERSION.SDK_INT<N) {
            contentView.setInt(R.id.fileName, "setTextColor", isDarkNotificationTheme(UpdateService.this) == true ? Color.WHITE : Color.BLACK);
            contentView.setInt(R.id.tv_continue, "setTextColor", isDarkNotificationTheme(UpdateService.this) == true ? Color.WHITE : Color.BLACK);
            contentView.setTextViewText(R.id.fileName, "More" + Constants.APP_version + ".apk");
        }
        // 指定个性化视图
        if (notification != null) {
            notification.contentView = contentView;
            notification.contentIntent = pendingIntent;
            startForeground(downloadId, notification);
        }
        manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
    }

    private void testWakeLock() {
        new Thread() {
            @Override
            public void run() {
                Log.e("zune", "ready to acquire wakelock!");
                twl = new TestWakeLock(UpdateService.this);
                twl.acquireWakeLock();
                initDownLoad();
            }
        }.start();
    }

    public BroadcastReceiver receiver_onclick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ONCLICK)) {
                if (isNetBad) {
                    isContinue = !isContinue;
                    if (isContinue) {
                        FileDownloader.getImpl().pause(downloadId);
                        if (notification != null) {
                            RemoteViews contentView = notification.contentView;
                            contentView.setTextViewText(R.id.tv_continue, "继续");
                            startForeground(downloadId, notification);
                        }
                    } else {
                        new Thread() {
                            @Override
                            public void run() {
                                downloadId = createDownloadTask(1).start();
                            }
                        }.start();
                        int soFarBytes = downloadTask.getSoFarBytes();
                        Log.i("zune:", "now_sofarBytes = " + soFarBytes);
                        if (notification != null) {
                            RemoteViews contentView = notification.contentView;
                            contentView.setTextViewText(R.id.tv_continue, "暂停");
                            startForeground(downloadId, notification);
                        }
                    }
                } else if (notification != null) {
                    Log.i("zune:", "取消");
                    shouldConcle = true;
                }
            }
        }
    };

    public static boolean isDarkNotificationTheme(Context context) {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context));
    }

    /**
     * 获取通知栏颜色
     *
     * @param context
     * @return
     */
    public static int getNotificationColor(Context context) {
        if (Build.VERSION.SDK_INT < N) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Notification notification = builder.build();
            int layoutId = notification.contentView.getLayoutId();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
            if (viewGroup.findViewById(android.R.id.title) != null) {
                return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
            }
            return findColor(viewGroup);
        } else {
            Notification.Builder builder = new Notification.Builder(context);
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.download_notification_layout);
            builder.setCustomContentView(contentView);
            int layoutId = contentView.getLayoutId();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
            if (viewGroup.findViewById(android.R.id.title) != null) {
                return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
            }
            return findColor(viewGroup);
        }
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < 180.0) {
            return true;
        }
        return false;
    }

    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }
}
