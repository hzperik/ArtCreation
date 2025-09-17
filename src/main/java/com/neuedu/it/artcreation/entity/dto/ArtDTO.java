package com.neuedu.it.artcreation.entity.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class ArtDTO {
    private String title;
    private String content;
    private String keyword;
    private String imageUrl;
}
