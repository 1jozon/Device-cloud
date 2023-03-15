package cn.rmy.dao;

import cn.rmy.common.beans.faultManagement.FaultCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaultCodeDao extends BaseMapper<FaultCode> {
    Integer insertBatchSomeColumn(List<FaultCode> faultCodes);

    Integer updateBatchSomeColumn(List<FaultCode> faultCodes);
}
