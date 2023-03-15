package cn.rmy.service.Impl;

import cn.rmy.beans.SelectResult;
import cn.rmy.beans.dto.CheckDataDto;
import cn.rmy.beans.dto.ProjectNameVO;
import cn.rmy.common.beans.checkData.CheckData;
import cn.rmy.dao.CheckDataDao;
import cn.rmy.dao.CheckDataDtoDao;
import cn.rmy.dao.CheckDataMapper;
import cn.rmy.dao.ProjectNameVODao;
import cn.rmy.service.CheckDataService;
import cn.rmy.service.imp.UsersServiceImp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@Slf4j
public class CheckDataServiceImpl implements CheckDataService {

    @Autowired
    private CheckDataDao checkDataDao;

    @Autowired
    private CheckDataMapper checkDataMapper;

    @Autowired
    private ProjectNameVODao projectNameVODao;

    @Autowired
    private CheckDataDtoDao checkDataDtoDao;

    @Autowired
    private UsersServiceImp usersService;


    @Override
    public void insert(List<CheckData> list) {
        for(CheckData checkData :list){
            int rec = checkDataDao.insert(checkData);
            if(rec<1) log.info("检验数据插入失败 ："+checkData);
        }
    }

    @Override
    public void insertBatch(List<CheckData> list) {

        for(CheckData checkData :list){
            checkDataMapper.insertBatch(checkData);
        }
    }

    @Override
    public int update(CheckData checkData) {
        CheckData temp = checkDataDao.selectById(checkData.getId());
        if(temp == null) return -1;
        if(checkData.getPatientName()!=null) temp.setPatientName(checkData.getPatientName());
        if(checkData.getPatientArea()!=null) temp.setPatientArea(checkData.getPatientArea());
        if(checkData.getPatientSex()!=0) temp.setPatientSex(checkData.getPatientSex());
        if(checkData.getPatientAge()!=0) temp.setPatientAge(checkData.getPatientAge());
        int rec = checkDataDao.updateById(temp);
        return rec;
    }

    @Override
    public int delete(CheckData checkData) {
        if(checkData.getId()<=0) return -1;
        int rec = checkDataDao.deleteById(checkData.getId());
        return rec;
    }

