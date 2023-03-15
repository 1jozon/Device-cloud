package cn.rmy.service.Imp;

import cn.rmy.common.beans.analysis.ProjectInfo;
import cn.rmy.common.pojo.dto.ProjectSumUsedDto;
import cn.rmy.dao.ProjectDao;
import cn.rmy.dao.ProjectMapper;
import cn.rmy.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 项目服务的实现
 *
 * @author chu
 * @date 2022/02/21
 */
@Service
public class ProjectServiceImp implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 插入项目信息
     *
     * @param projectInfo 项目信息
     * @return int
     */
    @Override
    public int insertProject(ProjectInfo projectInfo){
        if (projectInfo != null && projectInfo.getProjectId() != null && projectInfo.getProjectId().length() > 0){
            ProjectInfo oldProjectInfo = getProjectExist(projectInfo.getProjectId());
            try{
                if(oldProjectInfo == null){
                    //不同-新添加
                    projectDao.insert(projectInfo);
                }else if(oldProjectInfo.getProjectId().equals(projectInfo.getProjectId())
                        && ((!oldProjectInfo.getProjectAbbr().equals(projectInfo.getProjectAbbr()))
                        || (!oldProjectInfo.getProjectName().equals(projectInfo.getProjectName())))){
                    //相同-更新数据
                    oldProjectInfo.setProjectName(projectInfo.getProjectName());
                    oldProjectInfo.setProjectAbbr(projectInfo.getProjectAbbr());
                    projectDao.updateById(oldProjectInfo);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return 1;
        }
        //插入失败
        return 0;
    }

    /**
     * 项目存在判断
     *
     * @param projectId 项目id
     * @return int
     */
    @Override
    public ProjectInfo getProjectExist(String projectId) {
        QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        ProjectInfo info = projectDao.selectOne(queryWrapper);

        return info;
    }

    /**
     * 得到所有项目信息
     *
     * @return {@link List}<{@link ProjectInfo}>
     */
    @Override
    public List<ProjectInfo> getAllPro() {
        QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project_id");
        List<ProjectInfo> list = projectDao.selectList(queryWrapper);

        if (list == null || list.size() == 0){
            return null;
        }
        return list;
    }

    /**
     * 获取仪器的项目使用量
     *
     * @param insId ins id
     * @return {@link List}<{@link ProjectSumUsedDto}>
     */
    @Override
    public List<ProjectSumUsedDto> getInsProjectUsed(String insId) {
        List<ProjectSumUsedDto> usedSumList = new ArrayList<>();
        usedSumList = projectMapper.getProjectSumUsed(insId);

        if(usedSumList == null ||  usedSumList.size() == 0){
            return null;
        }
        return usedSumList;

    }
}
