package ru.fildv.groceryshop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isServiceLayer() {
    }

//    @Pointcut("within(ru.fildv.groceryshop.mapper..*Mapper)")
//    public void isMapperLayer() {
//    }

    @Before("isServiceLayer()")
    public void loggingBeforeMethodCall(JoinPoint joinPoint) {
        log.info("Incoming params for method [ "
                + joinPoint.getTarget().getClass().getSimpleName()
                + "."
                + joinPoint.getSignature().getName()
                + " ]: "
                + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "isServiceLayer()", returning = "result")
    public void loggingAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Outcome result for method [ "
                + joinPoint.getTarget().getClass().getSimpleName()
                + "."
                + joinPoint.getSignature().getName()
                + " ]: " + result);
    }

//    @Before("isMapperLayer()")
//    public void loggingBeforeMapperCall(JoinPoint joinPoint) {
//        log.info("Incoming params for mapper [ "
//                + joinPoint.getTarget().getClass().getSimpleName()
//                + "."
//                + joinPoint.getSignature().getName()
//                + " ]: "
//                + Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @After(value = "isMapperLayer()")
//    public void loggingAfterMapper(JoinPoint joinPoint) {
//        log.info("Ending for mapper [ "
//                + joinPoint.getTarget().getClass().getSimpleName()
//                + "."
//                + joinPoint.getSignature().getName()
//                + " ]");
//    }
}
