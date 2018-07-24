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
package org.jmin.ioc.impl.config.xml.element;

import java.util.List;

import org.jdom.Element;
import org.jmin.ioc.BeanElement;
import org.jmin.ioc.BeanElementException;
import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.BeanParameterException;
import org.jmin.ioc.element.InjectionInvocation;
import org.jmin.ioc.impl.config.xml.BeanXMLNodeUtil;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLFactory;
import org.jmin.ioc.impl.util.StringUtil;

/**
 * 注入方法XML查找器
 * 
 * @author Chris
 */
public class InjectionInvocationFinder implements  BeanElementXMLFinder{

	/**
	 * 查找Bean定义元素
	 */
	public BeanElement find(Element beanNode,String beanid,String spaceName,String file,BeanParameterXMLFactory beanParameterXMLFactory,BeanEelementXMLTags xmlTags,BeanXMLNodeUtil xmlUtil) throws BeanElementException{
		String methodName = xmlUtil.getChildext(beanNode,xmlTags.Method_Name);
		
		try {
			if (StringUtil.isNull(methodName))
				throw new BeanElementException("Null method name of bean:"+beanid +" at file:"+file);
			
			Element paramsNode = beanNode.getChild(xmlTags.Method_Param_Values);
			if(paramsNode == null) 
				throw new BeanElementException("Missed <method-param-values>node for method["+methodName+"]of bean:"+beanid +" at file:"+file);
			
			List paramValueList = paramsNode.getChildren(xmlTags.Method_Param_Value);
			BeanParameter[] parameters = new 	BeanParameter[paramValueList.size()];
			for(int i=0,n=parameters.length;i<n; i++) {
				Element argNode = (Element) paramValueList.get(i);
				parameters[i] = beanParameterXMLFactory.find(spaceName,argNode,beanParameterXMLFactory,xmlUtil);
			}
			return new InjectionInvocation(methodName,parameters);
		} catch (BeanParameterException e) {
			throw new BeanElementException("Failed to find parameters for method["+methodName+"]of bean:"+beanid +" at file:"+file,e);
		}
	}
}