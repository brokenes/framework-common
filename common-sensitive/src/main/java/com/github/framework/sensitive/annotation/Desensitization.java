package com.github.framework.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 标注在方法上是否启用脱敏
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitization {

}
