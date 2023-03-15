package cn.rmy.beans.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import schemasMicrosoftComOfficeOffice.STInsetMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_project")
public class ProjectNameVO {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    private String projectId;

    private String projectAbbr;

    private String projectName;



}
