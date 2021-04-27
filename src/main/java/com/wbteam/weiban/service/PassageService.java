package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Passage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PassageService {

    int insertPassage(Passage passage);

    int deletePassageById(String id);

    int updatePassage(Passage passage);

    Passage getPassageById(String id);

    List<Passage> getPassage(int currPage, int pageSize);

    List<Passage> getPassageByAuthorId(String authorId);

    boolean changeSocre(String id, double score);
}
