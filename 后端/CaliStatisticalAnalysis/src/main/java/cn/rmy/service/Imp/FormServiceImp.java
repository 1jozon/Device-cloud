package cn.rmy.service.Imp;

import cn.rmy.common.pojo.dto.CaliConditionDto;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.beans.analysis.CaliDataInfo;
import cn.rmy.dao.CaliDataDao;
import cn.rmy.dao.FormDao;
import cn.rmy.service.FormService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定标列表实现
 *
 * @author chu
 * @date 2021/12/20
 */
@Service
public class FormServiceImp implements FormService {

    @Autowired
    private FormDao formDao;


    @Autowired
    private CaliDataDao caliDataDao;

    /**
     * 分页条件查询定标列表-y
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @param authorityInsList
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult getCaliListByCondition(int current, int size, CaliConditionDto condition, List<String> authorityInsList) {
       // int all = condition.getAll();
        String insId = condition.getInsId();
        String projectId = condition.getProjectId();
        Date startTime = condition.getStartTime();
        Date endTime = condition.getEndTime();
        if (current <= 0){
            current = 1;
        }
        if ((size <= 0)){
            size = 10;
        }
        Page<CaliDataInfo> page =new Page<>(current,size);
        List<CaliDataInfo> selectDateList = new ArrayList<>();
        QueryWrapper<CaliDataInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ins_id", authorityInsList);
        if (startTime != null && endTime != null && insId == null && projectId == null){
            //全部-规定时间
            queryWrapper.eq("deleted", 0)
                    .between("cali_time",startTime,endTime)
                    .orderByDesc("cali_time");
        }else if (insId != null && insId.length() != 0 && startTime != null && endTime != null){
            //仪器-规定时间
            queryWrapper.like("ins_id", insId)
                    .between("cali_time",startTime,endTime)
                    .orderByDesc("cali_time");
        }else if(insId != null && insId.length() != 0){
            //仪器-不规定时间
            queryWrapper.like("ins_id",insId)
                    .orderByDesc("cali_time");
        }else if(projectId != null && projectId.length() != 0 && startTime != null && endTime != null ){
            //项目名称id-规定时间
            queryWrapper.like("project_id",projectId)
                .between("cali_time",startTime,endTime)
                .orderByDesc("cali_time");
        }else if(projectId != null && projectId.length() != 0) {
            //项目名称id-不规定时间
            queryWrapper.like("project_id", projectId)
                    .orderByDesc("cali_time");
        }else{
            //全部
            queryWrapper.eq("deleted", 0)
                    .orderByDesc("cali_time");
        }

        caliDataDao.selectPage(page,queryWrapper);
        selectDateList = page.getRecords();

        if (selectDateList == null || page.getTotal() == 0){
            return new SelectResult((long) 0, selectDateList);
        }
        return new SelectResult((long) page.getTotal(), selectDateList);
    }
}
