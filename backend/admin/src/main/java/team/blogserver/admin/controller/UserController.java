package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.ark.core.security.JwtUtils;
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
@CrossOrigin //跨域
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 查询用户列表
     *
     * @param page  当前页
     * @param limit 每页显示数
     * @param name  输入用户名或者昵称查找
     *
     * @return 用户列表
     */
    @ApiOperation(value = "用户列表", notes = "传name既是根据用户名或者昵称模糊查找，为空则查全部")
    @GetMapping("/list/{page}/{limit}/{name}")
    public R list(@PathVariable Long page, @PathVariable Long limit, String name) {
        Page<User> pageParam = new Page<>(page, limit);
        IPage<User> listPage = userService.listPage(pageParam, name);
        return R.ok(listPage);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable Long id) {
        boolean result = userService.removeById(id);
        if (result) {
            return R.ok();
        }
        return R.error("删除失败");
    }

    /**
     * 禁用或启用用户
     *
     * @param id 用户id
     */
    @ApiOperation("禁用或启用用户")
    @PutMapping("/update/{id}")
    public R updateById(@PathVariable Long id) {
        boolean result = userService.updateById(id);
        if (result) {
            return R.ok();
        }
        return R.error("禁用失败");
    }

    /**
     * 新增用户
     *
     * @param user 用户实体类
     */
    @ApiOperation("新增用户")
    @PostMapping("/save")
    public R save(@RequestBody User user) {
        boolean result = userService.save(user);
        if (result) {
            return R.ok();
        }
        return R.error("新增失败");
    }

    /**
     * 更新用户
     *
     * @param user 用户实体类
     */
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public R update(@RequestBody User user) {
        return R.judge(userService.updateById(user));
    }

    /**
     * 根据当前Token获取用户信息详情
     */
    @ApiOperation("根据当前Token获取用户信息详情")
    @GetMapping("/my")
    public R my() {
        return R.judge(userService.getById(JwtUtils.<Long>getId()));
    }
}
