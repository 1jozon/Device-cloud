package cn.rmy.service.Impl;

import cn.rmy.beans.PackageType;
import cn.rmy.beans.SelectResult;
import cn.rmy.beans.UpgradePackage;
import cn.rmy.beans.UpgradePermission;
import cn.rmy.beans.dto.UpgradePackSendVo;
import cn.rmy.beans.dto.UpgradePackageVO;

import cn.rmy.common.beans.InstrumentModel;
import cn.rmy.dao.InstrumentModelDao;
import cn.rmy.dao.PackageTypeDao;
import cn.rmy.dao.UpgradePackageDao;
import cn.rmy.mqttUtils.MqttSendHandle;
import cn.rmy.service.UpgradePackageService;
import cn.rmy.service.UpgradePermissionService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
@Slf4j
public class UpgradePackageServiceImpl implements UpgradePackageService {

    @Autowired
    private UpgradePackageDao upgradePackageDao;

    @Autowired
    private PackageTypeDao packageTypeDao;

    @Autowired
    private InstrumentModelDao instrumentModelDao;

    @Autowired
    private UpgradePermissionService upgradePermissionService;

    @Autowired
    private MqttSendHandle mqttSendHandle;

    @Override
    public int insertPackage(UpgradePackage upgradePackage) {

        QueryWrapper<UpgradePackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("package_name", upgradePackage.getPackageName())
                .eq("model_id", upgradePackage.getModelId());
        int rec = 0;
        if (upgradePackageDao.selectOne(queryWrapper) == null) {
            rec = upgradePackageDao.insert(upgradePackage);
        } else
            rec = -1;

        UpgradePackage pp = upgradePackageDao.selectOne(queryWrapper);
        if (pp != null) {
            //UpgradePermission upp = new UpgradePermission();
            //upp.setPackageId(pp.getId());
            upgradePermissionService.insert(pp);
        }


        return rec;
    }

    @Override
    public int deletePackage(int id) {
        int rec;
        if (upgradePackageDao.selectById(id) == null)
            rec = -1;
        else rec = upgradePackageDao.deleteById(id);
        return rec;
    }

    @Override
    public SelectResult getByCondition(UpgradePackage upgradePackage, int current, int size) {
        QueryWrapper<UpgradePackage> queryWrapper = new QueryWrapper<>();
        if (upgradePackage.getTypeId() != 0)
            queryWrapper.eq("type_id", upgradePackage.getTypeId());
        if (upgradePackage.getModelId() != 0)
            queryWrapper.eq("model_id", upgradePackage.getModelId());
        if (upgradePackage.getPackageName() != null)
            queryWrapper.like("package_name", upgradePackage.getPackageName());
        queryWrapper.orderByDesc("id");

        current = current <= 0 ? 1 : current;
        size = size <= 0 ? 4 : size;
        Page<UpgradePackage> page = new Page<>(current, size);

        upgradePackageDao.selectPage(page, queryWrapper);
        List<UpgradePackage> list = page.getRecords();
        List<UpgradePackageVO> res = new ArrayList<>();

        for (UpgradePackage pp : list) {
            String type;
            if (packageTypeDao.selectById(pp.getTypeId()) != null)
                type = packageTypeDao.selectById(pp.getTypeId()).getTypeName();
            else type = "升级包类型名被删除";
            String model;
            if (instrumentModelDao.selectById(pp.getModelId()) != null)
                model = instrumentModelDao.selectById(pp.getModelId()).getInstrumentModel();
            else model = "仪器类型被删除";
            UpgradePackageVO upgradePackageVO = new UpgradePackageVO(pp.getId(), pp.getPackageName(), pp.getTypeId(), type, pp.getModelId(), model, pp.getPackVersion(), pp.getDescription(), pp.getUrl(), pp.getAuthorization(), pp.getCreateTime(), pp.getUpdateTime(), pp.getVersion(), pp.getDeleted());
            res.add(upgradePackageVO);
        }

        return new SelectResult(page.getTotal(), res);
    }

