package cn.net.wujun;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ${USER} on ${DATE}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class BaseSpringBootTest {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
}
