package com.example.advance.test;

import com.example.advance.test.config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.test.DemoBeanIntegrationTests
 * @Description: 测试类
 * @create 2018/07/10 16:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("prod")
public class DemoBeanIntegrationTests {
    @Autowired
    private TestBean testBean;

    @Test
    public void prodBeanShouldInject(){
        String excepted = "from production profile";
        String actual = testBean.getContent();
        Assert.assertEquals(excepted,actual);
    }
}
