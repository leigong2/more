package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017-07-18-0018.
 */

public class SignSpaceList {

    /**
     * exist : true
     * ownersid : 0
     * sininMoreSpaces : [{"age":0,"birthday":"2017-07-18T02:57:10.322Z","gender":"string","signInteractionDto":{"age":0,"colseTime":0,"content":"string","deled":true,"exprise":0,"gender":"string","gift":0,"merchantName":"string","mid":0,"nickName":"string","sid":0,"timestamp":0,"type":0,"uid":0,"userThumb":"string"},"signinTime":0,"type":0,"uid":0,"userAvatar":"string","userName":"string"}]
     */

    private boolean exist;
    private int ownersid;
    private List<SininMoreSpacesBean> sininMoreSpaces;
    private SininMoreSpacesBean.SignInteractionDtoBean sininMoreSpace;

    public SininMoreSpacesBean.SignInteractionDtoBean getSininMoreSpace() {
        return sininMoreSpace;
    }

    public void setSininMoreSpace(SininMoreSpacesBean.SignInteractionDtoBean sininMoreSpace) {
        this.sininMoreSpace = sininMoreSpace;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public int getOwnersid() {
        return ownersid;
    }

    public void setOwnersid(int ownersid) {
        this.ownersid = ownersid;
    }

    public List<SininMoreSpacesBean> getSininMoreSpaces() {
        return sininMoreSpaces;
    }

    public void setSininMoreSpaces(List<SininMoreSpacesBean> sininMoreSpaces) {
        this.sininMoreSpaces = sininMoreSpaces;
    }

    public static class SininMoreSpacesBean {
        /**
         * age : 0
         * birthday : 2017-07-18T02:57:10.322Z
         * gender : string
         * signInteractionDto : {"age":0,"colseTime":0,"content":"string","deled":true,"exprise":0,"gender":"string","gift":0,"merchantName":"string","mid":0,"nickName":"string","sid":0,"timestamp":0,"type":0,"uid":0,"userThumb":"string"}
         * signinTime : 0
         * type : 0
         * uid : 0
         * userAvatar : string
         * userName : string
         */

        private int age;
        private String birthday;
        private String gender;
        private SignInteractionDtoBean signInteractionDto;
        private long signinTime;
        private int type;
        private Long uid;
        private int mid;
        private String userAvatar;
        private String userName;
        private String merchantName;

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public SignInteractionDtoBean getSignInteractionDto() {
            return signInteractionDto;
        }

        public void setSignInteractionDto(SignInteractionDtoBean signInteractionDto) {
            this.signInteractionDto = signInteractionDto;
        }

        public long getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(long signinTime) {
            this.signinTime = signinTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public static class SignInteractionDtoBean {
            /**
             * age : 0
             * colseTime : 0
             * content : string
             * deled : true
             * exprise : 0
             * gender : string
             * gift : 0
             * merchantName : string
             * mid : 0
             * nickName : string
             * sid : 0
             * timestamp : 0
             * type : 0
             * uid : 0
             * userThumb : string
             */

            private int age;
            private int colseTime;
            private String content;
            private boolean deled;
            private int exprise;
            private String gender;
            private int gift;
            private String merchantName;
            private int mid;
            private String nickName;
            private int sid;
            private int timestamp;
            private int type;
            private Long uid;
            private String userThumb;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public int getColseTime() {
                return colseTime;
            }

            public void setColseTime(int colseTime) {
                this.colseTime = colseTime;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isDeled() {
                return deled;
            }

            public void setDeled(boolean deled) {
                this.deled = deled;
            }

            public int getExprise() {
                return exprise;
            }

            public void setExprise(int exprise) {
                this.exprise = exprise;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public int getGift() {
                return gift;
            }

            public void setGift(int gift) {
                this.gift = gift;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getSid() {
                return sid;
            }

            public void setSid(int sid) {
                this.sid = sid;
            }

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Long getUid() {
                return uid;
            }

            public void setUid(Long uid) {
                this.uid = uid;
            }

            public String getUserThumb() {
                return userThumb;
            }

            public void setUserThumb(String userThumb) {
                this.userThumb = userThumb;
            }
        }
    }
    public static class SininMoreSpaceBean {
        /**
         * age : 0
         * birthday : 2017-07-18T02:57:10.322Z
         * gender : string
         * signInteractionDto : {"age":0,"colseTime":0,"content":"string","deled":true,"exprise":0,"gender":"string","gift":0,"merchantName":"string","mid":0,"nickName":"string","sid":0,"timestamp":0,"type":0,"uid":0,"userThumb":"string"}
         * signinTime : 0
         * type : 0
         * uid : 0
         * userAvatar : string
         * userName : string
         */

        private int age;
        private String birthday;
        private String gender;
        private SininMoreSpacesBean.SignInteractionDtoBean signInteractionDto;
        private long signinTime;
        private int type;
        private Long uid;
        private int mid;
        private String userAvatar;
        private String userName;
        private String merchantName;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public SininMoreSpacesBean.SignInteractionDtoBean getSignInteractionDto() {
            return signInteractionDto;
        }

        public void setSignInteractionDto(SininMoreSpacesBean.SignInteractionDtoBean signInteractionDto) {
            this.signInteractionDto = signInteractionDto;
        }

        public long getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(long signinTime) {
            this.signinTime = signinTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }
    }
}
