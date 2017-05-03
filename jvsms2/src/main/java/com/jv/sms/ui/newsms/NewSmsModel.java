package com.jv.sms.ui.newsms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.jv.sms.base.mvp.BaseModel;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.ContactsBean;
import com.jv.sms.entity.LinkmanBean;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.utils.SmsUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/5/3.
 */
@ActivityScope
public class NewSmsModel extends BaseModel implements NewSmsContract.Model {

    private final String PHONE_BOOK_LABEL = "phonebook_label"; //联系人列表 名字字母标识
    private String flag = "";

    private int position = 0;
    private int getPosition = -1;

    @Inject
    public NewSmsModel() {

    }

    /**
     * 获取当前所有联系人列表 并进行第一个文字 拼音缩写划分子集合
     *
     * @return
     */
    @Override
    public List<ContactsBean> findContactsAll() {
        List<ContactsBean> list = new ArrayList<>();
        ContactsBean contactsBean = null;

        long time1 = System.currentTimeMillis();

        //查询raw_contacts表获得联系人id
        ContentResolver resolver = mApplication.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //查询联系人数据
        Cursor cursor = resolver.query(uri, null, null, null, "phonebook_label ASC");

        while (cursor.moveToNext()) {

            //获取联系人拼音字符头
            String label = cursor.getString(cursor.getColumnIndex(PHONE_BOOK_LABEL));
            if (label == null || label.equals("")) {
                label = "...";
            }

            if (label.equals("#")) {
                getPosition = position;
            }

            //第一次进入 设置字符头flag 创建联系人实体类
            if (flag.equals("")) {
                flag = label;
                contactsBean = new ContactsBean(flag, new ArrayList<LinkmanBean>());
            } else if (!flag.equals(label)) { //非第一次进入 且flag不等于当前flag 创建新的字符头 和联系人 表示下一组
                list.add(contactsBean);
                flag = label;
                contactsBean = new ContactsBean(flag, new ArrayList<LinkmanBean>());
            }

            //获取Thread_id
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ", "");
            String thread_id = "-1";
            Cursor cur1 = resolver.query(Uri.parse("content://sms/"), new String[]{"thread_id"}, "address=?", new String[]{phoneNumber}, null);
            if (cur1.moveToFirst()) {
                thread_id = cur1.getString(cur1.getColumnIndex("thread_id"));
            }

            //创建联系人 保存数据
            LinkmanBean bean = new LinkmanBean();
            bean.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            bean.setPhoneNumber(phoneNumber);
            bean.setWord(label);
            bean.setColorType(Integer.parseInt(bean.getPhoneNumber().substring(bean.getPhoneNumber().length() - 1)) == 9 ? 0 : Integer.parseInt(bean.getPhoneNumber().substring(bean.getPhoneNumber().length() - 1)));
            bean.setThread_id(thread_id);
            contactsBean.getLinkmanList().add(bean);
            position++;
        }
        //最后一次无法判断 直接添加
        list.add(contactsBean);

        //#号头标放置最后
        if (getPosition != -1) {
            ContactsBean bean = list.get(getPosition);
            list.remove(getPosition);
            list.add(bean);
        }

        Log.i("时间：", System.currentTimeMillis() - time1 + "");

        return list;
    }

    /**
     * 获取输入提示联系人列表
     *
     * @return
     */
    @Override
    public List<LinkmanBean> findLinkmanAll() {
        List<LinkmanBean> linkmanList = new ArrayList<>();

        //查询raw_contacts表获得联系人id
        ContentResolver resolver = mApplication.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //查询联系人数据 根据第一个字符头排序
        Cursor cursor = resolver.query(uri, null, null, null, "phonebook_label ASC");

        while (cursor.moveToNext()) {

            //获取Thread_id
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ", "");
            String thread_id = "-1";
            Cursor cur1 = resolver.query(Uri.parse("content://sms/"), new String[]{"thread_id"}, "address=?", new String[]{phoneNumber}, null);
            if (cur1.moveToFirst()) {
                thread_id = cur1.getString(cur1.getColumnIndex("thread_id"));
            }
            //创建联系人 保存数据
            LinkmanBean bean = new LinkmanBean();
            bean.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            bean.setPhoneNumber(phoneNumber);
            bean.setColorType(Integer.parseInt(bean.getPhoneNumber().substring(bean.getPhoneNumber().length() - 1)) == 9 ? 0 : Integer.parseInt(bean.getPhoneNumber().substring(bean.getPhoneNumber().length() - 1)));
            bean.setThread_id(thread_id);
            linkmanList.add(bean); //添加至集合
        }
        return linkmanList;
    }

    @Override
    public SmsBean findLinkmanByPhoneNumber(String phoneNumber) {
        SmsBean bean = null;

        //查询raw_contacts表获得联系人id
        ContentResolver resolver = mApplication.getContentResolver();
        Cursor cur = resolver.query(Uri.parse("content://sms/"), new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read"}, "address=?", new String[]{phoneNumber}, "date desc limit 1");
        while (cur.moveToNext()) {
            bean = new SmsBean();
            bean.setPhoneNumber(phoneNumber);
            bean.setName(SmsUtil.getPeopleNameFromPerson(phoneNumber, mApplication));
            bean.setThread_id(cur.getString(cur.getColumnIndex("thread_id")));
            bean.setColorPosition(Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1)) == 9 ? 0 : Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1)));
        }
        if (bean == null) {
            bean = new SmsBean();
            bean.setPhoneNumber(phoneNumber);
            bean.setName(phoneNumber);
            bean.setThread_id("-1");
            bean.setColorPosition(Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1)) == 9 ? 0 : Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 1)));
        }
        return bean;
    }
}
