package cn.net.wujun.dev.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Jason Wu on 2017/05/25.
 */
@Service
public class HelloService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String hello(String name) {
        String result = String.format("hello %s!", name);
        logger.debug("service ---->>>>>>:{}", result);
        return result;
    }
}
