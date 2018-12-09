package com.moreclub.moreapp.main.utils;

import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;

import java.util.Map;

/**
 * Created by Administrator on 2017-09-04-0004.
 */

public class UpdateUser {
    static UpdateUser user;
    private Map<String,SignInteractSquareAdapter> adapters;

    private UpdateUser() {
    }

    public static UpdateUser getInstance() {
        if (user == null) {
            user = new UpdateUser();
        }
        return user;
    }
    public Map<String, SignInteractSquareAdapter> getAdapters() {
        return adapters;
    }

    public void setAdapters(Map<String, SignInteractSquareAdapter> adapters) {
        this.adapters = adapters;
    }
}
