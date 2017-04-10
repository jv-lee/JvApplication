package com.jv.daily.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/3/7.
 */

@Entity
public class TopStoriesBean {
    @Id
    private long id;
    private String image;
    private int type;
    private String ga_prefix;
    private String title;
    private String date;

    @Generated(hash = 979047387)
    public TopStoriesBean(long id, String image, int type, String ga_prefix,
            String title, String date) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.ga_prefix = ga_prefix;
        this.title = title;
        this.date = date;
    }

    @Generated(hash = 704345866)
    public TopStoriesBean() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
