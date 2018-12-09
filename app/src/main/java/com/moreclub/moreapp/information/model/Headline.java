package com.moreclub.moreapp.information.model;

import java.util.List;

/**
 * Created by Administrator on 2017-05-16.
 */

public class Headline {
    private int total;
    private List<HeadlineDetail> list;

    public static class HeadlineDetail {
        private Integer brandId;
        private Integer cityId;
        private boolean colloct;
        private boolean del;
        private Integer displayModal;
        private Integer likeTime1;
        private Integer likeTime2;
        private Integer likeTime3;
        private Integer likeTime4;
        private Integer likeTime5;
        private Integer mediaType;
        private Integer sid;
        private Integer type;
        private Long createTime;
        private Long forwardId;
        private Long uid;
        private String clicked;
        private String content;
        private String introduction;
        private String likeTimes;
        private String mediaUrl;
        private String readTimes;
        private String title;
        private String titlePicture;
        private String username;
        private String userthumb;
        private int commentsCount;
        private Integer textType;

        public Integer getTextType() {
            return textType;
        }

        public void setTextType(Integer textType) {
            this.textType = textType;
        }

        public int getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }

        public boolean isColloct() {
            return colloct;
        }

        public boolean isDel() {
            return del;
        }

        public int getConmentsCount() {
            return commentsCount;
        }

        public void setConmentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }

        public Integer getBrandId() {
            return brandId;
        }

        public void setBrandId(Integer brandId) {
            this.brandId = brandId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public boolean getColloct() {
            return colloct;
        }

        public void setColloct(boolean colloct) {
            this.colloct = colloct;
        }

        public boolean getDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public Integer getDisplayModal() {
            return displayModal;
        }

        public void setDisplayModal(Integer displayModal) {
            this.displayModal = displayModal;
        }

        public Integer getLikeTime1() {
            return likeTime1;
        }

        public void setLikeTime1(Integer likeTime1) {
            this.likeTime1 = likeTime1;
        }

        public Integer getLikeTime2() {
            return likeTime2;
        }

        public void setLikeTime2(Integer likeTime2) {
            this.likeTime2 = likeTime2;
        }

        public Integer getLikeTime3() {
            return likeTime3;
        }

        public void setLikeTime3(Integer likeTime3) {
            this.likeTime3 = likeTime3;
        }

        public Integer getLikeTime4() {
            return likeTime4;
        }

        public void setLikeTime4(Integer likeTime4) {
            this.likeTime4 = likeTime4;
        }

        public Integer getLikeTime5() {
            return likeTime5;
        }

        public void setLikeTime5(Integer likeTime5) {
            this.likeTime5 = likeTime5;
        }

        public Integer getMediaType() {
            return mediaType;
        }

        public void setMediaType(Integer mediaType) {
            this.mediaType = mediaType;
        }

        public Integer getSid() {
            return sid;
        }

        public void setSid(Integer sid) {
            this.sid = sid;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getForwardId() {
            return forwardId;
        }

        public void setForwardId(Long forwardId) {
            this.forwardId = forwardId;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getClicked() {
            return clicked;
        }

        public void setClicked(String clicked) {
            this.clicked = clicked;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getLikeTimes() {
            return likeTimes;
        }

        public void setLikeTimes(String likeTimes) {
            this.likeTimes = likeTimes;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public String getReadTimes() {
            return readTimes;
        }

        public void setReadTimes(String readTimes) {
            this.readTimes = readTimes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitlePicture() {
            return titlePicture;
        }

        public void setTitlePicture(String titlePicture) {
            this.titlePicture = titlePicture;
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

        @Override
        public String toString() {
            return "HeadlineDetail{" +
                    "brandId=" + brandId +
                    ", cityId=" + cityId +
                    ", colloct=" + colloct +
                    ", del=" + del +
                    ", displayModal=" + displayModal +
                    ", likeTime1=" + likeTime1 +
                    ", likeTime2=" + likeTime2 +
                    ", likeTime3=" + likeTime3 +
                    ", likeTime4=" + likeTime4 +
                    ", likeTime5=" + likeTime5 +
                    ", mediaType=" + mediaType +
                    ", sid=" + sid +
                    ", type=" + type +
                    ", createTime=" + createTime +
                    ", forwardId=" + forwardId +
                    ", uid=" + uid +
                    ", clicked='" + clicked + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", likeTimes='" + likeTimes + '\'' +
                    ", mediaUrl='" + mediaUrl + '\'' +
                    ", readTimes='" + readTimes + '\'' +
                    ", title='" + title + '\'' +
                    ", titlePicture='" + titlePicture + '\'' +
                    ", username='" + username + '\'' +
                    ", userthumb='" + userthumb + '\'' +
                    '}';
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HeadlineDetail> getList() {
        return list;
    }

    public void setList(List<HeadlineDetail> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Headline{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
