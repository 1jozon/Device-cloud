package cn.rmy.service;

import cn.rmy.common.beans.analysis.ProjectInfo;
import cn.rmy.common.pojo.dto.ProjectSumUsedDto;

import java.util.List;

/**
 * 项目服务
 *
 * @author chu
 * @date 2022/02/21
 */
public interface ProjectService {
    /**
     * 插入项目信息
     *
     * @param projectInfo 项目信息
     * @return int
     */
    int insertProject(ProjectInfo projectInfo);

    /**
     * 项目存在判断
     *
     * @param projectId 项目id
     * @return int
     */
    ProjectInfo getProjectExist(String projectId);

    /**
     * 得到所有项目信息
     *
     * @return {@link List}<{@link ProjectInfo}>
     */
    List<ProjectInfo> getAllPro();


    /**
     * 获取仪器的项目使用量
     *
     * @param insId ins id
     * @return {@link List}<{@link ProjectSumUsedDto}>
     */
    List<ProjectSumUsedDto> getInsProjectUsed(String insId);



}
