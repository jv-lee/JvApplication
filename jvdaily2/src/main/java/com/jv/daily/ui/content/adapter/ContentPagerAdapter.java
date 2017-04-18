package com.jv.daily.ui.content.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jv.daily.base.app.AppComponent;
import com.jv.daily.ui.content.ContentFragment;
import com.jv.daily.ui.content.inject.ContentModule;
import com.jv.daily.ui.content.inject.DaggerContentComponent;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ContentPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> ids;
    public String id;
    public AppComponent appComponent;

    public ContentPagerAdapter(FragmentManager fm, List<String> ids, String id, AppComponent appComponent) {
        super(fm);
        this.ids = ids;
        this.id = id;
        this.appComponent = appComponent;
    }

    public int getThisPosition(){
        return ids.indexOf(id);
    }

    @Override
    public Fragment getItem(int position) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", ids.get(position));
        fragment.setArguments(bundle);

        DaggerContentComponent.builder()
                .appComponent(appComponent)
                .contentModule(new ContentModule(fragment))
                .build()
                .inject(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return ids.size();
    }
}
