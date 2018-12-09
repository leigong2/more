package com.moreclub.moreapp.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

import com.moreclub.moreapp.app.MainApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017-09-05-0005.
 */

public class FileSaveUtils {

    public static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)
            ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    public static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/more";//保存的确切位置
    public static final String SAVE_REAL_PATH_TEMP = SAVE_PIC_PATH + "/more/temp";//保存的确切位置
    private static final int NOTIFY_SUCCESS = 1001;
    private static final int NOTIFY_FAIL = 1002;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NOTIFY_SUCCESS) {
                Toast.makeText(MainApp.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == NOTIFY_FAIL) {
                Toast.makeText(MainApp.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public interface OnSaveListener {
        void getFilesResponse(Bitmap map);

        void getFilesFail();
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String path) {
        // 首先保存图片
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = "More" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            handler.sendEmptyMessage(NOTIFY_SUCCESS);
        } catch (FileNotFoundException e) {
            handler.sendEmptyMessage(NOTIFY_FAIL);
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public void getBitMBitmap(final String urlpath, final OnSaveListener listener) {
        new Thread() {
            @Override
            public void run() {
                Bitmap map = null;
                try {
                    URL url = new URL(urlpath);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    InputStream in;
                    in = conn.getInputStream();
                    map = BitmapFactory.decodeStream(in);
                    listener.getFilesResponse(map);
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.getFilesFail();
                }
            }
        }.start();
    }
}
