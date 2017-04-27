package com.jv.sms.entity;

/**
 * Created by Administrator on 2017/1/10.
 */

public class SettingBean {

    private String title;
    private String description;
    private boolean isSwitch;
    private boolean hasOp;

    public SettingBean() {
    }

    public SettingBean(String title, String description, boolean isSwitch, boolean hasOp) {
        this.title = title;
        this.description = description;
        this.isSwitch = isSwitch;
        this.hasOp = hasOp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public boolean isHasOp() {
        return hasOp;
    }

    public void setHasOp(boolean hasOp) {
        this.hasOp = hasOp;
    }
}
