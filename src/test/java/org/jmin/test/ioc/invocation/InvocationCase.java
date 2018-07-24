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
package org.jmin.test.ioc.invocation;

import org.jmin.ioc.BeanContainer;
import org.jmin.ioc.BeanElementFactory;
import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.BeanParameterFactory;
import org.jmin.ioc.element.InjectionInvocation;
import org.jmin.ioc.impl.MinBeanContainer;
import org.jmin.test.ioc.BeanTestCase;

/**
 * 调用注入
 * 
 * @author Chris Liao 
 */
public class InvocationCase  extends BeanTestCase{

	/**
	 * 运行测试代码
	 */
	public static void test()throws Throwable{
		BeanContainer container =  new MinBeanContainer();
		BeanElementFactory beanElementFactory =container.getBeanElementFactory();
		BeanParameterFactory beanParameterFactory =container.getBeanParameterFactory();
		
		BeanParameter helloParmeter = beanParameterFactory.createStringParameter("Chris");
		InjectionInvocation invocation = beanElementFactory.createInjectionInvocation("sayHello",new BeanParameter[] {helloParmeter });
		container.registerClass("Bean1", Man.class, new InjectionInvocation[] {invocation });
		
		Man man = (Man)container.getBean("Bean1");
		if(man!=null){
			if("Chris".equals(man.getName())){
				System.out.println("[Container]........方法调用测试成功..........");
			}else{
				throw new Error("[Container]..........方法调用测试失败............");
			}
		}
	}

	//启动测试方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}