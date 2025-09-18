package com.neuedu.it.artcreation.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("comment")
public class Comment {
    @TableId("comment_id")
    private Integer commentId;
    @TableField("s_id")
    private Integer sId;
    @TableField("comment_content")
    private String commentContent;
}
