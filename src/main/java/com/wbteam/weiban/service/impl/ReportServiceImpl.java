package com.wbteam.weiban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbteam.weiban.entity.Report;
import com.wbteam.weiban.mapper.ReportMapper;
import com.wbteam.weiban.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public int insertReport(Report report) {
        try {
            report.setId(UUID.randomUUID().toString());
            report.setTime(new Date());
            return reportMapper.insert(report);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updateReport(Report report) {
        try {
            report.setTime(new Date());
            return reportMapper.updateById(report);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteReport(String id) {
        try {
            return reportMapper.deleteById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Report getNewReport(String elderId) {
        try {
            return reportMapper.getNewReport(elderId);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Report> getList(String elderId) {
        try {
            QueryWrapper<Report> wrapper = new QueryWrapper<>();
            wrapper.eq("elder_id", elderId);
            return reportMapper.selectList(wrapper);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
