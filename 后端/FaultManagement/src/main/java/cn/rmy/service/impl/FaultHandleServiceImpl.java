package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.faultManagement.FaultHandle;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.common.dto.Users;
import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.dao.FaultHandleDao;
import cn.rmy.dao.FaultRecordDao;
import cn.rmy.dto.FaultHandlePageDto;
import cn.rmy.emailUtil.SendMailUtil;
import cn.rmy.service.FaultCodeService;
import cn.rmy.service.FaultHandleService;
import cn.rmy.service.Impl.UserWithInstServiceImpl;
import cn.rmy.service.InstrumentService;
import cn.rmy.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Service
public class FaultHandleServiceImpl extends ServiceImpl<FaultHandleDao, FaultHandle> implements FaultHandleService {

    @Autowired
    private FaultCodeService faultCodeService;

    @Autowired
    private FaultHandleDao faultHandleDao;

    @Autowired
    private FaultRecordDao faultRecordDao;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserWithInstServiceImpl userWithInstService;

//    @Autowired
//    private RedisTemplate redisTemplate;


    //     ---------查询操作  都需要进行根据faultCode作为外键查询  多表模糊查询--------------
    /**
     * 查询所有物品
     * @return
     */
    public List<FaultHandle> selectAll(String faultCode) {
        QueryWrapper<FaultHandle> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("fault_code",faultCode);
        List<FaultHandle> faultHandles =  faultHandleDao.selectList(queryWrapper);
        return faultHandles;
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
        QueryWrapper<FaultHandle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fault_record_id",faultRecordId);

        FaultHandle faultHandle = faultHandleDao.selectOne(queryWrapper);
        System.out.println("faultHandle ==========" + faultHandle);
        return CommonResult.success(faultHandle);
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
     * 分页查询所有
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
        Page<FaultHandle> page = new Page<>(current,size);
        QueryWrapper<FaultHandle> wrapper = new QueryWrapper<>();

        if(conditionsMap.containsKey("faultClass")){
            wrapper.like("fault_class",conditionsMap.get("faultClass"));
            conditionsMap.remove("faultClass");
        }
        if(conditionsMap.containsKey("handleStatus")){
            wrapper.eq("handle_status",conditionsMap.get("handleStatus"));
            conditionsMap.remove("handleStatus");
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

        System.out.println("测试结束-----");
        wrapper.in("device_id",devicesList);
        wrapper.orderByDesc("fault_time");
        faultHandleDao.selectPage(page, wrapper);
//        }
        List<FaultHandle> faultHandles = page.getRecords();
        System.out.println("xsc分页查询  selectByPage2  end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==faultHandles.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new FaultHandlePageDto(total,faultHandles));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<FaultHandle> list = null;
        QueryWrapper<FaultHandle> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = faultHandleDao.selectList(null);
        }else {
            // 有条件
            if(!conditionsMap.containsKey("userId")){
                return CommonResult.error(CommonResultEm.ERROR_PARMAS_USERID_NOT_EXIST);
            }
            String userId = conditionsMap.get("userId").toString();
            conditionsMap.remove("userId");
            List<String> devicesList = usersService.getcurrentUserInsIdList(userId);
            if (devicesList == null || devicesList.size() == 0){
                return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
            }
            if(conditionsMap.containsKey("handleStatus")){
                wrapper.eq("handle_status",conditionsMap.get("handleStatus"));
                conditionsMap.remove("handleStatus");
            }
            if(conditionsMap.containsKey("faultClass")){
                wrapper.like("fault_class",conditionsMap.get("faultClass"));
                conditionsMap.remove("faultClass");
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
            wrapper.in("deivce_id",devicesList);
            wrapper.orderByDesc("fault_time");
            list = faultHandleDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.success();
        }else {
            return CommonResult.success(list);
        }
    }


//     ---------插入操作--------------

    /**
     * 故障信息的录入 —— 注意需要
     * @param faultHandle
     * @return
     */
    public CommonResult insert(FaultHandle faultHandle) {
        System.out.println("物品信息的录入 insert faultCode:" + faultHandle);
        if(CommonUtil.isNull(faultHandle)){
            return CommonResult.error();
//        }else if(CommonUtil.isNotNull(faultCodeDao.selectById(goods.getGoodsId()))){
//            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
//            FaultHandle newFaultHandle = new FaultHandle();

            Instrument instrument = instrumentService.getOneByInstrumentId(faultHandle.getDeviceId());
            faultHandle.setDeviceType(instrument.getInstrumentModel());

            // 根据faultCode查询
//            FaultCode faultCode = JSONObject.parseObject(JSONObject
//                                        .toJSONString(faultCodeService
//                                            .selectByFaultCode(faultRecordReq.getFaultCode())),FaultCode.class);
            /**
             * 缺 业务逻辑 如果faultCode为  null  需要插入新的faultCode
             */
            //  暂定 自己编写
//            newFaultHandle.setDeviceId(faultHandle.getDeviceId())
//                    .setFaultCode(faultHandle.getFaultCode())
//                    .setFaultDescribe(faultHandle.getFaultDescribe())
////                    .setFaultType(faultHandle.getFaultType())
//                    .setFaultAdvice(faultHandle.getFaultAdvice())
//                    .setModuleCode(faultHandle.getModuleCode())
//                    .setFaultClass(faultHandle.getFaultClass())
//                    .setFaultTime(faultHandle.getFaultTime())
//                    .setHandleStatus(faultHandle.getHandleStatus())
//                    .setUserName(faultHandle.getUserName())
//                    .setHandleTime(faultHandle.getHandleTime())
//                    .setHandleAdvice(faultHandle.getHandleAdvice())
//                    .setDeviceType(instrument.getInstrumentModel());
            int rec = faultHandleDao.insert(faultHandle);
            if (rec == 1) {
//                redisTemplate.set(faultCode.getFaultCode(),newFaultRecord);
                return CommonResult.success();
            }
            else
                return CommonResult.error();
        }

    }





//     ---------删除操作--------------

    /**
     * 根据ID删除故障记录
     * @param faultHandleId
     * @return
     */
    @LogAnno(operateType = "删除故障处理")
    public CommonResult deletById(int faultHandleId) {
        FaultHandle faultRecord = faultHandleDao.selectById(faultHandleId);
        if(CommonUtil.isNull(faultRecord)){
//            FaultRecord redisFaultCode = JSONObject
//                    .parseObject(redisTemplate
//                            .get(String.valueOf(faultRecord.getFaultRecordId())),FaultRecord.class);
//            if(CommonUtil.isNotNull(redisFaultCode)){
//                redisTemplate.del(String.valueOf(faultcodeId));
//            }
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = faultHandleDao.deleteById(faultHandleId);
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
     * 更新物品
     * @param faultHandle
     * @return
     */
    public CommonResult update(FaultHandle faultHandle) {
        if (CommonUtil.isNull(faultHandle)) {
            return CommonResult.error();
        } else{

            int rec = faultHandleDao.updateById(faultHandle);
            if (rec == 1) {
    //                    redisTemplate.del(String.valueOf(faultCode.getFaultCodeId()));
    //                    redisTemplate.set(String.valueOf(newFaultCode.getFaultCodeId()),newFaultCode);
    //                    redisTemplate.del(faultCode.getFaultCode());
    //                    redisTemplate.set(newFaultCode.getFaultCode(),newFaultCode);
                return CommonResult.success();
            }
            else{
                return CommonResult.error();

            }
        }

    }
    /**
     * 批量更新
     * @param faultHandleList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateBatch(List<FaultHandle> faultHandleList){
        if(0 == faultHandleList.size()){
            return CommonResult.error();
        }
        int rec = faultHandleDao.updateBatchSomeColumn(faultHandleList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }

//     ---------导出报表--------------

//    /**
//     * 导出EXCEL报表   xsc
//     * @param generateExcel
//     * @return
//     * @throws IOException
//     * @throws ParseException
//     */
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
                + "的仪器故障已处理完成";
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


}
