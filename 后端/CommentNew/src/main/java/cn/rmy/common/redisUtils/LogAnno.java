package cn.rmy.common.redisUtils;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 方法注解
@Retention(RetentionPolicy.RUNTIME) // 运行时可见
public @interface LogAnno {
//    String operateor();  // 记录日志的操作人
    String operateType();// 记录日志的操作类型
//    String operateDate();
}