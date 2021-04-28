package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.entity.Passage;
import com.wbteam.weiban.mapper.PassageMapper;
import com.wbteam.weiban.service.PassageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PassageServiceImpl implements PassageService {

    @Autowired
    private PassageMapper passageMapper;

    @Override
    public int insertPassage(Passage passage) {
        try {
            passage.setId(UUID.randomUUID().toString());
            passage.setTime(new Date());
            passage.setScore(5.0);
            return passageMapper.insertPassage(passage);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deletePassageById(String id) {
        try {
            return passageMapper.deletePassageById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updatePassage(Passage passage) {
        try {
            passage.setTime(new Date());
            return passageMapper.updatePassage(passage);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Passage getPassageById(String id) {
        try {
            return passageMapper.getPassageById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Passage> getPassage(int currPage, int pageSize) {
        try {
            return passageMapper.getPassage(currPage,pageSize);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Passage> getPassageByAuthorId(String authorId) {
        try {
            return passageMapper.getPassageByAuthorId(authorId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public boolean changeSocre(String passageId, double score) {
        try {
            return passageMapper.changeSocre(passageId, score);
        } catch (Exception e) {
            log.info(e.toString());
            return false;
        }
    }
}
