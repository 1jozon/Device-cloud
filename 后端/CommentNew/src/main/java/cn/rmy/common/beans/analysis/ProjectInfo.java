package cn.rmy.common.beans.analysis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目信息
 *
 * @author chu
 * @date 2022/02/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_project")
public class ProjectInfo {

    @TableId(value = "id", type = IdType.AUTO)
    int id;

    /**
     * 项目id
     */
    String projectId;

    /**
     * 项目简称
     */
    String projectAbbr;

    /**
     * 项目名称
     */
    String projectName;
}
