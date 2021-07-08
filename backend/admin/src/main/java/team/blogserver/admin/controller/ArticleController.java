package team.blogserver.admin.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ark.core.response.R;
import team.blogserver.admin.service.ArticleService;

import javax.annotation.Resource;

/**
 * 文章控制器
 *
 * @author Ashinch
 * @date 2021/07/08
 */
@Slf4j
@RestController
@RequestMapping("/article")
@Api(value = "文章API")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    /**
     * 获取所有文章
     */
    @GetMapping("/getAll")
    public R getAll() {
        return R.ok(articleService.getAll());
    }
}
