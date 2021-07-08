package team.ark.core.lock;

import java.lang.annotation.*;

/**
 * 各种锁统一注解(JVM锁, 分布式锁)
 *
 * @author Ashinch
 * @date 2021/01/01
 * @see LockType
 * @see LockAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /**
     * 锁类型, 默认JVM锁
     */
    LockType value() default LockType.JVM;

    /**
     * 额外参数
     */
    String extra() default "";
}
