package com.wbteam.weiban.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper {

    String getAdCodeByCityName(String cityName);

}
