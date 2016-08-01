package org.brave.spark.web.service;

import org.brave.spark.web.bo.MoiveLinkBo;
import org.brave.spark.web.bo.RecommandationBo;
import org.brave.spark.web.dao.RecommandationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by yuchen
 * on 2016/7/26 0026.
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
     * @param moiveId
     * @param moiveUrl
     * @param moiveImgUrl
     */
    public void saveMoiveUrls(String moiveId, String moiveUrl, String moiveImgUrl){
         recommandationDao.saveMoiveUrls(moiveId,moiveUrl,moiveImgUrl);
    }

    /**
     * 获取所有电影链接
     * @return List<OrderBo>
     */
    public List<MoiveLinkBo> getMoiveLinks() {
        return recommandationDao.getMoiveLinks();
    }

    /**
     * 通过MoiveId 获取link
     * @return List<OrderBo>
     */
    public MoiveLinkBo getMoiveLinkByMoiveId(String moive_id) {
        return recommandationDao.getMoiveLinkByMoiveId(moive_id);
    }

    /**
     * 更新电影链接的状态
     * 已爬取图片和链接
     *
     * @param moiveId 电影Id
     */
    public void updateMoiveLinkStateByMoiveId(String moiveId){
        recommandationDao.updateMoiveLinkStateByMoiveId(moiveId);
    }
}
