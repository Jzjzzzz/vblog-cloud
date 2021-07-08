//package team.ark.core.transaction;
//
//import io.seata.core.context.RootContext;
//import io.seata.core.exception.TransactionException;
//import io.seata.tm.api.GlobalTransaction;
//import io.seata.tm.api.GlobalTransactionContext;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import team.ark.core.util.StringUtils;
//
//import java.lang.reflect.Method;
//
///**
// * 动态Seata全局分布式事务切点
// *
// * @author Ashinch
// * @date 2021/01/01
// */
//@Slf4j
//@Aspect
//@Component
//public class SeataAspect {
//    @Before("execution(* team.ark.service..*.*(..))")
//    public void before(JoinPoint joinPoint) throws TransactionException {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        log.info("拦截到需要分布式事务的方法, " + method.getName());
//        // 此处可用redis或者定时任务来获取一个key判断是否需要关闭分布式事务
//        // 模拟动态关闭分布式事务
////        if ((int) (Math.random() * 100) % 2 == 0) {
//        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
//        tx.begin(300000, "test-client");
//        log.info("创建分布式事务 " + RootContext.getXID());
////        } else {
////            System.out.println("关闭分布式事务");
////        }
//    }
//
//    @AfterThrowing(throwing = "e", pointcut = "execution(* team.ark.service..*.*(..)) " +
//            "&& !execution(* io.seata.core.exception..*.*(..))")
//    public void doRecoveryActions(Throwable e) throws TransactionException {
//        log.info("方法执行异常: " + e.getMessage());
//        if (!StringUtils.isBlank(RootContext.getXID())) {
//            log.info("事务回滚: xid[{}]", RootContext.getXID());
//            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//        }
//    }
////    @AfterReturning(value = "execution(* team.ark.service..*.*(..))", returning = "result")
////    public void afterReturning(JoinPoint point, Object result) throws TransactionException {
////        log.info("方法执行结束: " + result);
////        if ((Boolean) result) {
////            if (!StringUtils.isBlank(RootContext.getXID())) {
////                System.out.println("分布式事务Id: " + RootContext.getXID());
////                GlobalTransactionContext.reload(RootContext.getXID()).commit();
////            }
////        }
////    }
//}
