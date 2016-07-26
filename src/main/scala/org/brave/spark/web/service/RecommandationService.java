package org.brave.spark.web.service;

import org.brave.spark.web.bo.RecommandationBo;
import org.brave.spark.web.dao.RecommandationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuchen
 * on 2016/7/26 0026.
 */
@Service
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
}
