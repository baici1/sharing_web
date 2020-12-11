package com.ctguqmx.service.impl;

import com.ctguqmx.entity.Blog;
import com.ctguqmx.mapper.BlogMapper;
import com.ctguqmx.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JUYI
 * @since 2020-08-24
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
