package com.jv.sms.entity;

/**
 * Created by Administrator on 2016/12/23.
 */

public class LinkmanBean {

    private String name;
    private String word;
    private String phoneNumber;
    private int colorType;
    private String thread_id;

    public LinkmanBean() {
    }

    public LinkmanBean(String name, String type, String phoneNumber, int colorType) {
        this.name = name;
        this.word = type;
        this.phoneNumber = phoneNumber;
        this.colorType = colorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

}
