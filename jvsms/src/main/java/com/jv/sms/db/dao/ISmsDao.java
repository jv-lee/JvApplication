package com.jv.sms.db.dao;

import com.jv.sms.bean.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */

public interface ISmsDao {

    boolean save(String[] ids);

    boolean delete(String[] onIds);

    List<SmsBean> findAll();

}
