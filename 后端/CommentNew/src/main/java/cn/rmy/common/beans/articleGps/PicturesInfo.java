package cn.rmy.common.beans.articleGps;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.security.PrivateKey;
import java.util.Date;

/**
 * 图片信息
 *
 * @author chu
 * @date 2021/11/23
 */
@Data
@TableName("tb_pictures")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PicturesInfo {

    //图片自增id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //轮播图标题
    private String pictureTitle;

    //图片名
    private String pictureName;

    //图片url
    private String pictureUrl;

    //图片审核并发布
    private Integer pushed;


    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
