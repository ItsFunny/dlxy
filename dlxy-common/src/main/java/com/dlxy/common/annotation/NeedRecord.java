/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:01:25
* 
*/
package com.dlxy.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午8:01:25
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface NeedRecord
{
	String dealWay() default "";
	

}
