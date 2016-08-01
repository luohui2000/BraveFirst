package org.brave.spark.web.dao;

import org.brave.spark.web.bo.MoiveLinkBo;
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
     *  将爬取的url存到数据库中
     * @param moiveId
     * @param moiveUrl
     * @param moiveImgUrl
     */
    void saveMoiveUrls(String moiveId,String moiveUrl,String moiveImgUrl);

    /**
     * 获取所有电影链接
     * @return List<OrderBo>
     */
    public List<MoiveLinkBo> getMoiveLinks() ;

    /**
     * 通过MoiveId 获取link
     * @return List<OrderBo>
     */
    public MoiveLinkBo getMoiveLinkByMoiveId(String moive_id) ;

    /**
     * 更新电影链接的状态
     * @param moiveId 电影Id
     */
   public void updateMoiveLinkStateByMoiveId(String moiveId);
}


