package org.brave.spark.web.bo;

/**
 * Created by yuchen
 * on 2016/7/26 0026.
 */
public class RecommandationBo {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户ID
     */
    private String user_id;
    /**
     * 电影ID
     */
    private String moive_id;
    /**
     * 电影名称
     */
    private String moive_name;
    /**
     * 电影图片URL
     */
    private String moive_img_url;
    /**
     * 电影地址
     */
    private String movie_url;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMoive_id() {
        return moive_id;
    }

    public void setMoive_id(String moive_id) {
        this.moive_id = moive_id;
    }

    public String getMoive_name() {
        return moive_name;
    }

    public void setMoive_name(String moive_name) {
        this.moive_name = moive_name;
    }

    public String getMoive_img_url() {
        return moive_img_url;
    }

    public void setMoive_img_url(String moive_img_url) {
        this.moive_img_url = moive_img_url;
    }

    public String getMovie_url() {
        return movie_url;
    }

    public void setMovie_url(String movie_url) {
        this.movie_url = movie_url;
    }
}
