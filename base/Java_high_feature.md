# 泛型

## 简介

 *  Java 泛型就是一种语法糖，通过泛型使得在编译阶段完成一些类型转换的工作，避免在运行时强制类型转换而出现ClassCastException，即类型转换异常。

 *  泛型是在Java 1.5之后才出现

 * 类型安全。类型错误现在在编译期间就被捕获到了，而不是在运行时当作java.lang.ClassCastException展示出来，将类型检查从运行时挪到编译时有助于开发者更容易找到错误，并提高程序的可靠性。

 * 消除了代码中许多的强制类型转换，增强了代码的可读性。

 *  为较大的优化带来了可能

## 注意
方法声明中定义的形参只能在该方法里使用，而接口、类声明中定义的类型形参则可以在整个接口、类中使用。
```
class Demo{  
  public <T> T fun(T t){   // 可以接收任意类型的数据  
   return t ;     // 直接把参数返回  
  }  
};  
public class GenericsDemo26{  
  public static void main(String args[]){  
    Demo d = new Demo() ; // 实例化Demo对象  
    String str = d.fun("汤姆") ; // 传递字符串  
    int i = d.fun(30) ;  // 传递数字，自动装箱  
    System.out.println(str) ; // 输出内容  
    System.out.println(i) ;  // 输出内容  
  }  
};
```

# 反射
## 概述
Java反射机制是在运行状态中，对于任意一个类，都能够知道这个类中的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制

## 通过反射查看类型信息
### 获取类信息
```
//第一种方式 通过Class类的静态方法——forName()来实现
class1 = Class.forName("com.lvr.reflection.Person");
//第二种方式 通过类的class属性
class1 = Person.class;
//第三种方式 通过对象getClass方法
Person person = new Person();
Class<?> class1 = person.getClass();
```
### 获取成员变量
```
Field[] allFields = class1.getDeclaredFields();//获取class对象的所有属性
Field[] publicFields = class1.getFields();//获取class对象的public属性
Field ageField = class1.getDeclaredField("age");//获取class指定属性
Field desField = class1.getField("des");//获取class指定的public属性
```
### 获取成员方法
```
Method[] methods = class1.getDeclaredMethods();//获取class对象的所有声明方法
Method[] allMethods = class1.getMethods();//获取class对象的所有public方法 包括父类的方法
Method method = class1.getMethod("info", String.class);//返回次Class对象对应类的、带指定形参列表的public方法
Method declaredMethod = class1.getDeclaredMethod("info", String.class);//返回次Class对象对应类的、带指定形参列表的方法
```

### 获取构造函数
```
Constructor<?>[] allConstructors = class1.getDeclaredConstructors();//获取class对象的所有声明构造函数
Constructor<?>[] publicConstructors = class1.getConstructors();//获取class对象public构造函数
Constructor<?> constructor = class1.getDeclaredConstructor(String.class);//获取指定声明构造函数
Constructor publicConstructor = class1.getConstructor(String.class);//获取指定声明的public构造函数
```
### 其他方法
```
Annotation[] annotations = (Annotation[]) class1.getAnnotations();//获取class对象的所有注解
Annotation annotation = (Annotation) class1.getAnnotation(Deprecated.class);//获取class对象指定注解
Type genericSuperclass = class1.getGenericSuperclass();//获取class对象的直接超类的 Type
Type[] interfaceTypes = class1.getGenericInterfaces();//获取class对象的所有接口的type集合
```

```
boolean isPrimitive = class1.isPrimitive();//判断是否是基础类型
boolean isArray = class1.isArray();//判断是否是集合类
boolean isAnnotation = class1.isAnnotation();//判断是否是注解类
boolean isInterface = class1.isInterface();//判断是否是接口类
boolean isEnum = class1.isEnum();//判断是否是枚举类
boolean isAnonymousClass = class1.isAnonymousClass();//判断是否是匿名内部类
boolean isAnnotationPresent = class1.isAnnotationPresent(Deprecated.class);//判断是否被某个注解类修饰
String className = class1.getName();//获取class名字 包含包名路径
Package aPackage = class1.getPackage();//获取class的包信息
String simpleName = class1.getSimpleName();//获取class类名
int modifiers = class1.getModifiers();//获取class访问权限
Class<?>[] declaredClasses = class1.getDeclaredClasses();//内部类
Class<?> declaringClass = class1.getDeclaringClass();//外部类
```

## 实际使用
### 通过反射创建类的实例
```
//第一种方式 Class对象调用newInstance()方法生成
Object obj = class1.newInstance();
//第二种方式 对象获得对应的Constructor对象，再通过该Constructor对象的newInstance()方法生成
Constructor<?> constructor = class1.getDeclaredConstructor(String.class);//获取指定声明构造函数
obj = constructor.newInstance("hello");
```
### 通过反射调用实例方法
```
 // 生成新的对象：用newInstance()方法
 Object obj = class1.newInstance();
//首先需要获得与该方法对应的Method对象
Method method = class1.getDeclaredMethod("setAge", int.class);
//调用指定的函数并传递参数
method.invoke(obj, 28);
```
如果程序确实需要调用某个对象的private方法，则可以先调用Method对象的如下方法。
setAccessible(boolean flag)：将Method对象的acessible设置为指定的布尔值。值为true，指示该Method在使用时应该取消Java语言的访问权限检查；值为false，则知识该Method在使用时要实施Java语言的访问权限检查。
### 访问成员变量

1.通过Class对象的getFields()方法或者getField()方法获得指定方法，返回Field数组或对象。

2.Field提供了两组方法来读取或设置成员变量的值：
getXXX(Object obj):获取obj对象的该成员变量的值。此处的XXX对应8种基本类型。如果该成员变量的类型是引用类型，则取消get后面的XXX。
setXXX(Object obj,XXX val)：将obj对象的该成员变量设置成val值。
```
//生成新的对象：用newInstance()方法 
Object obj = class1.newInstance();
//获取age成员变量
Field field = class1.getField("age");
//将obj对象的age的值设置为10
field.setInt(obj, 10);
//获取obj对象的age的值
field.getInt(obj);
//如果不是基本类型则直接用get,返回一个object对象
field.get(obj) 
```

## 代理模式，基于反射的动态代理

### 代理模式的理解
给定一个对象，但不直接控制或者引用该对象，而是通过另外一个代理对象来间接持有该对象。即客户不直接持有对象，而是通过代理对象来持有原对象。

### 代理模式分类
* 静态代理：代理类是在编译时就实现好的。也就是说 Java 编译完成后代理类是一个实际的 class 文件。
* 动态代理：代理类是在运行时生成的。也就是说 Java 编译完之后并没有实际的 class 文件，而是在运行时动态生成的类字节码，并加载到JVM中。

###  代理模式的好处：
假如有这样的需求，要在某些模块方法调用前后加上一些统一的前后处理操作，比如在添加购物车、修改订单等操作前后统一加上登陆验证与日志记录处理，该怎样实现？首先想到最简单的就是直接修改源码，在对应模块的对应方法前后添加操作。如果模块很多，你会发现，修改源码不仅非常麻烦、难以维护，而且会使代码显得十分臃肿。

## Java反射机制和动态代理



# 注解