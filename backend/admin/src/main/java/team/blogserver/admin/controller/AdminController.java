package team.blogserver.admin.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ark.core.cache.store.CacheStore;
import team.ark.core.response.R;
import team.ark.core.security.JwtUtils;
import team.blogserver.admin.service.UserService;

import javax.annotation.Resource;

@Slf4j
@RestController()
@Api(value = "测试集")
public class AdminController {
    @Resource
    private UserService userService;
    @Resource
    private CacheStore cacheStore;

//    @GetMapping("/add")
//    public R add() {
//        return R.ok(userService.insert());
//    }

    @GetMapping("/cache")
    public R cache() {
        if (cacheStore.isExist("cache")) {
            return R.ok(null);
        } else {
            cacheStore.put("cache", 1);
            return R.error(null);
        }
    }

    @GetMapping("/jwt")
    public R jwt() {
        log.info("secret {}", JwtUtils.JWT.getSecretKey());
        log.info("username {}", JwtUtils.getUsername());
        log.info("id {}", JwtUtils.<Long>getId());
//        Assert.notNull(null, "用户不存在");
        return R.judge(JwtUtils.getUser());
    }
}
