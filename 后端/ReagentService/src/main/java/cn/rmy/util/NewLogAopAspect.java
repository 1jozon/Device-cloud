package cn.rmy.util;//package cn.rmy.cn.rmy.util;
//
//import cn.rmy.common.beans.faultManagement.Log;
//import cn.rmy.common.utils.LogAnno;
//import cn.rmy.cn.rmy.service.LogService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.Serializable;
//import java.lang.reflect.Method;
//import java.cn.rmy.util.*;
//
///**
// * 一句话功能描述.
// * 项目名称:
// * 包:
// * 类名称:
// * 类描述:   类功能详细描述
// * 创建人:
// * 创建时间:
// */
//@Component
//@Aspect
//public class NewLogAopAspect {
//    private Logger logger = LoggerFactory.getLogger(NewLogAopAspect.class);
//
//    @Autowired
//    private LogService logService;// 日志Service
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ThreadLocal<Date> startTime = new ThreadLocal<Date>();
//
//    @Pointcut("@annotation(cn.rmy.common.utils.LogA)")
//    public void pointcut() {
//
//    }
//    /**
//     * 前置通知，在Controller层操作前拦截
//     *
//     * @param joinPoint 切入点
//     */
//    @Before("pointcut()")
//    public void doBefore(JoinPoint joinPoint) {
//        // 获取当前调用时间
//        startTime.set(new Date());
//    }
//
//    /**
//     * 正常情况返回
//     *
//     * @param joinPoint 切入点
//     * @param rvt       正常结果
//     */
//    @AfterReturning(pointcut = "pointcut()", returning = "rvt")
//    public void doAfter(JoinPoint joinPoint, Object rvt) throws Exception {
//        handleLog(joinPoint, null, rvt);
//    }
//
//    /**
//     * 异常信息拦截
//     *
//     * @param joinPoint
//     * @param e
//     */
//    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
//    public void doAfter(JoinPoint joinPoint, Exception e) throws Exception {
//        handleLog(joinPoint, e, null);
//    }
//
//    @Async
//    private void handleLog(final JoinPoint joinPoint, final Exception e, Object rvt) throws Exception{
//        // 获得注解
//        Method method = getMethod(joinPoint);
//        LogAnno logAnno = getAnnotationLog(method);
//        if (logAnno == null) {
//            return;
//        }
//        Date now = new Date();
//        // 操作数据库日志表
//        Log log = new Log();
//        log.setOperateType(logAnno.operateType());// 操作说明
//
//        // 获取session  待整合后获取当前session域中的user对象
////        User user = (User) ServletActionContext.getRequest().getSession().getAttribute("userinfo");//获取session中的user对象进而获取操作人名字
////        log.setOperateor(user.getUsername());// 设置操作人
//        log.setOperateor("xsc");
//
//        Object result = null;
//        try {
//            //让代理方法执行
//            result = pjp.proceed();
//            // 2.相当于后置通知(方法成功执行之后走这里)
//            log.setOperateResult("正常");// 设置操作结果
//        } catch (Exception e) {
//            // 3.相当于异常通知部分
//            log.setOperateResult("失败");// 设置操作结果
//        } finally {
//            // 4.相当于最终通知
//            log.setOperateDate(new Date());// 设置操作日期
//            logService.insert(log);// 添加日志记录
//        }
//        return result;
//
//////        ErpLog erpLog = new ErpLog();
////        erpLog.setErrorCode(0);
////        erpLog.setIsDeleted(0);
////        // 请求信息
////        HttpServletRequest request = ToolUtil.getRequest();
////        erpLog.setType(ToolUtil.isAjaxRequest(request) ? "Ajax请求" : "普通请求");
////        erpLog.setTitle(log.value());
////        erpLog.setHost(request.getRemoteHost());
////        erpLog.setUri(request.getRequestURI().toString());
//////        erpLog.setHeader(request.getHeader(HttpHeaders.USER_AGENT));
////        erpLog.setHttpMethod(request.getMethod());
////        erpLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
////        // 请求的方法参数值
////        Object[] args = joinPoint.getArgs();
////        // 请求的方法参数名称
////        LocalVariableTableParameterNameDiscoverer u
////                = new LocalVariableTableParameterNameDiscoverer();
////        String[] paramNames = u.getParameterNames(method);
////        if (args != null && paramNames != null) {
////            StringBuilder params = new StringBuilder();
////            params = handleParams(params, args, Arrays.asList(paramNames));
////            erpLog.setParams(params.toString());
////        }
////        String retString = JsonUtil.bean2Json(rvt);
////        erpLog.setResponseValue(retString.length() > 5000 ? JsonUtil.bean2Json("请求参数数据过长不与显示") : retString);
////        if (e != null) {
////            erpLog.setErrorCode(1);
////            erpLog.setErrorMessage(e.getMessage());
////        }
////        Date stime = startTime.get();
////        erpLog.setStartTime(stime);
////        erpLog.setEndTime(now);
////        erpLog.setExecuteTime(now.getTime() - stime.getTime());
////        erpLog.setUsername(MySysUser.loginName());
////        HashMap<String, String> browserMap = ToolUtil.getOsAndBrowserInfo(request);
////        erpLog.setOperatingSystem(browserMap.get("os"));
////        erpLog.setBrower(browserMap.get("browser"));
////        erpLog.setId(IdUtil.simpleUUID());
////        logService.insertSelective(erpLog);
//    }
//
//    /**
//     * 是否存在注解，如果存在就获取
//     */
//    private LogAnno getAnnotationLog(Method method) {
//        if (method != null) {
//            return method.getAnnotation(LogAnno.class);
//        }
//        return null;
//    }
//
//    private Method getMethod(JoinPoint joinPoint) {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//        if (method != null) {
//            return method;
//        }
//        return null;
//    }
//
//    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
//        for (int i = 0; i < args.length; i++) {
//            if (args[i] instanceof Map) {
//                Set set = ((Map) args[i]).keySet();
//                List list = new ArrayList();
//                List paramList = new ArrayList<>();
//                for (Object key : set) {
//                    list.add(((Map) args[i]).get(key));
//                    paramList.add(key);
//                }
//                return handleParams(params, list.toArray(), paramList);
//            } else {
//                if (args[i] instanceof Serializable) {
//                    Class<?> aClass = args[i].getClass();
//                    try {
//                        aClass.getDeclaredMethod("toString", new Class[]{null});
//                        // 如果不抛出NoSuchMethodException 异常则存在 toString 方法 ，安全的writeValueAsString ，否则 走 Object的 toString方法
//                        params.append("  ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
//                    } catch (NoSuchMethodException e) {
//                        params.append("  ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
//                    }
//                } else if (args[i] instanceof MultipartFile) {
//                    MultipartFile file = (MultipartFile) args[i];
//                    params.append("  ").append(paramNames.get(i)).append(": ").append(file.getName());
//                } else {
//                    params.append("  ").append(paramNames.get(i)).append(": ").append(args[i]);
//                }
//            }
//        }
//        return params;
//    }
//
//}
