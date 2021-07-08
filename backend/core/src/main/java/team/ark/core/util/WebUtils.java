package team.ark.core.util;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import team.ark.core.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Web工具类
 *
 * @author Ashinch
 * @date 2020/12/16
 */
@Slf4j
public class WebUtils extends ServletUtil {
    /**
     * 返回当前 HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
    }

    /**
     * 输出响应结果
     *
     * @param response HttpServletResponse
     * @param r        响应结果
     */
    @SneakyThrows
    @SuppressWarnings("deprecation")
    public static void write(HttpServletResponse response, R r) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.toJson(r));
        out.flush();
        out.close();
    }
}
