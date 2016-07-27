package org.brave.spark.web.dao;

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

}


