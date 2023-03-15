package cn.rmy.dao;

import cn.rmy.common.beans.analysis.CaliDataInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CaliDataDao extends BaseMapper<CaliDataInfo> {
}
