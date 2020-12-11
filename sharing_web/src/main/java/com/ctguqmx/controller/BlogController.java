package com.ctguqmx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctguqmx.common.lang.Result;
import com.ctguqmx.entity.Blog;
import com.ctguqmx.entity.Category;
import com.ctguqmx.entity.Comment;
import com.ctguqmx.service.BlogService;
import com.ctguqmx.service.CommentService;
import com.ctguqmx.util.SendEmailUtils;
import com.ctguqmx.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JUYI
 * @since 2020-08-24
 */
@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    SendEmailUtils sendEmailUtils;
//    博客列表
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.succ(pageData);
    }
//    查看博客详细信息
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该分享已被删除");
        return Result.succ(blog);
    }
//    删除博客
    @RequiresAuthentication
    @DeleteMapping("/blog/dele/{id}")
    public Result deleBlog(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该分享已被删除");
        Assert.isTrue(blog.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限删除");
        blogService.removeById(id);
        return Result.succ("删除成功",null);
    }

    //登录用户的所有博客
    @RequiresAuthentication
    @GetMapping ("/blog/userBlogs")
    public Result userBlogs(@RequestParam(defaultValue = "1") Integer currentPage) {
        Long userId = ShiroUtil.getProfile().getId();
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created").lambda().eq(Blog::getUserId,userId));

        return Result.succ(pageData);
    }
    //更改和增加博客
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }
    //作者删除评论

    @RequiresAuthentication
    @DeleteMapping("/blog/deleComment/{blogId}/{id}")
    public Result deleComment (@PathVariable(name = "blogId") Long blogId,@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(blogId);
        Assert.isTrue(blog.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限删除");
        Comment comment = commentService.getById(id);
        Assert.notNull(comment, "该评论已被删除");
        commentService.removeById(id);
        return Result.succ("删除成功",null);
    }

//    失效按钮
    @PostMapping("/blog/failure")
    public Result failure(@RequestBody Blog blog){
        sendEmailUtils.sendfailure(blog);
        return Result.succ(null);
    }
//  模糊查询博客
    @GetMapping("/blog/search")
    public Result search(@RequestParam("str") String str) {
        List<Blog> blogList = blogService.list(new QueryWrapper<Blog>().like("title",str).or().like("content",str));
        if(blogList.size() == 0) {
            return Result.fail("无相关信息");
        }
        return Result.succ(blogList);
    }
    //根据类别查看博客
    @GetMapping("/blog/oneCategory")
    public Result oneCategory (@RequestParam("id") Long id,@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created").lambda().eq(Blog::getCategoryId,id));

        return Result.succ(pageData);
    }
}
