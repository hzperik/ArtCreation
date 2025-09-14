package com.neuedu.it.artcreation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.it.artcreation.entity.pojo.User;
import com.neuedu.it.artcreation.mapper.UserMapper;
import com.neuedu.it.artcreation.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {
}
