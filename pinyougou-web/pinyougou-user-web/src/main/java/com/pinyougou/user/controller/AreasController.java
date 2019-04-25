package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Areas;
import com.pinyougou.service.AreasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/areas")
public class AreasController {

    @Reference(timeout = 10000)
    private AreasService areasService;

    @GetMapping("/findByCityId")
    public List<Areas> findByCityId(String cityId) {
        try {
            return areasService.findByCityId(cityId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
