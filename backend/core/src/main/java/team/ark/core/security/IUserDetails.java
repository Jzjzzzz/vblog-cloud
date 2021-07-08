package team.ark.core.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * 自定义包装Spring Security的UserDetails接口
 *
 * <p>
 * 使用本依赖时处理Spring Security的UserDetails应当继承自该接口,
 * 而不是{@linkplain org.springframework.security.core.userdetails.UserDetails}
 *
 * @author Ashinch
 * @date 2021/01/28
 */
public interface IUserDetails extends org.springframework.security.core.userdetails.UserDetails {
    /**
     * Get Spring Security UserDetails ID
     */
    Object getId();

    /**
     * Set Spring Security UserDetails ID
     *
     * @param id ID
     */
    void setId(Object id);

    /**
     * Get Spring Security UserDetails Username
     */
    @Override
    String getUsername();

    /**
     * Set Spring Security UserDetails Username
     *
     * @param subject subject
     */
    void setUsername(Object subject);

    /**
     * Get Spring Security UserDetails Authorities
     */
    @Override
    Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * Set Spring Security UserDetails Authorities
     *
     * @param authorityList Authorities
     */
    void setAuthorities(List<GrantedAuthority> authorityList);
}
