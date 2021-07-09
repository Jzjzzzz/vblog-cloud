package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.ArticleService;
import team.blogserver.common.model.domain.Article;

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
    @ApiOperation("获取所有文章（不含详情）")
    @GetMapping("/list")
    public R list() {
        return R.judge(articleService.list(new QueryWrapper<Article>()
                .select(Article.class, i -> !i.getProperty().endsWith("Content"))));
    }

    /**
     * 获取文章内容
     */
    @ApiOperation("获取文章内容（含详情）")
    @GetMapping("/details")
    public R details(Integer id) {
        return R.judge(articleService.getById(id));
    }

    /**
     * 更新文章
     *
     * @param article 文章实体类
     */
    @ApiOperation(value = "更新文章", notes = "文章ID必需，其他传什么就会更新什么，不想更新的字段切记传null或者不传")
    @PutMapping("/update")
    public R update(@RequestBody Article article) {
        return R.judge(articleService.updateById(article));
    }

    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    @ApiOperation("根据ID删除文章")
    @DeleteMapping("/delete")
    public R delete(Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setState(2);
        return R.judge(articleService.updateById(article));
    }
}
