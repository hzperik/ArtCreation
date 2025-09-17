package com.neuedu.it.artcreation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface CreationMapper extends BaseMapper<Creation> {

    @Select("${sql}")
    @Results({
            @Result(column = "s_id",        property = "id"),
            @Result(column = "s_title",     property = "title"),
            @Result(column = "s_content",   property = "content"),
            @Result(column = "s_img",       property = "img"),
            @Result(column = "s_click",     property = "click"),
            @Result(column = "s_userid",    property = "userId"),
            @Result(column = "s_cgy",       property = "cgyId"),
            @Result(column = "s_keyword",   property = "keyword"),
            @Result(column = "s_createtime",property = "createTime"),
            @Result(column = "s_url",       property = "url")
    })
    List<Creation> execute(String sql);

    @Update("${sql}")
    @Results({
            @Result(column = "s_id",        property = "id"),
            @Result(column = "s_title",     property = "title"),
            @Result(column = "s_content",   property = "content"),
            @Result(column = "s_img",       property = "img"),
            @Result(column = "s_click",     property = "click"),
            @Result(column = "s_userid",    property = "userId"),
            @Result(column = "s_cgy",       property = "cgyId"),
            @Result(column = "s_keyword",   property = "keyword"),
            @Result(column = "s_createtime",property = "createTime"),
            @Result(column = "s_url",       property = "url")
    })
    void execute1(String sql);

}
