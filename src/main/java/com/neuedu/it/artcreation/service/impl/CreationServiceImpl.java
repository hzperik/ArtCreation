package com.neuedu.it.artcreation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.mapper.CreationMapper;
import com.neuedu.it.artcreation.service.CreationService;
import org.springframework.stereotype.Service;

@Service
public class CreationServiceImpl
        extends ServiceImpl<CreationMapper, Creation>
        implements CreationService {
}
