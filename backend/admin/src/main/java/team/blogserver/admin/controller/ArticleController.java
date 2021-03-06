package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.ArticleService;
import team.blogserver.admin.service.CategoryService;
import team.blogserver.common.mapper.ArticleTagsMapper;
import team.blogserver.common.model.domain.Article;
import team.blogserver.common.model.domain.ArticleTags;
import team.blogserver.common.model.domain.Tags;

import javax.annotation.Resource;
import java.util.HashMap;

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
    @Resource
    private CategoryService categoryService;
    @Resource
    private ArticleTagsMapper articleTagsMapper;

    /**
     * 获取所有文章
     */
    @ApiOperation("获取所有文章（不含详情）")
    @GetMapping("/list/{page}/{limit}")
    public R list(@PathVariable Long page, @PathVariable Long limit, String title,Long state) {
        return R.judge(articleService.listPage(new Page<>(page, limit), title,state));
    }

    /**
     * 根据文章状态筛选所有文章
     */
    @ApiOperation("根据文章状态筛选所有文章（不含详情）")
    @GetMapping("/listByState/{page}/{limit}/{state}")
    public R listByState(@PathVariable Long page, @PathVariable Long limit,
                         @PathVariable Integer state) {
        return R.judge(articleService.listPageByState(new Page<>(page, limit), state));
    }

    /**
     * 获取所有文章（倒序排序）
     */
    @ApiOperation("获取所有文章（倒序排序, 不含详情）")
    @GetMapping("/listOrderByDESC/{page}/{limit}/{field}")
    public R listOrderByDESC(@PathVariable Long page, @PathVariable Long limit,
                             @PathVariable String field) {
        return R.judge(articleService.listPageOrderByDESC(new Page<>(page, limit), field));
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
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("aid", article.getId());
        articleTagsMapper.deleteByMap(map);
        for (Tags tags : article.getTags()) {
            articleTagsMapper.insert(new ArticleTags(article.getId(), tags.getId()));
        }
        return R.judge(articleService.updateById(article));
    }

    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    @ApiOperation("根据ID删除文章")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('超级管理员')")  //TODO: 临时测试权限
    public R delete(@PathVariable Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setState(2);
        return R.judge(articleService.updateById(article));
    }

    /**
     * 设置文章状态为草稿箱
     *
     * @param id 文章ID
     */
    @ApiOperation("根据ID设置文章状态为草稿")
    @PutMapping("/drafts/{id}")
    public R drafts(@PathVariable Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setState(0);
        return R.judge(articleService.updateById(article));
    }

    /**
     * 设置文章状态为已发表
     *
     * @param id 文章ID
     */
    @ApiOperation("根据ID设置文章状态为已发表")
    @PutMapping("/publish/{id}")
    public R publish(@PathVariable Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setState(1);
        return R.judge(articleService.updateById(article));
    }

    /**
     * 添加文章
     */
    @ApiOperation(value = "添加文章（需登录）", notes = "不需要传id, uid, publishDate, editTime, 以当前登录的用户身份添加文章")
    @PostMapping("/add")
    public R add(@RequestBody Article article) {
        return R.judge(articleService.addArticleTags(article));
    }

    /**
     * 获取所有分类和标签
     */
    @ApiOperation("获取所有分类和标签")
    @GetMapping("/getTagCategory")
    public R getTagCategory() {
        return R.judge(categoryService.getTagCategory());
    }

    /**
     * 获取所有文章浏览量综合
     */
    @ApiOperation("获取所有文章浏览量综合")
    @GetMapping("/getAllPageView")
    public R getAllPageView() {
        return R.judge(articleService.list(new QueryWrapper<Article>()
                .select(Article.class, i -> "pageView".equals(i.getProperty())))
                .stream().mapToInt(Article::getPageView)
                .sum());
    }
}
