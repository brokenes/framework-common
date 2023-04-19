package com.github.framework.core.reflect;

import com.github.framework.core.lang.CustomStringUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName Reflects
 * @Description 反射工具类提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class,
 *              被AOP过的真实类等工具函数.
 * @author henryzhou
 * @author Sam
 * @Date 2019/3/18 14:12
 */
public class Reflects {
    /**
     * set方法名前缀，必须是set
     */
    public static final String SETTER_PREFIX = "set";

    /**
     * get方法名前缀，必须是get
     */
    public static final String GETTER_PREFIX = "get";

    /**
     * spring使用cglib生成代理类的类名分隔符
     */
    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    public static final Object[] EMPTY_OBJS = new Object[] {};

    public static final char JVM_VOID = 'V';


    /**
     * boolean(Z).
     */
    public static final char JVM_BOOLEAN = 'Z';

    /**
     * byte(B).
     */
    public static final char JVM_BYTE = 'B';

    /**
     * char(C).
     */
    public static final char JVM_CHAR = 'C';

    /**
     * double(D).
     */
    public static final char JVM_DOUBLE = 'D';

    /**
     * float(F).
     */
    public static final char JVM_FLOAT = 'F';

    /**
     * int(I).
     */
    public static final char JVM_INT = 'I';

    /**
     * long(J).
     */
    public static final char JVM_LONG = 'J';

    /**
     * short(S).
     */
    public static final char JVM_SHORT = 'S';

    private static final ConcurrentMap<String, Class<?>> DESC_CLASS_CACHE = new ConcurrentHashMap<>();
    public static final String VOID = "void";
    public static final String BOOLEAN = "boolean";
    public static final String BYTE = "byte";
    public static final String CHAR = "char";
    public static final String DOUBLE = "double";
    public static final String FLOAT = "float";
    public static final String INT = "int";
    public static final String LONG = "long";
    public static final String SHORT = "short";
    public static final char CHAR_DOT = '.';
    public static final char CHAR_SEP = '/';
    public static final String MAP = "Map";

