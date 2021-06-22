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
   迭代器模式提供一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部的表示。把游走的任务放在迭代器上，而不是聚合上。这样简化了聚合的接口和实现，也让责任各得其所。
   
   常见的java数据结构， 例如HashMap，ArrayList等。
   
   客户端类持有了迭代器的引用。
   
### 实例   

```
public class Client {

    public void operation(){
        Object[] objArray = {"One","Two","Three","Four","Five","Six"};
        //创建聚合对象
        Aggregate agg = new ConcreteAggregate(objArray);
        //循环输出聚合对象中的值
        Iterator it = agg.createIterator();
        while(!it.isDone()){
            System.out.println(it.currentItem());
            it.next();
        }
    }
    public static void main(String[] args) {

        Client client = new Client();
        client.operation();
    }

}
```  
   
# 观察者模式

### 简介
   
    观察者模式是对象行为模式，又叫发布-订阅模式。它定义一种一对多的依赖关系，让多个观察者同时监听某一个主题对象。
    
    这个主题对象在发生变化时会通知，会通知所有的观察者，让他们更新自己的数据。
    
    一个软件系统常常要求在某一个对象的状态发生变化的时候，某些其他的对象做出相应的改变。做到这一点的设计方案有很多，但是为了使系统能够易于复用，应该选择低耦合度的设计方案。减少对象之间的耦合有利于系统的复用，但是同时设计师需要使这些低耦合度的对象之间能够维持行动的协调一致，保证高度的协作。观察者模式是满足这一要求的各种设计方案中最重要的一种。
    
    被观察者会持有所有观察者的引用，在被观察者本身数据发生改变是通过发生消息的形式通知被观察者更新自己的数据。
    
### 观察者模式所设计的角色

　　**抽象主题(Subject)角色：**
    
     抽象主题角色把所有对观察者对象的引用保存在一个聚集（比如ArrayList对象）里，每个主题都可以有任何数量的观察者。抽象主题提供一个接口，可以增加和删除观察者对象，抽象主题角色又叫做抽象被观察者(**Observable**)角色。

　　**具体主题(ConcreteSubject)角色：**
    
     将有关状态存入具体观察者对象；在具体主题的内部状态改变时，给所有登记过的观察者发出通知。具体主题角色又叫做具体被观察者(Concrete Observable)角色。

　　**抽象观察者(Observer)角色：**

     为所有的具体观察者定义一个接口，在得到主题的通知时更新自己，这个接口叫做更新接口。

　  **具体观察者(ConcreteObserver)角色：**

     存储与主题的状态自恰的状态。具体观察者角色实现抽象观察者角色所要求的更新接口，以便使本身的状态与主题的状态相协调。如果需要，具体观察者角色可以保持一个指向具体主题对象的引用。
    
### 具体实例

**抽象主题角色类(此处是抽象类，改成接口将会提高复用)**

```
public abstract class Subject {
    /**
     * 用来保存注册的观察者对象
     */
    private    List<Observer> list = new ArrayList<Observer>();

    /**
     * 注册观察者对象
     * @param observer    观察者对象
     */
    public void attach(Observer observer){

        list.add(observer);
        System.out.println("Attached an observer");
    }

    /**
     * 删除观察者对象
     * @param observer    观察者对象
     */
    public void detach(Observer observer){

        list.remove(observer);
    }

    /**
     * 通知所有注册的观察者对象
     */
    public void nodifyObservers(String newState){

        for(Observer observer : list){
            observer.update(newState);
        }
    }
}
```


**具体主题角色类:**

```
public class ConcreteSubject extends Subject{

    private String state;

    public String getState() {
        return state;
    }

    public void change(String newState){
        state = newState;
        System.out.println("主题状态为：" + state);
        //状态发生改变，通知各个观察者
        this.nodifyObservers(state);
    }
}
```

**抽象观察者角色类**

```
public interface Observer {

    /**
     * 更新接口
     * @param state    更新的状态
     */
    public void update(String state);
}
```

**具体观察者角色类**

```
public class ConcreteObserver implements Observer {
    //观察者的状态
    private String observerState;

    @Override
    public void update(String state) {
        /**
         * 更新观察者的状态，使其与目标的状态保持一致
         */
        observerState = state;
        System.out.println("状态为："+observerState);
    }
}
```

**具体使用：**

```
public class Client {

    public static void main(String[] args) {
        //创建主题对象
        ConcreteSubject subject = new ConcreteSubject();
        //创建观察者对象
        Observer observer = new ConcreteObserver();
        //将观察者对象登记到主题对象上
        subject.attach(observer);
        //改变主题对象的状态
        subject.change("new state");
    }
}
```

当主题对象的状态改变时，将通知所有观察者，观察者接收到主题对象的通知后，将可以进行其他操作，进行响应。

### 推模型和拉模型
    　
  在观察者模式中，又分为推模型和拉模型两种方式。
    
  * 推模型
    
     主题对象向观察者推送主题的详细信息，不管观察者是否需要，推送的信息通常是主题对象的全部或部分数据。
    
  * 拉模型
    
     主题对象在通知观察者的时候，只传递少量信息。如果观察者需要更具体的信息，由观察者主动到主题对象中获取，相当于是观察者从主题对象中拉数据。一般这种模型的实现中，会把主题对象自身通过update()方法传递给观察者，这样在观察者需要获取数据的时候，就可以通过这个引用来获取了。
    

# 策略模式

### 简介
   
    策略模式属于对象的行为模式。其用意是针对一组算法，将每一个算法封装到具有共同接口的独立的类中，从而使得它们可以相互替换。策略模式使得算法可以在不影响到客户端的情况下发生变化。
   
    针对一个对象，其行为有些是固定的不变的，有些是容易变化的，针对不同情况有不同的表现形式。那么对于这些容易变化的行为，我们不希望将其实现绑定在对象中，而是希望以动态的形式，针对不同情况产生不同的应对策略。那么这个时候就要用到策略模式了。简言之，策略模式就是为了应对对象中复杂多变的行为而产生的       

　　这个模式涉及到三个角色：

　　●　　环境(Context)角色：持有一个Strategy的引用，即具有复杂多变行为的对象。

　　●　　抽象策略(Strategy)角色：这是一个抽象角色，通常由一个接口或抽象类实现。此角色给出所有的具体策略类所需的接口。

　　●　　具体策略(ConcreteStrategy)角色：包装了相关的算法或行为。

# 模板方法

### 简介
 
  模板方法模式是类的行为模式。
  
  准备一个抽象类，将部分逻辑以具体方法以及具体构造函数的形式实现，然后声明一些抽象方法来迫使子类实现剩余的逻辑。
  
  不同的子类可以以不同的方式实现这些抽象方法，从而对剩余的逻辑有不同的实现。这就是模板方法模式的用意
  
  模板方法模式就是常用的Base类。
        