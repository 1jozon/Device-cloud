package cn.rmy.service.imp;

import cn.rmy.common.dto.Users;
import cn.rmy.common.jsonUtils.JsonUtil;
import cn.rmy.common.pojo.dto.Article;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.pojo.dto.ArticlePush;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.beans.articleGps.PicturesInfo;

import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.redisUtils.RedisUtils;
import cn.rmy.dao.ArticleDao;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.mqttUtils.MqttSendHandle;
import cn.rmy.service.ArticleService;
import cn.rmy.service.Impl.UserWithInstServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
public class ArticleServiceImp implements ArticleService {

    @Value("${effectiveDuration}")
    private String effectDuration;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    @Lazy
    private PictureServiceImp pictureService;

    @Autowired
    @Lazy
    private LabelServiceImp labelService;

    @Autowired
    private UserWithInstServiceImpl userWithInstService;

    @Autowired
    private MqttSendHandle mqttSendHandle;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private InstrumentDao instrumentDao;


    /**
     * 选择所有
     * 分页查询所有文章
     *
     * @param current 当前的
     * @param size    大小
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult selectAll(int current, int size) {

        if(current<=0 || CommonUtil.isNull(current)){
            current = 1;
        }
        if(size<=0 || CommonUtil.isNull(size)){
            size = 4;
        }
        Page<ArticleInfo> page = new Page<>(current,size);
        QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("approved", 2);
        queryWrapper.orderByDesc("create_time");
        articleDao.selectPage(page,queryWrapper);
        List<ArticleInfo> list = page.getRecords();
        long total = page.getTotal();


        return new SelectResult(total,list);
    }

    /**
     * 插入文章
     *
     * @param articleInfo 条信息
     * @param labelNames  标签的名称
     * @param files       文件
     * @return int
     */
    @Override
    public int insert(ArticleInfo articleInfo, List<String> labelNames, MultipartFile[] files) {

        if (articleInfo == null){
            //添加失败，文章不为空
            return 0;
        }else {
            ArticleInfo article = getArtInfoByTitle(articleInfo.getTitle());
            if (article != null){
                //文章已存在
                return 2;
            }
            ArticleInfo newArticleInfo = new ArticleInfo();
            newArticleInfo.setTitle(articleInfo.getTitle())
                    .setContent(articleInfo.getContent())
                    .setType(articleInfo.getType())
                    .setWriter(articleInfo.getWriter())
                    .setJsonMsg(articleInfo.getJsonMsg());
            System.out.println("插入文章");
            int rec = articleDao.insert(newArticleInfo);
            if (rec != 1){
                //文章基本信息添加失败
                return 3;
            }

            //插入图片文件
            int recPic = 0;
            if (files != null && files.length > 0 &&!files[0].isEmpty()){
                System.out.println("插入图片");
                recPic = pictureService.insertPicToArt(articleInfo.getTitle(),files);
                switch (recPic){
                    //图片文件上传失败
                    case -1: return 5;
                    //图片文件上传失败，文件格式错误
                    case 2: return 4;
                    //添加到数据库失败
                    case 3: return 7;
                    //图片表添加失败
                    case 5: return 8;
                    //文章图片表添加失败
                    case 6: return 9;
                }
            }

            //插入标签
            int recLab = 0;
            if (labelNames != null && labelNames.size() > 0 && !labelNames.contains("")) {
                System.out.println("插入标签");
                recLab = labelService.insertLabToArt(articleInfo.getTitle(), labelNames);
                if (recLab == -1) {
                    System.out.println("标签插入文章失败");
                    return 6;
                }
            }
            boolean r2 = (recLab == 1 || recLab == 0);
            boolean r3 = (recPic == 1 || recPic == 0);

            if (rec == 1 && r2 && r3){
                return 1; //文章所有信息插入成功
            }else {
                System.out.println("文章插入失败");
                return-1;
            }
        }
    }

    /**
     * 按标题删除文章
     *
     * @param title 标题
     * @return int
     */
    @Override
    public int deleteByTitle(String title) throws Exception {
        ArticleInfo articleInfo = getArtInfoByTitle(title);
        if (articleInfo == null){
            return 5;
        }
        Integer id = articleInfo.getId();
        //删除文章表
        int rec = articleDao.deleteById(id);
        if (rec == -1){
            return 2;  //文章表删除失败
        }
        //删除文章图片表
        int recPic = pictureService.deletePicOfArtByTitle(title);
        if (recPic == -1){
            return 3;  //文章图片关系删除失败
        }
        //删除文章标签表
        int recLab = 1;
        Set<String> labelNames = labelService.selectByArticleTitle(title);
        if (labelNames != null){
            for (String labelName : labelNames){
                recLab = labelService.deleteLabOfArtByTitle(title, labelName);
                if (recLab != 1){
                    continue;
                }
            }
        }
        if (rec != 1 ||  recPic != 1 ||  recLab != 1 ){
            return -1;  //删除失败
        }
        return 1; // 删除成功
    }

