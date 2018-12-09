package com.moreclub.moreapp.main.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/8/28.
 */

public class AddUGCParam {

//    {
//        "chainInters": [
//        0
//        ],
//        "content": "string",
//            "mediaType": 0,
//            "mediaUrl": "string",
//            "mid": 0,
//            "pictures": [
//        "string"
//        ],
//        "titlePicture": "string",
//            "uid": 0
//    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitlePicture() {
        return titlePicture;
    }

    public void setTitlePicture(String titlePicture) {
        this.titlePicture = titlePicture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<Integer> getChainInters() {
        return chainInters;
    }

    public void setChainInters(ArrayList<Integer> chainInters) {
        this.chainInters = chainInters;
    }

    private long uid;
    private String titlePicture;
    private String content;
    private int mediaType;
    private String mediaUrl;
    private Integer mid;
    public int getCity() {
        return city;
    }
    public void setCity(int city) {
        this.city = city;
    }
    private int city;
    private ArrayList<String> pictures;
    private ArrayList<Integer> chainInters;


}
