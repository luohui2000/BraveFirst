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
    private String movie_id;
    /**
     * 电影名称
     */
    private String movie_name;
    /**
     * 电影图片URL
     */
    private String movie_img_url;
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

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_img_url() {
        return movie_img_url;
    }

    public void setMovie_img_url(String movie_img_url) {
        this.movie_img_url = movie_img_url;
    }

    public String getMovie_url() {
        return movie_url;
    }

    public void setMovie_url(String movie_url) {
        this.movie_url = movie_url;
    }
}
