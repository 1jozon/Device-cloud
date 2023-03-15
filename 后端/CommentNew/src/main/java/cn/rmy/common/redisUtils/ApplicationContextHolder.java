package cn.rmy.common.redisUtils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@Component
public class ApplicationContextHolder {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }


    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }
}
