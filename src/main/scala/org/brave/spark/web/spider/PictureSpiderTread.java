package org.brave.spark.web.spider;

import org.brave.spark.web.bo.MoiveLinkBo;
import org.brave.spark.web.service.RecommandationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 *
 * 爬虫入口
 *
 * Created by yuchen
 * on 2016/7/29 0029.
 */
public class PictureSpiderTread extends Thread{
    PictureSpider pictureSpider= new PictureSpider();
    @Override
    public void run() {
        while (true) {
            try {
            ApplicationContext context = new
                    ClassPathXmlApplicationContext("applicationContext.xml");
            RecommandationService recommandationService = (RecommandationService)context.getBean("recommandationService");
            List<MoiveLinkBo> moiveLinkBos = recommandationService.getMoiveLinks();
            if(!moiveLinkBos.isEmpty()){
                for(MoiveLinkBo moiveLinkBo:moiveLinkBos){
                    if(pictureSpider==null){
                        pictureSpider= new PictureSpider();
                    }
                    pictureSpider.getImageUrlByMoive(moiveLinkBo);
                    Thread.sleep(1000);
                    }
                }else{
                Thread.sleep(1000000000);
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new PictureSpiderTread().start();
    }
}
