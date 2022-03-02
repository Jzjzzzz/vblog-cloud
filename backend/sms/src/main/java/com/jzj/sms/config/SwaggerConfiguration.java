package com.jzj.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import team.ark.core.response.R;

import java.util.ArrayList;

/**
 * Swagger配置
 *
 * @author Jzj
 * @date 2021/11/27
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    /**
     * 接口文档基础信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API Doc",
                "",
                "1.0.0",
                "",
                new Contact("Jzj",
                        "https://www.jzjzzzz.icu/",
                        "946232976@qq.com"),
                "None",
                "",
                new ArrayList<>());
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                // 不显示Spring自带的ErrorController
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                //.directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(R.class)
                // 不显示默认响应码
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }
}
