package cn.rmy.controller;


import cn.rmy.beans.*;
import cn.rmy.beans.dto.ContentVO;
import cn.rmy.beans.dto.UpgradePackSendVo;
import cn.rmy.beans.dto.UpgradePermissionList;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.dao.UpgradePermissionDao;
import cn.rmy.fileUtils.OssFileUtils;
import cn.rmy.mqttUtils.MqttSendHandle;
import cn.rmy.service.PackageTypeService;
import cn.rmy.service.UpgradePackageService;

import cn.rmy.service.UpgradePermissionService;
import cn.rmy.socketUtils.SocketClientTest;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/upgrade")
public class UpgradeController {

    @Value("${filePath}")
    public String filePath;

    @Autowired
    private UpgradePackageService upgradePackageService;

    @Autowired
    private PackageTypeService packageTypeService;

    @Autowired
    private UpgradePermissionService upgradePermissionService;

    @Autowired
    private OssFileUtils ossFileUtils;

    @Autowired
    private MqttSendHandle mqttSendHandle;

    @Autowired
    private UpgradePermissionDao upgradePermissionDao;

    @RequestMapping("/testSocket")
    public CommonResult testSocket(@RequestParam("fileName") String fileName) throws IOException {
        SocketClientTest sct = new SocketClientTest();
        sct.test(1,fileName);
        return CommonResult.success("success");
    }

