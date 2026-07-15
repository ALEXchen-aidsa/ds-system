package com.ds.common;

import com.ds.entity.OperationLog;
import com.ds.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {

    private final OperationLogService logService;

    public LogAspect(OperationLogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* com.ds.controller..*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog log = new OperationLog();

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                log.setIp(request.getRemoteAddr());
            }

            log.setModule(joinPoint.getTarget().getClass().getSimpleName());
            log.setOperation(joinPoint.getSignature().getName());
            log.setMethod(joinPoint.getSignature().toShortString());
            log.setStatus(1);

            Object result = joinPoint.proceed();
            log.setConsumeTime(System.currentTimeMillis() - startTime);
            logService.saveLog(log);
            return result;
        } catch (Throwable e) {
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            log.setConsumeTime(System.currentTimeMillis() - startTime);
            logService.saveLog(log);
            throw e;
        }
    }
}