    /**
     * 编辑、更新文章
     *
     * @param newContent 新内容
     * @param title      标题
     * @param jsonMsg    json味精
     * @return int
     */
    @Override
    public int update(String title, String newContent, String jsonMsg) {
        QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        ArticleInfo articleInfo = articleDao.selectOne(queryWrapper);

        if (articleInfo == null){
            //原文章不存在
            return 0;
        }

        articleInfo.setContent(newContent);
        articleInfo.setJsonMsg(jsonMsg);
        int rec = articleDao.updateById(articleInfo);
        if (rec == 1){
            //文章更新成功
            return 1;
        }else{
            //文章更新失败
            return -1;
        }
    }

    /**
     * 通过id查询文章info
     *
     * @param articleId 文章的id
     * @return {@link ArticleInfo}
     */
    @Override
    public List<ArticleInfo> selectArtInfoBdyId(List<Integer> articleId) {
        List<ArticleInfo> infoList = new ArrayList<>();
        QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
        if(articleId ==null || articleId.size() == 0){
            return null;
        }
        queryWrapper.in("id", articleId);
        infoList = articleDao.selectList(queryWrapper);

        return infoList;
    }

    /**
     * //条件查询文章
     *
     * @param condition 条件
     * @return {@link List}<{@link ArticleInfo}>
     */
    @Override
    public SelectResult selectByCondition(Map<String, Object> condition, int current, int size) {

        Subject currentUser = SecurityUtils.getSubject();
        Users users = (Users) currentUser.getPrincipal();

        if (current <= 0 ||CommonUtil.isNull(current)){
            current = 1;
        }
        if (size <= 0 || CommonUtil.isNull(size)){
            size = 4;
        }
        String title = "";
        int type = 0;
        String labelName = "";
        int approved = 2;
        int all = 0;
        if (condition.containsKey("title")){
            title = condition.get("title").toString();
        }
        if (condition.containsKey("type")){
            type = (int)condition.get("type");
        }
        if (condition.containsKey("labelName")){
            labelName = condition.get("labelName").toString();
        }
        if (condition.containsKey("approved")){
            approved = (int) condition.get("approved");
        }
        if (condition.containsKey("all")){
            all = (int)condition.get("all");
        }

        List<ArticleInfo> list = new ArrayList<>();
        List<Article> articleList = new ArrayList<>();

        QueryWrapper<ArticleInfo> queryWrapper =  new QueryWrapper<>();

        if (condition.containsKey("title")) {
            queryWrapper.like("title", title)
                    .eq("deleted", 0);
        }
        if (condition.containsKey("approved")){
            queryWrapper.eq("approved",approved);
        }
        if (condition.containsKey("type")){
            queryWrapper.eq("type",type);
        }
        if (all == 1 || condition.size() == 0){
            queryWrapper.ge("id",1).eq("deleted",0);
        }
        if (users.getCurrentRoleId() < 7){
            //普通用户只能看到审核通过的,或者作者是自己的
            queryWrapper.eq("approved", 2).or().eq("writer",users.getUserId());
        }
        //降序展示
        queryWrapper.orderByDesc("create_time");
        list = articleDao.selectList(queryWrapper);
        if (list == null || list.size() == 0){
            return new SelectResult(0L,list);
        }

    /*    for (ArticleInfo articleInfo : list) {
            Article article = new Article();
            article = getArtByTitle(articleInfo.getTitle());
            if (condition.containsKey("labelName")) {
                if (article.getLabelNames() == null || !(article.getLabelNames().contains(labelName))) {
                    continue;
                }
            }
            articleList.add(article);
        }

        System.out.print("文章dto查询");
        Long thirdStart = System.currentTimeMillis();
        System.out.println(thirdStart - start);

        List<Article> res = new ArrayList<>();
        current = current - 1;
        for (int i = current * size; i < Math.min(current * size + size, articleList.size()); i++) {
            res.add(articleList.get(i));
        }*/

        List<Article> res = new ArrayList<>();
        current = current - 1;
        for (int i = current * size; i < Math.min(current * size + size, list.size()); i++) {

            Article article = new Article();
            //article = getArtByTitle(articleInfo.getTitle());
            article = getArtByTitle(list.get(i).getTitle());
            if (condition.containsKey("labelName")) {
                if (article.getLabelNames() == null || !(article.getLabelNames().contains(labelName))) {
                    continue;
                }
            }
            res.add(article);
        }

        return new SelectResult((long) list.size(), res);
    }

