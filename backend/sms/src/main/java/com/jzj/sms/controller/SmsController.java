package com.jzj.sms.controller;

import com.jzj.sms.service.TencentSmsService;
import com.jzj.sms.client.CoreUserInfoClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ark.core.response.R;
import team.ark.core.util.RandomUtils;
import team.ark.core.util.RegexValidateUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jzj
 * @Date 2021/11/25 22:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/sms")
@Api(value = "短信管理")
@Slf4j
public class SmsController {
    @Resource
    private TencentSmsService tencentSmsService;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @Resource
    private RedisTemplate redisTemplate;


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
        //判断是否有该用户
        Boolean isUser = coreUserInfoClient.getOneByMobile(mobile);
        if (isUser == false) {
            return R.error("用户不存在");
        }
        tencentSmsService.send(mobile, code);
        //将验证码存入redis中
        redisTemplate.opsForValue().set("vblog:sms:code:"+mobile,code,5, TimeUnit.MINUTES);
        return R.ok();
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"), //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "5"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少后跳闸
    })
    @ApiOperation("熔断测试")
    @GetMapping("/test/{number}")
    public R test(@PathVariable Integer number) {
        if(number<0) throw new ArithmeticException("数字异常");
        Boolean test = coreUserInfoClient.getTest();
        return R.ok();
    }
}
