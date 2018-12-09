package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Captain on 2017/7/18.
 */

public class PackageCheckItem {
//    {"data":{"pageNum":1,"pageSize":10,"size":10,"orderBy":"create_time desc","startRow":1,"endRow":10,"total":10,"pages":1,
// "list":[{"orderId":20100365271006,"itemNum":10,"totalPrice":0.10,"actualPrice":0.01,"pid":75,"merchantId":71,"status":2,
// "prdType":0,"payMethod":0,"title":"app核销套餐","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/07/18/V78XBEKUUxVJKGW5eOa1bmEXA.gif",
// "createTime":1500361270000,"payTime":1500361278000,"contactName":"辰希童童","contactPhone":"13882159573","consumCode":23069524,
// "usedTime":1500364665000,"comment":false},{"orderId":90149164721497,"itemNum":1,"totalPrice":38.00,"actualPrice":0.00,"pid":23,
// "merchantId":71,"status":2,"prdType":0,"payMethod":3,"title":"懒人特色4杯试饮",
// "thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg","createTime":1499161724000,
// "payTime":1499161729000,"contactName":"辰希童童","contactPhone":"13882159573","consumCode":58720656,"usedTime":1499161771000,
// "comment":false},{"orderId":30859074711198,"itemNum":1,"totalPrice":38.00,"actualPrice":0.00,"pid":23,"merchantId":71,"status":2,
// "prdType":0,"payMethod":3,"title":"懒人特色4杯试饮",
// "thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg","createTime":1499078715000,
// "payTime":1499078719000,"contactName":"辰希童童","contactPhone":"13882159573","consumCode":96470985,"usedTime":1499078771000,
// "comment":false},
// {"orderId":20779074891790,"itemNum":1,"totalPrice":38.00,"actualPrice":0.00,"pid":23,"merchantId":71,"status":2,"prdType":0,"payMethod":3,
// "title":"懒人特色4杯试饮","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg",
// "createTime":1499077897000,"payTime":1499077903000,"contactName":"希希辰","contactPhone":"13882159573","consumCode":33554439,
// "usedTime":1499077937000,"comment":false},{"orderId":70739074341996,"itemNum":1,"totalPrice":38.00,"actualPrice":0.00,"pid":23,
// "merchantId":71,"status":2,"prdType":0,"payMethod":3,"title":"懒人特色4杯试饮",
// "thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg","createTime":1499077344000,
// "payTime":1499077354000,"contactName":"希希辰","contactPhone":"13882159573","consumCode":83887879,"usedTime":1499077403000,
// "comment":false},{"orderId":30658804171499,"itemNum":1,"totalPrice":38.00,"actualPrice":0.00,"pid":23,"merchantId":71,"status":2,"prdType":0,
// "payMethod":3,"title":"懒人特色4杯试饮","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg",
// "createTime":1498806175000,"payTime":1498806177000,"contactName":"辰希童童","contactPhone":"13882159573","consumCode":31458277,
// "usedTime":1498815197000,"comment":false},{"orderId":30473304671193,"itemNum":1,"totalPrice":198.00,"actualPrice":198.00,"pid":21,
// "merchantId":71,"status":2,"prdType":0,"payMethod":1,"title":"懒人酒桶套餐",
// "thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/ry9s7AayeZaY2GmhtoFRMDQ1M.jpg","createTime":1493304677000,
// "payTime":1493304692000,"contactName":"jinz","contactPhone":"15810518409","consumCode":10486089,"usedTime":1493304893000,
// "comment":false},{"orderId":80132094451391,"itemNum":1,"totalPrice":258.00,"actualPrice":258.00,"pid":22,"merchantId":71,
// "status":2,"prdType":0,"payMethod":1,"title":"懒人酒桶套餐2⃣️","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/cDc10zsX6YQeInnnvElYgWIUn.jpg","createTime":1492091453000,"payTime":1492091467000,"contactName":"蔡震霖","contactPhone":"13987677033","consumCode":62915083,"usedTime":1492091563000,"comment":false},{"orderId":60832084661292,"itemNum":1,"totalPrice":38.00,"actualPrice":38.00,"pid":23,"merchantId":71,"status":2,"prdType":0,"payMethod":0,"title":"懒人特色4杯试饮","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg","createTime":1492088663000,"payTime":1492088678000,"contactName":"蔡震霖","contactPhone":"13987677033","consumCode":48235307,"usedTime":1492088888000,"comment"
//        D/OkHttp: ":2,"prdType":0,"payMethod":1,"title":"懒人特色4杯试饮","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/04/05/dy6SFBvKVRafWg1TR1NAGcSux.jpg","createTime":1492087543000,"payTime":1492087566000,"contactName":"蔡震霖","contactPhone":"13987677033","consumCode":77596011,"usedTime":1492088906000,"comment":false}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]},"errorCode":null,"errorDesc":null,"elapsedMilliseconds":30,"success":true}
//        D/OkHttp: <-- END HTTP (4755-byte body)

//    {"orderId":20100365271006,"itemNum":10,"totalPrice":0.10,"actualPrice":0.01,"pid":75,"merchantId":71,"status":2,
// "prdType":0,"payMethod":0,"title":"app核销套餐","thumb":"http://more-info.oss-cn-beijing.aliyuncs.com/2017/07/18/V78XBEKUUxVJKGW5eOa1bmEXA.gif",
// "createTime":1500361270000,"payTime":1500361278000,"contactName":"辰希童童","contactPhone":"13882159573","consumCode":23069524,
// "usedTime":1500364665000,"comment":false}

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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

    public int getPrdType() {
        return prdType;
    }

    public void setPrdType(int prdType) {
        this.prdType = prdType;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getConsumCode() {
        return consumCode;
    }

    public void setConsumCode(String consumCode) {
        this.consumCode = consumCode;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    private long orderId;
    private int itemNum;
    private float totalPrice;
    private float actualPrice;
    private int pid;
    private int merchantId;
    private int status;
    private int prdType;
    private int payMethod;
    private String title;
    private String thumb;
    private long createTime;
    private long payTime;
    private String contactName;
    private String contactPhone;
    private String consumCode;
    private long usedTime;
    private boolean comment;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    private float price;
    private String packName;



}
