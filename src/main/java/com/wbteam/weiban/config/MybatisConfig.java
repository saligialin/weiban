package com.wbteam.weiban.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true);//开启驼峰命名法
        };
    }
}
