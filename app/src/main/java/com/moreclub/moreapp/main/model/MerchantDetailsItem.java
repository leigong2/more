package com.moreclub.moreapp.main.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/2/28.
 */

public class MerchantDetailsItem {

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    private boolean featured;
    private int id;
    private String name;
    private String thumb;



    public String getDics() {
        return dics;
    }

    public void setDics(String dics) {
        this.dics = dics;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    private ArrayList<String> pictures;
    private String dics;
    private String ext;

    public boolean isExistMusic() {
        return isExistMusic;
    }

    public void setExistMusic(boolean existMusic) {
        isExistMusic = existMusic;
    }

    private boolean isExistMusic;

    public String getMusicPlayUrl() {
        return musicPlayUrl;
    }

    public void setMusicPlayUrl(String musicPlayUrl) {
        this.musicPlayUrl = musicPlayUrl;
    }

    private String musicPlayUrl;



}
