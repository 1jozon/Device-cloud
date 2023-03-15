package cn.rmy.service.Imp;

import cn.rmy.common.pojo.dto.CaliAnaDateRes;
import cn.rmy.common.pojo.dto.CaliAnalysisDataDto;
import cn.rmy.common.pojo.dto.CaliConditionDto;
import cn.rmy.dao.CaliAnaMapper;
import cn.rmy.dao.ChartDao;
import cn.rmy.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 图服务小鬼
 *
 * @author chu
 * @date 2021/11/19
 */
@Service
public class ChartServiceImp implements ChartService {

    @Autowired
    private ChartDao chartDao;

    @Autowired
    private CaliAnaMapper caliAnaMapper;

    /**
     * 得到分析
     * 定标分析
     *
     * @param condition        条件
     * @param authorityInsList 权威ins列表
     * @return {@link CaliAnaDateRes}
     */
    @Override
    public CaliAnaDateRes getAnalysis(CaliConditionDto condition, List<String> authorityInsList) {
        String insId = condition.getInsId();
        String projectId = condition.getProjectId();
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();
        List<CaliAnalysisDataDto> list = new ArrayList<>();

        CaliAnaDateRes caliAnaDateRes = new CaliAnaDateRes();
        int total = 0;

        if (startTime != null && endTime != null && insId != null && projectId != null){
            //指定仪器-指定项目-规定日期
            list = chartDao.getInsProTimeCaliAnalysis(insId, projectId,startTime,endTime);
        }else if (startTime == null && endTime == null && insId != null && projectId != null) {
            //指定仪器-指定项目-不规定日期
            list = chartDao.getInsProCaliAnalysis(insId,projectId);
        }else if (startTime != null && endTime != null && insId == null && projectId == null){
            //全部-规定日期
            list = caliAnaMapper.getAllTimeCaliAnalysis(authorityInsList,startTime,endTime);
        }else if (insId != null && insId.length() != 0 && startTime != null && endTime != null){
            //指定仪器-规定日期
            list = chartDao.getInsTimeCaliAnalysis(insId,startTime,endTime);
        }else if (insId != null && insId.length() != 0){
            //指定仪器-不规定日期
            list = chartDao.getInsCaliAnalysis(insId);
        }else if(projectId != null && projectId.length() != 0 && startTime != null && endTime != null){
            //指定项目-规定日期
            list = caliAnaMapper.getProTimeCaliAnalysis(projectId,authorityInsList,startTime,endTime);
        }else if(projectId != null && projectId.length() != 0){
            list = caliAnaMapper.getProCaliAnalysis(projectId, authorityInsList);
        }else{
            //全部-不规定日期
            list = caliAnaMapper.getAllCaliAnalysis(authorityInsList);
        }
        if(list != null && list.size() > 0){
            for(CaliAnalysisDataDto info : list){
                total += info.getAmount();
            }
            caliAnaDateRes.setTotal(total);
            caliAnaDateRes.setCaliAnalysisDataDtos(list.subList(0, (list.size() > 9 ? 9 : list.size())));
            return caliAnaDateRes;
        }
        return caliAnaDateRes;
    }
}
