package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017-07-10.
 */

public class HotMerchant {

    /**
     * homePageSkyMap : {"actHeadUrl":"string","actTextColor":"string","actUrl":"string","areaUrl":"string","backgroundUrl":"string","enbaled":true,"headBorder":"string","homeUrl":"string","linkLine":"string","msgUrl":"string","newMsgUrl":"string","newSearchText":"string","newTopUrl":"string","newUrl":"string","personalUrl":"string","plusUrl":"string","searchImgUrl":"string","searchText":"string","topUrl":"string","yellowValue":"string"}
     * respList : [{"busId":0,"busName":"string","hot":0,"merchantNum":0,"merchants":[{"activityBannerPhoto":"string","activityId":0,"activityStatus":0,"activityTitle":"string","activityType":0,"cardList":[{"cardRate":0,"cardType":0}],"distance":0,"hot":0,"lat":0,"lng":0,"merchantId":0,"merchantName":"string","shelveDate":"2017-07-10T01:56:27.168Z","supportCoupon":true,"tags":"string","thumb":"string"}]}]
     * serverTime : 0
     */

    private HomePageSkyMapBean homePageSkyMap;
    private int serverTime;
    private List<RespListBean> respList;

    public HomePageSkyMapBean getHomePageSkyMap() {
        return homePageSkyMap;
    }

    public void setHomePageSkyMap(HomePageSkyMapBean homePageSkyMap) {
        this.homePageSkyMap = homePageSkyMap;
    }

    public int getServerTime() {
        return serverTime;
    }

    public void setServerTime(int serverTime) {
        this.serverTime = serverTime;
    }

    public List<RespListBean> getRespList() {
        return respList;
    }

    public void setRespList(List<RespListBean> respList) {
        this.respList = respList;
    }

    public static class HomePageSkyMapBean {
        /**
         * actHeadUrl : string
         * actTextColor : string
         * actUrl : string
         * areaUrl : string
         * backgroundUrl : string
         * enbaled : true
         * headBorder : string
         * homeUrl : string
         * linkLine : string
         * msgUrl : string
         * newMsgUrl : string
         * newSearchText : string
         * newTopUrl : string
         * newUrl : string
         * personalUrl : string
         * plusUrl : string
         * searchImgUrl : string
         * searchText : string
         * topUrl : string
         * yellowValue : string
         */

        private String actHeadUrl;
        private String actTextColor;
        private String actUrl;
        private String areaUrl;
        private String backgroundUrl;
        private boolean enbaled;
        private String headBorder;
        private String homeUrl;
        private String linkLine;
        private String msgUrl;
        private String newMsgUrl;
        private String newSearchText;
        private String newTopUrl;
        private String newUrl;
        private String personalUrl;
        private String plusUrl;
        private String searchImgUrl;
        private String searchText;
        private String topUrl;
        private String yellowValue;

        public String getActHeadUrl() {
            return actHeadUrl;
        }

        public void setActHeadUrl(String actHeadUrl) {
            this.actHeadUrl = actHeadUrl;
        }

        public String getActTextColor() {
            return actTextColor;
        }

        public void setActTextColor(String actTextColor) {
            this.actTextColor = actTextColor;
        }

        public String getActUrl() {
            return actUrl;
        }

        public void setActUrl(String actUrl) {
            this.actUrl = actUrl;
        }

        public String getAreaUrl() {
            return areaUrl;
        }

        public void setAreaUrl(String areaUrl) {
            this.areaUrl = areaUrl;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl) {
            this.backgroundUrl = backgroundUrl;
        }

        public boolean isEnbaled() {
            return enbaled;
        }

        public void setEnbaled(boolean enbaled) {
            this.enbaled = enbaled;
        }

        public String getHeadBorder() {
            return headBorder;
        }

        public void setHeadBorder(String headBorder) {
            this.headBorder = headBorder;
        }

        public String getHomeUrl() {
            return homeUrl;
        }

        public void setHomeUrl(String homeUrl) {
            this.homeUrl = homeUrl;
        }

        public String getLinkLine() {
            return linkLine;
        }

        public void setLinkLine(String linkLine) {
            this.linkLine = linkLine;
        }

        public String getMsgUrl() {
            return msgUrl;
        }

        public void setMsgUrl(String msgUrl) {
            this.msgUrl = msgUrl;
        }

        public String getNewMsgUrl() {
            return newMsgUrl;
        }

        public void setNewMsgUrl(String newMsgUrl) {
            this.newMsgUrl = newMsgUrl;
        }

        public String getNewSearchText() {
            return newSearchText;
        }

        public void setNewSearchText(String newSearchText) {
            this.newSearchText = newSearchText;
        }

        public String getNewTopUrl() {
            return newTopUrl;
        }

        public void setNewTopUrl(String newTopUrl) {
            this.newTopUrl = newTopUrl;
        }

        public String getNewUrl() {
            return newUrl;
        }

        public void setNewUrl(String newUrl) {
            this.newUrl = newUrl;
        }

