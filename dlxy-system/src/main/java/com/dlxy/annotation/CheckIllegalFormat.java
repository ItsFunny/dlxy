/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月22日 下午12:58:48
* 
*/
package com.dlxy.annotation;

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
* @date 创建时间：2018年7月22日 下午12:58:48
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CheckIllegalFormat
{

}
