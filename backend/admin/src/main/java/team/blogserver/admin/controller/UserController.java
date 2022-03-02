package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import team.ark.core.cache.store.CacheStore;
import team.ark.core.oss.OssProperties;
import team.ark.core.response.R;
import team.ark.core.security.JwtUtils;
import team.ark.core.security.UserDetails;
import team.ark.core.util.BeanUtil;
import team.blogserver.admin.service.UserService;
import team.blogserver.common.mapper.RolesUserMapper;
import team.blogserver.common.model.domain.RolesUser;
import team.blogserver.common.model.domain.User;
import team.blogserver.common.model.vo.MobileCode;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * @author lshz
 * @date 2021/07/08
 */
@Slf4j
@RestController
@Api(value = "用户管理")
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RolesUserMapper rolesUserMapper;
    @Resource
    private CacheStore cacheStore;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

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
    @GetMapping("/list/{page}/{limit}")
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
            RolesUser rolesUser = new RolesUser();
            rolesUser.setUid(user.getId());
            rolesUser.setRid(2);
            return R.judge(rolesUserMapper.insert(rolesUser));
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

    @ApiOperation("批量删用户")
    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<User> userList) {
        boolean result = userService.deleteBatchTag(userList);
        if (result) {
            return R.ok();
        }
        return R.error("批量删除失败");
    }

    /**
     * 根据当前Token获取用户信息详情
     */
    @ApiOperation("根据当前Token获取用户信息详情")
    @GetMapping("/my")
    public R my() {
        return R.judge(userService.getById(JwtUtils.<Long>getId()));
    }

    @PostMapping("/loginMobile")
    public R loginMobile(@RequestBody MobileCode mobileCode) {
        String mobile = mobileCode.getMobile();
        String code = mobileCode.getCode();
        // 判断手机号键和验证码值是否在缓存中
        Assert.isTrue(redisTemplate.opsForValue().get("vblog:sms:code:"+mobile).equals(code), "验证码不正确");
        // 消费缓存
        cacheStore.delete(mobile);
        // 生成Security用户
        User user = userService.getOne(new QueryWrapper<User>().eq("mobile", mobile));
        UserDetails userDetails = new UserDetails();
        BeanUtil.copyProperties(user, userDetails);
        // 附加权限
        userDetails.setAuthorities(rolesUserMapper.selectRoleByUid(user.getId())
                .stream()
                .map(i -> new SimpleGrantedAuthority(i.getName()))
                .collect(Collectors.toList()));
        // 生成Token
        return R.ok(JwtUtils.createAccessToken(userDetails));
    }


    @ApiOperation("根据手机号查询用户是否存在")
    @GetMapping("/getOneByMobile/{mobile}")
    public Boolean getOneByMobile(@PathVariable String mobile) {
        log.info("执行了8080");
        User user = userService.getOne(new QueryWrapper<User>().eq("mobile",mobile));
        if(user!=null){
            return true;
        }
        return false;
    }
    @ApiOperation("熔断测试")
    @GetMapping("/getTest")
    public Boolean getTest() {
        log.info(OssProperties.BUCKET_NAME);
        return true;
    }
}
