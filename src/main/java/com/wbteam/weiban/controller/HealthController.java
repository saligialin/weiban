package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.Health;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.HealthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "健康信息接口")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    /**
     *
     * @param health
     * @return
     */
    @ApiOperation("更新健康信息")
    @PutMapping("/update")
    public ResponseData update(@RequestBody Health health) {
        if (health.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int updateHealth = healthService.updateHealth(health);
        if (updateHealth>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("删除健康信息")
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiJsonObject(name = "healthDelete", value = @ApiJsonProperty(key = "id", example = "健康信息ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int deleteHealth = healthService.deleteHealth(id);
        if (deleteHealth>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("根据ID获取健康信息")
    @PostMapping("/getById")
    public ResponseData getHealthById(@ApiJsonObject(name = "healthGet", value = @ApiJsonProperty(key = "id", example = "健康信息ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Health health = healthService.getHealthById(id);
        if (health==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("health", health);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("根据老人ID获取健康信息")
    @PostMapping("/getByElderId")
    public ResponseData getHealthByElderId(@ApiJsonObject(name = "elderGetHealth", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String, String> parameter) {
        String elderId = parameter.get("elderId");
        if (elderId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Health health = healthService.getHealthByElderId(elderId);
        if (health==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("health", health);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }
}
