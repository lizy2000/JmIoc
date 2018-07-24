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

import java.util.Collection;
import java.util.Map;

import org.jmin.ioc.BeanContext;
import org.jmin.ioc.BeanContextFactory;
import org.jmin.ioc.BeanException;

/**
 * Bean Context Factory implement
 *
 * @author Chris Liao
 * @version 1.0
 */
public class BeanContextFactoryImpl implements BeanContextFactory{
	
	/**
	 * Create Bean context
	 */
	public BeanContext createContext(Map initMap)throws BeanException{
		Object source =initMap.get(BeanContext.BEAN_CONTEXT_FILE);
		if(source!=null){
		  if(source instanceof String){
		  	String file = (String)source;
		  	if(file.trim().length()==0)
		  		throw new BeanException("Context file name can't be null");
		  	else
		  	 return new BeanContextImpl();
		  }else if(source instanceof String[]){
		  	String[]files = (String[])source;
		  	if(files.length==0)
		  		throw new BeanException("Context file array siz must be more than zero");
		  	return new BeanContextImpl(files);
			}else if(source instanceof Collection){
		    Collection collection =(Collection)source;
		  	if(collection.size()==0)
		  		throw new BeanException("Context file list siz must be more than zero");
			 	return new BeanContextImpl((String[])collection.toArray(new String[collection.size()]));
			}else{
				throw new BeanException("Not support context source type:["+source.getClass().getName()+"]");
			}
		}else{
			throw new BeanException("Not found context file["+BeanContext.BEAN_CONTEXT_FILE+"]in map");
		}
	}
}
