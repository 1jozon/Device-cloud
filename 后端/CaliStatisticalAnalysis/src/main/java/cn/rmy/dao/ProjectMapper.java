package cn.rmy.dao;

import cn.rmy.common.pojo.dto.ProjectSumUsedDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {

    List<ProjectSumUsedDto> getProjectSumUsed(@Param("insId") String insId);
}
