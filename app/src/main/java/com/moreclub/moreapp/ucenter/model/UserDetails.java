package com.moreclub.moreapp.ucenter.model;

import com.moreclub.moreapp.information.model.ActivityDetail;
import com.moreclub.moreapp.information.model.ActivityIntros;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 2017/5/2.
 */

public class UserDetails {

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

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public String getFoodPrefer() {
        return foodPrefer;
    }

    public void setFoodPrefer(String foodPrefer) {
        this.foodPrefer = foodPrefer;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean getHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean hasFollow) {
        this.hasFollow = hasFollow;
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

    public int getParties() {
        return parties;
    }

    public void setParties(int parties) {
        this.parties = parties;
    }

    public String getPersonalMark() {
        return personalMark;
    }

    public void setPersonalMark(String personalMark) {
        this.personalMark = personalMark;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSecuritySubject() {
        return securitySubject;
    }

    public void setSecuritySubject(String securitySubject) {
        this.securitySubject = securitySubject;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getWinePrefer() {
        return winePrefer;
    }

    public void setWinePrefer(String winePrefer) {
        this.winePrefer = winePrefer;
    }

    public ArrayList<ActivityIntros> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivityIntros> activities) {
        this.activities = activities;
    }

    private String birthday;
    private String bookPrefer;
    private String brandPrefer;
    private int career;
    private String careerName;
    private String city;
    private String country;
    private int follow;
    private int follower;
    private String foodPrefer;
    private int grade;
    private boolean hasFollow;
    private String hobby;
    private String nickName;
    private int parties;
    private String personalMark;
    private String province;
    private String securitySubject;
    private String sex;
    private String tags;
    private String thumb;
    private int type;
    private long uid;
    private String winePrefer;
    private ArrayList<ActivityIntros> activities;
    private int ugcCount;
    private List<UserDetailsV2.UgcSimpledtosBean> ugcSimpleDtos;

    public int getUgcCount() {
        return ugcCount;
    }

    public void setUgcCount(int ugcCount) {
        this.ugcCount = ugcCount;
    }

    public List<UserDetailsV2.UgcSimpledtosBean> getUgcSimpleDtos() {
        return ugcSimpleDtos;
    }

    public void setUgcSimpleDtos(List<UserDetailsV2.UgcSimpledtosBean> ugcSimpleDtos) {
        this.ugcSimpleDtos = ugcSimpleDtos;
    }

    //    {
//        activities = "<null>";
//        birthday = "1990-10-31";
//        bookPrefer = "\U7384\U5b66 \U4e09\U4f53 \U9c81\U8fc5";
//        brandPrefer = "LANCOME hm \U5361\U5730\U4e9a \U4f8b\U5916";
//        career = 1;
//        careerName = "\U4e92\U8054\U7f51";
//        city = "\U65b0\U7af9\U53bf";
//        country = CN;
//        follow = 0;
//        follower = 8;
//        foodPrefer = "\U5ddd\U83dc \U65e5\U672c\U6599\U7406 \U725b\U6392";
//        grade = "<null>";
//        hasFollow = 0;
//        hobby = "\U7fbd\U6bdb\U7403 \U6444\U5f71 \U6e38\U6cf3";
//        nickName = "\U5218\U614c\U614c\U4e0d\U614c\U5f20";
//        parties = 0;
//        personalMark = "\U63a2\U5bfb\U751f\U547d\U7684\U5168\U90e8\U53ef\U80fd \U4e0d\U7740\U6025\U6162\U6162\U6765";
//        province = "\U53f0\U6e7e";
//        securitySubject = "<null>";
//        sex = 0;
//        tags = "<null>";
//        thumb = "http://more-user.oss-cn-beijing.aliyuncs.com/2017/03/17/u_3097829803361280_1489753333.jpg";
//        type = 0;
//        uid = 3097829803361280;
//        winePrefer = "\U8f69\U5c3c\U8bd7 \U7cbe\U917f\U5564\U9152 \U767e\U9f84\U575b";
//    }



}