    @Override
    public int updatePackage(UpgradePackage upgradePackage) {
        int rec = 0;
        if (upgradePackageDao.selectById(upgradePackage.getId()) == null) rec = -1;
        else {
            UpgradePackage pp = upgradePackageDao.selectById(upgradePackage.getId());
            if (upgradePackage.getDescription() != null && upgradePackage.getDescription().length() > 0)
                pp.setDescription(upgradePackage.getDescription());
            if (upgradePackage.getTypeId() != 0)
                pp.setTypeId(upgradePackage.getTypeId());
            if (upgradePackage.getModelId() != 0)
                pp.setModelId(upgradePackage.getModelId());
            if (upgradePackage.getAuthorization() != 0)
                pp.setAuthorization(upgradePackage.getAuthorization());
            rec = upgradePackageDao.updateById(pp);
        }
        return rec;
    }

    @Override
    public UpgradePackSendVo getSendPack(UpgradePackage upgradePackage) {
        UpgradePackage temp = upgradePackageDao.selectById(upgradePackage.getId());
        PackageType packageType = packageTypeDao.selectById(temp.getTypeId());
        InstrumentModel instrumentModel = instrumentModelDao.selectById(temp.getModelId());

        UpgradePackSendVo upgradePackSendVo = new UpgradePackSendVo();
        upgradePackSendVo.setPackageName(temp.getPackageName());
        upgradePackSendVo.setPackVersion(temp.getPackVersion());
        upgradePackSendVo.setDescription(temp.getDescription());
        upgradePackSendVo.setUrl(temp.getUrl());
        if (packageType != null) upgradePackSendVo.setType(packageType.getTypeName());
        if (instrumentModel != null) upgradePackSendVo.setModel(instrumentModel.getInstrumentModel());

        return upgradePackSendVo;
    }

    @Override
    public int sendUpgrade(UpgradePackage upgradePackage) {

        if (upgradePackage.getId() == 0) {
            log.info("未指定升级包");
            return -1;
        }

        UpgradePackSendVo upgradePackSendVo = getSendPack(upgradePackage);
        String msg = JSON.toJSONString(upgradePackSendVo);

        List<UpgradePermission> list = upgradePermissionService.getSendInstByPackId(upgradePackage);
        if (list == null || list.size() == 0) {
            log.info("推送0台仪器");
            return 0;
        }
        for (UpgradePermission upp : list) {
            String topic = "/upgrade/" + upp.getInstId();
            //String topic = "/upgrade";
            mqttSendHandle.sendHandle(topic, 2, msg);
        }

        return list.size();
    }

    /**
     * 异步是先插入
     *
     * @param upgradePackage 升级包
     * @return int
     * @throws Exception 异常
     */
    @Override
    public int insertPackageSyn(UpgradePackage upgradePackage) throws Exception {

        QueryWrapper<UpgradePackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("package_name", upgradePackage.getPackageName())
                .eq("model_id", upgradePackage.getModelId());
        int rec = 0;
        if (upgradePackageDao.selectOne(queryWrapper) != null) {
            return -1;
        }

        // 线程池异步方法
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(new syInsert(upgradePackage, upgradePackageDao, upgradePermissionService));

        return rec;
    }

    class syInsert implements Runnable {

        private UpgradePackage upgradePackage;

        private UpgradePackageDao upgradePackageDao;

        private UpgradePermissionService upgradePermissionService;

        public syInsert(UpgradePackage upgradePkg, UpgradePackageDao upgradePkgDao, UpgradePermissionService upgradePermService) {
            this.upgradePackage = upgradePkg;
            this.upgradePackageDao = upgradePkgDao;
            this.upgradePermissionService = upgradePermService;
        }

        @Override
        public void run() {
            long s2 = System.currentTimeMillis();
            System.out.println("异步线程开始:" + s2);
            try {
                QueryWrapper<UpgradePackage> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("package_name", upgradePackage.getPackageName())
                        .eq("model_id", upgradePackage.getModelId());

                upgradePackageDao.insert(upgradePackage);
                UpgradePackage pp = upgradePackageDao.selectOne(queryWrapper);
                if (pp != null) {
                    //UpgradePermission upp = new UpgradePermission();
                    //upp.setPackageId(pp.getId());
                    upgradePermissionService.insert(pp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("异步线程结束。" + (System.currentTimeMillis() - s2));
        }
    }
}








