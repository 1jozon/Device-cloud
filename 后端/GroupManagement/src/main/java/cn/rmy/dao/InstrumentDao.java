package cn.rmy.dao;

import cn.rmy.common.beans.Instrument;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InstrumentDao extends BaseMapper<Instrument> {

}
