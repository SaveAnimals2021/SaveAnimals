package org.sa.common.controller;


import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sa.common.config.CommonConfig;
import org.sa.common.config.ServletConfig;
import org.sa.common.config.WebConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CommonConfig.class, ServletConfig.class, WebConfig.class})
public class SaControllerTests {

    @Autowired
    SaController controller;

    @Test
    public void testSaController(){
        log.info("controller : " + controller);
    }

}