    /**
     * 审批文章
     *
     * @param title  标题
     * @param status 状态
     * @return int
     */
    @Override
    public int approveArticle(String title, int status) {
        if (title.length() == 0 ){
            //文章不存在
            return 0;
        }
        if (status != 1 && status != 2){
            //注册状态异常，请输入正确审批码
            return 3;
        }
        ArticleInfo articleInfo = getArtInfoByTitle(title);
        if (articleInfo == null){
            return 0;
        }
        if (articleInfo.getApproved() == status){
            //文章已审批
            return 2;
        }
        articleInfo.setApproved(status);
        int rec = articleDao.updateById(articleInfo);
        if (rec == 1){
            //成功
            return 1;
        }else {
            //失败
            return -1;
        }
    }

    /**
     * 推送
     *
     * @param articleInfo 条信息
     * @param userId      用户id
     * @throws Exception 异常
     */
    @Override
    public void pushAdvertisement(ArticleInfo articleInfo, String userId) throws MqttException {
        //获取文章
        Article article = getArtByTitle(articleInfo.getTitle(),userId);
        String content = article.getContent();
        Set<String> ids = getInsIdsByTitle(userId);
        ArticlePush articlePushMsg = new ArticlePush();
        if(ids == null){
            ids = new HashSet<>();

        }
        articlePushMsg.setIds(ids);
        articlePushMsg.setArticle(content);

        Set<String> pictureUrls = new HashSet<>();
        List<String> urlsList = article.getPicturesInfoUrls();
        if (urlsList != null && urlsList.size() != 0){
            for (String url : urlsList){
                if (url != null && url.length() != 0){
                    pictureUrls.add(url);
                }
                continue;
            }
        }else {
            pictureUrls = null;
        }
        articlePushMsg.setPicture(pictureUrls);
        try{
            System.out.println("测试审核入缓存");
        //    System.out.println(articlePushMsg);
            mqttSendHandle.sendHandle("articlePush",2, JsonUtil.jsonToString(articlePushMsg));
            redisUtils.set("newArt:" + articleInfo.getId(),articleInfo.getId(),Integer.valueOf(effectDuration) * 24 * 60 * 60);
        }catch (Exception e){
            e.getCause();
        }
        articleInfo.setPublished(1);
        articleDao.updateById(articleInfo);
    }

    /**
     * 推动计划上所有的广告
     */
    @Override
    public void pushAllAdOnScheduled() {

        List<ArticleInfo> infoList = new ArrayList<>();
        Set<String> keyList = redisTemplate.keys("newArt:" + "*");
        List<Object> newArtIdList = redisTemplate.opsForValue().multiGet(keyList);
        //System.out.println("测试缓存内文章id" + newArtIdList);
        if(newArtIdList.size() == 0){
            return;
        }
        List<Integer> newArtIds = new LinkedList<>();
        for(Object id : newArtIdList){
            //System.out.println("String id: " + id);
            newArtIds.add((int)Integer.valueOf(id.toString()));
        }
        infoList = selectArtInfoBdyId(newArtIds);

        Set<String> insIds = getAllInsId();
        //System.out.println("测试收到仪器id：" + insIds);

        if(insIds.size() == 0){
            return;
        }
        for(ArticleInfo info : infoList){
            Article article = getArtByTitle(info.getTitle());
            String content = article.getContent();

            ArticlePush articlePushMsg = new ArticlePush();

            articlePushMsg.setIds(insIds);
            articlePushMsg.setArticle(content);

            Set<String> pictureUrls = new HashSet<>();
            List<String> urlsList = article.getPicturesInfoUrls();
            if (urlsList != null && urlsList.size() != 0){
                for (String url : urlsList){
                    if (url != null && url.length() != 0){
                        pictureUrls.add(url);
                    }
                    continue;
                }
            }else {
                pictureUrls = null;
            }
            articlePushMsg.setPicture(pictureUrls);
            mqttSendHandle.sendHandle("articlePush",2, JsonUtil.jsonToString(articlePushMsg));
        }

    }

