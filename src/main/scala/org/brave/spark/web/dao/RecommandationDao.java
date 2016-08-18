package org.brave.spark.web.dao;

import org.brave.spark.web.bo.MovieLinkBo;
import org.brave.spark.web.bo.RecommandationBo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuchen
 * on 2016/7/26 0026.
 */
@Repository
public interface RecommandationDao {


    /**
     * 通过用户ID获取电影列表
     * @param userId
     * @return List<OrderBo>
     */
    List<RecommandationBo> getRecommandationMoviesByUserId(String userId);

    /**
     * 实时
     * 通过用户ID获取电影列表
     * @param userId
     * @return List<OrderBo>
     */
    List<RecommandationBo> getStreamingRecommandationMoviesByUserId(String userId);


    /**
     *  将爬取的url存到数据库中
     * @param movieId
     * @param movieUrl
     * @param movieImgUrl
     */
    void savemovieUrls(String movieId,String movieUrl,String movieImgUrl);

    /**
     * 获取所有电影链接
     * @return List<OrderBo>
     */
    public List<MovieLinkBo> getmovieLinks() ;

    /**
     * 通过movieId 获取link
     * @return List<OrderBo>
     */
    public MovieLinkBo getmovieLinkBymovieId(String movie_id) ;

    /**
     * 更新电影链接的状态
     * @param movieId 电影Id
     */
   public void updatemovieLinkStateBymovieId(String movieId);

    /**
     * 将日期和数量日志写入
     * @param day1
     * @param num
     */
    void saveLogs(String day1,String num);

}


