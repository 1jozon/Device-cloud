package cn.rmy.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 文章
 *
 * @author chu
 * @date 2021/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    /**
     * 文章的id
     */
    private Integer articleId;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容，小于150字
     */
    private String content;

    /**
     * 文章类型，0-文章； 1-广告
     */
    private int type;

    /**
     * 阅读量
     */
    private int countRead;

    /**
     * json味精
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

    /**
     * 文章图片信息集合
     */
    private List<String> picturesInfoUrls;


    /**
     * 文章标签名字集合
     */
    private Set<String> labelNames;

    /**
     * 订阅该文章的仪器ID
     */
    private Set<String> instrumentIds;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
