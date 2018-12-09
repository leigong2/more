package com.moreclub.moreapp.information.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-05-18.
 */

public class ActivityData implements Serializable {
    public static class ActivityItem {
        private boolean collected;
        private int activityId;
        private int merchantId;
        private int status;
        private int type;
        private long createTime;
        private long endTime;
        private long startTime;
        private String bannerPhoto;
        private String featuredDrink;
        private String introduction;
        private String merchantName;
        private String title;
        private int holdingType;
        private String holdingTimes;


        @Override
        public String toString() {
            return "ActivityItem{" +
                    "collected=" + collected +
                    ", activityId=" + activityId +
                    ", merchantId=" + merchantId +
                    ", status=" + status +
                    ", type=" + type +
                    ", createTime=" + createTime +
                    ", endTime=" + endTime +
                    ", startTime=" + startTime +
                    ", bannerPhoto='" + bannerPhoto + '\'' +
                    ", featuredDrink='" + featuredDrink + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", merchantName='" + merchantName + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }

        public int getHoldingType() {
            return holdingType;
        }

        public void setHoldingType(int holdingType) {
            this.holdingType = holdingType;
        }

        public String getHoldingTimes() {
            return holdingTimes;
        }

        public void setHoldingTimes(String holdingTimes) {
            this.holdingTimes = holdingTimes;
        }

        public boolean isCollect() {
            return collected;
        }

        public void setCollect(boolean collected) {
            this.collected = collected;
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getBannerPhoto() {
            return bannerPhoto;
        }

        public void setBannerPhoto(String bannerPhoto) {
            this.bannerPhoto = bannerPhoto;
        }

        public String getFeaturedDrink() {
            return featuredDrink;
        }

        public void setFeaturedDrink(String featuredDrink) {
            this.featuredDrink = featuredDrink;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    private int endRow;
    private int firstPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int lastPage;
    private int navigatePages;
    private int nextPage;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int prePage;
    private int size;
    private int startRow;
    private int total;
    private String navigatepageNums;
    private String orderBy;
    private List<ActivityItem> list;

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(String navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<ActivityItem> getList() {
        return list;
    }

    public void setList(List<ActivityItem> list) {
        this.list = list;
    }
}
