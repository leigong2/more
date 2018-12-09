package com.moreclub.common.ui.view.scrollview;

/**
 * Created by Administrator on 2017/3/9.
 */

public enum ScrollState {
    /**
     * Widget is stopped.
     * This state does not always mean that this widget have never been scrolled.
     */
    STOP,

    /**
     * Widget is scrolled up by swiping it down.
     */
    UP,

    /**
     * Widget is scrolled down by swiping it up.
     */
    DOWN,
}
