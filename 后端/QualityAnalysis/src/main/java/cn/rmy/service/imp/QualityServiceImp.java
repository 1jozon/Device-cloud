package cn.rmy.service.imp;

import cn.rmy.common.beans.analysis.QualityDataInfo;
import cn.rmy.common.pojo.dto.QualityConditionDto;
import cn.rmy.common.pojo.dto.QualityDataDto;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.dao.QualityDao;
import cn.rmy.service.QualityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 质控统计实现
 *
 * @author chu
 * @date 2021/12/21
 */
@Service
@Transactional
public class QualityServiceImp implements QualityService {

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private UsersServiceImp usersService;

    /**
     * 获得质控信息
     *
     * @param qualityData 质量数据
     * @return {@link QualityDataInfo}
     */
    @Override
    public QualityDataInfo getQualityInfo(QualityDataDto qualityData) {
        String qctrlResult;
        if (qualityData.getQctrlResult() == null || qualityData.getQctrlResult().length() == 0){
            qctrlResult = null;
        }else {
            qctrlResult = qualityData.getQctrlResult();
        }
        QualityDataInfo qualityDataInfo = new QualityDataInfo();
        qualityDataInfo.setInsId(qualityData.getInsId());
        qualityDataInfo.setProjectName(qualityData.getProjectName());
        qualityDataInfo.setQctrlRlu(qualityData.getQctrlRlu());
        qualityDataInfo.setQctrlResult(qctrlResult);
        qualityDataInfo.setQctrlId(qualityData.getQctrlId());
        qualityDataInfo.setProjectId(qualityData.getProjectId());
        qualityDataInfo.setReagentBatchId(qualityData.getReagentBatchId());
        qualityDataInfo.setException(qualityData.getException());
        qualityDataInfo.setQctrlTime(qualityData.getQctrlTime());

        return qualityDataInfo;
    }

    /**
     * 插入质控信息
     *
     * @param qualityData@return int
     */
    @Override
    public int insertQualityInfo(QualityDataDto qualityData) {
        QualityDataInfo qualityDataInfo = getQualityInfo(qualityData);
        int rec = qualityDao.insert(qualityDataInfo);
        return rec == 1 ? 1 : 0;
    }

    /**
     * 异步插入质量信息
     *
     * @param qualityData 质量数据
     */
    @Override
    @Async("threadPoolTaskExecutor")
    public void AsyncInsertQualityInfo(QualityDataDto qualityData) {
        System.out.println("质控数据插入前线程："+Thread.currentThread().getName());
        QualityDataInfo qualityDataInfo = getQualityInfo(qualityData);
         try {
             System.out.println("质控数据插入线程："+Thread.currentThread().getName());
             qualityDao.insert(qualityDataInfo);
         }catch (Exception e){
             System.out.println("异步质控消息插入异常");
         }
    }

    /**
     * 更新质控信息
     *
     * @param qualityDataInfo 高质量的数据信息
     * @return int
     */
    @Override
    public int updateQualityInfo(QualityDataInfo qualityDataInfo) {

        QualityDataInfo oldInfo = qualityDao.selectById(qualityDataInfo.getId());
        oldInfo.setQctrlResult(qualityDataInfo.getQctrlResult());
        oldInfo.setQctrlRlu(qualityDataInfo.getQctrlRlu());

        int rec = qualityDao.updateById(oldInfo);

        return rec == 1 ? 1 : 0;
    }

    /**
     * 通过条件分页查询
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult getByCondition(int current, int size, QualityConditionDto condition) {
        if (current <= 0){
            current = 1;
        }
        if (size <= 0 ){
            size = 4;
        }
        List<QualityDataInfo> qualityDataInfoList = new ArrayList<>();
        //用户权限下仪器
        List<String> userInsIdList = usersService.getcurrentUserInsIdList(condition.getUserId());
        if(userInsIdList == null || userInsIdList.size() == 0)
        {
            return new SelectResult((long)0, qualityDataInfoList);
        }
        Page<QualityDataInfo> page = new Page<>(current, size);
        QueryWrapper<QualityDataInfo> queryWrapper = new QueryWrapper<>();

        if (condition.getInsId() != null && condition.getInsId().length() > 0
            && userInsIdList.contains(condition.getInsId())){
            queryWrapper.like("ins_id", condition.getInsId());
        }
        if (condition.getProjectName() !=  null && condition.getProjectName().length() > 0){
            queryWrapper.like("project_name",condition.getProjectName());
        }
        if(condition.getStartTime() != null && condition.getEndTime() != null){
            queryWrapper.between("qctrl_time",condition.getStartTime(),condition.getEndTime());
        }
        if (condition.getException() != null) {
                queryWrapper.eq("exception", condition.getException());
        }
        //质控时间倒序排列
        queryWrapper.eq("deleted", 0)
                .in("ins_id", userInsIdList)
                .orderByDesc("qctrl_time");

        qualityDao.selectPage(page, queryWrapper);
        qualityDataInfoList = page.getRecords();
        long total = page.getTotal();
        if (qualityDataInfoList.size() == 0){
            return new SelectResult((long)0, qualityDataInfoList);
        }
        return new SelectResult(total, qualityDataInfoList);
    }

    /**
     * 通过id获取详细质控数据
     *
     * @param id id
     * @return {@link QualityDataInfo}
     */
    @Override
    public QualityDataInfo getById(int id) {
        QualityDataInfo qualityDataInfo = qualityDao.selectById(id);
        if (qualityDataInfo == null){
            return null;
        }
        return qualityDataInfo;
    }
}
