package cn.rmy.dao;

import cn.rmy.common.beans.InstrumentModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InstrumentModelDao extends BaseMapper<InstrumentModel> {

    @Select("SELECT instrument_models.`instrument_model` FROM instrument_models WHERE instrument_models.deleted=0")
    List<String> getAllModels();

}
