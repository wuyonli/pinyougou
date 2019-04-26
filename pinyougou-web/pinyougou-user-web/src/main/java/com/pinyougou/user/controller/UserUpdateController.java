package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.support.Parameter;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.AreasService;
import com.pinyougou.service.CitiesService;
import com.pinyougou.service.ProvincesService;
import com.pinyougou.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 个人信心
 *
 * @author ASUS
 * @description com.pinyougou.user.controller
 * @date 2019/4/25
 */
@RestController
@RequestMapping("/userUpdate")
public class UserUpdateController {

    @Reference(timeout = 10000)
    private UserService userService;
    @Reference(timeout = 10000)
    private ProvincesService provincesService;
    @Reference(timeout = 10000)
    private CitiesService citiesService;
    @Reference(timeout = 10000)
    private AreasService areasService;



    @PostMapping("/updateNickName")
    public boolean updateNickName(@RequestBody User user){

//        //根据用户名获取用户id
//        User user = userService.findByUsername(username);
//        //根据用户id,修改用户信息
//        user.setNickName(nickName);
//        user.setSex(sex);
        User user1 = userService.findByUsername(user.getUsername());
        user1.setNickName(user.getNickName());
        user1.setSex(user.getSex());
        user1.setJob(user.getJob());
        user1.setBirthday(user.getBirthday());
        user1.setHeadPic(user.getHeadPic());
        user1.setAddress(user.getAddress());

        try {
            userService.update(user1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 根据父级id查询省份 */
    @GetMapping("/findProvinceByParentId")
    public List<Provinces> findProvinceByParentId(String provinceId){
        return provincesService.findProvinceByParentId(provinceId);
    }

    /** 根据省份id查询城市 */
    @GetMapping("/findCityByParentId")
    public List<Cities> findCityByParentId(String provinceId){
        return citiesService.findCityByParentId(provinceId);
    }

    /** 根据城市id查询区域 */
    @GetMapping("/findAreaByParentId")
    public List<Areas> findAreaByParentId(String cityId){
        return areasService.findAreaByParentId(cityId);
    }

}
