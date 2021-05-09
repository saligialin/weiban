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
import java.util.List;
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
    @ApiOperation("增加健康信息")
    @PostMapping("/add")
    public ResponseData add(@RequestBody Health health) {
        if (health.getElderId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int insertHealth = healthService.insertHealth(health);
        if (insertHealth>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param health
     * @return
     */
    @ApiOperation("更新健康信息（用不到）")
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

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取最新的一条健康信息")
    @PostMapping("/getNewest")
    public ResponseData getNewest(@ApiJsonObject(name = "getNewest", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String, String> parameter) {
        String elderId = parameter.get("elderId");
        List<Health> list = healthService.getNewestList(1, elderId);
        if (list==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        if (list.isEmpty()) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("health",list.get(0));
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }


    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取最新的七条健康信息")
    @PostMapping("/getNewest7")
    public ResponseData getNewest7(@ApiJsonObject(name = "getNewest7", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String, String> parameter) {
        String elderId = parameter.get("elderId");
        List<Health> list = healthService.getNewestList(7, elderId);
        if (list==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        if (list.isEmpty()) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("list",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取最新的N条健康信息")
    @PostMapping("/getNewestN")
    public ResponseData getNewestN(@ApiJsonObject(name = "getNewestN", value = {
            @ApiJsonProperty(key = "elderId", example = "老人ID"),
            @ApiJsonProperty(key = "count", example = "数量")
    })@RequestBody Map<String, Object> parameter) {
        String elderId = (String) parameter.get("elderId");
        int count = (Integer) parameter.get("count");
        List<Health> list = healthService.getNewestList(count, elderId);
        if (list==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        if (list.isEmpty()) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("list",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取所有的健康信息")
    @PostMapping("/getAll")
    public ResponseData getAll(@ApiJsonObject(name = "getAll", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String, String> parameter) {
        String elderId = parameter.get("elderId");
        int count = healthService.getCountByElderId(elderId);
        List<Health> list = healthService.getNewestList(count, elderId);
        if (list==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        if (list.isEmpty()) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("list",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

}
