package team.blogserver.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ark.core.exception.ArkServiceException;
import team.ark.core.util.StringUtils;
import team.blogserver.common.mapper.TagsMapper;
import team.blogserver.common.mapper.UserMapper;
import team.blogserver.common.mapper.RolesUserMapper;
import team.blogserver.common.model.domain.Tags;
import team.blogserver.common.model.domain.User;


/**
 * @author lshz
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public IPage<User> listPage(Page<User> pageParam) {
        return baseMapper.selectPage(pageParam, null);
    }

    public IPage<User> listPage(Page<User> pageParam, String name) {
        QueryWrapper<User> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper
                .like(StringUtils.isNotBlank(name), "nickname", name)
                .or()
                .like(StringUtils.isNotBlank(name), "username", name);
        return baseMapper.selectPage(pageParam, tagQueryWrapper);
    }

    public boolean updateById(Long id) {
        User user=baseMapper.selectById(id);
        user.setEnabled( !user.getEnabled());
        baseMapper.updateById(user);
        return true;
    }
}
