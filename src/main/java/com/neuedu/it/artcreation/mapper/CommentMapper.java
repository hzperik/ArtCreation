package com.neuedu.it.artcreation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neuedu.it.artcreation.entity.pojo.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select comment_content from comment where s_id = #{sId}")
    List<String> listBySId(Integer sId);
}
