/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/
package cn.rmy.common.redisUtils;

import java.lang.reflect.Field;

/**
 * 类作用：ReflectUtil工具类.
 * 项目名称: color-common
 * 包: com.migu.colorcommon.util
 * 类名称: ReflectUtil.java
 * 类描述: 反射工具类
 * 创建人: nirui
 * 创建时间: 2017-07-25 11:46
 */
public class ReflectUtil {

    /**
     *
      *〈一句话功能简述〉
      *〈功能详细描述〉
      * @param instance instance
      * @param fieldName fieldName
      * @return field.get(instance)
      * @throws IllegalAccessException
      * @throws NoSuchFieldException
     */
    public static Object getValue(Object instance, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = getField(instance.getClass(), fieldName);
        // 参数值为true，禁用访问控制检查
        field.setAccessible(true);
        return field.get(instance);
    }
    /**
     * getField.
      *〈一句话功能简述〉
      *〈功能详细描述〉
      * @param thisClass   类
      * @param fieldName  名称
      * @return  1
      * @throws NoSuchFieldException 1
     */
    public static Field getField(Class thisClass, String fieldName)
            throws NoSuchFieldException {

        if (fieldName == null) {
            throw new NoSuchFieldException("Error field !");
        }

        Field field = thisClass.getDeclaredField(fieldName);
        return field;
    }

}
