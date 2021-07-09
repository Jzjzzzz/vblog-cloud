package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.TagsService;
import team.blogserver.common.model.domain.Tags;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Slf4j
@RestController
@Api(value = "后台标签接口")
@RequestMapping("/tags")
@CrossOrigin //跨域
public class TagsController {
    @Resource
    private TagsService tagsService;

    @ApiOperation("博客标签列表")
    @GetMapping("/list/{page}/{limit}")
    public R list(@PathVariable Long page, @PathVariable Long limit,String tagName) {
        Page<Tags> pageParam = new Page<>(page, limit);
        IPage<Tags> listPage = tagsService.listPage(pageParam,tagName);
        return R.ok(listPage);
    }

    @ApiOperation("新增博客标签")
    @PostMapping("/save")
    public R save(@RequestBody Tags tags) {
        tags.setSort(0);
        boolean result = tagsService.save(tags);
        if (result) {
            return R.ok();
        }
        return R.error("新增失败");
    }

    @ApiOperation("删除博客标签")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable Integer id) {
        boolean result = tagsService.removeById(id);
        if (result) {
            return R.ok();
        }
        return R.error("删除失败");
    }

    @ApiOperation("根据ID查询博客标签")
    @GetMapping("/getById/{id}")
    public R getById(@PathVariable Integer id) {
        Tags model = tagsService.getById(id);
        if (model != null) {
            return R.ok(model);
        }
        return R.error("数据不存在");
    }

    @ApiOperation("修改博客标签")
    @PutMapping("/update")
    public R updateById(@RequestBody Tags tags) {
        boolean result = tagsService.updateById(tags);
        if (result) {
            return R.ok();
        }
        return R.error("修改失败");
    }

    @ApiOperation("批量删除标签")
    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Tags> tagList){
        boolean result = tagsService.deleteBatchTag(tagList);
        if(result){
            return R.ok();
        }
        return R.error("批量删除失败");
    }

    @ApiOperation("根据id置顶博客标签")
    @PutMapping("/stickyBlogById/{id}")
    public R stickyBlogById(@PathVariable Integer id){
        boolean result = tagsService.topBlogById(id);
        if(result){
            return R.ok();
        }
        return R.error("操作失败");
    }
}
