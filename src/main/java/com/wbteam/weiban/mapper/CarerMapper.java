package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.Carer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarerMapper{
    int insertCarer(Carer carer);

    Carer getCarerById(String id);

    Carer getCarerByTel(String tel);

    int updateCarerById(Carer carer);

    int deleteCarerById(String id);

    boolean changePassword(Carer carer);
}
