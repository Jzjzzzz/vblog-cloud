package team.ark.core.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * JSON工具类
 *
 * @author Ashinch
 * @date 2020/12/11
 */
public class JsonUtils {
    private static final ObjectMapper DEFAULT_MAPPER = getDefaultMapper();

    public static ObjectMapper getDefaultMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        return objectMapper;
    }

    public static ObjectMapper getCompleteMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域， field、get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类是否为final修饰，final修饰的类，比如String、Integer等会抛出异常
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        // 指定序列化时间格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        return objectMapper;
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return toJson(obj, DEFAULT_MAPPER);
    }

    @SneakyThrows
    public static String toJson(Object obj, ObjectMapper customMapper) {
        return customMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static Object toObj(String json, Class<?> type) {
        return toObj(json, type, DEFAULT_MAPPER);
    }

    @SneakyThrows
    public static <T> T toObj(String json) {
        return DEFAULT_MAPPER.readValue(json, new TypeReference<T>() {
        });
    }

    @SneakyThrows
    public static <T> T toObj(String json, ObjectMapper customMapper) {
        return customMapper.readValue(json, new TypeReference<T>() {
        });
    }

    @SneakyThrows
    public static Object toObj(String json, Class<?> type, ObjectMapper customMapper) {
        return customMapper.readValue(json, type);
    }

    @SneakyThrows
    public static Object toObj(InputStream stream, Class<?> type) {
        return toObj(stream, type, DEFAULT_MAPPER);
    }

    @SneakyThrows
    public static <T> T toObj(InputStream stream) {
        return DEFAULT_MAPPER.readValue(stream, new TypeReference<T>() {
        });
    }

    @SneakyThrows
    public static <T> T toObj(InputStream stream, ObjectMapper customMapper) {
        return customMapper.readValue(stream, new TypeReference<T>() {
        });
    }

    @SneakyThrows
    public static Object toObj(InputStream stream, Class<?> type, ObjectMapper customMapper) {
        return customMapper.readValue(stream, type);
    }
}
