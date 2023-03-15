package cn.rmy.dao;

import cn.rmy.common.beans.analysis.ProjectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectDao extends BaseMapper<ProjectInfo> {
}
