package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Carer;
import org.springframework.stereotype.Service;

@Service
public interface CarerService {

    int insertCarer(Carer carer);

    int deleteById(String id);

    int updateById(Carer carer);

    Carer selectById(String id);

    Carer selectByTel(String tel);

    boolean changePassword(Carer carer);
}
