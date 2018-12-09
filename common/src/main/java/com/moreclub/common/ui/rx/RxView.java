package com.moreclub.common.ui.rx;


import com.moreclub.common.ui.DataBinder;

public interface RxView<T> extends DataBinder {

    void onReceiveData2Api(T t, boolean isMore);
}