    @RequestMapping("/insertType")
    public CommonResult insertType(@RequestBody PackageType packageType){
        if(packageType.getTypeName()==null) return CommonResult.error(CommonResultEm.ERROR,"类型名不能为空");
        int rec = packageTypeService.insertType(packageType);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"类型名已存在");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"类型名创建失败");
        else return CommonResult.success("新建类型名成功");
    }


    //这个接口隐藏掉，不能给用户提供删除接口，否则删除类型后，后面的表会查不到类型
    @RequestMapping("/deleteType")
    public CommonResult deleteType(@RequestBody PackageType packageType){
        if(packageType.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"类型id不能为空");
        int rec = packageTypeService.delete(packageType.getId());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"类型名不存在，可能已被删除");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"类型名删除失败");
        else return CommonResult.success("类型删除成功");
    }

    @RequestMapping("/updateType")
    public CommonResult updateType(@RequestBody PackageType packageType){
        if(packageType.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未传入类型对应的id");
        int rec = packageTypeService.updateType(packageType);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"未查询到类型");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"修改类型失败");
        else return CommonResult.success("修改类型成功");
    }

    @RequestMapping("/getAllTypes")
    public CommonResult getAllTypes(){
        List<PackageType> list = packageTypeService.getAllType();
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"无类型");
        return CommonResult.success(list);
    }

    @RequestMapping("/getTypeByCondition/{current}/{size}")
    public CommonResult getTypeByCondition(@RequestBody PackageType packageType,@PathVariable("current") int current, @PathVariable("size") int size){
        if(packageType.getTypeName()==null) packageType.setTypeName("");
        SelectResult selectResult = packageTypeService.getTypeByCondition(packageType,current,size);
        if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到升级包类型");
        return CommonResult.success(selectResult);
    }



    @RequestMapping("/uploadFile")
    public CommonResult uploadFile(@RequestParam("packageName") String packageName,
                                   @RequestParam("typeId") int typeId,
                                   @RequestParam("modelId") int modelId,
                                   @RequestParam("packVersion") String packVersion,
                                   @RequestParam("description") String description,
                                   @RequestParam("url") String url) {
        UpgradePackage upgradePackage = new UpgradePackage();
        upgradePackage.setPackageName(packageName);
        upgradePackage.setTypeId(typeId);
        upgradePackage.setModelId(modelId);
        upgradePackage.setPackVersion(packVersion);
        upgradePackage.setAuthorization(-1);
        upgradePackage.setDescription(description);
        upgradePackage.setUrl(url);
        if(upgradePackage.getPackageName()==null) return CommonResult.error(CommonResultEm.ERROR,"升级包的包名不合法");
        if(upgradePackage.getTypeId()==0) return CommonResult.error(CommonResultEm.ERROR,"请选择升级包类型");
        if(upgradePackage.getModelId()==0) return CommonResult.error(CommonResultEm.ERROR,"请选择升级包适用的仪器型号");
        if(upgradePackage.getPackVersion()==null) return CommonResult.error(CommonResultEm.ERROR,"请填写升级包版本号");
        if(upgradePackage.getDescription()==null||upgradePackage.getDescription().length()==0) return CommonResult.error(CommonResultEm.ERROR,"请填写描述信息");
        if(upgradePackage.getUrl()==null) return CommonResult.error(CommonResultEm.ERROR,"请填写升级包地址");
        //int rec = UpLoad.upLoadFile(filePath, upgradePackage.getPackageName(), file);
        int rec = 0;
        try {
            //url = ossFileUtils.uploadFile(file,3,0);
            // Oss 简单上传
            //url = ossFileUtils.uploadFile(file,3,0,packVersion);
            // Oss 分片上传
            //url = ossFileUtils.uploadFileMultipart(file,3,0,packVersion);

            rec = upgradePackageService.insertPackageSyn(upgradePackage);
        } catch (Exception e) {
            log.info(e.toString());
            return CommonResult.error(CommonResultEm.ERROR,"升级包上传失败");
        }

        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"升级包名称重复");
       // else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"升级包信息写入出错");

        return CommonResult.success("升级包上传成功");
    }

    @RequestMapping("/deletePackage")
    public CommonResult deletePackage(@RequestBody UpgradePackage upgradePackage){
        if(upgradePackage.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回待删除升级包的id");
        int rec = upgradePackageService.deletePackage(upgradePackage.getId());
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"升级包不存在，可能已被删除");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"删除失败");
        else{
            UpgradePermission upgradePermission = new UpgradePermission();
            upgradePermission.setPackageId(upgradePackage.getId());
            int rr = upgradePermissionService.deleteByPackageId(upgradePermission);
            if(rr==-1) log.info("未查询到升级包 id="+upgradePackage.getId()+" 对应的授权记录");
            else if(rr==0) log.info("删除升级包授权记录-失败失败- packageId="+upgradePackage.getId());
            else log.info("成功删除升级包授权记录");
            return CommonResult.success("删除成功");
        }
    }

    @RequestMapping("/getPackageByCondition/{current}/{size}")
    public CommonResult getPackageByCondition(@RequestBody UpgradePackage upgradePackage, @PathVariable("current") int current, @PathVariable("size") int size){
        SelectResult selectResult = upgradePackageService.getByCondition(upgradePackage,current,size);
        if(selectResult.getList()==null || selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到升级包信息");
        else return CommonResult.success(selectResult);
    }

    @RequestMapping("/updatePackage")
    public CommonResult updatePackage(@RequestBody UpgradePackage upgradePackage){
        if(upgradePackage.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回升级包的id");
        int rec = upgradePackageService.updatePackage(upgradePackage);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"未查询到升级包信息");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"升级包信息更新失败");
        else return CommonResult.success("升级包信息更新成功");
    }

    @RequestMapping("/getPermissionByPackageId")
    public CommonResult getPermissionByPackageId(@RequestBody UpgradePermission upgradePermission){
        if(upgradePermission.getPackageId()==0) return CommonResult.error(CommonResultEm.ERROR,"未传入升级包的packageId");
        List<UpgradePermission> list = upgradePermissionService.getByPackageId(upgradePermission);
        if(list==null||list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到仪器权限信息");
        else return CommonResult.success(list);
    }


    @RequestMapping("/updateAllPermission")
    public CommonResult updateAllPermission(@RequestBody UpgradePermission upgradePermission){
        if(upgradePermission.getPackageId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回升级包id");
        if(upgradePermission.getAllowed()==0) return CommonResult.error(CommonResultEm.ERROR,"未指明是否全部授权还是全部禁止");
        int rec = upgradePermissionService.updateAll(upgradePermission);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"未查询到要更新的授权信息");
        else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"授权信息更新失败");
        else {
            UpgradePackage upgradePackage = new UpgradePackage();
            upgradePackage.setId(upgradePermission.getPackageId());
            upgradePackage.setAuthorization(upgradePermission.getAllowed()==-1 ? -1:2);
            int rr = upgradePackageService.updatePackage(upgradePackage);
            if(rr==-1) log.info("未查询到升级包 id="+upgradePackage.getId());
            else if(rr==0) log.info("失败！！修改为全部授权或全部禁止失败 packageId="+upgradePackage.getId());
            else log.info("成功修改升级包为全部授权或全部禁止");
            int sendNum = upgradePackageService.sendUpgrade(upgradePackage);
            if(sendNum==-1) log.info("未查询到升级包");
            else log.info("升级包"+upgradePackage.getId()+"推送"+sendNum+"台仪器");
            if(sendNum>0) return CommonResult.success("更新成功,推送 "+sendNum+" 台仪器");
            else return CommonResult.success("授权信息更新成功");
        }
    }

    @RequestMapping("/updatePart")
    public CommonResult updatePart(@RequestBody UpgradePermissionList upgradePermissionList){
        List<UpgradePermission> list = upgradePermissionList.getList();
        if(list==null || list.size()==0) return CommonResult.error(CommonResultEm.ERROR,"返回的list为空");
        for(UpgradePermission upp : list){
            if(upp.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回授权记录的id");
            if(upp.getAllowed()==0) return CommonResult.error(CommonResultEm.ERROR,"未指明授权信息，授权（1），禁止（-1）");
        }
        int rec = upgradePermissionService.update(list);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"未查询到待更新的目录，可能是id错误或者记录已被删除");
        //else if(rec==0) return CommonResult.error(CommonResultEm.ERROR,"更新失败");
        else{
            UpgradePermission temp = upgradePermissionDao.selectById(list.get(0).getId());
            UpgradePackage upgradePackage = new UpgradePackage();
            upgradePackage.setId(temp.getPackageId());
            upgradePackage.setAuthorization(1);
            int rr = upgradePackageService.updatePackage(upgradePackage);
            if(rr==-1) log.info("未查询到升级包 id="+upgradePackage.getId());
            else if(rr==0) log.info("失败！！修改为部分授权失败 packageId="+upgradePackage.getId());
            else log.info("成功修改升级包为部分授权");
            int sendNum = upgradePackageService.sendUpgrade(upgradePackage);
            if(sendNum==-1) log.info("未查询到升级包");
            else log.info("升级包"+upgradePackage.getId()+"推送"+sendNum+"台仪器");
            if(sendNum>=0) return CommonResult.success("更新成功,推送 "+sendNum+" 台仪器");
            else return CommonResult.success("更新成功");
        }
    }


    @RequestMapping("/sendUpgrade")
    public CommonResult sendUpgrade(@RequestBody UpgradePackage upgradePackage){
        if(upgradePackage.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未指定升级包");

        UpgradePackSendVo upgradePackSendVo = upgradePackageService.getSendPack(upgradePackage);
        String msg = JSON.toJSONString(upgradePackSendVo);

        List<UpgradePermission> list = upgradePermissionService.getSendInstByPackId(upgradePackage);
        if(list==null || list.size()==0) return CommonResult.success("推送0台仪器");
        for(UpgradePermission upp:list){
            String topic = "/upgrade/"+upp.getInstId();
            //String topic = "/upgrade";
            mqttSendHandle.sendHandle(topic,2,msg);
        }

        return CommonResult.success("推送 "+list.size()+" 台仪器");
    }

    @RequestMapping("/sendCommand")
    public CommonResult sendCommand(@RequestBody CommandSend commandSend){

        List<String> list = commandSend.getInstIds();
        for(String instId:list){
            String topic = instId+"/command";
            //String content = "content:"+commandSend.getContent();
            ContentVO contentVO = new ContentVO();
            contentVO.setContent(commandSend.getContent());
            String msg = JSON.toJSONString(contentVO);
            //System.out.println(msg);
            mqttSendHandle.sendHandle(topic,2,msg);
        }


        return CommonResult.success("命令已推送"+list.size()+"台仪器");
    }

}
