package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
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
@Service(interfaceName = "com.pinyougou.service.CitiesService")
@Transactional
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesMapper citiesMapper;

    @Override
    public void save(Cities cities) {

    }

    @Override
    public void update(Cities cities) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Cities findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Cities> findAll() {
        return null;
    }

    @Override
    public List<Cities> findByPage(Cities cities, int page, int rows) {
        return null;
    }

    @Override
    public List<Cities> findByProvinceId(String provinceId) {
        return null;
    }

    @Override
    public List<Cities> findCityByParentId(String provinceId) {
        try {
            // SELECT * FROM `tb_cities` where provinceid='440000';
            Example example = new Example(Cities.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("provinceId",provinceId);

            return citiesMapper.selectByExample(example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Cities> findByProvinceId(String provinceId) {
        try {
            Cities cities = new Cities();
            cities.setProvinceId(provinceId);
            return citiesMapper.select(cities);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
