package cn.rmy.service.imp;

import cn.rmy.common.dto.ConAnalysisDto;
import cn.rmy.common.dto.ConAnalysisResDto;
import cn.rmy.common.dto.ConsumablesConditionDto;
import cn.rmy.dao.ConsumablesDao;
import cn.rmy.dao.ConsumablesMapper;
import cn.rmy.service.ConAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConAnalysisServiceImp implements ConAnalysisService {

    @Autowired
    private ConsumablesDao consumablesDao;

    @Autowired
    private ConsumablesMapper consumablesMapper;

    /**
     * 得到仪器耗材分析
     *
     * @param condition 条件
     * @return {@link List}<{@link ConAnalysisDto}>
     */
    @Override
    public ConAnalysisResDto getInsAnalysis(ConsumablesConditionDto condition, List<String> authorityInsList) {
        int all = condition.getAll();
        int solidState = condition.getSolidState();
        String insId = condition.getInsId();
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();

        ConAnalysisResDto conAnalysisRes = new ConAnalysisResDto();
        int total = 0;
        List<ConAnalysisDto> list = new ArrayList<>();

        if(insId != null && insId.length() > 0 && startTime != null && endTime != null){
            //指定仪器，规定时间
            list = consumablesDao.conGetInsTimeAnalysis(insId,solidState,startTime, endTime);
        }else if(insId != null && insId.length() > 0 && (startTime == null || endTime == null)){
            //指定仪器，不规定时间
            list = consumablesDao.conGetInsAnalysis(solidState,insId);
        }else if (startTime != null && endTime != null){
            //全部仪器，规定时间
            list = consumablesMapper.conGetAllTimeAnalysis(solidState,authorityInsList,startTime, endTime);
        }else{
            //全部仪器，不规定时间
            list = consumablesMapper.conGetAllAnalysis(solidState,authorityInsList);
        }

        /*if(insId != null && startTime != null && endTime != null){
            //全部、规定时间
            list = consumablesMapper.conGetAllTimeAnalysis(solidState,authorityInsList,startTime, endTime);
        }else if(insId != null && insId.length() != 0 && startTime == null && endTime == null){
            //指定仪器，不限时间
            list = consumablesDao.conGetInsAnalysis(solidState,insId);
        }else if(insId != null && insId.length() != 0 && startTime != null && endTime != null){
            //指定仪器，规定时间
            list = consumablesDao.conGetInsTimeAnalysis(insId,solidState,startTime, endTime);
        }else {
            //全部、不规定时间
            list = consumablesMapper.conGetAllAnalysis(solidState,authorityInsList);
        }*/

        if(list != null && list.size() > 0){
            for(ConAnalysisDto info : list){
                total += info.getAmount();
            }
            //取前8个
            conAnalysisRes.setConAnalysisList(list.subList(0, (list.size() > 9 ? 9 : list.size())));
            conAnalysisRes.setTotal(total);
        }

        return conAnalysisRes;
    }

    /**
     * 得到耗材名称分析
     *
     * @param condition 条件
     * @return {@link List}<{@link ConAnalysisDto}>
     */
    @Override
    public ConAnalysisResDto getConAnalysis(ConsumablesConditionDto condition, List<String> authorityInsList) {

        String conName = condition.getConsumName();
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();

        ConAnalysisResDto conAnalysisRes = new ConAnalysisResDto();
        int total = 0;
        List<ConAnalysisDto> list = new ArrayList<>();
        if (conName != null && conName.length() != 0 && startTime != null && endTime != null){
            //指定耗材名称-规定时间
            list = consumablesMapper.conGetConTimeAnalysis(conName, authorityInsList,startTime,endTime);
        }else{
            list = consumablesMapper.conGetConAnalysis(conName,authorityInsList);
        }
        if(list != null && list.size() > 0){
            for(ConAnalysisDto info : list){
                total += info.getAmount();
            }
            //取前8个
            conAnalysisRes.setConAnalysisList(list.subList(0, (list.size() > 9 ? 9 : list.size())));
            conAnalysisRes.setTotal(total);
        }
        return conAnalysisRes;
    }
}
