package cn.net.wujun.dev.demo.controller;

import cn.net.wujun.dev.demo.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jason Wu on 2017/05/25.
 */
@RestController
public class HelloController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HelloService helloService;

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        logger.debug("--controller->>>>>>>>hello param:{}", name);
        String result = helloService.hello(name);
        return result;
    }
}
