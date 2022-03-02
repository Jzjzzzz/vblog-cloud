package team.blogserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Import(team.ark.core.util.SpringUtils.class)
@SpringBootApplication(scanBasePackages = {"team"})
@EnableEurekaClient
public class Main8080Application {
    public static void main(String[] args) {
        SpringApplication.run(Main8080Application.class, args);
    }
}
