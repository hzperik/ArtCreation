package com.neuedu.it.artcreation.controller;

import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.pojo.Comment;
import com.neuedu.it.artcreation.entity.pojo.User;
import com.neuedu.it.artcreation.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    @Qualifier("chatClientComment")
    private ChatClient chatClientComment;
    @PostMapping("/comment/publish")
    public RespEntity commentPublish(HttpServletRequest request, String commentContent){
        String answer = chatClientComment.prompt()
                .user(commentContent)
                .call()
                .content();
        System.out.println(answer);
        if (answer.equals("forbid")){
            return RespEntity.error("评论不合规",null);
        }
        User user= (User) request.getAttribute("curUser");
        Integer userId = user.getId();
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        comment.setSId(userId);
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