    @Override
    public SelectResult getByCondition(CheckDataDto checkDataDto, int current, int size, String startTime, String endTime,String userId)  {
        /*if(checkDataDto.getProjectName()==null) checkDataDto.setProjectName("");
        if(checkDataDto.getInstrumentId()==null) checkDataDto.setInstrumentId("");
        //if(checkDataDto.getPatientSex()>0) wrapper.eq("patient_sex",checkDataDto.getPatientSex());

        //if(checkDataDto.getCreateTime()==null) checkDataDto.setCreateTime(startTime);
        //if(checkDataDto.getUpdateTime()==null) checkDataDto.setUpdateTime(endTime);

        checkDataDto.setInstrumentId("%"+checkDataDto.getInstrumentId()+"%");
        checkDataDto.setProjectName("%"+checkDataDto.getProjectName()+"%");


        List<CheckDataDto> list;
        if(checkDataDto.getPatientSex()>0){
            list = checkDataDtoDao.getByIPSSEU(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),checkDataDto.getPatientSex(),startTime,endTime,userId);
        }
        else{
            list = checkDataDtoDao.getByIPSEU(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),startTime,endTime,userId);
        }

        List<CheckDataDto> res = new ArrayList<>();
        int be = (current-1)*size;
        for(int i=(current-1)*size;i<Math.min(be+size,list.size());i++){
            res.add(list.get(i));
        }
        return new SelectResult((long) list.size(), res);
        */

        if (checkDataDto.getProjectName() == null) checkDataDto.setProjectName("");
        if (checkDataDto.getInstrumentId() == null) checkDataDto.setInstrumentId("");
        //if(checkDataDto.getPatientSex()>0) wrapper.eq("patient_sex",checkDataDto.getPatientSex());

        //if(checkDataDto.getCreateTime()==null) checkDataDto.setCreateTime(startTime);
        //if(checkDataDto.getUpdateTime()==null) checkDataDto.setUpdateTime(endTime);


        //分页查询
        List<CheckDataDto> list = new ArrayList<>();
        List<CheckData> checkDataList = new ArrayList<>();
        Page<CheckData> page = new Page<>(current, size);

        QueryWrapper<CheckData> queryWrapper = new QueryWrapper<>();
        if (!checkDataDto.getInstrumentId().equals("")) {
            //按照仪器
            queryWrapper.eq("instrument_id", checkDataDto.getInstrumentId());
        }else if(checkDataDto.getInstrumentId().equals("")){
            // 权限下所有仪器，根据 userid 指定的仪器id数据
            List<String> authorityInsList = usersService.getcurrentUserInsIdList(userId);
            if (authorityInsList != null && authorityInsList.size() > 0){
                queryWrapper.in("instrument_id", authorityInsList);
            }
        }
        if (checkDataDto.getPatientSex() > 0) {
            //按照性别
            queryWrapper.eq("patient_sex", checkDataDto.getPatientSex());
        }
        if (!checkDataDto.getProjectName().equals("")) {
            //按照项目名称
            QueryWrapper queryWrapperPro = new QueryWrapper();
            queryWrapperPro.eq("project_name", checkDataDto.getProjectName());
            List<ProjectNameVO> selectProject = projectNameVODao.selectList(queryWrapperPro);
            if (selectProject != null && selectProject.size() == 1) {
                queryWrapper.eq("project_id", selectProject.get(0).getProjectId());
            } else {
                System.out.println("项目信息重复！");
            }
        }

        queryWrapper.between("check_time", startTime, endTime);
        checkDataDao.selectPage(page, queryWrapper);
        checkDataList = page.getRecords();
        Map<String, String> projectMap = new HashMap<>();
        for (CheckData checkData : checkDataList) {
            CheckDataDto checkDataDto1 = new CheckDataDto();
            if (projectMap.containsKey(checkData.getProjectId())) {
                String projectName = projectMap.get(checkData.getProjectId());
                checkDataDto1.setProjectName(projectName);
            } else {
                QueryWrapper queryWrapperPro = new QueryWrapper();
                queryWrapperPro.eq("project_id", checkData.getProjectId());
                List<ProjectNameVO> projectNameVOList = projectNameVODao.selectList(queryWrapperPro);
                if (projectNameVOList != null && projectNameVOList.size() == 1) {
                    checkDataDto1.setProjectName(projectNameVOList.get(0).getProjectName());
                    projectMap.put(projectNameVOList.get(0).getProjectId(), projectNameVOList.get(0).getProjectName());
                }
            }
            checkDataDto1.setInstrumentId(checkData.getInstrumentId())
                    .setId(checkData.getId())
                    .setModelId(checkData.getModelId())
                    .setPatientAge(checkData.getPatientAge())
                    .setPatientSex(checkData.getPatientSex())
                    .setPatientArea(checkData.getPatientArea())
                    .setPatientName(checkData.getPatientName())
                    .setTestRlu(checkData.getTestRlu())
                    .setTestResult(checkData.getTestResult())
                    .setReferenceRange(checkData.getReferenceRange())
                    .setUnit(checkData.getUnit())
                    .setDiluRatio(checkData.getDiluRatio())
                    .setReagentBatchId(checkData.getReagentBatchId())
                    .setSampleType(checkData.getSampleType())
                    .setException(checkData.getException())
                    .setCheckTime(checkData.getCheckTime())
                    .setUpdateTime(checkData.getUpdateTime())
                    .setCreateTime(checkData.getCreateTime())
                    .setVersion(checkData.getVersion())
                    .setDeleted(checkData.getDeleted());
            list.add(checkDataDto1);
        }
        List<CheckDataDto> res = new ArrayList<>();
        for (int i = 0; i < checkDataList.size(); i++) {
            res.add(list.get(i));
        }
        return new SelectResult(page.getTotal(), res);
    }

