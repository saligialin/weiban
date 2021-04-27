package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.MemoKind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemoKindMapper {
    List<MemoKind> getList();

    MemoKind getMemoKindById(Integer id);
}
