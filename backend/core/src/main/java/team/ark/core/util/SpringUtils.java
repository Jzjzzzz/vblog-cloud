package team.ark.core.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * Spring工具类
 *
 * @author Ashinch
 * @date 2021/02/16
 */
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
     * 通过Name获取Bean
     *
     * @param name Bean名称
     * @param <T>  Bean类型
     *
     * @return Bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 通过Class获取Bean
     *
     * @param clazz Bean类
     * @param <T>   Bean类型
     *
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过Name, 以及Clazz返回指定的Bean
     *
     * @param name  Bean名称
     * @param clazz Bean类型
     * @param <T>   Bean类型
     *
     * @return Bean对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 通过request, class获取Bean
     *
     * @param request HttpServletRequest
     * @param clazz   Bean类
     * @param <T>     Bean类型
     *
     * @return Bean对象
     */
    @SuppressWarnings("ConstantConditions")
    public static <T> T getBean(HttpServletRequest request, Class<T> clazz) {
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        return ctx.getBean(clazz);
    }

    /**
     * 通过request, class获取Bean
     *
     * @param request HttpServletRequest
     * @param name    Bean名称
     * @param <T>     Bean类型
     *
     * @return Bean对象
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static <T> T getBean(HttpServletRequest request, String name) {
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        return (T) ctx.getBean(name);
    }

    /**
     * 通过类型参考返回带泛型参数的Bean
     *
     * @param reference 类型参考, 用于持有转换后的泛型类型
     * @param <T>       Bean类型
     *
     * @return 带泛型参数的Bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(TypeReference<T> reference) {
        final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
        final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
        final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
        final String[] beanNames = applicationContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
        return getBean(beanNames[0], rawType);
    }

    /**
     * 获取指定类型对应的所有Bean, 包括子类
     *
     * @param type 类/接口, null表示获取所有bean
     * @param <T>  Bean类型
     *
     * @return 类型对应的bean, Key是Bean注册的Name, Value是Bean
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 获取指定类型对应的Bean名称，包括子类
     *
     * @param type 类/接口, null表示获取所有Bean名称
     *
     * @return Bean名称
     */
    public static String[] getBeanNamesForType(Class<?> type) {
        return applicationContext.getBeanNamesForType(type);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项键名
     *
     * @return 对应配置值
     */
    public static String getProperty(String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }

    /**
     * 获取当前的环境配置, 无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置, 当有多个环境配置时, 只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();
        return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
    }

    /**
     * 动态注册Bean
     *
     * @param beanName Bean名称
     * @param bean     Bean
     * @param <T>      Bean类型
     */
    public static <T> void registerBean(String beanName, T bean) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        context.getBeanFactory().registerSingleton(beanName, bean);
    }
}
