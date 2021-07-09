package team.blogserver.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.blogserver.common.mapper.WebConfigMapper;
import team.blogserver.common.model.domain.WebConfig;

import java.util.Date;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Service
@Slf4j
public class WebConfigService extends ServiceImpl<WebConfigMapper, WebConfig> {
    public WebConfig getWebConfig() {
        WebConfig webConfig = baseMapper.selectOne(null);
        if (webConfig == null) {
            WebConfig model = new WebConfig();
            model.setCreatetime(new Date());
            baseMapper.insert(model);
            return baseMapper.selectOne(null);
        }
        return webConfig;
    }

}
