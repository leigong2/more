package com.moreclub.moreapp.ucenter.model;

import java.util.List;

/**
 * Created by Administrator on 2017-08-25-0025.
 */

public class UserDetailsV2 {

    /**
     * birthday : 2017-08-25T06:48:22.509Z
     * bookPrefer : string
     * brandPrefer : string
     * careerName : string
     * city : string
     * foodPrefer : string
     * hobby : string
     * nickName : string
     * personalMark : string
     * profileAlbum : {"albums":[{"ctime":0,"id":0,"thumbUrl":"string","uid":0,"url":"string"}],"count":0}
     * profileLike : {"count":0,"likes":[{"nickName":"string","thumb":"string","uid":0}]}
     * sex : string
     * thumb : string
     * winePrefer : string
     */

    private String birthday;
    private String bookPrefer;
    private String brandPrefer;
    private String careerName;
    private String city;
    private String foodPrefer;
    private String hobby;
    private String nickName;
    private String personalMark;
    private ProfileAlbumBean profileAlbum;
    private ProfileLikeBean profileLike;
    private String sex;
    private String thumb;
    private String winePrefer;
    private boolean hasFollow;
    private int ugcCount;
    private List<UgcSimpledtosBean> ugcSimpleDtos;
    private ProfileAcitivityBean profileAcitivity;
    public ProfileAcitivityBean getProfileAcitivity() {
        return profileAcitivity;
    }

    public void setProfileAcitivity(ProfileAcitivityBean profileAcitivity) {
        this.profileAcitivity = profileAcitivity;
    }

    public int getUgcCount() {
        return ugcCount;
    }

    public void setUgcCount(int ugcCount) {
        this.ugcCount = ugcCount;
    }

    public List<UgcSimpledtosBean> getUgcSimpleDtos() {
        return ugcSimpleDtos;
    }

    public void setUgcSimpleDtos(List<UgcSimpledtosBean> ugcSimpleDtos) {
        this.ugcSimpleDtos = ugcSimpleDtos;
    }

    public boolean getHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBookPrefer() {
        return bookPrefer;
    }

    public void setBookPrefer(String bookPrefer) {
        this.bookPrefer = bookPrefer;
    }

    public String getBrandPrefer() {
        return brandPrefer;
    }