    @Override
    public SelectResult getByCondition(CheckDataDto checkDataDto, int current, int size, String startTime, String endTime) {

        //分页修改前代码
/*      if(checkDataDto.getProjectName()==null) checkDataDto.setProjectName("");
        if(checkDataDto.getInstrumentId()==null) checkDataDto.setInstrumentId("");
        //if(checkDataDto.getPatientSex()>0) wrapper.eq("patient_sex",checkDataDto.getPatientSex());

        //if(checkDataDto.getCreateTime()==null) checkDataDto.setCreateTime(startTime);
        //if(checkDataDto.getUpdateTime()==null) checkDataDto.setUpdateTime(endTime);

        checkDataDto.setInstrumentId("%"+checkDataDto.getInstrumentId()+"%");
        checkDataDto.setProjectName("%"+checkDataDto.getProjectName()+"%");


        List<CheckDataDto> list;
        if(checkDataDto.getPatientSex()>0){
            list = checkDataDtoDao.getByIPSSE(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),checkDataDto.getPatientSex(),startTime,endTime);
        }
        else{
            list = checkDataDtoDao.getByIPSE(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),startTime,endTime);
        }

        List<CheckDataDto> res = new ArrayList<>();
        int be = (current-1)*size;
        for(int i=(current-1)*size;i<Math.min(be+size,list.size());i++){
            res.add(list.get(i));
        }*/

        if (checkDataDto.getProjectName() == null) checkDataDto.setProjectName("");
        if (checkDataDto.getInstrumentId() == null) checkDataDto.setInstrumentId("");
        //if(checkDataDto.getPatientSex()>0) wrapper.eq("patient_sex",checkDataDto.getPatientSex());

        //if(checkDataDto.getCreateTime()==null) checkDataDto.setCreateTime(startTime);
        //if(checkDataDto.getUpdateTime()==null) checkDataDto.setUpdateTime(endTime);


        //分页查询
        List<CheckDataDto> list = new ArrayList<>();
        List<CheckData> checkDataList = new ArrayList<>();
        Page<CheckData> page = new Page<>(current, size);

        QueryWrapper<CheckData> queryWrapper = new QueryWrapper<>();
        if (!checkDataDto.getInstrumentId().equals("")) {
            //按照仪器
            queryWrapper.eq("instrument_id", checkDataDto.getInstrumentId());
        }
        if (checkDataDto.getPatientSex() > 0) {
            //按照性别
            queryWrapper.eq("patient_sex", checkDataDto.getPatientSex());
        }
        if (!checkDataDto.getProjectName().equals("")) {
            //按照项目名称
            QueryWrapper queryWrapperPro = new QueryWrapper();
            queryWrapperPro.eq("project_name", checkDataDto.getProjectName());
            List<ProjectNameVO> selectProject = projectNameVODao.selectList(queryWrapperPro);
            if (selectProject != null && selectProject.size() == 1) {
                queryWrapper.eq("project_id", selectProject.get(0).getProjectId());
            } else {
                System.out.println("项目信息重复！");
            }
        }
        queryWrapper.between("check_time", startTime, endTime);
        checkDataDao.selectPage(page, queryWrapper);

        checkDataList = page.getRecords();
        Map<String, String> projectMap = new HashMap<>();
        for (CheckData checkData : checkDataList) {
            CheckDataDto checkDataDto1 = new CheckDataDto();
            if (projectMap.containsKey(checkData.getProjectId())) {
                String projectName = projectMap.get(checkData.getProjectId());
                checkDataDto1.setProjectName(projectName);
            } else {
                QueryWrapper queryWrapperPro = new QueryWrapper();
                queryWrapperPro.eq("project_id", checkData.getProjectId());
                List<ProjectNameVO> projectNameVOList = projectNameVODao.selectList(queryWrapperPro);
                if (projectNameVOList != null && projectNameVOList.size() == 1) {
                    checkDataDto1.setProjectName(projectNameVOList.get(0).getProjectName());
                    projectMap.put(projectNameVOList.get(0).getProjectId(), projectNameVOList.get(0).getProjectName());
                }
            }
            checkDataDto1.setInstrumentId(checkData.getInstrumentId())
                    .setId(checkData.getId())
                    .setModelId(checkData.getModelId())
                    .setPatientAge(checkData.getPatientAge())
                    .setPatientSex(checkData.getPatientSex())
                    .setPatientName(checkData.getPatientName())
                    .setPatientArea(checkData.getPatientArea())
                    .setTestRlu(checkData.getTestRlu())
                    .setTestResult(checkData.getTestResult())
                    .setReferenceRange(checkData.getReferenceRange())
                    .setUnit(checkData.getUnit())
                    .setDiluRatio(checkData.getDiluRatio())
                    .setReagentBatchId(checkData.getReagentBatchId())
                    .setSampleType(checkData.getSampleType())
                    .setException(checkData.getException())
                    .setCheckTime(checkData.getCheckTime())
                    .setUpdateTime(checkData.getUpdateTime())
                    .setCreateTime(checkData.getCreateTime())
                    .setVersion(checkData.getVersion())
                    .setDeleted(checkData.getDeleted());
            list.add(checkDataDto1);
        }

        List<CheckDataDto> res = new ArrayList<>();

        for (int i = 0; i < checkDataList.size(); i++) {
            res.add(list.get(i));
        }
        return new SelectResult(page.getTotal(), res);
    }


