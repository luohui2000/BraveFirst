package org.brave.spark.web.spider;

import org.brave.spark.web.bo.MovieLinkBo;
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
        ApplicationContext context = new
                ClassPathXmlApplicationContext("applicationContext.xml");
        RecommandationService recommandationService = (RecommandationService)context.getBean("recommandationService");
        PictureSpider.threadLocal.set(recommandationService);
        while (true) {
            try {
            List<MovieLinkBo> MovieLinkBos = recommandationService.getmovieLinks();
            if(!MovieLinkBos.isEmpty()){
                for(MovieLinkBo movieLinkBo:MovieLinkBos){
                    if(pictureSpider==null){
                        pictureSpider= new PictureSpider();
                    }
                    pictureSpider.getImageUrlBymovie(movieLinkBo);
                    Thread.sleep(1000);
                    }
                }else{
                Thread.sleep(10000);
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
