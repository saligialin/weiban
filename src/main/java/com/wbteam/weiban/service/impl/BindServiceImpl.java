package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.entity.Bind;
import com.wbteam.weiban.mapper.BindMapper;
import com.wbteam.weiban.service.BindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BindServiceImpl implements BindService {

    @Autowired
    private BindMapper bindMapper;

    @Override
    public int insertBind(Bind bind) {
        try {
            bind.setId(UUID.randomUUID().toString());
            bind.setIsAccepted(0);
            return bindMapper.insertBind(bind);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public List<Bind> getBindByElderId(String elderId) {
        try {
            return bindMapper.getBindByElderId(elderId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Bind> getBindByYouthId(String youthId) {
        try {
            return bindMapper.getBindByYouthId(youthId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public int updateBind(Bind bind) {
        try {
            return bindMapper.updateBind(bind);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteBind(String id) {
        try {
            return bindMapper.deleteBind(id);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public List<Bind> getUnconfirmedBindingForElder(String elderId) {
        try {
            return bindMapper.getUnconfirmedBindingForElder(elderId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Bind> getUnconfirmedBindingForYouth(String youthId) {
        try {
            return bindMapper.getUnconfirmedBindingForYouth(youthId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public int agreeToBind(String id) {
        try {
            Bind bind = new Bind();
            bind.setId(id);
            bind.setIsAccepted(1);
            return bindMapper.updateBind(bind);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }
}
