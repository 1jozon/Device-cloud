package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {

    //图片id
    private Integer pictureId;

    //轮播图标题
    private String pictureTitle;

    //图片名
    private String pictureName;

    //图片url
    private String pictureUrl;

    //图片审核并发布
    private Integer pushed;
}
