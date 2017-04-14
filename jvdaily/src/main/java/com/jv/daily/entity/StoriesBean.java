package com.jv.daily.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/3/7.
 */
@Entity
public class StoriesBean {
//    public NewsItem createItem(MultiTypeAdapter adapter) {
//        return new NewsItem(adapter, this);
//    }

    @Id
    private long id;
    private String title;
    private String ga_prefix;
    private boolean multipic;
    private int type;
    private String image;
    private String date;

    @Generated(hash = 875948405)
    public StoriesBean(long id, String title, String ga_prefix, boolean multipic,
            int type, String image, String date) {
        this.id = id;
        this.title = title;
        this.ga_prefix = ga_prefix;
        this.multipic = multipic;
        this.type = type;
        this.image = image;
        this.date = date;
    }

    @Generated(hash = 929118848)
    public StoriesBean() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getMultipic() {
        return this.multipic;
    }
}
