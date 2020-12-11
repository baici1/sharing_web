package com.ctguqmx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctguqmx.common.lang.Result;
import com.ctguqmx.entity.Blog;
import com.ctguqmx.entity.Category;
import com.ctguqmx.entity.Comment;
import com.ctguqmx.entity.User;
import com.ctguqmx.service.CategoryService;
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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @DeleteMapping("/dele/{id}")
    public Result deleCategory(@PathVariable(name = "id") Long id) {
        Category category = categoryService.getById(id);
        Assert.notNull(category, "该类别已被删除");
        categoryService.removeById(id);
        return Result.succ("删除成功",null);
    }

    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody Category category) {

        Category temp = null;
        if(category.getId() != null) {
            temp = categoryService.getById(category.getId());
        } else {
            temp = new Category();
            temp.setStatus(0);
        }
        BeanUtil.copyProperties(category, temp, "id","status");
        categoryService.saveOrUpdate(temp);

        return Result.succ(null);
    }

//    类别列表
    @GetMapping("/categories")
    public Result categories(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage pageData = categoryService.page(page, new QueryWrapper<Category>().orderByAsc("id"));

        return Result.succ(pageData);
    }

//    获取类别信息
    @PostMapping("/categoryMessage")
    public Result categorymessage(@RequestParam(value = "id") String id){
        Category category = categoryService.getById(id);
        return Result.succ(category);
    }
}
