package cn.rmy.dao.dtoDao;

import cn.rmy.common.beans.ProjectSumUsedVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectVODao extends BaseMapper<ProjectSumUsedVO> {

    @Select("select b.project_name as projectName, sum(a.reagent_use_num) as usedNumber " +
            "from tb_reagentsurpluscount as a, tb_project as b " +
            "where a.device_id = #{insId} " +
            "and a.reagent_num = b.project_id " +
            "group by a.reagent_num " +
            "order by usedNumber DESC")
    List<ProjectSumUsedVO> getProjectSumUsed(@Param("insId") String insId);
}
