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
package org.jmin.ioc.parameter.primitive;

import org.jmin.ioc.BeanParameterException;
import org.jmin.ioc.parameter.PrimitiveParameter;

/**
 * Double primitive Type
 *
 * @author Chris Liao
 * @version 1.0
 */

public final class DoubleParameter extends PrimitiveParameter {

  /**
   * Constructor
   */
  public DoubleParameter(double value)throws BeanParameterException {
    super(Double.valueOf(value));
  }
  
  /**
   * Constructor
   */
  public DoubleParameter(Double value)throws BeanParameterException {
  	super(value);
  }
 
  /**
   * Return class for Primitive
   */
  public Class getTypeClass() {
    return Double.TYPE;
  }
}