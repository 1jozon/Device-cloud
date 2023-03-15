package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLabels {

    //自增id
    private Integer id;

    //文章标题
    private String articleTitle;

    //标签id
    private Integer labelId;
}