        public String getPersonalUrl() {
            return personalUrl;
        }

        public void setPersonalUrl(String personalUrl) {
            this.personalUrl = personalUrl;
        }

        public String getPlusUrl() {
            return plusUrl;
        }

        public void setPlusUrl(String plusUrl) {
            this.plusUrl = plusUrl;
        }

        public String getSearchImgUrl() {
            return searchImgUrl;
        }

        public void setSearchImgUrl(String searchImgUrl) {
            this.searchImgUrl = searchImgUrl;
        }

        public String getSearchText() {
            return searchText;
        }

        public void setSearchText(String searchText) {
            this.searchText = searchText;
        }

        public String getTopUrl() {
            return topUrl;
        }

        public void setTopUrl(String topUrl) {
            this.topUrl = topUrl;
        }

        public String getYellowValue() {
            return yellowValue;
        }

        public void setYellowValue(String yellowValue) {
            this.yellowValue = yellowValue;
        }
    }

    public static class RespListBean {
        /**
         * busId : 0
         * busName : string
         * hot : 0
         * merchantNum : 0
         * merchants : [{"activityBannerPhoto":"string","activityId":0,"activityStatus":0,"activityTitle":"string","activityType":0,"cardList":[{"cardRate":0,"cardType":0}],"distance":0,"hot":0,"lat":0,"lng":0,"merchantId":0,"merchantName":"string","shelveDate":"2017-07-10T01:56:27.168Z","supportCoupon":true,"tags":"string","thumb":"string"}]
         */

        private int busId;
        private String busName;
        private int hot;
        private int merchantNum;
        private List<MerchantsBean> merchants;

        public int getBusId() {
            return busId;
        }

        public void setBusId(int busId) {
            this.busId = busId;
        }

        public String getBusName() {
            return busName;
        }

        public void setBusName(String busName) {
            this.busName = busName;
        }

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getMerchantNum() {
            return merchantNum;
        }

        public void setMerchantNum(int merchantNum) {
            this.merchantNum = merchantNum;
        }

        public List<MerchantsBean> getMerchants() {
            return merchants;
        }

        public void setMerchants(List<MerchantsBean> merchants) {
            this.merchants = merchants;
        }

        public static class MerchantsBean {
            /**
             * activityBannerPhoto : string
             * activityId : 0
             * activityStatus : 0
             * activityTitle : string
             * activityType : 0
             * cardList : [{"cardRate":0,"cardType":0}]
             * distance : 0
             * hot : 0
             * lat : 0
             * lng : 0
             * merchantId : 0
             * merchantName : string
             * shelveDate : 2017-07-10T01:56:27.168Z
             * supportCoupon : true
             * tags : string
             * thumb : string
             */

            private String activityBannerPhoto;
            private int activityId;
            private int activityStatus;
            private String activityTitle;
            private int activityType;
            private double distance;
            private int hot;
            private double lat;
            private double lng;
            private int merchantId;
            private String merchantName;
            private String shelveDate;
            private boolean supportCoupon;
            private String tags;
            private String thumb;
            private List<CardListBean> cardList;

            public String getActivityBannerPhoto() {
                return activityBannerPhoto;
            }

            public void setActivityBannerPhoto(String activityBannerPhoto) {
                this.activityBannerPhoto = activityBannerPhoto;
            }

            public int getActivityId() {
                return activityId;
            }

            public void setActivityId(int activityId) {
                this.activityId = activityId;
            }

            public int getActivityStatus() {
                return activityStatus;
            }

            public void setActivityStatus(int activityStatus) {
                this.activityStatus = activityStatus;
            }

            public String getActivityTitle() {
                return activityTitle;
            }

            public void setActivityTitle(String activityTitle) {
                this.activityTitle = activityTitle;
            }

            public int getActivityType() {
                return activityType;
            }

            public void setActivityType(int activityType) {
                this.activityType = activityType;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public int getHot() {
                return hot;
            }

            public void setHot(int hot) {
                this.hot = hot;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public int getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(int merchantId) {
                this.merchantId = merchantId;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public String getShelveDate() {
                return shelveDate;
            }

            public void setShelveDate(String shelveDate) {
                this.shelveDate = shelveDate;
            }

            public boolean isSupportCoupon() {
                return supportCoupon;
            }

            public void setSupportCoupon(boolean supportCoupon) {
                this.supportCoupon = supportCoupon;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public List<CardListBean> getCardList() {
                return cardList;
            }

            public void setCardList(List<CardListBean> cardList) {
                this.cardList = cardList;
            }

            public static class CardListBean {
                /**
                 * cardRate : 0
                 * cardType : 0
                 */

                private double cardRate;
                private int cardType;

                public double getCardRate() {
                    return cardRate;
                }

                public void setCardRate(double cardRate) {
                    this.cardRate = cardRate;
                }

                public int getCardType() {
                    return cardType;
                }

                public void setCardType(int cardType) {
                    this.cardType = cardType;
                }
            }
        }
    }
}
