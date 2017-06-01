package cn.net.wujun;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ${USER} on ${DATE}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@ActiveProfiles("local-test")
public class BaseSpringBootTest {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        System.setProperty("LOG_PATH", "./logs");
    }

    @Autowired
    protected ApplicationContext context;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
}
