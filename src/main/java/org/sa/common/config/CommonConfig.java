package org.sa.common.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.sa.batch.config.AnimalJobConfig;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Log4j
@Configuration
@EnableTransactionManagement //트랜잭션 추가

@Import(AnimalJobConfig.class)
public class CommonConfig {

    @Bean
    public DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://112.169.196.210:3309/saveanimals?serverTimezone=UTC");
        hikariConfig.setUsername("saproject");
        hikariConfig.setPassword("saveanimals");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        return dataSource;

    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource());
        //sqlSessionFactory.setConfigLocation(null); - mybatis-config

        return sqlSessionFactory.getObject();
    }

    //트랜잭션 추가
    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @Primary
    public DataSource batchEmbeddedDatasource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();

        // memory로 만드는 DB
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean jrfb = new JobRepositoryFactoryBean();
        jrfb.setDataSource(batchEmbeddedDatasource());
        jrfb.setTransactionManager(rtxManager());
        return jrfb.getObject();

    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository());
        return launcher;
    }

    @Bean
    public ResourcelessTransactionManager rtxManager() throws Exception {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() throws Exception {
        JobLauncherTestUtils utils = new JobLauncherTestUtils();

        return utils;
    }


    // ========== BATCH
//    @Bean
//    public ResourcelessTransactionManager rtxManager(){
//        return new ResourcelessTransactionManager();
//    }
//
//    @Bean
//    public JobLauncher jobLauncher() throws Exception{
//        SimpleJobLauncher sjl = new SimpleJobLauncher();
//        sjl.setJobRepository(jobRepository());
//        return sjl;
//    }
//
//    @Bean
//    public JobRepository jobRepository() throws Exception{
//        JobRepositoryFactoryBean jrf = new JobRepositoryFactoryBean();
//        jrf.setDataSource(dataSource());
//        jrf.setTransactionManager(rtxManager());
//        jrf.setIsolationLevelForCreate("ISOLATION_DEFAULT");
//
//        return jrf.getObject();
//    }
//
//    @Bean
//    public JobOperator jobOperator() throws Exception{
//        SimpleJobOperator sjo = new SimpleJobOperator();
//        sjo.setJobLauncher(jobLauncher());
//        sjo.setJobExplorer(jobExplorer());
//        sjo.setJobRepository(jobRepository());
//        sjo.setJobRegistry(jobRegistry());
//
//        return sjo;
//    }
//
//    @Bean
//    public JobExplorer jobExplorer() throws Exception{
//        JobExplorerFactoryBean jefb = new JobExplorerFactoryBean();
//        return jefb.getObject();
//    }
//
//    @Bean
//    public JobRegistry jobRegistry() throws Exception {
//        return new MapJobRegistry();
//    }
//
//    @Bean
//    public JobLauncherTestUtils jobLauncherTestUtils(){
//        return new JobLauncherTestUtils();
//    }


}
