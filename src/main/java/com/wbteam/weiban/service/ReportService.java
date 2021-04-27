package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Report;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportService {

    int insertReport(Report report);

    int updateReport(Report report);

    int deleteReport(String id);

    Report getNewReport(String elderId);

    List<Report> getList(String elderId);
}
