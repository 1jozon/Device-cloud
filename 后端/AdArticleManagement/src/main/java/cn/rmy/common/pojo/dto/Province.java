package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 省份仪器分布
 *
 * @author chu
 * @date 2021/11/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Province {

    /**
     * 省
     */
    String province;

    /**
     * 值
     */
    int value;
}
