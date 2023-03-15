package cn.rmy.service;

import cn.rmy.common.beans.ProjectSumUsedVO;
import cn.rmy.common.beans.analysis.ProjectInfo;


import java.util.List;

/**
 * 项目服务
 *
 * @author chu
 * @date 2022/02/21
 */
public interface ProjectVOService {


    /**
     * 获取仪器的项目使用量
     *
     * @param insId ins id
     *
     */
    List<ProjectSumUsedVO> getInsProjectUsed(String insId);



}
