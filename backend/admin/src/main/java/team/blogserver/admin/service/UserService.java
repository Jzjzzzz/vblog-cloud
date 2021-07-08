package team.blogserver.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ark.core.exception.ArkServiceException;
import team.blogserver.common.mapper.UserMapper;
import team.blogserver.common.mapper.RolesUserMapper;
import team.blogserver.common.model.domain.User;
import team.blogserver.common.model.domain.RolesUser;

import javax.annotation.Resource;

/**
 * 用户Service
 *
 * @author Ashinch
 * @date 2021/02/05
 */
@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RolesUserMapper rolesUserMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean insert() {
        User user = new User();
        user.setUsername("13630");
        user.setPassword(new BCryptPasswordEncoder().encode("123"));
        if (userMapper.insert(user) < 1) {
            log.error("userMapper.insert(user) < 1: {}", user);
            throw new ArkServiceException("注册失败");
        }
        RolesUser rolesUser = new RolesUser();
        rolesUser.setUid(user.getId());
        rolesUser.setRid(3);
        if (rolesUserMapper.insert(rolesUser) < 1) {
            log.error("userRoleMapper.insert(userRole) < 1: {}", rolesUser);
            throw new ArkServiceException("注册失败");
        }
        return true;
    }
}
