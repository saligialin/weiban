package com.wbteam.weiban.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        ApiInfo apiInfo = new ApiInfoBuilder()
                .contact(   //配置swagger开发文档
                        new Contact(
                                "WeiBan team",     //文档发布者名称
                                "localhost:9030",   //文档发布者大额网站地址
                                "saligialin@qq.com")    //文档发布者的邮箱
                )
                .title("WeiBan API")
                .description("WeiBan API documentation")
                .version("1.0")
                .build();

        docket
                .apiInfo(apiInfo)
                .select()   //获取docket中的选择器
                .apis(RequestHandlerSelectors.basePackage("com.wbteam.weiban"));   //配置扫描哪个包

        return docket;
    }
}
