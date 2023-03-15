package cn.rmy.service.imp;

import cn.rmy.common.beans.analysis.ProjectInfo;
import cn.rmy.common.pojo.dto.*;
import cn.rmy.dao.ProjectDao;
import cn.rmy.dao.QualityDao;
import cn.rmy.dao.QualityMapper;
import cn.rmy.service.AnalysisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnalysisServiceImp implements AnalysisService {

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private QualityMapper qualityMapper;

    /**
     * 仪器质控分析
     *
     * @param condition 条件
     * @param authorithIns
     * @return {@link List}<{@link QualityAnalysisInsDto}>
     */
    @Override
    public QualityAnaInsRes qualityInsAnalysis(QualityConditionDto condition, List<String> authorithIns) {
        String insId = condition.getInsId();
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();
        QualityAnaInsRes qualityAnaInsRes = new QualityAnaInsRes();
        int total = 0;
        List<QualityAnalysisInsDto> list = new ArrayList<>();
        //默认全部仪器
        if(startTime == null  && endTime == null && insId == null){
            list = qualityMapper.getAllInsAnalysis(authorithIns);
        }//所有仪器，指定时间段
        else if(startTime != null && endTime != null && insId == null){
            list = qualityMapper.getAllTimeInsAnalysis(authorithIns,startTime, endTime);
        }//指定仪器 指定时间段
        else if (insId != null && startTime != null && endTime != null ){
            list = qualityMapper.getTimeInsAnalysis(insId, startTime, endTime);
        }//指定仪器
        else if (insId != null && startTime == null && endTime == null){
            list = qualityMapper.getInsAnalysis(insId);
        }
        if(list != null && list.size() > 0){
            for(QualityAnalysisInsDto info : list){
                total += info.getProCount();
            }
            //取前8条
            qualityAnaInsRes.setQualityAnalysisInsDtos(list.subList(0, list.size() > 9 ? 9 : list.size()));
            qualityAnaInsRes.setTotal(total);
            return qualityAnaInsRes;
        }
        return qualityAnaInsRes;
    }

    /**
     * 项目质控分析
     *
     * @param condition 条件
     * @return {@link List}<{@link QualityAnalysisProjectDto}>
     */
    @Override
    public QualityAnaProRes qualityProAnalysis(QualityConditionDto condition, List<String> authorithIns) {
        String projectName = condition.getProjectName();
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();
        String insId = condition.getInsId();

        QualityAnaProRes qualityAnaProRes = new QualityAnaProRes();
        int total = 0;
        List<QualityAnalysisProjectDto> list = new ArrayList<>();
        if(insId != null && projectName != null && startTime != null && endTime != null){
            list = qualityMapper.getProInsTimeAnalysis(projectName, insId, startTime, endTime);
        }else if(insId != null && projectName != null){
            list = qualityMapper.getProInsAnalysis(projectName, insId);
        }else if(insId == null && startTime != null && endTime != null){
            list = qualityMapper.getProTimeAnalysis(projectName, authorithIns,startTime,endTime);
        }else if (insId == null && projectName != null && (startTime == null || endTime == null)){
            list = qualityMapper.getProAnalysis(projectName, authorithIns);
        }
        if(list != null && list.size() > 0){
            for(QualityAnalysisProjectDto info : list){
                total += info.getInsModelCount();
            }
            //取前8条
            qualityAnaProRes.setQualityAnalysisProjectDtos(list.subList(0, list.size() > 9 ? 9 : list.size()));
            qualityAnaProRes.setTotal(total);
            return qualityAnaProRes;
        }
        return qualityAnaProRes;
    }

    /**
     * 把所有项目名称
     * 所有项目名称
     *
     * @param projectName 项目名称
     * @return {@link List}<{@link ProjectDataDto}>
     */
    @Override
    public List<ProjectDataDto> getAllProjectNames(String projectName) {
        List<ProjectDataDto> projectDataDtoList = new ArrayList<>();
        QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
        if(projectName == null || projectName.length() == 0){
            queryWrapper.groupBy("project_id");

        }else{
            queryWrapper.like("project_name", projectName);
        }
        queryWrapper.last("limit 8");
        List<ProjectInfo> infoList = projectDao.selectList(queryWrapper);

        if(infoList.size() > 0){
            for(ProjectInfo info :infoList){
                ProjectDataDto projectDataDto = new ProjectDataDto();
                projectDataDto.setProjectId(info.getProjectId());
                projectDataDto.setProjectName(info.getProjectName());
                projectDataDtoList.add(projectDataDto);
            }
        }else{
            return null;
        }
        return projectDataDtoList;
    }
}
