package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.*;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.CarerService;
import com.wbteam.weiban.service.ChildService;
import com.wbteam.weiban.service.ElderService;
import com.wbteam.weiban.service.MemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "备忘录接口")
@RestController
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoService memoService;

    @Autowired
    private ElderService elderService;

    @Autowired
    private ChildService childService;

    @Autowired
    private CarerService carerService;

    /**
     *
     * @param memo
     * @return
     */
    @ApiOperation("新增备忘录")
    @PostMapping("/add")
    public ResponseData add(@RequestBody Memo memo) {
        int insertMemo = memoService.insertMemo(memo);
        if (insertMemo>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param memo
     * @return
     */
    @ApiOperation("新增备忘录(老人自用)")
    @PostMapping("/addBySelf")
    public ResponseData addMemoSelf(@RequestBody Memo memo) {
        int insertMemoBySelf = memoService.insertMemoBySelf(memo);
        if (insertMemoBySelf>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param memo
     * @return
     */
    @ApiOperation("更新备忘录")
    @PutMapping("/update")
    public ResponseData update(@RequestBody Memo memo){
        if (memo.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int updateMemo = memoService.updateMemo(memo);
        if (updateMemo>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation(("删除备忘录"))
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiJsonObject(name = "memoDelete", value = @ApiJsonProperty(key = "id", example = "备忘录ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int deleteMemo = memoService.deleteMemo(id);
        if (deleteMemo>0)  return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("根据ID获取备忘录")
    @PostMapping("/get")
    public ResponseData getMemoById(@ApiJsonObject(name = "memoGet", value = @ApiJsonProperty(key = "id", example = "备忘录ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Memo memo = memoService.getMemoById(id);
        if (memo==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Map<String, Object> data = new HashMap<>();
        Elder elder = elderService.selectById(memo.getElderId());
        data.put("elder",elder);
        Child child = childService.selectById(memo.getYouthId());
        if(child==null) {
            Carer carer = carerService.selectById(memo.getYouthId());
            if (carer==null) {
                data.put("youth", elder);
                data.put("youthRole",1);
            } else {
                data.put("youth", carer);
                data.put("youthRole",3);
            }
        } else {
            data.put("youth",child);
            data.put("youthRole",2);
        }
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取备忘录列表（老人用）")
    @PostMapping("/getMemosElderId")
    public ResponseData getMemosElderId(@ApiJsonObject(name = "elderGetMemos", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String, String> parameter) {
        String elderId = parameter.get("elderId");
        if (elderId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Memo> memos = memoService.getListByElderId(elderId);
        if (memos==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Memo memo : memos) {
            Map<String, Object> res = new HashMap<>();
            res.put("memo",memo);
            Elder elder = elderService.selectById(memo.getElderId());
            res.put("elder", elder);
            Child child = childService.selectById(memo.getYouthId());
            if (child==null) {
                Carer carer = carerService.selectById(memo.getYouthId());
                if(carer!=null) {
                    res.put("youth",carer);
                    res.put("youthRole",3);
                } else {
                    res.put("youth",elder);
                    res.put("youthRole",1);
                }
            } else {
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
    @ApiOperation("获取备忘录列表（家属/护工用）")
    @PostMapping("/getMemosYouthId")
    public ResponseData getMemosYouthId(@ApiJsonObject(name = "youthGetMemos", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String, String> parameter) {
        String youthId = parameter.get("youthId");
        if (youthId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Memo> memos = memoService.getListByYouthId(youthId);
        if (memos==null) return new ResponseData(ResponseStates.RESULT_IS_NULL.getValue(), ResponseStates.RESULT_IS_NULL.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Memo memo : memos) {
            Map<String, Object> res = new HashMap<>();
            res.put("memo",memo);
            Child child = childService.selectById(memo.getYouthId());
            if(child==null) {
                Carer carer = carerService.selectById(memo.getYouthId());
                if(carer!=null){
                    res.put("youth",carer);
                    res.put("youthRole",3);
                }
            } else {
                res.put("youth",child);
                res.put("youthRole",2);
            }
            Elder elder = elderService.selectById(memo.getElderId());
            res.put("elder",elder);
            list.add(res);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }
}
