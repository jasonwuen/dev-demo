package cn.net.wujun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by ${USER} on ${DATE}.
 */
@SpringBootApplication
public class Application {

    static {
        System.setProperty("LOG_PATH", "./logs");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}