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
package org.jmin.test.ioc.create;

import org.jmin.ioc.BeanContainer;
import org.jmin.ioc.BeanElement;
import org.jmin.ioc.BeanElementFactory;
import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.BeanParameterFactory;
import org.jmin.ioc.element.InstanceCreation;
import org.jmin.ioc.impl.MinBeanContainer;
import org.jmin.test.ioc.BeanTestCase;
import org.jmin.test.ioc.create.object.Toy;
import org.jmin.test.ioc.create.object.ToyFactory;

/**
 * A IOC creation example
 *
 * @author Chris
 */
public class CreateCase extends BeanTestCase{
	
	/**
	 * 运行测试代码
	 */
	public static void test()throws Throwable{
		BeanContainer container = new MinBeanContainer();
		BeanElementFactory beanElementFactory =container.getBeanElementFactory();
		BeanParameterFactory beanParameterFactory =container.getBeanParameterFactory();
		
		BeanParameter nameParmeter = beanParameterFactory.createStringParameter("Cat");
		
		InstanceCreation classCreation = beanElementFactory.createInstanceCreation(new BeanParameter[]{nameParmeter});
		container.registerClass("Bean1",Toy.class,new BeanElement[]{classCreation});
		
		InstanceCreation methodCreation = beanElementFactory.createInstanceCreation("create",new BeanParameter[]{nameParmeter});
		container.registerClass("Bean2",Toy.class,new BeanElement[]{methodCreation});
		
		InstanceCreation beanCreation = beanElementFactory.createInstanceCreation("factory","create",new BeanParameter[]{nameParmeter});
		container.registerClass("Bean3",Toy.class,new BeanElement[]{beanCreation});
		container.registerClass("factory",ToyFactory.class);
		
		Toy toy =(Toy)container.getBean("Bean1");
		if(toy!=null){
				System.out.println("[Container].........类反射创建测试成功..........");
		}else{
				throw new Error("[Container]...........类反射创建测试失败............");
		}
	
		Toy toy2 =(Toy)container.getBean("Bean2");
		if(toy2!=null){
			System.out.println("[Container].........工厂方法创建测试成功..........");
		}else{
			throw new Error("[Container]...........工厂方法创建测试失败............");
		}
		
		Toy toy3 =(Toy)container.getBean("Bean3");
		if(toy3!=null){
			System.out.println("[Container].........工厂Bean测试成功..........");
		}else{
			throw new Error("[Container]...........工厂Bean测试失败............");
		}
	}
	
	//启动测试方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}
