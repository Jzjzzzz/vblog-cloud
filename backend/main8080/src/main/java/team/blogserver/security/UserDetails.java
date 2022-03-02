package team.blogserver.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import team.ark.core.security.IUserDetails;
import team.blogserver.common.model.domain.User;

import java.util.Collection;
import java.util.List;

/**
 * 实现Spring Security的UserDetails接口
 *
 * @author Ashinch
 * @date 2021/01/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User", description = "用户实体类", parent = User.class)
public class UserDetails extends User implements IUserDetails {
    private static final long serialVersionUID = 1422452511614520433L;
    @ApiModelProperty("角色")
    private transient List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public void setId(Object id) {
        this.setId((Integer) id);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(Object username) {
        super.setUsername((String) username);
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
        return super.getEnabled();
    }
}
