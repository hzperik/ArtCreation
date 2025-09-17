package com.neuedu.it.artcreation;

import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.mapper.CreationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ArtCreationApplicationTests {
    @Autowired
    CreationMapper creationMapper;


    @Test
    void contextLoads() {
        List<Creation> creations=creationMapper.execute("SELECT * FROM creations WHERE s_userid = 1");
        System.out.println(creations);
    }

}
