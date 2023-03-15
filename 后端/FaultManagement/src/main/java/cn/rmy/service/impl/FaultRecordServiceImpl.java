package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.dto.Users;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.faultManagement.FaultCode;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.beans.faultManagement.FaultRecordReq;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.dao.FaultRecordDao;
import cn.rmy.dto.*;
import cn.rmy.emailUtil.SendMailUtil;
import cn.rmy.service.*;
import cn.rmy.service.Impl.UserWithInstServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class FaultRecordServiceImpl extends ServiceImpl<FaultRecordDao, FaultRecord> implements FaultRecordService {

    @Autowired
    private FaultRecordDao faultRecordDao;

    @Autowired
    private FaultHandleService faultHandleService;

    @Autowired
    private FaultCodeService faultCodeService;

    @Autowired
    private InstrumentService instrumentService;


    @Autowired
    private UserWithInstServiceImpl userWithInstService;

    @Autowired
    private UsersService usersService;
//    @Autowired
//    private RedisTemplate redisTemplate;

    @Autowired
    private HttpSession session;

    //     ---------查询操作  都需要进行根据faultCode作为外键查询  多表模糊查询--------------
    /**
     * 查询所有物品
     * @return
     */
    public List<FaultRecord> selectAll(String faultCode) {
        QueryWrapper<FaultRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("fault_code",faultCode);
        List<FaultRecord> faultRecords =  faultRecordDao.selectList(queryWrapper);
        return faultRecords;
    }

    /**
     * 根据ID查询物品
     * @param faultRecordId
     * @return
     */
    public CommonResult selectById(int faultRecordId) {
//        String result = redisTemplate.get(String.valueOf(faultRecordId));
//        System.out.println("selectById result------" + result);
//        if(result.contains("error")){
            FaultRecord faultRecord = faultRecordDao.selectById(faultRecordId);
            if(CommonUtil.isNull(faultRecord)){
                return CommonResult.error(CommonResultEm.NOT_EXIST);
//            }else{
//                redisTemplate.set(String.valueOf(faultRecordId),faultRecord);
//                return CommonResult.success(faultRecord);
//            }
        }
        return CommonResult.success(faultRecord);
    }


//    public CommonResult selectByPage1(int current,int size,FaultRecord faultRecord) {
//        System.out.println("xsc分页查询  selectByPage1 begin");
//        if (current <= 0 || CommonUtil.isNull(current)) {
//            current = 1;
//        }
//        if (size <= 0 || CommonUtil.isNull(size)) {
//            size = 4;
//        }
//        Page<FaultRecord> page = new Page<>(current,size);
//
//        List<FaultRecordReq> resList = faultRecordDao.selectFaultRecordByPage(page,faultRecord);
//        System.out.println("xsc分页查询 selectByPage1   end");
//        long total = page.getTotal();
////        long current = page.getCurrent();
////        long size = page.getSize();
//        System.out.println(total + "--" + current + "--" + size);
//        if(0 ==resList.size()){
//            return CommonResult.success();
//        }
//        return CommonResult.success(new FaultRecordResPageDto(total,resList));
//    }


    /**
     * 分页查询所有   仪器ID、模块号、故障码、故障状态
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    public CommonResult selectByPage2(int current, int size, Map<String,Object> conditionsMap) {
        System.out.println("xsc分页查询 selectByPage2  begin");
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
        Page<FaultRecord> page = new Page<>(current,size);
        QueryWrapper<FaultRecord> wrapper = new QueryWrapper<>();
        // 有条件  仪器ID、模块号、故障码、故障状态
        if(conditionsMap.containsKey("deviceId")){
            wrapper.like("device_id",conditionsMap.get("deviceId"));
            conditionsMap.remove("deviceId");
        }
        if(conditionsMap.containsKey("faultCode")){
            wrapper.like("fault_code",conditionsMap.get("faultCode"));
            conditionsMap.remove("faultCode");
        }
        if (conditionsMap.containsKey("deviceType")) {
            wrapper.like("device_type", conditionsMap.get("deviceType"));
        }
        if(conditionsMap.containsKey("handleStatus")){
            wrapper.eq("handle_status",conditionsMap.get("handleStatus"));
            conditionsMap.remove("handleStatus");
        }
        if(conditionsMap.containsKey("moduleCode")){
            wrapper.eq("module_code",conditionsMap.get("moduleCode"));
            conditionsMap.remove("moduleCode");
        }
        if (conditionsMap.containsKey("beginTime")) {
            //  如果只有申请日期查询，条件为大于等于这个日期
            //  如果有申请日期，还有归还日期，则筛选这两个日期间的
            Object value = conditionsMap.get("beginTime");
            if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                wrapper.ge("fault_time", value);
            }
            conditionsMap.remove("beginTime");
        }
        if (conditionsMap.containsKey("endTime")) {
            Object value = conditionsMap.get("endTime");
            if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                wrapper.le("fault_time", value);
            }
            conditionsMap.remove("endTime");
        }
        System.out.println("测试开始-----");
        for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
            String key = entry.getKey();
            System.out.println("key:" + key);
            Object value = entry.getValue();
            System.out.println("value :" + value);
            if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String)value))) {
                wrapper.like(CommonUtil.camel2Underline(key), value);
            }
        }

//        if(conditionsMap.containsKey("faultCode")){
//            wrapper.eq("fault_code",conditionsMap.get("faultCode"));
//            conditionsMap.remove("faultCode");
//        }
//        if(conditionsMap.containsKey("faultDescribe")){
//            wrapper.like("fault_describe",conditionsMap.get("faultDescribe"));
//            conditionsMap.remove("faultDescribe");
//        }
        System.out.println("测试结束-----");
        wrapper.in("device_id",devicesList);
        wrapper.orderByDesc("fault_time");
        faultRecordDao.selectPage(page, wrapper);
//        }
        List<FaultRecord> faultRecords = page.getRecords();
        System.out.println("xsc分页查询  selectByPage2  end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==faultRecords.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new FaultRecordPageDto(total,faultRecords));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<FaultRecord> list = null;
        QueryWrapper<FaultRecord> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = faultRecordDao.selectList(null);
        }else {
            // 有条件  仪器ID、模块号、故障码、故障状态
            if(!conditionsMap.containsKey("userId")){
                return CommonResult.error(CommonResultEm.ERROR_PARMAS_USERID_NOT_EXIST);
            }
            String userId = conditionsMap.get("userId").toString();
            conditionsMap.remove("userId");
            List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
            if (devicesList == null || devicesList.size() == 0){
                return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
            }
            if(conditionsMap.containsKey("deviceId")){
                wrapper.like("device_id",conditionsMap.get("deviceId"));
                conditionsMap.remove("deviceId");
            }
            if(conditionsMap.containsKey("faultCode")){
                wrapper.like("fault_code",conditionsMap.get("faultCode"));
                conditionsMap.remove("faultCode");
            }
            if (conditionsMap.containsKey("deviceType")) {
                wrapper.like("device_type", conditionsMap.get("deviceType"));
            }
            if(conditionsMap.containsKey("handleStatus")){
                wrapper.eq("handle_status",conditionsMap.get("handleStatus"));
                conditionsMap.remove("handleStatus");
            }
            if(conditionsMap.containsKey("moduleCode")){
                wrapper.eq("module_code",conditionsMap.get("moduleCode"));
                conditionsMap.remove("moduleCode");
            }
            if (conditionsMap.containsKey("beginTime")) {
                //  如果只有申请日期查询，条件为大于等于这个日期
                //  如果有申请日期，还有归还日期，则筛选这两个日期间的
                Object value = conditionsMap.get("beginTime");
                if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                    wrapper.ge("fault_time", value);
                }
                conditionsMap.remove("beginTime");
            }
            if (conditionsMap.containsKey("endTime")) {
                Object value = conditionsMap.get("endTime");
                if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String) value))) {
                    wrapper.le("fault_time", value);
                }
                conditionsMap.remove("endTime");
            }
            System.out.println("测试开始-----");
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                System.out.println("key:" + key);
                Object value = entry.getValue();
                System.out.println("value :" + value);
                if (CommonUtil.isNotNull(value) && !value.equals("") && StringUtils.isNotBlank(((String)value))) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            wrapper.in("device_id",devicesList);
            wrapper.orderByDesc("fault_time");
            list = faultRecordDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.success();
        }else {
            return CommonResult.success(list);
        }
    }


    /**
     * 故障的条件统计   传入的字段有：   beginTime  endTime statisticalDemand（统计需求，1-需求1  2-需求2）
     *              需要定时？
     *
     * 故障统计：筛选条件（时间段、仪器名称、编号、状态）deviceId(仪器名称，编号)  deviceType（仪器类型） handleStatus
     * 需求1：统计时间段内各个仪器的故障数量，及总故障状态（已解决、未解决）的数量变化
     *          select device_id,count(*) as '故障数量'
     *          from tb_fault_record
     *          GROUP BY device_id
     *
     *
     *          select device_id,handle_status,count(*) as '故障数量'
     *          from tb_fault_record
     *          GROUP BY device_id,handle_status
     *
     * 需求2：统计时间段内所有仪器中各个故障码出现的故障数量变化
     *          select fault_code,count(*) as '故障数量'
     *          where fault_time >= '2021-08-31 14:12:45'
     *              from tb_fault_record
     *              group by fault_code
     */
    //     ---------按故障码分类--------------
    public List<DrfDto> countForFaultCode01(int nearlyDay,List<String> deviceList){
        List<DrfDto> drfDtos = faultRecordDao
                .countForFaultCode01(nearlyDay, deviceList);
        return drfDtos;
    }
    public List<DrfDto> countForFaultCode02(Date beginTime, Date endTime, List<String> deviceList){
        List<DrfDto> drfDtos = faultRecordDao
                .countForFaultCode02(beginTime,endTime, deviceList);
        return drfDtos;
    }
    public List<DrfDto> countForFaultCode1(String reagentNum,int nearlyDay,List<String> deviceList){
        List<DrfDto> drfDtos = faultRecordDao
                .countForFaultCode1(reagentNum, nearlyDay, deviceList);
        return drfDtos;
    }
    public List<DrfDto> countForFaultCode2(String reagentNum, Date beginTime, Date endTime, List<String> deviceList){
        List<DrfDto> drfDtos = faultRecordDao
                .countForFaultCode2(reagentNum, beginTime,endTime, deviceList);
        return drfDtos;
    }
    public List<DrfDto> countForFaultCode31(String reagentNum,String deviceId,int nearlyDay,List<String> deviceList){
        List<DrfDto> drfDtos = faultRecordDao
                .countForFaultCode31(reagentNum, deviceId,nearlyDay, deviceList);
        return drfDtos;
    }
    public List<DrfDto> countForFaultCode32(String reagentNum, String deviceId,Date beginTime, Date endTime, List<String> deviceList){
        List<DrfDto> drfDtos = faultRecordDao
                .countForFaultCode32(reagentNum, deviceId,beginTime,endTime, deviceList);
        return drfDtos;
    }

    public List<FrdDto> countForDevice1(String deviceId,int nearlyDay){
        List<FrdDto> frdDtos = faultRecordDao.countForDevice1(deviceId, nearlyDay);
        return frdDtos;
    }

    public List<FrdDto> countForDevice2(String deviceId, Date beginTime, Date endTime){
        List<FrdDto> frdDtos = faultRecordDao.countForDevice2(deviceId, beginTime, endTime);
        return frdDtos;
    }




//    public CommonResult countFaultByConditions(Map<String,Object> conditionsMap) {
//        System.out.println("故障的条件统计 begin");
////        List<Map<String, Object>> list = null;
//        QueryWrapper<FaultRecord> queryWrapper = new QueryWrapper<>();
//        //没有条件
//        // 有条件  故障统计：仪器ID  时间段  故障状态 这3个条件进行故障的统计
//        int statisticalDemand = Integer.parseInt(conditionsMap.get("statisticalDemand").toString());
//        int nearlyDay = -1;// 说明非自定义 1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
//        List<String> devicesList = usersService.getcurrentUserInsIdList(conditionsMap.get("userId"));
//        if (devicesList == null || devicesList.size() == 0){
//            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
//        }
//        Object beginTime = null,endTime = null;
//        if (conditionsMap.containsKey("beginTime")) {
//            //  如果只有申请日期查询，条件为大于等于这个日期
//            //  如果有申请日期，还有归还日期，则筛选这两个日期间的
//            beginTime = conditionsMap.get("beginTime");
//        }
//        if (conditionsMap.containsKey("endTime")) {
//            endTime = conditionsMap.get("endTime");
//        }
//        if (conditionsMap.containsKey("nearlyDay")) {
//            //  如果只有申请日期查询，条件为大于等于这个日期
//            //  如果有申请日期，还有归还日期，则筛选这两个日期间的
//            Object temp = conditionsMap.get("nearlyDay");
//            if(CommonUtil.isNull(temp)){
//                nearlyDay = 3;
//            }else {
//                int tempInt = Integer.parseInt(temp.toString());
//                nearlyDay = tempInt;
//            }
//        }
//        if (1 == statisticalDemand) {
//            // 需求1 统计时间段内各个仪器的故障数量 前5  并展示其未处理、已处理，及总故障状态（已解决、未解决）的数量变化
//            // 判断是否有条件 deviceId(仪器名称，编号)  deviceType（仪器类型）
//            /**
//             # 第一步：
//             select device_id,count(fault_code) as '故障数量'
//             from tb_fault_record
//             GROUP BY device_id
//             ORDER BY COUNT(fault_code) desc
//             */
//            queryWrapper.select("device_id ,count(fault_code) as fault_code_num");
//
//            if (conditionsMap.containsKey("deviceId")) {
//                queryWrapper.like("device_id", conditionsMap.get("deviceId"));
//            }
//            if (conditionsMap.containsKey("deviceType")) {
//                queryWrapper.like("device_type", conditionsMap.get("deviceType"));
//            }
//            if (conditionsMap.containsKey("handleStatus")) {
//                queryWrapper.like("handle_status", conditionsMap.get("handleStatus"));
//            }
//            if(1 == nearlyDay){
//                queryWrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(fault_time)");
//            }else if(2 == nearlyDay){
//                queryWrapper.apply("WEEK(CURDATE()) -1<= WEEK(fault_time)");
//            }else if(3 == nearlyDay){
//                queryWrapper.apply("MONTH(CURDATE()) -1 <= MONTH(fault_time)");
//            }else if(4 == nearlyDay){
//                queryWrapper.apply("MONTH(CURDATE()) -3 <= MONTH(fault_time)");
//            }else {
//                //时间段  faultTime
//                if (CommonUtil.isNotNull(beginTime) && !beginTime.equals("") && StringUtils.isNotBlank(((String) beginTime))) {
//                    queryWrapper.ge("fault_time", beginTime);
//                }
//                if (CommonUtil.isNotNull(endTime) && !endTime.equals("") && StringUtils.isNotBlank(((String) endTime))) {
//                    queryWrapper.le("fault_time", endTime);
//                }
//            }
//            /**3.27 只能看到自己的  业务begin*/
////            Object obj = session.getAttribute("userId");
////            User user = (User) obj;
////            List<Instrument> devices = userWithInstService.getInstsByUserId(user.getUserId());
////            queryWrapper.in("device_id",devices);
//            /** 3.27 只能看到自己的  业务end*/
//
//            queryWrapper.groupBy("device_id").orderByDesc("fault_code_num");
//            List<Map<String, Object>> mapList = faultRecordDao.selectMaps(queryWrapper);
//            int len = (mapList.size() <= 10) ? mapList.size() : 10;
//            // 下面用于自测
//            int n = 0;
//            List<FaultRecordCountDto> result = new ArrayList();
//            for (Map<String, Object> map : mapList) {
//                n++;
//                if (n <= len) {
//                    FaultRecordCountDto frcDto = new FaultRecordCountDto();
//                    for (Map.Entry<String, Object> entry : map.entrySet()) {
//                        String key = entry.getKey();
//                        if (key.equals("device_id")) {
//                            frcDto.setDeviceId(entry.getValue().toString());
//                        } //count(fault_code)
//                        if (key.equals("fault_code_num")) {
//                            frcDto.setFaultCountNum(Integer.parseInt(entry.getValue().toString()));
//                        }
//                    }
//                    result.add(frcDto);
//                }
//            }
//            System.out.println(result);
//            return CommonResult.success(mapList.subList(0,len));
//        } else {
//            // 需求2
//            queryWrapper.select("fault_code ,count(fault_code) as fault_code_num");
//            //时间段  faultTime
//            //时间段  faultTime
//            if (conditionsMap.containsKey("faultCode")) {
//                queryWrapper.like("fault_code", conditionsMap.get("faultCode"));
//            }
//            if(1 == nearlyDay){
//                queryWrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(fault_time)");
//            }else if(2 == nearlyDay){
//                queryWrapper.apply("WEEK(CURDATE()) -1<= WEEK(fault_time)");
//            }else if(3 == nearlyDay){
//                queryWrapper.apply("MONTH(CURDATE()) -1 <= MONTH(fault_time)");
//            }else if(4 == nearlyDay){
//                queryWrapper.apply("MONTH(CURDATE()) -3 <= MONTH(fault_time)");
//            }else {
//                if (CommonUtil.isNotNull(beginTime) && !beginTime.equals("") && StringUtils.isNotBlank(((String) beginTime))) {
//                    queryWrapper.ge("fault_time", beginTime);
//                }
//                if (CommonUtil.isNotNull(endTime) && !endTime.equals("") && StringUtils.isNotBlank(((String) endTime))) {
//                    queryWrapper.le("fault_time", endTime);
//                }
//            }
//            /**3.27 只能看到自己的  业务begin*/
////            Object obj = session.getAttribute("userId");
//            // 获取session  待整合后获取当前session域中的user对象
////            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
////            Object obj = requestAttributes.getAttribute("userId",RequestAttributes.SCOPE_REQUEST);
////            User user = (User) obj;
////            List<Instrument> devices = userWithInstService.getInstsByUserId(user.getUserId());
////            queryWrapper.in("device_id",devices);
//            /** 3.27 只能看到自己的  业务end*/
//            queryWrapper.groupBy("fault_code").orderByDesc("fault_code_num");
//            return CommonResult.success(faultRecordDao.selectMaps(queryWrapper));
//
//        }
//    }



    // 用于需求1、2 的第二步统计：各个仪器的总故障状态（已解决、未解决）的数量变化
    public CommonResult countFaultForHandleStatus(Map<String,Object> conditionsMap){
        /** 继续查询
         # 第二步  取前5
         select handle_status,COUNT(fault_code) as '故障数量'
         from tb_fault_record
         where device_id = '3'
         GROUP BY handle_status
         */

        Object beginTime = null,endTime = null;
        List<String> deviceIdList = new ArrayList<>();// 针对需求1
        List<String> faultCodeList = new ArrayList<>();// 针对需求2
        int nearlyDay = -1;// 说明非自定义 1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
        if (conditionsMap.containsKey("beginTime")) {
            //  如果只有申请日期查询，条件为大于等于这个日期
            //  如果有申请日期，还有归还日期，则筛选这两个日期间的
            beginTime = conditionsMap.get("beginTime");
        }
        if (conditionsMap.containsKey("endTime")) {
            endTime = conditionsMap.get("endTime");
        }
        if (conditionsMap.containsKey("nearlyDay")) {
            //  如果只有申请日期查询，条件为大于等于这个日期
            //  如果有申请日期，还有归还日期，则筛选这两个日期间的
            Object temp = conditionsMap.get("nearlyDay");
            if(CommonUtil.isNull(temp)){
                nearlyDay = 3;
            }else {
                int tempInt = Integer.parseInt(temp.toString());
                nearlyDay = tempInt;
            }
        }
        int countByDeviceOrFaultCode = Integer.parseInt(conditionsMap.get("countByDeviceOrFaultCode").toString());
        if (2 == countByDeviceOrFaultCode) {
            if (conditionsMap.containsKey("deviceIdList")) {
                deviceIdList = castList(conditionsMap.get("deviceIdList"), String.class);
            }
            List<FrcReturnDto> frcReturnDtoList = new ArrayList<>();
            for (String deviceId : deviceIdList) {
                QueryWrapper<FaultRecord> wrapper = new QueryWrapper<>();
                wrapper.select("handle_status,count(fault_code) as fault_code_num");
                if (1 == nearlyDay) {
                    wrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(fault_time)");
                } else if (2 == nearlyDay) {
                    wrapper.apply("WEEK(CURDATE()) -1<= WEEK(fault_time)");
                } else if (3 == nearlyDay) {
                    wrapper.apply("MONTH(CURDATE()) -1 <= MONTH(fault_time)");
                } else if (4 == nearlyDay) {
                    wrapper.apply("MONTH(CURDATE()) -3 <= MONTH(fault_time)");
                } else {
                    if (CommonUtil.isNotNull(beginTime) && !beginTime.equals("") && StringUtils.isNotBlank(((String) beginTime))) {
                        wrapper.ge("fault_time", beginTime);
                    }
                    if (CommonUtil.isNotNull(endTime) && !endTime.equals("") && StringUtils.isNotBlank(((String) endTime))) {
                        wrapper.le("fault_time", endTime);
                    }
                }
                wrapper.eq("device_id", deviceId).groupBy("handle_status");
                List<Map<String, Object>> maps = faultRecordDao.selectMaps(wrapper);
                FrcReturnDto frcReturnDto = new FrcReturnDto();
                List<FrcDto> frcDtoList = new ArrayList<>();
                for (Map<String, Object> map : maps) {
                    FrcDto frcDto = new FrcDto();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.equals("handle_status")) {
                            frcDto.setHandle_status(value.toString());
                        }
                        if (key.equals("fault_code_num")) {
                            frcDto.setFault_code_num(value);
                        }

                    }
                    frcDtoList.add(frcDto);
                }
                    frcReturnDto.setDeviceId(deviceId)
                            .setFrcDtoList(frcDtoList);
                    System.out.println("仪器" + deviceId + "的总故障状态（已解决、未解决）的数量变化：\t" + frcReturnDto);
                frcReturnDtoList.add(frcReturnDto);
            } // for循环结束
            return CommonResult.success(frcReturnDtoList);
        }else {
            if (conditionsMap.containsKey("faultCodeList")) {
                faultCodeList = castList(conditionsMap.get("faultCodeList"), String.class);
            }
            List<FrcReturnDto2> frcReturnDto2List = new ArrayList<>();
            for (String faultCode : faultCodeList) {
                FrcReturnDto2 frcReturnDto2 = new FrcReturnDto2();
                QueryWrapper<FaultRecord> wrapper = new QueryWrapper<>();
                wrapper.select("handle_status,count(fault_code) as fault_code_num");
                if (1 == nearlyDay) {
                    wrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(fault_time)");
                } else if (2 == nearlyDay) {
                    wrapper.apply("WEEK(CURDATE()) -1<= WEEK(fault_time)");
                } else if (3 == nearlyDay) {
                    wrapper.apply("MONTH(CURDATE()) -1 <= MONTH(fault_time)");
                } else if (4 == nearlyDay) {
                    wrapper.apply("MONTH(CURDATE()) -3 <= MONTH(fault_time)");
                } else {
                    if (CommonUtil.isNotNull(beginTime) && !beginTime.equals("") && StringUtils.isNotBlank(((String) beginTime))) {
                        wrapper.ge("fault_time", beginTime);
                    }
                    if (CommonUtil.isNotNull(endTime) && !endTime.equals("") && StringUtils.isNotBlank(((String) endTime))) {
                        wrapper.le("fault_time", endTime);
                    }
                }
                wrapper.eq("fault_code", faultCode).groupBy("handle_status");
                List<Map<String, Object>> maps = faultRecordDao.selectMaps(wrapper);
//                System.out.println("maps:>>>>>>>>>>>" + maps);
                List<FrcDto> frcDtoList = new ArrayList<>();
                for (Map<String, Object> map : maps) {
                    FrcDto frcDto = new FrcDto();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
//                        System.out.println("key:>>>" + key + "  value:>>>" + value);
                        if (key.equals("handle_status")) {
                            frcDto.setHandle_status(value.toString());
                        }
                        if (key.equals("fault_code_num")) {
                            frcDto.setFault_code_num(value.toString());
                        }
                    }
//                    System.out.println("frcDto:>>>" + frcDto);
                    frcDtoList.add(frcDto);
                }
                    frcReturnDto2.setFaultCode(faultCode)
                            .setFrcDtoList(frcDtoList);
                    System.out.println("故障码" + faultCode + "的总故障状态（已解决、未解决）的数量变化：\t" + frcReturnDto2);
                frcReturnDto2List.add(frcReturnDto2);
            } // for循环结束
            return CommonResult.success(frcReturnDto2List);
        }
    }

