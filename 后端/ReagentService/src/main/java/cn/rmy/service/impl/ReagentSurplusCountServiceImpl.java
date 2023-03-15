package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.dao.ReagentSurplusCountDao;
import cn.rmy.domain.*;
import cn.rmy.dto.ReagentSurplusCountPageDto;
import cn.rmy.service.ReagentSurplusCountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


@Service
public class ReagentSurplusCountServiceImpl extends ServiceImpl<ReagentSurplusCountDao, ReagentSurplusCount> implements ReagentSurplusCountService {

    @Autowired
    private ReagentSurplusCountDao reagentSurplusCountDao;



//    @Autowired
//    private RedisTemplate redisTemplate;


    /**
     * 统计该仪器上所使用的所有试剂总耗量
     * @param deviceId
     * @return
     */
    public int sumReagentUseNumByDeviceId(String deviceId){
        QueryWrapper<ReagentSurplusCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(reagent_use_num) as totalReagentUseNum")
                .eq("device_id",deviceId);
        Map<String, Object> map = this.getMap(queryWrapper);
        return (int) map.get("totalReagentUseNum");
    }

    /**
     * 用于试剂项目的下拉
     * @return
     */
    public List<String> sumReagentNum(){
        List<String> list = new ArrayList<>();
        List<Map<String,Object>> result = null;
        QueryWrapper<ReagentSurplusCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("reagent_num")
                .groupBy("reagent_num");
        result= reagentSurplusCountDao.selectMaps(queryWrapper);
        for(Map<String,Object> map :result){
            for(Map.Entry<String,Object> entry:map.entrySet()){
                String key = entry.getKey();
                if(key.equals("reagent_num")){
                    list.add(String.valueOf(entry.getValue()));
                }
            }
        }
//        Map<String, Object> map = this.getMap(queryWrapper);
//        map.get("reagent_num");
        return list;
    }



    //     ---------按试剂分类--------------
    public List<RsdDto> countForReagent01(int nearlyDay,List<String> deviceList){
        List<RsdDto> rsdDtos = reagentSurplusCountDao
                .countForReagent01(nearlyDay, deviceList);
        return rsdDtos;
    }
    public List<RsdDto> countForReagent02(Date beginTime,Date endTime,List<String> deviceList){
        List<RsdDto> rsdDtos = reagentSurplusCountDao
                .countForReagent02( beginTime,endTime, deviceList);
        return rsdDtos;
    }
    public List<RsdDto> countForReagent1(String reagentNum,int nearlyDay,List<String> deviceList){
        List<RsdDto> rsdDtos = reagentSurplusCountDao
                .countForReagent1(reagentNum, nearlyDay, deviceList);
        return rsdDtos;
    }
    public List<RsdDto> countForReagent2(String reagentNum,Date beginTime,Date endTime,List<String> deviceList){
        List<RsdDto> rsdDtos = reagentSurplusCountDao
                .countForReagent2(reagentNum, beginTime,endTime, deviceList);
        return rsdDtos;
    }
    public List<RsdDto> countForReagent31(String reagentNum,String deviceId,int nearlyDay,List<String> deviceList){
        List<RsdDto> rsdDtos = reagentSurplusCountDao
                .countForReagent31(reagentNum, deviceId,nearlyDay, deviceList);
        return rsdDtos;
    }
    public List<RsdDto> countForReagent32(String reagentNum,String deviceId,Date beginTime,Date endTime,List<String> deviceList){
        List<RsdDto> rsdDtos = reagentSurplusCountDao
                .countForReagent32(reagentNum, deviceId,beginTime,endTime, deviceList);
        return rsdDtos;
    }
    public List<RscDto> countForDevice1(String deviceId,int nearlyDay){
        List<RscDto> rscDtos = reagentSurplusCountDao.countForDevice1(deviceId, nearlyDay);
        return rscDtos;
    }

    public List<RscDto> countForDevice2(String deviceId, Date beginTime, Date endTime){
        List<RscDto> rscDtos = reagentSurplusCountDao.countForDevice2(deviceId, beginTime, endTime);
        return rscDtos;
    }



