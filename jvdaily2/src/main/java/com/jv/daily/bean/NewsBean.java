package com.jv.daily.bean;




import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
public class NewsBean {

    /**
     * date : 20170224
     * stories : [{"title":"为什么「我们的穿山甲是人工饲养的」根本站不住脚？","ga_prefix":"022411","images":["http://pic1.zhimg.com/ffba1db649b998bb0532d90b55ac6fd0.jpg"],"multipic":true,"type":0,"id":9246977},{"images":["http://pic3.zhimg.com/6c3e156f5121dd96c61ac5eae3df457e.jpg"],"type":0,"id":9246149,"ga_prefix":"022410","title":"为什么频繁进行收购的公司更容易财务造假？"},{"images":["http://pic1.zhimg.com/7b36e89f1717d3723c284722625aaec8.jpg"],"type":0,"id":9245969,"ga_prefix":"022409","title":"孕妇如何正确地系安全带？不是孕妇也值得记在心里"},{"images":["http://pic4.zhimg.com/1861338918eac0c99b39b55a1f5467ff.jpg"],"type":0,"id":9243959,"ga_prefix":"022408","title":"混合动力的汽油车见过挺多，怎么很少听说柴油混动车？"},{"title":"逃避可耻但有用，咖喱好吃却长胖","ga_prefix":"022407","images":["http://pic1.zhimg.com/972c4bed38a5b1079d24b7c34497daa4.jpg"],"multipic":true,"type":0,"id":9246129},{"title":"中国西北土壤荒漠化目前都采用了哪些治理措施？","ga_prefix":"022407","images":["http://pic1.zhimg.com/6e3474cefa2ea1e528614319c2d0b9e8.jpg"],"multipic":true,"type":0,"id":9233639},{"images":["http://pic2.zhimg.com/b0f76577e4c5911a250978132e5dce79.jpg"],"type":0,"id":9245906,"ga_prefix":"022407","title":"顺丰都自己建机场了，一个快递公司这么做有必要吗？"},{"images":["http://pic3.zhimg.com/dd9e805ea3ac1d9bc8331342d7470486.jpg"],"type":0,"id":9239420,"ga_prefix":"022406","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic4.zhimg.com/150a174eb20e365aef43cff9e1ca7913.jpg","type":0,"id":9246977,"ga_prefix":"022411","title":"为什么「我们的穿山甲是人工饲养的」根本站不住脚？"},{"image":"http://pic3.zhimg.com/fca19967c57cc7c4b7ec4bbe1f8d3776.jpg","type":0,"id":9246129,"ga_prefix":"022407","title":"逃避可耻但有用，咖喱好吃却长胖"},{"image":"http://pic3.zhimg.com/b0787c64fe585ab9619f523aa83353e6.jpg","type":0,"id":9245906,"ga_prefix":"022407","title":"顺丰都自己建机场了，一个快递公司这么做有必要吗？"},{"image":"http://pic3.zhimg.com/1429212c6ea92ffb6b80c2195fe5cb8e.jpg","type":0,"id":9244868,"ga_prefix":"022317","title":"当选篮协主席，姚明要面对的是一大堆问题"},{"image":"http://pic3.zhimg.com/e59f9ea79543402b6bec2346cbb1bcf6.jpg","type":0,"id":9244744,"ga_prefix":"022318","title":"奥斯卡 · 《你的名字》没被提名，是因为不够好吗？"}]
     */
    private long id;
    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

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

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {

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

    public static class TopStoriesBean {

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

        private long newsBeanId;

        public long getNewsBeanId() {
            return newsBeanId;
        }

        public void setNewsBeanId(int newsBeanId) {
            this.newsBeanId = newsBeanId;
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
}
