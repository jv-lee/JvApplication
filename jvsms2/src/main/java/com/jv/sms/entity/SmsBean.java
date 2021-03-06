package com.jv.sms.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsBean implements Serializable {

    private int noId;
    private String id;
    private String thread_id;
    private String name;
    private String phoneNumber;
    private String smsBody;
    private String date;
    private boolean isShowDate = false;
    private Type type;
    private ReadType readType;
    private int colorPosition;
    private int status;
    public String selectStr = "";

    public enum ReadType {
        IS_READ, NOT_READ
    }

    public enum Type {
        SEND, RECEIVE, DRAFT
    }

    public SmsBean() {
    }

    public SmsBean(int noId, String id) {
        this.noId = noId;
        this.id = id;
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

    public SmsBean(String id, String name, String phoneNumber, String smsBody, String date, Type type, String thread_id, ReadType readType, int status) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsBody = smsBody;
        this.date = date;
        this.type = type;
        this.thread_id = thread_id;
        this.readType = readType;
        this.status = status;
//        this.colorPosition = (int) (Math.random() * 9 );
        this.colorPosition = Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1)) == 9 ? 0 : Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1));
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

    public int getColorPosition() {
        return colorPosition;
    }

    public void setColorPosition(int colorPosition) {
        this.colorPosition = colorPosition;
    }

    public int getNoId() {
        return noId;
    }

    public void setNoId(int noId) {
        this.noId = noId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
