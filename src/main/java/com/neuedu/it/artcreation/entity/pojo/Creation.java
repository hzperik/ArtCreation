package com.neuedu.it.artcreation.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

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
    private Date createTime;
    @TableField(exist = false)
    private String userNickName;
    @TableField(exist = false)
    private String cgyName;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCgyId() {
        return cgyId;
    }

    public void setCgyId(Integer cgyId) {
        this.cgyId = cgyId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getCgyName() {
        return cgyName;
    }

    public void setCgyName(String cgyName) {
        this.cgyName = cgyName;
    }
}
