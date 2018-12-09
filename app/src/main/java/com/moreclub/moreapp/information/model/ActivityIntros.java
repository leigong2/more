package com.moreclub.moreapp.information.model;

/**
 * Created by Administrator on 2017-05-27.
 */

public class ActivityIntros {
    /**
     * {
     * "activityChain": {},
     * "activityId": 0,
     * "addr": "string",
     * "bannerPhoto": "string",
     * "busareaId": 0,
     * "channel": 0,
     * "collected": true,
     * "createTime": {},
     * "details": "string",
     * "endTime": {},
     * "featuredDrink": "string",
     * "holdingTimes": "string",
     * "holdingType": 0,
     * "introduction": "string",
     * "lat": 0,
     * "layout": 0,
     * "lng": 0,
     * "merchant": "string",
     * "payRemark": "string",
     * "productItems": "string",
     * "sex": "string",
     * "sponsor": 0,
     * "sponsorName": "string",
     * "sponsorThumb": "string",
     * "startTime": {},
     * "status": 0,
     * "summary": "string",
     * "tag": "string",
     * "title": "string",
     * "type": 0
     * }
     */

    private ActivityChainBean activityChain;
    private int activityId;
    private String addr;
    private String bannerPhoto;
    private int busareaId;
    private int channel;
    private boolean collected;
    private Long createTime;
    private String details;
    private Long endTime;
    private String featuredDrink;
    private String holdingTimes;
    private int holdingType;
    private String introduction;
    private double lat;
    private int layout;
    private double lng;
    private String merchant;
    private String payRemark;
    private String productItems;
    private String sex;
    private long sponsor;
    private String sponsorName;
    private String sponsorThumb;
    private Long startTime;
    private int status;
    private String summary;
    private String tag;
    private String title;
    private int type;
    private Integer textType;
    public static class ActivityChainBean {
        private int acId;
        private String chainThumb;
        private String chainTitle;
        private String chainUrl;
        private boolean ticket;

        public int getAcId() {
            return acId;
        }

        public void setAcId(int acId) {
            this.acId = acId;
        }

        public String getChainThumb() {
            return chainThumb;
        }

        public void setChainThumb(String chainThumb) {
            this.chainThumb = chainThumb;
        }

        public String getChainTitle() {
            return chainTitle;
        }

        public void setChainTitle(String chainTitle) {
            this.chainTitle = chainTitle;
        }

        public String getChainUrl() {
            return chainUrl;
        }

        public void setChainUrl(String chainUrl) {
            this.chainUrl = chainUrl;
        }

        public boolean isTicket() {
            return ticket;
        }

        public void setTicket(boolean ticket) {
            this.ticket = ticket;
        }
    }

    public ActivityChainBean getActivityChain() {
        return activityChain;
    }

    public void setActivityChain(ActivityChainBean activityChain) {
        this.activityChain = activityChain;
    }

    public Integer getTextType() {
        return textType;
    }

    public void setTextType(Integer textType) {
        this.textType = textType;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBannerPhoto() {
        return bannerPhoto;
    }

    public void setBannerPhoto(String bannerPhoto) {
        this.bannerPhoto = bannerPhoto;
    }

    public int getBusareaId() {
        return busareaId;
    }

    public void setBusareaId(int busareaId) {
        this.busareaId = busareaId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getFeaturedDrink() {
        return featuredDrink;
    }

    public void setFeaturedDrink(String featuredDrink) {
        this.featuredDrink = featuredDrink;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark;
    }

    public String getProductItems() {
        return productItems;
    }

    public void setProductItems(String productItems) {
        this.productItems = productItems;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getSponsor() {
        return sponsor;
    }

    public void setSponsor(long sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorThumb() {
        return sponsorThumb;
    }

    public void setSponsorThumb(String sponsorThumb) {
        this.sponsorThumb = sponsorThumb;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ActivityIntros{" +
                "activityChain=" + activityChain +
                ", activityId=" + activityId +
                ", addr='" + addr + '\'' +
                ", bannerPhoto='" + bannerPhoto + '\'' +
                ", busareaId=" + busareaId +
                ", channel=" + channel +
                ", collected=" + collected +
                ", createTime=" + createTime +
                ", details='" + details + '\'' +
                ", endTime=" + endTime +
                ", featuredDrink='" + featuredDrink + '\'' +
                ", holdingTimes='" + holdingTimes + '\'' +
                ", holdingType=" + holdingType +
                ", introduction='" + introduction + '\'' +
                ", lat=" + lat +
                ", layout=" + layout +
                ", lng=" + lng +
                ", merchant='" + merchant + '\'' +
                ", payRemark='" + payRemark + '\'' +
                ", productItems='" + productItems + '\'' +
                ", sex='" + sex + '\'' +
                ", sponsor=" + sponsor +
                ", sponsorName='" + sponsorName + '\'' +
                ", sponsorThumb='" + sponsorThumb + '\'' +
                ", startTime=" + startTime +
                ", status=" + status +
                ", summary='" + summary + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
