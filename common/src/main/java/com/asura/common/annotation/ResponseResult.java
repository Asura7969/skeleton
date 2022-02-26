package com.asura.common.annotation;

import java.lang.annotation.*;

/**
 * @author gongwenzhou
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ResponseResult {
}
