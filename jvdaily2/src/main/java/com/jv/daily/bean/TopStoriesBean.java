package com.jv.daily.bean;



/**
 * Created by Administrator on 2017/3/7.
 */

public class TopStoriesBean  {

    /**
     * image : http://pic4.zhimg.com/150a174eb20e365aef43cff9e1ca7913.jpg
     * type : 0
     * id : 9246977
     * ga_prefix : 022411
     * title : 为什么「我们的穿山甲是人工饲养的」根本站不住脚？
     */

    private String image;
    private int type;
    private long id;
    private String ga_prefix;
    private String title;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
