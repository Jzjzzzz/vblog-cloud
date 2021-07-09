package team.blogserver.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.ark.core.exception.ArkServiceException;
import team.ark.core.response.R;
import team.blogserver.admin.service.FileService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jzj
 * @date 2021/07/08
 */
@Slf4j
@Api(value = "阿里云文件管理")
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件上传
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块", required = true)
            @RequestParam("module") String module) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);
            //返回r对象
            return R.ok(uploadUrl);
        } catch (IOException e) {
            throw new ArkServiceException("文件上传错误");
        }
    }

    @ApiOperation("删除OSS文件")
    @DeleteMapping("/remove")
    public R remove(
            @ApiParam(value = "要删除的文件路径", required = true)
            @RequestParam("url") String url) {
        fileService.removeFile(url);
        return R.ok();
    }
}
