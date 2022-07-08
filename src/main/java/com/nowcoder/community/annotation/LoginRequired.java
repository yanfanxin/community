package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 使用元注解进行描述
// 声明自定义注解可以写在什么位置，作用在哪个类型上
@Target(ElementType.METHOD)   //方法上
// 声明自定义注解的有效时间
@Retention(RetentionPolicy.RUNTIME)  // 运行时有效
public @interface LoginRequired {
}
