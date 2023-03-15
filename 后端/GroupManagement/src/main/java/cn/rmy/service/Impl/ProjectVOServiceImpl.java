package cn.rmy.service.Impl;

import cn.rmy.common.beans.ProjectSumUsedVO;
import cn.rmy.common.beans.analysis.ProjectInfo;

import cn.rmy.dao.dtoDao.ProjectVODao;
import cn.rmy.service.ProjectVOService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目服务的实现
 *
 * @author chu
 * @date 2022/02/21
 */
@Service
public class ProjectVOServiceImpl implements ProjectVOService {



    @Autowired
    ProjectVODao projectVODao;


    /**
     * 获取仪器的项目使用量
     *
     *
     */
    @Override
    public List<ProjectSumUsedVO> getInsProjectUsed(String insId) {
        List<ProjectSumUsedVO> usedSumList = new ArrayList<>();
        usedSumList = projectVODao.getProjectSumUsed(insId);

        if(usedSumList == null ||  usedSumList.size() == 0){
            return null;
        }
        return usedSumList;

    }
}
