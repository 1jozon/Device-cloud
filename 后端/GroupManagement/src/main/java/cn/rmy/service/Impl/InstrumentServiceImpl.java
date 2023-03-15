package cn.rmy.service.Impl;

import cn.rmy.common.beans.*;
import cn.rmy.common.beans.gps.InsMsgVODto;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.common.pojo.dto.InstCondition;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.dao.InstrumentDao;

import cn.rmy.dao.InstrumentModelDao;
import cn.rmy.dao.dtoDao.GetInstByCondition;
import cn.rmy.dao.dtoDao.UpgradePermVODao;
import cn.rmy.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    InstrumentDao instrumentDao;

    @Autowired
    GetInstByCondition getInstByCondition;


    @Autowired
    ReagentSurplusCountVOService reagentSurplusCountService;

    @Autowired
    InsMsgVOService insMsgVOService;

    @Autowired
    InstrumentModelDao instrumentModelDao;

    @Autowired
    UpgradePermVODao upgradePermVODao;

    @Autowired
    UserWithInstService userWithInstService;

    @Autowired
    ProjectVOService projectVOService;


    @Override
    public int insert(Instrument instrument) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_id",instrument.getInstrumentId());
        int rec = 0;
        //System.out.println(instrumentDao.selectOne(queryWrapper));
        instrument.setFaultStatus(1);
        instrument.setOnlineStatus(-1);
        Instrument temp = instrumentDao.selectOne(queryWrapper);
        if(temp==null){
            rec = instrumentDao.insert(instrument);
            //创建新仪器和历史升级包的对应关系O
            setUpgradePermission(instrument.getInstrumentId());
        }
        else if(temp.getInstrumentInstallerId().equals("test-0000")){
            temp.setInstrumentModel(instrument.getInstrumentModel());
            temp.setInstrumentAddress(instrument.getInstrumentAddress());
            temp.setInstrumentDate(instrument.getInstrumentDate());
            temp.setInstrumentInstallerId(instrument.getInstrumentInstallerId());
            temp.setInstrumentInstallerName(instrument.getInstrumentInstallerName());
            temp.setInstrumentMaintainerId(instrument.getInstrumentMaintainerId());
            temp.setInstrumentMaintainerName(instrument.getInstrumentMaintainerName());
            temp.setInstrumentMaintainerPhone(instrument.getInstrumentMaintainerPhone());

            rec = instrumentDao.updateById(temp);
        }
        else rec = -1;
        return rec;
    }

    @Override
    public int insertTestInstruemnt(Instrument instrument) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_id",instrument.getInstrumentId());
        if(instrumentDao.selectOne(queryWrapper)!=null) return -2;//仪器已存在

        if(instrument.getInstrumentId()==null) return -3;//未正确传入仪器Id
        if(instrument.getInstrumentModel()==null) return -4;//未正确传入仪器类型

        String modelName = instrument.getInstrumentModel();
        QueryWrapper<InstrumentModel> wrapper = new QueryWrapper<>();
        wrapper.eq("instrument_model",modelName);
        if(instrumentModelDao.selectOne(wrapper)==null){
            InstrumentModel instrumentModel = new InstrumentModel();
            instrumentModel.setInstrumentModel(modelName);
            instrumentModelDao.insert(instrumentModel);
        }

        if(instrument.getInstrumentAddress()==null||instrument.getInstrumentAddress().length()==0)
            instrument.setInstrumentAddress("江苏省南京市栖霞区红枫产业园测试");
        if(instrument.getInstrumentDate()==null){
            Date date = new Date();
            instrument.setInstrumentDate(date);
        }
        instrument.setFaultStatus(1);
        instrument.setOnlineStatus(-1);
        instrument.setInstrumentInstallerId("test-0000");
        instrument.setInstrumentInstallerName("内部测试");
        instrument.setInstrumentMaintainerId("test-0000");
        instrument.setInstrumentMaintainerName("内部测试");
        instrument.setInstrumentMaintainerPhone("00000000000");
        instrument.setHospitalName("内部测试");

        int rec = instrumentDao.insert(instrument);
        //创建新仪器和历史升级包的对应关系
        setUpgradePermission(instrument.getInstrumentId());
        return rec;
    }

    @Override
    public SelectResult getInstByCondition(InstCondition instCondition, int current, int size) {
        String onlineStatus,faultStatus;
        if(instCondition.getInstrumentId()==null) instCondition.setInstrumentId("");
        if(instCondition.getInstrumentModel()==null) instCondition.setInstrumentModel("");
        if(instCondition.getInstrumentAddress()==null) instCondition.setInstrumentAddress("");
        if(instCondition.getOnlineStatus()==0) onlineStatus="%";
        else onlineStatus=String.valueOf(instCondition.getOnlineStatus());
        if(instCondition.getFaultStatus()==0) faultStatus="%";
        else faultStatus = String.valueOf(instCondition.getFaultStatus());
        if(instCondition.getHospitalName()==null) instCondition.setHospitalName("");
        instCondition.setInstrumentId("%"+instCondition.getInstrumentId()+"%");
        instCondition.setInstrumentModel("%"+instCondition.getInstrumentModel()+"%");
        instCondition.setInstrumentAddress("%"+instCondition.getInstrumentAddress()+"%");
        instCondition.setHospitalName("%"+instCondition.getHospitalName()+"%");
        //onlineStatus = "%"+onlineStatus+"%";
        //faultStatus = "%"+faultStatus+"%";

        List<Instrument> list;
        if(instCondition.getGroupId()!=0 && instCondition.getUserId()!=null && instCondition.getUserId().length()>0)
            list = getInstByCondition.getInst(instCondition.getInstrumentId(),instCondition.getInstrumentModel(),
                instCondition.getInstrumentAddress(),onlineStatus,faultStatus,instCondition.getHospitalName(),instCondition.getGroupId(),instCondition.getUserId());
        else if(instCondition.getGroupId()==0 && instCondition.getUserId()!=null && instCondition.getUserId().length()>0)
            list = getInstByCondition.getInstWithoutGroupId(instCondition.getInstrumentId(),instCondition.getInstrumentModel(),
                    instCondition.getInstrumentAddress(),onlineStatus,faultStatus,instCondition.getHospitalName(),instCondition.getUserId());
        else if(instCondition.getGroupId()!=0 && (instCondition.getUserId()==null || instCondition.getUserId().length()==0))
            list = getInstByCondition.getInstWithoutUserId(instCondition.getInstrumentId(),instCondition.getInstrumentModel(),
                    instCondition.getInstrumentAddress(),onlineStatus,faultStatus,instCondition.getHospitalName(),instCondition.getGroupId());
        else list = getInstByCondition.getInstWithoutAll(instCondition.getInstrumentId(),instCondition.getInstrumentModel(),
                    instCondition.getInstrumentAddress(),onlineStatus,faultStatus,instCondition.getHospitalName());
        if(list==null||list.size()==0) return new SelectResult(0l,list);
        List<Instrument> res = new ArrayList<>();
        current = current-1;
        for(int i=current*size;i<Math.min(current*size+size,list.size());i++){
            res.add(list.get(i));
        }
        return new SelectResult((long) list.size(),res);
    }

    @Override
    public SelectResult getInstByCondition(InstCondition instCondition) {
        List<Instrument> res ;
        if(instCondition.getUserId()==null||instCondition.getUserId().length()==0){
            res = instrumentDao.selectList(null);
        }
        else res = userWithInstService.getInstsByUserId(instCondition.getUserId());

        SelectResult selectResult = new SelectResult(Long.valueOf(res.size()),res);

        return selectResult;
    }

    @Override
    public InstDetails getInstDetails(String instrumentId) {

        if(instrumentId==null||instrumentId.length()==0){
            Instrument tt = instrumentDao.selectOne(null);
            instrumentId = tt.getInstrumentId();
        }


        InstDetails instDetails = new InstDetails();
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id",instrumentId);

        Instrument instrument = instrumentDao.selectOne(queryWrapper);
        if(instrument==null) return null;

        instDetails.setId(instrument.getId());
        instDetails.setInstrumentMaintainerName(instrument.getInstrumentMaintainerName());
        instDetails.setHospitalName(instrument.getHospitalName());
        instDetails.setOnlineStatus(instrument.getOnlineStatus());
        instDetails.setInstrumentAddress(instrument.getInstrumentAddress());
        instDetails.setInstrumentId(instrument.getInstrumentId());
        instDetails.setInstrumentModel(instrument.getInstrumentModel());
        instDetails.setInstrumentDate(instrument.getInstrumentDate());


        if(reagentSurplusCountService.sumReagentUseNumByDeviceId(instrument.getInstrumentId())!=null)
            instDetails.setSumReagent(reagentSurplusCountService.sumReagentUseNumByDeviceId(instrument.getInstrumentId()));
        else instDetails.setSumReagent("0");
        InsMsgVODto insMsgVODto = insMsgVOService.getMsgByInsId(instrument.getInstrumentId());
        if(insMsgVODto!=null)
        instDetails.setLocation(insMsgVODto.getLac()+" "+insMsgVODto.getCid());
        else instDetails.setLocation("未获取到定位信息");
        List<UserT> list = userWithInstService.getUsersByInstId(instrumentId);
        List<String> relateUserId = new ArrayList<>();
        if(list!=null){
            for(UserT userT:list) relateUserId.add(userT.getUserId());
        }
        instDetails.setReagentConsume(relateUserId);


        List<ProjectSumUsedVO> proList = projectVOService.getInsProjectUsed(instrumentId);
        instDetails.setSampleConsume(proList);


        return instDetails;
    }

    @Override
    public int setUpgradePermission(String instrumentId) {
        //在升级包权限表中插入新仪器
        if(instrumentId==null) return 0;
        List<UpgradePermVO> list = upgradePermVODao.getUpgradePerms(instrumentId);
        if(list==null) return 0;
        for(UpgradePermVO e:list) {
            e.setAllowed(-1);
            QueryWrapper<UpgradePermVO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("package_id",e.getPackageId())
                    .eq("inst_id",e.getInstId());
            if(upgradePermVODao.selectOne(queryWrapper)==null){
                upgradePermVODao.insert(e);
                //System.out.println(e.getPackageId()+" "+e.getInstId());
            }
        }
        return 0;
    }

    @Override
    public SelectResult getByCondition(Instrument inst, int current, int size) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        if(inst.getInstrumentId()!=null)
            queryWrapper.like("instrument_id",inst.getInstrumentId());
        if(inst.getInstrumentModel()!=null)
            queryWrapper.like("instrument_model",inst.getInstrumentModel());
        if(inst.getInstrumentAddress()!=null)
            queryWrapper.like("instrument_address",inst.getInstrumentAddress());
        if(inst.getInstrumentDate()!=null)
            queryWrapper.like("instrument_date",inst.getInstrumentDate());
        if(inst.getOnlineStatus()!=0)
            queryWrapper.like("online_status",inst.getOnlineStatus());
        if(inst.getFaultStatus()!=0)
            queryWrapper.like("fault_status",inst.getFaultStatus());
        if(inst.getInstrumentInstallerId()!=null)
            queryWrapper.like("instrument_installer_id",inst.getInstrumentInstallerId());
        if(inst.getInstrumentInstallerName()!=null)
            queryWrapper.like("instrument_installer_name",inst.getInstrumentInstallerName());
        if(inst.getInstrumentMaintainerId()!=null)
            queryWrapper.like("instrument_maintainer_id",inst.getInstrumentMaintainerId());
        if(inst.getInstrumentMaintainerName()!=null)
            queryWrapper.like("instrument_maintainer_name",inst.getInstrumentMaintainerName());
        if(inst.getInstrumentMaintainerPhone()!=null)
            queryWrapper.like("instrument_maintainer_phone",inst.getInstrumentMaintainerPhone());
        if(inst.getHospitalName()!=null)
            queryWrapper.like("hospital_name",inst.getHospitalName());

        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }
        Page<Instrument> page = new Page<>(current,size);
        instrumentDao.selectPage(page,queryWrapper);
        List<Instrument> list = page.getRecords();


        return new SelectResult(page.getTotal(),list);
    }

    @Override
    public int update(Instrument temp) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        Instrument instrument = instrumentDao.selectById(temp.getId());
        int rec;
        if(instrument==null){
            queryWrapper.eq("instrument_id",temp.getInstrumentId());
            instrument = instrumentDao.selectOne(queryWrapper);
        }
        if(instrument!=null){
            instrument.setInstrumentAddress(temp.getInstrumentAddress())
                    .setInstrumentDate(temp.getInstrumentDate())
                    .setOnlineStatus(temp.getOnlineStatus())
                    .setFaultStatus(temp.getFaultStatus())
                    .setInstrumentInstallerId(temp.getInstrumentInstallerId())
                    .setInstrumentInstallerName(temp.getInstrumentInstallerName())
                    .setInstrumentMaintainerId(temp.getInstrumentMaintainerId())
                    .setInstrumentMaintainerName(temp.getInstrumentMaintainerName())
                    .setInstrumentMaintainerPhone(temp.getInstrumentMaintainerPhone())
                    .setHospitalName(temp.getHospitalName());
            rec = instrumentDao.updateById(instrument);
        }
        else{
            rec = -1;
        }
        return rec;
    }

    @Override
    public int delete(Integer id) {
        int rec = 0;
        if(instrumentDao.selectById(id)==null)
            rec = -1;
        else
            rec = instrumentDao.deleteById(id);
        return rec;
    }

    @Override
    public Instrument getOneByInstrumentId(String instrumentId) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id",instrumentId);
        Instrument result = instrumentDao.selectOne(queryWrapper);
        return result;
    }

    @Override
    public int setOnlineStatus(String instrumentId, int onlineStatus) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_id",instrumentId);
        Instrument instrument = instrumentDao.selectOne(queryWrapper);
        int rec;
        if(instrument!=null){
            instrument.setOnlineStatus(onlineStatus);
            rec = instrumentDao.updateById(instrument);
        }
        else rec=-1;
        return rec;//0:失败，-1：仪器不存在，>0 ：成功
    }

    @Override
    public int setFaultStatus(String instrumentId, int faultStatus) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_id",instrumentId);
        Instrument instrument = instrumentDao.selectOne(queryWrapper);
        int rec;
        if(instrument!=null){
            instrument.setFaultStatus(faultStatus);
            rec = instrumentDao.updateById(instrument);
        }
        else rec=-1;
        return rec;//0:失败，-1：仪器不存在，>0 ：成功
    }




}











