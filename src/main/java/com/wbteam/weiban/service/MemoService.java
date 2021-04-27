package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Memo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemoService {
    int insertMemo(Memo memo);

    int updateMemo(Memo memo);

    int deleteMemo(String id);

    Memo getMemoById(String id);

    List<Memo> getListByElderId(String elderId);

    List<Memo> getListByYouthId(String youthId);
}
