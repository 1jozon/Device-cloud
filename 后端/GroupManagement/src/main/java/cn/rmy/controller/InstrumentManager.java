package cn.rmy.controller;



import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.InstDetails;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.InstrumentModel;
import cn.rmy.common.beans.LegalInstrument;
import cn.rmy.common.beans.groupManager.*;
import cn.rmy.common.pojo.dto.*;

import cn.rmy.service.*;
import cn.rmy.common.redisUtils.CommonResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rmy/instrument")
public class InstrumentManager {

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private LegalInstrumentService legalInstrumentService;

    @Autowired
    private InstrumentModelService instrumentModelService;

    @Autowired
    private InstGroupService instGroupService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private InstToGroupService instToGroupService;

    @Autowired
    private UserToGroupService userToGroupService;

    @Autowired
    private UserTService userTService;

    @Autowired
    private GroupToGroupService groupToGroupService;

    @Autowired
    private UserWithInstService userWithInstService;

    @Autowired
    private InstIdVOService instIdVOService;

/*
    @Autowired
    private MQTTConnect mqttConnect; */

    /**
     *
     * tjk
     * 向数据库中添加测试数据
     *
     *
    @RequestMapping("/testmqtt")
    public CommonResult testmqtt() throws MqttException {
        mqttConnect.publish("/tjk", JsonUtil.jsonToString("分组模块测试mqtt成功--0924"));
        return CommonResult.success("success");
    }
*/


    /**
     *
     * tjk
     * 向数据库中添加测试数据
     *
     */
    @RequestMapping("/insertUser")
    public CommonResult insertUser(@RequestBody UserT userT){
        int rec = userTService.insert(userT);
        if(rec<1) return CommonResult.error(CommonResultEm.ERROR,rec);
        return CommonResult.success(rec);
    }


    @RequestMapping("/getUserByUserId")
    public CommonResult getUserByUserId(@RequestBody UserT userT){
        if(userT.getUserId()==null||userT.getUserId().length()==0) return CommonResult.error(CommonResultEm.ERROR,"未传入userId");
        UserT res = userTService.getUserByUserId(userT.getUserId());
        if(res==null) return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        return CommonResult.success(res);
    }

    @RequestMapping("/getGroupNameByUserId")
    public CommonResult getGroupNameByUserId(@RequestBody UserT userT){
        return CommonResult.success(userToGroupService.getGroupNameByUserId(userT.getId()));
    }


    @RequestMapping("/getPermByInstId")
    public CommonResult getPermByInstId(@RequestBody Instrument instrument){

        instrumentService.setUpgradePermission(instrument.getInstrumentId());
        return CommonResult.success(0);
    }


    /**
     *
     * tjk
     * 仪器操作（注册，条件查询，仪器信息编辑，仪器删除）
     *
     */

    @RequestMapping("/addTestInstrument")
    public CommonResult addTestInstrument(@RequestBody Instrument instrument){
        int rec = instrumentService.insertTestInstruemnt(instrument);
        if(rec<=0) return CommonResult.error(CommonResultEm.ERROR,"错误码: "+rec);
        else return CommonResult.success("成功码："+rec);
    }

