package cn.rmy.dao;

import cn.rmy.common.beans.articleGps.PicturesInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图片信息
 *
 * @author chu
 * @date 2022/05/30
 */
@Mapper
public interface PictureInfoDao extends BaseMapper<PicturesInfo> {
}
