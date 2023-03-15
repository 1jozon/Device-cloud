package cn.rmy.dao;

import cn.rmy.domain.ReagentSurplusInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReagentSurplusInfoDao extends BaseMapper<ReagentSurplusInfo> {
    Integer insertBatchSomeColumn(List<ReagentSurplusInfo> reagentSurplusInfos);

    Integer updateBatchSomeColumn(List<ReagentSurplusInfo> reagentSurplusInfos);
}
