package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.Bind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BindMapper{

    int insertBind(Bind bind);

    List<Bind> getBindByElderId(String elderId);

    List<Bind> getBindByYouthId(String youthId);

    int updateBind(Bind bind);

    int deleteBind(String id);

    List<Bind> getUnconfirmedBindingForElder(String elderId);

    List<Bind> getUnconfirmedBindingForYouth(String youthId);

}
