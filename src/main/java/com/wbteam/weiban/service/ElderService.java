package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Elder;
import org.springframework.stereotype.Service;

@Service
public interface ElderService {
    int insertElder(Elder elder);

    int deleteById(String id);

    int updateById(Elder elder);

    Elder selectById(String id);

    Elder selectByTel(String tel);

    boolean changePassword(Elder elder);
}
