package com.ctguqmx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctguqmx.common.lang.Result;
import com.ctguqmx.entity.Blog;
import com.ctguqmx.entity.Comment;
import com.ctguqmx.service.CommentService;
import com.ctguqmx.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JUYI
 * @since 2020-09-09
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/comments")
    public Result comments (@RequestParam("id") Long id,@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage pageData = commentService.page(page, new QueryWrapper<Comment>().orderByDesc("created").lambda().eq(Comment::getBlogId,id));
        return Result.succ(pageData);
    }

    @RequiresAuthentication
    @PostMapping("/add/{blogId}")
    public Result edit(@Validated @RequestBody Comment comment,@PathVariable(name = "blogId") Long blogId) {

        Comment temp = null;
        temp = new Comment();
        temp.setUserId(ShiroUtil.getProfile().getId());
        temp.setBlogId(blogId);
        temp.setCreated(LocalDateTime.now());
        temp.setStatus(0);
        BeanUtil.copyProperties(comment, temp, "id", "userId","blogId","created", "status");
        commentService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @DeleteMapping("/dele/{id}")
    public Result deleComment(@PathVariable(name = "id") Long id) {
        Comment comment = commentService.getById(id);
        Assert.notNull(comment, "该评论已被删除");
        Assert.isTrue(comment.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限删除");
        commentService.removeById(id);
        return Result.succ("删除成功",null);
    }
}