    public List<Map<String,Object>> countForReagent(String reagentNum,int nearlyDay,Date... time){
        List<Map<String, Object>> result = null;


        QueryWrapper<ReagentSurplusCount> qw = new QueryWrapper<>();
        qw.select("device_id ,sum(reagent_use_num) as use_num");
        QueryWrapper<ReagentSurplusCount> queryWrapper = new QueryWrapper<>();


        queryWrapper.select("device_id ,sum(reagent_use_num) as use_num");
        if(time.length == 0){ // 说明非自定义 1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
            if(1 == nearlyDay){
                queryWrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(get_time)");
            }else if(2 == nearlyDay){
                queryWrapper.apply("WEEK(CURDATE()) -1<= WEEK(get_time)");
            }else if(3 == nearlyDay){
                queryWrapper.apply("MONTH(CURDATE()) -1 <= MONTH(get_time)");
            }else if(4 == nearlyDay){
                queryWrapper.apply("MONTH(CURDATE()) -3 <= MONTH(get_time)");
            }

        }else{
            Date beginTime = time[0];
            Date endTime = time[1];
            queryWrapper.ge("get_time", beginTime);
            queryWrapper.le("get_time", endTime);

        }
        queryWrapper.eq("reagent_num",reagentNum);
        queryWrapper.groupBy("reagent_num").orderByDesc("use_num");
        result= reagentSurplusCountDao.selectMaps(queryWrapper);
        return result;
    }

    //     ---------按仪器分类--------------

    public List<Map<String,Object>> countForDeviceId(String deviceId,int nearlyDay,Date... time){
        List<Map<String, Object>> result = null;
        QueryWrapper<ReagentSurplusCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("device_id ,sum(reagent_use_num) as use_num");
        if(time.length == 0){ // 说明非自定义 1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
            if(1 == nearlyDay){
                queryWrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(get_time)");
            }else if(2 == nearlyDay){
                queryWrapper.apply("WEEK(CURDATE()) -1<= WEEK(get_time)");
            }else if(3 == nearlyDay){
                queryWrapper.apply("MONTH(CURDATE()) -1 <= MONTH(get_time)");
            }else if(4 == nearlyDay){
                queryWrapper.apply("MONTH(CURDATE()) -3 <= MONTH(get_time)");
            }

        }else{
            Date beginTime = time[0];
            Date endTime = time[1];
            queryWrapper.ge("get_time", beginTime);
            queryWrapper.le("get_time", endTime);
        }
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.groupBy("device_id").orderByDesc("use_num");
        result= reagentSurplusCountDao.selectMaps(queryWrapper);

        return result;
    }





    //     ---------查询操作--------------

    public ReagentSurplusCount selectByBoxIdAndDeviceId(String reagentBoxId,String deviceId){
        QueryWrapper<ReagentSurplusCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reagent_box_id",reagentBoxId);
        queryWrapper.eq("device_id",deviceId);
        ReagentSurplusCount result = reagentSurplusCountDao.selectOne(queryWrapper);
        return result;
    }






