package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 项目数据dto
 *
 * @author chu
 * @date 2022/03/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDataDto {

    private String projectId;

    private String projectName;
}
