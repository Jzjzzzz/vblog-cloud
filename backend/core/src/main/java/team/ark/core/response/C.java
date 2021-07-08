package team.ark.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author Ashinch
 * @date 2021/01/01
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum C {
    OK(200, "成功"),
    ERROR(500, "失败"),
    /**
     * 参数错误: 1001 - 1999
     */
    NULL(1001, "数据为空"),
    /**
     * 认证错误: 2001 - 2999
     */
    LOGIN_SUCCESS(200, "登录成功"),
    USER_NOT_LOGIN(2001, "未登录"),
    ACCESS_DENIED(2002, "权限不足"),
    USER_NOT_FOUND(2003, "用户不存在"),
    BAD_CREDENTIALS(2004, "用户名或密码错误"),

    ;
    /**
     * 响应码
     */
    private final Integer code;
    /**
     * 响应消息
     */
    private final String message;
}
