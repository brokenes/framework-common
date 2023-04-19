package com.github.framework.core.reflect;


import com.github.framework.core.collection.CustomList;
import com.github.framework.core.exception.Ex;
import com.github.framework.core.exception.SystemErrorException;
import com.github.framework.core.lang.CustomStringUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 包装Class对象
 * (代码来自Nutz开源框架中的Mirror类，这里只是整理了一下)
 *
 *
 */
public class ClassWrapper<T> {

    protected static Logger logger = LoggerFactory.getLogger(ClassWrapper.class);

    public static final String PREFIX_IS = "is";
    private static final Pattern PTN = Pattern.compile("(<)(.+)(>)");
    private Class<T> klass;

    private ClassWrapper(Class<T> cls) {
        this.klass = cls;
    }

    @SuppressWarnings("unchecked")
    public static <T> ClassWrapper<T> wrapByName(String type) {
        Class<T> cls = null;
        try {
            cls = (Class<T>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new SystemErrorException( e);
        }
        return wrap(cls);
    }

    public static <T> ClassWrapper<T> wrap(Class<T> cls) {
        return new ClassWrapper(cls);
    }

    @SuppressWarnings("unchecked")
    public static <T> ClassWrapper<T> wrap(T o) {
        return (ClassWrapper<T>) wrap(o.getClass());
    }

    /**
     * 根据传进来的参数找到匹配的构造函数实例化一个新的对象
     *
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public T newOne(Object... args) {
        Class[] paramTypes = ClassWrapper.getTypes(args);

        if (CustomList.isEmpty(paramTypes)) {
            try {
                return klass.newInstance();
            } catch (Exception e) {
                
                throw Ex.systemError( e, "Can't new instance of '%s',please check the source code.", klass.getName());
            }
        }
        Constructor isme = getConstructor(paramTypes);

        try {
            return (T) isme.newInstance(args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     */
    @SuppressWarnings("unchecked")
    public Class getSuperClassGenricType(final int index) {

        Type genType = klass.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }


    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public Method getMethod(String methodName, Class<?>... parameterTypes) {

        for (Class<?> superClass = klass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                logger.info("循环向上转型,获取对象的DeclaredMethod {}", e);
            }
        }
        return null;
    }

    /**
     * 根据Annotation查找方法
     *
     * @param ann
     * @return
     */
    public <T extends Annotation> Method[] getMethods(Class<T> ann) {
        Method[] methods = getMethods();
        List<Method> methodList = new ArrayList<Method>();
        for (Method m : methods) {
            if (m.getAnnotation(ann) != null) {
                methodList.add(m);
            }
        }
        return methodList.toArray(new Method[]{});
    }

    /**
     * 根据Annotation和方法名查找某个方法，如果出现有重载方法则只返回第一找到的
     *
     * @param <T>
     * @param methodName
     * @param ann
     * @return
     */
    public <T extends Annotation> Method getMethod(String methodName, Class<T> ann) {
        Method[] methods = getMethods(ann);
        for (Method m : methods) {
            if (CustomStringUtils.equals(methodName, m.getName())) {
                return m;
            }
        }
        return null;
    }


    /**
     * 返回被包装的类的名称
     *
     * @return
     */
    public String getName() {
        return klass.getName();
    }

    /**
     * 根据指定的参数类型数组查找某个匹配的构造器
     *
     * @param paramTypes 参数类型
     * @return
     */
    public Constructor getConstructor(Class... paramTypes) {
        Constructor[] creators = klass.getDeclaredConstructors();
        for (Constructor c : creators) {
            Class[] types = c.getParameterTypes();
            if (CustomList.isEmpty(paramTypes) && CustomList.isEmpty(types)) {
                return c;
            }
            if (CustomList.isNotEmpty(types) && types.length == paramTypes.length && isEquals(types, paramTypes)) {
                return c;
            }
        }
        throw Ex.systemError( "Can't find the Constructor!");

    }

    private boolean isEquals(Class[] types, Class... paramTypes) {
        boolean equals = true;
        for (int i = 0, len = types.length; i < len; i++) {
            if (!isChildOf(types[i], paramTypes[i])) {
                equals = false;
                break;
            }
        }
        return equals;
    }


