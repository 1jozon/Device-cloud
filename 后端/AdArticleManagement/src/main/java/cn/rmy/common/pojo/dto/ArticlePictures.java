package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePictures {

    //自增id
    private Integer id;

    //文章标题
    private String articleTitle;

    //图片id
    private Integer pictureId;
}
