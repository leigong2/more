package com.moreclub.moreapp.main.model;


import java.util.List;

/**
 * Created by Captain on 2017/8/28.
 */

public class UGCChannel {

    /**
     * sid : 248
     * title :
     * introduction :
     * mediaType : 0
     * titlePicture : http://more-user.oss-cn-beijing.aliyuncs.com/2017/9/7/9938702475562017090722765cKYZrakJKmk7.jpg
     * mediaUrl : null
     * type : null
     * textType : null
     * createTime : 1504774531000
     * uid : 993870247556
     * cityId : 2
     * readTimes : null
     * likeTime1 : 0
     * likeTime2 : null
     * likeTime3 : null
     * likeTime4 : null
     * likeTime5 : null
     * brandId : null
     * commentsCount : 0
     * pictures : ["http://more-user.oss-cn-beijing.aliyuncs.com/2017/9/7/9938702475562017090722765cKYZrakJKmk7.jpg"]
     * chainInters : [{"interId":1,"title":"黑怕不怕黑，表白红花会"}]
     * merchantName : 莽起喝ANTISOBER
     * mid : 486
     * ftype : 1
     * displayModal : null
     * username : 王志龙
     * userthumb : http://more-user.oss-cn-beijing.aliyuncs.com/2017/7/29/9938702475562017072912666.jpg
     * likeTimes : null
     * content : 哼哼唧唧
     * clicked : null
     * forwardId : 993870247556
     * colloct : false
     * del : false
     */

    private int sid;
    private String title;
    private String introduction;
    private int mediaType;
    private String titlePicture;
    private String mediaUrl;
    private int type;
    private Integer textType;
    private long createTime;
    private long uid;
    private int cityId;
    private long readTimes;
    private int likeTime1;
    private int likeTime2;
    private int likeTime3;
    private int likeTime4;
    private int likeTime5;
    private long brandId;
    private int commentsCount;
    private String merchantName;
    private int mid;
    private int ftype;
    private int displayModal;
    private String username;
    private String userthumb;
    private int likeTimes;
    private String content;
    private String clicked;
    private long forwardId;
    private boolean colloct;
    private boolean del;
    private boolean follow;
    private List<String> pictures;
    private List<ChainIntersBean> chainInters;

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitlePicture() {
        return titlePicture;
    }

    public void setTitlePicture(String titlePicture) {
        this.titlePicture = titlePicture;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getTextType() {
        return textType;
    }

    public void setTextType(Integer textType) {
        this.textType = textType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public long getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(long readTimes) {
        this.readTimes = readTimes;
    }

    public int getLikeTime1() {
        return likeTime1;
    }

    public void setLikeTime1(int likeTime1) {
        this.likeTime1 = likeTime1;
    }

    public int getLikeTime2() {
        return likeTime2;
    }

    public void setLikeTime2(int likeTime2) {
        this.likeTime2 = likeTime2;
    }

    public int getLikeTime3() {
        return likeTime3;
    }

    public void setLikeTime3(int likeTime3) {
        this.likeTime3 = likeTime3;
    }

    public int getLikeTime4() {
        return likeTime4;
    }

    public void setLikeTime4(int likeTime4) {
        this.likeTime4 = likeTime4;
    }

    public int getLikeTime5() {
        return likeTime5;
    }

    public void setLikeTime5(int likeTime5) {
        this.likeTime5 = likeTime5;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getDisplayModal() {
        return displayModal;
    }

    public void setDisplayModal(int displayModal) {
        this.displayModal = displayModal;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserthumb() {
        return userthumb;
    }

    public void setUserthumb(String userthumb) {
        this.userthumb = userthumb;
    }

    public int getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(int likeTimes) {
        this.likeTimes = likeTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String isClicked() {
        return clicked;
    }

    public void setClicked(String clicked) {
        this.clicked = clicked;
    }

    public long getForwardId() {
        return forwardId;
    }

    public void setForwardId(long forwardId) {
        this.forwardId = forwardId;
    }

    public boolean isColloct() {
        return colloct;
    }

    public void setColloct(boolean colloct) {
        this.colloct = colloct;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<ChainIntersBean> getChainInters() {
        return chainInters;
    }

    public void setChainInters(List<ChainIntersBean> chainInters) {
        this.chainInters = chainInters;
    }

    public static class ChainIntersBean {
        /**
         * interId : 1
         * title : 黑怕不怕黑，表白红花会
         */

        private int interId;
        private String title;

        public int getInterId() {
            return interId;
        }

        public void setInterId(int interId) {
            this.interId = interId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
