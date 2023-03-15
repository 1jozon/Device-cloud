package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 推送内容
 *
 * @author chu
 * @date 2021/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePush {

    /**
     * 内容
     */
    private String article;

    /**
     * 照片urls
     */
    private Set<String> picture;

    /**
     * 符合权限仪器ids
     */
    private Set<String> ids;

}
