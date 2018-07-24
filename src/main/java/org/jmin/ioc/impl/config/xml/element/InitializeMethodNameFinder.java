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
import org.jmin.ioc.element.InitializeMethodName;
import org.jmin.ioc.impl.config.xml.BeanXMLNodeUtil;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLFactory;
import org.jmin.ioc.impl.util.StringUtil;

/**
 * Bean Element Finder
 *
 * @author Chris Liao
 * @version 1.0
 */

public class InitializeMethodNameFinder implements  BeanElementXMLFinder{
	
	/**
	 * 查找Bean定义元素
	 */
	public BeanElement find(Element beanNode,String beanid,String spaceName,String file,BeanParameterXMLFactory beanParameterXMLFactory,BeanEelementXMLTags xmlTags,BeanXMLNodeUtil xmlUtil) throws BeanElementException{
    String nodeValue = xmlUtil.getValueByName(beanNode,xmlTags.Init_method);
    if(!StringUtil.isNull(nodeValue))
    	return new InitializeMethodName(nodeValue.trim());
    else
    	return null;
	}
}