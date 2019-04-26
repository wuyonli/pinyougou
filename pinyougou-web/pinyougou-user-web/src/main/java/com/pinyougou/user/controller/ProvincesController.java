package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.ProvincesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProvincesController {
    @Reference(timeout = 10000)
    private ProvincesService provincesService;

    @GetMapping("/provinces/findAll")
    public List<Provinces> findAll() {
        try {
            return provincesService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
