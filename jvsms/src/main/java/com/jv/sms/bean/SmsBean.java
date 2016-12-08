package com.jv.sms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsBean implements Serializable {
    private String thread_id;

    public List<Sms> listSms;

    public SmsBean() {
    }

    public SmsBean(String thread_id, List<Sms> listSms) {
        this.thread_id = thread_id;
        this.listSms = listSms;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public List<Sms> getListSms() {
        return listSms;
    }

    public void setListSms(List<Sms> listSms) {
        this.listSms = listSms;
    }

    public static class Sms implements Serializable {

        private String id;
        private String name;
        private String phoneNumber;
        private String smsBody;
        private String date;
        private boolean isShowDate = false;
        private Type type;

        @Override
        public String toString() {
            return "Sms{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", smsBody='" + smsBody + '\'' +
                    ", date='" + date + '\'' +
                    ", isShowDate=" + isShowDate +
                    ", type=" + type +
                    '}';
        }

        public enum Type {
            SEND, RECEIVE, DRAFT
        }

        public boolean getShowDate() {
            return isShowDate;
        }

        public void setShowDate(boolean showDate) {
            isShowDate = showDate;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSmsBody() {
            return smsBody;
        }

        public void setSmsBody(String smsBody) {
            this.smsBody = smsBody;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


        public Sms() {
        }

        public Sms(String id, String name, String phoneNumber, String smsBody, String date, Type type) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.smsBody = smsBody;
            this.date = date;
            this.type = type;
        }
    }

}
