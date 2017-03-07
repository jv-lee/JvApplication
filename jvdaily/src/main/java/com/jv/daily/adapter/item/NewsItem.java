package com.jv.daily.adapter.item;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jv.daily.R;
import com.jv.daily.activity.ContentActivity;
import com.jv.daily.activity.MainActivity;
import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.app.MyApplication;
import com.jv.daily.entity.StoriesBean;
import com.jv.daily.mvp.module.NewsBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/27.
 */

public class NewsItem extends BaseItem {
    @Override
    public int getLayout() {
        return R.layout.item_news;
    }

    private final StoriesBean bean;

    public NewsItem(final MultiTypeAdapter adapter, final StoriesBean bean) {
        this.bean = bean;

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.item_container:

                        toggleClick();
                        adapter.notifyItemChanged(adapter.findPosition(NewsItem.this));

                        Log.i("NewsItem", bean.getTitle() + " position " + adapter.findPosition(NewsItem.this));

                        //跳转至对应新闻内容
                        if (adapter.context != null) {
                            Intent intent = new Intent(adapter.context, ContentActivity.class);
                            intent.putExtra("id", bean.getId() + "");
                            adapter.context.startActivity(intent);
                        }

                        break;
                    case R.id.iv_news_pic:
                        Log.i("NewsItem", "图片" + " position " + adapter.findPosition(NewsItem.this));
                        break;
                }
            }
        });

        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                switch (view.getId()) {
                    case R.id.item_container:
                        Log.i("NewsItem", "长按" + " position " + adapter.findPosition(NewsItem.this));
                        break;
                }
                return true;
            }
        });

    }

    public String getUrl() {
        return bean.getImage();
    }

    public String getContent() {
        return bean.getTitle();
    }

    public boolean isMultipic() {
        return bean.isMultipic();
    }

    private void toggleClick() {
        bean.setMultipic(!bean.isMultipic());
    }

}
