package cn.rmy.service.imp;

import cn.rmy.common.dto.SampleAnalysisDto;
import cn.rmy.common.dto.SampleAnalysisResDto;
import cn.rmy.common.dto.SampleConditionDto;
import cn.rmy.dao.SampleMapper;
import cn.rmy.service.SampleAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class SampleAnalysisServiceImp implements SampleAnalysisService {

    @Autowired
    private SampleMapper sampleMapper;


    /**
     * 仪器分类
     *
     * @param condition       条件
     * @param authorityInsIds 权威ins id
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    @Override
    public SampleAnalysisResDto getSampleInsAnalysis(SampleConditionDto condition, List<String> authorityInsIds) {

        SampleAnalysisResDto sampleAnalysisRes = new SampleAnalysisResDto();
        Integer totalSize = 0;

        List<SampleAnalysisDto> sampleAnalysis = new ArrayList<>();
        //authorityInsIds.add("B280012200170");
        if(condition.getStartTime() != null && condition.getEndTime() != null){
            //获取全部-有时间限制
            sampleAnalysis = sampleMapper.getSampleAnaInsTime(authorityInsIds, condition.getStartTime(), condition.getEndTime());
        }else{
            //获取全部-无时间限制
            sampleAnalysis = sampleMapper.getSampleAnaInsAll(authorityInsIds);
        }
        if(sampleAnalysis != null && sampleAnalysis.size() > 0){
            for(SampleAnalysisDto info : sampleAnalysis){
                totalSize += info.getNumber();
            }
            //取前8条
            sampleAnalysisRes.setSampleAnalysisList(sampleAnalysis.subList(0,(sampleAnalysis.size() >= 8 ? 8 : sampleAnalysis.size())));
            sampleAnalysisRes.setTotal(totalSize);
        }
        return sampleAnalysisRes;
    }

    /**
     * 项目分类
     *
     * @param condition       条件
     * @param authorityInsIds 权威ins id
     * @return {@link SampleAnalysisResDto}
     */
    @Override
    public SampleAnalysisResDto getSampleProAnalysis(SampleConditionDto condition, List<String> authorityInsIds) {
        SampleAnalysisResDto sampleAnalysisRes = new SampleAnalysisResDto();
        Integer totalSize = 0;
        List<SampleAnalysisDto> sampleAnalysis = new ArrayList<>();
        //四种情况
        if(condition.getInsId() != null && condition.getStartTime() != null && condition.getEndTime() != null) {
            //指定仪器-指定时间-项目
            sampleAnalysis = sampleMapper.getSampleAnaProSinInsTime(condition.getInsId(), condition.getStartTime(), condition.getEndTime());
        }else if(condition.getInsId() != null && (condition.getStartTime() == null || condition.getEndTime() == null)) {
            //指定仪器-所有时间-项目
            sampleAnalysis = sampleMapper.getSampleAnaProSinIns(condition.getInsId());
        }else if(condition.getInsId() == null && condition.getStartTime() != null && condition.getEndTime() != null){
            //所有仪器-指定时间-项目
            sampleAnalysis = sampleMapper.getSampleAnaProAllInsTime(authorityInsIds,condition.getStartTime(), condition.getEndTime());
        }else{
            //所有仪器-所有时间-项目
            sampleAnalysis = sampleMapper.getSampleAnaProAllIns(authorityInsIds);
        }

        if(sampleAnalysis != null && sampleAnalysis.size() > 0){
            for(SampleAnalysisDto info : sampleAnalysis){
                totalSize += info.getNumber();
            }
            //sampleAnalysisRes.setSampleAnalysisList(sampleAnalysis);
            sampleAnalysisRes.setSampleAnalysisList(sampleAnalysis.subList(0,(sampleAnalysis.size() >= 8 ? 8 : sampleAnalysis.size())));
            sampleAnalysisRes.setTotal(totalSize);
        }

        return sampleAnalysisRes;
    }

    /**
     * 得到样本年龄分析
     *
     * @param condition       条件
     * @param authorityInsIds 权威ins id
     * @return {@link SampleAnalysisResDto}
     */
    @Override
    public SampleAnalysisResDto getSampleAgeAnalysis(SampleConditionDto condition, List<String> authorityInsIds) {
        SampleAnalysisResDto sampleAnalysisRes = new SampleAnalysisResDto();
        Integer total = 0;
        List<SampleAnalysisDto> sampleAnalysisList = new ArrayList<>();
        if(condition.getInsId() != null){
            //指定仪器
            if(condition.getException() == 0){
                sampleAnalysisList = sampleMapper.getSampleAnaAgeSinIns(condition.getProjectId(),
                        condition.getInsId(),condition.getStartTime(),condition.getEndTime());
            }else{
                sampleAnalysisList = sampleMapper.getSampleAnaAgeSinInsExp(condition.getProjectId(),
                        condition.getInsId(), condition.getStartTime(),condition.getEndTime());
            }
        }else{
            //所有仪器
            if(condition.getException() == 0){
                sampleAnalysisList = sampleMapper.getSampleAnaAgeAllIns(condition.getProjectId(),
                        authorityInsIds, condition.getStartTime(),condition.getEndTime());
            }else{
                sampleAnalysisList = sampleMapper.getSampleAnaAgeAllInsExp(condition.getProjectId(),
                        authorityInsIds, condition.getStartTime(),condition.getEndTime());
            }

        }

        //初始化所有年龄段
        if(sampleAnalysisList == null || sampleAnalysisList.size() == 0){
            sampleAnalysisRes.setTotal(0);
            return sampleAnalysisRes;
        }
        if(sampleAnalysisList.size() < 7){
            HashMap<String, Boolean> exitAgeGroup = new HashMap<>();
            for (SampleAnalysisDto info : sampleAnalysisList){
                exitAgeGroup.put(info.getObject(), true);
            }
            if(!exitAgeGroup.containsKey("婴儿")){
                sampleAnalysisList.add(new SampleAnalysisDto("婴儿",0,0));
            }
            if(!exitAgeGroup.containsKey("少年")){
                sampleAnalysisList.add(new SampleAnalysisDto("少年",0,0));
            }
            if(!exitAgeGroup.containsKey("青少年")){
                sampleAnalysisList.add(new SampleAnalysisDto("青少年",0,0));
            }
            if(!exitAgeGroup.containsKey("青年")){
                sampleAnalysisList.add(new SampleAnalysisDto("青年",0,0));
            }
            if(!exitAgeGroup.containsKey("中年")){
                sampleAnalysisList.add(new SampleAnalysisDto("中年",0,0));
            }
            if(!exitAgeGroup.containsKey("老年")){
                sampleAnalysisList.add(new SampleAnalysisDto("老年",0,0));
            }
            if(!exitAgeGroup.containsKey("其他")){
                sampleAnalysisList.add(new SampleAnalysisDto("其他",0,0));
            }
        }

        sampleAnalysisRes.setSampleAnalysisList(sampleAnalysisList);
        for(SampleAnalysisDto info : sampleAnalysisList){
            total += info.getNumber();
        }
        sampleAnalysisRes.setTotal(total);
        return sampleAnalysisRes;

    }
}
