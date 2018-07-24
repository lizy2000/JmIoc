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
package org.jmin.test.ioc.create.object;

/**
 * 创建测试的类
 *
 * @author Chris Liao
 */
public class Toy {

  /**
   * 名称
   */
  private String name;
  
  /**
   * 构造函数
   */
  public Toy(){}
  
  /**
   * 构造函数
   */
  public Toy(String name){
    this.name=name;
  }

  /**
   * 名称
   */
  public String getName() {
    return name;
  }

  /**
   * 名称
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 静态方法创建
   */
  public static Toy create(String name){
    return new Toy(name);
  }
}