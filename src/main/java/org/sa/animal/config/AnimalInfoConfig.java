package org.sa.animal.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages= {"org.sa.animal.service"})
@MapperScan(basePackages= {"org.sa.animal.mapper"})
public class AnimalInfoConfig {
}
