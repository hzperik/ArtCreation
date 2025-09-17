package com.neuedu.it.artcreation.controller;

import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.dto.CommentDTO;
import com.neuedu.it.artcreation.entity.pojo.Comment;
import com.neuedu.it.artcreation.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/comment/publish")
    public RespEntity commentPublish(@RequestBody CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setSId(commentDTO.getCreationId());
        comment.setCommentContent(commentDTO.getCommentContent());
        commentService.save(comment);
        return RespEntity.success("评论成功",null);
    }
    @PostMapping("/comment/query")
    public RespEntity<List<String>> getComment(Integer creationId){
        List<String> comments = commentService.getComments(creationId);
        System.out.println(comments);
        return RespEntity.success("查询成功",comments);
    }
}
