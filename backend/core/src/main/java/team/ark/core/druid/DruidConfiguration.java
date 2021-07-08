package team.ark.core.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.ark.core.util.StringUtils;

/**
 * Druid监控页面配置
 *
 * @author Ashinch
 * @date 2021/02/20
 */
@Slf4j
@Configuration
public class DruidConfiguration {
    /**
     * 访问地址(相对路径)
     */
    @Value("${ark.druid.url:/druid}")
    private String url;
    /**
     * 登录用户名
     */
    @Value("${ark.druid.username:ark}")
    private String username;
    /**
     * 登录密码
     */
    @Value("${ark.druid.password:ark276.+?}")
    private String password;
    /**
     * 是否允许重置数据
     */
    @Value("${ark.druid.reset:false}")
    private String reset;
    /**
     * 白名单
     */
    @Value("#{'${ark.druid.allow:127.0.0.1}'.split(',')}")
    private String[] allow;
    /**
     * 黑名单
     */
    @Value("#{'${ark.druid.deny:}'.split(',')}")
    private String[] deny;

    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean
                = new ServletRegistrationBean<>(new StatViewServlet(), url + "/*");
        servletRegistrationBean.addInitParameter("allow", StringUtils.joinWith(",", (Object[]) allow));
        servletRegistrationBean.addInitParameter("deny", StringUtils.joinWith(",", (Object[]) deny));
        servletRegistrationBean.addInitParameter("loginUsername", username);
        servletRegistrationBean.addInitParameter("loginPassword", password);
        servletRegistrationBean.addInitParameter("resetEnable", reset);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean
                = new FilterRegistrationBean<>(new WebStatFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略文件
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.png,*.css,*.ico," + url + "/*");
        return filterRegistrationBean;
    }
}
