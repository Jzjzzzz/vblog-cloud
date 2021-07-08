package team.blogserver.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.WebConfigService;
import team.blogserver.common.model.domain.WebConfig;

import javax.annotation.Resource;

/**
 *
 * @author Jzj
 * @date 2021/07/08
 */

@Slf4j
@RestController
@RequestMapping("/config")
@Api(value = "后台网站设置接口")
public class WebConfigController {
    @Resource
    private WebConfigService webConfigService;

    @ApiOperation("获取网站配置")
    @GetMapping("/getWebConfig")
    public R getWebConfig(){
        WebConfig webConfig = webConfigService.getWebConfig();
        return R.ok(webConfig);
    }

    @ApiOperation("修改网站配置")
    @PutMapping("/editWebConfig")
    public R editWebConfig(@RequestBody WebConfig webConfig){
        boolean result = webConfigService.updateById(webConfig);
        Assert.notNull(result, "修改失败");
        return R.ok(null);
    }


}