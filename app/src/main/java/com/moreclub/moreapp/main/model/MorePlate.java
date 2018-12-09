package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017-09-13-0013.
 */

public class MorePlate {

    /**
     * activitysSimple : {"activiteNewDay":0,"activiteNewWeek":0}
     * saleSimple : {"saleNewCard":0,"saleNewShops":0}
     * signSimple : {"content":"string","nickname":"string","sininCount":0,"userAvatar":"string"}
     */

    private ActivitysSimpleBean activitysSimple;
    private SaleSimpleBean saleSimple;
    private SignSimpleBean signSimple;

    public ActivitysSimpleBean getActivitysSimple() {
        return activitysSimple;
    }

    public void setActivitysSimple(ActivitysSimpleBean activitysSimple) {
        this.activitysSimple = activitysSimple;
    }

    public SaleSimpleBean getSaleSimple() {
        return saleSimple;
    }

    public void setSaleSimple(SaleSimpleBean saleSimple) {
        this.saleSimple = saleSimple;
    }

    public SignSimpleBean getSignSimple() {
        return signSimple;
    }

    public void setSignSimple(SignSimpleBean signSimple) {
        this.signSimple = signSimple;
    }

    public static class ActivitysSimpleBean {
        /**
         * activiteNewDay : 0
         * activiteNewWeek : 0
         */

        private int activiteNewDay;
        private int activiteNewWeek;

        public int getActiviteNewDay() {
            return activiteNewDay;
        }

        public void setActiviteNewDay(int activiteNewDay) {
            this.activiteNewDay = activiteNewDay;
        }

        public int getActiviteNewWeek() {
            return activiteNewWeek;
        }

        public void setActiviteNewWeek(int activiteNewWeek) {
            this.activiteNewWeek = activiteNewWeek;
        }
    }

    public static class SaleSimpleBean {
        /**
         * saleNewCard : 0
         * saleNewShops : 0
         */

        private int saleNewCard;
        private int saleNewShops;

        public int getSaleNewCard() {
            return saleNewCard;
        }

        public void setSaleNewCard(int saleNewCard) {
            this.saleNewCard = saleNewCard;
        }

        public int getSaleNewShops() {
            return saleNewShops;
        }

        public void setSaleNewShops(int saleNewShops) {
            this.saleNewShops = saleNewShops;
        }
    }

    public static class SignSimpleBean {
        /**
         * content : string
         * nickname : string
         * sininCount : 0
         * userAvatar : string
         */

        private int sininCount;
        private List<SignUser> simpleDtoList;

        public int getSininCount() {
            return sininCount;
        }

        public void setSininCount(int sininCount) {
            this.sininCount = sininCount;
        }

        public List<SignUser> getSimpleDtoList() {
            return simpleDtoList;
        }

        public void setSimpleDtoList(List<SignUser> simpleDtoList) {
            this.simpleDtoList = simpleDtoList;
        }

        public static class SignUser {

            private String content;
            private String nickname;
            private String userAvatar;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUserAvatar() {
                return userAvatar;
            }

            public void setUserAvatar(String userAvatar) {
                this.userAvatar = userAvatar;
            }
        }
    }
}
