package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Bind;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BindService {

    int insertBind(Bind bind);

    List<Bind> getBindByElderId(String elderId);

    List<Bind> getBindByYouthId(String youthId);

    int updateBind(Bind bind);

    int deleteBind(String id);

    List<Bind> getUnconfirmedBindingForElder(String elderId);

    List<Bind> getUnconfirmedBindingForYouth(String youthId);

    int agreeToBind(String id);
}
