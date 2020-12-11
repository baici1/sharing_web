package com.ctguqmx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctguqmx.common.dto.LoginDto;
import com.ctguqmx.common.lang.Result;
import com.ctguqmx.entity.User;
import com.ctguqmx.service.UserService;
import com.ctguqmx.util.RandomTools;
import com.ctguqmx.util.SendEmailUtils;
import com.ctguqmx.util.ShiroUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.micrometer.core.instrument.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JUYI
 * @since 2020-08-24
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    SendEmailUtils sendEmailUtils;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/index")
    public Result index(){
        User user =  userService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){
        return Result.succ(user);
    }

    @PostMapping("/regist")
    public Result regist(@RequestBody User user,@RequestParam(value = "code") String code){
        User user2 = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(user2 != null){
            return Result.fail("该用户名已存在");
        }
        User user3 = userService.getOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        if(user3 != null){
            return Result.fail("该邮箱已被注册");
        }
        User user1 = null;
        user1 = new User();
        user1.setStatus(0);
        user1.setLastLogin(null);
        user1.setCreated(LocalDateTime.now());
        String password = user.getPassword();
        Md5Hash md5 = new Md5Hash(password);
        user1.setPassword(md5.toString());
        BeanUtil.copyProperties(user, user1, "created","status","password","lastLogin");
        userService.saveOrUpdate(user1);
        if(sendEmailUtils.checkCode(user1.getEmail(),code)){
            sendEmailUtils.sendsucc(user1.getEmail());
            return Result.succ("注册成功",null);
        }else {
            userService.removeById(user1.getId());
            return Result.fail("注册失败，验证码错误");
        }
    }

    @PostMapping("/sendCode")
    public Result sendCode(@RequestParam(value = "email") String email){
        try{
            String code = RandomTools.randomCode();
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(email,code);
            redisTemplate.expire(email,60*5,TimeUnit.SECONDS);
            log.info("开始发送邮件");
            sendEmailUtils.sendRegisterCode(email,code);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.fail("发送失败");
        }
        return Result.succ("发送成功");
    }

    @PostMapping("/changePw")
    public Result changePw(@RequestParam(value = "email") String email,@RequestParam(value = "password") String password,@RequestParam(value = "code") String code) {
        if (sendEmailUtils.checkCode(email,code)) {
            User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
            if(user == null){
                return Result.fail("该邮箱未被注册！");
            }else {
                Md5Hash md5 = new Md5Hash(password);
                user.setPassword(md5.toString());
                userService.saveOrUpdate(user);
                return Result.succ("修改成功");
            }
        }else {
            return Result.fail("验证码错误");
        }
    }

    @RequiresAuthentication
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody User user) {

        User temp = null;
        temp = userService.getById(user.getId());
        Assert.isTrue(temp.getId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        BeanUtil.copyProperties(user, temp,  "created", "status","password","last_login");
        userService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @PostMapping("/userMessage")
    public Result usermessage(@RequestParam(value = "id") String id){
        User user = userService.getById(id);
        return Result.succ(user);
    }


//    管理员的功能

    @GetMapping("/users")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        Page page = new Page(currentPage, 5);
        IPage pageData = userService.page(page, new QueryWrapper<User>().orderByDesc("created"));

        return Result.succ(pageData);
    }

    @PostMapping("/adminedit")
    public Result adminedit(@Validated @RequestBody User user) {

        User temp = null;
        if(user.getId() != null) {
            temp = userService.getById(user.getId());
        } else {
            User user2 = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
            if(user2 != null){
                return Result.fail("该用户名已存在");
            }
            User user3 = userService.getOne(new QueryWrapper<User>().eq("email", user.getEmail()));
            if(user3 != null){
                return Result.fail("该邮箱已被注册");
            }
            temp = new User();
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
            temp.setLastLogin(null);
            String password = user.getPassword();
            Md5Hash md5 = new Md5Hash(password);
            temp.setPassword(md5.toString());
        }

        BeanUtil.copyProperties(user, temp,  "created", "status","password","last_login");
        userService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @DeleteMapping("/dele/{id}")
    public Result deleUser(@PathVariable(name = "id") Long id) {
        User user = userService.getById(id);
        Assert.notNull(user, "该分享已被删除");
        userService.removeById(id);
        return Result.succ("删除成功",null);
    }
}
