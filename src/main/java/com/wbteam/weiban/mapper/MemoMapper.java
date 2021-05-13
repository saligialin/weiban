package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.Memo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface MemoMapper{
    int insertMemo(Memo memo);

    int insertMemoBySelf(Memo memo);

    int updateMemo(Memo memo);

    int deleteMemo(String id);

    Memo getMemoById(String id);

    List<Memo> getListByElderId(String elderId, Date time);

    List<Memo> getListByYouthId(String youthId, Date time);
}
