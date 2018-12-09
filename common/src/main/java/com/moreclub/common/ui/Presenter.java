package com.moreclub.common.ui;

public interface Presenter<V> {
    void attachView(V mvpView);
    void detachView();
}
