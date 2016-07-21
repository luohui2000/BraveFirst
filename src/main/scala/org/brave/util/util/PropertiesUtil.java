package org.brave.util.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by yuchen
 * on 2016-07-14.
 * <p>
 * 配置文件的通用读取
 */
public class PropertiesUtil {
    public static Properties loadProperties(String configpath) {
        Properties properties = new Properties();
        try {
            InputStream localInputStream = PropertiesUtil.class.getResourceAsStream(configpath);
            properties.load(localInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static void main(String[] args){
    	
    }
}
