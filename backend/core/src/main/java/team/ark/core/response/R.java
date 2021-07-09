package team.ark.core.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import team.ark.core.exception.ArkAbstractException;

import java.io.Serializable;

/**
 * 响应结果封装
 *
 * @author Ashinch
 * @date 2021/01/01
 */
@Data
@ApiModel(value = "R", description = "响应结果模型")
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class R<T> implements Serializable {
    private static final long serialVersionUID = -8137570074597109327L;
    @ApiModelProperty("响应码")
    private Integer code;
    @ApiModelProperty("响应消息")
    private String message;
    @ApiModelProperty("响应体")
    private T data;

    public static <T> R<T> with(C c, T data) {
        R<T> r = new R<>();
        r.setCode(c.getCode());
        r.setMessage(c.getMessage());
        r.setData(data);
        return r;
    }

    public static <T> R<T> with(C c) {
        return with(c, null);
    }

    public static <T> R<T> with(ArkAbstractException e) {
        R<T> r = new R<>();
        r.setMessage(e.getMessage());
        r.setCode(e.getStatus().value());
        return r;
    }

    public static <T> R<T> ok() {
        R<T> r = with(C.OK);
        r.setData(null);
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = with(C.OK);
        r.setData(data);
        return r;
    }

    public static <T> R<T> error(String message) {
        R<T> r = with(C.ERROR);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> judge(T data) {
        if (data != null) {
            try {
                return (Boolean) data ? ok(data) : with(C.ERROR);
            } catch (ClassCastException e) {
                return ok(data);
            }
        }
        return with(C.ERROR);
    }
}
