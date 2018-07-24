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
package org.jmin.ioc.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;

import org.jmin.ioc.BeanContainer;
import org.jmin.ioc.BeanDefinition;
import org.jmin.ioc.BeanElement;
import org.jmin.ioc.BeanElementFactory;
import org.jmin.ioc.BeanException;
import org.jmin.ioc.BeanParameterFactory;
import org.jmin.ioc.BeanTypeConverter;
import org.jmin.ioc.element.DestroyMethodName;
import org.jmin.ioc.element.InitializeMethodName;
import org.jmin.ioc.element.InjectionField;
import org.jmin.ioc.element.InjectionInvocation;
import org.jmin.ioc.element.InjectionProperty;
import org.jmin.ioc.element.InstanceSingleton;
import org.jmin.ioc.element.InvocationInterception;
import org.jmin.ioc.element.ParentReferenceId;
import org.jmin.ioc.element.ProxyInterfaces;
import org.jmin.ioc.impl.converter.BeanTypeBaseConverter;
import org.jmin.ioc.impl.converter.BeanTypeConvertFactory;
import org.jmin.ioc.impl.converter.base.BoolConverter;
import org.jmin.ioc.impl.converter.base.ByteConverter;
import org.jmin.ioc.impl.converter.base.CharConverter;
import org.jmin.ioc.impl.converter.base.DoubleConverter;
import org.jmin.ioc.impl.converter.base.FloatConverter;
import org.jmin.ioc.impl.converter.base.IntegerConverter;
import org.jmin.ioc.impl.converter.base.LongConverter;
import org.jmin.ioc.impl.converter.base.ShortConverter;
import org.jmin.ioc.impl.converter.base.StringConverter;
import org.jmin.ioc.impl.converter.date.CalendarConverter;
import org.jmin.ioc.impl.converter.date.DateConverter;
import org.jmin.ioc.impl.converter.date.DateTimeConverter;
import org.jmin.ioc.impl.converter.date.DateTimestampConverter;
import org.jmin.ioc.impl.converter.date.UtilDateConverter;
import org.jmin.ioc.impl.converter.list.ArrayListConverter;
import org.jmin.ioc.impl.converter.list.EnumerationConverter;
import org.jmin.ioc.impl.converter.list.HashSetConverter;
import org.jmin.ioc.impl.converter.list.IteratorConverter;
import org.jmin.ioc.impl.converter.list.LinkedListConverter;
import org.jmin.ioc.impl.converter.list.ObjectArrayConverter;
import org.jmin.ioc.impl.converter.list.StackConverter;
import org.jmin.ioc.impl.converter.list.TreeSetConverter;
import org.jmin.ioc.impl.converter.list.VectorConverter;
import org.jmin.ioc.impl.converter.map.HashMapConverter;
import org.jmin.ioc.impl.converter.map.HashtableConverter;
import org.jmin.ioc.impl.converter.map.PropertiesConverter;
import org.jmin.ioc.impl.converter.map.TreeMapConverter;
import org.jmin.ioc.impl.converter.map.WeakHashMapConverter;
import org.jmin.ioc.impl.converter.math.BigDecimalConverter;
import org.jmin.ioc.impl.converter.math.BigIntegerConverter;
import org.jmin.ioc.impl.definition.BaseDefinition;
import org.jmin.ioc.impl.definition.ClassDefinition;
import org.jmin.ioc.impl.definition.InstanceDefinition;
import org.jmin.ioc.impl.exception.BeanClassException;
import org.jmin.ioc.impl.exception.BeanDefinitionNotFoundException;
import org.jmin.ioc.impl.exception.BeanIdDuplicateRegisterException;
import org.jmin.ioc.impl.instance.InstanceContainer;
import org.jmin.ioc.impl.instance.ParamConvertFactory;
import org.jmin.ioc.impl.util.BeanUtil;
import org.jmin.log.LogPrinter;

