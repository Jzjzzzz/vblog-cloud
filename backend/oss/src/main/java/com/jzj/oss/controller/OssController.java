package com.jzj.oss.controller;

import com.jzj.oss.service.OssService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.ark.core.exception.ArkServiceException;
import team.ark.core.response.R;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jzj
 * @version 1.0
 * @date 2021/7/27 17:57
 */
@RestController
@RequestMapping("/api/oss/fileoss")
@CrossOrigin
public class OssController {

    @Resource
    private OssService ossService;

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
            String uploadUrl = ossService.upload(inputStream, module, originalFilename);
            //返回r对象
            return R.ok(uploadUrl);
        } catch (IOException e) {
            throw new ArkServiceException("文件上传错误");
        }
    }



    @ApiOperation("删除OSS文件")
    @DeleteMapping("/remove")
    public R remove(@ApiParam(value = "要删除的文件路径", required = true) @RequestParam("url") String url) {
        ossService.removeFile(url);
        return R.ok();
    }
}
