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
package org.jmin.ioc.impl.exception;

import org.jmin.ioc.BeanException;

/**
 * A exception for bean create
 *
 * @author Chris Liao
 * @version 1.0
 */

public class BeanInvocationInjectException extends BeanException {

	/**
	 * 构造函数
	 */
	public BeanInvocationInjectException(String message) {
		super(message);
	}
	
	/**
	 * 构造函数
	 */
	public BeanInvocationInjectException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 构造函数
	 */
	public BeanInvocationInjectException(String message,Throwable cause) {
		super(message,cause);
	}
}