package cn.rmy.service;

import cn.rmy.common.pojo.dto.Label;
import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.beans.articleGps.LabelsInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 标签服务
 *
 * @author chu
 * @date 2021/11/08
 */
public interface LabelService {



    /**
     * 文章插入标签
     *
     * @param title      标题
     * @param labelNames 标签的名称
     * @return int
     */
    int insertLabToArt(String title, List<String> labelNames);

    /**
     * 删除文章对应标签
     *
     * @param title 标题
     * @return int
     */
    int deleteLabOfArtByTitle(String title, String labelName);

    /**
     * 通过文章标题选择
     *
     * @param title 标题
     * @return {@link Set}<{@link String}>
     */
    Set<String> selectByArticleTitle(String title);

    /**
     * 添加新标签
     *
     * @param labelName 标签名称
     * @return int
     */
    int addLab(String labelName);

    /**
     * 删除实验室
     * 删除新标签
     *
     * @param labelName 标签名称
     * @return int
     */
    int deleteLab(String labelName);

    /**
     * 通过名称获取标签
     *
     * @param labelName 标签名称
     * @return {@link Label}
     */
    Label getLabelByName(String labelName);


    /**
     * 通过id获取标签
     *
     * @param labelId 标签id
     * @return {@link Label}
     */
    Label getLabelById(Integer labelId);


    /**
     * 选择艺术实验室
     * 获取标签使用的所以文章
     *
     * @param labelName 标签名称
     * @return {@link List}<{@link ArticleInfo}>
     */
    List<ArticleInfo> selectArtsOfLab(String labelName);

    /**
     * 条件搜索标签
     *
     * @param condition 条件
     * @return {@link List}<{@link LabelsInfo}>
     */
    List<LabelsInfo> getLabelsByCondition(Map<String,String> condition);

    /**
     * test重新插入id
     *
     * @return {@link Integer}
     */
    Integer insertBackId();

}
