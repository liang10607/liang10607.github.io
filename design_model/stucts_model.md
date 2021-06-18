[回目录页](..)

# 结构型模式简介
   描述如何将类和对象结合起来，形成更大的结构，就像搭积木一样，把简单结构组合形成更大更复杂的结构。
   
   结构型模式，可分为类结构型模式，对象结构型模式：

1. 类结构模式主要关心类的组合，由多个类组合成更大的系统，在类结构模式中只存在继承关系和实现关系

2. 对象结构模式关心类和对象的组合，通关联关系在一个类中定义另外一个类的对象，通过合成复用的原则，通过关联关系来替代继承关系，因此大部分结构型模式都是对象型模式

结构型模式主要包括：
* 适配器模式（Adapter）
* 桥接模式（Bridge）
* 组合模式（Composite）
* 装饰模式(Decorator)
* 外观模式（Facade）
* 享源模式（FlyWeight）
* 代理模式（Proxy）

# 适配器模式(Adapter)

### 简介
  适配器模式把一个类的接口变换成客户端所期待的另一种接口，从而使原本因接口不匹配而无法在一起工作的两个类能够在一起工作。
   
  例如电源插座，从两个口转化为三个口得工具就是适配器。

  适配器模式有类的适配器模式和对象的适配器模式两种形式

### 类的适配器模式

### 对象的适配器模式

   类的适配器模式主要是依靠继承关系来实现，如下述代码中的Adaptee和Target两个类则通过继承关系，实现了两个类一起工作

```
public class Adapter extends Adaptee implements Target {
    /**
     * 由于源类Adaptee没有方法sampleOperation2()
     * 因此适配器补充上这个方法
     */
    @Override
    public void sampleOperation2() {
        //写相关的代码
    }

}
```

### 对象适配器模式

   与类的适配器模式不同的是，对象适配器模式不是使用对象关系连接到Adapter,而是使用委派方式把关联类的对象委派到Adapter中

```
public class Adapter {
    private Adaptee adaptee;  //Adapter类持有关联类的对象，便拥有了关联类对象的功能

    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }
    /**
     * 源类Adaptee有方法sampleOperation1
     * 因此适配器类直接委派即可
     */
    public void sampleOperation1(){
        this.adaptee.sampleOperation1();
    }
    /**
     * 源类Adaptee没有方法sampleOperation2
     * 因此由适配器类需要补充此方法
     */
    public void sampleOperation2(){
        //写相关的代码
    }
}
```

### 类适配器和对象适配器的使用场景

●　　类适配器使用对象继承的方式，是静态的定义方式；而对象适配器使用对象组合的方式，是动态组合的方式。

●　　对于类适配器，由于适配器直接继承了Adaptee，使得适配器不能和Adaptee的子类一起工作，因为继承是静态的关系，当适配器继承了Adaptee后，就不可能再去处理 Adaptee的子类了。

●　　对于对象适配器，一个适配器可以把多种不同的源适配到同一个目标。换言之，同一个适配器可以把源类和它的子类都适配到目标接口。因为对象适配器采用的是对象组合的关系，只要对象类型正确，是不是子类都无所谓。

●　 对于类适配器，适配器可以重定义Adaptee的部分行为，相当于子类覆盖父类的部分实现方法。

●　 对于对象适配器，要重定义Adaptee的行为比较困难，这种情况下，需要定义Adaptee的子类来实现重定义，然后让适配器组合子类。虽然重定义Adaptee的行为比较困难，但是想要增加一些新的行为则方便的很，而且新增加的行为可同时适用于所有的源。

●　　对于类适配器，仅仅引入了一个对象，并不需要额外的引用来间接得到Adaptee。

●　　对于对象适配器，需要额外的引用来间接得到Adaptee。

建议尽量使用对象适配器的实现方式，多用合成/聚合、少用继承。当然，具体问题具体分析，根据需要来选用实现方式，最适合的才是最好的。

### Android中适配器模式的使用

   Android中最常见的适配器模式便是ListView对应的Adapter了

```
public abstract class AbsListView extends AdapterView<ListAdapter>
        implements TextWatcher,
        ViewTreeObserver.OnGlobalLayoutListener, Filter.FilterListener,
        ViewTreeObserver.OnTouchModeChangeListener,
        RemoteViewsAdapter.RemoteAdapterConnectionCallback {

    ListAdapter mAdapter;

    // 关联到Window时调用的函数
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 代码省略
        // 给适配器注册一个观察者。
        if (mAdapter != null&&
        mDataSetObserver == null){
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);

            // Data may have changed while we were detached. Refresh.
            mDataChanged = true;
            mOldItemCount = mItemCount
            // 获取Item的数量,调用的是mAdapter的getCount方法
            mItemCount = mAdapter.getCount();
        }
        mIsAttached = true;
    }

    /**
     * 子类需要覆写layoutChildren()函数来布局child view,也就是Item View
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mInLayout = true;
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i<childCount; i++) {
                getChildAt(i).forceLayout();
            }
            mRecycler.markChildrenDirty();
        }

        if (mFastScroller != null&&
        mItemCount != mOldItemCount){
            mFastScroller.onItemCountChanged(mOldItemCount, mItemCount);
        }
        // 布局Child View
        layoutChildren();
        mInLayout = false;

        mOverscrollMax = (b - t) / OVERSCROLL_LIMIT_DIVISOR;
    }

    // 获取一个Item View
    View obtainView(int position, boolean[] isScrap) {
        isScrap[0] = false;
        View scrapView;
        // 从缓存的Item View中获取,ListView的复用机制就在这里
        scrapView = mRecycler.getScrapView(position);

        View child;
        if (scrapView != null) {
            // 代码省略
            child = mAdapter.getView(position, scrapView, this);
            // 代码省略
        } else {
            child = mAdapter.getView(position, null, this);
            // 代码省略
        }

        return child;
    }
}
```

