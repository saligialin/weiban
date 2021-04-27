package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.PassageKind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PassageKindMapper {
    List<PassageKind> getList();

    PassageKind getPassageKindById(String id);
}
