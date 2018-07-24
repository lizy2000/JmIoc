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
package org.jmin.test.ioc.invocation;

/**
 * A bean class for IOC test.
 *
 * @author Chris Liao
 */
public class Man implements java.io.Serializable{

  /**
   * name
   */
  private String name;

  /**
   * age
   */
  private int age;

  public Man(){}

  /**
   * Default constructor
   */
  public Man(String name,int age){
    this.name=name;
    this.age =age;
  }

  /**
   * Return man age
   */
  public int getAge() {
    return age;
  }

  /**
   * set Age
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * return name
   */
  public String getName() {
    return name;
  }

  /**
   * Set name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Print hello
   * @param target
   */
  public void sayHello(String target){
    this.name = target;
  }

  /**
   * destroy method
   */
  public void destroy(){
    System.out.println("destroy");
  }

  /**
   * Static factory method to create a man
   */
  public static Man create(String name,int age){
    return new Man(name,age);
  }
}