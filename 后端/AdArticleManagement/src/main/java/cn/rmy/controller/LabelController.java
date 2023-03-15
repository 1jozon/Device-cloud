package cn.rmy.controller;

import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.beans.articleGps.LabelsInfo;
import cn.rmy.common.pojo.dto.Label;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.service.imp.LabelServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 标签控制器
 *
 * @author chu
 * @date 2021/11/5
 */
@RestController
@RequestMapping("rmy/label")
public class LabelController {

    @Autowired
    private LabelServiceImp labelService;

    /**
     * 创建新标签
     *
     * @param label 标签
     * @return {@link CommonResult}
     */
    @RequestMapping("/insertNewLab")
    public CommonResult insertNewLab(@RequestBody Label label){
        if (label.getLabelName().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入标签名！");
        }
        int rec = labelService.addLab(label.getLabelName());
        switch (rec){
            case -1: return CommonResult.error(CommonResultEm.ERROR,"添加失败！");

            case 1: return CommonResult.success("添加成功！");

            case 3: return CommonResult.error(CommonResultEm.ERROR,"标签已存在！");

            default: return CommonResult.error(CommonResultEm.ERROR,"添加失败，未知错误！");
        }
    }

    /**
     * 添加标签到文章
     *
     * @param map 地图
     * @return {@link CommonResult}
     */
    @RequestMapping("/insertLabToArt")
    public CommonResult insertLabToArt(@RequestBody Map<String, Object> map){

        String title = map.get("title").toString();
        List<String> labelNames =(ArrayList<String>)map.get("labelNames");

        int rec = labelService.insertLabToArt(title,labelNames);
        if (rec == -1){
            return CommonResult.error(CommonResultEm.ERROR,"添加文章对应label失败！");
        }else if(rec == 0){
            return CommonResult.error(CommonResultEm.ERROR,"添加失败，不含新插入内容！");
        }else if (rec == 1){
            return CommonResult.success("插入成功！");
        }else if (rec == 2){
            return CommonResult.error(CommonResultEm.ERROR,"添加失败，请输入正确的标签名！");
        }else if(rec == 3){
            return CommonResult.error(CommonResultEm.ERROR,"添加失败，为找到该标题文章！");
        } else {
            return CommonResult.error(CommonResultEm.ERROR,"插入失败，为未知错误！");
        }
    }

    /**
     * 删除实验艺术的标题
     * 删除文章中多个标签
     *
     * @param message 消息
     * @return {@link CommonResult}
     */
    @RequestMapping("/deleteLabOfArtByTitle")
    public CommonResult deleteLabOfArtByTitle(@RequestBody Map<String,ArrayList<String>> message) {
        if (!message.containsKey("title") || !message.containsKey("labelNames")){
            return CommonResult.error(CommonResultEm.ERROR,"请输入标题title、标签名labelNames");
        }
        String title = message.get("title").get(0);
        ArrayList<String> lebelNames = message.get("labelNames");
        if (title.length() == 0 || lebelNames.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入正确的标题title、标签名labelNames");
        }
        for (String labelName : lebelNames){
            if (title.length() == 0 || labelName.length() == 0){
                return CommonResult.error(CommonResultEm.ERROR,"删除失败，请输入文章标题和标签名！");
            }
            int rec = labelService.deleteLabOfArtByTitle(title, labelName);
            if (rec != 1){
                return CommonResult.error(CommonResultEm.ERROR,"删除失败！");
            }
        }
        return CommonResult.success("删除成功！");
    }

    /**
     * 获取文章中所有标签名
     *
     * @param title 标题
     * @return {@link CommonResult}
     */
    @RequestMapping("/selectByArticleTitle")
    public CommonResult selectByArticleTitle(@RequestParam String title){
        Set<String> labelNames = labelService.selectByArticleTitle(title);

        if (labelNames != null){
            return CommonResult.success(labelNames);
        }else {
            return CommonResult.error(CommonResultEm.ERROR,"未找到符合条件标签文章！");
        }
    }

    /**
     * 获取标签使用的所以文章
     *
     * @param label 标签
     * @return {@link CommonResult}
     */
    @RequestMapping("/selectArtsOfLab")
    public CommonResult selectArtsOfLab(@RequestBody Label label){
        if (label.getLabelName().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请添加标签名");
        }
        List<ArticleInfo> articleInfos = labelService.selectArtsOfLab(label.getLabelName());
        if (articleInfos != null){
            return CommonResult.success(articleInfos);
        }else {
            return CommonResult.error(CommonResultEm.ERROR,"未找到改文章标签信息！");
        }
    }

    @RequestMapping("/getAllLabels")
    public CommonResult getAllLabels(){
        List<String> labelsList = new ArrayList<>();
        Map<String,String> condition = new HashMap<>();
        //查询全部
        condition.put("all","yes");
        List<LabelsInfo> list = labelService.getLabelsByCondition(condition);
        if (list.size() == 0 || list == null){
            return CommonResult.error(CommonResultEm.ERROR,"不存在标签，请创建新标签");
        }
        for (LabelsInfo label : list){
            labelsList.add(label.getLabelName());
        }
        return CommonResult.success(labelsList);
    }
}
