package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.UserService;
import team.blogserver.common.model.domain.User;

import javax.annotation.Resource;

/**
 * @author lshz
 */
@Slf4j
@RestController
@Api(value = "用户管理")
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation("用户列表")
    @GetMapping("/list/{page}/{limit}/{name}")
    public R list(@PathVariable Long page, @PathVariable Long limit, String name) {
        Page<User> pageParam = new Page<>(page, limit);
        IPage<User> listPage = userService.listPage(pageParam, name);
        return R.ok(listPage);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable Long id) {
        boolean result = userService.removeById(id);
        if (result) {
            return R.ok(null);
        }
        return R.error("删除失败");
    }

    @ApiOperation("禁用用户")
    @PutMapping("/update/{id}")
    public R updateById(@PathVariable Long id) {
        boolean result = userService.updateById(id);
        if (result) {
            return R.ok(null);
        }
        return R.error("禁用失败");
    }
}
