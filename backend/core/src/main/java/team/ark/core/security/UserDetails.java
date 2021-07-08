package team.ark.core.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * 实现包装的UserDetails接口, 暂时用于模块中JWT令牌生成,
 * 故使用本依赖的模块应实现同样的{@linkplain IUserDetails}接口
 *
 * @author Ashinch
 * @date 2021/01/28
 */
public class UserDetails implements IUserDetails {
    private static final long serialVersionUID = -8167282813628416952L;
    private Object id;
    private Object username;
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public void setAuthorities(List<GrantedAuthority> authorityList) {
        this.authorities = authorityList;
    }

    @Override
    public void setId(Object id) {
        this.id = id;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void setUsername(Object subject) {
        username = subject;
    }

    @Override
    public String getUsername() {
        return String.valueOf(username);
    }

    /**
     * 无需调用, 仅应付实现
     * {@linkplain org.springframework.security.core.userdetails.UserDetails}
     * 必须重写的方法
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 证书是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否已启用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
