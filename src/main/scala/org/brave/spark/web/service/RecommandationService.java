package org.brave.spark.web.service;


import org.brave.spark.web.bo.MovieLinkBo;
import org.brave.spark.web.bo.RecommandationBo;
import org.brave.spark.web.dao.RecommandationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by yuchen
 * on 2016/7/26 0026.
 * 更新了文件的编码格式，从GBK改为UTF-8
 */

public class RecommandationService {
    @Autowired
    private RecommandationDao recommandationDao;
    /**
     * 通过用户ID获取电影列表
     * @param userId
     * @return List<OrderBo>
     */
    public List<RecommandationBo> getRecommandationMoviesByUserId(String userId) {
        return recommandationDao.getRecommandationMoviesByUserId(userId);
    }
    /**
     *  将爬取的url存到数据库中
     * @param movieId
     * @param movieUrl
     * @param movieImgUrl
     */
    public void savemovieUrls(String movieId, String movieUrl, String movieImgUrl){
         recommandationDao.savemovieUrls(movieId,movieUrl,movieImgUrl);
    }

    /**
     * 获取所有电影链接
     * @return List<OrderBo>
     */
    public List<MovieLinkBo> getmovieLinks() {
        return recommandationDao.getmovieLinks();
    }

    /**
     * 通过movieId 获取link
     * @return List<OrderBo>
     */
    public MovieLinkBo getmovieLinkBymovieId(String movie_id) {
        return recommandationDao.getmovieLinkBymovieId(movie_id);
    }

    /**
     * 更新电影链接的状态
     * 已爬取图片和链接
     *
     * @param movieId 电影Id
     */
    public void updatemovieLinkStateBymovieId(String movieId){
        recommandationDao.updatemovieLinkStateBymovieId(movieId);
    }

    /**
     * 将日期和数量日志写入
     * @param day1
     * @param num
     */
    public void saveLogs(String day1,String num){
        recommandationDao.saveLogs(day1,num);
    }
}
