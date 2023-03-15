package cn.rmy.controller;

import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.pojo.dto.Article;
import cn.rmy.common.pojo.dto.Message;
import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.service.imp.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 文章控制器
 *
 * @author chu
 * @date 2021/11/11
 */
@RestController
@RequestMapping("rmy/article")
public class ArticleController {

    @Autowired
    private ArticleServiceImp articleService;

    @Autowired
    public UserInfoServiceImp userInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 添加文章
     *
     * @param title     标题
     * @param content   内容
     * @param type      类型
     * @param labelName 标签名称
     * @param files     文件
     * @return {@link CommonResult}
     */
    @RequestMapping("/addArticle")
    public CommonResult addArticle( String title, String content, String jsonMsg ,String userId,Integer type, String[] labelName, MultipartFile[] files){

        if(title == null || title.equals("") || title.length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入文章标题");
        }
        //文章信息
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setTitle(title)
                .setContent(content)
                .setJsonMsg(jsonMsg)
                .setType(type)
                .setWriter(userId);

        //标签信息
        List<String> labelNames = new ArrayList<>();
        for(String label : labelName){
            labelNames.add(label);
        }
        //插入
        int rec = articleService.insert(articleInfo,labelNames,files);

        switch (rec) {
            case -1:
                return CommonResult.error(CommonResultEm.ERROR, "添加文章失败");

            case 0:
                return CommonResult.error(CommonResultEm.ERROR, "添加文章失败，文章为空，请输入文章内容");

            case 1:
                return CommonResult.success("添加成功");

            case 2:
                return CommonResult.error(CommonResultEm.ERROR, "文章标题已存在，请更换适合标题");

            case 3:
                return CommonResult.error(CommonResultEm.ERROR, "文章基本信息添加失败！");

            case 4:
                return CommonResult.error(CommonResultEm.ERROR, "文章图片上传失败，请添加正确文件格式");

            case 5:
                return CommonResult.error(CommonResultEm.ERROR, "文件图片上传失败");

            case 6:
                return CommonResult.error(CommonResultEm.ERROR, "标签插入文章失败");

            case 7:
                return CommonResult.error(CommonResultEm.ERROR, "图片添加到数据库失败");

            case 8:
                return CommonResult.error(CommonResultEm.ERROR, "图片添加图片表失败");

            case 9:
                return CommonResult.error(CommonResultEm.ERROR, "图片添加文章图片表失败");

            default:
                return CommonResult.error(CommonResultEm.ERROR, "未知错误");
        }
    }

