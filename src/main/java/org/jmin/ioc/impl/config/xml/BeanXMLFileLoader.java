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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.jmin.ioc.BeanContainer;
import org.jmin.ioc.BeanElement;
import org.jmin.ioc.BeanException;
import org.jmin.ioc.impl.config.xml.element.BeanEelementXMLTags;
import org.jmin.ioc.impl.config.xml.element.BeanElementXMLFactory;
import org.jmin.ioc.impl.config.xml.parameter.BeanParameterXMLTags;
import org.jmin.ioc.impl.exception.BeanClassNotFoundException;
import org.jmin.ioc.impl.exception.BeanConfiguerationFileException;
import org.jmin.ioc.impl.exception.BeanDefinitionException;
import org.jmin.ioc.impl.exception.BeanIdDuplicateRegisterException;
import org.jmin.ioc.impl.util.ClassUtil;
import org.jmin.ioc.impl.util.StringUtil;

/**
 * 部署文件Loader
 * 
 * @author chris
 */

public class BeanXMLFileLoader {
	
	/**
	 * 默认装载的XML配置文件
	 */
	private final String IOC_FILE_NAME = "ioc";

	/**
	 * 默认装载的XML配置文件
	 */
	private final String DEFAULT_IOC_FILE_NAME = "/ioc.xml";

	
	/**
	 * 类的扫描
	 */
	private BeanClassScanner beanClassScanner = new BeanClassScanner();
	
	/**
	 * XML查找器
	 */
	private BeanXMLFileFinder beanXMLFileFinder = new BeanXMLFileFinder();
	
	/**
	 * bean xml node util
	 */
	private BeanXMLNodeUtil elementXMLUtil = new BeanXMLNodeUtil();
	

	/**
	 * xml标记
	 */
	private BeanXMLConstants beanXMLTags = new BeanXMLConstants();
	
	/**
	 * Eelement XML Tags
	 */
	private BeanEelementXMLTags elementXMLTags = new BeanEelementXMLTags();
	
	/**
	 * parameter XML Tags
	 */
	private BeanParameterXMLTags parameterXMLTags = new BeanParameterXMLTags();
	
  /**
   * Bean XML工厂类
   */
	private BeanElementXMLFactory beanElementXMLFactory = new BeanElementXMLFactory(elementXMLUtil,elementXMLTags,parameterXMLTags);
	
	/**
	 * xml解析
	 */
	private SAXBuilder saxBuilder = new SAXBuilder();
	

	/**
	 * 装载默认文件
	 */
	public void loadDefaultFile(BeanContainer container) throws BeanException {
	  loadIocFile(container, new URL[]{getDefaultIocFile()});
	}

	/**
	 * 装载默认文件
	 */
	public void loadIocFile(BeanContainer container, URL fileURL)throws BeanException {
		if(fileURL == null)
			throw new BeanConfiguerationFileException("File url can't be null");
		loadIocFile(container,new URL[]{fileURL});
	}

	/**
	 * 装载默认文件
	 */
	public void loadIocFile(BeanContainer container, File file)throws BeanException {
		if(file == null)
			throw new BeanConfiguerationFileException("File can't be null");
		loadIocFile(container,new File[]{file});
	}
	
	/**
	 * 装载默认文件
	 */
	public void loadIocFile(BeanContainer container, String filename)throws BeanException {
		if(StringUtil.isNull(filename))
			throw new BeanConfiguerationFileException("File name can't be null");
		loadIocFile(container,new String[]{filename});
	}

	/**
	 * 装载默认文件
	 */
	public void loadIocFile(BeanContainer container, File[] files)throws BeanException {
		if(files ==null)
		 throw new BeanConfiguerationFileException("File array can't be null");
		for(int i=0,n=files.length;i<n;i++){
			if(files[i]!=null)
				try {
					load(container, new URL(files[i].getAbsolutePath()));
				} catch (MalformedURLException e) {
					throw new BeanConfiguerationFileException("File exception at file:"+files[i].getName(),e);
				} 
		}
	}

	/**
	 * 装载默认文件
	 */
	public void loadIocFile(BeanContainer container, URL[] fileURLs)throws BeanException {
		if(fileURLs ==null)
			 throw new BeanConfiguerationFileException("File url array can't be null");
		for(int i=0,n=fileURLs.length;i<n;i++){
			if(fileURLs[i]!=null)
			load(container,fileURLs[i]);
		}
	}
	
	/**
	 * 装载默认文件
	 */
	public void loadIocFile(BeanContainer container, String[] filenames)throws BeanException {
		if(filenames ==null)
			 throw new BeanConfiguerationFileException("File name array can't be null");
		
		for(int i=0,n=filenames.length;i<n;i++){
			load(container,beanXMLFileFinder.find(null,filenames[i],beanXMLTags));
		}
	}

