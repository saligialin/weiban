package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Child;
import org.springframework.stereotype.Service;

@Service
public interface ChildService {
    int insertChild(Child child);

    int deleteById(String id);

    int updateById(Child child);

    Child selectById(String id);

    Child selectByTel(String tel);

    boolean changePassword(Child child);
}
