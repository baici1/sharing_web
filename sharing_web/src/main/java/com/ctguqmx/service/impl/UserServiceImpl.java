package com.ctguqmx.service.impl;

import com.ctguqmx.entity.User;
import com.ctguqmx.mapper.UserMapper;
import com.ctguqmx.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
