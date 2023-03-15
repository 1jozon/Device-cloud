package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ins味精dto
 *
 * @author chu
 * @date 2021/12/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsMsgDto {


    /**
     * 位置区域码
     */
    private String lac;

    /**
     * 基站编号
     */
    private String cid;
}