    /**
     * 根据标题删除文章
     *
     * @param title 标题
     * @return {@link CommonResult}
     */
    @RequestMapping("/deleteArticle")
    public CommonResult deleteArtByTitle(@RequestParam String title){
        if (title.length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请数据文章标题！");
        }
        int rec = 0;
        try {
            rec = articleService.deleteByTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (rec){
            case -1: return CommonResult.error(CommonResultEm.ERROR,"文章删除失败");

            case 1: return CommonResult.success("文章删除成功");

            case 2: return CommonResult.error(CommonResultEm.ERROR,"文章数据删除失败");

            case 3: return CommonResult.error(CommonResultEm.ERROR,"文章图片关系删除失败");

            case 4: return CommonResult.error(CommonResultEm.ERROR,"文章标签关系删除失败");

            case 5: return CommonResult.error(CommonResultEm.ERROR,"文章不存在！");

            default: return CommonResult.error(CommonResultEm.ERROR,"未知错误");
        }
    }

    /**
     * 更新文章
     *
     * @param title      标题
     * @param newContent 新内容
     * @return {@link CommonResult}
     */
    @RequestMapping("/updateArticle")
    public CommonResult updateArticle(@RequestParam String title, String newContent, String jsonMsg,String userId, int currentRoleId){

        //获取文章信息
        ArticleInfo articleInfo = articleService.getArtInfoByTitle(title);
        if (articleInfo == null){
            return CommonResult.error(CommonResultEm.ERROR,"原文章不存在！");
        }

        //确定文章修改权限
        //非管理员用户和创建者无权修改
        if (!userId.equals(articleInfo.getWriter()) && currentRoleId < 7){
            return CommonResult.error(CommonResultEm.ERROR,"对不起，您无权修改该文章！");
        }

        //有权修改
        int rec = articleService.update(title,newContent,jsonMsg);
        switch (rec){
            case -1: return CommonResult.error(CommonResultEm.ERROR,"更新失败！");

            case 0:  return CommonResult.error(CommonResultEm.ERROR,"原文章不存在！");

            case 1:  return CommonResult.success("更新成功");

            default: return CommonResult.error(CommonResultEm.ERROR,"出现未知错误！");
        }

    }

    /**
     * 多条件查询
     *
     * @param conditions 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/selectArticle/{current}/{size}")
    public CommonResult selectByCondition(@PathVariable("current") int current, @PathVariable("size") int size,@RequestBody Map<String, Object> conditions){
        SelectResult articles = articleService.selectByCondition(conditions,current,size);
        if (articles.getList() == null||articles.getTotal() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"未查到结果，请重新查询");
        }else{
            return CommonResult.success(articles);
        }
    }

    /**
     * 查看文章
     *
     * @param title 标题
     * @return {@link CommonResult}
     */
    @RequestMapping("/seeArticle")
    public CommonResult seeArticle(@RequestParam String title){
        if (title == null || title.length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入准确文章标题！");
        }

        Article article = articleService.getArtByTitle(title);
        /*if(article.getPicturesInfoUrls() == null || article.getPicturesInfoUrls().size() == 0){
            article.setPicturesInfoUrls(null);
        }*/
        if(article == null){
            return CommonResult.error(CommonResultEm.ERROR,"未找到该文章，请确认文章标题正确！");
        }
        //记录阅读量到Redis
        String key = "article:" + article.getArticleId().toString();
        Boolean exists = stringRedisTemplate.hasKey(key);
        Long count;
        if (exists){
            Object object = stringRedisTemplate.opsForValue().get(key);
            count = Long.parseLong(object.toString());
        }
        //测试是否记录成功
        count = stringRedisTemplate.opsForValue().increment(key);
        return CommonResult.success(article);
    }

    /**
     * 首页消息列表
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/messageList/{current}/{size}")
    public CommonResult messageList(@PathVariable("current") int current, @PathVariable("size") int size){

   /*     Users currentUsers = (Users)SecurityUtils.getSubject().getPrincipal();
        String currentUserId = currentUsers.getUserId();*/

        List<Message> messageList = new ArrayList<>();
        SelectResult list = articleService.selectAll(current, size);

        List<ArticleInfo> articles = (List<ArticleInfo>) list.getList();

        for (ArticleInfo articleInfo : articles){
            Message message = new Message();
            message.setId(articleInfo.getId());
            message.setTitle(articleInfo.getTitle())
                    .setTime(articleInfo.getCreateTime());

            messageList.add(message);
        }
        return CommonResult.success(messageList);
    }

    /**
     * 审批文章
     *
     * @param title  标题
     * @param status 状态
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "文章审批")
    @RequestMapping("/approveArticle")
    public CommonResult approveArticle(@RequestParam String title, int status){

        int rec = articleService.approveArticle(title,status);

        switch (rec){

            case -1: return CommonResult.error(CommonResultEm.ERROR,"审核失败");

            case 0: return CommonResult.success("文章不存在，请确定文章标题正确");

            case 1: return CommonResult.success("文章审核成功");

            case 2: return CommonResult.success("文章审核完毕，请勿重复审核");

            case 3: return CommonResult.error(CommonResultEm.ERROR,"审批状态码异常，请输入正确审核吗");

            default: return CommonResult.error(CommonResultEm.ERROR,"出现未知错误");
        }
    }

    /**
     * 发布推送文章
     *
     * @param title 标题
     * @return {@link CommonResult}
     * @throws MqttException mqtt例外
     */
    @LogAnno(operateType = "文章推送")
    @RequestMapping("/releaseArticle")
    public CommonResult releaseArticle(@RequestParam String title, String userId) throws MqttException{

        ArticleInfo articleInfo = articleService.getArtInfoByTitle(title);
        int approved = articleInfo.getApproved();
        if (approved == 0){
            return CommonResult.error(CommonResultEm.ERROR,"文章未审核，请审核后推送！");
        }else if (approved == 1){
            return CommonResult.error(CommonResultEm.ERROR,"文章审核不通过，请审核通过后推送！");
        }
        try{
            articleService.pushAdvertisement(articleInfo, userId) ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.success("推送成功！");
    }
}
