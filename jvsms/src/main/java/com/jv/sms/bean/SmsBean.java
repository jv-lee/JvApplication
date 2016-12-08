package com.jv.sms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsBean implements Serializable {

    private String id;
    private String thread_id;
    private String name;
    private String phoneNumber;
    private String smsBody;
    private String date;
    private boolean isShowDate = false;
    private Type type;
    private ReadType readType;

    public enum ReadType {
        isRead, notRead
    }

    public enum Type {
        SEND, RECEIVE, DRAFT
    }

    public SmsBean() {
    }

    public SmsBean(String id, String thread_id, String name, String phoneNumber, String smsBody, String date, boolean isShowDate, Type type, ReadType readType) {
        this.id = id;
        this.thread_id = thread_id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsBody = smsBody;
        this.date = date;
        this.isShowDate = isShowDate;
        this.type = type;
        this.readType = readType;
    }

    public SmsBean(String id, String name, String phoneNumber, String smsBody, String date, Type type, String thread_id, ReadType readType) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsBody = smsBody;
        this.date = date;
        this.type = type;
        this.thread_id = thread_id;
        this.readType = readType;
    }

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


    public boolean isShowDate() {
        return isShowDate;
    }

    public ReadType getReadType() {
        return readType;
    }

    public void setReadType(ReadType readType) {
        this.readType = readType;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
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


}
