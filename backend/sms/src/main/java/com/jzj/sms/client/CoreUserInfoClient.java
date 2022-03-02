package com.jzj.sms.client;

import com.jzj.sms.client.fallback.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Jzj
 * @Date 2021/11/27 15:56
 * @Version 1.0
 */
@FeignClient(value = "BLOGSERVER",fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {
    @GetMapping("user/getOneByMobile/{mobile}")
    Boolean getOneByMobile(@PathVariable String mobile);

    @GetMapping("user/getTest")
     Boolean getTest();
}
