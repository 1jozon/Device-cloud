package cn.rmy.dao;

import cn.rmy.common.beans.faultManagement.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogDao extends BaseMapper<Log> {
//    Integer insertBatchSomeColumn(List<Log> logs);
//
//    Integer updateBatchSomeColumn(List<Log> logs);
}
