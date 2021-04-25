package com.wbteam.weiban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbteam.weiban.entity.Child;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChildMapper extends BaseMapper<Child> {

    Child getChildByTel(String tel);

    boolean changePassword(Child child);
}
