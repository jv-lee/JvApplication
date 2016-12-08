package com.jv.sms.factory;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.jv.sms.fragment.ContactsFragment;
import com.jv.sms.fragment.SmsFragment;


/**
 * Created by Administrator on 2016/12/1.
 */

public class FragmentFactory {

    private static SparseArray<Fragment> smsFragments = new SparseArray<>();
    public static Fragment[] smsFragmentsClass = {new SmsFragment(), new ContactsFragment()};
    public static String[] smsFragmentTitles = {"短信", "联系人"};


    public static Fragment getSmsFragment(int position) {
        Fragment fragment = smsFragments.get(position);
        if (fragment == null) {
            for (int i = 0; i < smsFragmentsClass.length; i++) {
                if (position == i) {
                    fragment = smsFragmentsClass[i];
                    smsFragments.put(position, fragment);
                    break;
                }
            }
        }
        return fragment;
    }


}
