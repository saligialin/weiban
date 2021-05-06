package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.Bind;
import com.wbteam.weiban.entity.Carer;
import com.wbteam.weiban.entity.Child;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.BindService;
import com.wbteam.weiban.service.CarerService;
import com.wbteam.weiban.service.ChildService;
import com.wbteam.weiban.service.ElderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "关系绑定接口")
@RestController
@RequestMapping("/bind")
public class BindController {

    @Autowired
    private BindService bindService;

    @Autowired
    private ElderService elderService;

    @Autowired
    private ChildService childService;

    @Autowired
    private CarerService carerService;

    /**
     *
     * @param bind
     * @return
     */
    @ApiOperation("建立新关系：传两端用户ID")
    @PostMapping("/add")
    public ResponseData newBind(@RequestBody Bind bind) {
        int i = bindService.insertBind(bind);
        if (i>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取关系列表（老人用）")
    @PostMapping("/elderGetBinds")
    public ResponseData elderGetBinds(@ApiJsonObject(name = "elderGetBinds", value = @ApiJsonProperty(key = "elderId",example = "老人ID")) @RequestBody Map<String,String> parameter) {
        String elderId = parameter.get("elderId");
        if (elderId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Bind> binds = bindService.getBindByElderId(elderId);
        if (binds==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Bind bind : binds) {
            Map<String, Object> res = new HashMap<>();
            res.put("bind",bind);
            if (bind.getBindKind().getId()==6) {
                Carer carer = carerService.selectById(bind.getYouthId());
                res.put("youth",carer);
                res.put("youthRole",3);
            } else {
                Child child = childService.selectById(bind.getYouthId());
                res.put("youth",child);
                res.put("youthRole",2);
            }
            list.add(res);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取关系列表（家属/护工用）")
    @PostMapping("/youthGetBinds")
    public ResponseData youthGetBinds(@ApiJsonObject(name = "youthGetBinds", value = @ApiJsonProperty(key = "youthId", example = "家属/护工ID"))@RequestBody Map<String, String> parameter) {
        String youthId = parameter.get("youthId");
        if (youthId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Bind> binds = bindService.getBindByYouthId(youthId);
        if (binds==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Bind bind : binds) {
            Map<String, Object> res = new HashMap<>();
            res.put("bind",bind);
            res.put("elder",elderService.selectById(bind.getElderId()));
            list.add(res);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("result", list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取未确认的绑定（老人用）")
    @PostMapping("/elderGetNotBinds")
    public ResponseData elderGetNotBinds(@ApiJsonObject(name = "elderGetNotBinds", value = @ApiJsonProperty(key = "elderId",example = "老人ID")) @RequestBody Map<String,String> parameter) {
        String elderId = parameter.get("elderId");
        if (elderId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Bind> binds = bindService.getUnconfirmedBindingForElder(elderId);
        if (binds==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Bind bind : binds) {
            Map<String, Object> res = new HashMap<>();
            res.put("bind",bind);
            if (bind.getBindKind().getId()==6) {
                Carer carer = carerService.selectById(bind.getYouthId());
                res.put("youth",carer);
                res.put("youthRole",3);
            } else {
                Child child = childService.selectById(bind.getYouthId());
                res.put("youth",child);
                res.put("youthRole",2);
            }
            list.add(res);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("result", list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取未确认的绑定（家属/护工用）")
    @PostMapping("/youthGetNotBinds")
    public ResponseData youthGetNotBinds(@ApiJsonObject(name = "youthGetNotBinds", value = @ApiJsonProperty(key = "youthId", example = "家属/护工ID")) @RequestBody Map<String,String> parameter) {
        String youthId = parameter.get("youthId");
        if (youthId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Bind> binds = bindService.getUnconfirmedBindingForYouth(youthId);
        if (binds==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Bind bind : binds) {
            Map<String, Object> res = new HashMap<>();
            res.put("bind",bind);
            res.put("elder",elderService.selectById(bind.getElderId()));
            list.add(res);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("result", list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("同意关系绑定")
    @PostMapping("/agree")
    public ResponseData agreeToBind(@ApiJsonObject(name = "agreeToBind", value = @ApiJsonProperty(key = "id", example = "关系ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int agree = bindService.agreeToBind(id);
        if (agree>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("拒绝关系绑定")
    @PostMapping("/disagree")
    public ResponseData disagreeToBind(@ApiJsonObject(name = "disagreeToBind", value = @ApiJsonProperty(key = "id", example = "关系ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int disagree = bindService.deleteBind(id);
        if (disagree>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param bind
     * @return
     */
    @ApiOperation("更新关系")
    @PutMapping("/update")
    public ResponseData update(@RequestBody Bind bind) {
        if (bind.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int updateBind = bindService.updateBind(bind);
        if (updateBind>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("删除关系")
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiJsonObject(name = "bindDelete", value = @ApiJsonProperty(key = "id", example = "关系ID"))@RequestBody Map<String,String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int deleteBind = bindService.deleteBind(id);
        if (deleteBind>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

}