/**
 * Bean mico-container implement
 *
 * @author Chris Liao
 * @version 1.0
 */

public class MinBeanContainer implements BeanContainer {
	
	/**
	* store ioc wrappers
	*/
	private Map definitionMap;
	
	/**
	* Instance container
	*/
	private InstanceContainer instanceContainer = null;
	
	/**
	* desctroy hook
	*/
	private ContainerDestroyHook instanceDestroyHook = null;
	
  /**
   * element factory
   */
	private BeanElementFactory beanElementFactory =null;
	
	/**
   * parameter factory
   */
	private BeanParameterFactory beanParameterFactory =null;

	/**
   * type conversion factory
   */
	private BeanTypeConvertFactory beanTypeConvertFactory =null;
	
	/**
	 * Parameter Object converter
	 */
	private ParamConvertFactory paramConvertFactory=null;
	
	/**
	* logger
	*/
	private LogPrinter log = LogPrinter.getLogPrinter(MinBeanContainer.class);
	
	/**
	 * contaienr creation
	 */
	public MinBeanContainer() {
		this.definitionMap = new HashMap();
		this.instanceContainer = new InstanceContainer(this);
		this.beanElementFactory = new BeanElementFactory();
		this.beanParameterFactory = new BeanParameterFactory();
		this.paramConvertFactory=new ParamConvertFactory();
		this.beanTypeConvertFactory = new BeanTypeConvertFactory();
		
		this.registerDefaultConverter();
		this.instanceDestroyHook = new ContainerDestroyHook(this);
		Runtime.getRuntime().addShutdownHook(this.instanceDestroyHook);
	}

	/**
	 * contains beanID
	 */
	public boolean containsId(Object id) {
		return definitionMap.containsKey(id);
	}

	/**
	* Returns a instance for bean by ID
	*/
	public Object getBean(Object id)throws BeanException {
		return this.instanceContainer.getBean(id,null);
	}
	
	/**
	* Returns a instance for bean by class
	*/
	public Object getBeanOfType(Class beanClass)throws BeanException {
		return this.instanceContainer.getBeanByClass(beanClass,null);
	}
	
	/**
	* Find all bean instance map
	*/
	public Map getBeansOfType(Class beanClass)throws BeanException{
		return this.instanceContainer.getBeansByClass(beanClass,null);
	}
	
	/**
	* register a object instance
	*/
	public void registerInstance(Object instance)throws BeanException {
	  registerInstance(instance,instance);
	}
	
	/**
	* Register a object instance with a ID
	*/
	public void registerInstance(Object id, Object instance)throws BeanException {
	 this.checkIdDuplicateRegister(id);
	 this.definitionMap.put(id,new InstanceDefinition(id,instance));
	 log.info("Registered a bean instance:"+instance +" with id:" + id);
	}
	
	/**
	* Register a classes
	*/
	public void registerClass(Class beanClass) throws BeanException{
	 registerClass(beanClass,beanClass,null);
	}

	/**
	* Register a classes
	*/
	public void registerClass(Class beanClass, BeanElement[] beanElements)throws BeanException {
	 registerClass(beanClass, beanClass, beanElements);
	}
	
	/**
	* Register a classes
	*/
	public void registerClass(Object id, Class beanClass)throws BeanException {
	 registerClass(id, beanClass,null);
	}
	
	/**
	 * Register a classes
	 */
	public void registerClass(Object id,Class beanClass,BeanElement[] beanElements)throws BeanException {
		this.checkIdDuplicateRegister(id);
		this.checkBeanClass(id,beanClass);
		this.definitionMap.put(id, new ClassDefinition(id, beanClass, beanElements));
	}
	
	/**
	* deregister a bean definition
	*/
	public void deregister(Object id)throws BeanException {
	  definitionMap.remove(id);
	}

