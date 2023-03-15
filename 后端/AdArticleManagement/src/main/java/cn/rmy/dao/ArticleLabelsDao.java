package cn.rmy.dao;

import cn.rmy.common.beans.articleGps.ArticleLabelsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章标签
 *
 * @author chu
 * @date 2021/11/12
 */
@Mapper
public interface ArticleLabelsDao extends BaseMapper<ArticleLabelsInfo> {
}
