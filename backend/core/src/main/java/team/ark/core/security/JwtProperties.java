package team.ark.core.security;

import lombok.Data;

import java.io.Serializable;

/**
 * JWT配置
 *
 * @author Ashinch
 * @date 2021/02/06
 */
@Data
public class JwtProperties implements Serializable {
    private static final long serialVersionUID = 4453205788254045985L;
    /**
     * 密匙Key
     */
    private String secretKey = "ImV4cCI6MTYxMjc4MzY5";
    /**
     * HeaderKey
     */
    private String tokenHeader = "X-Token";
    /**
     * Token前缀
     */
    private String tokenPrefix = "";
    /**
     * 过期时间 (单位: 秒)
     */
    private Integer expiration = 86400;
}
