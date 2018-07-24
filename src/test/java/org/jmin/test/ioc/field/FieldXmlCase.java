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

import org.jmin.ioc.BeanContext;
import org.jmin.ioc.impl.config.BeanContextImpl;
import org.jmin.test.ioc.BeanTestCase;

/**
 * A IOC field example
 *
 * @author Chris
 */
public class FieldXmlCase extends BeanTestCase{
	
	/**
	 * 运行测试代码
	 */
	public static void test()throws Throwable{
		BeanContext context = new BeanContextImpl("org/jmin/test/ioc/field/pojo.xml");
		Boy boy = (Boy)context.getBean("Bean1");
		if(boy!=null){
			if("Chris".equals(boy.getName())){
				System.out.println("[XML].........字段参数注射测试成功..........");
			}else{
				throw new Error("[XML] ...........字段参数注射测试测试失败............");
			}
		}
		
		Boy boy2 = (Boy)context.getBean("Bean2");
		if(boy!=null){
			if((28== boy2.getAge())){
				System.out.println("[XML]...........字段参数注射测试成功.............");
			}else{
				throw new Error("[XML]............字段参数注射测试测试失败...............");
			}
		}
	}
	
	//方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}