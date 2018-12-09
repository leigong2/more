package com.moreclub.moreapp.packages.model;

import java.util.List;

/**
 * Created by Administrator on 2017-06-14.
 */

public class SalesInfo {

    /**
     * endRow : 0
     * firstPage : 0
     * hasNextPage : true
     * hasPreviousPage : true
     * isFirstPage : true
     * isLastPage : true
     * lastPage : 0
     * list : [{}]
     * navigatePages : 0
     * navigatepageNums : [0]
     * nextPage : 0
     * orderBy : string
     * pageNum : 0
     * pageSize : 0
     * pages : 0
     * prePage : 0
     * size : 0
     * startRow : 0
     * total : 0
     */

    private int endRow;
    private int firstPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int lastPage;
    private int navigatePages;
    private int nextPage;
    private String orderBy;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int prePage;
    private int size;
    private int startRow;
    private int total;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

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

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {

        /**
         * avlInventory : 99
         * collected : false
         * lat : 30.631435
         * lng : 104.055378
         * merchantId : 56
         * merchantName : 卡慕尼酒吧
         * oldPrice : 1456.0
         * personNum : 4
         * photos : http://more-info.oss-cn-beijing.aliyuncs.com/2017/06/14/2UDrPyvSS5wLMaYWoGzTHDrXy.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/06/14/pK9AYX6RwhNCC1SMxuXO38glK.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/06/14/z9h9u6wJVySydYVfnP1CgzVJ4.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/06/14/5L0OFSp5hB5xmaSX7UNiVfy9V.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/06/14/OxjfF9Nm9foq0epHXLriEVwSC.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/06/14/om7EAJUUHR1Apf3OhzklMvNiJ.jpg
         * pid : 64
         * price : 980.0
         * sellDays : 1
         * status : 1
         * title : 威士忌｜经典臻选套餐
         * type : 1
         */

        private int avlInventory;
        private boolean collected;
        private double lat;
        private double lng;
        private int merchantId;
        private String merchantName;
        private String oldPrice;
        private int personNum;
        private String photos;
        private int pid;
        private String price;
        private int sellDays;
        private int status;
        private String title;
        private int type;

        public int getAvlInventory() {
            return avlInventory;
        }

        public void setAvlInventory(int avlInventory) {
            this.avlInventory = avlInventory;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
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

        public String getOldPrice() {
            return oldPrice;
        }

        public void setOldPrice(String oldPrice) {
            this.oldPrice = oldPrice;
        }

        public int getPersonNum() {
            return personNum;
        }

        public void setPersonNum(int personNum) {
            this.personNum = personNum;
        }

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getSellDays() {
            return sellDays;
        }

        public void setSellDays(int sellDays) {
            this.sellDays = sellDays;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
