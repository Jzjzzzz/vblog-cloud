package team.ark.core.lock;

/**
 * 锁类型枚举
 *
 * @author Ashinch
 * @date 2021/01/01
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum LockType {
    JVM,
    MYSQL,
    REDIS,
}
