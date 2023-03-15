package cn.rmy.dao;

import cn.rmy.common.pojo.dto.CaliMsgDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表格
 *
 * @author chu
 * @date 2021/12/20
 */
@Mapper
public interface FormDao extends BaseMapper<CaliMsgDTO> {

}
