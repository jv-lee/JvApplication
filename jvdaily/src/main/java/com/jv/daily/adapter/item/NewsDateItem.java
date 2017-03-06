package com.jv.daily.adapter.item;

import com.jv.daily.R;
import com.jv.daily.adapter.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/3/2.
 */

public class NewsDateItem extends BaseItem {

    String date;

    @Override
    public int getLayout() {
        return R.layout.item_news_date;
    }

    public NewsDateItem(MultiTypeAdapter adapter, String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

}
