[回目录页](/README.md)

# Java常用的知识点

## 浅拷贝,深拷贝和Clone

## 概念
* 浅拷贝会创建新的对象，对象中的基本数据类型会拷贝一份数据，但对象中的实例对象属性则只是拷贝其内存地址（对象引用），拷贝后的属性对象跟原来的属性对象其实还是同一个
* 深拷贝不仅仅拷贝基本数据类型，实例对象也会相应拷贝，即实例对象的拷贝是要创建一个新的对象，并复制实例对象的属性到新对象中，拷贝后的属性对象跟原对象不是同一个。
* 所有的 Class 都继承自 Object ，而在 Object 上，存在一个 clone() 方法，它被声明为了 protected ，所以我们可以在其子类中，使用它。而无论是浅拷贝还是深拷贝，都需要实现 clone() 方法，来完成操作。
* 自定义类要实现Cloneable，并且其方法权限设定为public，外部则可直接使用
### 日常使用
* 若是浅拷贝，则直接使用Object.clone()方法即可
* 实现深拷贝则有两种方式，一种是实现Serialization把对象序列化后，再反序列化回来；
* 深拷贝的另外一种是重写clone，待拷贝对象的所有非基本类型对象实例类都实现cloneable接口，且是所有的子属性中的非基本类型实例对象都要实现该接口。
#### 通过clone实现深拷贝的例子
![avatar](/image/deep_clone.jpeg)

## Java transient关键字
1）一旦变量被transient修饰，变量将不再是对象持久化的一部分，该变量内容在序列化后无法获得访问。

2）transient关键字只能修饰变量，而不能修饰方法和类。注意，本地变量是不能被transient关键字修饰的。变量如果是用户自定义类变量，则该类需要实现Serializable接口。

3）被transient关键字修饰的变量不再能被序列化，一个静态变量不管是否被transient修饰，均不能被序列化。

第三点可能有些人很迷惑，因为发现在User类中的username字段前加上static关键字后，程序运行结果依然不变，即static类型的username也读出来为“Alexia”了，这不与第三点说的矛盾吗？实际上是这样的：第三点确实没错（一个静态变量不管是否被transient修饰，均不能被序列化），反序列化后类中static型变量username的值为当前JVM中对应static变量的值，这个值是JVM中的不是反序列化得出的，不相信？好吧，下面我来证明：

## Java的finally和Return的执行顺序
### 问题1：finally一定会被执行到么？
try...catch...finally块中的finally语句是不是一定会被执行？很多人都说不是，当然他们的回答是正确的，经过我试验，至少有两种情况下finally语句是不会被执行的：

（1）try语句没有被执行到，如在try语句之前就返回了，这样finally语句就不会执行，这也说明了finally语句被执行的必要而非充分条件是：相应的try语句一定被执行到。

（2）在try块中有System.exit(0);这样的语句，System.exit(0);是终止Java虚拟机JVM的，连JVM都停止了，所有都结束了，当然finally语句也不会被执行到。

### 问题2：try语句块中，有return语句,finally语句块会被执行到么，若是执行到其数据是怎样的。
（1）try代码块中有return时，finally语句块会被执行
（2）finally执行在return之后，但在return内容返回前，即要等finally执行完成后，才会把内容结果return. 也就是说， 先执行代码块，在执行内容返回。可以看下面这个典型的例子
```
public class FinallyTest1 {

    public static void main(String[] args) {
        
        System.out.println(test11());
    }
    
    public static String test11() {
        try {
            System.out.println("try block");

           return test12();
      } finally {
           System.out.println("finally block");
       }
  }

  public static String test12() {
       System.out.println("return statement");

       return "after return";
   }
    
}
```
上述代码执行结果为：
```
try block
return statement
finally block
after return
```
先执行try的代码块，再执行return中的代码块，再执行finally，最后执行内容返回

### 问题3：其他情况
* try代码块中有return, finally中也有Return代码块，finally块中的return语句会覆盖try块中的return返回
* 为在return之前发生了异常，所以try中的return不会被执行到，而是接着执行捕获异常的catch 语句和最终的finally语句
* 如果finally语句中没有return语句覆盖返回值，那么原来的返回值可能因为finally里的修改而改变也可能不变。要看修改的内容属性是基本数据类型还是实例对象，也就是看传值还是传引用
* 说明了发生异常后，catch中的return语句先执行，确定了返回值后再去执行finally块，执行完了catch再返回，finally里对b的改变对返回值无影响，原因同前面一样，也就是说情况与try中的return语句执行完全一样。执行顺序与问题2一致

## Java 8的新特性
***待排时间研究***