package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;

    /** 用户注册 */
    @PostMapping("/save")
    public boolean save(@RequestBody User user, String code){
        try{
            // 检验验证码是否正确
            boolean flag = userService.checkSmsCode(user.getPhone(), code);
            if (flag) {
                userService.save(user);
            }
            return flag;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    /** 发送短信验证码 */
    @GetMapping("/sendSmsCode")
    public boolean sendSmsCode(String phone){
        try{
            return userService.sendCode(phone);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 更新用户密码 */
    @PostMapping("/updatePassword")
    public boolean updatePassword(@RequestBody Map<String,String> updateinfo){
        try {
            userService.updatePassword(updateinfo.get("userName"), updateinfo.get("newPassword"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 获取用户信息*/
    @GetMapping("getUserInfo")
    @ResponseBody
    public User getUserInfo(String userName){
        try {
            User user= userService.getUserInfo(userName);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/sendMsg")
    public boolean sendMsg(@RequestBody Map<String,String> phoneInfo,HttpServletRequest request){
        // 1. 判断验证码
        // 1.1 从Session中获取验证码
        String oldCode = (String) request.getSession()
                .getAttribute(VerifyController.VERIFY_CODE);
        System.out.println("oldCode = " + oldCode);
        if (phoneInfo.get("inputCode").equalsIgnoreCase(oldCode)) {
                /** 发送验证码 */
            boolean success = userService.sendCode(phoneInfo.get("phone"));
            return success;
        }else {
            return false;
        }
    }

    @PostMapping("/msgCodeVerify")
    public boolean msgCodeVerify(@RequestBody Map<String,String> Info){
        // 调用 用户服务层  进行 短信验证码验证
        boolean success = userService.checkSmsCode(Info.get("phone"), Info.get("msgCode"));
        return success;
    }

    @PostMapping("/newPhoneMsgCodeVerify")
    public boolean newPhoneMsgCodeVerify(@RequestBody Map<String,String> Info){
        // 调用 用户服务层  进行 短信验证码验证
        boolean success = userService.checkSmsCode(Info.get("phone"), Info.get("msgCode"));
        // 验证成功
        if (success){
            // 将新手机绑定到对应的用户中
            userService.updatePhone(Info.get("userName"),Info.get("phone"));
        }
        return success;

    }

}
