package cn.rmy.common.beans.articleGps;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文章信息
 *
 * @author chu
 * @date 2021/11/11
 */
@Data
@TableName("tb_ads_articles")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleInfo {

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 文章主题
     */
    private String title;

    /**
     * 文章内容，小于150字
     */
    private String content;

    /**
     * 文章类型 0-文章、1-广告、2-轮播图
     */
    private int type;

    /**
     * 阅读量
     */
    private int countRead;

    /**
     * json信息
     */
    private String jsonMsg;

    /**
     * 文章创建用户名
     */
    private String writer;

    /**
     * 审核状态 0-未审核； 1-审核不通过； 2-审核通过
     */
    private int approved;

    /**
     * 发布状态 0-未发布；1-已发布
     */
    private int published;

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
