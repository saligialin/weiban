package com.wbteam.weiban.mapper;

import com.wbteam.weiban.entity.Company;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper {
    List<Company> getList();

    Company getCompanyById(Integer id);
}
