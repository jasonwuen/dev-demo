package cn.net.wujun.dev.demo;

import cn.net.wujun.BaseSpringBootTest;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by Jason Wu on 2017/06/01.
 */
@ActiveProfiles(value = "default")
public class LocalTest extends BaseSpringBootTest {

    @Value("${hello.name}")
    private String name;

    @Value("${logging.file}")
    private String loggingFile;

    @Test
    public void test() {
        logger.debug("application.name:{}", context.getApplicationName());
        logger.debug("test :{}", JSON.toJSONString(context.getEnvironment().getActiveProfiles()));
        logger.debug("loggingFile:{}", loggingFile);
        logger.debug("test:{}", name);
        logger.debug("test=====>>>>:{}", context.getEnvironment().getProperty("hello.name"));
    }
}