    public void setBrandPrefer(String brandPrefer) {
        this.brandPrefer = brandPrefer;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFoodPrefer() {
        return foodPrefer;
    }

    public void setFoodPrefer(String foodPrefer) {
        this.foodPrefer = foodPrefer;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonalMark() {
        return personalMark;
    }

    public void setPersonalMark(String personalMark) {
        this.personalMark = personalMark;
    }

    public ProfileAlbumBean getProfileAlbum() {
        return profileAlbum;
    }

    public void setProfileAlbum(ProfileAlbumBean profileAlbum) {
        this.profileAlbum = profileAlbum;
    }

    public ProfileLikeBean getProfileLike() {
        return profileLike;
    }

    public void setProfileLike(ProfileLikeBean profileLike) {
        this.profileLike = profileLike;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getWinePrefer() {
        return winePrefer;
    }

    public void setWinePrefer(String winePrefer) {
        this.winePrefer = winePrefer;
    }

    public static class ProfileAlbumBean {
        /**
         * albums : [{"ctime":0,"id":0,"thumbUrl":"string","uid":0,"url":"string"}]
         * count : 0
         */

        private int count;
        private List<AlbumsBean> albums;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<AlbumsBean> getAlbums() {
            return albums;
        }

        public void setAlbums(List<AlbumsBean> albums) {
            this.albums = albums;
        }

        public static class AlbumsBean {
            /**
             * ctime : 0
             * id : 0
             * thumbUrl : string
             * uid : 0
             * url : string
             */

            private long ctime;
            private long id;
            private String thumbUrl;
            private long uid;
            private String url;

            public long getCtime() {
                return ctime;
            }

            public void setCtime(long ctime) {
                this.ctime = ctime;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getThumbUrl() {
                return thumbUrl;
            }

            public void setThumbUrl(String thumbUrl) {
                this.thumbUrl = thumbUrl;
            }

            public long getUid() {
                return uid;
            }

            public void setUid(long uid) {
                this.uid = uid;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class ProfileLikeBean {
        /**
         * count : 0
         * likes : [{"nickName":"string","thumb":"string","uid":0}]
         */

        private int count;
        private List<LikesBean> likes;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<LikesBean> getLikes() {
            return likes;
        }

        public void setLikes(List<LikesBean> likes) {
            this.likes = likes;
        }

        public static class LikesBean {
            /**
             * nickName : string
             * thumb : string
             * uid : 0
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

    public static class UgcSimpledtosBean {

        /**
         * titlePictrues : http://more-user.oss-cn-beijing.aliyuncs.com/2016/11/13/u_16095689156_1479022559.jpg
         * content : 刷卡好地方,过来玩啊帅哥，很便宜的
         * mediaType : 1
         * mediaUrl : null
         */

        private String titlePictrues;
        private String content;
        private int mediaType;
        private String mediaUrl;
        private long sid;

        public long getSid() {
            return sid;
        }

        public void setSid(long sid) {
            this.sid = sid;
        }

        public String getTitlePictrues() {
            return titlePictrues;
        }

        public void setTitlePictrues(String titlePictrues) {
            this.titlePictrues = titlePictrues;
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
    }

    public static class ProfileAcitivityBean{

        /**
         * count : 0
         * respList : [{"activityId":0,"address":"string","bannerPhoto":"string","endTime":{"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0},"holdingTimes":"string","holdingType":0,"maxParticipants":0,"merchant":"string","merchantId":0,"sponsor":0,"sponsorName":"string","startTime":{"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0},"status":0,"title":"string"}]
         */

        private int count;
        private List<RespListBean> respList;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<RespListBean> getRespList() {
            return respList;
        }

        public void setRespList(List<RespListBean> respList) {
            this.respList = respList;
        }

        public static class RespListBean {
            /**
             * activityId : 0
             * address : string
             * bannerPhoto : string
             * endTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
             * holdingTimes : string
             * holdingType : 0
             * maxParticipants : 0
             * merchant : string
             * merchantId : 0
             * sponsor : 0
             * sponsorName : string
             * startTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
             * status : 0
             * title : string
             */

            private int activityId;
            private String address;
            private String bannerPhoto;
            private Long endTime;
            private String holdingTimes;
            private int holdingType;
            private int maxParticipants;
            private String merchant;
            private int merchantId;
            private int sponsor;
            private String sponsorName;
            private Long startTime;
            private int status;
            private String title;

            public Long getEndTime() {
                return endTime;
            }

            public void setEndTime(Long endTime) {
                this.endTime = endTime;
            }

            public Long getStartTime() {
                return startTime;
            }

            public void setStartTime(Long startTime) {
                this.startTime = startTime;
            }

            public int getActivityId() {
                return activityId;
            }

            public void setActivityId(int activityId) {
                this.activityId = activityId;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBannerPhoto() {
                return bannerPhoto;
            }

            public void setBannerPhoto(String bannerPhoto) {
                this.bannerPhoto = bannerPhoto;
            }

            public String getHoldingTimes() {
                return holdingTimes;
            }

            public void setHoldingTimes(String holdingTimes) {
                this.holdingTimes = holdingTimes;
            }

            public int getHoldingType() {
                return holdingType;
            }

            public void setHoldingType(int holdingType) {
                this.holdingType = holdingType;
            }

            public int getMaxParticipants() {
                return maxParticipants;
            }

            public void setMaxParticipants(int maxParticipants) {
                this.maxParticipants = maxParticipants;
            }

            public String getMerchant() {
                return merchant;
            }

            public void setMerchant(String merchant) {
                this.merchant = merchant;
            }

            public int getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(int merchantId) {
                this.merchantId = merchantId;
            }

            public int getSponsor() {
                return sponsor;
            }

            public void setSponsor(int sponsor) {
                this.sponsor = sponsor;
            }

            public String getSponsorName() {
                return sponsorName;
            }

            public void setSponsorName(String sponsorName) {
                this.sponsorName = sponsorName;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
