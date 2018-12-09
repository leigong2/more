package com.moreclub.moreapp.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.moreclub.moreapp.app.MainApp;

/**
 * Created by Administrator on 2017-09-21-0021.
 */

public class SimpleGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        ActivityManager manager = (ActivityManager) MainApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        int heapsize = manager.getMemoryClass();
        Log.i("zune:", "memoryClassSize = " + heapsize);

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        Log.i("zune:", "defaultMemorySize = " + defaultMemoryCacheSize);
        int cutOff = (int) (Runtime.getRuntime().maxMemory() / 8f); // 16% of total memory for bitmaps
        Log.i("zune:", "cutOffSize = " + cutOff);
        if (cutOff < 0 || cutOff > defaultMemoryCacheSize) {
            cutOff = defaultMemoryCacheSize;
        }
        builder.setMemoryCache(new LruResourceCache(cutOff));
        builder.setBitmapPool(new LruBitmapPool(cutOff));
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
