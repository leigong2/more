package com.moreclub.common.ui;

public class BasePresenter<T extends DataBinder> implements Presenter<T> {

    private T mBinder;

    @Override
    public void attachView(T binder) {
        this.mBinder = binder;
    }

    @Override
    public void detachView() {
        this.mBinder = null;
    }

    public boolean isViewBind() {
        return mBinder != null;
    }


    public T getBinder() {
        return mBinder;
    }

}
