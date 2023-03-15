package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.faultManagement.FaultCode;
import cn.rmy.common.beans.faultManagement.FaultHandle;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.dao.FaultCodeDao;
import cn.rmy.dto.FaultCodePageDto;
import cn.rmy.dto.FaultCodeReq;
import cn.rmy.service.FaultCodeService;
import cn.rmy.service.FaultHandleService;
import cn.rmy.service.FaultRecordService;
import cn.rmy.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@Service
public class FaultCodeServiceImpl extends ServiceImpl<FaultCodeDao, FaultCode> implements FaultCodeService {

    @Autowired
    private FaultCodeDao faultCodeDao;

    @Autowired
    private FaultRecordService faultRecordService;

    @Autowired
    private FaultHandleService faultHandleService;

    @Autowired
    private UsersService usersService;


//    @Autowired
//    private RedisTemplate redisTemplate;


    //     ---------查询操作--------------

    /**
     * 查询所有物品
     *
     * @return
     */
    public List<FaultCode> selectAll(String faultCode) {
        QueryWrapper<FaultCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("fault_code",faultCode);
        List<FaultCode> faultCodes =  faultCodeDao.selectList(queryWrapper);
        return faultCodes;
    }

    public List<FaultCode> selectAll(String faultCode,String userId) {
//        List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
//        if (devicesList == null || devicesList.size() == 0){
//            return null;
//        }
        QueryWrapper<FaultCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("fault_code",faultCode);
//        queryWrapper.in("device_id",devicesList);
        List<FaultCode> faultCodes =  faultCodeDao.selectList(queryWrapper);
        return faultCodes;
    }

    /**
     * 根据ID查询物品
     *
     * @param faultCodeId
     * @return
     */
    public CommonResult selectById(int faultCodeId) {
//        String result = redisTemplate.get(String.valueOf(faultCodeId));
//        System.out.println("selectById result------" + result);
//        if(result.contains("error")){
        FaultCode faultCode = faultCodeDao.selectById(faultCodeId);
        if (CommonUtil.isNull(faultCode)) {
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        } else {
//                redisTemplate.set(String.valueOf(faultCodeId),faultCode);
            return CommonResult.success(faultCode);
        }
    }
    /**
     * 根据faultCode查询物品
     * @param faultCode 错误码
     * @return
     */
    public FaultCode selectByFaultCode(String faultCode) {
        FaultCode faultCode1 = new FaultCode();
        if(StringUtils.isBlank(faultCode)){
            return faultCode1;
        }
//        String result = redisTemplate.get(String.valueOf(faultCode));
//        System.out.println("selectById result------" + result);
//        if(result.contains("error")) {
            QueryWrapper<FaultCode> wrapper = new QueryWrapper<>();
            wrapper.eq("fault_code",faultCode);

            faultCode1 = faultCodeDao.selectOne(wrapper);
            return faultCode1;
        }
//        return CommonResult.success();
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
//        if(!conditionsMap.containsKey("userId")){
//            return CommonResult.error(CommonResultEm.ERROR_PARMAS_USERID_NOT_EXIST);
//        }
//        String userId = conditionsMap.get("userId").toString();
//        List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
//        if (devicesList == null || devicesList.size() == 0){
//            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
//        }
        Page<FaultCode> page = new Page<>(current,size);
        QueryWrapper<FaultCode> wrapper = new QueryWrapper<>();

