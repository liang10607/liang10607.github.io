[回目录页](..)
# 创建型设计模式

# 简介
   创建型模式对类得实例化过程进行了抽象，能够将软件模块中对象得创建和对象得使用进行分离。

   创建型模式隐藏了类得实例创建细节。

   常用得创建型模式包括：

1. 简单工厂模式（Simple Factory）
3. 抽象工厂模式（Abstract Factory）
4. 建造者模式(Builder)
5. 原型模式(Prototype)
6. 单例模式(Singleton)

# 简单工厂模式（Simple Factory）
   在简单工厂模式中，可以根据参数的不同返回**不同类**的实例。  
   简单工厂模式**专门定义一个工厂类**来负责创建其他类的实例，被创建的实例通常都具有共同的父类。

## 使用简单工厂模式得场景
工厂类负责创建的对象比较少：由于创建的对象较少，不会造成工厂方法中的业务逻辑太过复杂。

客户端只知道传入工厂类的参数，对于如何创建对象不关心：客户端既不需要关心创建细节，甚至连类名都不需要记住，只需要知道类型所对应的参数。

##  简单工厂模式在Java中的应用
①JDK类库中广泛使用了简单工厂模式，如工具类java.text.DateFormat，它用于格式化一个本地日期或者时间。
```
public final static DateFormat getDateInstance();
public final static DateFormat getDateInstance(int style);
public final static DateFormat getDateInstance(int style,Locale
locale);
```

②Java加密技术
获取不同加密算法的密钥生成器:
```
KeyGenerator keyGen=KeyGenerator.getInstance("DESede");
```
创建密码器:
```
Cipher cp=Cipher.getInstance("DESede");

```

# 工厂方法模式（Factory Method）
在工厂方法模式中，工厂父类负责定义创建产品对象的公共接口，而工厂子类则负责生成具体的产品对象，**这样做的目的是将产品类的实例化操作延迟到工厂子类中完成**，即通过**工厂子类**来确定究竟应该实例化哪一个具体产品类。

要创建不同得类对象实例，要通过不同得工厂子类来创建。
```
static void Main(string[] args)
{
    //先给我来个灯泡
    ICreator creator = new BulbCreator();
    ILight light = creator.CreateLight();
    light.TurnOn();
    light.TurnOff();

    //再来个灯管看看
    creator = new TubeCreator();
    light = creator.CreateLight();
    light.TurnOn();
    light.TurnOff();

}
```

## 使用工厂方法模式得场景
①一个类不知道它所需要的对象的类：在工厂方法模式中，客户端不需要知道具体产品类的类名，只需要知道所对应的工厂即可，具体的产品对象由具体工厂类创建；客户端需要知道创建具体产品的工厂类。

②一个类通过其子类来指定创建哪个对象：在工厂方法模式中，对于抽象工厂类只需要提供一个创建产品的接口，而由其子类来确定具体要创建的对象，利用面向对象的多态性和里氏代换原则，在程序运行时，子类对象将覆盖父类对象，从而使得系统更容易扩展。

③将创建对象的任务委托给多个工厂子类中的某一个，客户端在使用时可以无须关心是哪一个工厂子类创建产品子类，需要时再动态指定，可将具体工厂类的类名存储在配置文件或数据库中。

## 工厂方法模式在Java中得实际应用
```
Connection conn=DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DB;user=sa;password=");
Statement statement=conn.createStatement();
ResultSet rs=statement.executeQuery("select * from UserInfo");
```

# 抽象工厂模式
抽象工厂模式(Abstract Factory Pattern)：提供一个创建一系列相关或相互依赖对象的接口，而无须指定它们具体的类。抽象工厂模式又称为Kit模式，属于对象创建型模式。
```
public static void Main(string[] args)
{
    //采购商要一台iPad和一台Tab
    Factory factory = new Factory_Pad();
    Apple apple = factory.createAppleProduct();
    apple.AppleStyle();
    Sumsung sumsung = factory.createSumsungProduct();
    sumsung.BangziStyle();

    //采购商又要一台iPhone和一台Note2
    factory = new Factory_Phone();
    apple = factory.createAppleProduct();
    apple.AppleStyle();
    sumsung = factory.createSumsungProduct();
    sumsung.BangziStyle();
    Console.ReadKey();
}
```
  抽象工厂模式，总结起来就是有一个抽象工厂父类，然后有由具体不同的工厂子类负责真正创建对象，其对应的产品类也是有抽象父类和具体子类，不同的具体工厂子类创建不同的产品子类。
  
# 单例模式
   作为对象的创建模式，单例模式确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例。这个类称为单例类。  
## 懒汉式，Double Check模式

```
public static Singleton getSingleton() {
    if (instance == null) {                         //Single Checked
        synchronized (Singleton.class) {
            if (instance == null) {                 //Double Checked
                instance = new Singleton();
            }
        }
    }
    return instance ;
}
```
这段代码看起来很完美，很可惜，它是有问题。主要在于instance = new Singleton()这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情:
1.给 instance 分配内存

2.调用 Singleton 的构造函数来初始化成员变量

3.将instance对象指向分配的内存空间（执行完这步 instance 就为非 null 了）。

上述步骤中的 2 和3可能会出现指令重排，如果执行顺序是132，然后另外一个线程获得了锁，在2执行前，使用instance的成员变量，则会有问题

我们只需要将 instance 变量声明成 volatile 就可以了。
**也就是说，在 volatile 变量的赋值操作后面会有一个内存屏障（生成的汇编代码上），读操作不会被重排序到内存屏障之前**

## 饿汉式 Static final Filed
因为单例的实例被声明成 static 和 final 变量了，在第一次加载类到内存中时就会初始化，所以创建实例本身是线程安全的。

```
public class Singleton{
    //类加载时就初始化
    private static final Singleton instance = new Singleton();

    private Singleton(){}

    public static Singleton getInstance(){
        return instance;
    }
}
```

## 静态内部类 static nested class
1. 静态单例对象没有作为Singleton的成员变量直接实例化，因此类加载时不会实例化Singleton
2. 第一次调用getInstance()时才再加载内部类SingletonHolder，此时也才进行单例对象的创建，并且类加载过程中，系统会保障线程安全
3. 它也是一种懒汉式的加载方式
```
public class Singleton {  
    private static class SingletonHolder {  
        private static final Singleton INSTANCE = new Singleton();  
    }  
    private Singleton (){}  
    public static final Singleton getInstance() {  
        return SingletonHolder.INSTANCE; 
    }  
}
```

## 枚举单例
   用枚举写单例实在太简单了！这也是它最大的优点。下面这段代码就是声明枚举实例的通常做法。
   即用枚举类来实现单例，可以避免反序列化和反射的攻击
```
public enum EasySingleton{
    INSTANCE;
}
```

# 建造者模式（Builder）
**优点**
   良好的封装性， 使用建造者模式可以使客户端不必知道产品内部组成的细节；
   建造者独立，容易扩展；
在对象创建过程中会使用到系统中的一些其它对象，这些对象在产品对象的创建过程中不易得到。
**缺点**
   会产生多余的Builder对象以及Director对象，消耗内存；
   对象的构建过程暴露。