    /**
     * 根据名称获取一个 Getter
     * <p>
     * 比如，你想获取 abc 的 getter ，那么优先查找 getAbc()，如果 没有，则查找 abc()。
     *
     * @param fieldName
     * @return 方法
     */
    public Method getGetter(String fieldName) {
        try {
            String fn = CustomStringUtils.capitalize(fieldName);
            Method varMethod = getMethodByName("get" + fn);
            if (varMethod != null) {
                return varMethod;
            }
            varMethod = getMethodByName("is" + fn);

            if (varMethod != null && !ClassWrapper.wrap(varMethod.getReturnType()).isBoolean()) {
                return getMethodByName(fieldName);
            }
            return varMethod;
        } catch (Exception e) {
            throw Ex.systemError(e,"Fail to find getter for [%s]->[%s]", klass.getName(), fieldName);
        }

    }

    /**
     * 根据字段获取一个 Getter。
     * <p>
     * 比如，你想获取 abc 的 getter ，那么优先查找 getAbc()，如果 没有，则查找 abc()。
     *
     * @param field
     * @return 方法
     */
    public Method getGetter(Field field) {
        try {
            String fn = CustomStringUtils.capitalize(field.getName());
            Method varMethod;
            if (ClassWrapper.wrap(field.getType()).is(boolean.class)) {
                varMethod = getMethodByName("is" + fn);
                if (varMethod != null) {
                    return varMethod;
                }
            } else {
                varMethod = getMethodByName("get" + fn);
                if (varMethod != null) {
                    return varMethod;
                }
            }

            return getMethodByName(field.getName());
        } catch (Exception e) {
            throw Ex.systemError(e,   "Fail to find getter for [%s]->[%s]", klass.getName(), field.getName());
        }
    }

    /**
     * 根据一个字段获取 Setter
     * <p>
     * 比如，你想获取 abc 的 setter ，那么优先查找 setAbc(T abc)，如果 没有，则查找 abc(T abc)。
     *
     * @param field 字段
     * @return 方法
     */
    public Method getSetter(Field field) {
        try {
            Method varMethod = getMethodByName("set" + CustomStringUtils.capitalize(field.getName()), field.getType());
            if (varMethod != null) {
                return varMethod;
            }

            if (field.getName().startsWith(PREFIX_IS) && ClassWrapper.wrap(field.getType()).is(boolean.class)) {
                varMethod = getMethodByName("set" + field.getName().substring(2), field.getType());
                if (varMethod != null) {
                    return varMethod;
                }
            }
            return getMethodByName(field.getName(), field.getType());

        } catch (Exception e) {
            throw Ex.systemError(e,  "Fail to find setter for [%s]->[%s]", klass.getName(), field.getName());
        }
    }

    /**
     * 根据一个字段名、字段类型获取 Setter
     *
     * @param fieldName 字段名
     * @param paramType 字段类型
     * @return 方法
     * @throws NoSuchMethodException 没找到 Setter
     */
    public Method getSetter(String fieldName, Class<?> paramType) {
        try {
            String setterName = getSetterName(fieldName);
            Method varMethod = getMethodByName(setterName, paramType);
            if (varMethod != null) {
                return varMethod;
            }

            varMethod = getMethodByName(fieldName, paramType);
            if (varMethod != null) {
                return varMethod;
            }

            ClassWrapper<?> type = ClassWrapper.wrap(paramType);
            for (Method method : klass.getMethods()) {
                boolean flag1 = method.getParameterTypes().length == 1;
                boolean flag2 = method.getName().equals(setterName) || method.getName().equals(fieldName);
                boolean flag3 = null == paramType || type.canCastToDirectly(method.getParameterTypes()[0]);
                if (flag1 && flag2 && flag3) {
                    return method;
                }
            }

            throw Ex.systemError( "根据一个字段名了字段类型获取 Setter");
        } catch (Exception e) {
            throw Ex.systemError(e,   "Fail to find setter for [%s]->[%s(%s)]", klass.getName(), fieldName, null == paramType ? paramType : paramType.getName());
        }

    }

