package cn.rmy.dao;

import cn.rmy.common.beans.faultManagement.FaultHandle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaultHandleDao extends BaseMapper<FaultHandle> {
    Integer insertBatchSomeColumn(List<FaultHandle> faultHandles);

    Integer updateBatchSomeColumn(List<FaultHandle> faultHandles);
}
