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
package org.jmin.ioc;

import java.util.Map;

/**
 * Bean Context
 *
 * @author Chris Liao
 * @version 1.0
 */

public abstract class BeanContext {
	
	//context file map key 
	public final static String BEAN_CONTEXT_FILE = "bean.context.file";
	
	//context factory  map key 
	public final static String BEAN_CONTEXT_FACTORY = "bean.context.factory";

	//context init method,
	public static BeanContext initContext(Map initMap)throws BeanException{
		try {
			if(initMap==null)
				throw new BeanException("bean init context map can't be null");
			String factoryName = (String)initMap.get(BEAN_CONTEXT_FACTORY);
			if(factoryName==null)
				throw new BeanException("Not found context factoy["+BEAN_CONTEXT_FACTORY+"]in map");
			
			Class factoryClass = Class.forName(factoryName);
			BeanContextFactory factory = (BeanContextFactory)factoryClass.newInstance();
			return factory.createContext(initMap);
		} catch (BeanException e) {
     throw e;
		} catch (Exception e) {
			throw new BeanException(e);
		}
	}
	
	/**
	 * refresh,reload xml files
	 */
	public abstract void refresh() throws BeanException;
	
  /**
   * contains beanID
   */
  public abstract boolean containsId(Object id)throws BeanException;

  /**
   * Find a bean instance from container by a id.
   * If not found, then return null.
   */
  public abstract Object getBean(Object id)throws BeanException;

  /**
   * Find a bean instance with a class. If not found, then return null.
   */
  public abstract Object getBeanOfType(Class cls)throws BeanException;

  /**
   * Find all bean instance map
   */
  public abstract Map getBeansOfType(Class cls)throws BeanException;

}
