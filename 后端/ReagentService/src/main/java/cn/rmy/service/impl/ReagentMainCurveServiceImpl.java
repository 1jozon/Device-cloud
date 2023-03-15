package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.dao.ReagentMainCurveDao;
import cn.rmy.domain.ReagentMainCurve;
import cn.rmy.dto.ReagentMainCurvePageDto;
import cn.rmy.service.ReagentMainCurveService;
import cn.rmy.service.UsersService;
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
public class ReagentMainCurveServiceImpl extends ServiceImpl<ReagentMainCurveDao, ReagentMainCurve> implements ReagentMainCurveService {

    @Autowired
    private ReagentMainCurveDao reagentMainCurveDao;

    @Autowired
    private UsersService usersService;



    //     ---------查询操作--------------

    /**
     * 查询所有
     *
     * @return
     */
    public CommonResult selectAll(){
        System.out.println("hello--- ");
        List<ReagentMainCurve> reagentMainCurves = reagentMainCurveDao.selectList(null);
        if (0 == reagentMainCurves.size()) {
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(reagentMainCurves);
    }

    /**
     * 根据ID查询物品
     *
     * @param reagentMainCurveId
     * @return
     */
    public CommonResult selectById(int reagentMainCurveId) {
//        String result = redisTemplate.get(String.valueOf(faultCodeId));
//        System.out.println("selectById result------" + result);
//        if(result.contains("error")){
        ReagentMainCurve reagentMainCurve = reagentMainCurveDao.selectById(reagentMainCurveId);
        if (CommonUtil.isNull(reagentMainCurve)) {
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        } else {
//                redisTemplate.set(String.valueOf(faultCodeId),faultCode);
            return CommonResult.success(reagentMainCurve);
        }
    }


//    public ReagentMainCurve selectByReagentBoxId(String reagentBoxId){
//        QueryWrapper<ReagentSurplusInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("reagent_box_id",reagentBoxId);
//        ReagentSurplusInfo result = reagentSurplusInfoDao.selectOne(queryWrapper);
//        return result;
//    }


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
        if(!conditionsMap.containsKey("userId")){
            return CommonResult.error(CommonResultEm.ERROR_PARMAS_USERID_NOT_EXIST);
        }
        String userId = conditionsMap.get("userId").toString();
        conditionsMap.remove("userId");
        List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
        if (devicesList == null || devicesList.size() == 0){
            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
        }
        Page<ReagentMainCurve> page = new Page<>(current,size);
        QueryWrapper<ReagentMainCurve> wrapper = new QueryWrapper<>();

//        if(conditionsMap.containsKey("faultClass")){
//            wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//            conditionsMap.remove("faultClass");
//        }
        //  试剂批号
        if(conditionsMap.containsKey("reagentBatchId")){
            wrapper.like("reagent_batch_id",conditionsMap.get("reagentBatchId"));
            conditionsMap.remove("reagentBatchId");
        }
        if(conditionsMap.containsKey("deviceId")){
            wrapper.like("device_id",conditionsMap.get("deviceId"));
            conditionsMap.remove("deviceId");
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
        /**
         * 其他字段，不知道是否是查询条件，所以暂时没写
         */
        System.out.println("测试结束-----");
        wrapper.in("device_id",devicesList);
        reagentMainCurveDao.selectPage(page, wrapper);
//        }
        List<ReagentMainCurve> reagentMainCurves = page.getRecords();
        System.out.println("xsc分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==reagentMainCurves.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new ReagentMainCurvePageDto(total,reagentMainCurves));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<ReagentMainCurve> list = null;
        QueryWrapper<ReagentMainCurve> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = reagentMainCurveDao.selectList(null);
        }else {
            // 有条件
            //  试剂批号
            if(!conditionsMap.containsKey("userId")){
                return CommonResult.error(CommonResultEm.ERROR_PARMAS_USERID_NOT_EXIST);
            }
            String userId = conditionsMap.get("userId").toString();
            conditionsMap.remove("userId");
            List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
            if (devicesList == null || devicesList.size() == 0){
                return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
            }
            if(conditionsMap.containsKey("reagentBatchId")){
                wrapper.like("reagent_batch_id",conditionsMap.get("reagentBatchId"));
                conditionsMap.remove("reagentBatchId");
            }
            if(conditionsMap.containsKey("deviceId")){
                wrapper.like("device_id",conditionsMap.get("deviceId"));
                conditionsMap.remove("deviceId");
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
            /**
             * 其他字段，不知道是否是查询条件，所以暂时没写
             */
            wrapper.in("device_id",devicesList);
            list = reagentMainCurveDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.success();
        }else {
            return CommonResult.success(list);
        }
    }


//     ---------插入操作--------------

    /**
     * 试剂卡信息的录入
     * @param reagentMainCurve
     * @return
     */
    public CommonResult insert(ReagentMainCurve reagentMainCurve) {
        System.out.println("试剂卡信息的录入 insert reagentMainCurve:" + reagentMainCurve);
        if(CommonUtil.isNull(reagentMainCurve)){
            return CommonResult.error();
        }else{
            ReagentMainCurve reagentMainCurve1 = selectByBatchIdAndDeviceId(reagentMainCurve.getReagentBatchId(),
                    reagentMainCurve.getDeviceId());
            if(CommonUtil.isNull(reagentMainCurve1)) {
                int rec = reagentMainCurveDao.insert(reagentMainCurve);
                if (rec == 1) {
//                redisTemplate.set(faultCode.getFaultCode(),faultCode);
                    return CommonResult.success();
                } else
                    return CommonResult.error();
            }else {
//                reagentMainCurve.setReagentMainCurveId(reagentMainCurve1.getReagentMainCurveId());

//                ReagentMainCurve newReagentMainCurve = new ReagentMainCurve();
                reagentMainCurve1.setDeviceId(reagentMainCurve.getDeviceId())
                        .setReagentBatchId(reagentMainCurve.getReagentBatchId())
                        .setRlu1(reagentMainCurve.getRlu1())
                        .setRlu2(reagentMainCurve.getRlu2())
                        .setRlu3(reagentMainCurve.getRlu3())
                        .setRlu4(reagentMainCurve.getRlu4())
                        .setRlu5(reagentMainCurve.getRlu5())
                        .setRlu6(reagentMainCurve.getRlu6())
                        .setRlu7(reagentMainCurve.getRlu7())
                        .setRlu8(reagentMainCurve.getRlu8())
                        .setRlu9(reagentMainCurve.getRlu9())
                        .setRlu10(reagentMainCurve.getRlu10())
                        .setConc1(reagentMainCurve.getConc1())
                        .setConc2(reagentMainCurve.getConc2())
                        .setConc3(reagentMainCurve.getConc3())
                        .setConc4(reagentMainCurve.getConc4())
                        .setConc5(reagentMainCurve.getConc5())
                        .setConc6(reagentMainCurve.getConc6())
                        .setConc7(reagentMainCurve.getConc7())
                        .setConc8(reagentMainCurve.getConc8())
                        .setConc9(reagentMainCurve.getConc9())
                        .setConc10(reagentMainCurve.getConc10())
                        .setCurveType(reagentMainCurve.getCurveType())
                        .setParamA(reagentMainCurve.getParamA())
                        .setParamB(reagentMainCurve.getParamB())
                        .setParamC(reagentMainCurve.getParamC())
                        .setParamD(reagentMainCurve.getParamD())
                        .setParamE(reagentMainCurve.getParamE())
                        .setTotalNum(reagentMainCurve.getTotalNum())
                        .setCaliNum(reagentMainCurve.getCaliNum())
                        .setEffectiveTime(reagentMainCurve.getEffectiveTime())
                        .setNsb0(reagentMainCurve.getNsb0())
                        .setRluMax0(reagentMainCurve.getRluMax0())
                        .setProCon(reagentMainCurve.getProCon())
                        .setProRlu(reagentMainCurve.getProRlu())
                        .setDiluRatio(reagentMainCurve.getDiluRatio())
                        .setGetTime(reagentMainCurve.getGetTime());
                int rec = reagentMainCurveDao.updateById(reagentMainCurve1);

                if (rec == 1) {
                    return CommonResult.success();
                } else
                    return CommonResult.error(CommonResultEm.UPDATE_ERROR);
            }
        }
    }

    public ReagentMainCurve selectByBatchIdAndDeviceId(String reagentBatchId,String deviceId){
        QueryWrapper<ReagentMainCurve> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reagent_batch_id",reagentBatchId);
        queryWrapper.eq("device_id",deviceId);
        ReagentMainCurve result = reagentMainCurveDao.selectOne(queryWrapper);
        return result;
    }





//     ---------删除操作--------------

    /**
     * 根据ID删除试剂余量信息
     * @param reagentMainCurveId
     * @return
     */
//    @LogAnno(operateType = "删除试剂卡信息")
    public CommonResult deletById(int reagentMainCurveId) {
        ReagentMainCurve reagentMainCurve = reagentMainCurveDao.selectById(reagentMainCurveId);
        if(CommonUtil.isNull(reagentMainCurve)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }else {
            int rec = reagentMainCurveDao.deleteById(reagentMainCurveId);
            if (rec == 1) {
                return CommonResult.success();
            } else
                return CommonResult.error();
        }
    }

//     ---------更新操作  同时需要更新记录表--------------

    /**
     * 更新物品
     * @param reagentMainCurve
     * @return
     */
//    @LogAnno(operateType = "修改试剂卡信息")
    public CommonResult update(ReagentMainCurve reagentMainCurve) {
        if (CommonUtil.isNull(reagentMainCurve)) {
            return CommonResult.error();
        } else {
            ReagentMainCurve newReagentMainCurve = reagentMainCurveDao.selectById(reagentMainCurve.getReagentMainCurveId());
            if (CommonUtil.isNull(newReagentMainCurve)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                int rec = reagentMainCurveDao.updateById(reagentMainCurve);

                if (rec == 1) {
                    return CommonResult.success();
                } else
                    return CommonResult.error(CommonResultEm.UPDATE_ERROR);
//            }

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
