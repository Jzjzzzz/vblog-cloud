package team.blogserver.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.ark.core.util.BeanUtil;
import team.blogserver.common.mapper.UserMapper;
import team.blogserver.common.mapper.RolesUserMapper;
import team.blogserver.common.model.domain.User;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * Security用户Service
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RolesUserMapper rolesUserMapper;

    @Override
    public org.springframework.security.core.userdetails.UserDetails
    loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            return null;
        }
        UserDetails userDetails = new UserDetails();
        BeanUtil.copy(user, userDetails);
        userDetails.setAuthorities(rolesUserMapper.selectRoleByUid(user.getId())
                .stream()
                .map(i -> new SimpleGrantedAuthority(i.getName()))
                .collect(Collectors.toList()));
        return userDetails;
    }
}
