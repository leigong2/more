package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-08-14-0014.
 */

public class UpLoadCityInfo {

    /**
     * city : string
     * country : string
     * region : string
     * uid : 0
     */

    private String city;
    private String country;
    private String region;
    private long uid;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
