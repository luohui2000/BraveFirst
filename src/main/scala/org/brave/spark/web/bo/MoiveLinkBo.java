package org.brave.spark.web.bo;

/**
 * Created by yuchen
 * on 2016/7/26 0026.
 */
public class MoiveLinkBo {

    /**
     * 电影ID
     */
    private String moive_id;
    /**
     * imdb_id
     */
    private String imdb_id;
    /**
     * tmdb_id
     */
    private String tmdb_id;
    /**
     * 抓取状态(null:未抓取，1：已抓取)
     */
    private String state;


    public String getMoive_id() {
        return moive_id;
    }

    public void setMoive_id(String moive_id) {
        this.moive_id = moive_id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getTmdb_id() {
        return tmdb_id;
    }

    public void setTmdb_id(String tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
