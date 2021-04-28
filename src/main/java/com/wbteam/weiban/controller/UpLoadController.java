package com.wbteam.weiban.controller;

import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.service.UpLoadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "图片文件接口")
@RestController
@RequestMapping(value = "/file")
public class UpLoadController {
    //不是我写的

    @Autowired
    UpLoadService upLoadService;

    @ApiOperation("上传图片")
    @PostMapping(value = "/upload")
    public ResponseData upLoadImage(@RequestParam(value = "image") MultipartFile multipartFile) {
        return upLoadService.picUpload(multipartFile);
    }

    @ApiOperation("删除图片")
    @DeleteMapping(value = "/del")
    public ResponseData delImage(@RequestParam("url") String url) {
        return upLoadService.picDelete(url);
    }
}
