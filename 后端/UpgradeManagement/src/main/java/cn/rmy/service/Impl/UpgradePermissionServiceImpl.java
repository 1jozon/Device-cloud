package cn.rmy.service.Impl;

import cn.rmy.beans.UpgradePackage;
import cn.rmy.beans.UpgradePermission;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.InstrumentModel;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.dao.InstrumentModelDao;
import cn.rmy.dao.UpgradePackageDao;
import cn.rmy.dao.UpgradePermissionDao;
import cn.rmy.service.UpgradePermissionService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UpgradePermissionServiceImpl implements UpgradePermissionService {

    @Autowired
    private UpgradePermissionDao upgradePermissionDao;

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private UpgradePackageDao upgradePackageDao;

    @Autowired
    private InstrumentModelDao instrumentModelDao;

    @Override
    public int insert(UpgradePackage upgradePackage) {
        int rec=0;
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        InstrumentModel instrumentModel = instrumentModelDao.selectById(upgradePackage.getModelId());
        queryWrapper.eq("instrument_model",instrumentModel.getInstrumentModel());
        List<Instrument> list = instrumentDao.selectList(queryWrapper);
        if(list!=null && list.size()!=0){
            for(Instrument inst : list){
                UpgradePermission upp = new UpgradePermission();
                upp.setPackageId(upgradePackage.getId())
                        .setInstId(inst.getInstrumentId())
                        .setAllowed(-1);
                rec = upgradePermissionDao.insert(upp);
            }
        }
        return rec;
    }

    @Override
    public List<UpgradePermission> getByPackageId(UpgradePermission upgradePermission) {

        //UpgradePackage upgradePackage = upgradePackageDao.selectById(upgradePermission.getPackageId());
        List<UpgradePermission> list = upgradePermissionDao.getByCondition(upgradePermission.getPackageId());
        return list;
    }

    @Override
    public int update(List<UpgradePermission> list) {
        int rec=-1;
        for(UpgradePermission upp : list){
            UpgradePermission uppTemp = upgradePermissionDao.selectById(upp.getId());
            uppTemp.setAllowed(upp.getAllowed());
            rec = upgradePermissionDao.updateById(uppTemp);
        }
        return rec;
    }

    @Override
    public int updateAll(UpgradePermission upgradePermission) {
        int rec=-1;
        QueryWrapper<UpgradePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("package_id",upgradePermission.getPackageId());
        List<UpgradePermission> list = upgradePermissionDao.selectList(queryWrapper);
        if(list!=null && list.size()!=0){
            for(UpgradePermission upp : list){
                upp.setAllowed(upgradePermission.getAllowed());
                rec = upgradePermissionDao.updateById(upp);
            }
        }
        return rec;
    }

    @Override
    public int updateUpgrade(UpgradePermission upgradePermission) {
        int rec=0;
        QueryWrapper<UpgradePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("package_id",upgradePermission.getPackageId())
                .eq("inst_id",upgradePermission.getInstId());
        UpgradePermission upp = upgradePermissionDao.selectOne(queryWrapper);
        if(upp==null) rec=-1;
        else{
            upp.setUpgraded(1);
            rec = upgradePermissionDao.updateById(upp);
        }

        return rec;
    }

    @Override
    public int deleteByPackageId(UpgradePermission upgradePermission) {
        QueryWrapper<UpgradePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("package_id",upgradePermission.getPackageId());
        if(upgradePermissionDao.selectList(wrapper)==null) return -1;
        int rec = upgradePermissionDao.delete(wrapper);
        return rec;
    }

    @Override
    public UpgradePermission getByCondition(String packageName, String instId) {
        QueryWrapper<UpgradePackage> wrapper = new QueryWrapper<>();
        wrapper.eq("package_name",packageName);
        UpgradePackage upgradePackage = upgradePackageDao.selectOne(wrapper);
        if(upgradePackage==null) return null;
        QueryWrapper<UpgradePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("package_id",upgradePackage.getId())
                .eq("inst_id",instId);
        UpgradePermission upgradePermission = upgradePermissionDao.selectOne(queryWrapper);
        return upgradePermission;
    }

    @Override
    public List<UpgradePermission> getSendInstByPackId(UpgradePackage upgradePackage) {
        QueryWrapper<UpgradePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("package_id",upgradePackage.getId());
        wrapper.eq("allowed",1);
        List<UpgradePermission> list = upgradePermissionDao.selectList(wrapper);


        return list;
    }
}
