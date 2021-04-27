package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.entity.Elder;
import com.wbteam.weiban.mapper.ElderMapper;
import com.wbteam.weiban.service.ElderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ElderServiceImpl implements ElderService {

    @Autowired
    private ElderMapper elderMapper;

    @Override
    public int insertElder(Elder elder) {
        log.info("开始向数据库中插入新老人用户");
        try {
            elder.setId(UUID.randomUUID().toString());
            return elderMapper.insert(elder);
        } catch (Exception e) {
            log.info("向数据库中插入新老人用户异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteById(String id) {
        log.info("开始删除老人用户"+id);
        try {
            return elderMapper.deleteById(id);
        } catch (Exception e) {
            log.info("删除老人用户"+id+"异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updateById(Elder elder) {
        log.info("开始更新老人用户"+elder.getId()+"的信息");
        try {
            return elderMapper.updateById(elder);
        } catch (Exception e) {
            log.info("更新老人用户"+elder.getId()+"的信息异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Elder selectById(String id) {
        log.info("开始获取老人用户"+id+"的信息");
        try {
            return elderMapper.selectById(id);
        } catch (Exception e) {
            log.info("获取老人用户"+id+"的信息异常：");
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public Elder selectByTel(String tel) {
        log.info("开始获取老人用户手机号为"+tel+"的信息");
        try {
            return elderMapper.getElderByTel(tel);
        } catch (Exception e) {
            log.info("获取老人用户手机号为"+tel+"的信息异常：");
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public boolean changePassword(Elder elder) {
        log.info("开始更改老人用户"+elder.getId()+"的密码");
        try {
            return elderMapper.changePassword(elder);
        } catch (Exception e) {
            log.info("更改老人用户"+elder.getId()+"的密码异常：");
            log.info(e.toString());
            return false;
        }
    }
}
