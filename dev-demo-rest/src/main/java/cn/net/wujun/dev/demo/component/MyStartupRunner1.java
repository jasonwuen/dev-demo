package cn.net.wujun.dev.demo.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by Jason Wu on 2017/06/02.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 10000)
public class MyStartupRunner1 implements CommandLineRunner {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        String[] defaultProfiles = context.getEnvironment().getDefaultProfiles();
        logger.info("No active profile set, falling back to default profiles: {}", StringUtils
                .arrayToCommaDelimitedString(defaultProfiles));
        logger.debug("run :{}", context.getEnvironment().getProperty("hello.name"));
    }
}
