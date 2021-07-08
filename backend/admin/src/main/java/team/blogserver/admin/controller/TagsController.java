package team.blogserver.admin.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.C;
import team.ark.core.response.R;
import team.blogserver.admin.service.CategoryService;
import team.blogserver.admin.service.TagsService;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Tags;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Jzj
 * @date 2021/07/08
 */

@Slf4j
@RestController
@Api(value = "后台标签接口")
@RequestMapping("/tags")
public class TagsController {
    @Resource
    private TagsService tagsService;

    @ApiOperation("博客标签列表")
    @GetMapping("/list")
    public R list(){
        List<Tags> list = tagsService.list();
        return R.ok(list);
    }
    @ApiOperation("新增博客标签")
    @PostMapping("/save")
    public R save(@RequestBody Tags tags){
        boolean result = tagsService.save(tags);
        if(result){
            return R.ok(null);
        }
        return R.error("新增失败");
    }

    @ApiOperation("删除博客标签")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable Long id){
        boolean result = tagsService.removeById(id);
        if(result){
            return R.ok(null);
        }
        return R.error("删除失败");
    }
    @ApiOperation("根据ID查询博客标签")
    @GetMapping("/getById/{id}")
    public R getById(@PathVariable Long id){
        Tags model = tagsService.getById(id);
        if(model!=null){
            return R.ok(model);
        }
        return R.error("数据不存在");
    }
    @ApiOperation("修改博客标签")
    @PutMapping("/update")
    public R updateById(@RequestBody Tags tags){
        boolean result = tagsService.updateById(tags);
        if(result){
            return R.ok(null);
        }
        return R.error("修改失败");
    }
}
