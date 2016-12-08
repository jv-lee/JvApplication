package com.jv.sms.adapter;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jv.sms.R;
import com.jv.sms.factory.FragmentFactory;

/**
 * Created by Administrator on 2016/12/1.
 */

public class SmsUiAdapter extends FragmentStatePagerAdapter {

    public int[] tabIconRes = {R.drawable.selected_mes, R.drawable.selected_contacts};
    private Activity mActivity;


    public SmsUiAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.getSmsFragment(position);
    }

    @Override
    public int getCount() {
        return FragmentFactory.smsFragmentsClass.length;
    }

    @Override
    public String getPageTitle(int position) {
        return FragmentFactory.smsFragmentTitles[position];
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
