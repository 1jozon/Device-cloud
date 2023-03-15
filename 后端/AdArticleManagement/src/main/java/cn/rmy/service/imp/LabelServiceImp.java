package cn.rmy.service.imp;


import cn.rmy.common.pojo.dto.Label;
import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.beans.articleGps.ArticleLabelsInfo;
import cn.rmy.common.beans.articleGps.LabelsInfo;
import cn.rmy.dao.ArticleDao;
import cn.rmy.dao.ArticleLabelsDao;
import cn.rmy.dao.LabelDao;
import cn.rmy.service.LabelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 标签服务
 *
 * @author chu
 * @date 2021/11/11
 */
@Service
@Transactional
public class LabelServiceImp implements LabelService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleServiceImp articleService;

    @Autowired
    private ArticleLabelsDao articleLabelsDao;

    @Autowired
    private LabelDao labelDao;


    /**
     * 添加标签到文章
     *
     * @param title      标题
     * @param labelNames 标签的名称
     * @return int
     */
    @Override
    public int insertLabToArt(String title, List<String> labelNames) {

        if (labelNames.size() == 0){
            //请输入正确标签名
            return 2;
        }
        ArticleInfo articleInfo = articleService.getArtInfoByTitle(title);
        if (articleInfo == null){
            //文章为空
            return 3;
        }

        //获取该文章所有标签名
        Set<String> labelNamesOfArt = selectByArticleTitle(title);
        List<ArticleLabelsInfo> insertArtLabInfos = new ArrayList<>();

        for (String labelName : labelNames){
            //Label label = getLabelByName(labelName);
            if (labelNamesOfArt != null && labelNamesOfArt.contains(labelName)){
                //该标签文章已经存在
                continue;
            }else{

                int rec1 = addLab(labelName);
                if (rec1 == -1 || rec1 == 2){
                    continue;
                }
                Label newLab = getLabelByName(labelName);
                ArticleLabelsInfo articleLabelsInfo = new ArticleLabelsInfo();
                articleLabelsInfo.setArticleTitle(title);
                articleLabelsInfo.setLabelId(newLab.getLabelId());
                insertArtLabInfos.add(articleLabelsInfo);
            }
        }
        if (insertArtLabInfos.size() > 0){
            for (ArticleLabelsInfo articleLabelsInfo : insertArtLabInfos){
                int rec = articleLabelsDao.insert(articleLabelsInfo);
                if (rec == -1){
                    //插入文章对应label失败
                    return -1;
                }
            }
        }
        //不含有新标签内容，或直接插入成功
        return 1;
    }

    /**
     * 删除文章中标签
     *
     * @param title 标题
     * @return int
     */
    @Override
    public int deleteLabOfArtByTitle(String title,String labelName) {

        Label label = getLabelByName(labelName);
        if (label == null){
            return 3;
        }
        QueryWrapper<ArticleLabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_title",title).eq("label_id",label.getLabelId());
        List<ArticleLabelsInfo> articleLabelsInfos = articleLabelsDao.selectList(queryWrapper);

        if (articleLabelsInfos.size() == 0){
            return 2;
        }

        //删除所有给标题对应标签
        for (ArticleLabelsInfo articleLabelsInfo : articleLabelsInfos){
            if (!articleLabelsInfo.getArticleTitle().equals(title)){
                break;
            }
            int id = articleLabelsInfo.getId();
            int rec = articleLabelsDao.deleteById(id);
            if (rec != 1){
                //删除失败
                return -1;
            }
        }
        // 删除成功
        return 1;
    }

    /**
     * 获取文章中所有标签名
     *
     * @param title 标题
     * @return {@link Set}<{@link String}>
     */
    @Override
    public Set<String>selectByArticleTitle(String title) {
        QueryWrapper<ArticleLabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_title", title);
        List<ArticleLabelsInfo> articleLabelsInfos = articleLabelsDao.selectList(queryWrapper);
        if (articleLabelsInfos.size() == 0){
            return null;
        }

        Set<Integer> labelIds = new HashSet<>();
        for(ArticleLabelsInfo articleLabelsInfo : articleLabelsInfos){
            labelIds.add(articleLabelsInfo.getLabelId());
        }
        Set<String> labelsNames = new HashSet<>();
        for (Integer labelId : labelIds){
            Label label = getLabelById(labelId);
            labelsNames.add(label.getLabelName());
        }

        return labelsNames;
    }

    /**
     * 添加新标签
     *
     * @param labelName 标签名称
     * @return int
     */
    @Override
    public int addLab(String labelName) {
        if(labelName.length() == 0){
            //标签为空
            return 2;
        }
        QueryWrapper<LabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_name", labelName);

        LabelsInfo labelsInfo1 = labelDao.selectOne(queryWrapper);
        if (labelsInfo1 != null) {
            //标签已存在
            return 3;
        }else{
            LabelsInfo labelsInfo = new LabelsInfo();
            labelsInfo.setLabelName(labelName);
            int rec = labelDao.insert(labelsInfo);
            if (rec == 1){
                //添加成功
                return 1;
            }else{
                //添加失败
                return -1;
            }
        }
    }

    /**
     * 删除标签
     *
     * @param labelName 标签名称
     * @return int
     */
    @Override
    public int deleteLab(String labelName) {
        if(labelName.length() == 0){
            //输入标签为空
            return 2;
        }
        QueryWrapper<LabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_name", labelName);
        LabelsInfo labelsInfo1 = labelDao.selectOne(queryWrapper);
        if(labelsInfo1 != null){
            int labId = labelsInfo1.getId();
            //确定没有文章用该标签
            if (selectArtsOfLab(labelName).size() != 0){
                //存在文章使用该标签，禁止删除
                return 4;
            }

            int rec = labelDao.deleteById(labId);
            if (rec == 1){
                //删除成功
                return 1;
            }else{
                //删除失败
                return -1;
            }
        }else{
            //该标签不存在
            return 3;
        }
    }

    /**
     * 得到标签
     *
     * @param labelName 标签名称
     * @return {@link Label}
     */
    @Override
    public Label getLabelByName(String labelName) {
        QueryWrapper<LabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_name", labelName);
        LabelsInfo labelsInfo = labelDao.selectOne(queryWrapper);
        if (labelsInfo == null){
            return null;
        }
        Label label = new Label();
        label.setLabelId(labelsInfo.getId());
        label.setLabelName(labelsInfo.getLabelName());

        return label;
    }

    /**
     * 通过id获取标签
     *
     * @param labelId 标签id
     * @return {@link Label}
     */
    @Override
    public Label getLabelById(Integer labelId) {
        QueryWrapper<LabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", labelId);
        LabelsInfo labelsInfo = labelDao.selectOne(queryWrapper);

        Label label = new Label();
        label.setLabelId(labelsInfo.getId());
        label.setLabelName(labelsInfo.getLabelName());

        return label;
    }

    /**
     * 获取标签使用的所以文章
     *
     * @param labelName 标签名称
     * @return {@link List}<{@link ArticleInfo}>
     */
    @Override
    public List<ArticleInfo> selectArtsOfLab(String labelName) {

        Label label = getLabelByName(labelName);

        QueryWrapper<ArticleLabelsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_id",label.getLabelId());
        List<ArticleLabelsInfo> articleLabelsInfo = articleLabelsDao.selectList(queryWrapper);
        if (articleLabelsInfo.size() == 0){
            return null;
        }
        List<ArticleInfo> articleInfos = new ArrayList<>();
        for (ArticleLabelsInfo al : articleLabelsInfo){
            ArticleInfo articleInfo = articleService.getArtInfoByTitle(al.getArticleTitle());
            if (articleInfo != null){
                articleInfos.add(articleInfo);
            }else{
                continue;
            }
        }

        return articleInfos;
    }

    /**
     * 条件搜索标签
     *
     * @param condition 条件
     * @return {@link List}<{@link LabelsInfo}>
     */
    @Override
    public List<LabelsInfo> getLabelsByCondition(Map<String, String> condition) {
        QueryWrapper<LabelsInfo> queryWrapper = new QueryWrapper<>();
        if (condition.containsKey("all") && condition.get("all").equals("yes")){
            queryWrapper.eq("deleted", 0);
        }

        List<LabelsInfo> labelsInfoList = labelDao.selectList(queryWrapper);

        return labelsInfoList;
    }

    /**
     * test重新插入id
     *
     * @return {@link Integer}
     */
    @Override
    public Integer insertBackId() {
        LabelsInfo info = new LabelsInfo();
        info.setLabelName("插入");

        int rec = labelDao.insert(info);
        int id = info.getId();

        return id;
    }
}
