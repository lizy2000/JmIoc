/*
 * Copyright (C) Chris Liao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmin.ioc.element.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bean annotation
 *
 * @author Chris Liao
 * @version 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented 
@Inherited   
public @interface Bean {
	
	/**
	 * Bean Id
	 */
	public String id()default "";
	
	/**
	 * parent id
	 */
	public String parentId() default "";
	
	/**
	 * singleton
	 */
	public boolean singleton() default true;
	
	/**
	 * init mehtod
	 */
	public String initMethod() default "";
	
	/**
	 * destroy mehtod
	 */
	public String destroyMethod() default "";
}
