package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/8/26.
 */

public class Topic {
    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private int tid;
    private String name;
    private boolean selected;
}
