package com.wbteam.weiban.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.CityService;
import com.wbteam.weiban.utils.RequestSendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "天气接口")
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Value("${gaode.weather.key}")
    private String key;

    private final String url = "http://restapi.amap.com/v3/weather/weatherInfo";

    @Autowired
    private CityService cityService;

    @ApiOperation("获取天气")
    @GetMapping("/get")
    public ResponseData getWeather(@RequestParam("cityName") String cityName) {
        if (cityName==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        String adCode = cityService.getAdCode(cityName);
        if (adCode!=null) {
            String requestUrl = url+"?key="+key+"&city="+adCode;
            String res = RequestSendUtil.sendGet(requestUrl);
            JSONObject result = JSON.parseObject(res);
            Map<String, Object> data = new HashMap<>();
            data.put("weather",result);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
        } else {
            return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        }
    }
}
