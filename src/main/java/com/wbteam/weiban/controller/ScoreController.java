package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.*;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "文章打分接口")
@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private PassageService passageService;

    @Autowired
    private ElderService elderService;

    @Autowired
    private ChildService childService;

    @Autowired
    private CarerService carerService;

    /**
     *
     * @param score
     * @return
     */
    @ApiOperation("增加文章打分")
    @PostMapping("/add")
    public ResponseData add(@RequestBody Score score) {
        int insertScore = scoreService.insertScore(score);
        if (insertScore>0) {
            Double average = scoreService.getAverageOfPassage(score.getPassageId());
            if (average==null) {
                int deleteScore = scoreService.deleteScore(score);
                return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
            } else {
                boolean b = passageService.changeSocre(score.getPassageId(), average);
                return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
            }
        } else {
            return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取文章打分列表")
    @PostMapping("/getList")
    public ResponseData getList(@ApiJsonObject(name = "scoreGetList", value = @ApiJsonProperty(key = "passageId", example = "文章ID")) @RequestBody Map<String, String> parameter) {
        String passageId = parameter.get("passageId");
        if (passageId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Score> scores = scoreService.getScoreListByPassageId(passageId);
        if (scores==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Score score : scores) {
            Map<String, Object> res = new HashMap<>();
            res.put("score",score);
            Elder elder = elderService.selectById(score.getReaderId());
            if (elder==null) {
                Child child = childService.selectById(score.getReaderId());
                if (child==null) {
                    Carer carer = carerService.selectById(score.getPassageId());
                    if (carer!=null) {
                        res.put("reader", carer);
                        res.put("readerRole",3);
                    } else {
                        res.put("reader", null);
                        res.put("readerRole",0);
                    }
                } else {
                    res.put("reader",child);
                    res.put("readerRole",2);
                }
            } else {
                res.put("reader",elder);
                res.put("readerRole",1);
            }
            list.add(res);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }
}
