package com.moreclub.moreapp.ucenter.model;

import java.util.List;

/**
 * Created by Administrator on 2017-08-26-0026.
 */

public class UserGoodLike {

    /**
     * count : 2
     * userLikeResps : [{"uid":1509741800836,"thumb":"http://more-user.oss-cn-beijing.aliyuncs.com/2017/8/10/02017081052072.jpg","nickName":"王志龙"},{"uid":1027888850308,"thumb":"http://wx.qlogo.cn/mmopen/R5SpPlkkibibIXOCr3vy2G1415C1uIzNqFBJtd3RE0rNQvvKlnsb2n99v7Csb51tdVMeBk4Kkre8YnVtZJZMyNkP29UK2Wu5EE/0","nickName":"陈鹏"}]
     */

    private int count;
    private int pages;
    private List<UserGoodLike.UserLikeRespsBean> userLikeResps;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserGoodLike.UserLikeRespsBean> getUserLikeResps() {
        return userLikeResps;
    }

    public void setUserLikeResps(List<UserGoodLike.UserLikeRespsBean> userLikeResps) {
        this.userLikeResps = userLikeResps;
    }

    public static class UserLikeRespsBean {
        /**
         * uid : 1509741800836
         * thumb : http://more-user.oss-cn-beijing.aliyuncs.com/2017/8/10/02017081052072.jpg
         * nickName : 王志龙
         */

        private long uid;
        private String thumb;
        private String nickName;

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}

