package com.jv.daily.bean;


import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public class StoriesBean {

    /**
     * title : 为什么「我们的穿山甲是人工饲养的」根本站不住脚？
     * ga_prefix : 022411
     * images : ["http://pic1.zhimg.com/ffba1db649b998bb0532d90b55ac6fd0.jpg"]
     * multipic : true
     * type : 0
     * id : 9246977
     */

    private String title;
    private String ga_prefix;
    private boolean multipic;
    private int type;
    private long id;
    private List<String> images;


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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
