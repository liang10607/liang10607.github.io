[回目录页](..)

# 行为型模式简介
   
    行为型模式是在不同对象之间划分清楚责任和算法，不仅关注类和对象的结构，还关注他们之间的相互作用
    
    通过行为型模式，可以更加清晰地划分类和对象的职责，并研究运行时实例对象的相互作用。
    
    **类似于结构型模式， 行为型模式也分为类行为型模式和对象行为型模式：**
 
1. 类行为型模式，主要通过继承方式在几个类之间分配行为，并通过多太来分配父类和子类的职责

2. 对象行为型模式，使用对象的聚合关联关系分配行为，根据合成复用原则，尽量使用聚合关联关系来取代继承关系。

###  **包含模式**
  
  **职责链模式(Chain of Responsibility)**
  
  **命令模式(Command)**
  
  解释器模式(Interpreter)
  
  **迭代器模式(Iterator)**
  
  中介者模式(Mediator)
  
  备忘录模式(Memento)
  
  **观察者模式(Observer)**
  
  状态模式(State)
  
  **策略模式(Strategy)**
  
  **模板方法模式(Template Method)**
  
  访问者模式(Visitor)
  
# 责任链模式

### 简介
   
    为请求创建接受者对象的链，可避免让请求发送着和接受者偶合起来，让多个对象都有可能接收到请求，将这些对象连成一条链，并且沿着这条链传递请求，直到有对象处理它为止。
    
    职责链上的处理中负责处理请求，客户只需要把请求发到职责链上即可，无需关心请求的处理细节和请求的传递。所以职责链把请求的发送者和处理者解耦了
    
    职责链处理消息时过滤很多道
    
    拦截的类都实现了统一的接口
    
    通常每个接收者都包含对另一个接收者的引用。如果一个对象不能处理该请求，那么它会把相同的请求传给下一个接收者，依此类推。类似于链表的实现
 
     
###  具体实例   
```
public abstract class AbstractLogger {
   public static int INFO = 1;
   public static int DEBUG = 2;
   public static int ERROR = 3;
 
   protected int level;
 
   //责任链中的下一个元素
   protected AbstractLogger nextLogger;
 
   public void setNextLogger(AbstractLogger nextLogger){
      this.nextLogger = nextLogger;
   }
 
   public void logMessage(int level, String message){
      if(this.level <= level){
         write(message);
      }
      if(nextLogger !=null){
         nextLogger.logMessage(level, message);
      }
   }
 
   abstract protected void write(String message);
   
}
```    

# 命令模式

### 简介

  命令模式属于对象的行为模式。命令模式又称为行动(Action)模式或交易(Transaction)模式。

  命令模式把一个请求或者操作封装到一个对象中。命令模式允许系统使用不同的请求把客户端参数化，具有请求排队或者记录请求日志，提供命令的撤销和恢复的功能。

  命令模式可以将请求发送者和接收者完全解耦，发送者与接收者之间没有直接引用关系，发送请求的对象只需要知道如何发送请求，而不必知道如何完成请求。
  
  请求者到接受者的请求，通过一个命令抽象类和命令实现类进行请求执行的中转。请求者持有命令抽象类的引用，命令实现类则持有接受者的引用
  
### 简单示例

#### **接收者角色类**
 
```  
  public class Receiver {
      /**
       * 真正执行命令相应的操作
       */
      public void action(){
          System.out.println("执行操作");
      }
  }
```

#### **抽象命令角色类**
  
```  
  public interface Command {
      /**
       * 执行方法
       */
      void execute();
  }
```

#### **具体命令角色类**
 
```  
  public class ConcreteCommand implements Command {
      //持有相应的接收者对象
      private Receiver receiver = null;
      /**
       * 构造方法
       */
      public ConcreteCommand(Receiver receiver){
          this.receiver = receiver;
      }
      @Override
      public void execute() {
          //通常会转调接收者对象的相应方法，让接收者来真正执行功能
          receiver.action();
      }
  
  }
```

#### **请求者角色类**
  
```  
  public class Invoker {
      /**
       * 持有命令对象
       */
      private Command command = null;
      /**
       * 构造方法
       */
      public Invoker(Command command){
          this.command = command;
      }
      /**
       * 行动方法
       */
      public void action(){
  
          command.execute();
      }
  }
```

#### **客户端角色类**
  
```  
  public class Client {
  
      public static void main(String[] args) {
          //创建接收者
          Receiver receiver = new Receiver();
          //创建命令对象，设定它的接收者
          Command command = new ConcreteCommand(receiver);
          //创建请求者，把命令对象设置进去
          Invoker invoker = new Invoker(command);
          //执行方法
          invoker.action();
      }
  
  }
```

# 迭代器模式

### 简介

  
    