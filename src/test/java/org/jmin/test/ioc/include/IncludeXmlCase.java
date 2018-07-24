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
package org.jmin.test.ioc.include;

import org.jmin.ioc.BeanContext;
import org.jmin.ioc.impl.config.BeanContextImpl;
import org.jmin.test.ioc.BeanTestCase;
import org.jmin.test.ioc.include.object.Computer;

/**
 * XML include测试
 *
 * @author Chris
 */
public class IncludeXmlCase extends BeanTestCase{
	
	/**
	 * 运行测试代码
	 */
	public static void test()throws Throwable{
		BeanContext context=new BeanContextImpl("org/jmin/test/ioc/include/pojo.xml");
		Computer computer = (Computer)context.getBean("Bean1");
		if(computer!=null && computer.getKeyBoard()!=null && computer.getMonitor()!=null){
			System.out.println("[XML].........Include xml file 测试成功..........");
		}else{
			throw new Error("[XML] ...........Include xml file 测试失败............");
	  }
	}
	
	//启动测试方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}