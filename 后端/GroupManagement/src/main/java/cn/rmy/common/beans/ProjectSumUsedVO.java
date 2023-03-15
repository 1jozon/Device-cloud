package cn.rmy.common.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSumUsedVO {
    String projectName;

    String usedNumber;
}
