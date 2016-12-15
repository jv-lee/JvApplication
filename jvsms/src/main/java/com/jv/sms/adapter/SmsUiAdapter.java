package com.jv.sms.adapter;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jv.sms.R;
import com.jv.sms.interfaces.DataLoadLayoutListener;
import com.jv.sms.fragment.ContactsFragment;
import com.jv.sms.fragment.SmsFragment;

/**
 * Created by Administrator on 2016/12/1.
 */

public class SmsUiAdapter extends FragmentStatePagerAdapter {

    public Fragment[] smsFragmentsClass;
    public String[] smsFragmentTitles = {"短信", "联系人"};
    public int[] tabIconRes = {R.drawable.selected_mes, R.drawable.selected_contacts};
    private Activity mActivity;

    public SmsUiAdapter(FragmentManager fm, Activity activity, DataLoadLayoutListener listener) {
        super(fm);
        this.mActivity = activity;
        smsFragmentsClass = new Fragment[]{new SmsFragment(listener), new ContactsFragment(listener)};
    }

    @Override
    public Fragment getItem(int position) {
        return smsFragmentsClass[position];
    }

    @Override
    public int getCount() {
        return smsFragmentsClass.length;
    }

    @Override
    public String getPageTitle(int position) {
        return smsFragmentTitles[position];
    }

    public void setTabIcon(TabLayout tab) {
        for (int i = 0; i < tab.getTabCount(); i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(75, 75));
            imageView.setImageResource(tabIconRes[i]);
            tab.getTabAt(i).setCustomView(imageView);
        }
    }

}