    /**
     * 根据文章标题(模糊标题)获取文章信息
     *
     * @param title 标题
     * @return {@link List}<{@link ArticleInfo}>
     */
    @Override
    public List<ArticleInfo> getArtInfoByEasyTitle(String title) {
        QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        List<ArticleInfo> articleInfos = articleDao.selectList(queryWrapper);

        if (articleInfos.size() == 0){
            return null;
        }
        return  articleInfos;

    }

    /**
     * 根据文章标题(准确标题)获取文章信息
     *
     * @param title 标题
     * @return {@link ArticleInfo}
     */
    @Override
    public ArticleInfo getArtInfoByTitle(String title) {

        QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        List<ArticleInfo> articleInfos = articleDao.selectList(queryWrapper);

        if (articleInfos.size() == 0 ){
            return null;
        }
        return  articleInfos.get(0);
    }

    /**
     * 根据文章标题获取文章
     *
     * @param title         标题
     * @param currentUserId 当前用户id
     * @return {@link Article}
     */
    @Override
    public Article getArtByTitle(String title, String currentUserId) {
        ArticleInfo articleInfo = getArtInfoByTitle(title);

        //订阅文章仪器集合
        Set<String> instrumentIds;
        List<PicturesInfo> picturesInfos;  //文章图片集
        Set<String> labelNames;  //文章标签名集合

        picturesInfos = pictureService.selectByArticleTitle(title);
        List<String> pictureInfoUrls = new ArrayList<>();
        if (picturesInfos != null){
            for (PicturesInfo info : picturesInfos){
                String url = info.getPictureUrl();
                if (url.length() == 0){
                    continue;
                }
                pictureInfoUrls.add(url);
            }
        }

        labelNames = labelService.selectByArticleTitle(title);
        instrumentIds = getInsIdsByTitle(currentUserId);

        Article article = new Article(articleInfo.getId(),articleInfo.getTitle(),articleInfo.getContent()
                ,articleInfo.getType(),articleInfo.getCountRead(),articleInfo.getJsonMsg(),articleInfo.getWriter(),articleInfo.getApproved(),articleInfo.getPublished(),pictureInfoUrls
                ,labelNames,instrumentIds,articleInfo.getCreateTime());

        return article;
    }

    @Override
    public Article getArtByTitle(String title) {

        ArticleInfo articleInfo = getArtInfoByTitle(title);
        if (CommonUtil.isNull(articleInfo)){
            return null;
        }

        List<PicturesInfo> picturesInfos;  //文章图片集
        Set<String> labelNames;  //文章标签名集合

        picturesInfos = pictureService.selectByArticleTitle(title);
        List<String> pictureInfoUrls = new ArrayList<>();
        if (picturesInfos != null){
            for (PicturesInfo info : picturesInfos){
                String url = info.getPictureUrl();
                if (url.length() == 0){
                    continue;
                }
                pictureInfoUrls.add(url);
            }
        }

        labelNames = labelService.selectByArticleTitle(title);

        Article article = new Article(articleInfo.getId(),articleInfo.getTitle(),articleInfo.getContent()
                ,articleInfo.getType(),articleInfo.getCountRead(),articleInfo.getJsonMsg(),articleInfo.getWriter(),articleInfo.getApproved(),articleInfo.getPublished(),pictureInfoUrls
                ,labelNames,null,articleInfo.getCreateTime());

        return article;
    }

    /**
     * 根据当前用户权限获取仪器id
     *
     * @param currentUserId 当前用户id
     * @return {@link Set}<{@link String}>
     */
    @Override
    public Set<String> getInsIdsByTitle(String currentUserId) {
        Set<String> IDS = new HashSet<>();
        //Article article = getArtByTitle(title);

        List<Instrument> instruments = userWithInstService.getInstsByUserId(currentUserId);

        for (Instrument ins : instruments){
            IDS.add(ins.getInstrumentId());
        }

        return IDS;
    }

    /**
     * 得到所有ins id
     *
     * @return {@link List}<{@link String}>
     */
    @Override
    public Set<String> getAllInsId() {
        Set<String> insIdList = new HashSet<>();
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        List<Instrument> instrumentList = instrumentDao.selectList(queryWrapper);
        if(instrumentList.size() == 0){
            return null;
        }
        for(Instrument ins : instrumentList){
            insIdList.add(ins.getInstrumentId());
        }

        return insIdList;
    }
}
