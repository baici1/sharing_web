package com.ctguqmx.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctguqmx.entity.Blog;
import com.ctguqmx.entity.User;
import com.ctguqmx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class SendEmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    UserService userService;

    @Value("${spring.mail.username}")
    private String username;
    /**
     * 发送邮件
     * @param title 标题
     * @param content 邮件内容
     * @param email 接收者的邮箱
     */
    private void sendNormalEmail(String email,String title,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(email);
        message.setSubject(title);
        message.setText("尊敬的用户,您好:\n" + "\n本次请求的邮件验证码为:" + content + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");
        try{
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 发送注册验证码
     * @param email 接收者的邮箱
     * @param code 验证码
     */
    public void sendRegisterCode(String email,String code) {
        String content = "尊敬的用户,您好:\n" + "\n本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）";
        sendNormalEmail(email, "资源分享网站注册验证码", content);
    }
    public void sendsucc(String email) {
        String content = "尊敬的用户,您好:\n" + "\n恭喜您注册成功！！！" + "\n(这是一封自动发送的邮件，请不要直接回复）";
        sendNormalEmail(email, "注册成功", content);
    }

    public void sendfailure(Blog blog) {
        String content = "尊敬的用户,您好:\n" + "\n你的题目为："+ blog.getTitle() +"的资源链接已失效！\n"+"请前往修改"
                + "\n(这是一封自动发送的邮件，请不要直接回复）";
        User user = userService.getById(blog.getUserId());
        sendNormalEmail(user.getEmail(), "资源链接失效", content);
    }

    public Boolean checkCode(String email,String code){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String R_code = (String) valueOperations.get(email);
        if (R_code != null && R_code.equals(code)){
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }
}
