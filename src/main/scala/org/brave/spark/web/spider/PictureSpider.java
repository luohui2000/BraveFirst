package org.brave.spark.web.spider;

import org.brave.spark.web.bo.MovieLinkBo;
import org.brave.spark.web.service.RecommandationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 爬虫
 * Created by yuchen
 * on 2016/7/29 0029.
 */
@Service
public class PictureSpider {
    public static ThreadLocal threadLocal=new ThreadLocal();
    /**
     *
     *<div class="poster">
     *<a href="/title/tt0112760/mediaviewer/rm1361744384?ref_=tt_ov_i">
     * <img alt="Cutthroat Island Poster" title="Cutthroat Island Poster"
     *src="http://ia.media-imdb.com/images/M/MV5BMTYyNjc2MjY2MV5BMl5BanBnXkFtZTcwNjU3NjM0MQ@@._V1_UY268_CR4,0,182,268_AL_.jpg"
     *itemprop="image" />
     *</a>    </div>
     *
     * 解析上面字符串中的src
     *
     * @param movieLinkBo
     * @return
     */
    public  void getImageUrlBymovie(MovieLinkBo movieLinkBo) {
         this.getImageUrlBymovie(movieLinkBo.getMovie_id(), movieLinkBo.getImdb_id());
    }

    /**
     *
     *<div class="poster">
     *<a href="/title/tt0112760/mediaviewer/rm1361744384?ref_=tt_ov_i">
     * <img alt="Cutthroat Island Poster" title="Cutthroat Island Poster"
     *src="http://ia.media-imdb.com/images/M/MV5BMTYyNjc2MjY2MV5BMl5BanBnXkFtZTcwNjU3NjM0MQ@@._V1_UY268_CR4,0,182,268_AL_.jpg"
     *itemprop="image" />
     *</a>    </div>
     *
     * 解析上面字符串中的src
     *
     * @return
     */
    public  void getImageUrlBymovie(String movie_id,String imdb_id) {
        String movieUrl="http://www.imdb.com/title/tt"+imdb_id;
        long startTime=System.currentTimeMillis();
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        StringBuffer sb=new StringBuffer();
        String line;
        int responsecode;
        try{
            URL url = new URL(movieUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/31.0 (compatible; MSIE 10.0; Windows NT; DigExt)"); //防止报403错误。

            responsecode=urlConnection.getResponseCode();
            if(responsecode==200){
                //得到输入流，即获得了网页的内容
                reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));
                while((line=reader.readLine())!=null){
                    sb.append(line);
                }

            }
            else{
                System.out.println("获取不到网页的源码，服务器响应代码为："+responsecode);
            }

//            String content = UtilFile.readToString(url.openStream(), "UTF-8");
            String content =sb.toString();
            String[] str1 = content.split("<div class=\"poster\">");
            String[] str2 = str1[1].split("itemprop");
            String[] str3 = str2[0].split("src");
            String imgUrl=str3[1].substring(2, str3[1].length() - 1);
            System.out.println(movieUrl);
            System.out.println(imgUrl);
            System.out.println("爬取图片链接共耗时（秒）："+(System.currentTimeMillis()-startTime)/1000);
            this.savemovieUrls(movie_id, movieUrl, imgUrl);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("movieUrl----"+movieUrl+"------movieUrl");
            System.out.println("movieId----"+movie_id+"------movie_id");
        }
    }


    /**
     *  将爬取的url存到数据库中
     * @param movieId
     * @param movieUrl
     * @param movieImgUrl
     */
    public  void savemovieUrls(String movieId,String movieUrl,String movieImgUrl){
        RecommandationService recommandationService = (RecommandationService)threadLocal.get();
        if(recommandationService==null){
            ApplicationContext context = new
                    ClassPathXmlApplicationContext("applicationContext.xml");
            recommandationService = (RecommandationService)context.getBean("recommandationService");
        }
       recommandationService.savemovieUrls(movieId, movieUrl, movieImgUrl);
       recommandationService.updatemovieLinkStateBymovieId(movieId);
    }

    public static void main(String[] args) throws Exception {
       // new PictureSpider().getImageUrlBymovie("1", "114709");
    }
}
