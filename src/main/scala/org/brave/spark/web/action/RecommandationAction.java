package org.brave.spark.web.action;

import com.alibaba.fastjson.JSON;
import org.brave.spark.web.bo.RecommandationBo;
import org.brave.spark.web.service.RecommandationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 推荐
 *
 * Created by yuchen
 * on 2016/7/26 0026.
 */

@Controller
@RequestMapping("recommandation")
public class RecommandationAction  {
    protected static String jsonData = "";

    private static final Logger logger = LoggerFactory
            .getLogger(RecommandationAction.class);

    @Autowired
    private RecommandationService recommandationService;


    /**
     * 正在跳转至商务分配页面
     *
     * @return
     */
    @RequestMapping("gotoUserMoviceList")
    public String gotoUserMoviceList(HttpServletRequest request,String userId) {
        logger.debug("正在进入用户推荐电影页面");
        return "/recommandation/userMoviceList";
    }
    /**
     * 正在跳转至商务分配页面
     *
     * @return
     */
    @RequestMapping(value = "getUserMoviceList", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUserMoviceList(HttpServletRequest request,String userId) {
        logger.debug("正在用户推荐电影页面");
        if(userId==null||"".equals(userId))
            userId="1";
        List<RecommandationBo> recommandationBos= recommandationService.getRecommandationMoviesByUserId(userId);
        String json=JSON.toJSONString(recommandationBos);
        return json;
    }
}