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
