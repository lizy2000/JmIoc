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
package org.jmin.test.ioc.interception;

import org.jmin.ioc.BeanContext;
import org.jmin.ioc.impl.config.BeanContextImpl;
import org.jmin.test.ioc.BeanTestCase;
import org.jmin.test.ioc.interception.object.Young;

/**
 *  代理测试
 *
 * @author Chris
 */
public class ProxyXmlCase extends BeanTestCase{
	
	/**
	 * 运行测试代码
	 */
	public static void test()throws Throwable{
		BeanContext context=new BeanContextImpl("org/jmin/test/ioc/interception/pojo.xml");
		Young child = (Young)context.getBean("Bean1");
		child.sayHello("Chris");
		System.out.println("[XML].........Proxy拦截测试成功..........");
	}
	
  //启动测试方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}