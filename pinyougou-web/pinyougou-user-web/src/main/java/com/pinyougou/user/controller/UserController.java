package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.User;
import com.pinyougou.service.CartService;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.UserService;
import com.pinyougou.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
    @Reference(timeout = 10000)
    private OrderService orderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;
    @Reference(timeout = 10000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

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

    /** 以下是功能7新增的方法 */
    /** 查询订单 */
    @GetMapping("/findOrderByUserId")
    public PageResult findOrderByUserId(String userId , Integer page , Integer row){
        try {
            if (StringUtils.isNoneBlank(userId)){
                return orderService.findOrderByUserId(userId , page , row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 微信订单提交*/
    @PostMapping("/saveOrder")
    public boolean saveOrder(@RequestBody Order order) {
        try {
            /** 保存到redis */
            return cartService.saveOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 微信支付生成二维码 */
    @GetMapping("/genPayCode")
    public Map<String , Object> genPayCode(){
        try {
            String remoteUser = request.getRemoteUser();
            Order userOrder = cartService.findUserOrder(remoteUser);
            String totalMoney = String.valueOf(userOrder.getPayment().doubleValue()*100);
            String[] split = totalMoney.split("\\.");
            System.out.println("============="+totalMoney);
            String money = null;
            if (split.length > 0){
                money = split[0];
            }else {
                money = totalMoney;
            }
            return weixinPayService.genPayCode(String.valueOf(userOrder.getOrderId()) , money);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 查询订单 http://user.pinyougou.com/order/queryPayStatus?outTradeNo=21996*/
    /** 检测支付状态 */
    @GetMapping("/queryPayStatus")
    public Map<String, Integer> queryPayStatus(String outTradeNo){
        Map<String, Integer> data = new HashMap<>();
        data.put("status", 3);
        try {
            // 调用微信支付服务接口
            Map<String,String> resMap = weixinPayService.queryPayStatus(outTradeNo);

            if (resMap != null){
                // SUCCESS-支付成功
                if ("SUCCESS".equals(resMap.get("trade_state"))){

                    // 支付成功，修改支付日志的状态、订单的状态
                    orderService.updateUserPayStatus(outTradeNo);

                    data.put("status", 1);
                }
                // NOTPAY—未支付
                if ("NOTPAY".equals(resMap.get("trade_state"))){
                    data.put("status", 2);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return data;
    }

}