    private Method getMethodByName(String name) {
        try {
            return klass.getMethod(name);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Method getMethodByName(String name, Class<?> paramType) {
        try {
            return klass.getMethod(name, paramType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }


    /**
     * @param fieldName 字段名
     * @return Setter 的名字
     */
    public static String getSetterName(String fieldName) {
        return new StringBuilder("set").append(CustomStringUtils.capitalize(fieldName)).toString();
    }

    /**
     * @param fieldName 字段名
     * @return Bool 型的 Setter 的名字。如果字段名以 "is"开头，会被截去
     */
    public static String getBooleanSetterName(String fieldName) {
        String str = fieldName;
        if (str.startsWith(PREFIX_IS)) {
            str = fieldName.substring(2);
        }
        return new StringBuilder("set").append(CustomStringUtils.capitalize(str)).toString();
    }

    /**
     * @param fieldName 字段名
     * @return Getter 的名字
     */
    public static String getGetterName(String fieldName) {
        return new StringBuilder("get").append(CustomStringUtils.capitalize(fieldName)).toString();
    }

    /**
     * @param fieldName 字段名
     * @return Bool 型的 Getter 的名字。以 "is"开头
     */
    public static String getBooleanGetterName(String fieldName) {
        String tmpFieldName = fieldName;
        if (fieldName.startsWith(PREFIX_IS)) {
            tmpFieldName = fieldName.substring(PREFIX_IS.length());
        }
        return new StringBuilder(PREFIX_IS).append(CustomStringUtils.capitalize(tmpFieldName)).toString();
    }


    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     */
    public Object invoke(final Object object, final String methodName,
                         final Class<?>[] parameterTypes,
                         final Object[] parameters) {
        Method method = ClassWrapper.wrap(object).getMethod(methodName, parameterTypes);
        if (method == null) {
            throw Ex.systemError("Could not find method [%s] on target [%s]",  methodName,object);
        }
        method.setAccessible(true);
        try {
            return method.invoke(object, parameters);
        } catch (Exception e) {
            throw Ex.systemError(e, "An error on invoke method[%s] on target [%s]", method, object);
        }
    }

    /**
     * 根据一个字段名，获取一组有可能成为 Setter 函数
     *
     * @param fieldName
     * @return 函数数组
     */
    public Method[] findSetters(String fieldName) {
        String mName = "set" + CustomStringUtils.capitalize(fieldName);
        ArrayList<Method> ms = new ArrayList<>();
        for (Method m : this.klass.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.getParameterTypes().length != 1) {
                continue;
            }
            if (m.getName().equals(mName)) {
                ms.add(m);
            }
        }
        return ms.toArray(new Method[ms.size()]);
    }

    /**
     * 获取一个字段。这个字段可以是当前类型或者其父类的私有字段。
     *
     * @param name 字段名
     * @return 字段
     * @throws NoSuchFieldException
     */
    public Field getField(String name) {
        Class<?> theClass = klass;
        Field f;
        while (null != theClass && theClass != Object.class) {
            try {
                f = theClass.getDeclaredField(name);
                return f;
            } catch (NoSuchFieldException e) {
                theClass = theClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取一个字段。这个字段必须声明特殊的注解，第一遇到的对象会被返回
     *
     * @param ann 注解
     * @return 字段
     * @throws NoSuchFieldException
     */
    public <T extends Annotation> Field getField(Class<T> ann) {
        for (Field field : this.getFields()) {
            if (null != field.getAnnotation(ann)) {
                return field;
            }
        }

        throw Ex.systemError("Can NOT find field [@%s] in class [%s] and it's parents classes",
                ann.getName(),
                klass.getName());
    }

    /**
     * 获取一组声明了特殊注解的字段
     *
     * @param ann 注解类型
     * @return 字段数组
     */
    public <T extends Annotation> Field[] getFields(Class<T> ann) {
        List<Field> fields = new LinkedList<>();
        for (Field f : this.getFields()) {
            if (null != f.getAnnotation(ann)) {
                fields.add(f);
            }
        }
        return fields.toArray(new Field[fields.size()]);
    }

    public List<Field> getFieldsByType(Class<?> type) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = klass.getDeclaredFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.getType().isAssignableFrom(type)) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    private static boolean isIgnoredField(Field f) {
        if (Modifier.isStatic(f.getModifiers())) {
            return true;
        }
        if (Modifier.isFinal(f.getModifiers())) {
            return true;
        }
        return f.getName().startsWith("this$");
    }

    /**
     * 获得所有的属性，包括私有属性。不包括 Object 的属性
     */
    public Field[] getFields() {
        Class<?> theClass = klass;
        Map<String, Field> list = new HashMap<>(50);
        while (null != theClass && theClass != Object.class) {
            Field[] fs = theClass.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                if (isIgnoredField(fs[i]) || list.containsKey(fs[i].getName())) {
                    continue;
                }
                list.put(fs[i].getName(), fs[i]);
            }
            theClass = theClass.getSuperclass();
        }
        return list.values().toArray(new Field[list.size()]);
    }

    /**
     * 获取本类型所有的方法，包括私有方法。不包括 Object 的方法
     */
    public Method[] getMethods() {
        Class<?> theClass = klass;
        List<Method> list = new LinkedList<>();
        while (null != theClass && theClass != Object.class) {
            Method[] ms = theClass.getDeclaredMethods();
            for (int i = 0; i < ms.length; i++) {
                list.add(ms[i]);
            }
            theClass = theClass.getSuperclass();
        }
        return list.toArray(new Method[list.size()]);
    }


    /**
     * 获取当前对象，所有的方法，包括私有方法。递归查找至自己某一个父类为止
     * <p>
     * 并且这个按照名称，消除重复的方法。子类方法优先
     *
     * @param top 截至的父类
     * @return 方法数组
     */
    public Method[] getAllDeclaredMethods(Class<?> top) {
        Class<?> cc = klass;
        HashMap<String, Method> map = new HashMap<>(50);
        while (null != cc && cc != Object.class) {
            Method[] fs = cc.getDeclaredMethods();
            for (int i = 0; i < fs.length; i++) {
                String key = fs[i].getName() + ClassWrapper.getParamDescriptor(fs[i].getParameterTypes());
                if (!map.containsKey(key)) {
                    map.put(key, fs[i]);
                }
            }
            cc = cc.getSuperclass() == top ? null : cc.getSuperclass();
        }
        return map.values().toArray(new Method[map.size()]);
    }

    /**
     * 相当于 getAllDeclaredMethods(Object.class)
     *
     * @return 方法数组
     */
    public Method[] getAllDeclaredMethodsWithoutTop() {
        return getAllDeclaredMethods(Object.class);
    }

    /**
     * 获取所有静态方法
     *
     * @return 所有静态方法
     */
    public Method[] getStaticMethods() {
        List<Method> list = new LinkedList<>();
        for (Method m : klass.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
                list.add(m);
            }
        }
        return list.toArray(new Method[]{});
    }

    private static RuntimeException makeSetValueException(Class<?> type,
                                                          String name,
                                                          Object value,
                                                          Exception e) {
        throw Ex.systemError(e, "Fail to set value [%s] to [%s]->[%s] because '%s'", value, type.getName(), name);
    }


    /**
     * 为对象的一个字段设值。 不会调用对象的 setter，直接设置字段的值
     *
     * @param obj   对象
     * @param field 字段
     * @param val   值。如果为 null，字符和数字字段，都会设成 0
     */
    public void setValue(Object obj, Field field, Object val) {
        Object value = val;
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        ClassWrapper<?> me = ClassWrapper.wrap(field.getType());
        if (null != value) {
            try {
                if (!ClassWrapper.wrap(value.getClass()).canCastToDirectly(me.getWrapClass())) {
                    value = ConvertUtils.convert(value, field.getType());
                }
            } catch (Exception e) {
                throw makeSetValueException(obj.getClass(), field.getName(), value, e);
            }
        } else {
            if (me.isNumber()) {
                value = 0;
            } else if (me.isChar()) {
                value = (char) 0;
            }
        }
        try {
            field.set(obj, value);
        } catch (Exception e) {
            throw makeSetValueException(obj.getClass(), field.getName(), value, e);
        }
    }

    /**
     * 为对象的一个字段设值。优先调用 setter 方法
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param value     值
     */
    public void setValue(Object obj, String fieldName, Object value) {
        try {
            this.getSetter(fieldName, value.getClass()).invoke(obj, value);
        } catch (Exception e) {
            try {
                Field field = this.getField(fieldName);
                setValue(obj, field, value);
            } catch (Exception e1) {
                throw makeSetValueException(obj.getClass(), fieldName, value, e1);
            }
        }
    }

    private static RuntimeException makeGetValueException(Class<?> type, String name) {
        return Ex.systemError(  "Fail to get value for [%s]->[%s]", type.getName(), name);
    }

    /**
     * 不调用 getter，直接获得字段的值
     *
     * @param obj 对象
     * @param f   字段
     * @return 字段的值。
     */
    public Object getValue(Object obj, Field f) {
        try {
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            return f.get(obj);
        } catch (Exception e) {
            throw makeGetValueException(obj.getClass(), f.getName());
        }
    }

    /**
     * 优先通过 getter 获取字段值，如果没有，则直接获取字段值
     *
     * @param obj  对象
     * @param name 字段名
     * @return 字段值
     */
    public Object getValue(Object obj, String name) {
        try {
            return this.getGetter(name).invoke(obj);
        } catch (Exception e) {
            try {
                Field f = getField(name);
                return getValue(obj, f);
            } catch (Exception e1) {
                throw makeGetValueException(obj.getClass(), name);
            }
        }
    }

    /**
     * 判断当前对象是否为一个类型。精确匹配，即使是父类和接口，也不相等
     *
     * @param type 类型
     * @return 是否相等
     */
    public boolean is(Class<?> type) {
        if (null == type) {
            return false;
        }
        return klass == type;
    }

    /**
     * 判断当前对象是否为一个类型。精确匹配，即使是父类和接口，也不相等
     *
     * @param className 类型名称
     * @return 是否相等
     */
    public boolean is(String className) {
        return CustomStringUtils.equals(klass.getName(), className);
    }

    /**
     * 判断当前对象是否为一个类型的子类或者接口的实现类
     *
     * @param type 类型或接口名
     * @return 当前对象是否为一个类型的子类，或者一个接口的实现类
     */
    public boolean isOf(Class<?> type) {
        return type.isAssignableFrom(klass);
    }

    /**
     * @return 当前对象是否为字符串
     */
    public boolean isString() {
        return is(String.class);
    }

    /**
     * @return 当前对象是否为CharSequence的子类
     */
    public boolean isStringLike() {
        return CharSequence.class.isAssignableFrom(klass);
    }

    /**
     * @return 当前对象是否为字符
     */
    public boolean isChar() {
        return is(char.class) || is(Character.class);
    }

    /**
     * @return 当前对象是否为枚举
     */
    public boolean isEnum() {
        return klass.isEnum();
    }

    /**
     * @return 当前对象是否为布尔
     */
    public boolean isBoolean() {
        return is(boolean.class) || is(Boolean.class);
    }

    /**
     * @return 当前对象是否为浮点
     */
    public boolean isFloat() {
        return is(float.class) || is(Float.class);
    }

    /**
     * @return 当前对象是否为双精度浮点
     */
    public boolean isDouble() {
        return is(double.class) || is(Double.class);
    }

    /**
     * @return 当前对象是否为整型
     */
    public boolean isInt() {
        return is(int.class) || is(Integer.class);
    }

    /**
     * @return 当前对象是否为整数（包括 int, long, short, byte）
     */
    public boolean isIntLike() {
        return isInt() || isLong() || isShort() || isByte() || is(BigDecimal.class);
    }

    /**
     * @return 当前对象是否为小数 (float, dobule)
     */
    public boolean isDecimal() {
        return isFloat() || isDouble();
    }

    /**
     * @return 当前对象是否为长整型
     */
    public boolean isLong() {
        return is(long.class) || is(Long.class);
    }

    /**
     * @return 当前对象是否为短整型
     */
    public boolean isShort() {
        return is(short.class) || is(Short.class);
    }

    /**
     * @return 当前对象是否为字节型
     */
    public boolean isByte() {
        return is(byte.class) || is(Byte.class);
    }

    public Class<T> getWrapClass() {
        return klass;
    }

    /**
     * 判断传进来的类型是否是原型包装类型的一种
     *
     * @param type 类型
     * @return 否为一个对象的外覆类
     */
    public boolean isPrimitiveWrapClass(Class<?> type) {
        try {
            return ClassWrapper.wrap(type).getPrimitiveWrapClass() == klass;
        } catch (Exception e) {
            logger.info("判断传进来的类型是否是原型包装类型的一种异常，返回false。 {}", e);
        }
        return false;
    }

    /**
     * @param type 目标类型
     * @return 判断当前对象是否能直接转换到目标类型，而不产生异常
     */
    public boolean canCastToDirectly(Class<?> type) {
        if (klass == type) {
            return true;
        }
        if (type.isAssignableFrom(klass)) {
            return true;
        }
        boolean flag = klass.isPrimitive() && type.isPrimitive() && this.isPrimitiveNumber() && ClassWrapper.wrap(type).isPrimitiveNumber();

        if (flag) {
            return true;
        }

        try {
            return ClassWrapper.wrap(type).getPrimitiveWrapClass() == this.getPrimitiveWrapClass();
        } catch (Exception e) {
            logger.info("判断当前对象是否能直接转换到目标类型异常，返回false。 {}", e);
        }
        return false;
    }

    /**
     * @return 当前对象是否为原生的数字类型 （即不包括 boolean 和 char）
     */
    public boolean isPrimitiveNumber() {
        return isInt() || isLong() || isFloat() || isDouble() || isByte() || isShort();
    }

    /**
     * 是否原型类型或者原型类型包装类
     * @return
     */
    public boolean isPrimitiveType() {
        return isPrimitiveNumber() || isBoolean() || isChar();
    }

    /**
     * @return 当前对象是否为数字
     */
    public boolean isNumber() {
        return Number.class.isAssignableFrom(klass)
                || klass.isPrimitive()
                && !is(boolean.class)
                && !is(char.class);
    }

    /**
     * @return 当前对象是否在表示日期或时间
     */
    public boolean isDateTimeLike() {
        return Calendar.class.isAssignableFrom(klass)
                || Date.class.isAssignableFrom(klass)
                || java.sql.Date.class.isAssignableFrom(klass)
                || java.sql.Time.class.isAssignableFrom(klass);
    }

    @Override
    public String toString() {
        return klass.getName();
    }

    static Object[] blankArrayArg(Class<?>[] pts) {
        return (Object[]) Array.newInstance(pts[pts.length - 1].getComponentType(), 0);
    }

    /**
     * 获取一个类的泛型参数数组，如果这个类没有泛型参数，返回 null
     */
    public static Type[] getTypeParams(Class<?> klass) {
        if (klass == null) {
            return new Type[0];
        }
        Type superclass = klass.getGenericSuperclass();

        if (superclass instanceof ParameterizedType) {
            return ((ParameterizedType) superclass).getActualTypeArguments();
        }
        return getTypeParams(klass.getSuperclass());
    }

    /**
     * 获取一个字段的泛型参数数组，如果这个字段没有泛型，返回空数组
     *
     * @param f 字段
     * @return 泛型参数数组
     */
    public static Class<?>[] getGenericTypes(Field f) {
        String gts = f.toGenericString();
        Matcher m = PTN.matcher(gts);
        if (m.find()) {
            String s = m.group(2);
            String[] ss = CustomStringUtils.splitIgnoreBlank(s);
            if (ss.length > 0) {
                return getClass(ss);
            }
        }
        return new Class<?>[0];
    }

    private static Class<?>[] getClass(String[] ss){
        Class<?>[] re = new Class<?>[ss.length];
        try {
            for (int i = 0; i < ss.length; i++) {
                String className = ss[i];
                if (className.startsWith("?")) {
                    re[i] = Object.class;
                } else {
                    int pos = className.indexOf('<');
                    if (pos < 0) {
                        re[i] = Class.forName(className);
                    } else {
                        re[i] = Class.forName(className.substring(0, pos));
                    }
                }
            }
            return re;
        } catch (ClassNotFoundException e) {
            throw new SystemErrorException(e);
        }
    }


    /**
     * @return 获得外覆类
     * @throws RuntimeException 如果当前类型不是原生类型，则抛出
     */
    public Class<?> getPrimitiveWrapClass() {
        if (!klass.isPrimitive()) {
            if (this.isPrimitiveNumber() || this.is(Boolean.class) || this.is(Character.class)) {
                return klass;
            }
            throw Ex.systemError("Class '%s' should be a primitive class", klass.getName());

        }
        if (is(int.class)) {
            return Integer.class;
        }
        if (is(char.class)) {
            return Character.class;
        }
        if (is(boolean.class)) {
            return Boolean.class;
        }
        if (is(long.class)) {
            return Long.class;
        }
        if (is(float.class)) {
            return Float.class;
        }
        if (is(byte.class)) {
            return Byte.class;
        }
        if (is(short.class)) {
            return Short.class;
        }
        if (is(double.class)) {
            return Double.class;
        }

        throw Ex.systemError("Class [%s] has no wrapper class!", klass.getName());
    }

    /**
     * @param parameterTypes 函数的参数类型数组
     * @return 参数的描述符
     */
    public static String getParamDescriptor(Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> pt : parameterTypes) {
            sb.append(getTypeDescriptor(pt));
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * @param method 方法
     * @return 这个方法的描述符
     */
    public static String getMethodDescriptor(Method method) {
        return getParamDescriptor(method.getParameterTypes())
                + getTypeDescriptor(method.getReturnType());
    }

    /**
     * @param c 构造函数
     * @return 构造函数的描述符
     */
    public static String getConstructorDescriptor(Constructor<?> c) {
        return getParamDescriptor(c.getParameterTypes()) + "V";
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T>[] getTypes(T... objs) {
        List<Class<T>> clsList = new ArrayList<Class<T>>();
        for (T o : objs) {
            if (o != null) {
                clsList.add((Class<T>) o.getClass());
            }
        }

        return clsList.toArray(new Class[0]);
    }

    /**
     * @param klass 类型
     * @return 获得一个类型的描述符
     */
    public static String getTypeDescriptor(Class<?> klass) {
        if (klass.isPrimitive()) {
            if (klass == void.class) {
                return "V";
            } else if (klass == int.class) {
                return "I";
            } else if (klass == long.class) {
                return "J";
            } else if (klass == byte.class) {
                return "B";
            } else if (klass == short.class) {
                return "S";
            } else if (klass == float.class) {
                return "F";
            } else if (klass == double.class) {
                return "D";
            } else if (klass == char.class) {
                return "C";
            } else {
                return "Z";
            }
        }
        StringBuilder sb = new StringBuilder();
        if (klass.isArray()) {
            return sb.append('[').append(getTypeDescriptor(klass.getComponentType())).toString();
        }
        return sb.append('L').append(ClassWrapper.getPath(klass)).append(';').toString();
    }

    /**
     * @param klass 类型
     * @return 一个类型的包路径
     */
    public static String getPath(Class<?> klass) {
        return klass.getName().replace('.', '/');
    }

    /**
     * 判断一个类是否是另一个类的子类，或者孙子类、曾孙类....
     *
     * @param child
     * @param parent
     * @return
     */
    public static boolean isChildOf(Class<?> parent, Class<?> child) {
        Class<?> theClass = child;
        while (null != theClass && theClass != Object.class) {
            if (theClass.isAssignableFrom(parent)) {
                return true;
            }
            theClass = theClass.getSuperclass();
        }
        return false;
    }


    public static ClassLoader getClassLoader() {
        ClassLoader loader = ClassWrapper.class.getClassLoader();
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader;
    }
}
