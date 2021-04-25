package com.wbteam.weiban.service.impl;

import com.wbteam.weiban.entity.Child;
import com.wbteam.weiban.mapper.ChildMapper;
import com.wbteam.weiban.service.ChildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ChildServiceImpl implements ChildService {


    @Autowired
    private ChildMapper childMapper;

    @Override
    public int insertChild(Child child) {
        log.info("开始向数据库中插入新家属用户");
        try {
            child.setId(UUID.randomUUID().toString());
            return childMapper.insert(child);
        } catch (Exception e) {
            log.info("向数据库中插入新家属用户异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteById(String id) {
        log.info("开始删除家属用户"+id);
        try {
            return childMapper.deleteById(id);
        } catch (Exception e) {
            log.info("向数据库中插入新家属用户异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int updateById(Child child) {
        log.info("开始更新家属用户"+child.getId()+"的信息");
        try {
            return childMapper.updateById(child);
        } catch (Exception e) {
            log.info("更新家属用户"+child.getId()+"的信息异常：");
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public Child selectById(String id) {
        log.info("开始获取家属用户"+id+"的信息");
        try {
            return childMapper.selectById(id);
        } catch (Exception e) {
            log.info("获取家属用户"+id+"的信息异常：");
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public Child selectByTel(String tel) {
        log.info("开始获取手机号为"+tel+"家属用户的信息");
        try {
            return childMapper.getChildByTel(tel);
        } catch (Exception e) {
            log.info("获取手机号为"+tel+"家属用户的信息异常：");
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public boolean changePassword(Child child) {
        log.info("开始更改家属用户"+child.getId()+"的密码");
        try {
            return childMapper.changePassword(child);
        } catch (Exception e) {
            log.info("更改家属用户"+child.getId()+"的密码异常：");
            log.info(e.toString());
            return false;
        }
    }
}
