package com.neuedu.it.artcreation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neuedu.it.artcreation.entity.pojo.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {
    List<String> getComments(Integer sId);

}
