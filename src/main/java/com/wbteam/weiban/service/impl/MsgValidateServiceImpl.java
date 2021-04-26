package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.service.MsgValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MsgValidateServiceImpl implements MsgValidateService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean messsageSend(String tel) {
        return false;
    }

    @Override
    public boolean messageCheck(String tel, String code) {
        return false;
    }
}
