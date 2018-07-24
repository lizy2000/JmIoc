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
package org.jmin.ioc.impl.converter.base;

import java.util.Calendar;
import java.util.Date;

import org.jmin.ioc.BeanTypeConvertException;
import org.jmin.ioc.impl.converter.BeanTypeBaseConverter;

/**
 * Short转换
 * 
 * @author chris
 */

public class ShortConverter extends BeanTypeBaseConverter{
	
	/**
	 * 转换为目标类型
	 */
	public Object convert(Object value)throws BeanTypeConvertException{
		if(value ==null){
			return null;
		}else if(value instanceof String){
		 return Short.valueOf((String)value);
		}else if(value instanceof Character){
			return Short.valueOf((short)((Character)value).charValue());
		}else if(value instanceof Number){
			return Short.valueOf(((Number)value).shortValue());
		}else if(value instanceof Date){
			return Short.valueOf(Long.valueOf(((Date)value).getTime()).shortValue());
		}else if(value instanceof Calendar){
			return Short.valueOf(Long.valueOf(((Calendar)value).getTime().getTime()).shortValue());
		}else{
			throw new BeanTypeConvertException("Doesn't support object conversion from type: "+ value.getClass().getName() + " to type: Short.class");
		}
	}
}