//        if(1 == statisticalDemand) {
//            // 需求1
//            queryWrapper.groupBy("device_id,handle_status");
//
//        }else{
//            // 需求2
//            queryWrapper.groupBy("fault_code").orderByDesc("count(fault_code)");
//
//        }

//     ---------插入操作--------------

    /**
     * 故障信息的录入 —— 注意需要
     * @param faultRecordReq
     * @return
     */
    @Override
    public void insert(FaultRecordReq faultRecordReq) {
        System.out.println("故障信息的录入 insert faultCode:" + faultRecordReq);
        if(faultRecordReq!=null){
            FaultRecord newFaultRecord = new FaultRecord();

            // 根据faultCode查询
            FaultCode faultCode = faultCodeService.selectByFaultCode(faultRecordReq.getFaultCode());
            if(faultCode == null){ //  如果故障码不存在就要添加故障码
                faultCode = new FaultCode();
                faultCode.setFaultCode(faultRecordReq.getFaultCode())
                        .setFaultAdvice("默认值，请修改")
                        .setFaultDescribe("默认值，请修改");
                faultCodeService.insert(faultCode);
            }

            Instrument instrument = instrumentService.getOneByInstrumentId(faultRecordReq.getDeviceId());
            System.out.println("instrument 信息: " + instrument);
            String deviceype = instrument.getInstrumentModel();
            if(deviceype.equals("") || deviceype == null)
                deviceype = "默认值，请修改";
            /**
             * 缺 业务逻辑 如果faultCode为  null  需要插入新的faultCode
             */
            //  暂定 自己编写
            newFaultRecord.setDeviceId(faultRecordReq.getDeviceId())
                    .setFaultCode(faultRecordReq.getFaultCode())
                    .setFaultDescribe(faultCode.getFaultDescribe())
//                    .setFaultType(faultCode.getFaultType())
                    .setFaultAdvice(faultCode.getFaultAdvice())
                    .setModuleCode(faultRecordReq.getModuleCode())
                    .setFaultClass(faultRecordReq.getFaultClass())
                    .setFaultTime(faultRecordReq.getFaultTime())
                    .setHandleStatus("未处理")
                    .setDeviceType(deviceype);

            faultRecordDao.insert(newFaultRecord);
            instrumentService.setFaultStatus(faultRecordReq.getDeviceId(),-1);
        }

    }





