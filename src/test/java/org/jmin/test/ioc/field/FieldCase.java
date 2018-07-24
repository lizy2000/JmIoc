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
package org.jmin.test.ioc.field;

import org.jmin.ioc.BeanContainer;
import org.jmin.ioc.BeanElement;
import org.jmin.ioc.BeanElementFactory;
import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.BeanParameterFactory;
import org.jmin.ioc.element.InjectionField;
import org.jmin.ioc.impl.MinBeanContainer;
import org.jmin.test.ioc.BeanTestCase;

/**
 * A IOC field example
 *
 * @author Chris
 */
public class FieldCase extends BeanTestCase{
	
	/**
	 * 运行测试代码
	 */
	public static void test()throws Throwable{
		BeanContainer container = new MinBeanContainer();
		BeanElementFactory beanElementFactory = container.getBeanElementFactory();
		BeanParameterFactory beanParameterFactory = container.getBeanParameterFactory();
		
    BeanParameter nameParmeter = beanParameterFactory.createStringParameter("Chris");
    InjectionField nameField = beanElementFactory.createInjectionField("name",nameParmeter);
		container.registerClass("Bean1", Boy.class,new BeanElement[]{nameField});
  
    BeanParameter ageParmeter = beanParameterFactory.createIntegerParameter(28);
    InjectionField ageField = beanElementFactory.createInjectionField("age",ageParmeter);
    container.registerClass("Bean2", Boy.class,new BeanElement[]{ageField});
 
 
		Boy boy = (Boy)container.getBean("Bean1");
		if(boy!=null){
			if("Chris".equals(boy.getName())){
				System.out.println("[Container].........字段参数注射测试成功..........");
			}else{
				throw new Error("[Container]............字段参数注射测试测试失败............");
			}
		}
		
		Boy boy2 = (Boy)container.getBean("Bean2");
		if(boy!=null){
			if((28== boy2.getAge())){
				System.out.println("[Container]...........字段参数注射测试成功.............");
			}else{
				throw new Error("[Container]............字段参数注射测试测试失败...............");
			}
		}
	}
 
	//方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}
