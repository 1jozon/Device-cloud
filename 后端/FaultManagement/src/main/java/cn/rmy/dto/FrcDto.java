package cn.rmy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  未处理：1
 *  已处理：2
 */
@Data
@Accessors(chain = true)
public class FrcDto {

    private String handle_status;

    private Object fault_code_num;
}
