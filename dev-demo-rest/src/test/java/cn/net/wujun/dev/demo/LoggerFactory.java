package cn.net.wujun.dev.demo;

import cn.net.wujun.dev.demo.component.Profiles;
import com.google.common.collect.Maps;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import sun.applet.Main;

import java.util.Map;
import java.util.Properties;

/**
 * Created by Jason Wu on 2017/06/01.
 */
public class LoggerFactory {

    //private static Map<String, String> allProperties = Maps.newConcurrentMap();

    private static Properties allProperties;

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
            allProperties = properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(Profiles.Default);
        System.out.println(allProperties.getProperty("spring.profiles.active"));
    }
}
