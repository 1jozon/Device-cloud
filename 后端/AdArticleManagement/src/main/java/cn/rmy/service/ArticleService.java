package cn.rmy.service;

import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.pojo.dto.Article;
import cn.rmy.common.beans.articleGps.ArticleInfo;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleService {

    /**
     * 查询所有文章
     *
     * @param current 当前的
     * @param size    大小
     * @return {@link SelectResult}
     */
    SelectResult selectAll(int current, int size);

    /**
     * 添加文章
     *
     * @param articleInfo 条信息
     * @param labelNames  标签的名称
     * @param files       文件
     * @return int
     */
    int insert(ArticleInfo articleInfo, List<String> labelNames, MultipartFile[] files);

    /**
     * 根据文章标题删除文章
     *
     * @param title 标题
     * @return int
     */
    int deleteByTitle(String title) throws Exception;

    /**
     * 更新文章信息
     *
     * @param title      标题
     * @param newContent 新内容
     * @param jsonsg     jsonsg
     * @return int
     */
    int update(String title, String newContent, String jsonsg);

    /**
     * 通过id查询文章info
     *
     * @param articleId 文章的id
     * @return {@link ArticleInfo}
     */
    List<ArticleInfo> selectArtInfoBdyId(List<Integer> articleId);

    /**
     * 选择的条件
     * 根据条件分页查询文章
     *
     * @param condition 条件
     * @param current   当前的
     * @param size      大小
     * @return {@link SelectResult}
     */
    SelectResult selectByCondition(Map<String, Object> condition,int current, int size);

    /**
     * 审批发布
     *
     * @param title  标题
     * @param status 状态
     * @return int
     */
    int approveArticle(String title, int status);

    /**
     * 推动广告
     * 推送
     *
     * @param articleInfo 条信息
     * @param userId      用户id
     * @throws MqttException mqtt例外
     */
    void pushAdvertisement(ArticleInfo articleInfo, String userId) throws MqttException;

    /**
     * 推动计划上所有的广告
     */
    void pushAllAdOnScheduled();

    /**
     * 根据标题（大致标题）获取文章信息
     *
     * @param title 标题
     * @return {@link List}<{@link ArticleInfo}>
     */
    List<ArticleInfo> getArtInfoByEasyTitle(String title);

    /**
     * 根据标题（准确标题）获取文章信息
     *
     * @param title 标题
     * @return {@link ArticleInfo}
     */
    ArticleInfo getArtInfoByTitle(String title);

    /**
     * 根据文章标题获取文章
     *
     * @param title         标题
     * @param currentUserId 当前用户id
     * @return {@link Article}
     */
    Article getArtByTitle(String title, String currentUserId);

    /**
     * 根据文章标题获取文章
     *
     * @param title 标题
     * @return {@link Article}
     */
    Article getArtByTitle(String title);

    /**
     * 根据文章获得仪器IDS
     *
     * @param currentUserId 当前用户id
     * @return {@link Set}<{@link String}>
     */
    Set<String> getInsIdsByTitle(String currentUserId);

    /**
     * 得到所有ins id
     *
     * @return {@link List}<{@link String}>
     */
    Set<String> getAllInsId();

}
