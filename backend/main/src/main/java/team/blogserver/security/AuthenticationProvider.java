package team.blogserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import team.ark.core.exception.ArkNotFoundException;
import team.ark.core.response.C;
import team.ark.core.util.JsonUtils;
import team.ark.core.util.StringUtils;
import team.ark.core.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 身份验证处理器
 *
 * @author Ashinch
 * @date 2021/02/06
 */
@Slf4j
@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 身份验证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = WebUtils.getRequest();
        if (request == null) {
            throw new ArkNotFoundException(C.ERROR.getMessage());
        }
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            Map<String, String> stream;
            try {
                stream = JsonUtils.toObj(request.getInputStream());
                username = stream.get("username");
                password = stream.get("password");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
        // 账户不存在
        if (userDetails == null) {
            throw new UsernameNotFoundException(C.USER_NOT_FOUND.getMessage());
        }
        // 用户名或密码错误
        if (!new BCryptPasswordEncoder().matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(C.BAD_CREDENTIALS.getMessage());
        }
        // TODO: 待添加账户锁定/过期等判断
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    /**
     * 支持指定的身份验证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
