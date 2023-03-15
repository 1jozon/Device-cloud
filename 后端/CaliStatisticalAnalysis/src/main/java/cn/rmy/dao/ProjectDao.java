package cn.rmy.dao;

import cn.rmy.common.beans.analysis.ProjectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目
 *
 * @author chu
 * @date 2022/02/21
 */
@Mapper
public interface ProjectDao extends BaseMapper<ProjectInfo> {
}
