package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.Passage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PassageMapper{

    int insertPassage(Passage passage);

    int deletePassageById(String id);

    int updatePassage(Passage passage);

    Passage getPassageById(String id);

    List<Passage> getPassage(int currPage, int pageSize);

    List<Passage> getPassageByAuthorId(String authorId);

    boolean changeSocre(String passageId, double score);
}
