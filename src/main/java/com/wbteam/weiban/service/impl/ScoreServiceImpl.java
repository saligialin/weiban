package com.wbteam.weiban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbteam.weiban.entity.Score;
import com.wbteam.weiban.mapper.ScoreMapper;
import com.wbteam.weiban.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public int insertScore(Score score) {
        try {
            score.setId(UUID.randomUUID().toString());
            return scoreMapper.insert(score);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteScore(Score score) {
        try {
            QueryWrapper<Score> wrapper = new QueryWrapper<>();
            wrapper.eq("passage_id", score.getPassageId());
            wrapper.eq("reader_id", score.getReaderId());
            wrapper.eq("score", score.getScore());
            return scoreMapper.delete(wrapper);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public List<Score> getScoreListByPassageId(String passageId) {
        try {
            QueryWrapper<Score> wrapper = new QueryWrapper<>();
            wrapper.eq("passage_id",passageId);
            return scoreMapper.selectList(wrapper);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public Double getAverageOfPassage(String passageId) {
        try {
            return scoreMapper.getAverageOfPassage(passageId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
