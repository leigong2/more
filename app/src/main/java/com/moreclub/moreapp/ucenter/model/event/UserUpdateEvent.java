package com.moreclub.moreapp.ucenter.model.event;

import android.net.Uri;

/**
 * Created by Captain on 2017/3/21.
 */

public class UserUpdateEvent {
    private Uri uri;

    public UserUpdateEvent(Uri uri){
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}
