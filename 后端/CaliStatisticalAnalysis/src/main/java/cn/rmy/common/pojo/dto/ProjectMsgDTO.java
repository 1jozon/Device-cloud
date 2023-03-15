package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目信息dto
 *
 * @author chu
 * @date 2022/02/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMsgDTO {
    String projectId;

    String projectAbbr;

    String projectName;
}