    @RequestMapping("/registerInstrument")
    public CommonResult registerInstrument(@RequestBody Instrument instrument){

        if(instrument.getInstrumentInstallerId()==null) return CommonResult.error(CommonResultEm.ERROR,"仪器编号 不能为空");
        if(instrument.getInstrumentModel()==null) return CommonResult.error(CommonResultEm.ERROR,"仪器型号 不能为空");
        if(instrument.getInstrumentAddress()==null) return CommonResult.error(CommonResultEm.ERROR,"注册地址 不能为空");
        if(instrument.getInstrumentDate()==null) return CommonResult.error(CommonResultEm.ERROR,"注册日期 不能为空");
        if(instrument.getInstrumentMaintainerId()==null) return CommonResult.error(CommonResultEm.ERROR,"维护人员id 不能为空");
        if(instrument.getInstrumentMaintainerName()==null) return CommonResult.error(CommonResultEm.ERROR,"维护人员姓名 不能为空");
        if(instrument.getInstrumentMaintainerPhone()==null) return CommonResult.error(CommonResultEm.ERROR,"维护人员手机号 不能为空");
        if(instrument.getHospitalName()==null) return CommonResult.error(CommonResultEm.ERROR,"医院名称 不能为空");
        if(instrument.getInstrumentInstallerId()==null) instrument.setInstrumentInstallerId(instrument.getInstrumentMaintainerId());
        if(instrument.getInstrumentInstallerName()==null) instrument.setInstrumentInstallerName(instrument.getInstrumentMaintainerName());

        LegalInstrument l = legalInstrumentService.getByInstrumentId(instrument.getInstrumentId());
        //if(l==null) return CommonResult.error(CommonResultEm.ERROR,"仪器编号不合法");
        int rec = instrumentService.insert(instrument);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"仪器已注册");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"仪器注册失败");
        else return CommonResult.success("仪器 "+instrument.getInstrumentId()+" 注册成功");
    }

    /*
    * 分页查询仪器
    * */
    @RequestMapping("/selectInstByCondition/{current}/{size}")
    public CommonResult selectInstByCondition(@PathVariable("current") int current,@PathVariable("size") int pageSize, @RequestBody InstCondition instCondition){
        SelectResult selectResult = instrumentService.getInstByCondition(instCondition,current,pageSize);
        if(selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到仪器");
        return CommonResult.success(selectResult);
    }

    //管理员账户接口 不传入userId
    @RequestMapping("/selectAllInstByCondition/{current}/{size}")
    public CommonResult selectAllInstByCondition(@PathVariable("current") int current,@PathVariable("size") int pageSize, @RequestBody InstCondition instCondition){
        SelectResult selectResult = instrumentService.getInstByCondition(instCondition,current,pageSize);
        if(selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到仪器");
        return CommonResult.success(selectResult);
    }


    /*
    * 不分页查询仪器
    * */
    @RequestMapping("/selectInstByInstId")
    public CommonResult selectInstByInstId(@RequestBody InstCondition instCondition){
        SelectResult selectResult = instrumentService.getInstByCondition(instCondition);
        if(selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到仪器");
        return CommonResult.success(selectResult);
    }




    //模糊查询仪器id，下拉框显示5~10个
    @RequestMapping("/getInstrumentIdForBox")
    public CommonResult getInstrumentIdForBox(@RequestBody InstCondition instCondition) {
        List<InstIdVO> list = instIdVOService.getInstIdVO(instCondition);
        return CommonResult.success(list);
    }

    @RequestMapping("/updateInstrument")
    public CommonResult updateInstrument(@RequestBody Instrument instrument){
        if(instrument.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"id 不能为空");
        else if(instrument.getInstrumentInstallerId()==null) return CommonResult.error(CommonResultEm.ERROR,"仪器编号 不能为空");
        else if(instrument.getInstrumentModel()==null) return CommonResult.error(CommonResultEm.ERROR,"仪器型号 不能为空");
        else if(instrument.getInstrumentAddress()==null) return CommonResult.error(CommonResultEm.ERROR,"注册地址 不能为空");
        else if(instrument.getInstrumentDate()==null) return CommonResult.error(CommonResultEm.ERROR,"注册日期 不能为空");
        else if(instrument.getOnlineStatus()==0) return CommonResult.error(CommonResultEm.ERROR,"在线状态 不能为空");
        else if(instrument.getFaultStatus()==0) return CommonResult.error(CommonResultEm.ERROR,"故障状态 不能为空");
        else if(instrument.getInstrumentInstallerId()==null) return CommonResult.error(CommonResultEm.ERROR,"安装人员id 不能为空");
        else if(instrument.getInstrumentInstallerName()==null) return CommonResult.error(CommonResultEm.ERROR,"安装人员姓名 不能为空");
        else if(instrument.getInstrumentMaintainerId()==null) return CommonResult.error(CommonResultEm.ERROR,"维护人员id 不能为空");
        else if(instrument.getInstrumentMaintainerName()==null) return CommonResult.error(CommonResultEm.ERROR,"维护人员姓名 不能为空");
        else if(instrument.getInstrumentMaintainerPhone()==null) return CommonResult.error(CommonResultEm.ERROR,"维护人员手机号 不能为空");
        else if(instrument.getHospitalName()==null) return CommonResult.error(CommonResultEm.ERROR,"医院名称 不能为空");
        else{
            int rec = instrumentService.update(instrument);
            if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"仪器不存在");
            else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"仪器信息更新失败");
            else return CommonResult.success("仪器信息更新成功");
        }
    }

    @RequestMapping("/deleteInstrument")
    public CommonResult deleteInstrument(@RequestBody Instrument instrument){
        if(instrument.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"id 不能为空");
        int rec = instrumentService.delete(instrument.getId());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"仪器不存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"仪器删除失败");
        else{
            return CommonResult.success("仪器删除成功");
        }
    }

    @RequestMapping("/getInstDetails")
    public CommonResult getInstDetails(@RequestBody Instrument instrument){

        InstDetails instDetails = instrumentService.getInstDetails(instrument.getInstrumentId());
        if(instDetails==null) return CommonResult.error(CommonResultEm.ERROR,"仪器不存在");
        return CommonResult.success(instDetails);
    }

    @RequestMapping("/getInstDetailsForApplet")
    public CommonResult getInstDetailsForApplet(@RequestBody Instrument instrument){
        if(instrument.getInstrumentId()==null||instrument.getInstrumentId().length()==0)
            return CommonResult.error(CommonResultEm.ERROR,"未传入仪器ID");
        Instrument res = instrumentService.getOneByInstrumentId(instrument.getInstrumentId());
        if(res==null) return CommonResult.error(CommonResultEm.ERROR,"仪器不存在");

        return CommonResult.success(res);
    }


    /**
     * tjk
     * 仪器型号操作(仪器型号的增加，删除，查询，校验)
     *
     */
    @RequestMapping("/addModel")
    public CommonResult addModel(@RequestBody InstrumentModel instrumentModel){
        if(instrumentModel.getInstrumentModel()==null) return CommonResult.error(CommonResultEm.ERROR,"型号 不能为空");
        int rec = instrumentModelService.insert(instrumentModel);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"该型号 已存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"新建型号失败");
        else return CommonResult.success("新建型号成功");
    }

    //这个接口也要隐藏掉，不能提供删除操作，智能提供更新操作
    @RequestMapping("/delModel")
    public CommonResult delModel(@RequestBody InstrumentModel instrumentModel){
        if(instrumentModel.getInstrumentModel()==null) return CommonResult.error(CommonResultEm.ERROR,"型号 不能为空");
        int rec = instrumentModelService.delete(instrumentModel.getInstrumentModel());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"该型号 不存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"删除型号失败");
        else return CommonResult.success("删除型号成功");
    }

    @RequestMapping("/updateModel")
    public CommonResult updateModel(@RequestBody InstrumentModel instrumentModel){
        if(instrumentModel.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未传入型号对应的id");
        int rec = instrumentModelService.updateModel(instrumentModel);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"型号不存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"修改型号失败");
        else return CommonResult.success("修改型号成功");
    }

    @RequestMapping("/getAllModels")
    public CommonResult getAllModels(){
        List<InstrumentModel> list = instrumentModelService.getAllModel();
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"无型号");
        else return CommonResult.success(list);
    }


    /**
     * tjk
     * 仪器分组操作(创建仪器组,更改仪器组名称，删除仪器组，查询仪器组名称)
     *
     */
    @RequestMapping("/createInstGroup")
    public CommonResult createInstGroup(@RequestBody InstGroup group){
        if(group.getGroupName()==null||group.getGroupName().length()==0) return CommonResult.error(CommonResultEm.ERROR,"分组名称不能为空");
        if(group.getCreatorId()==null||group.getCreatorId().length()==0) return CommonResult.error(CommonResultEm.ERROR,"分组负责人ID不能为空");
        int rec = instGroupService.insert(group);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"该分组名称已存在");
        else if(rec==-2) return CommonResult.error(CommonResultEm.ERROR,"分组负责人ID不存在(根据userId未在用户表中查询到用户)");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"新建分组失败");
        else return CommonResult.success("新建分组成功");
    }

    @RequestMapping("/updateInstGroupName")
    public CommonResult updateInstGroupName(@RequestBody InstGroup group){
        if(group.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"分组ID不能为空");
        if(group.getGroupName()==null) return CommonResult.error(CommonResultEm.ERROR,"新的分组名称不能为空");
        int rec = instGroupService.update(group);
        if(rec==2) return CommonResult.error(CommonResultEm.ERROR,"该分组名称已存在");
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"分组ID错误或分组不存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"更新分组失败");
        else return CommonResult.success("分组名称更改成功");
    }

    @RequestMapping("/deleteInstGroup")
    public CommonResult deleteInstGroup(@RequestBody InstGroup group){
        //该部分的业务逻辑还应包含其他几个表的操作，对应的仪器-分组表中的删除，仪器分组-人员分组对应关系的删除
        if(group.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回仪器组Id,无法删除");
        int rec = instGroupService.delete(group.getId());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"待删除仪器组不存在，可能已被删除");
        //else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"仪器组删除失败");
        else return CommonResult.success("仪器组删除成功");
    }

    @RequestMapping("/getInstGroupNameByCondition")
    public CommonResult getInstGroupNameByCondition(@RequestBody InstGroup group){
        List<InstGroup> list = instGroupService.getGroupByCondition(group);
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到仪器分组名称");
        else return CommonResult.success(list);
    }