    @Override
    public List<CheckDataDto> getByCondition(CheckDataDto checkDataDto,String startTime,String endTime,String userId) {
        if(checkDataDto.getProjectName()==null) checkDataDto.setProjectName("");
        if(checkDataDto.getInstrumentId()==null) checkDataDto.setInstrumentId("");
        //if(checkDataDto.getPatientSex()>0) wrapper.eq("patient_sex",checkDataDto.getPatientSex());

        //if(checkDataDto.getCreateTime()!=null) checkDataDto.setCreateTime(startTime);
        //if(checkDataDto.getUpdateTime()!=null) checkDataDto.setUpdateTime(endTime);

        checkDataDto.setInstrumentId("%"+checkDataDto.getInstrumentId()+"%");
        checkDataDto.setProjectName("%"+checkDataDto.getProjectName()+"%");

        //String end = checkDataDto.getUpdateTime().toString();

        List<CheckDataDto> list;
        if(checkDataDto.getPatientSex()>0){
            list = checkDataDtoDao.getByIPSSEU(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),checkDataDto.getPatientSex(),startTime,endTime,userId);
        }
        else{
            list = checkDataDtoDao.getByIPSEU(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),startTime,endTime,userId);
        }
        return list;
    }

    @Override
    public List<CheckDataDto> getByCondition(CheckDataDto checkDataDto,String startTime,String endTime) {
        if(checkDataDto.getProjectName()==null) checkDataDto.setProjectName("");
        if(checkDataDto.getInstrumentId()==null) checkDataDto.setInstrumentId("");
        //if(checkDataDto.getPatientSex()>0) wrapper.eq("patient_sex",checkDataDto.getPatientSex());

        //if(checkDataDto.getCreateTime()!=null) checkDataDto.setCreateTime(startTime);
        //if(checkDataDto.getUpdateTime()!=null) checkDataDto.setUpdateTime(endTime);

        checkDataDto.setInstrumentId("%"+checkDataDto.getInstrumentId()+"%");
        checkDataDto.setProjectName("%"+checkDataDto.getProjectName()+"%");

        //String end = checkDataDto.getUpdateTime().toString();

        List<CheckDataDto> list;
        if(checkDataDto.getPatientSex()>0){
            list = checkDataDtoDao.getByIPSSE(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),checkDataDto.getPatientSex(),startTime,endTime);
        }
        else{
            list = checkDataDtoDao.getByIPSE(checkDataDto.getInstrumentId(),checkDataDto.getProjectName(),startTime,endTime);
        }
        return list;
    }
}
