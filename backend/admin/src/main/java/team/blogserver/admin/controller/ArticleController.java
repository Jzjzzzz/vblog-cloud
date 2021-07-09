package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.ark.core.security.JwtUtils;
import team.blogserver.admin.service.ArticleService;
import team.blogserver.common.model.domain.Article;
import team.blogserver.common.model.domain.User;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;

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
    @GetMapping("/list/{page}/{limit}")
    public R list(@PathVariable Long page, @PathVariable Long limit, String title) {
        return R.judge(articleService.listPage(new Page<>(page, limit), title));
    }

    /**
     * 获取文章内容
     */
    @ApiOperation("获取文章内容（含详情）")
    @GetMapping("/details/{id}")
    public R details(@PathVariable Integer id) {
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
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setState(2);
        return R.judge(articleService.updateById(article));
    }

    /**
     * 添加文章
     */
    @ApiOperation(value = "添加文章（需登录）",notes = "不需要传id, uid, publishDate, editTime, 以当前登录的用户身份添加文章")
    @PostMapping("/add")
    public R add(@RequestBody Article article) {
        article.setUid(JwtUtils.<Integer>getId());
        article.setPublishDate(new Date());
        article.setEditTime(new Date());
        return R.judge(articleService.save(article));
    }
}
