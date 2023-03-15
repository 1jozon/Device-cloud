package cn.rmy.controller;

import cn.rmy.common.beans.articleGps.PicturesInfo;
import cn.rmy.common.pojo.dto.Article;
import cn.rmy.common.pojo.dto.Picture;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.dao.PictureDao;
import cn.rmy.fileUtils.OssFileUtils;
import cn.rmy.service.imp.PictureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 图控制器
 *
 * @author chu
 * @date 2021/11/11
 */
@RestController
@RequestMapping("rmy/rotationPic")
public class PictureController {

    @Autowired
    private PictureServiceImp pictureService;

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private OssFileUtils ossFileUtils;

    /**
     * 创建轮播图
     *
     * @param title 标题
     * @param files 文件
     * @return {@link CommonResult}
     */
    @RequestMapping("/createRotationPic")
    public CommonResult createRotationPic(String title, MultipartFile[] files){

        if (title == null ){
            return CommonResult.error(CommonResultEm.ERROR,"轮播图标题不能为空！");
        }else if (files == null){
            return CommonResult.error(CommonResultEm.ERROR,"轮播图图片文件不能为空");
        }

        int rec = pictureService.insertPicToArt(title,files);
        switch (rec){
            case -1: return CommonResult.error(CommonResultEm.ERROR,"文件上传失败！");

            case 1:  return CommonResult.success("上传成功");

            case 2:  return CommonResult.error(CommonResultEm.ERROR,"文件上传失败，文件格式错误！");

            case 3:  return CommonResult.error(CommonResultEm.ERROR,"文件上传失败，添加到数据库出错！");

            case 4:  return CommonResult.error(CommonResultEm.ERROR,"文件上传失败，文件为空！");

            case 7: return CommonResult.error(CommonResultEm.ERROR,"文件上传失败，文件名重复，请更改文件名！");

            default: return CommonResult.error(CommonResultEm.ERROR,"文件上传失败，出现未知错误!");
        }
    }

    /**
     * 审核发布轮播图
     *
     * @param picture 图片
     * @return {@link CommonResult}
     */
    @RequestMapping("/pubRotationPic")
    public CommonResult pubRotationPic(@RequestBody Picture picture){
        int picId = picture.getPictureId();
        int status = picture.getPushed();

        PicturesInfo pic = pictureService.getByPicId(picId);
        if (pic == null){
            return CommonResult.error(CommonResultEm.ERROR,"图片不存在！");
        }
        if (pic.getPushed() == 2){
            return CommonResult.success("该图已审批，且拒绝发布");
        }else {
            pic.setPushed(status);
            int rec = pictureDao.updateById(pic);
            if (rec == 1 && status == 1){
                return CommonResult.success("审批并发布成功");
            }else if (rec == 1 && status == 2){
                return CommonResult.success("审批并拒绝发布");
            }else {
                return CommonResult.error(CommonResultEm.ERROR,"操作失败");
            }
        }
    }

    /**
     * 删除轮播图
     *
     * @param picturesInfo 图片信息
     * @return {@link CommonResult}
     */
    @RequestMapping("/delRotationPic")
    public CommonResult delRotationPic(@RequestBody PicturesInfo picturesInfo){

        if (picturesInfo == null || picturesInfo.getId() == null){
            return CommonResult.error(CommonResultEm.ERROR,"文件id信息为空，请选择删除图片！");
        }

        int rec = pictureService.deletePic(picturesInfo.getId());
        if (rec == 1) {
            return CommonResult.success("删除成功！");
        }else if(rec == 2){
            return CommonResult.error(CommonResultEm.ERROR,"图片不存在或已删除！");
        }else {
            return CommonResult.error(CommonResultEm.ERROR,"删除失败！");
        }
    }

    /**
     * 得到所有轮播图片链接
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getAllRotation")
    public CommonResult getAllRotationPic(){
        List<Article> rotationList = new ArrayList<>();
        rotationList = pictureService.getAllPicture(2);

        if (rotationList == null || rotationList.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"不存在轮播图信息");
        }
        return CommonResult.success(rotationList);
    }

    /**
     * 修改轮播图图片
     *
     * @param newFile 新文件
     * @return {@link CommonResult}
     */
    @RequestMapping("/modRotationPic")
    public CommonResult modifyRotationPic(String title, String oldUrl, MultipartFile newFile){
        if (title.length() == 0 || newFile == null){
            return CommonResult.error(CommonResultEm.ERROR,"请输入正确信息");
        }
        List<PicturesInfo> listPicInfos = pictureService.selectByArticleTitle(title);
        //新添加图片
        if (listPicInfos == null || listPicInfos.size() == 0){
            //return CommonResult.error(CommonResultEm.ERROR,"未找到符合条件标题的轮播图信息");
            MultipartFile[] files = new MultipartFile[1];
            files[0] = newFile;
            try {
                pictureService.insertPicToArt(title, files);
            }catch (Exception e){
                e.getCause();
            }
            return CommonResult.success("修改成功");

        }
        //更新老图片
        for (PicturesInfo picturesInfo : listPicInfos){
            if (picturesInfo.getPictureUrl().equals(oldUrl)){
                try{
                    String url = ossFileUtils.uploadFile(newFile, 2, 0,null);
                    picturesInfo.setPictureUrl(url);
                    pictureDao.updateById(picturesInfo);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return CommonResult.success("修改成功");
    }
}