通过增加Adapter一层来将Item View的操作抽象起来，ListView等集合视图通过Adapter对象获得Item的个数、数据元素、Item View等，从而达到适配各种数据、各种Item视图的效果。

因为Item View和数据类型千变万化，Android的架构师们将这些变化的部分交给用户来处理，通过getCount、getItem、getView等几个方法抽象出来，也就是将Item View的构造过程交给用户来处理，灵活地运用了适配器模式，达到了无限适配、拥抱变化的目的。

# 外观模式（Facade Pattern）

   **外部与一个子系统通信，必须通过一个统一的外观对象进行。**

![avatar](/image/Facde_sample.png)

**外观角色类:**

### 外观模式示例
```
public class Facade {
    //示意方法，满足客户端需要的功能
    public void test(){
        ModuleA a = new ModuleA();
        a.testA();
        ModuleB b = new ModuleB();
        b.testB();
        ModuleC c = new ModuleC();
        c.testC();
    }
}
```

**客户端（外部调用者）角色类**

```
public class Client {

    public static void main(String[] args) {

        Facade facade = new Facade();
        facade.test();
    }

}
```

  **使用外观模式还有一个附带的好处，就是能够有选择性地暴露方法**

### 文件读取-文件加密-文件写入新文件

**EncryptFacade：加密外观类，充当外观类**

```
class EncryptFacade  
    {  
        //维持对其他对象的引用  
         private FileReader reader;  
        private CipherMachine cipher;  
        private FileWriter writer;  

        public EncryptFacade()  
        {  
            reader = new FileReader();  
            cipher = new CipherMachine();  
            writer = new FileWriter();  
        }  

        //调用其他对象的业务方法  
         public void FileEncrypt(string fileNameSrc, string fileNameDes)  
        {  
            string plainStr = reader.Read(fileNameSrc);  
            string encryptStr = cipher.Encrypt(plainStr);  
            writer.Write(encryptStr, fileNameDes);  
        }  
    }
 ```

**Program：客户端测试类**

```
class Program  
    {  
        static void Main(string[] args)  
        {  
            EncryptFacade ef = new EncryptFacade();  
            ef.FileEncrypt("src.txt", "des.txt");  
            Console.Read();  
        }  
    }
```

### 外观模式的优点
●　　松散耦合

外观模式松散了客户端与子系统的耦合关系，让子系统内部的模块能更容易扩展和维护。

●　　简单易用

外观模式让子系统更加易用，客户端不再需要了解子系统内部的实现，也不需要跟众多子系统内部的模块进行交互，只需要跟外观类交互就可以了。

●　　更好的划分访问层次

通过合理使用Facade，可以帮助我们更好地划分访问的层次。有些方法是对系统外的，有些方法是系统内部使用的。把需要暴露给外部的功能集中到外观中，这样既方便客户端使用，也很好地隐藏了内部的细节。


# 装饰者模式(Decorator)

   装饰者模式又名包装（Wrapper）模式, 装饰者模式以对客户端透明的方式扩展对象的功能，是继承关系的一个替代方案

   装饰者模式动态地将责任附加到对象身上。若要扩展功能，装饰者提供了比继承者更有弹性的替代方案

   装饰者模式可以在不使用传教更多的子类的情况下，将对象的功能加以扩展。
   
   用一个新类（包装类）来包装功能类，即新类持有功能类的引用，并且在新类中科院扩展功能类功能，**包装类跟功能类实现了同一个接口**，但抽象接口方法最终是在功能类中实现，包装类来实现调用和扩展功能类方法

```
public class RedShapeDecorator extends ShapeDecorator {
 
   public RedShapeDecorator(Shape decoratedShape) {
      super(decoratedShape);     
   }
 
   @Override
   public void draw() {
      decoratedShape.draw();         
      setRedBorder(decoratedShape);
   }
 
   private void setRedBorder(Shape decoratedShape){
      System.out.println("Border Color: Red");
   }
}
```

# 代理模式(Proxy)

### 代理模式定义

  给某一个对象提供一个代 理，并由代理对象控制对原对象的引用

  **代理模式包含如下角色：**

 1. Subject: 抽象主题角色： 代理类和真实实现类都继承的接口
 
 2. Proxy: 代理主题角色，持有真实代理类的引用。
 
 3. RealSubject: 真实主题角色，真实实现功能的类
 
   **抽象对象角色**
 
 ```
 public abstract class AbstractObject {
     //操作
     public abstract void operation();
 }
```
 
   **目标对象角色**
   
``` 
 public class RealObject extends AbstractObject {
     @Override
     public void operation() {
         //一些操作
         System.out.println("一些操作");
     }
 }
```
 
  **代理对象角色**
 
```
 public class ProxyObject extends AbstractObject{
     RealObject realObject = new RealObject();
     @Override
     public void operation() {
         //调用目标对象之前可以做相关操作
         System.out.println("before");        
         realObject.operation();        
         //调用目标对象之后可以做相关操作
         System.out.println("after");
     }
 }
```
 
  **客户端**
 
```
 public class Client {
     public static void main(String[] args) {
         AbstractObject obj = new ProxyObject();
         obj.operation();
     }
 }
```
### 优缺点
  **优点**
     给对象增加了本地化的扩展性，增加了存取操作控制
  **缺点**
     会产生多余的代理类


