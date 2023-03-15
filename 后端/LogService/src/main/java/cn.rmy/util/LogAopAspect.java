package cn.rmy.util;

import cn.rmy.common.beans.faultManagement.Log;
import cn.rmy.common.dto.Users;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.service.LogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.http.*;


/**
 * AOP实现日志
 *
 * @author liqiang
 *
 */
@Component
@Aspect
public class LogAopAspect {
    private final static Logger logger = LoggerFactory.getLogger(LogAopAspect.class);

    @Autowired
    private LogService logService;// 日志Service

    @Autowired
    private HttpSession session;


    /**
     * 环绕通知记录日志通过注解匹配到需要增加日志功能的方法
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(cn.rmy.common.redisUtils.LogAnno)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        // 1.方法执行前的处理，相当于前置通知
        // 获取方法签名
        //1.这里获取到所有的参数值的数组
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();

        // 获取方法上面的注解
        LogAnno logAnno = method.getAnnotation(LogAnno.class);
        // 获取操作描述的属性值
        StringBuffer operateType = new StringBuffer(logAnno.operateType());
        operateType.append("，操作列表：");
        // 创建一个日志对象(准备记录日志)
        Log log = new Log();
        for (int i = 0; i < parameterNames.length; i++) {
            operateType.append("参数名："+parameterNames[i]+ "，参数值："+args[i]);
        }
        log.setOperateType(operateType.toString());// 操作说明

        // 获取session  待整合后获取当前session域中的user对象
//        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//        Object obj = requestAttributes.getAttribute("userId",RequestAttributes.SCOPE_REQUEST);
        // 获取session  待整合后获取当前session域中的user对象
//        Object obj = session.getAttribute("userId");


        //shiro从线程绑定中获取user
        Subject subject = SecurityUtils.getSubject();
        logger.info("日志切面：userId:" + subject);
        Users currentUser;
        if(CommonUtil.isNull(subject)){
            //用户未登录
            log.setOperateor("游客");
        }else{
            //用户正常登录
            currentUser = (Users) subject.getPrincipal();
            String uis = currentUser.getUserId();
            System.out.println(uis);
            log.setOperateor(uis);
        }



//        if(obj instanceof User) {
//        if(CommonUtil.isNotNull(obj)){
//            User user = (User) obj;
//            log.setOperateor(obj.toString());// 设置操作人
//        }else
//            log.setOperateor("游客");

//        User user = (User) ServletActionContext.getRequest().getSession().getAttribute("userId");//获取session中的user对象进而获取操作人名

        Object result = null;
        try {
            //让代理方法执行
            result = pjp.proceed();
            logger.info("日志切面 result:" + result);
            if(result instanceof CommonResult){
                CommonResult res = (CommonResult) result;
                if(!res.getResultCode().equals("200"))
                    log.setOperateResult("正常");// 设置操作结果
                else
                    log.setOperateResult("失败");// 设置操作结果
            }else
                log.setOperateResult("正常");
            // 2.相当于后置通知(方法成功执行之后走这里)

        } catch (Exception e) {
            // 3.相当于异常通知部分
            log.setOperateResult("失败");// 设置操作结果
        } finally {
            // 4.相当于最终通知
            log.setOperateDate(new Date());// 设置操作日期
            logService.insert(log);// 添加日志记录
        }
        return result;
    }
}
