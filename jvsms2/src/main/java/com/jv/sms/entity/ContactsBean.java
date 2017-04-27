package com.jv.sms.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public class ContactsBean {

    private String work;
    private List<LinkmanBean> linkmanList;

    public ContactsBean(String work, List<LinkmanBean> linkmanList) {
        this.work = work;
        this.linkmanList = linkmanList;
    }

    public ContactsBean() {
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public List<LinkmanBean> getLinkmanList() {
        return linkmanList;
    }

    public void setLinkmanList(List<LinkmanBean> linkmanList) {
        this.linkmanList = linkmanList;
    }
}
