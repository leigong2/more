package com.moreclub.moreapp.ucenter.model;

import com.moreclub.moreapp.main.model.Comments;

import java.util.List;

/**
 * Created by Administrator on 2017-08-28-0028.
 */

public class UserUgc {

    /**
     * chainInters : [{"interId":1,"title":"自由解放者"}]
     * commentCount : 0
     * comments : []
     * content : 这里的酒好喝，便宜，好玩
     * createTime : 1503907029000
     * id : 136
     * likeDto : {"clicked":false,"likeTimes":1,"userLikes":[{"nickName":"志在四方","thumb":"http://more-user.oss-cn-beijing.aliyuncs.com/2016/11/02/u_3097832504509440_1478073471.jpg","uid":3097832504509440}]}
     * likeTime1 : 1
     * mediaType : 0
     * mediaUrl :
     * mid : 91
     * nickName : 志在四方
     * pictures : ["http://more-user.oss-cn-beijing.aliyuncs.com/2017/8/28/30978325045094402017082808995YwXVraPGQK0v.jpg","http://more-user.oss-cn-beijing.aliyuncs.com/2017/8/28/30978325045094402017082808341VIgCkpiw8qOR.jpg","http://more-user.oss-cn-beijing.aliyuncs.com/2017/8/28/30978325045094402017082808888LTDFg9zNus1r.jpg"]
     * preferred :
     * readTimes : 0
     * subtype :
     * thumb : http://more-user.oss-cn-beijing.aliyuncs.com/2016/11/02/u_3097832504509440_1478073471.jpg
     * titlePicture : http://more-user.oss-cn-beijing.aliyuncs.com/2017/8/28/30978325045094402017082808995YwXVraPGQK0v.jpg
     * uid : 3097832504509440
     */

    private int commentCount;
    private String content;
    private long createTime;
    private int ugcId;
    private LikeDtoBean likeDto;
    private int likeTime1;
    private int mediaType;
    private String mediaUrl;
    private int mid;
    private String nickName;
    private String preferred;
    private int readTimes;
    private SubType subtype;
    private String thumb;
    private String titlePicture;
    private long uid;
    private String merchantName;
    private List<ChainIntersBean> chainInters;
    private List<Comments> comments;
    private List<String> pictures;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUgcId() {
        return ugcId;
    }

    public void setUgcId(int ugcId) {
        this.ugcId = ugcId;
    }

    public LikeDtoBean getLikeDto() {
        return likeDto;
    }

    public void setLikeDto(LikeDtoBean likeDto) {
        this.likeDto = likeDto;
    }

    public int getLikeTime1() {
        return likeTime1;
    }

    public void setLikeTime1(int likeTime1) {
        this.likeTime1 = likeTime1;
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

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public SubType getSubtype() {
        return subtype;
    }

    public void setSubtype(SubType subtype) {
        this.subtype = subtype;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitlePicture() {
        return titlePicture;
    }

    public void setTitlePicture(String titlePicture) {
        this.titlePicture = titlePicture;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<ChainIntersBean> getChainInters() {
        return chainInters;
    }

    public void setChainInters(List<ChainIntersBean> chainInters) {
        this.chainInters = chainInters;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public static class LikeDtoBean {
        /**
         * clicked : false
         * likeTimes : 1
         * userLikes : [{"nickName":"志在四方","thumb":"http://more-user.oss-cn-beijing.aliyuncs.com/2016/11/02/u_3097832504509440_1478073471.jpg","uid":3097832504509440}]
         */

        private boolean clicked;
        private int likeTimes;
        private List<UserLikesBean> userLikes;

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }

        public int getLikeTimes() {
            return likeTimes;
        }

        public void setLikeTimes(int likeTimes) {
            this.likeTimes = likeTimes;
        }

        public List<UserLikesBean> getUserLikes() {
            return userLikes;
        }

        public void setUserLikes(List<UserLikesBean> userLikes) {
            this.userLikes = userLikes;
        }

        public static class UserLikesBean {
            /**
             * nickName : 志在四方
             * thumb : http://more-user.oss-cn-beijing.aliyuncs.com/2016/11/02/u_3097832504509440_1478073471.jpg
             * uid : 3097832504509440
             */

            private String nickName;
            private String thumb;
            private long uid;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public long getUid() {
                return uid;
            }

            public void setUid(long uid) {
                this.uid = uid;
            }
        }
    }

    public static class ChainIntersBean {
        /**
         * interId : 1
         * title : 自由解放者
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

    public static  class SubType {
        private int stid;
        private String name;

        public int getStid() {
            return stid;
        }

        public void setStid(int stid) {
            this.stid = stid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
