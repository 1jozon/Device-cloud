package cn.rmy.service.imp;

import cn.rmy.common.pojo.dto.Article;
import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.beans.articleGps.ArticlePicturesInfo;
import cn.rmy.common.beans.articleGps.PicturesInfo;
import cn.rmy.dao.ArticleDao;
import cn.rmy.dao.ArticlePicturesDao;
import cn.rmy.dao.PictureDao;
import cn.rmy.fileUtils.OssFileUtils;
import cn.rmy.service.PictureService;
import com.aliyun.oss.model.OSSObjectSummary;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片服务实现
 *
 * @author chu
 * @date 2021/11/09
 */
@Service
@Transactional
public class PictureServiceImp implements PictureService {

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleServiceImp articleService;

    @Autowired
    private ArticlePicturesDao articlePicturesDao;

    private OssFileUtils ossFileUtils;
    @Autowired
    public void getFileUtils(OssFileUtils ossFileUtils){
        this.ossFileUtils = ossFileUtils;
    }

    /**
     * 文章中插入图片
     *
     * @param title 标题
     * @param files 文件
     * @return int
     */
    @Override
    public int insertPicToArt(String title, MultipartFile[] files) {
        //图片文件上传
        if (files != null){
            for (MultipartFile file : files){
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)){

                    //查询是否图片重名,不需要检测，id不同
                    /*PicturesInfo picture = getByPicName(fileName);
                    if (picture != null){
                        return 7;
                    }*/

                    //获取后缀名
                    String suffix = ossFileUtils.getExtension(file);
                    if (!(suffix.equalsIgnoreCase(".png")
                            || suffix.equalsIgnoreCase(".jpg")
                            || suffix.equalsIgnoreCase(".jpeg"))){
                        //文件上传失败，文件格式错误
                        return 2;
                    }
                    //文件保存到oss
                    try{
                        String url = ossFileUtils.uploadFile(file,2,0,null);
                        //在图片表中插入
                        PicturesInfo picturesInfo = new PicturesInfo();
                        picturesInfo.setPictureName(fileName);
                        picturesInfo.setPictureTitle(title);
                        picturesInfo.setPictureUrl(url);
                        picturesInfo.setPushed(0);
                        int rec = pictureDao.insert(picturesInfo);
                        if (rec != 1){
                            return 3;
                        }
                        //获取插入后id
                        int insertId = picturesInfo.getId();

                        //在文章图片表中插入,将图片id与文章对应起来
                        ArticlePicturesInfo articlePicturesInfo = new ArticlePicturesInfo();
                        articlePicturesInfo.setArticleTitle(title)
                                .setPictureId(insertId);
                        int recArtPic = articlePicturesDao.insert(articlePicturesInfo);
                        if (recArtPic != 1){
                            //文章图片表添加失败
                            return 6;
                        }
                    }catch (Exception ioe){
                        ioe.printStackTrace();
                        //添加到数据库错误
                        return 3;
                    }
                }else{
                    //图片文件为空
                    return 4;
                }
            }
        }else{
            return -1;
        }
        //成功
        return 1;
    }

    /**
     * 刪除文章中的轮播图
     *
     * @param title 标题
     * @return int
     */
    @Override
    public int deletePicOfArtByTitle(String title) {
        QueryWrapper<ArticlePicturesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_title",title);
        List<ArticlePicturesInfo> articlePicturesInfos = articlePicturesDao.selectList(queryWrapper);

        if (articlePicturesInfos.size() == 0){
            return 1;
        }
        //删除所有该标题的对应图片关系
        for (ArticlePicturesInfo articlePicturesInfo : articlePicturesInfos){
            if (!articlePicturesInfo.getArticleTitle().equals(title)){
                break;
            }
            int id = articlePicturesInfo.getId();
            //删除图片库-oss
            int recPic = deletePic(articlePicturesInfo.getPictureId());
            //删除文章-图片关系
            int rec = articlePicturesDao.deleteById(id);

            if (rec != 1 && recPic != 1){
                //删除失败
                return -1;
            }
        }
        //删除成功
        return 1;
    }

    /**
     * 通过文章标题搜索所有图片
     *
     * @param title 标题
     * @return {@link List}<{@link PicturesInfo}>
     */
    @Override
    public List<PicturesInfo> selectByArticleTitle(String title) {

        QueryWrapper<ArticlePicturesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_title", title);
        List<ArticlePicturesInfo> list = articlePicturesDao.selectList(queryWrapper);

        List<PicturesInfo> picturesInfos = new ArrayList<>();
        for (ArticlePicturesInfo articlePicturesInfo : list){
            PicturesInfo picturesInfo = getByPicId(articlePicturesInfo.getPictureId());
            if (picturesInfo == null){
                continue;
            }
            picturesInfos.add(picturesInfo);
        }
        return picturesInfos;
    }

    /**
     * 获取所有文章类型照片
     *
     * @param type 类型 0-文章、1-广告、2-轮播图
     * @return {@link List}<{@link Article}>
     */
    @Override
    public List<Article> getAllPicture(int type) {
        QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type);
        queryWrapper.eq("approved", 2);
        queryWrapper.orderByDesc("update_time");
        List<ArticleInfo> infoList = articleDao.selectList(queryWrapper);
        List<Article> list = new ArrayList<>();
        if (infoList == null || infoList.size() == 0){
            return null;
        }else{
            for (ArticleInfo info : infoList){
                Article article = articleService.getArtByTitle(info.getTitle());
                if (article == null || article.getPicturesInfoUrls() == null || article.getPicturesInfoUrls().size() == 0){
                    continue;
                }
                list.add(article);
            }
        }
        return list;
    }

    /**
     * 删除图片库图片
     *
     * @param id id
     * @return int
     */
    @Override
    public int deletePic(Integer id) {
        QueryWrapper<PicturesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        List<PicturesInfo> list = pictureDao.selectList(queryWrapper);

        if (list.size() == 0){
            //图片不存在或已删除
            return 2;
        }
        //删除图片库 + oss库图片
        int rec = pictureDao.deleteById(list.get(0).getId());
        boolean flag = deleteFile(list.get(0).getPictureUrl());
        if(rec == 1 && flag){
            //删除成功
            return 1;
        }else{
            //删除失败
            return -1;
        }
    }

    /**
     * 通过图片名获取图片信息
     *
     * @param pictureName 图片标题
     * @return {@link PicturesInfo}
     */
    @Override
    public PicturesInfo getByPicName(String pictureName) {
        QueryWrapper<PicturesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("picture_name",pictureName);
        List<PicturesInfo> picturesInfo = pictureDao.selectList(queryWrapper);
        if (picturesInfo.size() == 0){
            return null;
        }
        return picturesInfo.get(0);
    }

    /**
     * 通过图片id获取图片
     *
     * @param pictureId 照片的身份证
     * @return {@link PicturesInfo}
     */
    @Override
    public PicturesInfo getByPicId(Integer pictureId) {
        QueryWrapper<PicturesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",pictureId);
        List<PicturesInfo> picturesInfo = pictureDao.selectList(queryWrapper);
        if (picturesInfo.size() == 0){
            return null;
        }
        return picturesInfo.get(0);
    }

    /**
     * 上传图片文件到oss
     *
     * @param file 文件
     */
    @Override
    public String uploadPictureFile(MultipartFile file, Integer type){

        String url = "";

        if (!file.isEmpty()){
            try{
                url = ossFileUtils.uploadFile(file,type,0,null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            url = null;
        }
        return url;
    }

    /**
     * 删除oss文件
     *
     * @param url url
     * @return boolean
     */
    @Override
    public boolean deleteFile(String url){

        boolean flag;
        if (url.length() != 0){
            flag = ossFileUtils.deleteFile(url);
        }else{
            return false;
        }
        return flag;
    }

    /**
     * 得到oss所有图片文件
     *
     * @param type 类型
     * @return {@link List}<{@link OSSObjectSummary}>
     */
    @Override
    public List<OSSObjectSummary> getOssPicFile(Integer type) {
        List<OSSObjectSummary> list = ossFileUtils.listOssPrefixFile(type);
        return list;
    }
}
