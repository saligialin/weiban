package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.BindKind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BindKindMapper {

    List<BindKind> getList();

    BindKind getBindKindById(Integer id);
}
