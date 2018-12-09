package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017/2/23.
 */

public class BizArea {
    private Integer  busId;
    private String  busName;
    private Integer  busType;
    private String  city;
    private String  thumb;

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public Integer getBusType() {
        return busType;
    }

    public void setBusType(Integer busType) {
        this.busType = busType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "BizArea{" +
                "busId=" + busId +
                ", busName='" + busName + '\'' +
                ", busType=" + busType +
                ", city='" + city + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
