package com.wbteam.weiban.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.*;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.CarerService;
import com.wbteam.weiban.service.ChildService;
import com.wbteam.weiban.service.ElderService;
import com.wbteam.weiban.service.PassageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "文章接口")
@RestController
@RequestMapping("/passage")
public class PassageController {

    @Autowired
    private PassageService passageService;

    @Autowired
    private ChildService childService;

    @Autowired
    private CarerService carerService;

    @Autowired
    private ElderService elderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *
     * @param passage
     * @return
     */
    @ApiOperation("发布文章")
    @PostMapping("/add")
    public ResponseData addPassage(@RequestBody Passage passage) {
        int insertPassage = passageService.insertPassage(passage);
        if (insertPassage>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param passage
     * @return
     */
    @ApiOperation("更新文章")
    @PutMapping("/update")
    public ResponseData update(@RequestBody Passage passage) {
        if (passage.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int updatePassage = passageService.updatePassage(passage);
        if (updatePassage>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     */
    @ApiOperation("删除文章")
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiJsonObject(name = "passageDelete", value = @ApiJsonProperty(key = "id", example = "文章ID")) @RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int deletePassage = passageService.deletePassageById(id);
        if (deletePassage>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("根据ID获取文章")
    @PostMapping("/getById")
    public ResponseData getById(@ApiJsonObject(name = "passageGet", value = @ApiJsonProperty(key = "id", example = "文章ID")) @RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Passage passage = passageService.getPassageById(id);
        if (passage==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());

        Map<String,Object> data = new HashMap<>();
        data.put("passage",passage);
        Child child = childService.selectById(passage.getAuthorId());
        if (child!=null) {
            data.put("author",child);
        } else {
            Carer carer = carerService.selectById(passage.getAuthorId());
            if (carer!=null) {
                data.put("author",carer);
            } else {
                Elder elder = elderService.selectById(passage.getAuthorId());
                if (elder!=null) data.put("author", elder);
                else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
            }
        }
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("分页查询文章")
    @PostMapping("/getPassageList")
    public ResponseData getPassageList(@ApiJsonObject(name = "passageGetList", value = {
            @ApiJsonProperty(key = "currPage",example = "请求页数"),
            @ApiJsonProperty(key = "pageSize",example = "页面尺寸")
    })@RequestBody Map<String, Integer> parameter ) {
        Integer currPage = parameter.get("currPage");
        Integer pageSize = parameter.get("pageSize");
        List<Passage> passages = passageService.getPassage(currPage, pageSize);
        if (passages==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Passage passage : passages) {
            Map<String, Object> res = new HashMap<>();
            Child child = childService.selectById(passage.getAuthorId());
            if (child!=null) {
                res.put("author",child);
            } else {
                Carer carer = carerService.selectById(passage.getAuthorId());
                if (carer!=null) {
                    res.put("author",carer);
                } else {
                    Elder elder = elderService.selectById(passage.getAuthorId());
                    if (elder!=null) res.put("author", elder);
                    else res.put("author", null);
                }
            }
            res.put("passage",passage);
            list.add(res);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("根据作者ID获取文章")
    @PostMapping("/getByAuthor")
    public ResponseData getListByAuthorId(@ApiJsonObject(name = "passageGetByAuthorId", value = @ApiJsonProperty(key = "authorId",example = "作者ID"))@RequestBody Map<String, String> parameter) {
        String authorId = parameter.get("authorId");
        if (authorId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Passage> passages = passageService.getPassageByAuthorId(authorId);
        if (passages==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Passage passage : passages) {
            Map<String, Object> res = new HashMap<>();
            Child child = childService.selectById(authorId);
            if (child!=null) {
                res.put("author",child);
            } else {
                Carer carer = carerService.selectById(authorId);
                if (carer!=null) {
                    res.put("author",carer);
                } else {
                    Elder elder = elderService.selectById(authorId);
                    if (elder!=null) res.put("author", elder);
                    else res.put("author", null);
                }
            }
            res.put("passage",passage);
            list.add(res);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @param role
     * @return
     */
    @ApiOperation("增加浏览历史")
    @PostMapping("/read")
    public ResponseData read(@ApiJsonObject(name = "read", value = {
            @ApiJsonProperty(key = "readerId",example = "读者ID"),
            @ApiJsonProperty(key = "passageId",example = "文章Id")
    })@RequestBody Map<String, String> parameter, @ApiIgnore @ModelAttribute("role")String role) {
        String readerId = parameter.get("readerId");
        String passageId = parameter.get("passageId");
        if (readerId==null||passageId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage() );
        String listString = redisTemplate.opsForValue().get(role + readerId);
        List<String> history = null;
        if (listString==null) {
            history = new ArrayList<>();
        } else {
            JSONObject jsonObject = JSON.parseObject(listString);
            history = (List<String>) jsonObject.get("history");
            if (history.size()>30) history.remove(0);
        }
        history.add(passageId);
        Map<String,List<String>> map = new HashMap<>();
        map.put("history", history);
        String jsonString = JSON.toJSONString(map);
        redisTemplate.opsForValue().set(role+readerId,jsonString,15, TimeUnit.DAYS);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
    }

    /**
     *
     * @param parameter
     * @param role
     * @return
     */
    @ApiOperation("获取浏览历史")
    @PostMapping("/getHistory")
    public ResponseData getReadHistory(@ApiJsonObject(name = "getHistory", value =
            @ApiJsonProperty(key = "readerId", example = "阅读者ID")
    ) @RequestBody Map<String, String> parameter, @ApiIgnore @ModelAttribute("role") String role ) {
        String readerId = parameter.get("readerId");
        if (readerId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage() );
        String listString = redisTemplate.opsForValue().get(role + readerId);
        if (listString==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        JSONObject jsonObject = JSON.parseObject(listString);
        List<String> history = (List<String>) jsonObject.get("history");
        if (history.isEmpty()) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());

        List<Passage> list = new ArrayList<>();
        for (String passageId : history) {
            Passage passage = passageService.getPassageById(passageId);
            if (passage!=null) list.add(passage);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("history",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }
}
