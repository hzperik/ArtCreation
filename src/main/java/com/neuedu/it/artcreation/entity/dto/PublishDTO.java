package com.neuedu.it.artcreation.entity.dto;

import com.neuedu.it.artcreation.entity.pojo.Creation;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class PublishDTO extends Creation{
    MultipartFile pic;
}
