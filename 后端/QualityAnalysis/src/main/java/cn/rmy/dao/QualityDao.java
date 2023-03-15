package cn.rmy.dao;

import cn.rmy.common.beans.analysis.QualityDataInfo;
import cn.rmy.common.pojo.dto.QualityAnalysisInsDto;
import cn.rmy.common.pojo.dto.QualityAnalysisProjectDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 质量分析
 *
 * @author chu
 * @date 2021/12/21
 */
@Mapper
public interface QualityDao extends BaseMapper<QualityDataInfo> {

}
