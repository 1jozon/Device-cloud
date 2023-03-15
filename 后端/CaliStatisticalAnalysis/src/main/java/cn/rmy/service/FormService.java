package cn.rmy.service;

import cn.rmy.common.pojo.dto.CaliConditionDto;
import cn.rmy.common.pojo.dto.SelectResult;

import java.util.List;

/**
 * 定标列表
 *
 * @author chu
 * @date 2021/12/20
 */
public interface FormService {

    /**
     * 被条件卡利列表
     * 分页条件查询定标列表
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @param authorityInsList
     * @return {@link SelectResult}
     */
    SelectResult getCaliListByCondition(int current, int size, CaliConditionDto condition, List<String> authorityInsList);

}
