package com.wbteam.weiban.service;

import org.springframework.stereotype.Service;

@Service
public interface CityService {
    String getAdCode(String cityName);
}
