package cn.rmy.service.impl;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.dto.Users;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.dao.ReagentSurplusInfoDao;
import cn.rmy.domain.ConditionsSelectReq;
import cn.rmy.domain.ReagentSurplusInfo;
import cn.rmy.dto.ReagentSurplusInfoPageDto;
import cn.rmy.emailUtil.SendMailUtil;
import cn.rmy.service.Impl.UserWithInstServiceImpl;
import cn.rmy.service.InstrumentService;
import cn.rmy.service.ReagentSurplusInfoService;
import cn.rmy.service.imp.UsersServiceImp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ReagentSurplusInfoServiceImpl extends ServiceImpl<ReagentSurplusInfoDao, ReagentSurplusInfo> implements ReagentSurplusInfoService {

    @Autowired
    private ReagentSurplusInfoDao reagentSurplusInfoDao;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private UserWithInstServiceImpl userWithInstService;

    @Autowired
    private UsersServiceImp usersServiceImp;

//    @Autowired
//    private RedisTemplate redisTemplate;



    public List<ReagentSurplusInfo> countSurplusByConditions(ConditionsSelectReq conditionsSelectReq){
        int nearlyDay = conditionsSelectReq.getNearlyDay();// 条件为：0：自定义（日期范围查询），1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
        List<ReagentSurplusInfo> result = null;
        QueryWrapper<ReagentSurplusInfo> queryWrapper = new QueryWrapper<>();
        if(nearlyDay != 0){
            if(1 == nearlyDay){
                queryWrapper.apply("TO_DAYS(CURDATE())-1 <= TO_DAYS(get_time)");
            }else if(2 == nearlyDay){
                queryWrapper.apply("WEEK(CURDATE()) -1<= WEEK(get_time)");
            }else if(3 == nearlyDay){
                queryWrapper.apply("MONTH(CURDATE()) -1 <= MONTH(get_time)");
            }else if(4 == nearlyDay){
                queryWrapper.apply("MONTH(CURDATE()) -3 <= MONTH(get_time)");
            }
            result = reagentSurplusInfoDao.selectList(queryWrapper);
        }else {
            Date beginTime = conditionsSelectReq.getBeginTime();
            Date endTime = conditionsSelectReq.getEndTime();
            queryWrapper.ge("get_time", beginTime);
            queryWrapper.le("get_time", endTime);
            result = reagentSurplusInfoDao.selectList(queryWrapper);

        }
        return result;

    }



    // 出现故障后 将其故障信息发送给负责的该仪器的人
    public CommonResult emailSend(String deviceId) {
//        FaultRecord faultRecord = faultRecordDao.selectById(faultRecordId);
        Instrument instrument = instrumentService.getOneByInstrumentId(deviceId);

        MailDto mail = new MailDto();
        mail.setSubject("试剂余量处理通知");
        String content = "您好：\n"
                + "您所负责的编号为" + deviceId + "，类型为："+instrument.getInstrumentModel()
                + "的仪器出现试剂余量问题，试剂余量不减反增" +"\n"
                + "请您及时处理!";
        mail.setContent(content);
        //收件人
        List<UserT> list = userWithInstService.getUsersByInstId(deviceId);
        if (list.isEmpty()){
            return CommonResult.error();
        }
        int i = 0;
        String[] emails = new String[list.size()];
        for (UserT user : list){
            Users users = usersServiceImp.getUserById(user.getUserId());
            if (CommonUtil.isNull(users)){
                continue;
            }
            String email = users.getUserEmail();
            if (CommonUtil.isNull(email)){
                continue;
            }else{
                emails[i++] = email;
            }
        }
        mail.setTos(emails);

        //发送邮件
        SendMailUtil.send(mail);

        return CommonResult.success();
    }


    //     ---------查询操作--------------

    /**
     * 查询所有物品
     *
     * @return
     */
    public CommonResult selectAll(){
        System.out.println("hello--- ");
        List<ReagentSurplusInfo> reagentSurpluses = reagentSurplusInfoDao.selectList(null);
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
        ReagentSurplusInfo reagentSurplusesCount = reagentSurplusInfoDao.selectById(reagentSurplusesId);
        if (CommonUtil.isNull(reagentSurplusesCount)) {
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        } else {
//                redisTemplate.set(String.valueOf(faultCodeId),faultCode);
            return CommonResult.success(reagentSurplusesCount);
        }
    }


    public List<ReagentSurplusInfo> selectByDeviceIdAndReagentBoxId(String deviceId,String reagentBoxId){
        QueryWrapper<ReagentSurplusInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.eq("reagent_box_id",reagentBoxId);
        List<ReagentSurplusInfo> result = reagentSurplusInfoDao.selectList(queryWrapper);
        System.out.println("selectByDeviceIdAndReagentBoxId" + result);
        return result;
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
        Page<ReagentSurplusInfo> page = new Page<>(current,size);
        QueryWrapper<ReagentSurplusInfo> wrapper = new QueryWrapper<>();

//        if(conditionsMap.containsKey("faultClass")){
//            wrapper.eq("fault_class",conditionsMap.get("faultClass"));
//            conditionsMap.remove("faultClass");
//        }
        //  试剂盒号
        if(conditionsMap.containsKey("reagentBoxId")){
            wrapper.eq("reagent_box_id",conditionsMap.get("reagentBoxId"));
            conditionsMap.remove("reagentBoxId");
        }
        if(conditionsMap.containsKey("reagentSurplus")){
            wrapper.like("reagent_surplus",conditionsMap.get("reagentSurplus"));
            conditionsMap.remove("reagentSurplus");
        }
        System.out.println("测试结束-----");
        reagentSurplusInfoDao.selectPage(page, wrapper);
//        }
        List<ReagentSurplusInfo> reagentSurpluses = page.getRecords();
        System.out.println("xsc分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==reagentSurpluses.size()){
            return CommonResult.success();
        }
        return CommonResult.success(new ReagentSurplusInfoPageDto(total,reagentSurpluses));
    }

    /**
     * 条件查询所有  用于导出
     * @return
     */
    public CommonResult selectByConditions(Map<String,Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<ReagentSurplusInfo> list = null;
        QueryWrapper<ReagentSurplusInfo> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = reagentSurplusInfoDao.selectList(null);
        }else {
            // 有条件
            if(conditionsMap.containsKey("reagentBoxId")){
                wrapper.eq("reagent_box_id",conditionsMap.get("reagentBoxId"));
                conditionsMap.remove("reagentBoxId");
            }
            if(conditionsMap.containsKey("reagentSurplus")){
                wrapper.like("reagent_surplus",conditionsMap.get("reagentSurplus"));
                conditionsMap.remove("reagentSurplus");
            }
            list = reagentSurplusInfoDao.selectList(wrapper);
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
     * @param reagentSurplusInfo
     * @return
     */
    public CommonResult insert(ReagentSurplusInfo reagentSurplusInfo) {
        System.out.println("试剂余量信息的录入 insert reagentSurplusInfo:" + reagentSurplusInfo);
        if(CommonUtil.isNull(reagentSurplusInfo)){
            return CommonResult.error();
//        }else if(CommonUtil.isNotNull(reagentSurplusDao.selectById(goods.getGoodsId()))){
//            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else {
            QueryWrapper<ReagentSurplusInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("reagent_box_id", reagentSurplusInfo.getReagentBoxId());
            queryWrapper.eq("device_id", reagentSurplusInfo.getDeviceId());

            ReagentSurplusInfo reagentSurplusInfo1 = reagentSurplusInfoDao.selectOne(queryWrapper);
            if (!CommonUtil.isNull(reagentSurplusInfo1))
                return CommonResult.error(CommonResultEm.ERROR, "试剂余量信息插入错误");
            else {
                int rec = reagentSurplusInfoDao.insert(reagentSurplusInfo);
                if (rec == 1) {
//                redisTemplate.set(faultCode.getFaultCode(),faultCode);
                    return CommonResult.success();
                } else
                    return CommonResult.error();
            }
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
        int rec = reagentSurplusInfoDao.deleteById(reagentSurplusId);
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
     * @param reagentSurplusInfo
     * @return
     */
//    @LogAnno(operateType = "修改试剂余量信息")
    public CommonResult update(ReagentSurplusInfo reagentSurplusInfo) {
        if (CommonUtil.isNull(reagentSurplusInfo)) {
            return CommonResult.error();
        } else{
//            ReagentSurplusInfo newReagentSurplusInfo = reagentSurplusInfoDao.selectById(reagentSurplusInfo.getReagentSurplusInfoId());
//            if (CommonUtil.isNull(newReagentSurplusInfo)) {
//                return CommonResult.error(CommonResultEm.NOT_EXIST);
//            } else {
//                newReagentSurplusInfo.setReagentSurplusInfoId(reagentSurplusInfo.getReagentSurplusInfoId())
//                        .setReagentBoxId(reagentSurplusInfo.getReagentBoxId())
//                        .setReagentSurplus(reagentSurplusInfo.getReagentSurplus());
                int rec = reagentSurplusInfoDao.updateById(reagentSurplusInfo);

                if (rec == 1) {
//                    redisTemplate.del(String.valueOf(faultCode.getFaultCodeId()));
//                    redisTemplate.set(String.valueOf(newFaultCode.getFaultCodeId()),newFaultCode);
//                    redisTemplate.del(faultCode.getFaultCode());
//                    redisTemplate.set(newFaultCode.getFaultCode(),newFaultCode);
                    return CommonResult.success();
                }
                else
                    return CommonResult.error();
//            }

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
