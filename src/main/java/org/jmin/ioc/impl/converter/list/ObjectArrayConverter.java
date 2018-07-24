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
package org.jmin.ioc.impl.converter.list;

import java.util.ArrayList;
import java.util.Collection;

import org.jmin.ioc.BeanTypeConvertException;

/**
 * Object[] 转换器
 * 
 * @author Liao
 */
public class ObjectArrayConverter extends AbstractConverter{
	
	/**
	* 将对象转换为目标类型，如将结果转换为当前类型
	*/
	public Object convert(Object value)throws BeanTypeConvertException{
		if(value==null){
			return null;
	  }else if(value instanceof Object[]){
			return value;
		}else{
			Collection list = new ArrayList();
		  this.convert(list,value);
		  return list.toArray();
		}
	}
}
