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
package org.jmin.ioc.impl.instance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmin.ioc.BeanException;
import org.jmin.ioc.BeanInterceptor;
import org.jmin.ioc.BeanParameter;
import org.jmin.ioc.element.InvocationInterception;
import org.jmin.ioc.impl.MinBeanContainer;
import org.jmin.ioc.impl.converter.BeanTypeConvertFactory;
import org.jmin.ioc.impl.definition.BaseDefinition;
import org.jmin.ioc.impl.definition.ClassDefinition;
import org.jmin.ioc.impl.definition.InstanceDefinition;
import org.jmin.ioc.impl.exception.BeanClassException;
import org.jmin.ioc.impl.exception.BeanMatchedSubClassNotFoundException;
import org.jmin.ioc.impl.exception.BeanMultiMatchedSubClassFoundException;
import org.jmin.ioc.impl.intercept.InterceptionHandler;
import org.jmin.ioc.impl.intercept.InterceptorChain;
import org.jmin.ioc.impl.util.BeanUtil;
import org.jmin.ioc.impl.util.ClassUtil;
import org.jmin.log.LogPrinter;

/**
 * Bean实例查找工厂
 * 
 * @author Chris liao
 */

public class InstanceContainer {
	
	/**
	 * bean container
	 */
	private MinBeanContainer beanContainer;
	
	/**
	 * 实例构造工厂
	 */
  private InstanceCreateFactory instanceCreateFactory = new InstanceCreateFactory();
  
	/**
	 * 实例注入工厂
	 */
  private InstanceInjectFactory instanceInjectFactory = new InstanceInjectFactory();
 
	/**
	 * 日记
	 */
	private LogPrinter logger =LogPrinter.getLogPrinter(InstanceContainer.class);

	/**
	 * 构造函数
	 */
	public InstanceContainer(MinBeanContainer beanContainer){
		this.beanContainer = beanContainer;
	}
	
	/**
	 * 获得Bean Instance， 单例模式且初始
	 */
	public Object getBean(Object beanId,Map bijectionMap)throws BeanException{
		logger.debug(beanId,"Begin to lookup a instance");
		BaseDefinition definition = beanContainer.getBeanDefinition(beanId);
		
		if(bijectionMap!= null && bijectionMap.containsKey(beanId)){
			return bijectionMap.get(beanId);
		}else{
			if(ClassUtil.isAbstractClass(definition.getBeanClass())){//抽象类则查找其子类实现
				return getBeanByClass(definition.getBeanClass(),bijectionMap);
			}else{
				Object instance = null;
				if(definition.isInstaceCreated() && definition.isInstanceSingleton()){//完成过定义初始化，且单例模式
					instance =this.getSingletonInstance(definition);
					logger.debug(beanId,"Found a singleton instance:"+ instance);
					return instance;
				}else{//需要构造一个新的实例，并完成属性,方法注入
					if(definition.isInstanceSingleton()){//单例
						synchronized(definition){//单例模式,同步琐定对象，对于同一个definition，一次只允许一个Thread访问
							if(definition.isInstaceCreated()){//进入同步块，检查对象是否已经被其他线程创建好
								instance =this.getSingletonInstance(definition);
							}else{
								instance =this.createBeanInstance(beanId,definition,bijectionMap);
							}
						}//单例模式,同步结束
						
					}else{//多例模式
						instance = this.createBeanInstance(beanId,definition,bijectionMap);
					}
					return instance;
				}
			}
	  }
	}
	
	/**
	 * 获得单例实例
	 */
	private Object getSingletonInstance(BaseDefinition definition)throws BeanException{
		Object instance = null;
		if(definition instanceof InstanceDefinition){
			InstanceDefinition instanceDefinition =(InstanceDefinition)definition;
			if(instanceDefinition.getBeanProxyInstance()!=null){
				instance = instanceDefinition.getBeanProxyInstance();
			}else{
				instance= instanceDefinition.getSingletonInstance();
			}
		}else{
			ClassDefinition classDefinition =(ClassDefinition)definition;
			instance= classDefinition.getSingletonInstance();
		}
		
		return instance;
	}
	
	/**
	 * 创建Bean的实例
	 */
	private Object createBeanInstance(Object beanId,BaseDefinition definition,Map bijectionMap)throws BeanException{
		InterceptorChain interceptorChain=null;
		InterceptionHandler interceptionHandler =null;
		Class[] proxyInterfaces = definition.getProxyInterfaces();

		Object instance = instanceCreateFactory.createInstance(this,definition,bijectionMap);
		if(!definition.getInvocationInterceptionList().isEmpty()){//获取拦截链
			interceptorChain = getInterceptorChain(this,definition,bijectionMap);
		}
		
		if(interceptorChain!=null && proxyInterfaces!=null && proxyInterfaces.length >0){
			interceptionHandler = new InterceptionHandler(instance);
			instance = Proxy.newProxyInstance(this.getClass().getClassLoader(),proxyInterfaces,interceptionHandler);
			
			if(definition instanceof InstanceDefinition){
				((InstanceDefinition)definition).setProxyInstance(instance);
			}
		}
		
		if(bijectionMap == null)
			bijectionMap = new HashMap();
		bijectionMap.put(beanId,instance);
	
		instanceInjectFactory.injectInstance(instance,definition,this,bijectionMap);

		/***************************开始将对象实例与拦截器关联起来*********************/
		if (interceptorChain != null) {
			if (interceptionHandler != null) {
				interceptionHandler.setInterceptorChain(interceptorChain);
			} else {
				this.injectInterceptChain(instance, interceptorChain);
			}
		}
		/***************************开始将对象实例与拦截器关联起来*********************/
		
		if(definition instanceof ClassDefinition && definition.isInstanceSingleton()){
			ClassDefinition classDefinition =(ClassDefinition)definition;
			classDefinition.setSingletonInstance(instance);
		}
		
		if(!definition.isInstaceCreated())
			definition.setInstaceCreated(true);
		
		logger.debug(beanId,"Created a instance:"+instance);
		return instance;
	}
	
