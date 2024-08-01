package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "文件上传")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;//上传操作类
@PostMapping("/upload")
    public Result<String> Upload(MultipartFile file){
        log.info("文件上传：{}", file);
    try {
        String fileName = file.getOriginalFilename();//得到原始名称
        String index = fileName.substring(fileName.lastIndexOf('.'));//得到后缀名
        String newFileName = UUID.randomUUID().toString() + index;
        String url = aliOssUtil.upload(file.getBytes(), newFileName);//上传文件
        return Result.success(url);
    } catch (IOException e) {
        log.info("文件上传失败{}", e);
    }
    return Result.error(MessageConstant.UPLOAD_FAILED);//使用常量
    }

}
