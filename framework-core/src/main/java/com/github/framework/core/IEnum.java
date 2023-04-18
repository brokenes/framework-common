package com.github.framework.core;

/**
 * 枚举类型的值域对象，所有的枚举类型实现该接口；并提供基本的操作
 * @param <T>
 */
public interface IEnum<T> {

    /**
     * 枚举值
     * @return 枚举值 ，可以是任何类型，具体由实现类定义
     */
    T value();

    /**
     * 枚举名称
     * @return String,枚举名称
     */
    String named();

}