//     ---------删除操作--------------

    /**
     * 根据ID删除故障记录
     * @param faultRecodeId
     * @return
     */
    public CommonResult deletById(int faultRecodeId) {
        FaultRecord faultRecord = faultRecordDao.selectById(faultRecodeId);
        if(CommonUtil.isNull(faultRecord)){
//            FaultRecord redisFaultCode = JSONObject
//                    .parseObject(redisTemplate
//                            .get(String.valueOf(faultRecord.getFaultRecordId())),FaultRecord.class);
//            if(CommonUtil.isNotNull(redisFaultCode)){
//                redisTemplate.del(String.valueOf(faultcodeId));
//            }
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = faultRecordDao.deleteById(faultRecodeId);
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

//     ---------更新操作--------------

    /**
     * 更新故障
     * @param faultRecord
     * @return
     */
    public CommonResult update(FaultRecord faultRecord) {
        if (CommonUtil.isNull(faultRecord)) {
            return CommonResult.error();
        } else{

            int rec = faultRecordDao.updateById(faultRecord);
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

    /**
     * 批量更新
     * @param faultRecordList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateBatch(List<FaultRecord> faultRecordList){
        if(0 == faultRecordList.size()){
            return CommonResult.error();
        }
        int rec = faultRecordDao.updateBatchSomeColumn(faultRecordList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }


//     ---------导出报表--------------


    // 出现故障后 将其故障信息发送给负责的该仪器的人
    public CommonResult emailSend(int faultRecordId) throws ParseException {
        FaultRecord faultRecord = faultRecordDao.selectById(faultRecordId);
        SimpleDateFormat ft = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date faultTimeTemp = ft.parse(faultRecord.getFaultTime().toString());
        String faultTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(faultTimeTemp);
        System.out.println("faultTime" + faultTime);

        MailDto mail = new MailDto();
        mail.setSubject("故障处理通知");
        String content = "您好：\n"
                + "您所负责的编号为" + faultRecord.getDeviceId() + "，类型为："+faultRecord.getDeviceType()
                + "的仪器出现故障，故障信息为：\n"
                + "故障码：" + faultRecord.getFaultCode() + ",故障描述：" + faultRecord.getFaultDescribe()
                + ",故障建议：" + faultRecord.getFaultAdvice()
                + ",故障时间：" + faultTime + ",处理状态：" + faultRecord.getHandleStatus() + "\n"
                + "请您及时处理!";
        mail.setContent(content);
        //收件人
        List<UserT> list = userWithInstService.getUsersByInstId(faultRecord.getDeviceId());
        System.out.println("list:---"+ list);
        if (list.isEmpty()){
            return CommonResult.error();
        }
        int i = 0;
        String[] emails = new String[list.size()];
        for (UserT user : list){
            Users users = usersService.getUserById(user.getUserId());
            if (CommonUtil.isNull(users)){
                continue;
            }
            String email = users.getUserEmail();
            if (StringUtils.isBlank(email)|| CommonUtil.isNull(email)){
                continue;
            }else{
                emails[i++] = email;
            }
        }
        if(emails.length == 0)
            return CommonResult.error(CommonResultEm.ERROR,"负责人为空");

        mail.setTos(emails);

        //发送邮件
        SendMailUtil.send(mail);

        return CommonResult.success();
    }


    // 用于 Obj 转成 List
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }



}
