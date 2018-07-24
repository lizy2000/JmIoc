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
package org.jmin.test.ioc.autowired;

import org.jmin.ioc.BeanContext;
import org.jmin.ioc.impl.config.BeanContextImpl;
import org.jmin.test.ioc.BeanTestCase;

/**
 * A IOC Autowired example
 *
 * @author Chris
 */
public class AutowiredXmlCase extends BeanTestCase{
	
	/**
	 * Test method
	 */
	public static void test()throws Throwable{
		BeanContext context = new BeanContextImpl("org/jmin/test/ioc/autowired/pojo.xml");
		Boy boy = (Boy)context.getBean("Boy");
		if(boy!=null){
			if("Chris".equals(boy.getName())){
				System.out.println("[XML].........Autowired(byName) Success.........");
			}else{
				throw new Error("[XML]...........Autowired(byName) Failed............");
			}
		}
		
		Girl girl = (Girl)context.getBean("Girl");
		if(girl!=null){
			if((28== girl.getAge())){
				System.out.println("[XML]...........Autowired(byType) Success.............");
			}else{
				throw new Error("[XML]...........Autowired(byType) Failed...............");
			}
		}
	}
	
	//方法
	public static void main(String[] args)throws Throwable{
		test();
	}
}