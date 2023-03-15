package cn.rmy.controller;

import cn.rmy.common.beans.analysis.ProjectInfo;
import cn.rmy.common.pojo.dto.ProjectMsgDTO;
import cn.rmy.service.Imp.ProjectServiceImp;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 项目信息控制器
 *
 * @author chu
 * @date 2022/02/21
 */
@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/ProjectInfo")
@RabbitListener(queues = "rmy.queue.$share/g2/ProjectInfo")
public class ProjectTransController {

    @Autowired
    private ProjectServiceImp projectService;

    @RabbitHandler
    public void ProjectProcess(String message) throws Exception{
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JSONObject object = JSONObject.parseObject(message);
        ProjectInfo projectInfo = new ProjectInfo();
       // System.out.println("接收项目信息" + (String) object.get("ProjectID"));
        projectInfo.setProjectId((String) object.get("ProjectID"));
        projectInfo.setProjectAbbr((String) object.get("ProjectAbbr"));
        projectInfo.setProjectName((String) object.get("ProjectName"));

        int rec = projectService.insertProject(projectInfo);
        if (rec == 1){
            log.info("项目信息数据添加成功");
        }else{
            log.info("项目数据添加失败");
        }
    }

}
