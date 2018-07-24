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

import org.jdom.Element;

import org.jmin.ioc.BeanElement;
import org.jmin.ioc.BeanElementException;
import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.BeanParameterException;
import org.jmin.ioc.element.AutowiredType;
import org.jmin.ioc.element.InjectionField;
import org.jmin.ioc.impl.config.xml.BeanXMLNodeUtil;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLFactory;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLTags;
import org.jmin.ioc.impl.util.StringUtil;

/**
 * Bean Element Finder
 *
 * @author Chris Liao
 * @version 1.0
 */

public class InjectionFieldFinder implements BeanElementXMLFinder{
	/**
	 * 查找注入属性
	 */
	public BeanElement find(Element beanNode,String beanid,String spaceName,String file,BeanParameterXMLFactory beanParameterXMLFactory,BeanEelementXMLTags xmlTags,BeanXMLNodeUtil xmlUtil) throws BeanElementException{
		BeanParameterXMLTags paramTags = beanParameterXMLFactory.getBeanParameterXMLTags();
		String fieldName = xmlUtil.getValueByName(beanNode,paramTags.Constants_FIELD_NAME);
		try {
			if (!StringUtil.isNull(fieldName)){
				String autowiredType = xmlUtil.getValueByName(beanNode,paramTags.TAG_ID_Autowired_Type);
				 
				if(!StringUtil.isNull(autowiredType)){
				 if(!autowiredType.equalsIgnoreCase(paramTags.Constants_Autowired_By_Name) && !autowiredType.equalsIgnoreCase(paramTags.Constants_Autowired_By_Type))
		  		 throw new BeanElementException("Invalid autowired field["+fieldName+"]of bean:"+beanid +" at file:"+file+",it must be one of[ByName,ByType]");
		
					return new InjectionField(fieldName,AutowiredType.toAutowiredType(autowiredType));
				}else{
					BeanParameter beanParameter = beanParameterXMLFactory.find(spaceName,beanNode,beanParameterXMLFactory,xmlUtil);
					if(beanParameter==null){
						return new InjectionField(fieldName,AutowiredType.BY_NAME);
					}else{
						return new InjectionField(fieldName,beanParameter);
					}
				}
			}else
				throw new BeanElementException("Null field name of bean:"+beanid +" at file:"+file);
		} catch (BeanParameterException e) {
			 throw new BeanElementException("Failed to find parameter for field["+fieldName+"]of bean:"+beanid +" at file:"+file,e);
		}
	}
}