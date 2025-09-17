package com.neuedu.it.artcreation.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@TableName("creations")
public class Creation {
    @TableId("s_id")
    private Integer id;
    @TableField("s_title")
    private String title;
    @TableField("s_content")
    private String content;
    @TableField("s_img")
    private String img;
    @TableField("s_click")
    private Integer click;
    @TableField("s_userid")
    private Integer userId;
    @TableField("s_cgy")
    private Integer cgyId;
    @TableField("s_keyword")
    private String keyword;
    @TableField("s_createtime")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date createTime;
    @TableField("s_imgfile")
    private byte[] imgBytes;
    @TableField(exist = false)
    private String userNickName;
    @TableField(exist = false)
    private String cgyName;
    @TableField("s_url")
    private String url;
}
