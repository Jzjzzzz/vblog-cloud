package team.ark.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import team.ark.core.response.C;
import team.ark.core.response.R;
import team.ark.core.util.SpringUtils;
import team.ark.core.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT验证过滤器
 *
 * @author Ashinch
 * @date 2021/02/06
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        JwtProperties jwt = SpringUtils.getBean(request, JwtProperties.class);
        String token = request.getHeader(jwt.getTokenHeader());
        String requestURI = request.getRequestURI();
        if (token == null && !requestURI.startsWith("/doc.html") && !requestURI.startsWith(
                "/webjars/bycdao-ui/") && !requestURI.startsWith("/druid")) {
            log.info("request.getRequestURI(): {}",request.getRequestURI());
            WebUtils.write(response, R.with(C.USER_NOT_LOGIN));
            return;
        } else if (token != null && token.startsWith(jwt.getTokenPrefix())) {
            IUserDetails userDetails = JwtUtils.parseAccessToken(token);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                userDetails.getId(),
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
