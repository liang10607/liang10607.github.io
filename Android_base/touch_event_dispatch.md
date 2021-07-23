[回目录页](..)

# 1. Android事件分发机制简介

![事件分发机制详解](https://www.jianshu.com/p/38015afcdb58)

### 1.2 负责对事件进行分发的方法主要有三个，分别是：

* dispatchTouchEvent（
* onTouchEvent（）
* onInterceptTouchEvent（）。
它们并不存在于所有负责分发的组件中，其具体情况总结于下面的表格中：

|组件	dispatchTouchEvent	onTouchEvent	onInterceptTouchEvent
Activity	存在	存在	不存在
ViewGroup	存在	存在	存在
View	存在	存在	不存在
从表格中看，dispatchTouchEvent,onTouchEvent方法存在于上文的三个组件中。而onInterceptTouchEvent为ViewGroup独有。这些方法的具体作用在下文作介绍。

ViewGroup类中，实际是没有onTouchEvent方法的，但是由于ViewGroup继承自View，而View拥有onTouchEvent方法，故ViewGroup的对象也是可以调用onTouchEvent方法的。故在表格中表明ViewGroup中存在onTouchEvent方法的。

# 2.源码解析

### 2.1 Acvitity的事件分发

```

/**
  * 源码分析：Activity.dispatchTouchEvent（）
  */ 
  public boolean dispatchTouchEvent(MotionEvent ev) {

    // 仅贴出核心代码

    // ->>分析1
    if (getWindow().superDispatchTouchEvent(ev)) {

        return true;
        // 若getWindow().superDispatchTouchEvent(ev)的返回true
        // 则Activity.dispatchTouchEvent（）就返回true，则方法结束。即 ：该点击事件停止往下传递 & 事件传递过程结束
        // 否则：继续往下调用Activity.onTouchEvent

    }
    // ->>分析3
    return onTouchEvent(ev);
  }

/**
  * 分析1：getWindow().superDispatchTouchEvent(ev)
  * 说明：
  *     a. getWindow() = 获取Window类的对象
  *     b. Window类是抽象类，其唯一实现类 = PhoneWindow类
  *     c. Window类的superDispatchTouchEvent() = 1个抽象方法，由子类PhoneWindow类实现
  */
  @Override
  public boolean superDispatchTouchEvent(MotionEvent event) {

      return mDecor.superDispatchTouchEvent(event);
      // mDecor = 顶层View（DecorView）的实例对象
      // ->> 分析2
  }

/**
  * 分析2：mDecor.superDispatchTouchEvent(event)
  * 定义：属于顶层View（DecorView）
  * 说明：
  *     a. DecorView类是PhoneWindow类的一个内部类
  *     b. DecorView继承自FrameLayout，是所有界面的父类
  *     c. FrameLayout是ViewGroup的子类，故DecorView的间接父类 = ViewGroup
  */
  public boolean superDispatchTouchEvent(MotionEvent event) {

      return super.dispatchTouchEvent(event);
      // 调用父类的方法 = ViewGroup的dispatchTouchEvent()
      // 即将事件传递到ViewGroup去处理，详细请看后续章节分析的ViewGroup的事件分发机制

  }
  // 回到最初的分析2入口处

/**
  * 分析3：Activity.onTouchEvent()
  * 调用场景：当一个点击事件未被Activity下任何一个View接收/处理时，就会调用该方法
  */
  public boolean onTouchEvent(MotionEvent event) {

        // ->> 分析5
        if (mWindow.shouldCloseOnTouch(this, event)) {
            finish();
            return true;
        }
        
        return false;
        // 即 只有在点击事件在Window边界外才会返回true，一般情况都返回false，分析完毕
    }

/**
  * 分析4：mWindow.shouldCloseOnTouch(this, event)
  * 作用：主要是对于处理边界外点击事件的判断：是否是DOWN事件，event的坐标是否在边界内等
  */
  public boolean shouldCloseOnTouch(Context context, MotionEvent event) {

  if (mCloseOnTouchOutside && event.getAction() == MotionEvent.ACTION_DOWN
          && isOutOfBounds(context, event) && peekDecorView() != null) {

        // 返回true：说明事件在边界外，即 消费事件
        return true;
    }

    // 返回false：在边界内，即未消费（默认）
    return false;
  } 
```

### 2.2 ViewGroup的事件分发

```
/**
  * 源码分析：ViewGroup.dispatchTouchEvent（）
  */ 
  public boolean dispatchTouchEvent(MotionEvent ev) { 

  // 仅贴出关键代码
  ... 

  if (disallowIntercept || !onInterceptTouchEvent(ev)) {  
  // 分析1：ViewGroup每次事件分发时，都需调用onInterceptTouchEvent()询问是否拦截事件
    // 判断值1-disallowIntercept：是否禁用事件拦截的功能(默认是false)，可通过调用requestDisallowInterceptTouchEvent()修改
    // 判断值2-!onInterceptTouchEvent(ev) ：对onInterceptTouchEvent()返回值取反
        // a. 若在onInterceptTouchEvent()中返回false，即不拦截事件，从而进入到条件判断的内部
        // b. 若在onInterceptTouchEvent()中返回true，即拦截事件，从而跳出了该条件判断
        // c. 关于onInterceptTouchEvent() ->>分析1

  // 分析2
    // 1. 通过for循环，遍历当前ViewGroup下的所有子View
    for (int i = count - 1; i >= 0; i--) {  
        final View child = children[i];  
        if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE  
                || child.getAnimation() != null) {  
            child.getHitRect(frame);  

            // 2. 判断当前遍历的View是不是正在点击的View，从而找到当前被点击的View
            if (frame.contains(scrolledXInt, scrolledYInt)) {  
                final float xc = scrolledXFloat - child.mLeft;  
                final float yc = scrolledYFloat - child.mTop;  
                ev.setLocation(xc, yc);  
                child.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;  

                // 3. 条件判断的内部调用了该View的dispatchTouchEvent()
                // 即 实现了点击事件从ViewGroup到子View的传递（具体请看下面章节介绍的View事件分发机制）
                if (child.dispatchTouchEvent(ev))  { 

                // 调用子View的dispatchTouchEvent后是有返回值的
                // 若该控件可点击，那么点击时dispatchTouchEvent的返回值必定是true，因此会导致条件判断成立
                // 于是给ViewGroup的dispatchTouchEvent()直接返回了true，即直接跳出
                // 即该子View把ViewGroup的点击事件消费掉了

                mMotionTarget = child;  
                return true; 
                      }  
                  }  
              }  
          }  
      }  
    }  

  ...

  return super.dispatchTouchEvent(ev);
  // 若无任何View接收事件(如点击空白处)/ViewGroup本身拦截了事件(复写了onInterceptTouchEvent()返回true)
  // 会调用ViewGroup父类的dispatchTouchEvent()，即View.dispatchTouchEvent()
  // 因此会执行ViewGroup的onTouch() -> onTouchEvent() -> performClick（） -> onClick()，即自己处理该事件，事件不会往下传递
  // 具体请参考View事件分发机制中的View.dispatchTouchEvent()

  ... 

}

/**
  * 分析1：ViewGroup.onInterceptTouchEvent()
  * 作用：是否拦截事件
  * 说明：
  *     a. 返回false：不拦截（默认）
  *     b. 返回true：拦截，即事件停止往下传递（需手动复写onInterceptTouchEvent()其返回true）
  */
  public boolean onInterceptTouchEvent(MotionEvent ev) {  
    
    // 默认不拦截
    return false;

  } 
  // 回到调用原处
```

### 2.3 View的事件分发

```
/**
  * 源码分析：View.dispatchTouchEvent（）
  */
  public boolean dispatchTouchEvent(MotionEvent event) {  

       
        if ( (mViewFlags & ENABLED_MASK) == ENABLED && 
              mOnTouchListener != null &&  
              mOnTouchListener.onTouch(this, event)) {  
            return true;  
        } 

        return onTouchEvent(event);  
  }

// 1. 注册Touch事件监听setOnTouchListener 且 在onTouch()返回false
button.setOnTouchListener(new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("执行了onTouch(), 动作是:" + event.getAction());
            return true;
        }
    });

// 2. 注册点击事件OnClickListener()
button.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            System.out.println("执行了onClick()");
        }
        
  });

```

  若是设置了OnTouchListener事件监听，且接口中的ontouch方法返回了true,则表明，事件分发结束，不会再调用View的onTouchEvent()方法，而onClickListener依赖于onTouchEvent实现的，此时onClickListener也不会执行了
  
  若是OnTouchListerner的onTouch方法返回false，则即执行了OnTouchLister接口，也会执行onClick接口。
  
# 3、 思考点

* **onTouch()和onTouchEvent()的区别**
这两个方法都是在View的dispatchTouchEvent中调用，但onTouch优先于onTouchEvent执行。

如果在onTouch方法中返回true将事件消费掉，onTouchEvent()将不会再执行。

特别注意：请看下面代码

```
//&&为短路与，即如果前面条件为false，将不再往下执行
//所以，onTouch能够得到执行需要两个前提条件：
//1. mOnTouchListener的值不能为空
//2. 当前点击的控件必须是enable的。
mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&  
          mOnTouchListener.onTouch(this, event)

```

* **Touch事件的后续事件（MOVE、UP）层级传递**
因此如果你有一个控件是非enable的，那么给它注册onTouch事件将永远得不到执行。对于这一类控件，如果我们想要监听它的touch事件，就必须通过在该控件中重写onTouchEvent方法来实现。

如果给控件注册了Touch事件，每次点击都会触发一系列action事件（ACTION_DOWN，ACTION_MOVE，ACTION_UP等）

当dispatchTouchEvent在进行事件分发的时候，**只有前一个事件（如ACTION_DOWN）返回true，才会收到后一个事件（ACTION_MOVE和ACTION_UP）**

即如果在执行ACTION_DOWN时返回false，后面一系列的ACTION_MOVE和ACTION_UP事件都不会执行

从上面对事件分发机制分析知：

dispatchTouchEvent()和 onTouchEvent()消费事件、终结事件传递（返回true）

而onInterceptTouchEvent 并不能消费事件，它相当于是一个分叉口起到分流导流的作用，对后续的ACTION_MOVE和ACTION_UP事件接收起到非常大的作用

请记住：接收了ACTION_DOWN事件的函数不一定能收到后续事件（ACTION_MOVE、ACTION_UP）


![avatar](/image/touch_dispatch_flow.png)