    /**
     * 调用Getter方法. 支持多级，如：对象名.对象名.方法
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(final Object obj, final String propertyName) {
        Object object = obj;
        for (final String name : StringUtils.split(propertyName, CustomStringUtils.DOT)) {
            final String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
        }
        return (E) object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。 支持多级，如：对象名.对象名.方法
     */
    public static <E> void invokeSetter(final Object obj, final String propertyName, final E value) {
        Object object = obj;
        final String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                final String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
            } else {
                final String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    @SuppressWarnings("unchecked")
    public static <E> E getFieldValue(final Object obj, final String fieldName) {
        final Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            return null;
        }
        E result = null;
        try {
            result = (E) field.get(obj);
        }
        catch (final IllegalAccessException e) {}
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static <E> void setFieldValue(final Object obj, final String fieldName, final E value) {
        final Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            return;
        }
        try {
            field.set(obj, value);
        }
        catch (final IllegalAccessException e) {}
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                     final Object[] args) {
        if (obj == null || methodName == null) {
            return null;
        }
        final Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            return null;
        }
        try {
            return (E) method.invoke(obj, args);
        }
        catch (final Exception e) {
            final String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        final Method method = getAccessibleMethodByName(obj, methodName, args.length);
        if (method == null) {
            // 如果为空不报错，直接返回空。
            return null;
        }
        try {
            // 类型转换（将参数数据类型转换为目标方法参数类型）
            final Class<?>[] cs = method.getParameterTypes();
            for (int i = 0; i < cs.length; i++) {
                if (args[i] != null && !args[i].getClass().equals(cs[i])) {
                    if (cs[i] == String.class) {
                        args[i] = Objects.toString(args[i]);
                        if (StringUtils.endsWith((String) args[i], ".0")) {
                            args[i] = StringUtils.substringBefore((String) args[i], ".0");
                        }
                    }
                }
            }
            return (E) method.invoke(obj, args);
        }
        catch (final Exception e) {
            final String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        // 为空不报错。直接返回 null
        if (obj == null) {
            return null;
        }
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                final Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (final NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 匹配函数名+参数类型。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes) {
        // 为空不报错。直接返回 null
        if (obj == null) {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
                .getSuperclass()) {
            try {
                final Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (final NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName, final int argsNum) {
        // 为空不报错。直接返回 null
        if (obj == null) {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
                .getSuperclass()) {
            final Method[] methods = searchType.getDeclaredMethods();
            for (final Method method : methods) {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(final Method method) {
        boolean isNotPubicOrFinal = (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible();
        if (isNotPubicOrFinal) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(final Field field) {
        boolean isNotPubicOrFinal = (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible();
        if (isNotPubicOrFinal) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg. public
     * UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class<?> clazz) {
        return (Class<T>) getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 如public UserDao extends
     * HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    public static Class<?> getClassGenricType(final Class<?> clazz, final int index) {

        final Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        final Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class<?>) params[index];
    }

    public static Class<?> getUserClass(final Object instance) {
        if (instance == null) {
            throw new RuntimeException("Instance must not be null");
        }
        final Class<?> clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(final String msg, final Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(msg, e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(msg, ((InvocationTargetException) e).getTargetException());
        }
        return new RuntimeException(msg, e);
    }



    public static Field getClassField(final Class<?> anClass, final String fieldName) {
        return getSubClassField(anClass, fieldName, false);
    }

    public static Field getAccessibleClassField(final Class<?> anClass, final String fieldName) {
        return getSubClassField(anClass, fieldName, true);
    }

    private static Field getSubClassField(final Class<?> anClass, final String fieldName, final boolean anAccess) {
        for (Class<?> superClass = anClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                final Field field = superClass.getDeclaredField(fieldName);
                if (anAccess) {
                    makeAccessible(field);
                }
                return field;
            }
            catch (final NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    public static List<Method> findStaticMethodList(final Class<?> anClass) {
        final List<Method> mmList = new ArrayList<>();
        for (final Method mm : anClass.getMethods()) {
            if (mm.getDeclaringClass() != Object.class) {
                final int kk = mm.getModifiers();
                if (Modifier.isPublic(kk) && Modifier.isStatic(kk)) {
                    mmList.add(mm);
                }
            }
        }
        return mmList;
    }

    public static List<Field> findAllField(final Class<?> anClass, final boolean anAccess) {
        final List<Field> fieldList = new ArrayList<Field>();
        anClass.getFields();
        for (Class<?> superClass = anClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (final Field field : superClass.getDeclaredFields()) {
                if (anAccess) {
                    makeAccessible(field);
                }
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public static Class<?> desc2class(String desc) throws ClassNotFoundException {
        switch (desc.charAt(0)) {
            case JVM_VOID:
                return void.class;
            case JVM_BOOLEAN:
                return boolean.class;
            case JVM_BYTE:
                return byte.class;
            case JVM_CHAR:
                return char.class;
            case JVM_DOUBLE:
                return double.class;
            case JVM_FLOAT:
                return float.class;
            case JVM_INT:
                return int.class;
            case JVM_LONG:
                return long.class;
            case JVM_SHORT:
                return short.class;
            case 'L':
                // "Ljava/lang/Object;" ==>
                desc = desc.substring(1, desc.length() - 1).replace('/', '.');
                // "java.lang.Object"
                break;
            case '[':
                // "[[Ljava/lang/Object;" ==> "[[Ljava.lang.Object;"
                desc = desc.replace('/', '.');
                break;
            default:
                throw new ClassNotFoundException("Class not found: " + desc);
        }

        final ClassLoader cl = getClassLoader();
        Class<?> clazz = DESC_CLASS_CACHE.get(desc);
        if (clazz == null) {
            clazz = Class.forName(desc, true, cl);
            DESC_CLASS_CACHE.put(desc, clazz);
        }

        return clazz;
    }

    public static ClassLoader getClassLoader() {
        final Class<Reflects> cls = Reflects.class;
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (final Throwable ex) {}
        if (cl == null) {
            cl = cls.getClassLoader();
        }
        return cl;
    }

    public static String getDesc(Class<?> c) {
        final StringBuilder ret = new StringBuilder();

        while (c.isArray()) {
            ret.append('[');
            c = c.getComponentType();
        }

        if (c.isPrimitive()) {
            final String t = c.getName();
            if (VOID.equals(t)) {
                ret.append(JVM_VOID);
            } else if (BOOLEAN.equals(t)) {
                ret.append(JVM_BOOLEAN);
            } else if (BYTE.equals(t)) {
                ret.append(JVM_BYTE);
            } else if (CHAR.equals(t)) {
                ret.append(JVM_CHAR);
            } else if (DOUBLE.equals(t)) {
                ret.append(JVM_DOUBLE);
            } else if (FLOAT.equals(t)) {
                ret.append(JVM_FLOAT);
            } else if (INT.equals(t)) {
                ret.append(JVM_INT);
            } else if (LONG.equals(t)) {
                ret.append(JVM_LONG);
            } else if (SHORT.equals(t)) {
                ret.append(JVM_SHORT);
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace(CHAR_DOT, CHAR_SEP));
            ret.append(';');
        }
        return ret.toString();
    }

    public static String getDesc(final Class<?>[] cs) {
        if (cs.length == 0) {
            return CustomStringUtils.EMPTY;
        }

        final StringBuilder sb = new StringBuilder(64);
        for (final Class<?> c : cs) {
            sb.append(getDesc(c));
        }
        return sb.toString();
    }

    public static boolean checkZeroArgConstructor(final Class<?> clazz) {
        try {
            clazz.getDeclaredConstructor();
            return true;
        }
        catch (final NoSuchMethodException e) {
            return false;
        }
    }

    public static StringBuilder fieldToString(final StringBuilder sb, final Object obj)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        final Field[] fields = obj.getClass().getDeclaredFields();
        sb.append(obj.getClass().getName()).append(";");

        for (final Field f : fields) {
            f.setAccessible(true);
            sb.append(f.getName()).append("=").append(f.get(obj)).append(";");
        }
        return sb;
    }

    private static Method[] findParentMethods(final Class<?> anClass) {
        final List<Method> mList = new ArrayList<Method>(20);
        for (final Class<?> cc : anClass.getInterfaces()) {
            for (final Method mm : cc.getMethods()) {
                mList.add(mm);
            }
        }

        return mList.toArray(new Method[mList.size()]);
    }

    /**
     *
     * @Title: workForOverride
     * @Description: 查找父辈的定义方法信息
     * @param anClass
     * @param overrideMethod
     * @return TODO(说明)
     * @throws @author zhoucy
     * @date 2017年10月31日 上午9:38:46
     */
    public static Method[] workForOverride(final Class<?> anClass, final Map<String, Method> overrideMethod) {

        final List<Method> mList = new ArrayList<Method>();
        boolean findOver = false;
        final Method[] anMethods = findParentMethods(anClass);
        final Method[] defMethods = anClass.getDeclaredMethods();
        mList.addAll(Arrays.asList(defMethods));

        for (final Method workMethod : anMethods) {

            findOver = false;
            for (final Method mm : defMethods) {
                if (workMethod.getName().equals(mm.getName())) {
                    if (workMethod.getReturnType().equals(mm.getReturnType())) {
                        overrideMethod.put(mm.getName(), workMethod);
                        findOver = true;
                        break;
                    }
                }
            }
            if (findOver == false) {

                mList.add(workMethod);
            }
        }

        return mList.toArray(new Method[mList.size()]);
    }

    /**
     * <p>
     * 通过基类的方法覆盖超类的方法
     * </p>
     */
    public Method overrideMethod(final Class<?> anClass, final Method workMethod) {
        final Method[] defMethods = anClass.getDeclaredMethods();
        for (final Method mm : defMethods) {
            if (workMethod.getDeclaringClass() == mm.getDeclaringClass()) {
                return workMethod;
            } else if (workMethod.getName().equals(mm.getName())) {
                if (workMethod.getReturnType().equals(mm.getReturnType())) {

                    return mm;
                }
            }
        }
        return workMethod;
    }

    public static Method findGetterMethod(final String anPpropertyName, final Class<?> anClass) {
        try {
            final PropertyDescriptor propDesc = new PropertyDescriptor(anPpropertyName, anClass);
            return propDesc.getReadMethod();
        }
        catch (final Exception ex) {
            return null;
        }
    }

    /**
     * 反射设置值
     *
     * @param object 对象
     * @param method 方法
     * @param args   方法参数对象
     * @throws ReflectiveOperationException 反射操作异常
     */
    public static void setRefValue(final Object object, final Method method, final Object... args)
            throws ReflectiveOperationException {
        method.invoke(object, args);
    }

    /**
     * 有setter方法的字段及其setter方法
     *
     * @param clazz Class对象
     * @return 有setter方法的 字段及其setter方法
     * @throws IntrospectionException 内省异常
     */
    public static Map<Field, Method> fieldAndSetterMethod(final Class<?> clazz) throws IntrospectionException {
        final Map<Field, Method> map = new LinkedHashMap<>();
        final BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (final Field field : clazz.getDeclaredFields()) {
            for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getName().equals(field.getName())
                        && propertyDescriptor.getWriteMethod() != null) {
                    map.put(field, propertyDescriptor.getWriteMethod());
                }
            }
        }
        return map;
    }

    /**
     * @Title: findInterfaceMatchAnnotation
     * @Description: 根据定义注解class查找需要的Class
     * @param anClass 需要的Class
     * @param annoCls 需要的注解Class
     * @return 匹配的Class对象
     * @throws
     * @author chenz
     * @date 2019年3月8日 下午3:33:42
     */
    public static Class<?> findInterfaceMatchAnnotation(final Class<?> anClass,
                                                        final Class<? extends Annotation> annoCls) {
        Annotation anno = anClass.getAnnotation(annoCls);
        if (anno != null) {
            return anClass;
        }
        final Class<?>[] clsA = anClass.getInterfaces();
        for (final Class<?> cls : clsA) {
            anno = cls.getAnnotation(annoCls);
            if (anno != null) {
                return cls;
            }
        }
        return null;
    }

    /**
     * 根据定义路径查找需要的Class
     *
     * @param anClassName 需要的Class
     * @return
     */
    public static Class<?> findClassByName(final String anClassName, final List<String> anPackgeList) {
        if (StringUtils.isBlank(anClassName)) {

            return null;
        }
        for (String tmpClassPath : anPackgeList) {
            if (StringUtils.isNotBlank(tmpClassPath)) {
                tmpClassPath = tmpClassPath.trim().concat(".").concat(anClassName.trim());
            } else {
                tmpClassPath = anClassName;
            }
            try {
                if (tmpClassPath.indexOf(MAP) > 0) {
                    System.out.println(tmpClassPath);
                }
                final Class<?> cc = Class.forName(tmpClassPath);
                return cc;
            }
            catch (final ClassNotFoundException ex) {

            }
        }
        throw new RuntimeException("declare class " + anClassName + ", not fund in declare Packages");
    }

    /**
     * @Title: getClassList
     * @Description: 根据定义路径查找需要的Class
     * @param pkgName 要查询的包路径
     * @param isRecursive 是否递归查询
     * @param annotation 注解的Class对象
     * @return 匹配的Class集合
     * @throws
     * @author chenz
     * @date 2019年3月8日 下午3:42:25
     */
    public static List<Class<?>> getClassList(final String pkgName, final boolean isRecursive,
                                              final Class<? extends Annotation> annotation) {
        final List<Class<?>> classList = new ArrayList<>();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            // 按文件的形式去查找
            final String strFile = pkgName.replaceAll("\\.", "/");
            final Enumeration<URL> urls = loader.getResources(strFile);
            while (urls.hasMoreElements()) {
                final URL url = urls.nextElement();
                if (url != null) {
                    final String protocol = url.getProtocol();
                    final String pkgPath = url.getPath();
                    if ("file".equals(protocol)) {
                        // 本地自己可见的代码
                        findClassName(classList, pkgName, pkgPath, isRecursive, annotation);
                    } else if ("jar".equals(protocol)) {
                        // 引用第三方jar的代码
                        findClassName(classList, pkgName, url, isRecursive, annotation);
                    }
                }
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }

        return classList;
    }

    /**
     * @Title: findClassName
     * @Description: 根据定义路径查找需要的Class
     * @param clazzList 保存class对象的结合
     * @param pkgName 完整的包名称
     * @param pkgPath 搜索的文件路径
     * @param isRecursive 是否递归查询
     * @param annotation 注解的Class对象
     * @throws
     * @author chenz
     * @date 2019年3月8日 下午3:44:15
     */
    public static void findClassName(final List<Class<?>> clazzList, final String pkgName, final String pkgPath,
                                     final boolean isRecursive, final Class<? extends Annotation> annotation) {
        if (clazzList == null) {
            return;
        }
        // 过滤出.class文件及文件夹
        final File[] files = filterClassFiles(pkgPath);
        if (files != null) {
            for (final File f : files) {
                final String fileName = f.getName();
                if (f.isFile()) {
                    // .class 文件的情况
                    final String clazzName = getClassName(pkgName, fileName);
                    addClassName(clazzList, clazzName, annotation);
                } else {
                    // 文件夹的情况
                    if (isRecursive) {
                        // 需要继续查找该文件夹/包名下的类
                        final String subPkgName = pkgName + "." + fileName;
                        final String subPkgPath = pkgPath + "/" + fileName;
                        findClassName(clazzList, subPkgName, subPkgPath, true, annotation);
                    }
                }
            }
        }
    }

    /**
     * 第三方Jar类库的引用。<br/>
     *
     * @throws IOException
     */
    public static void findClassName(final List<Class<?>> clazzList, final String pkgName, final URL url,
                                     final boolean isRecursive, final Class<? extends Annotation> annotation) throws IOException {
        final JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        final JarFile jarFile = jarURLConnection.getJarFile();
        final Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            final JarEntry jarEntry = jarEntries.nextElement();
            final String jarEntryName = jarEntry.getName();
            String clazzName = jarEntryName.replace("/", ".");
            int endIndex = clazzName.lastIndexOf(".");
            String prefix = null;
            if (endIndex > 0) {
                clazzName = clazzName.substring(0, endIndex);
                endIndex = clazzName.lastIndexOf(".");
                if (endIndex > 0) {
                    prefix = clazzName.substring(0, endIndex);
                }
            }
            if (prefix != null && jarEntryName.endsWith(".class")) {
                if (prefix.equals(pkgName)) {
                    addClassName(clazzList, clazzName, annotation);
                } else if (isRecursive && prefix.startsWith(pkgName)) {
                    // 遍历子包名：子类
                    addClassName(clazzList, clazzName, annotation);
                }
            }
        }
    }

    private static File[] filterClassFiles(final String pkgPath) {
        if (pkgPath == null) {
            return null;
        }
        // 接收 .class 文件 或 类文件夹
        return new File(pkgPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.isFile() && file.getName().endsWith(".class") || file.isDirectory();
            }
        });
    }

    private static String getClassName(final String pkgName, final String fileName) {
        final int endIndex = fileName.lastIndexOf(".");
        String clazz = null;
        if (endIndex >= 0) {
            clazz = fileName.substring(0, endIndex);
        }
        String clazzName = null;
        if (clazz != null) {
            clazzName = pkgName + "." + clazz;
        }
        return clazzName;
    }

    private static void addClassName(final List<Class<?>> clazzList, final String clazzName,
                                     final Class<? extends Annotation> annotation) {
        if (clazzList != null && clazzName != null) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(clazzName);
            }
            catch (final ClassNotFoundException e) {}
            catch (final Exception e) {}
            catch (final Error e) {}
            if (clazz != null) {
                if (annotation == null) {
                    clazzList.add(clazz);
                } else if (clazz.isAnnotationPresent(annotation)) {
                    clazzList.add(clazz);
                }
            }
        }
    }

    public static List<Method> findClassReadMethods(final Class<?> anClass) {

        return findClassReadMethods(anClass, true);
    }

    public static List<Method> findClassReadMethods(final Class<?> anClass, final boolean anReader) {
        final PropertyUtilsBean utilsBean = new PropertyUtilsBean();
        final List<Method> list = new ArrayList<Method>();
        Method mm;
        for (final PropertyDescriptor pds : utilsBean.getPropertyDescriptors(anClass)) {
            if (anReader) {
                mm = utilsBean.getReadMethod(pds);
            } else {
                mm = utilsBean.getWriteMethod(pds);
            }
            if (mm != null) {
                list.add(mm);
            } else {}
        }

        return list;
    }

    public static Map<String, Method> findReadMethods(final Class<?> anClass) {

        return findMethodMap(anClass, true);
    }

    public static Map<String, Method> findMethodMap(final Class<?> anClass, final boolean anReader) {
        final Map<String, Method> mapMethod = new LinkedCaseInsensitiveMap<>();
        String tmpName;
        final List<Method> methodList = findClassReadMethods(anClass, anReader);
        for (final Method mm : methodList) {
            if (mm == null) {
                continue;
            }

            tmpName = StringUtils.uncapitalize(mm.getName().substring(3));
            mapMethod.put(tmpName, mm);
        }

        return mapMethod;
    }

    private Reflects() {}

}
