package com.moreclub.moreapp.util;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.moreclub.common.util.PrefsUtils;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/3.
 */

public class SaveSearchTask extends AsyncTask<String, Void ,Void> {
    private final static String KEY_HISTORY = "search.history";
    private final static int MAX_COUNT = 10;
    private Context context;

    public SaveSearchTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String keyword = params[0];
        String oldText =  PrefsUtils.getString(context, KEY_HISTORY, "");
        if (!TextUtils.isEmpty(oldText)) {
            String[] array = oldText.split(",");

            boolean exist = false;
            for(String s: array){
                if(s.equals(keyword))
                    exist =  true;
            }

            if (exist) return null;

            if(array.length >= MAX_COUNT){
                StringBuilder builder = new StringBuilder(keyword);
                for (int i = 0; i < MAX_COUNT - 1; i ++) {
                    builder.append(",");
                    builder.append(array[i]);
                }

                PrefsUtils.setString(context, KEY_HISTORY, builder.toString());
            } else {
                PrefsUtils.setString(context, KEY_HISTORY, keyword + "," + oldText);
            }
        } else {
            PrefsUtils.setString(context, KEY_HISTORY, keyword);
        }
        return null;
    }
}
