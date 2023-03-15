package cn.rmy.SpringUtils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContexUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext=null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContexUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static Object getBeanByNull(String beanName){
        try{
            return applicationContext.getBean(beanName);
        }
        catch (NullPointerException e){
            return null;
        }
    }

    public static boolean containsBean(String beanName){
        return applicationContext.containsBean(beanName);
    }

}
