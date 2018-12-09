package com.moreclub.moreapp.information.model;

import java.util.List;

/**
 * Created by Administrator on 2017-06-06.
 */

public class MessageWallReply {

    /**
     * mainComm : {"activityId":0,"comment":"string","commentId":0,"createTime":{"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0},"likeTimes":0,"parentId":0,"read":true,"replyNum":0,"replyUid":0,"uid":0,"userName":"string","userThumb":"string"}
     * replies : [{"activityId":0,"comment":"string","commentId":0,"createTime":{"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0},"likeTimes":0,"parentId":0,"read":true,"replyUid":0,"replyUserName":"string","replyuserThumb":"string","uid":0,"userName":"string","userThumb":"string"}]
     */

    private MainCommBean mainComm;
    private List<RepliesBean> replies;

    public MainCommBean getMainComm() {
        return mainComm;
    }

    public void setMainComm(MainCommBean mainComm) {
        this.mainComm = mainComm;
    }

    public List<RepliesBean> getReplies() {
        return replies;
    }

    public void setReplies(List<RepliesBean> replies) {
        this.replies = replies;
    }

    public static class MainCommBean {
        /**
         * activityId : 0
         * comment : string
         * commentId : 0
         * createTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
         * likeTimes : 0
         * parentId : 0
         * read : true
         * replyNum : 0
         * replyUid : 0
         * uid : 0
         * userName : string
         * userThumb : string
         */

        private int activityId;
        private String comment;
        private int commentId;
        private Long createTime;
        private int likeTimes;
        private int parentId;
        private boolean read;
        private int replyNum;
        private int replyUid;
        private Long uid;
        private String userName;
        private String userThumb;

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public int getLikeTimes() {
            return likeTimes;
        }

        public void setLikeTimes(int likeTimes) {
            this.likeTimes = likeTimes;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public int getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(int replyNum) {
            this.replyNum = replyNum;
        }

        public int getReplyUid() {
            return replyUid;
        }

        public void setReplyUid(int replyUid) {
            this.replyUid = replyUid;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserThumb() {
            return userThumb;
        }

        public void setUserThumb(String userThumb) {
            this.userThumb = userThumb;
        }
    }

    public static class RepliesBean {
        /**
         * activityId : 0
         * comment : string
         * commentId : 0
         * createTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
         * likeTimes : 0
         * parentId : 0
         * read : true
         * replyUid : 0
         * replyUserName : string
         * replyuserThumb : string
         * uid : 0
         * userName : string
         * userThumb : string
         */

        private int activityId;
        private String comment;
        private int commentId;
        private Long createTime;
        private int likeTimes;
        private int parentId;
        private boolean read;
        private Long replyUid;
        private String replyUserName;
        private String replyuserThumb;
        private Long uid;
        private String userName;
        private String userThumb;

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public int getLikeTimes() {
            return likeTimes;
        }

        public void setLikeTimes(int likeTimes) {
            this.likeTimes = likeTimes;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public Long getReplyUid() {
            return replyUid;
        }

        public void setReplyUid(Long replyUid) {
            this.replyUid = replyUid;
        }

        public String getReplyUserName() {
            return replyUserName;
        }

        public void setReplyUserName(String replyUserName) {
            this.replyUserName = replyUserName;
        }

        public String getReplyuserThumb() {
            return replyuserThumb;
        }

        public void setReplyuserThumb(String replyuserThumb) {
            this.replyuserThumb = replyuserThumb;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserThumb() {
            return userThumb;
        }

        public void setUserThumb(String userThumb) {
            this.userThumb = userThumb;
        }

        public static class CreateTimeBeanX {
            /**
             * date : 0
             * day : 0
             * hours : 0
             * minutes : 0
             * month : 0
             * nanos : 0
             * seconds : 0
             * time : 0
             * timezoneOffset : 0
             * year : 0
             */

            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private int time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