	/**
	* destroy all bean in container and clear all bean definition
	*/
	public void destroy() {
		this.destroy2();
		Runtime.getRuntime().removeShutdownHook(this.instanceDestroyHook);
	}

	/**
	* destroy all bean in container and clear all bean definition
	*/
	private void destroy2() {
		BaseDefinition[] definition = getAllDefinition();
		for(int i=0,n=definition.length;i<n;i++){
			try {
				Object instance = definition[i].getSingletonInstance();
				Method destroyMethod =definition[i].getDestroyMethod();
				if(instance!=null && destroyMethod!=null)
				 BeanUtil.invokeMethod(instance,destroyMethod,new Object[0]);
				instance = null;
			} catch (Exception e) {
				log.info("Bean("+ definition[i].getBeanID()+ ") * Fail to destroy instance",e);
			}
		}
		definitionMap.clear();
	}
	
  /**
   * Element factory
   */
  public BeanElementFactory getBeanElementFactory()throws BeanException{
  	return this.beanElementFactory;
  }
  
  /**
   * Parameter factory
   */
  public BeanParameterFactory getBeanParameterFactory()throws BeanException{
  	return this.beanParameterFactory;
  }
  
  /**
   * parameter Object converter
   */
  public ParamConvertFactory getParamConvertFactory()throws BeanException{
  	return this.paramConvertFactory;
  }
  
  /**
   * type conversion factory
   */
  public BeanTypeConvertFactory getBeanTypeConvertFactory()throws BeanException{
  	return this.beanTypeConvertFactory;
  }
	
  
  /**
   * add a field
   */
  public void addInjectionField(Object id,InjectionField field)throws BeanException{
   this.getBeanDefinition(id).addInjectionField(field);
  }

  /**
   * remove a field
   */
  public void removeInjectionField(Object id,InjectionField field)throws BeanException{
  	this.getBeanDefinition(id).removeInjectionField(field);
  }
 
	/**
	* Register properties
	*/
	public void addInjectionProperty(Object id, InjectionProperty property)throws BeanException {
	 this.getBeanDefinition(id).addInjectionProperty(property);
	}
	
	/**
	* Register properties
	*/
	public void removeInjectionProperty(Object id, InjectionProperty property)throws BeanException {
	 this.getBeanDefinition(id).removeInjectionProperty(property);
	}
	
	/**
	* Register a method invocation
	*/
	public void addInjectionInvocation(Object id, InjectionInvocation invocation)throws BeanException {
	 this.getBeanDefinition(id).addInjectionInvocation(invocation);
	}
	
	/**
	* Register a method invocation
	*/
	public void removeInjectionInvocation(Object id, InjectionInvocation invocation)throws BeanException {
	 this.getBeanDefinition(id).removeInjectionInvocation(invocation);
	}
	
	/**
	* Register interception
	*/
	public void addInvocationInterception(Object id, InvocationInterception interception)throws BeanException {
	 this.getBeanDefinition(id).addInvocationInterception(interception);
	}
	
	/**
	* Register interception
	*/
	public void removeInvocationInterception(Object id, InvocationInterception interception)throws BeanException {
	 this.getBeanDefinition(id).removeInvocationInterception(interception);
	}
	
	/**
	* Set a proxy interface on a object for
	*/
	public void setProxyInterfaces(Object id,ProxyInterfaces proxyInterfaces)throws BeanException{
	 this.setProxyInterfaces(id,proxyInterfaces.getInterfaces());
	}
	
	/**
	* Set a proxy interface on a object for
	*/
	public void setProxyInterfaces(Object id,Class[] proxyInterfaces)throws BeanException{
	 this.getBeanDefinition(id).setProxyInterfaces(proxyInterfaces);
	}
	
	/**
	* set Parent
	*/
	public void setParentReferenceId(Object id, ParentReferenceId parent)throws BeanException{
		this.getBeanDefinition(id).setParentReferenceId(parent.getReferenceId());
	}
	
