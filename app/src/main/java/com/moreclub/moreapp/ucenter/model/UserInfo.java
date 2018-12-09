package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Captain on 2017/3/15.
 */

public class UserInfo {

//    {
//        "uid":702091014404, "personalMark":"那就好", "career":null, "hobby":null, "brandPrefer":
//        null, "winePrefer":null, "bookPrefer":null, "foodPrefer":null, "tags":
//        null, "securitySubject":null, "country":"CN", "province":null, "city":"邯郸市", "nickName":
//        "你好", "thumb":
//        "http://img-cn-beijing.aliyuncs.com/2017/3/21/7020910144042017032139114.jpg", "sex":
//        "1", "type":null, "birthday":null, "careerName":null
//    }


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
        return nickName;
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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

    public String getPersonalMark() {
        return personalMark;
    }

    public void setPersonalMark(String personalMark) {
        this.personalMark = personalMark;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSexString() {
        return sexString;
    }

    public void setSexString(String sexString) {
        this.sexString = sexString;
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

    public int getCareerID() {
        return career;
    }

    public void setCareerID(int career) {
        this.career = career;
    }

    public String getFoodPrefer() {
        return foodPrefer;
    }

    public void setFoodPrefer(String foodPrefer) {
        this.foodPrefer = foodPrefer;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChooseCity() {
        return chooseCity;
    }

    public void setChooseCity(String chooseCity) {
        this.chooseCity = chooseCity;
    }

    public String getChooseCityID() {
        return chooseCityID;
    }

    public void setChooseCityID(String chooseCityID) {
        this.chooseCityID = chooseCityID;
    }
    private String access_token;
    private String birthday;
    private double expires_in;
    private String nickName;
    private String refresh_token;
    private double registTime;
    private String scope;
    private String sex;
    private String thdThumb;
    private String thumb;
    private String token_type;
    private Long uid;
    private String personalMark;
    private String city;
    private String sexString;
    private String bookPrefer;
    private String brandPrefer;
    private String careerName;
    private int career;
    private String foodPrefer;
    private String province;
    private String hobby;
    private String winePrefer;
    private int loginType;
    private String phone;
    private String question;
    private String chooseCity;
    private String chooseCityID;

//    user.uid = [userDic valueForKey:@"uid"];
//    user.registTime = [userDic valueForKey:@"registTime"];
//    user.accessToken = [userDic valueForKey:@"access_token"];
//    user.token_type = [userDic valueForKey:@"token_type"];
//    user.refresh_token = [userDic valueForKey:@"refresh_token"];
//    user.thumb = [userDic valueForKey:@"thumb"];
//    user.sexString = [userDic valueForKey:@"sexString"];
//    user.sex = [[userDic valueForKey:@"sex"] integerValue];
//    user.bookPrefer = [userDic valueForKey:@"bookPrefer"];
//    user.brandPrefer = [userDic valueForKey:@"brandPrefer"];
//    user.careerName = [userDic valueForKey:@"careerName"];
//    user.careerID = [[userDic valueForKey:@"careerID"] integerValue];
//    user.foodPrefer = [userDic valueForKey:@"foodPrefer"];
//    user.city = [userDic valueForKey:@"city"];
//    user.provice = [userDic valueForKey:@"province"];
//    user.hobby = [userDic valueForKey:@"hobby"];
//    user.nickName = [userDic valueForKey:@"nickname"];
//    user.personalMark = [userDic valueForKey:@"personalMark"];
//    user.winePrefer = [userDic valueForKey:@"winePrefer"];
//    user.loginType = [[userDic valueForKey:@"loginType"] integerValue];
//    user.phone = [userDic valueForKey:@"phone"];
//    user.question = [userDic valueForKey:@"question"];
//    user.chooseCity = [userDic valueForKey:@"chooseCity"];
//    user.chooseCityID = [userDic valueForKey:@"chooseCityID"];
}
