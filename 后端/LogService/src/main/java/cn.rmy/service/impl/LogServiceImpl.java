package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.faultManagement.Log;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.dao.LogDao;
import cn.rmy.dto.LogPageDto;
import cn.rmy.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService {

    @Autowired
    private LogDao loagDao;


//    @Autowired
//    private RedisTemplate redisTemplate;


    //     ---------查询操作--------------

    /**
     * 查询所有物品
     *
     * @return
     */
    public CommonResult selectAll() {
        System.out.println("hello--- ");
        List<Log> logs = loagDao.selectList(null);
        if (0 == logs.size()) {
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(logs);
    }

    /**
     * 根据ID查询
     *
     * @param logId
     * @return
     */
    public CommonResult selectById(int logId) {
//        String result = redisTemplate.get(String.valueOf(faultCodeId));
//        System.out.println("selectById result------" + result);
//        if(result.contains("error")){
        Log log = loagDao.selectById(logId);
        if (CommonUtil.isNull(log)) {
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        } else {
//                redisTemplate.set(String.valueOf(faultCodeId),faultCode);
            return CommonResult.success(log);
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
        Page<Log> page = new Page<>(current,size);
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        // 有条件
        if(conditionsMap.containsKey("operateor")){
            wrapper.like("operateor",conditionsMap.get("operateor"));
            conditionsMap.remove("operateor");
        }
//            if(conditionsMap.containsKey("faultClass")){
//                wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//                conditionsMap.remove("faultClass");
//            }
        if(conditionsMap.containsKey("operateType")){
            wrapper.like("operate_type",conditionsMap.get("operateType"));
            conditionsMap.remove("operateType");
        }
        /**
         * 下面的用于 是否会进行日期的范围条件查询，如果是的话  需要创建dto  起止时间传入，但是判断的都是operate_date
         * */
            if (conditionsMap.containsKey("beginTime")) {
                //  如果只有申请日期查询，条件为大于等于这个日期
                //  如果有申请日期，还有归还日期，则筛选这两个日期间的
                Object value = conditionsMap.get("beginTime");
                if(CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String)value))) {
                    wrapper.ge("operate_date", value);
                }
                conditionsMap.remove("beginTime");
            }
            if (conditionsMap.containsKey("endTime")) {
                Object value = conditionsMap.get("endTime");
                if(CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String)value))) {
                    wrapper.le("operate_date", value);
                }
                conditionsMap.remove("endTime");
            }
        if(conditionsMap.containsKey("operateResult")){
            wrapper.eq("operate_result",conditionsMap.get("operateResult"));
            conditionsMap.remove("operateResult");
        }
        wrapper.orderByDesc("operate_date");
        System.out.println("测试结束-----");
        loagDao.selectPage(page, wrapper);
//        }
        List<Log> logs = page.getRecords();
        System.out.println("xsc分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==logs.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new LogPageDto(total,logs));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<Log> list = null;
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = loagDao.selectList(null);
        }else {
            // 有条件
            if(conditionsMap.containsKey("operateor")){
                wrapper.like("operateor",conditionsMap.get("operateor"));
                conditionsMap.remove("operateor");
            }
//            if(conditionsMap.containsKey("faultClass")){
//                wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//                conditionsMap.remove("faultClass");
//            }
            if(conditionsMap.containsKey("operateType")){
                wrapper.like("operate_type",conditionsMap.get("operateType"));
                conditionsMap.remove("operateType");
            }
            /**
             * 下面的用于 是否会进行日期的范围条件查询，如果是的话  需要创建dto  起止时间传入，但是判断的都是operate_date
             * */
            if (conditionsMap.containsKey("beginTime")) {
                //  如果只有申请日期查询，条件为大于等于这个日期
                //  如果有申请日期，还有归还日期，则筛选这两个日期间的
                Object value = conditionsMap.get("beginTime");
                if(CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String)value))) {
                    wrapper.ge("operate_date", value);
                }
                conditionsMap.remove("beginTime");
            }
            if (conditionsMap.containsKey("endTime")) {
                Object value = conditionsMap.get("endTime");
                if(CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String)value))) {
                    wrapper.le("operate_date", value);
                }
                conditionsMap.remove("endTime");
            }
            if(conditionsMap.containsKey("operateResult")){
                wrapper.eq("operate_result",conditionsMap.get("operateResult"));
                conditionsMap.remove("operateResult");
            }
            wrapper.orderByDesc("operate_date");
            list = loagDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.success();
        }else {
            return CommonResult.success(list);
        }
    }


