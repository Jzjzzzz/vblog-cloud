package team.ark.core.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * String工具类
 *
 * @author Ashinch
 * @date 2020/12/18
 */
public class StringUtils extends org.springframework.util.StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";

    public static boolean isNotEmpty(final CharSequence str) {
        return !org.springframework.util.StringUtils.isEmpty(str);
    }

    public static boolean isBlank(final CharSequence str) {
        return !org.springframework.util.StringUtils.hasText(str);
    }

    public static boolean isNotBlank(final CharSequence str) {
        return org.springframework.util.StringUtils.hasText(str);
    }

    public static String joinWith(final String separator, Object... objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Object varargs must not be null");
        }
        if (objects instanceof String[]) {
            objects = Arrays.stream(objects).map(s -> s.toString().trim()).toArray();
        }
        final String sanitizedSeparator = defaultString(separator);
        final StringBuilder result = new StringBuilder();
        final Iterator<Object> iterator = Arrays.asList(objects).iterator();
        while (iterator.hasNext()) {
            final String value = Objects.toString(iterator.next(), "");
            result.append(value);
            if (iterator.hasNext()) {
                result.append(sanitizedSeparator);
            }
        }
        return result.toString();
    }

    public static String defaultString(final String str, final String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static String defaultString(final String str) {
        return defaultString(str, EMPTY);
    }
}
