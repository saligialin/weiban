package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UpLoadService {
    //不是我写的
    String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};
    ResponseData picUpload(MultipartFile multipartFile);
    ResponseData picDelete(String url);
}
