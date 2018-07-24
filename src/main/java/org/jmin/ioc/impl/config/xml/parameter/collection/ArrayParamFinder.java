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
package org.jmin.ioc.impl.config.xml.parameter.collection;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.BeanParameterException;
import org.jmin.ioc.impl.config.xml.BeanXMLNodeUtil;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLFactory;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLFinder;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLTags;
import org.jmin.ioc.impl.util.ClassUtil;
import org.jmin.ioc.impl.util.StringUtil;
import org.jmin.ioc.parameter.collection.ArrayParameter;

/**
 * 容器类型的Value
 *
 * @author Chris Liao
 * @version 1.0
 */
public class ArrayParamFinder implements BeanParameterXMLFinder{
	
	/**
	 * 解析获得数组参类
	 */
	public BeanParameter find(String spaceName,Element node,BeanParameterXMLFactory beanParameterXMLFactory,
		BeanParameterXMLTags paramXMLTags,BeanXMLNodeUtil xmlUtil)throws BeanParameterException{
		
		String typename =null;
		try {
			Class arrayType = Object.class;
			typename = xmlUtil.getValueByName(node,paramXMLTags.Constants_Array_Type);
			if(!StringUtil.isNull(typename)){
				arrayType = ClassUtil.loadClass(typename);
			}
		
			List childList = node.getChildren(paramXMLTags.Constants_Item);
			int childListSize = childList.size();
			List valueList= new ArrayList(childListSize);
			 for(int i=0;i<childListSize;i++){
				Element item = (Element)childList.get(i);
				valueList.add(beanParameterXMLFactory.find(spaceName,item,beanParameterXMLFactory,xmlUtil));
			}
			
			return new ArrayParameter(arrayType,valueList.toArray(new BeanParameter[valueList.size()]));
		} catch (ClassNotFoundException e) {
		 throw new BeanParameterException("Array parameter type["+typename+"] not found at node<"+node.getName()+">");
		}
	}
}