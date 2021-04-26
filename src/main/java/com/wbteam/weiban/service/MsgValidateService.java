package com.wbteam.weiban.service;

import org.springframework.stereotype.Service;

@Service
public interface MsgValidateService {

    boolean messsageSend(String tel);

    boolean messageCheck(String tel);
}
