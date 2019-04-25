package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference(timeout = 10000)
    private AddressService addressService;

    @GetMapping("/findAll")
    public List<Address> findAll() {
        try {
            return addressService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Address address) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            address.setUserId(name);
            address.setIsDefault("0");
            addressService.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean update(@RequestBody Address address) {
        try {
            addressService.update(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //删除
    @GetMapping("/delete")
    public boolean delete(Long id) {
        try {
            System.out.println("删除的id" + id);
            addressService.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //修改默认地址
    @GetMapping("/changer")
    public boolean changer(@RequestParam("id") Long id,@RequestParam("isDefault") String isDefault){
        try {
            Address address = new Address();
            address.setId(id);
            if("0".equals(isDefault)){
                isDefault = "1";
            }
            address.setIsDefault(isDefault);
            addressService.update(address);
            addressService.upadteByDefault(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
