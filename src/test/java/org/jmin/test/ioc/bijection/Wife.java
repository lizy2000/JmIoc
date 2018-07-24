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
package org.jmin.test.ioc.bijection;

import org.jmin.ioc.element.annotation.Bean;
import org.jmin.ioc.element.annotation.Parameter;

/**
 * 双向注入类
 * 
 * @author Chris liao
 */
@Bean
public class Wife {
	
	/**
	 *名字
	 */
	@Parameter("val:Summer")
	private String name;
	
	/**
	 * 丈夫
	 */
	@Parameter("ref:Husband")
	private Husband husband;

	/**
	 *名字
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 丈夫
	 */
	public Husband getHusband() {
		return husband;
	}
	
	/**
	 * 丈夫
	 */
	public void setHusband(Husband husband) {
		this.husband = husband;
	}
}
