package com.wbteam.weiban.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.UpLoadService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpLoadServiceImpl implements UpLoadService {
    //不是我写的

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.urlPrefix}")
    private String urlPrefix;

    @Override
    public ResponseData picUpload(MultipartFile multipartFile) {
        //验证上传的是否为图片
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            return new ResponseData(ResponseStates.IMAGE_FORMAT_ERROR.getValue(),ResponseStates.IMAGE_FORMAT_ERROR.getMessage());
        }
        String fileName = multipartFile.getOriginalFilename();
        String filePath = this.getFilePath(fileName);
        Map<String, Object> data = new HashMap<>();
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
            ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(multipartFile.getBytes()));
            data.put("path",urlPrefix+filePath);
            ossClient.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        }
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    @Override
    public ResponseData picDelete(String url) {
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
            ossClient.deleteObject(bucketName, url);
            ossClient.shutdown();
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        }


    }

    private String getFilePath(String fileName) {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String date = ft.format(dNow);
        String[] day = date.split("-");
        return "images/" + day[0] + "/" + day[1] + "/"
                + day[2] + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(fileName, ".");
    }
}
