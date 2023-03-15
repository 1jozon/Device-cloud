package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 城市仪器分布
 *
 * @author chu
 * @date 2021/11/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    /**
     * 城市
     */
    String city;

    /**
     * 值
     */
    int value;
}
