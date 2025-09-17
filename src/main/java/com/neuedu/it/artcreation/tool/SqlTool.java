package com.neuedu.it.artcreation.tool;

import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.mapper.CreationMapper;
import com.neuedu.it.artcreation.mapper.UserMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SqlTool {

    @Autowired
    CreationMapper creationMapper;

    @Tool(description = "根据SQL查询数据库creations表，返回结果")
    public List<Creation> searchDb(@ToolParam(description = "SQL语句") String sql){
        List<Creation> creations=creationMapper.execute(sql);
        return creations;
    }


    public void updateDb(@ToolParam(description = "SQL语句") String sql){
        creationMapper.execute1(sql);
    }

//    private JdbcTemplate jdbcTemplate;
//
//
//    @Tool(description = "SQL查询数据库")
//    /** 查询：AI 生成 select 语句 */
//    public List<Map<String, Object>> query(@ToolParam(description = "SQL语句")String sql) {
//        // 仅允许读
//        String upper = sql.trim().toUpperCase(Locale.ROOT);
//        if (!upper.startsWith("SELECT")) {
//            throw new IllegalArgumentException("只允许 SELECT 语句");
//        }
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    /** 写操作：AI 生成 update/delete/insert */
//    @Tool(description = "SQL更新数据库")
//    public int update(@ToolParam(description = "SQL语句") String sql) {
//        String upper = sql.trim().toUpperCase(Locale.ROOT);
//        if (!upper.startsWith("UPDATE") &&
//                !upper.startsWith("DELETE") &&
//                !upper.startsWith("INSERT")) {
//            throw new IllegalArgumentException("仅支持 UPDATE/DELETE/INSERT");
//        }
//        return jdbcTemplate.update(sql);
//    }

}
