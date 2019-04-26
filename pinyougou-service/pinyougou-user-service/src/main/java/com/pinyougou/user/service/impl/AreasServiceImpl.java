package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.pojo.Areas;
import com.pinyougou.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * @author ASUS
 * @description com.pinyougou.user.service.impl
 * @date 2019/4/26
 */
@Service(interfaceName = "com.pinyougou.service.AreasService")
@Transactional
public class AreasServiceImpl implements AreasService {

    @Autowired
    private AreasMapper areasMapper;

    @Override
    public void save(Areas areas) {

    }

    @Override
    public void update(Areas areas) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Areas findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Areas> findAll() {
        return null;
    }

    @Override
    public List<Areas> findByPage(Areas areas, int page, int rows) {
        return null;
    }

    @Override
    public List<Areas> findAreaByParentId(String cityId) {
        try {
            Example example = new Example(Areas.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("cityId",cityId);

            return areasMapper.selectByExample(example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
