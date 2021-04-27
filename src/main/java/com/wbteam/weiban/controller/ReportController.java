package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.Report;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "健康报告接口")
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     *
     * @param report
     * @return
     */
    @ApiOperation("新增健康报告")
    @PostMapping("/add")
    public ResponseData add(@RequestBody Report report) {
        int insertReport = reportService.insertReport(report);
        if (insertReport>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param report
     * @return
     */
    @ApiOperation("修改健康报告")
    @PutMapping("/update")
    public ResponseData update(@RequestBody Report report){
        if (report.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int updateReport = reportService.updateReport(report);
        if (updateReport>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("删除健康报告")
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiJsonObject(name = "reportDelete", value = @ApiJsonProperty(key = "id", example = "健康报告ID"))@RequestBody Map<String,String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int deleteReport = reportService.deleteReport(id);
        if (deleteReport>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取最新一篇健康报告")
    @PostMapping("/getNew")
    public ResponseData getNewRepoet(@ApiJsonObject(name = "reportGetNew", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String,String> parameter) {
        String elderId = parameter.get("elderId");
        if (elderId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Report newReport = reportService.getNewReport(elderId);
        if (newReport==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("report",newReport);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取所有健康报告")
    @PostMapping("/getList")
    public ResponseData getList(@ApiJsonObject(name = "reportGetList", value = @ApiJsonProperty(key = "elderId", example = "老人ID"))@RequestBody Map<String,String> parameter) {
        String elderId = parameter.get("elderId");
        if (elderId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Report> reports = reportService.getList(elderId);
        if (reports==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("reportList",reports);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

}
