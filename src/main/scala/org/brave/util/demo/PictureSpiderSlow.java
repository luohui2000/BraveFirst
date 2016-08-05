package org.brave.util.demo;

//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebClientOptions;
//import com.gargoylesoftware.htmlunit.html.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.IOException;

/**
 *
 * 另外一种方式爬取图片链接
 *
 * 需要在pom.xml中引入 htmlunit的jar
 *
 * 当前没有引入，所以暂时把这个class注释掉，有参考价值
 *
 *
 * @author yuchen
 * @since 2016/7/29.
 *
 */
public class PictureSpiderSlow {
    private final static String movieUrl = "http://www.imdb.com/title/tt0112760/?ref_=fn_al_tt_1";

//    /**
//     * 设置
//     * @return
//     */
//    public static WebClient getWebClient() {
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        WebClientOptions options = webClient.getOptions();
//        options.setJavaScriptEnabled(true);
//        options.setCssEnabled(true);
//        options.setRedirectEnabled(true);
//        options.setThrowExceptionOnScriptError(false);
//        options.setTimeout(30000);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        return webClient;
//    }
//
//    /**
//     *
//     *<div class="poster">
//     *<a href="/title/tt0112760/mediaviewer/rm1361744384?ref_=tt_ov_i">
//     * <img alt="Cutthroat Island Poster" title="Cutthroat Island Poster"
//     *src="http://ia.media-imdb.com/images/M/MV5BMTYyNjc2MjY2MV5BMl5BanBnXkFtZTcwNjU3NjM0MQ@@._V1_UY268_CR4,0,182,268_AL_.jpg"
//     *itemprop="image" />
//     *</a>    </div>
//     *
//     * 解析上面字符串中的
//     *
//     * @param movieUrl
//     * @return
//     */
//    public static String getImage(String movieUrl) {
//        WebClient client = getWebClient();
//        HtmlPage page = null;
//        try {
//            page = client.getPage(movieUrl);//获得电影页面
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (page != null) {
//            DomNodeList<DomElement>  domElements=  page.getElementsByTagName("div");
//            if(domElements!=null)
//                for(DomElement domElement:domElements){
//                    String className=domElement.getAttribute("class");
//                    if("poster".equals(className)){
//                        String sdfsd=domElement.getAttribute("src");
//                        Iterable<DomElement> dsdf= domElement.getChildElements();
//                        // DomElement d=   dsdf.();
//                        DomNodeList<DomNode>  clildDomNodes=  domElement.getChildNodes();
//                        if(clildDomNodes!=null){
//                            for(DomNode domNode:clildDomNodes){
//                                System.out.println(domNode);
//                                NamedNodeMap namedNodeMap= domNode.getAttributes();
//                                if(namedNodeMap!=null){
//                                    Node node = namedNodeMap.getNamedItem("href");
//                                    if(node!=null){
//                                        DomNodeList<DomNode> grandClildDomNodes= domNode.getChildNodes();
//                                        if(grandClildDomNodes!=null){
//                                            for(DomNode gradeDomNode:grandClildDomNodes){
//                                                if(gradeDomNode!=null){
//                                                    NamedNodeMap gradeNamedNodeMap= gradeDomNode.getAttributes();
//                                                    if(gradeNamedNodeMap!=null){
//                                                        Node gradeNode= gradeNamedNodeMap.getNamedItem("src");
//                                                        if(gradeNode!=null){
//
//                                                            return  gradeNode.getNodeValue();
//
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//        }
//        return null;
//    }
//
//    public static void main(String[] args) throws Exception {
//        long startTime=System.currentTimeMillis();
//        System.out.println("电影url地址为："+movieUrl);
//        System.out.println("电影宣传的图片url地址为：" + PictureSpiderSlow.getImage(movieUrl));
//        System.out.println("爬取图片共耗时（秒）："+(System.currentTimeMillis()-startTime)/1000);
//    }
}