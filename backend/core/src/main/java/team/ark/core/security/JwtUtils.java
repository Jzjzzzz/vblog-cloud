package team.ark.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import team.ark.core.util.DateTimeUtils;
import team.ark.core.util.JsonUtils;
import team.ark.core.util.SpringUtils;
import team.ark.core.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * JWT工具类
 *
 * @author Ashinch
 * @date 2021/02/06
 */
@Slf4j
public class JwtUtils {
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final JwtProperties JWT;

    static {
        JWT = SpringUtils.getBean(JwtProperties.class);
    }

    /**
     * 获取当前Key所含ID
     */
    @SuppressWarnings("unchecked")
    public static <T> T getId() {
        return (T) SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials();
    }

    /**
     * 获取当前Key所含Username
     */
    public static String getUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    /**
     * 获取当前Key所含实现自{@linkplain IUserDetails}的对象,
     * 无效Key或匿名用户{@link #ANONYMOUS_USER}返回Null
     */
    public static IUserDetails getUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (ANONYMOUS_USER.equals(String.valueOf(principal))) {
            return null;
        }
        return (IUserDetails) principal;
    }

    /**
     * 创建账户令牌
     *
     * @param userDetails 账户信息
     *
     * @return Token
     */
    public static String createAccessToken(IUserDetails userDetails) {
        // 设置JWT
        String token = Jwts.builder()
                // 用户Id
                .setId(String.valueOf(userDetails.getId()))
                // 主题
                .setSubject(userDetails.getUsername())
                // 签发时间
                .setIssuedAt(DateTimeUtils.now())
                // 签发者
                .setIssuer("Ash")
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(JWT.getExpiration())))
                // 签名算法、密钥
                .signWith(SignatureAlgorithm.HS512, JWT.getSecretKey())
                // 自定义其他属性，如用户组织机构ID，用户所拥有的角色，用户权限信息等
                .claim("authorities", JsonUtils.toJson(userDetails.getAuthorities()))
                .compact();
        return JWT.getTokenPrefix() + token;
    }

    /**
     * 解析账户令牌
     *
     * @param token Token信息
     *
     * @return 账户信息
     */
    public static IUserDetails parseAccessToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            // 去除JWT前缀
            token = token.substring(JWT.getTokenPrefix().length());
            // 解析Token
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            // 获取用户信息
            IUserDetails userDetails = new UserDetails();
            userDetails.setId(Integer.parseInt(claims.getId()));
            userDetails.setUsername(claims.getSubject());
            // 获取角色
            String authorities = claims.get("authorities").toString();
            List<GrantedAuthority> authorityList = new ArrayList<>();
            if (StringUtils.isNotEmpty(authorities)) {
                authorityList = JsonUtils.<List<Map<String, String>>>toObj(authorities)
                        .stream()
                        .map(i -> new SimpleGrantedAuthority(i.get("authority")))
                        .distinct()
                        .collect(Collectors.toList());
            }
            userDetails.setAuthorities(authorityList);
            return userDetails;
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
        }
        return null;
    }
}
