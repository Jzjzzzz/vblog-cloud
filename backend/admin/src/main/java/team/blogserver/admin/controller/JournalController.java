package team.blogserver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team.ark.core.response.R;
import team.blogserver.admin.service.CategoryService;
import team.blogserver.admin.service.JournalService;
import team.blogserver.common.model.domain.Category;
import team.blogserver.common.model.domain.Journal;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Slf4j
@RestController
@RequestMapping("/journal")
@Api(value = "后台日志接口")
public class JournalController {
    @Resource
    private JournalService journalService;

    @ApiOperation("日志列表列表")
    @GetMapping("/list/{page}/{limit}")
    public R list(@PathVariable Long page, @PathVariable Long limit) {
        Page<Journal> pageParam = new Page<>(page, limit);
        IPage<Journal> listPage = journalService.listPage(pageParam);
        return R.ok(listPage);
    }
}
