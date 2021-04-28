package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Score;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScoreService {

    int insertScore(Score score);

    int deleteScore(Score score);

    List<Score> getScoreListByPassageId(String passageId);

    Double getAverageOfPassage(String passageId);
}
