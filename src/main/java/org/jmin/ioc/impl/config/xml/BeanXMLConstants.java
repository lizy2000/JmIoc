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
package org.jmin.ioc.impl.config.xml;

/**
 * Bean XML Tags
 * 
 * @author Chris Liao 
 */

public class BeanXMLConstants {
	
	/**
	 * USERDIR
	 */
	public final String FILE_USERDIR="user.dir";
	
	/**
	 * xml property: classpath
	 */
	public final String FILE_CP= "cp:";
	
	/**
	 * xml property: classpath
	 */
	public final String FILE_CLASSPATH= "classpath:";
	
	/**
	 * XML
	 */
	public final String XML_FILE_EXTEND=".xml";
	
	/**
	 * xml property: UTF-8
	 */
	public final String FILE_UTF8_Encoding= "UTF-8";
	
	/**
	 *FILE Protocol
	 */
	public final String FILE_Protocol= "file";
	
	/**
	 * FILE Protocol
	 */
	public final String FILE_JAR_Protocol= "jar";
	
	/**
	 * .class
	 */
	public final String FILE_CLASS_EXTEND_NAME= ".class";
	
	public final String Symbols_DOT= ".";
	
	public final char Symbols_CHAR_DOT= '.';
	
	public final String Symbols_COMMA= ",";
	
	
	public final char Symbols_CHAR_LEFT_SEP_CHAR='\\';
	
	public final char Symbols_CHAR_RIGHT_SEP_CHAR='/';
	
	
	
	
	
	

	/**
	 * include tag
	 */
	public final String TAG_INCLUDE="include";
	
	/**
	 * xml tag: beans
	 */
	public final String TAG_BEANS = "beans";

	/**
	 * xml tag: bean
	 */
	public final String TAG_BEAN= "bean";
	
	/**
	 * xml tag: annotation
	 */
	public final String TAG_ANNOTATION= "annotation";
	
	
	/**
	 * xml property: file
	 */
	public final String ATTR_ID_INCLUDE_FILE= "file";
	
	/**
	 * xml property: namespace
	 */
	public final String ATTR_ID_NAMESPACE= "namespace";
	
	/**
	 * xml property: base-package
	 */
	public final String ATTR_ID_BASE_PACKAGE= "base-package";
	
	
	
	
	/**
	 *通过名字装配
	 */
	public String Constant_By_Name = "ByName"; 
	
	/**
	 * 通过类型装配
	 */
	public String Constant_By_Type = "ByType"; 
	
	/**
	 *类型
	 */
	public String Constant_Class = "class"; 
	
	/**
	 * 值
	 */
	public String Constant_Val = "val"; 
	
	/**
	 * 值
	 */
	public String Constant_Value = "value"; 
	
	/**
	 * 引用
	 */
	public String Constant_Ref = "ref"; 
	
	/**
	 * 引用
	 */
	public String  Constant_Reference = "reference"; 
	
}
