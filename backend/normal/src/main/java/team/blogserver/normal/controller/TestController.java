package team.blogserver.normal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.ark.core.response.R;
import team.blogserver.common.mapper.UserMapper;
import team.blogserver.common.model.domain.Roles;
import team.blogserver.common.model.domain.User;
import team.blogserver.common.model.dto.UserDTO;

import javax.annotation.Resource;

@Slf4j
@RestController
@Api(value = "测试集")
public class TestController {
    @Resource
    private UserMapper userMapper;

    @GetMapping("/test")
    @ApiOperation(value = "测试", notes = "用于测试")
    public R test(@ApiParam("参数文本") String a) {
        Roles roles = new Roles();
        User user = userMapper.selectById(4);
        user.setPassword("111");
//        userMapper.deleteByIdWithFill(user);
        Page<User> page = new Page<>(2, 1);
        userMapper.selectPage(page, null);
        System.out.println(page.getRecords());
        log.info("page: {}", page.toString());
        log.info("page.getTotal(): {}", page.getTotal());
//        userMapper.selectOne(new QueryWrapper<User>()
//                .lambda()
//                .eq(User::getPhone, "sf"));
//        return R.judge(userMapper.selectList(null));
        return R.judge(new UserDTO(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/auth")
    public R auth() {
        return R.ok(null);
    }

    @PostMapping("/raw")
    public R raw(@RequestBody Param data) {
        return R.ok(data);
    }

    @Data
    private static class Param {
        private String username;
        private String password;
    }
}