	/**
	 * 装载文件
	 */
	public void load(BeanContainer iocContainer,URL url)throws BeanException{
		try {
			beanXMLFileFinder.validateXMLFile(url,beanXMLTags);
			Document document = saxBuilder.build(url);
			Element rootElement = document.getRootElement();
			beanXMLFileFinder.validateXMLRoot(rootElement,beanXMLTags.TAG_BEANS,url.getPath());
			String spacename = rootElement.getAttributeValue(beanXMLTags.ATTR_ID_NAMESPACE);
	
			List annotationList=rootElement.getChildren(beanXMLTags.TAG_ANNOTATION);
			List includeList = rootElement.getChildren(beanXMLTags.TAG_INCLUDE);
			
			String fileFolder = beanXMLFileFinder.getFilePath(url.getPath());
			resolveAnnotation(fileFolder,annotationList,iocContainer);
			resolveIncludeFile(fileFolder,includeList,iocContainer);
			List beanElementList = rootElement.getChildren(beanXMLTags.TAG_BEAN);
			resolveBeanList(url.getFile(),spacename,beanElementList,iocContainer);
		} catch (IOException e) {
			throw new BeanConfiguerationFileException(e);
		} catch (JDOMException e) {
			throw new BeanConfiguerationFileException(e);
		} catch (BeanException e) {
			throw e;
	  }
	}

	/**
	 * 解析annotation
	 */
	private void resolveAnnotation(String parentFolder,List annotationList,BeanContainer iocContainer)throws BeanException{
		for(int i=0,n=annotationList.size();i<n;i++){
			Element element =(Element)annotationList.get(i);
			String basePackage = elementXMLUtil.getValueByName(element,beanXMLTags.ATTR_ID_BASE_PACKAGE);
			String[]folders = StringUtil.split(basePackage,beanXMLTags.Symbols_COMMA);
	    for(int j=0,m=folders.length;j<m;j++){
	    	beanClassScanner.scan(folders[j],iocContainer,beanXMLTags);
	    }
		}
	}
	
	/**
	 * 解析Include文件
	 */
	private void resolveIncludeFile(String parentFolder,List includeList,BeanContainer iocContainer)throws BeanException{
		URL importFileURL = null;
		for(int i=0,n=includeList.size();i<n;i++){
			Element element =(Element)includeList.get(i);
			String includeFile = elementXMLUtil.getValueByName(element,beanXMLTags.ATTR_ID_INCLUDE_FILE);
		  if(includeFile.toLowerCase().startsWith(beanXMLTags.FILE_CP)){
			 includeFile = includeFile.substring(beanXMLTags.FILE_CP.length());
			 importFileURL = beanXMLFileFinder.getClass().getClassLoader().getResource(includeFile);
		  }else if(includeFile.toLowerCase().startsWith(beanXMLTags.FILE_CLASSPATH)){
			 includeFile = includeFile.substring(beanXMLTags.FILE_CLASSPATH.length());
			 importFileURL = beanXMLFileFinder.getClass().getClassLoader().getResource(includeFile);
			}else {
				importFileURL = beanXMLFileFinder.find(parentFolder,includeFile,beanXMLTags);
			}
			
			if(importFileURL ==null)
				throw new BeanConfiguerationFileException("Not found include file:" + includeFile);
			
			 load(iocContainer,importFileURL);
		}
	}
	
	/**
	 * 解析Bean列表
	 */
	private void resolveBeanList(String filename,String spacename,List elementList,BeanContainer iocContainer)throws BeanException{
		for(int i=0,n=elementList.size();i<n;i++){
			Element element =(Element)elementList.get(i);
			String beanid = elementXMLUtil.getValueByName(element,elementXMLTags.Id);
			String className=elementXMLUtil.getValueByName(element,elementXMLTags.Class);
			if(StringUtil.isNull(beanid))
				throw new BeanDefinitionException("Missed bean id at file:"+ filename);
			if(iocContainer.containsId(beanid))
				throw new BeanIdDuplicateRegisterException("Duplicate Registered with bean id:" +beanid +" at file:"+ filename);
			if(StringUtil.isNull(className))
				throw new BeanDefinitionException("Missed bean class with bean id:"+beanid+" at file:"+ filename);
			if(!StringUtil.isNull(spacename))
				beanid = spacename.trim()+ beanXMLTags.Symbols_DOT +beanid;
		
			try {
				BeanElement[] beanElements = beanElementXMLFactory.find(element,beanid,spacename,filename);
				iocContainer.registerClass(beanid,ClassUtil.loadClass(className),beanElements);
			} catch (ClassNotFoundException e) {
				throw new BeanClassNotFoundException("Not found bean class:"+ className +" with id: "+beanid+" at file:"+filename);
			}
		}
	}

	/**
	 * 获得默认配置文件的URL
	 */
	private URL getDefaultIocFile()throws BeanException{
		URL url = BeanXMLFileLoader.class.getResource(DEFAULT_IOC_FILE_NAME);
		if(url == null){
			String filename = System.getProperty(IOC_FILE_NAME);
			if(!StringUtil.isNull(filename))
				url = beanXMLFileFinder.find(null,filename,beanXMLTags);
			else
			 throw new BeanConfiguerationFileException("Not found default ioc file:" + DEFAULT_IOC_FILE_NAME);
		}
		
		if(url != null)
			return url;
		else
			throw new BeanConfiguerationFileException("Not found default ioc file:" + DEFAULT_IOC_FILE_NAME);
	}
}
