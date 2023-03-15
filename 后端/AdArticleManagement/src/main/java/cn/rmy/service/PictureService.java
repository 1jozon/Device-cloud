package cn.rmy.service;

import cn.rmy.common.beans.articleGps.PicturesInfo;
import cn.rmy.common.pojo.dto.Article;
import com.aliyun.oss.model.OSSObjectSummary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 图片服务
 *
 * @author chu
 * @date 2021/11/11
 */
public interface PictureService {

    /**
     * 添加图片到文章
     *
     * @param title 标题
     * @param files 文件
     * @return int
     */
    int insertPicToArt(String title, MultipartFile[] files);

    /**
     * 刪除文章中的轮播图
     *
     * @param title 标题
     * @return int
     */
    int deletePicOfArtByTitle(String title);

    /**
     * 获取文章所有图片
     *
     * @param title 标题
     * @return {@link List}<{@link PicturesInfo}>
     */
    List<PicturesInfo> selectByArticleTitle(String title);

    /**
     * 获取所有文章类型照片-详细信息
     *
     * @param type 类型
     * @return {@link List}<{@link Article}>
     */
    List<Article> getAllPicture(int type);

    /**
     * 删除图片
     *
     * @param id@return int
     * @return int
     */
    int deletePic(Integer id);

    /**
     * 通过图片名获取图片信息
     *
     * @param pictureName 照片的名字
     * @return {@link PicturesInfo}
     */
    PicturesInfo getByPicName(String pictureName);

    /**
     * 通过图片id获取图片信息
     *
     * @param pictureId 照片的身份证
     * @return {@link PicturesInfo}
     */
    PicturesInfo getByPicId(Integer pictureId);

    /**
     * 上传图片文件
     *
     * @param file 文件
     * @param type 类型
     * @return {@link String}
     */
    String uploadPictureFile(MultipartFile file, Integer type);

    /**
     * 删除oss文件
     *
     * @param url url
     * @return boolean
     */
    boolean deleteFile(String url);

    /**
     * 得到oss图片文件
     *
     * @param type 类型
     * @return {@link List}<{@link OSSObjectSummary}>
     */
    List<OSSObjectSummary> getOssPicFile(Integer type);
}
