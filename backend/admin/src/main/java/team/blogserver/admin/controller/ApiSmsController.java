package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.cache.store.CacheStore;
import team.ark.core.response.R;
import team.ark.core.util.RandomUtils;
import team.ark.core.util.RegexValidateUtils;
import team.blogserver.admin.service.TengXunSmsService;
import team.blogserver.admin.service.UserService;
import team.blogserver.common.model.domain.User;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@CrossOrigin //跨域
@RestController
@RequestMapping("/sms")
@Api(value = "短信管理")
@Slf4j
public class ApiSmsController {
    @Resource
    private TengXunSmsService tengXunSmsService;
    @Resource
    private UserService userService;
    @Resource
    private CacheStore cacheStore;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(@PathVariable String mobile) {
        //手机号码不能为空
        if (mobile == null || mobile.equals("")) {
            return R.error("手机号不能为空");
        }
        //是否是合法的手机号码
        if (!RegexValidateUtils.checkCellphone(mobile)) {
            return R.error("手机号格式不正确");
        }
        String code = RandomUtils.getFourBitRandom();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile", mobile);
        //判断是否有该用户
        User user = userService.getOne(userQueryWrapper);
        if (user == null) {
            return R.error("用户不存在");
        }
        log.info("user:" + user);
        tengXunSmsService.send(mobile, code);
        //将验证码存入redis中
        cacheStore.put("mobile-code", code, 3, TimeUnit.MINUTES);
        return R.ok();
    }
}
