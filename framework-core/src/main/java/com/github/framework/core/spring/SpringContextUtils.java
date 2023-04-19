package com.github.framework.core.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import java.util.Locale;
import java.util.Map;


public class SpringContextUtils implements ApplicationContextAware {
    private final static Logger log = LoggerFactory.getLogger(SpringContextUtils.class);

    public static final int AUTOWIRE_NO = 0;

    public static final int AUTOWIRE_BY_NAME = 1;

    public static final int AUTOWIRE_BY_TYPE = 2;

    public static final int AUTOWIRE_CONSTRUCTOR = 3;

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /** 返回Environment */
    public static Environment getEnvrionment(){
        return applicationContext.getEnvironment();
    }

    /**
     * 动态为某个beanclass 注入spring环境中的bean
     * @param beanClass
     * @param autowireMode
     * @param dependencyCheck
     * @return
     */
    public static Object autowire(final Class<?> beanClass, final int autowireMode, final boolean dependencyCheck) {
        return getApplicationContext().getAutowireCapableBeanFactory().autowire(beanClass, autowireMode,
                dependencyCheck);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(final String name) {
        checkApplicationContext();
        try {
            return (T) applicationContext.getBean(name);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 如果同Class类型配置了多个Bean，只返回第一个
     */
    public static <T> T getBean(final Class<T> clazz) {
        checkApplicationContext();
        try {
            return applicationContext.getBean(clazz);
        }
        catch (final Exception ex) {
            return null;
        }

    }

    public static <T> Map<String, T> getBeansOfType(@Nullable final Class<T> type) {
        checkApplicationContext();
        try {
            return applicationContext.getBeansOfType(type);
        }
        catch (final Exception ex) {
            return null;
        }
    }

    /**
     * 获取一个在SpringContext定义的消息资源的某个消息
     *
     * @return
     */
    public static String getMessage(final String code, final String defaultMessage, final Locale locale,
            final Object... args) {

        try {
            return getApplicationContext().getMessage(code, args, defaultMessage, locale);
        }
        catch (final NoSuchMessageException ex) {
            log.error(ex.getMessage(), ex);
        }
        return code;
    }

    /**
     * 获取一个在SpringContext定义的消息资源的某个消息
     *
     * @return
     */
    public static String getMessage(final String code, final Locale locale, final Object... args) {
        try {
            return getApplicationContext().getMessage(code, args, locale);
        }
        catch (final NoSuchMessageException ex) {
            log.error(ex.getMessage(), ex);
        }
        return code;
    }

    public static String getMessage(final String code, final Object... args) {
        return getMessage(code, Locale.CHINA, args);
    }

    private static void checkApplicationContext() {

        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义Springs");
        }
    }

}
