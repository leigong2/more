package com.moreclub.moreapp.information.model;

import java.util.List;

/**
 * Created by Administrator on 2017-05-25.
 */

public class ActivityDetail {

    /**
     * activityId : 0
     * applyStatus : 0
     * currFemale : 0
     * currMale : 0
     * mainComm : [{"activityId":0,"comment":"string","commentId":0,"createTime":{"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0},"likeTimes":0,"parentId":0,"read":true,"replyNum":0,"replyUid":0,"uid":0,"userName":"string","userThumb":"string"}]
     * maxFemale : 0
     * maxMale : 0
     * mid : 0
     * participants : [{"nickname":"string","sex":"string","thumb":"string","uid":0}]
     * price : 0
     * question : string
     * room : string
     * sign : true
     * type : 0
     */

    private int activityId;
    private int applyStatus;
    private int currFemale;
    private int currMale;
    private int maxFemale;
    private int maxMale;
    private int mid;
    private int price;
    private String question;
    private String room;
    private boolean sign;
    private int type;
    private List<MainCommBean> mainComm;
    private List<ParticipantsBean> participants;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(int applyStatus) {
        this.applyStatus = applyStatus;
    }

    public int getCurrFemale() {
        return currFemale;
    }

    public void setCurrFemale(int currFemale) {
        this.currFemale = currFemale;
    }

    public int getCurrMale() {
        return currMale;
    }

    public void setCurrMale(int currMale) {
        this.currMale = currMale;
    }

    public int getMaxFemale() {
        return maxFemale;
    }

    public void setMaxFemale(int maxFemale) {
        this.maxFemale = maxFemale;
    }

    public int getMaxMale() {
        return maxMale;
    }

    public void setMaxMale(int maxMale) {
        this.maxMale = maxMale;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<MainCommBean> getMainComm() {
        return mainComm;
    }

    public void setMainComm(List<MainCommBean> mainComm) {
        this.mainComm = mainComm;
    }

    public List<ParticipantsBean> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantsBean> participants) {
        this.participants = participants;
    }

    public static class MainCommBean {

        @Override
        public String toString() {
            return "MainCommBean{" +
                    "activityId=" + activityId +
                    ", comment='" + comment + '\'' +
                    ", commentId=" + commentId +
                    ", createTime=" + createTime +
                    ", likeTimes=" + likeTimes +
                    ", parentId=" + parentId +
                    ", read=" + read +
                    ", replyNum=" + replyNum +
                    ", replyUid=" + replyUid +
                    ", uid=" + uid +
                    ", userName='" + userName + '\'' +
                    ", userThumb='" + userThumb + '\'' +
                    '}';
        }

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
        private long createTime;
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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
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

        public static class CreateTimeBean {
            @Override
            public String toString() {
                return "CreateTimeBean{" +
                        "date=" + date +
                        ", day=" + day +
                        ", hours=" + hours +
                        ", minutes=" + minutes +
                        ", month=" + month +
                        ", nanos=" + nanos +
                        ", seconds=" + seconds +
                        ", time=" + time +
                        ", timezoneOffset=" + timezoneOffset +
                        ", year=" + year +
                        '}';
            }

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

    public static class ParticipantsBean {
        @Override
        public String toString() {
            return "ParticipantsBean{" +
                    "nickname='" + nickname + '\'' +
                    ", sex='" + sex + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", uid=" + uid +
                    '}';
        }

        /**
         * nickname : string
         * sex : string
         * thumb : string
         * uid : 0
         */


        private String nickname;
        private String sex;
        private String thumb;
        private int uid;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }

    @Override
    public String toString() {
        return "ActivityDetail{" +
                "activityId=" + activityId +
                ", applyStatus=" + applyStatus +
                ", currFemale=" + currFemale +
                ", currMale=" + currMale +
                ", maxFemale=" + maxFemale +
                ", maxMale=" + maxMale +
                ", mid=" + mid +
                ", price=" + price +
                ", question='" + question + '\'' +
                ", room='" + room + '\'' +
                ", sign=" + sign +
                ", type=" + type +
                ", mainComm=" + mainComm +
                ", participants=" + participants +
                '}';
    }
}
