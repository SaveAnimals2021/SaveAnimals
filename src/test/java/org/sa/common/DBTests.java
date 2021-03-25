package org.sa.common;


import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sa.common.config.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CommonConfig.class})
public class DBTests {

    @Autowired
    private DataSource ds;

    @Autowired
    private SqlSessionFactory ssf;

    @Test
    public void testDataSource(){
        log.info("================= ds : "+ds+" ====================");
    }

    @Test
    public void testSqlSessionFactory() {
        // java.lang.IllegalStateException: Failed to load ApplicationContext 에러가 발생한다면
        // spring-jdbc를 pom.xml에 추가해야한다.
        try(SqlSession session = ssf.openSession()){
            log.info("================= session : "+session+" ====================");
            assertNotNull(session);
        }catch(Exception e) {
            log.info(e.getMessage());
        }
    }
}
