package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author ASUS
 * @description com.pinyougou.user.service.impl
 * @date 2019/4/25
 */
@Service(interfaceName = "com.pinyougou.service.ProvincesService")
@Transactional
public class ProvincesServiceImpl implements ProvincesService {

    @Autowired
    private ProvincesMapper provincesMapper;

    @Override
    public void save(Provinces provinces) {

    }

    @Override
    public void update(Provinces provinces) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Provinces findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Provinces> findAll() {
        try {
            return provincesMapper.selectAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Provinces> findByPage(Provinces provinces, int page, int rows) {
        return null;
    }

    @Override
    public List<Provinces> findProvinceByParentId(String provinceId) {
        try {
            /** 创建封装查询条件 */
            Provinces provinces = new Provinces();
            provinces.setProvinceId(provinceId);
            return provincesMapper.select(provinces);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
