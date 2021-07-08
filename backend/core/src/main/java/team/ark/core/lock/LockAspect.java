package team.ark.core.lock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 各种锁实现切点(JVM锁, 分布式锁)
 *
 * @author Ashinch
 * @date 2021/01/01
 * @see Lock
 */
@Slf4j
@Aspect
@Component
public class LockAspect {
    private final Lock lock = new ReentrantLock();

    @Pointcut("@annotation(team.ark.core.lock.Lock)")
    private void targetMethod() {
    }

    @Around("targetMethod()")
    public Object aroundTarget(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        log.debug("LockAop 环绕方法: [{}]", method.getName());
        Object[] args = point.getArgs();
        log.debug("LockAop 参数: {}", Arrays.toString(args));
        Object proceed;
        lock.lock();
        try {
            log.debug("LockAop 获取锁");
            proceed = point.proceed();
        } finally {
            lock.unlock();
            log.debug("LockAop 释放锁");
        }
        return proceed;
    }
}