	/**
	* set Parent
	*/
	public void setParentReferenceId(Object id,Object referenceId)throws BeanException{
		this.getBeanDefinition(id).setParentReferenceId(referenceId);
	}
	
	/**
	* Register singleton for bean
	*/
	public void setInstanceSingleton(Object id, boolean singleton)throws BeanException {
	 this.getBeanDefinition(id).setInstanceSingleton(singleton);
	}
	
	/**
	* Register singleton for bean
	*/
	public void setInstanceSingleton(Object id, InstanceSingleton singleton)throws BeanException {
	 this.setInstanceSingleton(id,singleton.isSingleton());
	}

	/**
	* Register init method
	*/
	public void setInitMethodName(Object id, String initMethod)throws BeanException {
	 this.getBeanDefinition(id).setInitMethodName(initMethod);
	}
	
	/**
	* Register init method
	*/
	public void setInitMethodName(Object id, InitializeMethodName initMethod)throws BeanException {
	 this.setInitMethodName(id,initMethod.getMethodName());
	}
	
	/**
	* Register destroy method
	*/
	public void setDestroyMethodName(Object id, String destroyMethod)throws BeanException {
	 BeanDefinition wrapper = this.getBeanDefinition(id);
	 wrapper.setDestroyMethodName(destroyMethod);
	}
	
	/**
	* Register init method
	*/
	public void setDestroyMethodName(Object id, DestroyMethodName destroyMethod)throws BeanException {
	 this.setDestroyMethodName(id,destroyMethod.getMethodName());
	}
	
	/**
	 * check contains converter by type
	 */
	public boolean containsTypeConverter(Class type)throws BeanException{
		return this.getBeanTypeConvertFactory().containsTypeConverter(type);
	}
  
  /**
   * remove type converter
   */
  public void removeTypeConverter(Class type)throws BeanException{
  	this.getBeanTypeConvertFactory().removeTypeConverter(type);
  }
  
  /**
   * get type converter
   */
  public BeanTypeConverter getTypeConverter(Class type)throws BeanException{
  	return (BeanTypeConverter)this.getBeanTypeConvertFactory().getTypeConverter(type);
  }
  
  /**
   * add type converter
   */
  public void addTypeConverter(Class type,BeanTypeConverter converter)throws BeanException{
  	this.getBeanTypeConvertFactory().addTypeConverter(type,converter);
  }
 
  /**
   * convert object
   */
  public Object convertObject(Object obj,Class type)throws BeanException{
  	return this.getBeanTypeConvertFactory().convert(obj,type);
  }
	
	

	
	/********************************以下方法非接口的实现,只供内部使用*******************************/

	/**
	* 获得所有Bean的定义信息
	*/
	public BaseDefinition[] getAllDefinition(){
	 return (BaseDefinition[])definitionMap.values().toArray(new BaseDefinition[definitionMap.size()]);
	}
	
	/**
	* 查找Bean定义
	*/
	public BaseDefinition getBeanDefinition(Object id)throws BeanException {
	 if(definitionMap.containsKey(id))
		 return (BaseDefinition)definitionMap.get(id);
	 else
		 throw new BeanDefinitionNotFoundException("Not found bean definition with id:" + id);
	}

	/**
	* 检查注册见键是否已存在
	*/
	private void checkIdDuplicateRegister(Object id)throws BeanException {
		if(this.definitionMap.containsKey(id))
			throw new BeanIdDuplicateRegisterException("Duplicate register bean definition with id:" + id);
	}
	
	/**
	* 检查注册见键是否已存在
	*/
	private void checkBeanClass(Object id,Class beanClass)throws BeanException {
		if(beanClass == null)
			throw new BeanClassException("Found null registered bean class with id:" + id);
	}
	
