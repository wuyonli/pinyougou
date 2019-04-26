package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(User user) {
        try {
            // 密码加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());
            // 添加数据
            userMapper.insertSelective(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /**
     * 发送短信验证码
     */
    // 发送验证码
    @Override
    public boolean sendCode(String phone) {
        try {
            /** 生成6位随机数 */
            String code = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .replaceAll("[a-z|A-Z]","")
                    .substring(0, 6);
            System.out.println("验证码：" + code);
            /** 调用短信发送接口 */
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 创建Map集合封装请求参数
            Map<String, String> param = new HashMap<>();
            param.put("phone", phone);
            param.put("signName", signName);
            param.put("templateCode", templateCode);
            param.put("templateParam", "{\"code\":\"" + code + "\"}");
            // 发送Post请求
            String content = httpClientUtils.sendPost(smsUrl, param);
            // 把json字符串转化成Map  content  = {"success":true\false}
            Map<String, Object> resMap = JSON.parseObject(content, Map.class);
            /** 存入Redis中(90秒) */
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            return (boolean)resMap.get("success");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    /**
     * 检验验证码是否正确
     */
    public boolean checkSmsCode(String phone, String code) {
        try {
            String oldCode = (String) redisTemplate.boundValueOps(phone).get();
            return code.equals(oldCode);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    // 修改用户密码
    @Override
    public User updatePassword(String userName, String newPassword) {
        try {
            User user = new User();
            user.setUsername(userName);
            // 根据用户名查询用户
            User user1 = userMapper.selectOne(user);
            // 设置爱新密码
            user1.setPassword(DigestUtils.md5Hex(newPassword));
            // 保存到数据库
            userMapper.updateByPrimaryKeySelective(user1);
            return user1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 根据用户名获取用户信息
    @Override
    public User getUserInfo(String userName) {
        try {
            User user = new User();
            user.setUsername(userName);
            return userMapper.selectOne(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updatePhone(String userName,String phone) {
        try {
            // 查询到用户对象
            User user = new User();
            user.setUsername(userName);
            User user1 = userMapper.selectOne(user);
            //修改手机号
            user1.setPhone(phone);
            userMapper.updateByPrimaryKeySelective(user1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            User user = new User();
            user.setUsername(username);
            return userMapper.selectOne(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
