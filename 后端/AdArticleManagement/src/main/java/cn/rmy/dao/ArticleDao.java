package cn.rmy.dao;

import cn.rmy.common.beans.articleGps.ArticleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章
 *
 * @author chu
 * @date 2021/11/12
 */
@Mapper
public interface ArticleDao extends BaseMapper<ArticleInfo> {

}
