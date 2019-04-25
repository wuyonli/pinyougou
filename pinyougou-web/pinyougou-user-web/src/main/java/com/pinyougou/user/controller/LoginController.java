package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录用控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-16<p>
 */
@RestController
public class LoginController {

    @Reference(timeout = 10000)
    private UserService userService;

    /** 获取登录用户名 */
    @GetMapping("/user/showName")
    public Map<String,String> showName(){

        // 获取安全上下文对象
        SecurityContext context = SecurityContextHolder.getContext();
        String loginName = context.getAuthentication().getName();

        //根据获取到的用户名,查询用户信息
        User user = userService.findByUsername(loginName);
        //得到url
        String headPic = user.getHeadPic();

        Map<String,String> data = new HashMap<>();
        data.put("loginName", loginName);
        data.put("headPic", headPic);
        return data;
    }
}
