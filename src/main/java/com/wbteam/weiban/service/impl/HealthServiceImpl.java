package com.wbteam.weiban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbteam.weiban.entity.Health;
import com.wbteam.weiban.mapper.HealthMapper;
import com.wbteam.weiban.service.HealthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthMapper healthMapper;

    @Override
    public int insertHealth(Health health) {
        try {
            health.setId(UUID.randomUUID().toString());
            return healthMapper.insert(health);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteHealth(String id) {
        try {
            return healthMapper.deleteById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updateHealth(Health health) {
        try {
            health.setTime(new Date());
            return healthMapper.updateById(health);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Health getHealthById(String id) {
        try {
            return healthMapper.selectById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public Health getHealthByElderId(String elderId) {
        try {
            QueryWrapper<Health> wrapper = new QueryWrapper<>();
            wrapper.eq("elder_id", elderId);
            return healthMapper.selectOne(wrapper);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public int getCountByElderId(String elderId) {
        try {
            QueryWrapper<Health> wrapper = new QueryWrapper<>();
            wrapper.eq("elder_id", elderId);
            return healthMapper.selectCount(wrapper);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Health> getNewestList(int count, String elderId) {
        try {
            return healthMapper.getNewsetHealth(count, elderId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