//     ---------插入操作--------------

    /**
     * 日志信息的录入
     * @param log
     * @return
     */
    public CommonResult insert(Log log) {
        System.out.println("日志信息的录入 insert log:" + log);
        if(CommonUtil.isNull(log)){
            return CommonResult.error();
//        }else if(CommonUtil.isNotNull(loagDao.selectById(goods.getGoodsId()))){
//            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
            Log newLog = new Log();
            //  暂定 自己编写
            newLog.setOperateor(log.getOperateor())
                    .setOperateType(log.getOperateType())
                    .setOperateDate(log.getOperateDate())
                    .setOperateResult(log.getOperateResult());
            int rec = loagDao.insert(newLog);
            if (rec == 1) {
//                redisTemplate.set(faultCode.getFaultCode(),faultCode);
                return CommonResult.success();
            }
            else
                return CommonResult.error();
        }

    }


    /**
     * 批量插入
     * @param goodsList
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
//    public CommonResult insertBatch(List<FaultCode> goodsList){
//        if(0 == goodsList.size()){
//            return CommonResult.error();
//        }
//        Integer rec = loagDao.insertBatchSomeColumn(goodsList);
//        if(rec == 0)
//            return CommonResult.error();
//        else
//            return CommonResult.success();
//    }



//     ---------删除操作--------------

    /**
     * 根据ID删除故障码
     * @param logId
     * @return
     */
    public CommonResult deletById(int logId) {
        Log log = loagDao.selectById(logId);
//        if(CommonUtil.isNull(faultCode)){
//            FaultCode redisFaultCode = JSONObject
//                    .parseObject(redisTemplate
//                            .get(String.valueOf(faultCode.getFaultCodeId())),FaultCode.class);
//            if(CommonUtil.isNotNull(redisFaultCode)){
//                redisTemplate.del(String.valueOf(faultcodeId));
//            }
//            return CommonResult.error(CommonResultEm.NOT_EXIST);
//        }
        int rec = loagDao.deleteById(logId);
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
     * @param faultCode
     * @return
     */
//    public CommonResult update(FaultCode faultCode) {
//        if (CommonUtil.isNull(faultCode)) {
//            return CommonResult.error();
//        } else{
//            FaultCode newFaultCode = loagDao.selectById(faultCode.getFaultCodeId());
//            if (CommonUtil.isNull(newFaultCode)) {
//                return CommonResult.error(CommonResultEm.NOT_EXIST);
//            } else {
//                newFaultCode.setFaultCodeId(faultCode.getFaultCodeId())
//                        .setFaultCode(faultCode.getFaultCode())
//                        .setFaultDescribe(faultCode.getFaultDescribe())
//                        .setFaultAdvice(faultCode.getFaultAdvice())
//                        .setFaultType(faultCode.getFaultType());
//                int rec = loagDao.updateById(newFaultCode);
//                //  同时要更新 faultRecord
//                Map<String,Object> map = new HashMap<>();
//                map.put("faultCode",faultCode.getFaultCode());
//                List<FaultRecord> list = JSONArray.parseArray(JSONArray
//                        .toJSONString(faultRecordService
//                                .selectByConditions(map).getObj())).toJavaList(FaultRecord.class);
//                if(0 != list.size()){
//                    for (FaultRecord faultRecord : list) {
//                        faultRecord.setFaultRecordId(faultRecord.getFaultRecordId())
//                                .setDeviceId(faultRecord.getDeviceId())
//                                .setFaultCode(faultCode.getFaultCode())
//                                .setFaultDescribe(faultCode.getFaultDescribe())
//                                .setFaultAdvice(faultCode.getFaultAdvice())
//                                .setFaultType(faultCode.getFaultType())
//                                .setModuleCode(faultRecord.getModuleCode())
//                                .setFaultClass(faultRecord.getFaultClass())
//                                .setFaultTime(faultRecord.getFaultTime())
//                                .setHandleStatus(faultRecord.getHandleStatus());
//                        faultRecordService.update(faultRecord);
//                    }
//                }
//                if (rec == 1) {
////                    redisTemplate.del(String.valueOf(faultCode.getFaultCodeId()));
////                    redisTemplate.set(String.valueOf(newFaultCode.getFaultCodeId()),newFaultCode);
////                    redisTemplate.del(faultCode.getFaultCode());
////                    redisTemplate.set(newFaultCode.getFaultCode(),newFaultCode);
//                    return CommonResult.success();
//                }
//                else
//                    return CommonResult.error();
//            }
//
//        }
//    }

    /**
     * 批量更新
     * @param goodsList
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
//    public CommonResult updateBatch(List<FaultCode> goodsList){
//        if(0 == goodsList.size()){
//            return CommonResult.error();
//        }
//        Integer rec = loagDao.updateBatchSomeColumn(goodsList);
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
