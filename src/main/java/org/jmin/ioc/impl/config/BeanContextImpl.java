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
package org.jmin.ioc.impl.config;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.jmin.ioc.BeanContainer;
import org.jmin.ioc.BeanContext;
import org.jmin.ioc.BeanException;
import org.jmin.ioc.impl.MinBeanContainer;
import org.jmin.ioc.impl.config.xml.BeanXMLFileLoader;
 
/**
 * IOC Bean context
 *
 * @author Chris Liao
 * @version 1.0
 */

public class BeanContextImpl extends BeanContext{
	
	/**
	 * file
	 */
	private File[] files=null;
	
	/**
	 * file 
	 */
	private URL[] fileURLs=null;
	
	/**
	 * file 
	 */
	private String[] filenames=null;
	
  /**
   * iocContainer
   */
  private BeanContainer iocContainer =new MinBeanContainer();;
  
  /**
   * constructor
   */
  public BeanContextImpl()throws BeanException{
  	new BeanXMLFileLoader().loadDefaultFile(iocContainer);
  }
 
  /**
	 * constructor
	 */
	public BeanContextImpl(File file) throws BeanException {
		this(new File[]{file});
	}

	/**
	 * constructor
	 */
	public BeanContextImpl(File[] files) throws BeanException {
   this.files = files;
   new BeanXMLFileLoader().loadIocFile(iocContainer,files);
	}

	/**
	 * constructor
	 */
	public BeanContextImpl(String filename) throws BeanException {
		this(new String[] {filename});
	}

	/**
	 * constructor
	 */
	public BeanContextImpl(String[] filenames) throws BeanException {
	 this.filenames = filenames;
	 new BeanXMLFileLoader().loadIocFile(iocContainer,this.filenames);
	}

	/**
	 * constructor
	 */
	public BeanContextImpl(URL fileURL) throws BeanException {
		this(new URL[] { fileURL });
	}

	/**
	 * constructor
	 */
	public BeanContextImpl(URL[] fileURLs) throws BeanException {
   this.fileURLs = fileURLs;
   new BeanXMLFileLoader().loadIocFile(iocContainer, fileURLs);
	}
	
	/**
	 * refresh,reload xml files
	 */
	public void refresh() throws BeanException {
		if(this.iocContainer != null)
			iocContainer.destroy();
		
		this.iocContainer = new MinBeanContainer();
		if(files!=null){
			new BeanXMLFileLoader().loadIocFile(this.iocContainer,this.files);
		}else if(fileURLs!=null){
			new BeanXMLFileLoader().loadIocFile(this.iocContainer,this.fileURLs);
		}else if(filenames!=null){
			new BeanXMLFileLoader().loadIocFile(this.iocContainer,this.filenames);
		}	
	}
	
	/**
	 * containsID
	 */
	public boolean containsId(Object key) throws BeanException {
		return iocContainer.containsId(key);
	}

	/**
	 * return bean
	 */
	public Object getBean(Object key) throws BeanException {
		return iocContainer.getBean(key);
	}

	/**
	 * return bean
	 */
	public Object getBeanOfType(Class key) throws BeanException {
		return iocContainer.getBeanOfType(key);
	}

	/**
	 * return bean
	 */
	public Map getBeansOfType(Class key) throws BeanException {
		return iocContainer.getBeansOfType(key);
	}
	
	/**
	 * containsID
	 */
	public void destroyContainer() throws BeanException {
		iocContainer.destroy();
	}
}