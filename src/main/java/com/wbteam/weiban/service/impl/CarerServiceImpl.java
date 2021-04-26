package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.entity.Carer;
import com.wbteam.weiban.mapper.CarerMapper;
import com.wbteam.weiban.service.CarerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CarerServiceImpl implements CarerService {

    @Autowired
    private CarerMapper carerMapper;

    @Override
    public int insertCarer(Carer carer) {
        log.info("开始向数据插入新的护工用户");
        try {
            carer.setId(UUID.randomUUID().toString());
            return carerMapper.insertCarer(carer);
        } catch (Exception e) {
            log.info("向数据插入新的护工用户异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteById(String id) {
        log.info("开始删除护工用户"+id);
        try {
            return carerMapper.deleteCarerById(id);
        } catch (Exception e) {
            log.info("删除护工用户"+id+"异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updateById(Carer carer) {
        log.info("开始更新护工用户"+carer.getId()+"的信息");
        try {
            return carerMapper.updateCarerById(carer);
        } catch (Exception e) {
            log.info("更新护工用户"+carer.getId()+"的信息异常");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Carer selectById(String id) {
        log.info("开始获取护工用户"+id+"的信息");
        try {
            return carerMapper.getCarerById(id);
        } catch (Exception e) {
            log.info("获取护工用户"+id+"的信息异常：");
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public Carer selectByTel(String tel) {
        log.info("开始获取手机号为"+tel+"的护工信息");
        try {
            return carerMapper.getCarerByTel(tel);
        } catch (Exception e) {
            log.info("获取手机号为"+tel+"的护工信息异常：");
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public boolean changePassword(Carer carer) {
        log.info("开始更改护工用户"+carer.getId()+"的密码");
        try {
            return carerMapper.changePassword(carer);
        } catch (Exception e) {
            log.info("更改护工用户"+carer.getId()+"的密码异常：");
            log.info(e.toString());
            return false;
        }
    }
}
