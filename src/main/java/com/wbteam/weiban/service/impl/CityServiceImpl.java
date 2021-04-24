package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.mapper.CityMapper;
import com.wbteam.weiban.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public String getAdCode(String cityName) {
        log.info("正在获取"+cityName+"的adCode");
        try {
            return cityMapper.getAdCodeByCityName(cityName);
        } catch (Exception e) {
            log.info("获取"+cityName+"的adCode异常");
            log.info(e.toString());
            return null;
        }

    }
}
