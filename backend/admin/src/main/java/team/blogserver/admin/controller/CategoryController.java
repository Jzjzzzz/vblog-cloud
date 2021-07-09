package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.CategoryService;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Tags;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Slf4j
@RestController
@RequestMapping("/category")
@Api(value = "后台分类接口")
@CrossOrigin //跨域
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @ApiOperation("博客分类列表")
    @GetMapping("/list/{page}/{limit}")
    public R list(@PathVariable Long page, @PathVariable Long limit,String cateName) {
        Page<Category> pageParam = new Page<>(page, limit);
        IPage<Category> listPage = categoryService.listPage(pageParam,cateName);
        return R.ok(listPage);
    }

    @ApiOperation("新增博客分类")
    @PostMapping("/save")
    public R save(@RequestBody Category category) {
        category.setSort(0);
        boolean result = categoryService.save(category);
        if (result) {
            return R.ok();
        }
        return R.error("新增失败");
    }

    @ApiOperation("删除博客分类")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable Integer id) {
        boolean result = categoryService.removeById(id);
        if (result) {
            return R.ok();
        }
        return R.error("删除失败");
    }

    @ApiOperation("根据ID查询博客分类")
    @GetMapping("/getById/{id}")
    public R getById(@PathVariable Integer id) {
        Category model = categoryService.getById(id);
        if (model != null) {
            return R.ok(model);
        }
        return R.error("数据不存在");
    }

    @ApiOperation("修改博客分类")
    @PutMapping("/update")
    public R updateById(@RequestBody Category category) {
        boolean result = categoryService.updateById(category);
        if (result) {
            return R.ok();
        }
        return R.error("修改失败");
    }

    @ApiOperation("批量删除分类")
    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Category> categoryList){
        boolean result = categoryService.deleteBatchCategory(categoryList);
        if(result){
            return R.ok();
        }
        return R.error("批量删除失败");
    }

    @ApiOperation("根据id置顶博客分类")
    @PutMapping("/stickyBlogById/{id}")
    public R stickyBlogById(@PathVariable Integer id){
        boolean result = categoryService.topBlogById(id);
        if(result){
            return R.ok();
        }
        return R.error("操作失败");
    }
}
