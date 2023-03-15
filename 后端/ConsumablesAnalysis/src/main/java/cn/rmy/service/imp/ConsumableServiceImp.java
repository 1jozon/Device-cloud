package cn.rmy.service.imp;

import cn.rmy.common.beans.analysis.ConsumablesDataInfo;
import cn.rmy.common.dto.ConsumablesDataDto;
import cn.rmy.dao.ConsumablesDao;
import cn.rmy.service.ConsumableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 可消费的服务小鬼
 *
 * @author chu
 * @date 2021/12/24
 */
@Service
public class ConsumableServiceImp implements ConsumableService {

    @Autowired
    private ConsumablesDao consumablesDao;

    /**
     * 插入数据
     *
     * @param consumablesData 耗材数据
     * @return int
     */
    @Override
    public int insertData(ConsumablesDataDto consumablesData) {

        ConsumablesDataInfo info = getDataInfo(consumablesData);
        int rec = consumablesDao.insert(info);
        return rec == 1 ? 1 : 0;
    }

    /**
     * 得到固体状态
     *
     * @param unit
     * @return int
     */
    @Override
    public int getSolidStatus(String unit) {
        if (unit.equals("个")){
            return 1;
        }else {
            return 0;
        }
    }

    /**
     * 得到单位
     *
     * @param volUsed 使用卷
     * @return {@link String}
     */
    @Override
    public String getUnit(String volUsed) {
        if (volUsed.contains("ml")){
            return "ml";
        }else if(volUsed.contains("L")) {
            return "L";
        }else{
            return "个";
        }
    }

    /**
     * 得到使用量
     *
     * @param volUsed 使用卷
     * @return {@link Float}
     */
    @Override
    public Float getVol(String volUsed) {
        Float value;
        String regex = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(volUsed);
        while(matcher.find()){
            String valueStr = matcher.group().toString();
            value = Float.valueOf(valueStr);
            String unit = getUnit(volUsed);
            return unit.equals("L") ? value * 1000 : value;
        }
        return (float)0;
    }

    /**
     * 获取数据信息
     *
     * @param data 数据
     * @return {@link ConsumablesDataInfo}
     */
    @Override
    public ConsumablesDataInfo getDataInfo(ConsumablesDataDto data) {
        ConsumablesDataInfo info = new ConsumablesDataInfo();
        info.setInsId(data.getInsId());
        info.setConName(data.getConName());
        info.setVolUsed(getVol(data.getVolUsed()));
        info.setUnit(getUnit(data.getVolUsed()));
        info.setUpdateTime(data.getUpdateTime());
        info.setDateTime(data.getDateTime());
        info.setSolidState(getSolidStatus(info.getUnit()));

        return info;
    }

    /**
     * 得到所有耗材类型
     *
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getAllConsumType() {
        List<String> infolist = consumablesDao.getAlltype();
        if (infolist != null && infolist.size() > 0){
            return infolist;
        }else{
            return null;
        }
    }
}