package team.blogserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.ark.core.response.C;
import team.ark.core.response.R;
import team.ark.core.security.JwtAuthenticationFilter;
import team.ark.core.security.JwtUtils;
import team.ark.core.util.WebUtils;

import javax.annotation.Resource;

/**
 * Spring Security配置
 *
 * @author Ashinch
 * @date 2021/02/06
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    private AuthenticationProvider authenticationProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 权限配置
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .and().logout().logoutUrl("/user/logout")
                // 登出成功处理器
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                    WebUtils.write(response, R.with(C.OK));
                }).and().csrf().disable().httpBasic()
                // 未登录处理器
                .authenticationEntryPoint((request, response, authenticationException) -> {
                    WebUtils.write(response, R.with(C.USER_NOT_LOGIN));
                })
                .and().exceptionHandling()
                // 拒绝访问处理器
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    WebUtils.write(response, R.with(C.ACCESS_DENIED));
                })
                // 开启表单登录
                .and().formLogin().loginProcessingUrl("/user/login")
                // 登录成功处理器
                .successHandler((request, response, authentication) -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String token = JwtUtils.createAccessToken(userDetails);
                    WebUtils.write(response, R.with(C.OK, token));
                })
                // 登录失败处理器
                .failureHandler((request, response, authenticationException) -> {
                    WebUtils.write(response, R.error(authenticationException.getMessage()));
                });
        // 禁用Session, 设置无状态会话
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT验证过滤器
        http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
    }
}
