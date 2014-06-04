package com.sammyer.json;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
sammyer
*/

@Retention(RetentionPolicy.RUNTIME)
public @interface JSONProperty {
	/* JSON key to use with this variable (by default uses variable name)*/
	String value() default "";
}
