package com.itheima.controller;

import com.itheima.entity.User;
import com.itheima.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author guojunzhang
 * @date 2024/08/01
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * 用户映射器
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * 保存
     *
     * @param user 用户
     * @return {@link User }
     */
    @PostMapping
    @CachePut(cacheNames = "userCache",key = "#user.id")  //语法规范
    //@CachePut(cacheNames = "userCache",key = "#result.id")  //对象导航写法
//    @CachePut(cacheNames = "userCache",key = "#result.id")  //对象导航写法
    public User save(@RequestBody User user){
        userMapper.insert(user);
        return user;
    }

    /**
     * 按 ID 删除
     *
     * @param id 同上
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "userCache",key = "#id") //删除数据库数据的同时删除redis里面的缓存数据
    public void deleteById(Long id){
        userMapper.deleteById(id);
    }

    /**
     * 全部删除
     */
    @DeleteMapping("/delAll")
    @CacheEvict(cacheNames = "userCache",allEntries = true)//删除userCache下的所有键值对
    public void deleteAll(){
        userMapper.deleteAll();
    }

    /**
     * 按 ID 获取
     *
     * @param id 同上
     * @return {@link User }
     */
    @GetMapping
    @Cacheable(cacheNames = "userCache",key = "#id")
    public User getById(Long id){
        User user = userMapper.getById(id);
        return user;
    }

}
