package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.entity.Memo;
import com.wbteam.weiban.mapper.MemoMapper;
import com.wbteam.weiban.service.MemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MemoServieImpl implements MemoService {

    @Autowired
    private MemoMapper memoMapper;

    @Override
    public int insertMemo(Memo memo) {
        try {
            memo.setId(UUID.randomUUID().toString());
            return memoMapper.insertMemo(memo);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int insertMemoBySelf(Memo memo) {
        try {
            memo.setId(UUID.randomUUID().toString());
            return memoMapper.insertMemoBySelf(memo);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updateMemo(Memo memo) {
        try {
            return memoMapper.updateMemo(memo);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteMemo(String id) {
        try {
            return memoMapper.deleteMemo(id);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Memo getMemoById(String id) {
        try {
            return memoMapper.getMemoById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Memo> getListByElderId(String elderId) {
        try {
            return memoMapper.getListByElderId(elderId, new Date());
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Memo> getListByYouthId(String youthId) {
        try {
            return memoMapper.getListByYouthId(youthId, new Date());
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
