package com.neuedu.it.artcreation.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class PublishDTO{
    private String title;
    private String content;
    private String img;
    private Integer click;
    private Integer userId;
    private Integer cgyId;
    private String keyword;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date createTime;
    MultipartFile pic;
}