	/**
	* 依据类去查找实例,如果可以匹配到到实例子，将抛出异常
	*/
	public Object getBeanByClass(Class beanClass,Map tempMap)throws BeanException {
		checkBeanClass(beanClass);
		List targetBeanIdList = new ArrayList();
		beanClass = ClassUtil.getWrapperClass(beanClass);

		BaseDefinition[] definition = beanContainer.getAllDefinition();
		if(!ClassUtil.isAbstractClass(beanClass)){//非抽象类，应该先找类相等
			for(int i=0,n=definition.length;i<n;i++){
				if(!ClassUtil.isAbstractClass(definition[i].getBeanClass()) && beanClass.equals(definition[i].getBeanClass())){
					targetBeanIdList.add(definition[i].getBeanID());
				}
			}
		}	
	  
		if(targetBeanIdList.isEmpty()){//没有找到相等类，就找子类对象
			for(int i=0,n=definition.length;i<n;i++){
				if(!ClassUtil.isAbstractClass(definition[i].getBeanClass()) && beanClass.isAssignableFrom(definition[i].getBeanClass())){
					targetBeanIdList.add(definition[i].getBeanID());
				}
			}
		}
		
		if(targetBeanIdList.size() >1){
			throw new BeanMultiMatchedSubClassFoundException("Multi-creatable sub classes were found based on type:" + beanClass);
		}else if(targetBeanIdList.size()==1){
			return getBean(targetBeanIdList.get(0),tempMap);
		}else{
			throw new BeanMatchedSubClassNotFoundException("Not found a matched creatable sub class based on type:" + beanClass);
		}
	}

	/**
	* 依据类去查找实例
	*/
	public Map getBeansByClass(Class beanClass,Map tempMap)throws BeanException{
		checkBeanClass(beanClass);
		List targetBeanIdList = new ArrayList();
		beanClass = ClassUtil.getWrapperClass(beanClass);
		BaseDefinition[] definition = beanContainer.getAllDefinition();
		for(int i=0,n=definition.length;i<n;i++){
			if(!ClassUtil.isAbstractClass(definition[i].getBeanClass()) && beanClass.isAssignableFrom(definition[i].getBeanClass())){
				targetBeanIdList.add(definition[i].getBeanID());
			}
		}
		
		Map resultMap = new HashMap(targetBeanIdList.size());
	  for(int i=0,n=targetBeanIdList.size();i<n;i++){
			Object beanId = targetBeanIdList.get(i);
			resultMap.put(beanId,getBean(beanId,tempMap));
		}
		return resultMap;
	}
	
 
	/**
	 * 获取拦截链
	 */
	private InterceptorChain getInterceptorChain(InstanceContainer container, BaseDefinition definition,Map tempMap)throws BeanException{
		if(definition.getInterceptorChain()!=null){
			return definition.getInterceptorChain();
		}else{
			InterceptorChain interceptorChain = null;
			List interceptionList = definition.getInvocationInterceptionList();
			if(!interceptionList.isEmpty()){
				interceptorChain = new InterceptorChain();
				
				for(int i=0,size=interceptionList.size();i<size;i++){
					InvocationInterception interception = (InvocationInterception)interceptionList.get(i);
					BeanParameter[] info = interception.getInterceptorsInfo();
					Object[] values = container.beanContainer.getParamConvertFactory().convertParameters(container,info,tempMap);
					for(int j=0,n=values.length;j<n;j++){
						interceptorChain.addMethodInterceptor(interception.getMethodName(),interception.getMethodParamTypes(),(BeanInterceptor)values[j]);
					}
				}
				
				definition.setInterceptorChain(interceptorChain);
			 }
			return interceptorChain;
		}
	}
	
	/**
	 * 拦截链注入
	 */
	private void injectInterceptChain(Object instace,InterceptorChain chain)throws BeanException{
		try {
		   Class beanClas = instace.getClass();
			 Method method = beanClas.getDeclaredMethod("setInterceptorChain", new Class[]{org.jmin.ioc.impl.intercept.InterceptorChain.class});
		 	 BeanUtil.invokeMethod(instace,method,new Object[]{chain});
		}catch(InvocationTargetException e){
			throw new BeanException(e.getTargetException());
		} catch (Exception e) {
			throw new BeanException(e);
		}
	}
	
	/**
	* 查找Bean定义
	*/
	public BaseDefinition getBeanDefinition(Object id)throws BeanException {
		return beanContainer.getBeanDefinition(id);
	}
	
	/**
	* 返回转换工厂
	*/
	public BeanTypeConvertFactory getBeanTypeConvertFactory()throws BeanException {
		return beanContainer.getBeanTypeConvertFactory();
	}
	
	/**
	* 返回参数转换工厂
	*/
	public ParamConvertFactory getParamConvertFactory()throws BeanException {
		return beanContainer.getParamConvertFactory();
	}
	
	/**
	* 检查注册见键是否已存在
	*/
	private void checkBeanClass(Class beanClass)throws BeanException {
		if(beanClass == null)
			throw new BeanClassException("Bean class cant't be null");
 }
}