/*
    @RequestMapping("/getAllInstGroup")
    public CommonResult getAllInstGroup(){
        List<InstGroup> list = instGroupService.getAllGroup();
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"没有仪器分组");
        else return CommonResult.success(list);
    }*/
    @RequestMapping("/getAllInstGroup/{current}/{size}")
    public CommonResult getAllInstGroup(@PathVariable("current") int current, @PathVariable("size") int size){
        SelectResult selectResult = instGroupService.getAllGroup(current,size);
        if(selectResult.getList()==null || selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"没有仪器分组");
        else return CommonResult.success(selectResult);
    }


    @RequestMapping("/getGroupByInstId")
    public CommonResult getGroupByInstId(@RequestBody Instrument instrument){
        List<InstGroup> list = instGroupService.getGroupByInstId(instrument);
        if(list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到数据");
        else return CommonResult.success(list);
    }

    /**
     * tjk
     * 人员分组操作(创建人员组,更改人员组名称，删除人员组，查询人员组名称)
     *
     */
    @RequestMapping("/createUserGroup")
    public CommonResult createUserGroup(@RequestBody UserGroup group){
        if(group.getGroupName()==null||group.getGroupName().length()==0) return CommonResult.error(CommonResultEm.ERROR,"分组名称不能为空");
        if(group.getCreatorId()==null||group.getCreatorId().length()==0) return CommonResult.error(CommonResultEm.ERROR,"分组负责人ID不能为空");
        int rec = userGroupService.insert(group);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"该分组名称已存在");
        else if(rec==-2) return CommonResult.error(CommonResultEm.ERROR,"分组负责人ID不存在(根据userId未在用户表中查询到用户)");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"新建分组失败");
        else return CommonResult.success("新建分组成功");
    }

    @RequestMapping("/updateUserGroupName")
    public CommonResult updateUserGroupName(@RequestBody UserGroup group){
        if(group.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"分组ID不能为空");
        if(group.getGroupName()==null) return CommonResult.error(CommonResultEm.ERROR,"新的分组名称不能为空");
        int rec = userGroupService.update(group);
        if(rec==2) return CommonResult.error(CommonResultEm.ERROR,"分组名称已存在");
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"分组ID错误或分组不存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"更新分组失败");
        else return CommonResult.success("分组名称更改成功");
    }

    @RequestMapping("/deleteUserGroup")
    public CommonResult deleteUserGroup(@RequestBody UserGroup group){
        //该部分的业务逻辑还应包含其他几个表的操作，对应的仪器-分组表中的删除，仪器分组-人员分组对应关系的删除
        if(group.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回人员组id,无法删除");
        int rec = userGroupService.delete(group.getId());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"待删除人员组不存在，可能已被删除");
        //else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"人员组删除失败");
        else return CommonResult.success("人员组删除成功");

    }

    @RequestMapping("/getUserGroupNameByCondition")
    public CommonResult getUserGroupNameByCondition(@RequestBody UserGroup group){
        List<UserGroup> list = userGroupService.getGroupByCondition(group);
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到人员分组名称");
        else return CommonResult.success(list);
    }

    @RequestMapping("/getAllUserGroup/{current}/{size}")
    public CommonResult getAllUserGroup(@PathVariable("current") int current, @PathVariable("size") int size){
        SelectResult selectResult = userGroupService.getAllGroup(current,size);
        if(selectResult.getList()==null || selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"没有人员分组");
        else return CommonResult.success(selectResult);
    }

    @RequestMapping("/getGroupByUserId")
    public CommonResult getGroupByUserId(@RequestBody UserT userT){
        List<UserGroup> list = userGroupService.getGroupByUserId(userT);
        if(list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到数据");
        else return CommonResult.success(list);
    }

    /**
     * tjk
     * 向分组中添加仪器 删除仪器 查询分组中的仪器
     *
     */
    @RequestMapping("/addInstToGroup")
    public CommonResult addInstToGroup(@RequestBody AddInstList addInstList){
        List<InstToGroup> list = addInstList.getList();
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"需加入分组的仪器为空");
        int rec = instToGroupService.insert(list);
        //if(rec == -1) return CommonResult.error(CommonResultEm.ERROR,"重复添加仪器到分组中");
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"部分仪器添加失败");
        else return CommonResult.success("仪器加入分组成功");
    }

    @RequestMapping("/delInstFromGroup")
    public CommonResult delInstFromGroup(@RequestBody InstToGroup instToGroup){
        if(instToGroup.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回id，无法删除");
        int rec = instToGroupService.delete(instToGroup);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"仪器不在分组中");
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"仪器移出分组失败");
        else return CommonResult.success("仪器移出分组成功");
    }

    @RequestMapping("/getInstByGroupId")
    public CommonResult getInstByGroupId(@RequestBody InstGroup group){
        if(group.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"请求参数不包括分组id");
        List<InstGroupVO> list = instToGroupService.getInstByGroup(group.getId());
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"当前分组没有仪器");
        return CommonResult.success(list);
    }

    /**
     * tjk
     * 向分组中添加人员 删除人员 查询分组中的人员
     *
     */
    @RequestMapping("/addUserToGroup")
    public CommonResult addUserToGroup(@RequestBody AddUserList addUserList){
        List<UserToGroup> list = addUserList.getList();
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"需加入分组的人员为空");
        int rec = userToGroupService.insert(list);
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"部分人员添加失败");
        else return CommonResult.success("人员加入分组成功");
    }

    @RequestMapping("/delUserFromGroup")
    public CommonResult delUserFromGroup(@RequestBody UserToGroup userToGroup){//根据id,所以必须传回id
        if(userToGroup.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回id，无法删除");
        int rec = userToGroupService.delById(userToGroup);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"人员不在分组中");
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"人员移出分组失败");
        else return CommonResult.success("人员移出分组成功");
    }

    @RequestMapping("/getUserByGroupId")
    public CommonResult getUserByGroupId(@RequestBody UserToGroup group){
        if(group.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"请求参数不包括分组id");
        List<UserGroupVO> list = userToGroupService.getUserByGroup(group.getId());
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"当前分组没有人员");
        return CommonResult.success(list);
    }

    /**
     * tjk
     * 向Group-To-Group中添加、移除、查询、更新
     *
     */
    @RequestMapping("/addGTG")
    public CommonResult addGTG(@RequestBody GroupToGroup gtg){
        if(gtg.getUsGroupId()==0) return CommonResult.error(CommonResultEm.ERROR,"对应关系中用户组缺失");
        if(gtg.getInstGroupId()==0) return CommonResult.error(CommonResultEm.ERROR,"对应关系中仪器组缺失");
        int rec = groupToGroupService.insertGTG(gtg);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"对应关系已存在，请勿重复添加");
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"创建对应关系失败");
        else return CommonResult.success("创建对应关系成功");
    }

    @RequestMapping("/updateGTG")
    public CommonResult updateGTG(@RequestBody GroupToGroup gtg){
        if(gtg.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未传入对应关系id");
        if(gtg.getUsGroupId()==0) return CommonResult.error(CommonResultEm.ERROR,"对应关系中用户组缺失");
        if(gtg.getInstGroupId()==0) return CommonResult.error(CommonResultEm.ERROR,"对应关系中仪器组缺失");
        int rec = groupToGroupService.updateGTG(gtg);
        if(rec==-2) return CommonResult.error(CommonResultEm.ERROR,"无法更改到目标对应关系，因为目标对应关系已存在");
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"对应关系不存在，无法更新");
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"更新对应关系失败");
        else return CommonResult.success("更新对应关系成功");
    }

    @RequestMapping("/delGTG")
    public CommonResult delGTG(@RequestBody GroupToGroup gtg){
        if(gtg.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未传入对应关系id，无法完成删除");
        int rec = groupToGroupService.deleteGTG(gtg.getId());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"对应关系不存在，无法删除");
        if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"删除对应关系失败");
        else return CommonResult.success("删除对应关系成功");
    }

    @RequestMapping("/getGTGByGroupId")
    public CommonResult getGTGByGroupId(@RequestBody GroupToGroup gtg){
        //if(gtg.getUsGroupId()==0 && gtg.getInstGroupId()==0) return CommonResult.error(CommonResultEm.ERROR,"请选择对应关系中包含的仪器组或者人员组");
        List<GroupToGroupVO> res = groupToGroupService.getByGroupId(gtg);
        if(res==null || res.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到对应关系");
        return CommonResult.success(res);
    }


    /**
     * tjk
     * 根据仪器id获取对应的负责人员、根据人员id获取所负责的仪器
     *
     */
    @RequestMapping("/getUsersByInstId")
    public CommonResult getUsersByInstId(@RequestBody Instrument instrument){
        if(instrument.getInstrumentId()==null) return CommonResult.error(CommonResultEm.ERROR,"未获取到仪器id");
        List<UserT> list = userWithInstService.getUsersByInstId(instrument.getInstrumentId());
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"仪器不存在");
        return CommonResult.success(list);
    }

    @RequestMapping("/getInstsByUserId")
    public CommonResult getInstsByUserId(@RequestBody UserT userT){
        if(userT.getUserId()==null) return CommonResult.error(CommonResultEm.ERROR,"未获取到用户id");
        List<Instrument> list = userWithInstService.getInstsByUserId(userT.getUserId());
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"人员暂无负责仪器");
        return CommonResult.success(list);
    }

}
