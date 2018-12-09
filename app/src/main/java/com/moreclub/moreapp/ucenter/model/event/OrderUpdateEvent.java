package com.moreclub.moreapp.ucenter.model.event;

/**
 * Created by Captain on 2017/8/14.
 */

public class OrderUpdateEvent {
    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    private boolean update;
}
