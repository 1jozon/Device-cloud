package cn.rmy.controller;

import cn.rmy.common.beans.analysis.ProjectInfo;
import cn.rmy.common.pojo.dto.CaliMsgDTO;
import cn.rmy.service.Imp.CaliPointDataServiceImp;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.service.Imp.ProjectServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * cali数据获取控制器
 *
 * @author chu
 * @date 2021/11/18
 */
@RestController
@RequestMapping("rmy/cali")
public class GetMsgController {

    private final static Logger logger = LoggerFactory.getLogger(GetMsgController.class);

    @Autowired
    private CaliPointDataServiceImp caliPointDataService;

    @Autowired
    private ProjectServiceImp projectService;


    /**
     * 添加cali所有数据-有用
     *
     * @param caliMsgDTO 卡利味精dto
     * @return {@link CommonResult}
     */
    @RequestMapping("/insertNewCaliMsg")
    public CommonResult insertNewCaliMsg(@RequestBody CaliMsgDTO caliMsgDTO) throws Exception{
        if (caliMsgDTO == null){
            return CommonResult.error(CommonResultEm.ERROR,"信息错误");
        }
        try {
            caliPointDataService.insertNewData(caliMsgDTO);
        }catch (Exception e){
            e.getCause();
            logger.info("定标数据插入异常");
        }

/*        if (rec == 0){
            return CommonResult.error(CommonResultEm.ERROR,"添加失败");
        }*/
        return CommonResult.success("添加成功");
    }

    /**
     * 得到所有的项目信息
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getAllProjectInfo")
    public CommonResult getAllProjectInfo(){
        List<ProjectInfo> listProject = projectService.getAllPro();
        if (listProject == null){
            return CommonResult.success("未找到项目信息数据");
        }
        return CommonResult.success(listProject);
    }
}
