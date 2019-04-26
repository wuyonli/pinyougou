package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 商家控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference(timeout = 10000)
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 商家申请入驻 */
    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller){
        try{
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 商家更改信息 */
    @PostMapping("/update")
    public boolean update(@RequestBody Seller seller){
        try{
            sellerService.update(seller);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**获取登入的商家信息*/
    @GetMapping("/findOne")
    public Seller findOne(){
        try {
            //获取登入用户名
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
           return sellerService.findOne(sellerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*修改登入的商家的密码*/
    @PostMapping("/updatePassword")
    public boolean updatePassword(@RequestParam("pwd") String pwd,@RequestParam("newPwd") String newPwd){
        try {
            //获取登入用户
            Seller seller = sellerService.findOne(SecurityContextHolder.getContext().getAuthentication().getName());
            //获取登入用户的密码
            String password = seller.getPassword();
            /*对旧密码加密*/
            BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            if (passwordEncoder.matches(pwd,password)){
                seller.setPassword(passwordEncoder.encode(newPwd));
                sellerService.update(seller);
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }



}
