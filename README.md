[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Introduction
---
a lightweight Ioc container implementation. 


Features
---
1: Very lightweight,container jar is about 170kb<br/>
2: Bean metadata definition  <br/>
3: interface implementation  <br/>
4: good code structure       <br/>
5: support XML configuration and annotations <br/>
6: support AOP  <br/>
7: dependency packet is less (javaassit, log4j, JDOM) <br/>

Source demo 
---

```java
public class Man {
  private String name;
  private int age;
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
     this.age = age;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
    this.name = name;
  }
}
```

```xml
<?xml version="1.0"?>
<beans>
 <bean id ="Bean1" class="Man">
  <property name="name" value="Chris"/>
  <property name="age"  value="28"/>
 </bean>
</beans>
```

```java
public class PropertyXMLCase{
 public static void test()throws Throwable{
	BeanContext context=new BeanContextImpl("org/jmin/test/ioc/property/pojo.xml");
	Man man = (Man)context.getBean("Bean1");
	if(man!=null){
	   if("Chris".equals(man.getName()) && (28== man.getAge())){
		System.out.println("[XML].........success ..........");
	    }else{
		throw new Error("[XML]...........failed............");
	    }
	}
    }
	
    public static void main(String[] args)throws Throwable{
      test();
    }
}
```
