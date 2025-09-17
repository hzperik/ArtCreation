package com.neuedu.it.artcreation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.it.artcreation.entity.pojo.Comment;
import com.neuedu.it.artcreation.mapper.CommentMapper;
import com.neuedu.it.artcreation.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl
        extends ServiceImpl<CommentMapper,Comment>
        implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public List<String> getComments(Integer sId) {
        return commentMapper.listBySId(sId);
    }
}