        if(conditionsMap.containsKey("faultCode")){
            wrapper.like("fault_code",conditionsMap.get("faultCode"));
            conditionsMap.remove("faultCode");
        }
//        if(conditionsMap.containsKey("faultClass")){
//            wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//            conditionsMap.remove("faultClass");
//        }
//        if(conditionsMap.containsKey("faultType")){
//            wrapper.like("fault_type",conditionsMap.get("faultType"));
//            conditionsMap.remove("faultType");
//        }
        if(conditionsMap.containsKey("faultDescribe")){
            wrapper.like("fault_describe",conditionsMap.get("faultDescribe"));
            conditionsMap.remove("faultDescribe");
        }
        if(conditionsMap.containsKey("faultAdvice")){
            wrapper.like("fault_advice",conditionsMap.get("faultAdvice"));
            conditionsMap.remove("faultAdvice");
        }
//        wrapper.in("device_id",devicesList);
        wrapper.orderByDesc("create_time");
        System.out.println("测试结束-----");
        faultCodeDao.selectPage(page, wrapper);
//        }
        List<FaultCode> faultCodes = page.getRecords();
        System.out.println("xsc分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==faultCodes.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new FaultCodePageDto(total,faultCodes));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<FaultCode> list = null;
        QueryWrapper<FaultCode> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = faultCodeDao.selectList(null);
        }else {
            // 有条件
//            if(!conditionsMap.containsKey("userId")){
//                return CommonResult.error(CommonResultEm.ERROR_PARMAS_USERID_NOT_EXIST);
//            }
//            String userId = conditionsMap.get("userId").toString();
//            List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
//            if (devicesList == null || devicesList.size() == 0){
//                return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
//            }
            if(conditionsMap.containsKey("faultCode")){
                wrapper.like("fault_code",conditionsMap.get("faultCode"));
                conditionsMap.remove("faultCode");
            }
//            if(conditionsMap.containsKey("faultClass")){
//                wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//                conditionsMap.remove("faultClass");
//            }
//            if(conditionsMap.containsKey("faultType")){
//                wrapper.eq("fault_type",conditionsMap.get("faultType"));
//                conditionsMap.remove("faultType");
//            }
            if(conditionsMap.containsKey("faultDescribe")){
                wrapper.like("fault_describe",conditionsMap.get("faultDescribe"));
                conditionsMap.remove("faultDescribe");
            }
            if(conditionsMap.containsKey("faultAdvice")){
                wrapper.like("fault_advice",conditionsMap.get("faultAdvice"));
                conditionsMap.remove("faultAdvice");
            }
//            wrapper.in("device_id",devicesList);
            wrapper.orderByDesc("create_time");
            list = faultCodeDao.selectList(wrapper);
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
     * @param faultCode
     * @return
     */
    public CommonResult insert(FaultCode faultCode) {
        System.out.println("故障码信息的录入 insert faultCode:" + faultCode);
        if(CommonUtil.isNull(faultCode)){
            return CommonResult.error(CommonResultEm.REQ_PARAM_IS_ERROR);
        }else if(CommonUtil.isNotNull(selectByFaultCode(faultCode.getFaultCode()))){
            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
            FaultCode newFaultCode = new FaultCode();
            //  暂定 自己编写
            newFaultCode.setFaultCode(faultCode.getFaultCode())
                    .setFaultDescribe(faultCode.getFaultDescribe())
                    .setFaultAdvice(faultCode.getFaultAdvice());
//                    .setFaultType(faultCode.getFaultType());
            int rec = faultCodeDao.insert(newFaultCode);
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
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertBatch(List<FaultCode> goodsList){
        if(0 == goodsList.size()){
            return CommonResult.error();
        }
        Integer rec = faultCodeDao.insertBatchSomeColumn(goodsList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除故障码
     * @param faultcodeId
     * @return
     */
    @LogAnno(operateType = "删除故障码")
    public CommonResult deletById(int faultcodeId) {
        FaultCode faultCode = faultCodeDao.selectById(faultcodeId);
//        if(CommonUtil.isNull(faultCode)){
//            FaultCode redisFaultCode = JSONObject
//                    .parseObject(redisTemplate
//                            .get(String.valueOf(faultCode.getFaultCodeId())),FaultCode.class);
//            if(CommonUtil.isNotNull(redisFaultCode)){
//                redisTemplate.del(String.valueOf(faultcodeId));
//            }
//            return CommonResult.error(CommonResultEm.NOT_EXIST);
//        }
        int rec = faultCodeDao.deleteById(faultcodeId);
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
    @LogAnno(operateType = "修改故障码")
    public CommonResult update(FaultCode faultCode){
        //  修改故障码，同时修改记录表、处理表中的数据
        int rec = faultCodeDao.updateById(faultCode);
        if(rec == 1){
            updateRecode(faultCode);
            return CommonResult.success();
        }else
            return CommonResult.error();
    }

    public void updateRecode(FaultCode faultCode){
        //  同时要更新 faultRecord
        System.out.println("faultCode:>>>>>>>>>>" + faultCode);
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("faultCode", faultCode.getFaultCode());
        List<FaultRecord> faultRecordList = faultRecordService.selectAll(faultCode.getFaultCode());

        System.out.println("FaultRecordList:>>>>>>>>>>" + faultRecordList);
//        List<FaultRecord> updateRecodeList = new ArrayList<>();
        if (!faultRecordList.isEmpty()) {
            for (FaultRecord faultRecord : faultRecordList) {
//                FaultRecord newFaultRecord = new FaultRecord();
                faultRecord.setFaultCode(faultCode.getFaultCode())
                        .setFaultDescribe(faultCode.getFaultDescribe())
                        .setFaultAdvice(faultCode.getFaultAdvice());
                System.out.println("faultRecord:>>>>>>>>>>" + faultRecord);
                faultRecordService.update(faultRecord);
//                updateRecodeList.add(faultRecord);
//                faultRecordService.update(faultRecord);
            }
//            faultRecordService.updateBatch(updateRecodeList);
        }
        // 同时更新故障处理表
        List<FaultHandle> faultHandleList = faultHandleService.selectAll(faultCode.getFaultCode());

//        List<FaultHandle> updateHandleList = new ArrayList<>();
        if (!faultHandleList.isEmpty()) {
            for (FaultHandle faultHandle : faultHandleList) {
//                FaultHandle newFaultHandle = new FaultHandle();
                faultHandle.setFaultCode(faultCode.getFaultCode())
                        .setFaultDescribe(faultCode.getFaultDescribe())
                        .setFaultAdvice(faultCode.getFaultAdvice());
                faultHandleService.update(faultHandle);
//                updateHandleList.add(faultHandle);
//                faultHandleService.update(faultHandle);
            }
//            faultHandleService.updateBatch(updateHandleList);
        }
    }


//    该业务添加逻辑：故障码可以修改，如果不可以修改，则采用上面的方法，如果可以修改，则采用下面方法
    public CommonResult updateForRecorde(FaultCodeReq faultCodeReq) {
        if (CommonUtil.isNull(faultCodeReq)) {
            return CommonResult.error();
        } else{
            String oldFaultCode = faultCodeReq.getOldFaultCode();
            String newFaultCode = faultCodeReq.getFaultCode().getFaultCode();
            if(!oldFaultCode.equals(newFaultCode)){
                FaultCode faultCode = selectByFaultCode(newFaultCode); // 查询已更新的故障码是否已经存在
                if(CommonUtil.isNotNull(faultCode)){
                    return CommonResult.error(CommonResultEm.ALREADY_EXIST); //说明要更新的故障码code已经存在
                }
            }

            int rec = faultCodeDao.updateById(faultCodeReq.getFaultCode());
            //  同时要更新 faultRecord
            updateRecode(faultCodeReq.getFaultCode());
//            Map<String, Object> map = new HashMap<>();
//            map.put("faultCode", faultCodeReq.getOldFaultCode());
//            List<FaultRecord> list = JSONArray.parseArray(JSONArray
//                    .toJSONString(faultRecordService
//                            .selectByConditions(map).getObj())).toJavaList(FaultRecord.class);
//            if (!list.isEmpty()) {
//                for (FaultRecord faultRecord : list) {
//                    faultRecord.setFaultRecordId(faultRecord.getFaultRecordId())
//                            .setDeviceId(faultRecord.getDeviceId())
//                            .setDeviceType(faultRecord.getDeviceType())
//                            .setFaultCode(faultCodeReq.getFaultCode().getFaultCode())
//                            .setFaultDescribe(faultCodeReq.getFaultCode().getFaultDescribe())
//                            .setFaultAdvice(faultCodeReq.getFaultCode().getFaultAdvice())
////                            .setFaultType(faultCodeReq.getFaultCode().getFaultType())
//                            .setModuleCode(faultRecord.getModuleCode())
//                            .setFaultClass(faultRecord.getFaultClass())
//                            .setFaultTime(faultRecord.getFaultTime())
//                            .setHandleStatus(faultRecord.getHandleStatus());
//                    faultRecordService.update(faultRecord);
//                }
//            }
            if (rec == 1) {
//                    redisTemplate.del(String.valueOf(faultCode.getFaultCodeId()));
//                    redisTemplate.set(String.valueOf(newFaultCode.getFaultCodeId()),newFaultCode);
//                    redisTemplate.del(faultCode.getFaultCode());
//                    redisTemplate.set(newFaultCode.getFaultCode(),newFaultCode);
                return CommonResult.success();
            } else
                return CommonResult.error();
                }
            }

//        }

    /**
     * 批量更新
     * @param goodsList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateBatch(List<FaultCode> goodsList){
        if(0 == goodsList.size()){
            return CommonResult.error();
        }
        Integer rec = faultCodeDao.updateBatchSomeColumn(goodsList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }


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