	/**
	 * 初始化默认转换器
	 */
	private void registerDefaultConverter(){
		  try {
				BeanTypeConverter converter = new BoolConverter();
				this.addTypeConverter(Boolean.class,converter);
				this.addTypeConverter(boolean.class,converter);

				converter = new ByteConverter();
				this.addTypeConverter(Byte.class,converter);
				this.addTypeConverter(byte.class,converter);
 
				converter = new ShortConverter();
				this.addTypeConverter(Short.class,converter);
				this.addTypeConverter(short.class,converter);
				
				converter = new IntegerConverter();
				this.addTypeConverter(Integer.class,converter);
				this.addTypeConverter(int.class,converter);
				
				converter = new LongConverter();
				this.addTypeConverter(Long.class,converter);
				this.addTypeConverter(long.class,converter);

				converter = new FloatConverter();
				this.addTypeConverter(Float.class,converter);
				this.addTypeConverter(float.class,converter);

				converter = new DoubleConverter();
				this.addTypeConverter(Double.class,converter);
				this.addTypeConverter(double.class,converter);
 
				converter = new CharConverter();
				this.addTypeConverter(char.class,converter);
				this.addTypeConverter(Character.class,converter);
				
				this.addTypeConverter(Object.class,new BeanTypeBaseConverter());
				this.addTypeConverter(BigInteger.class,new BigIntegerConverter());
				this.addTypeConverter(BigDecimal.class,new BigDecimalConverter());
				this.addTypeConverter(String.class,new StringConverter());

				this.addTypeConverter(Date.class,new DateConverter());
				this.addTypeConverter(Time.class,new DateTimeConverter());
				this.addTypeConverter(Timestamp.class,new DateTimestampConverter());

				this.addTypeConverter(Calendar.class,new CalendarConverter());
				this.addTypeConverter(java.util.Date.class,new UtilDateConverter());
				
		
				//放入List类型的转换器
				this.addTypeConverter(Stack.class,new StackConverter());
				this.addTypeConverter(Vector.class,new VectorConverter());
				this.addTypeConverter(LinkedList.class,new LinkedListConverter());
				
				converter = new ArrayListConverter();
				this.addTypeConverter(ArrayList.class,converter);
				this.addTypeConverter(AbstractList.class,converter);
				this.addTypeConverter(List.class,converter);
				this.addTypeConverter(Collection.class,converter);
				//放入List类型的转换器
				
				//放入Set类型的转换器
				this.addTypeConverter(HashSet.class,new HashSetConverter());
				converter = new TreeSetConverter();
				this.addTypeConverter(TreeSet.class,converter);
				this.addTypeConverter(AbstractSet.class,converter);
				this.addTypeConverter(Set.class,converter);
				//放入Set类型的转换器
				
				this.addTypeConverter(Iterator.class,new IteratorConverter());
				this.addTypeConverter(Enumeration.class,new EnumerationConverter());
				this.addTypeConverter(Object[].class,new ObjectArrayConverter());
				
				//放入Map类型的转换器
				this.addTypeConverter(Hashtable.class,new HashtableConverter());
				this.addTypeConverter(Properties.class,new PropertiesConverter());
				this.addTypeConverter(WeakHashMap.class,new WeakHashMapConverter());
				converter = new TreeMapConverter();
				this.addTypeConverter(TreeMap.class,converter);
				this.addTypeConverter(SortedMap.class,converter);
				
				converter = new HashMapConverter();
				this.addTypeConverter(HashMap.class,converter);
				this.addTypeConverter(Map.class,converter);
				//放入Map类型的转换器				
			}catch (Exception e) {
				log.info(e);
			}
	}
	
	
	private class ContainerDestroyHook extends Thread{

	  /**
	   * conatain a ioc container
	   */
	  private MinBeanContainer container;

	  /**
	   * constructor
	   */
	  public ContainerDestroyHook(MinBeanContainer container){
	    this.container = container;
	  }

	  /**
	   * main method of hook thread
	   */
	  public void run(){
	    this.container.destroy2();
	  }
 }
}

	