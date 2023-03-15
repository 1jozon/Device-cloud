package cn.rmy.dao;

import cn.rmy.domain.ReagentMainCurve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReagentMainCurveDao extends BaseMapper<ReagentMainCurve> {
    Integer insertBatchSomeColumn(List<ReagentMainCurve> reagentMainCurves);

    Integer updateBatchSomeColumn(List<ReagentMainCurve> reagentMainCurves);
}
