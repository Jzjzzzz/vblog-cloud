import cn.hutool.core.lang.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jzj
 * @Date 2021/12/5 17:43
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTests {
    @Resource
    private RedisTemplate redisTemplate;
    @Test
    public void saveDict(){
        //向数据库中存储string类型的键值对, 过期时间5分钟
        redisTemplate.opsForValue().set("code", "1111", 5, TimeUnit.MINUTES);
    }
}