    /**
     * 查询所有物品
     *
     * @return
     */
    public CommonResult selectAll(){
        System.out.println("hello--- ");
        List<ReagentSurplusCount> reagentSurpluses = reagentSurplusCountDao.selectList(null);
        if (0 == reagentSurpluses.size()) {
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(reagentSurpluses);
    }

    /**
     * 根据ID查询物品
     *
     * @param reagentSurplusesId
     * @return
     */
    public CommonResult selectById(int reagentSurplusesId) {
//        String result = redisTemplate.get(String.valueOf(faultCodeId));
//        System.out.println("selectById result------" + result);
//        if(result.contains("error")){
        ReagentSurplusCount reagentSurplusesCount = reagentSurplusCountDao.selectById(reagentSurplusesId);
        if (CommonUtil.isNull(reagentSurplusesCount)) {
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        } else {
//                redisTemplate.set(String.valueOf(faultCodeId),faultCode);
            return CommonResult.success(reagentSurplusesCount);
        }
    }



    /**
     * 分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    public CommonResult selectByPage(int current, int size, Map<String,Object> conditionsMap) {
        System.out.println("xsc分页查询 begin");
        if(current<=0 || CommonUtil.isNull(current)){
            current = 1;
        }
        if(size<=0 || CommonUtil.isNull(size)){
            size = 4;
        }
        Page<ReagentSurplusCount> page = new Page<>(current,size);
        QueryWrapper<ReagentSurplusCount> wrapper = new QueryWrapper<>();
        if(conditionsMap.containsKey("deviceId")){
            wrapper.like("device_id",conditionsMap.get("deviceId"));
            conditionsMap.remove("deviceId");
        }
//        if(conditionsMap.containsKey("faultClass")){
//            wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//            conditionsMap.remove("faultClass");
//        }
        //  试剂盒号
        if(conditionsMap.containsKey("reagentBoxId")){
            wrapper.eq("reagent_box_id",conditionsMap.get("reagentBoxId"));
            conditionsMap.remove("reagentBoxId");
        }
        if (conditionsMap.containsKey("beginTime")) {
            //  如果只有申请日期查询，条件为大于等于这个日期
            //  如果有申请日期，还有归还日期，则筛选这两个日期间的
            Object value = conditionsMap.get("beginTime");
            if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                wrapper.ge("get_time", value);
            }
            conditionsMap.remove("beginTime");
        }
        if (conditionsMap.containsKey("endTime")) {
            Object value = conditionsMap.get("endTime");
            if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                wrapper.le("get_time", value);
            }
            conditionsMap.remove("endTime");
        }
        System.out.println("测试结束-----");
        reagentSurplusCountDao.selectPage(page, wrapper);
//        }
        List<ReagentSurplusCount> reagentSurpluses = page.getRecords();
        System.out.println("xsc分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==reagentSurpluses.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new ReagentSurplusCountPageDto(total,reagentSurpluses));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<ReagentSurplusCount> list = null;
        QueryWrapper<ReagentSurplusCount> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = reagentSurplusCountDao.selectList(null);
        }else {
            // 有条件
            if(conditionsMap.containsKey("deviceId")){
                wrapper.like("device_id",conditionsMap.get("deviceId"));
                conditionsMap.remove("deviceId");
            }
//        if(conditionsMap.containsKey("faultClass")){
//            wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//            conditionsMap.remove("faultClass");
//        }
            //  试剂盒号
            if(conditionsMap.containsKey("reagentBoxId")){
                wrapper.eq("reagent_box_id",conditionsMap.get("reagentBoxId"));
                conditionsMap.remove("reagentBoxId");
            }
            if (conditionsMap.containsKey("beginTime")) {
                //  如果只有申请日期查询，条件为大于等于这个日期
                //  如果有申请日期，还有归还日期，则筛选这两个日期间的
                Object value = conditionsMap.get("beginTime");
                if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                    wrapper.ge("get_time", value);
                }
                conditionsMap.remove("beginTime");
            }
            if (conditionsMap.containsKey("endTime")) {
                Object value = conditionsMap.get("endTime");
                if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                    wrapper.le("get_time", value);
                }
                conditionsMap.remove("endTime");
            }
            list = reagentSurplusCountDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.success();
        }else {
            return CommonResult.success(list);
        }
    }


//     ---------插入操作--------------

    /**
     * 故障码信息的录入
     * @param reagentSurplus
     * @return
     */
    public CommonResult insert(ReagentSurplusCount reagentSurplus) {
        System.out.println("故障码信息的录入 insert faultCode:" + reagentSurplus);
        if(CommonUtil.isNull(reagentSurplus)){
            return CommonResult.error();
//        }else if(CommonUtil.isNotNull(reagentSurplusDao.selectById(goods.getGoodsId()))){
//            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
            int rec = reagentSurplusCountDao.insert(reagentSurplus);
            if (rec == 1) {
//                redisTemplate.set(faultCode.getFaultCode(),faultCode);
                return CommonResult.success();
            }
            else
                return CommonResult.error();
        }

    }





//     ---------删除操作--------------

    /**
     * 根据ID删除试剂余量信息
     * @param reagentSurplusId
     * @return
     */
//    @LogAnno(operateType = "删除试剂余量信息")
    public CommonResult deletById(int reagentSurplusId) {
//        ReagentSurplus reagentSurplus = reagentSurplusDao.selectById(reagentSurplusId);
//        if(CommonUtil.isNull(faultCode)){
//            FaultCode redisFaultCode = JSONObject
//                    .parseObject(redisTemplate
//                            .get(String.valueOf(faultCode.getFaultCodeId())),FaultCode.class);
//            if(CommonUtil.isNotNull(redisFaultCode)){
//                redisTemplate.del(String.valueOf(faultcodeId));
//            }
//            return CommonResult.error(CommonResultEm.NOT_EXIST);
//        }
        int rec = reagentSurplusCountDao.deleteById(reagentSurplusId);
        if(rec == 1){
//            FaultCode redisFaultCode = JSONObject
//                    .parseObject(redisTemplate
//                            .get(String.valueOf(faultCode.getFaultCodeId())),FaultCode.class);
//            if(CommonUtil.isNotNull(redisFaultCode)){
//                redisTemplate.del(String.valueOf(faultcodeId));
//            }
            return CommonResult.success();
        }else
            return CommonResult.error();
    }

//     ---------更新操作  同时需要更新记录表--------------

    /**
     * 更新物品
     * @param reagentSurplusCount
     * @return
     */
//    @LogAnno(operateType = "修改试剂余量信息")
    public CommonResult update(ReagentSurplusCount reagentSurplusCount) {
        if (CommonUtil.isNull(reagentSurplusCount)) {
            return CommonResult.error();
        } else{
            ReagentSurplusCount newReagentSurplusCount = reagentSurplusCountDao.selectById(reagentSurplusCount.getReagentSurplusCountId());
            if (CommonUtil.isNull(newReagentSurplusCount)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                newReagentSurplusCount.setReagentSurplusCountId(reagentSurplusCount.getReagentSurplusCountId())
                        .setReagentBoxId(reagentSurplusCount.getReagentBoxId())
                        .setDeviceId(reagentSurplusCount.getDeviceId())
                        .setReagentUseNum(reagentSurplusCount.getReagentUseNum())
                        .setGetTime(reagentSurplusCount.getGetTime());
                int rec = reagentSurplusCountDao.updateById(newReagentSurplusCount);

                if (rec == 1) {
//                    redisTemplate.del(String.valueOf(faultCode.getFaultCodeId()));
//                    redisTemplate.set(String.valueOf(newFaultCode.getFaultCodeId()),newFaultCode);
//                    redisTemplate.del(faultCode.getFaultCode());
//                    redisTemplate.set(newFaultCode.getFaultCode(),newFaultCode);
                    return CommonResult.success();
                }
                else
                    return CommonResult.error();
            }

        }
    }

//    /**
//     * 批量更新
//     * @param goodsList
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public CommonResult updateBatch(List<FaultCode> goodsList){
//        if(0 == goodsList.size()){
//            return CommonResult.error();
//        }
//        Integer rec = reagentSurplusDao.updateBatchSomeColumn(goodsList);
//        if(rec == 0)
//            return CommonResult.error();
//        else
//            return CommonResult.success();
//    }


//     ---------导出报表--------------

    /**
     * 导出EXCEL报表   xsc
     * @param generateExcel
     * @return
     * @throws IOException
     * @throws ParseException
     */
//    @Transactional(rollbackFor = Exception.class)
//    public CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException {
//        // 1、首先根据查询查出所需信息（查询全部、条件查询）
//        // 2、然后将结果封装成List<T>后 再封装成GenerateExcel实体
//        // 3、调用service.generateExcel（。。。）
//        List<Goods> goodsList = generateExcel.getGoodsList();
//        System.out.println("[generateExcel]  goodsList---\t" + goodsList);
//        // goodsList 中的 createTime 和 updateTime 正常，后面转成JSON就不正常了
//        // 需添加注解 @JSONField(format = "yyyy-MM-dd HH:mm:ss")  或者 将数据转换成json对象时，使用JSON.toJSON
//        if (0 == goodsList.size()) {
//            return CommonResult.error();
//        }
//        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(goodsList));
//        System.out.println("[generateExcel]  jsonArray---\t" + jsonArray.toJSONString());
//
//        //        "../excelTest/","test.xls"
//        CommonUtil.JsonToExcel(jsonArray, generateExcel.getOutUrl(), generateExcel.getFileName());
//        return CommonResult.success();
//    }



}
