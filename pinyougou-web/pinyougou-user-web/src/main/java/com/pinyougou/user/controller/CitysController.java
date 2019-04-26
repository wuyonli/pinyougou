package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/citys")
public class CitysController {

    @Reference(timeout = 10000)
    private CitiesService citiesService;

    @GetMapping("/findByProvinceId")
    public List<Cities> findByProvinceId(String provinceId) {
        try {
            System.out.println("查询city表");
            return citiesService.findByProvinceId(provinceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
