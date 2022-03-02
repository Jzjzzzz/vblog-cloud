package com.jzj.sms.client.fallback;

import com.jzj.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Jzj
 * @Date 2021/11/28 15:55
 * @Version 1.0
 */
@Component
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public Boolean getOneByMobile(String mobile) {
        log.info("走服务降级！:getOneByMobile");
        return false;
    }

    @Override
    public Boolean getTest() {
        log.info("走服务降级！:getTest");
        return false;
    }
}
