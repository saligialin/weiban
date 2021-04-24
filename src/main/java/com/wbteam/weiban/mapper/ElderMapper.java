package com.wbteam.weiban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbteam.weiban.entity.Elder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElderMapper extends BaseMapper<Elder> {
    Elder getElderByTel(String tel);

    boolean changePassword(Elder elder);
}
