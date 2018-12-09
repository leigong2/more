package com.moreclub.moreapp.util;

import android.content.Context;
import android.text.TextUtils;

import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.ucenter.constant.Constants;

/**
 * Created by Captain on 2017/3/7.
 */

public class MoreUser {
    private double userLocationLat;
    private double userLocationLng;
    private String city;
    private String access_token;
    private String birthday;
    private double expires_in;
    private String nickname;
    private String refresh_token;
    private double registTime;
    private String scope;
    private int sex;
    private String thdThumb;
    private String thumb;
    private String token_type;
    private Long uid;
    private String personalMark;
    private String locationCity;
    private int fellowerCount;
    private int followCount;
    private String brandPrefer;
    private String bookPrefer;
    private String foodPrefer;
    private String hobby;
    private String winePrefer;
    private String careerName;
    private int careerID;

    public int getCareerID() {
        return careerID;
    }

    public void setCareerID(int careerID) {
        this.careerID = careerID;
    }

    public String getBrandPrefer() {
        return brandPrefer;
    }

    public void setBrandPrefer(String brandPrefer) {
        this.brandPrefer = brandPrefer;
    }

    public String getBookPrefer() {
        return bookPrefer;
    }

    public void setBookPrefer(String bookPrefer) {
        this.bookPrefer = bookPrefer;
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

    public String getWinePrefer() {
        return winePrefer;
    }

    public void setWinePrefer(String winePrefer) {
        this.winePrefer = winePrefer;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String career) {
        this.careerName = career;
    }

    public boolean isShowCouponDialog() {
        return isShowCouponDialog;
    }

    public void setShowCouponDialog(boolean showCouponDialog) {
        isShowCouponDialog = showCouponDialog;
    }

    private boolean isShowCouponDialog;

    public boolean isAutoSignSuccess() {
        return autoSignSuccess;
    }

    public void setAutoSignSuccess(boolean autoSignSuccess) {
        this.autoSignSuccess = autoSignSuccess;
    }

    public boolean isManualSignSuccess() {
        return manualSignSuccess;
    }

    public void setManualSignSuccess(boolean manualSignSuccess) {
        this.manualSignSuccess = manualSignSuccess;
    }

    private boolean autoSignSuccess;
    private boolean manualSignSuccess;

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    private int cardLevel;

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    private String UserPhone;
    private boolean closePush;

    public boolean isClosePush() {
        return closePush;
    }

    public void setClosePush(boolean closePush) {
        this.closePush = closePush;
    }

    public String getPayAppID() {
        return payAppID;
    }

    public void setPayAppID(String payAppID) {
        this.payAppID = payAppID;
    }

    private String payAppID;

    public String getPersonalMark() {
        return personalMark;
    }

    public void setPersonalMark(String personalMark) {
        this.personalMark = personalMark;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(double expires_in) {
        this.expires_in = expires_in;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public double getRegistTime() {
        return registTime;
    }

    public void setRegistTime(double registTime) {
        this.registTime = registTime;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getThdThumb() {
        return thdThumb;
    }

    public void setThdThumb(String thdThumb) {
        this.thdThumb = thdThumb;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getFellowerCount() {
        return fellowerCount;
    }

    public void setFellowerCount(int fellowerCount) {
        this.fellowerCount = fellowerCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public static void setInstance(MoreUser instance) {
        MoreUser.instance = instance;
    }

    private static MoreUser instance = new MoreUser();

    public static MoreUser getInstance() {
        return instance;
    }

    private MoreUser() {

    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public double getUserLocationLat() {
        return userLocationLat;
    }

    public void setUserLocationLat(double userLocationLat) {
        this.userLocationLat = userLocationLat;

    }

    public double getUserLocationLng() {
        return userLocationLng;
    }

    public void setUserLocationLng(double userLocationLng) {
        this.userLocationLng = userLocationLng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void clearAccount(Context context) {
        try {
            setCity("");
            setAccess_token("");
            setBirthday("");
            setNickname("");
            setRefresh_token("");
            setThumb("");
            setThdThumb("");
            setToken_type("");
            setUid(new Long(0));
            setUserPhone("");
            setBrandPrefer("");
            setBookPrefer("");
            setFoodPrefer("");
            setHobby("");
            setWinePrefer("");
            setCareerName("");
            setCareerID(0);
            setClosePush(false);

            PrefsUtils.setString(context, Constants.KEY_BRAND_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_BOOK_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_FOOD_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_HOBBY, "");
            PrefsUtils.setString(context, Constants.KEY_WINE_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_CAREER_NAME, "");
            PrefsUtils.setString(context, Constants.KEY_CITY, "");
            PrefsUtils.setString(context, Constants.KEY_ACCESS_TOKEN, "");
            PrefsUtils.setString(context, Constants.KEY_REFRESH_TOKEN, "");
            PrefsUtils.setString(context, Constants.KEY_BIRTHDAY, "");
            PrefsUtils.setString(context, Constants.KEY_NICKNAME, "");
            PrefsUtils.setString(context, Constants.KEY_THIRD_AVATAR, "");
            PrefsUtils.setString(context, Constants.KEY_AVATAR, "");
            PrefsUtils.setString(context, Constants.KEY_TYPE_TOKEN, "");
            PrefsUtils.setString(context, Constants.KEY_PHONE_USER, "");
            PrefsUtils.setLong(context, Constants.KEY_UID, 0);
            PrefsUtils.setBoolean(context, Constants.KEY_AUTO_SIGN_POP, false);
            PrefsUtils.setInt(context, Constants.KEY_CARD_LEVEL, 0);
            PrefsUtils.setInt(context, Constants.KEY_CAREER_ID, 0);
            PrefsUtils.setString(context, Constants.KEY_BRAND_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_BOOK_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_FOOD_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_HOBBY, "");
            PrefsUtils.setString(context, Constants.KEY_WINE_PREFER, "");
            PrefsUtils.setString(context, Constants.KEY_CAREER_NAME, "");
            PrefsUtils.setString(context, Constants.KEY_BIND_NUMBER, "");
            PrefsUtils.setBoolean(context, Constants.KEY_CLOSE_PUSH, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAccount(Context context) {
        try {
            PrefsUtils.getEditor(context).putLong(Constants.KEY_UID, uid)
                    .putString(Constants.KEY_ACCESS_TOKEN, access_token)
                    .putString(Constants.KEY_REFRESH_TOKEN, refresh_token)
                    .putString(Constants.KEY_AVATAR, thumb)
                    .putString(Constants.KEY_THIRD_AVATAR, thdThumb)
                    .putString(Constants.KEY_BIRTHDAY, birthday)
                    .putString(Constants.KEY_NICKNAME, nickname)
                    .putString(Constants.KEY_TYPE_TOKEN, token_type)
                    .putInt(Constants.KEY_GENDER, sex)
                    .putString(Constants.KEY_CITY, city)
                    .putString(Constants.KEY_LOCATION_LAT, String.valueOf(userLocationLat))
                    .putString(Constants.KEY_LOCATION_LNG, String.valueOf(userLocationLng))
                    .putString(Constants.KEY_PHONE_USER, UserPhone)
                    .putInt(Constants.KEY_CARD_LEVEL, cardLevel)
                    .putBoolean(Constants.KEY_AUTO_SIGN_POP, autoSignSuccess)
                    .putInt(Constants.KEY_CAREER_ID, careerID)
                    .putString(Constants.KEY_BRAND_PREFER, brandPrefer)
                    .putString(Constants.KEY_BOOK_PREFER, bookPrefer)
                    .putString(Constants.KEY_FOOD_PREFER, foodPrefer)
                    .putString(Constants.KEY_HOBBY, hobby)
                    .putString(Constants.KEY_WINE_PREFER, winePrefer)
                    .putString(Constants.KEY_CAREER_NAME, careerName)
                    .putString(Constants.KEY_BIND_NUMBER, UserPhone)
                    .putBoolean(Constants.KEY_CLOSE_PUSH, closePush)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initAccount(Context context) {
        try {
            String latS = PrefsUtils.getString(context, Constants.KEY_LOCATION_LAT, "0");
            userLocationLat = Double.valueOf(latS);
            String lngS = PrefsUtils.getString(context, Constants.KEY_LOCATION_LNG, "0");
            userLocationLng = Double.valueOf(lngS);
            city = PrefsUtils.getString(context, Constants.KEY_CITY, "");
            access_token = PrefsUtils.getString(context, Constants.KEY_ACCESS_TOKEN, "");
            birthday = PrefsUtils.getString(context, Constants.KEY_BIRTHDAY, "");
            nickname = PrefsUtils.getString(context, Constants.KEY_NICKNAME, "");
            refresh_token = PrefsUtils.getString(context, Constants.KEY_REFRESH_TOKEN, "");
            sex = PrefsUtils.getInt(context, Constants.KEY_GENDER, 0);
            thdThumb = PrefsUtils.getString(context, Constants.KEY_THIRD_AVATAR, "");
            thumb = PrefsUtils.getString(context, Constants.KEY_AVATAR, "");
            token_type = PrefsUtils.getString(context, Constants.KEY_TYPE_TOKEN, "");
            uid = PrefsUtils.getLong(context, Constants.KEY_UID, 0);
            UserPhone = PrefsUtils.getString(context, Constants.KEY_PHONE_USER, "");
            cardLevel = PrefsUtils.getInt(context, Constants.KEY_CARD_LEVEL, 0);
            autoSignSuccess = PrefsUtils.getBoolean(context, Constants.KEY_AUTO_SIGN_POP, false);
            careerID = PrefsUtils.getInt(context, Constants.KEY_CAREER_ID, 0);
            brandPrefer = PrefsUtils.getString(context, Constants.KEY_BRAND_PREFER, "");
            bookPrefer = PrefsUtils.getString(context, Constants.KEY_BOOK_PREFER, "");
            foodPrefer = PrefsUtils.getString(context, Constants.KEY_FOOD_PREFER, "");
            hobby = PrefsUtils.getString(context, Constants.KEY_HOBBY, "");
            winePrefer = PrefsUtils.getString(context, Constants.KEY_WINE_PREFER, "");
            careerName = PrefsUtils.getString(context, Constants.KEY_CAREER_NAME, "");
            closePush = PrefsUtils.getBoolean(context, Constants.KEY_CLOSE_PUSH, false);
            if (TextUtils.isEmpty(UserPhone))
                UserPhone = PrefsUtils.getString(context, Constants.KEY_BIND_NUMBER, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLogin() {
        if (uid == null || uid == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "MoreUser{" +
                "userLocationLat=" + userLocationLat +
                ", userLocationLng=" + userLocationLng +
                ", city='" + city + '\'' +
                ", access_token='" + access_token + '\'' +
                ", birthday='" + birthday + '\'' +
                ", expires_in=" + expires_in +
                ", nickname='" + nickname + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", registTime=" + registTime +
                ", scope='" + scope + '\'' +
                ", sex=" + sex +
                ", thdThumb='" + thdThumb + '\'' +
                ", thumb='" + thumb + '\'' +
                ", token_type='" + token_type + '\'' +
                ", uid=" + uid +
                ", personalMark='" + personalMark + '\'' +
                ", locationCity='" + locationCity + '\'' +
                ", fellowerCount=" + fellowerCount +
                ", followCount=" + followCount +
                ", brandPrefer='" + brandPrefer + '\'' +
                ", bookPrefer='" + bookPrefer + '\'' +
                ", foodPrefer='" + foodPrefer + '\'' +
                ", hobby='" + hobby + '\'' +
                ", winePrefer='" + winePrefer + '\'' +
                ", careerName='" + careerName + '\'' +
                ", careerID=" + careerID +
                ", isShowCouponDialog=" + isShowCouponDialog +
                ", autoSignSuccess=" + autoSignSuccess +
                ", manualSignSuccess=" + manualSignSuccess +
                ", cardLevel=" + cardLevel +
                ", UserPhone='" + UserPhone + '\'' +
                ", payAppID='" + payAppID + '\'' +
                '}';
    }
}
