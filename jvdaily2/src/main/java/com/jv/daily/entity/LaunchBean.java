package com.jv.daily.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LaunchBean {

    private List<CreativesBean> creatives;

    public List<CreativesBean> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<CreativesBean> creatives) {
        this.creatives = creatives;
    }

    public static class CreativesBean {
        /**
         * url : https://pic1.zhimg.com/v2-0bf26092e8bd38a59d08dc9326fe5ca8.jpg
         * start_time : 1492568401
         * impression_tracks : ["https://sugar.zhihu.com/track?vs=1&ai=3908&ut=&cg=2&ts=1492568401.64&si=9e9bdd5d1db54e60a90e58af518eadbc&lu=0&hn=ad-engine.ad-engine.d23b4bcd&at=impression&pf=PC&az=11&sg=ee8e8cef964349feb927e469b4dbae71"]
         * type : 0
         * id : 3908
         */

        private String url;
        private int start_time;
        private int type;
        private String id;
        private List<String> impression_tracks;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getImpression_tracks() {
            return impression_tracks;
        }

        public void setImpression_tracks(List<String> impression_tracks) {
            this.impression_tracks = impression_tracks;
        }
    }
}
