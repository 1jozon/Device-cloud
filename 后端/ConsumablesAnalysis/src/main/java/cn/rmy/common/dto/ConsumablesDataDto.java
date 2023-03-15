package cn.rmy.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 耗材数据dto
 *
 * @author chu
 * @date 2021/12/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumablesDataDto {

    private String insId;

    private String conName;

    private String volUsed;

    /**
     * 更换耗材时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 测试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;
}
