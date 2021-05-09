package com.wbteam.weiban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbteam.weiban.entity.Health;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HealthMapper extends BaseMapper<Health> {
    List<Health> getNewsetHealth(int count, String elderId);
}
