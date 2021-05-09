package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Health;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HealthService {

    int insertHealth(Health health);

    int deleteHealth(String id);

    int updateHealth(Health health);

    Health getHealthById(String id);

    Health getHealthByElderId(String elderId);

    int getCountByElderId(String elderId);

    List<Health> getNewestList(int count, String elderId);
}
