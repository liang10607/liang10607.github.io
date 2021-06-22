[回目录页](..)

# 栈

### 简介
   栈是一种线性存储结构，有以下连个特点：
   
   * 1. 数据是后进先出
   
   * 2. 数据的添加和删除，都只能在栈顶进行
   
   栈通常有以下三种操作：
   
   * 1. push，向栈顶添加元素
   * 2. peed，返回栈顶元素
   * 3. pop,返回并删除栈顶元素
   
### 栈的Java实现

```
/**
 * Java : 数组实现的栈，能存储任意类型的数据
 *
 * @author skywang
 * @date 2013/11/07
 */
import java.lang.reflect.Array;

public class GeneralArrayStack<T> {

    private static final int DEFAULT_SIZE = 12;
    private T[] mArray;
    private int count;

    public GeneralArrayStack(Class<T> type) {
        this(type, DEFAULT_SIZE);
    }

    public GeneralArrayStack(Class<T> type, int size) {
        // 不能直接使用mArray = new T[DEFAULT_SIZE];
        mArray = (T[]) Array.newInstance(type, size);
        count = 0;
    }

    // 将val添加到栈中
    public void push(T val) {
        mArray[count++] = val;
    }

    // 返回“栈顶元素值”
    public T peek() {
        return mArray[count-1];
    }

    // 返回“栈顶元素值”，并删除“栈顶元素”
    public T pop() {
        T ret = mArray[count-1];
        count--;
        return ret;
    }

    // 返回“栈”的大小
    public int size() {
        return count;
    }

    // 返回“栈”是否为空
    public boolean isEmpty() {
        return size()==0;
    }

    // 打印“栈”
    public void PrintArrayStack() {
        if (isEmpty()) {
            System.out.printf("stack is Empty\n");
        }

        System.out.printf("stack size()=%d\n", size());

        int i=size()-1;
        while (i>=0) {
            System.out.println(mArray[i]);
            i--;
        }
    }
}
```   

# 队列

  队列也是一种线性存储结构，具有以下特点：
  
  * 1. 先进先出的数据结构
  
  * 2. 只能在队尾添加元素，在队首删除元素
  
  LinkedList即是双向列表实现，同时也实现了队列接口Queue。
  
  队列Queue常用方法包括
  * add 增加一个元索 如果队列已满，则抛出一个IIIegaISlabEepeplian异常
  * remove 移除并返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
  * element 返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
  * offer 添加一个元素并返回true 如果队列已满，则返回false
  * poll 移除并返问队列头部的元素 如果队列为空，则返回null
  * peek 返回队列头部的元素 如果队列为空，则返回null
  * put 添加一个元素 如果队列满，则阻塞
  * take 移除并返回队列头部的元素 如果队列为空，则阻塞

![avatar](/image/queue_method.png)


```
/**
 * Java : 数组实现“队列”，只能存储int数据。
 *
 * @author skywang
 * @date 2013/11/07
 */
public class ArrayQueue {

    private int[] mArray;
    private int mCount;

    public ArrayQueue(int sz) {
        mArray = new int[sz];
        mCount = 0;
    }

    // 将val添加到队列的末尾
    public void add(int val) {
        mArray[mCount++] = val;
    }

    // 返回“队列开头元素”
    public int front() {
        return mArray[0];
    }

    // 返回“队首元素值”，并删除“队首元素”
    public int pop() {
        int ret = mArray[0];
        mCount--;
        for (int i=1; i<=mCount; i++)
            mArray[i-1] = mArray[i];
        return ret;
    }

    // 返回“栈”的大小
    public int size() {
        return mCount;
    }

    // 返回“栈”是否为空
    public boolean isEmpty() {
        return size()==0;
    }
}
``` 
 
# 堆

   堆是一种二叉树，由它实现的优先级队列的插入和删除操作的时间复杂度是O(Log N)
   
   Java堆和数据结构堆是两种概念：
   
   * Java存储堆是计算机内存的一个空间，用于存储全局的对象内存
   * 数据结构堆是一种特殊的二叉树
   
### 数据结构堆
 
   * 堆是一个完全二叉树，也就是说除了最后一层节点不需要是满的外，其他层左右两侧都必须是满的。

![avatar](/image/heap_tree.png)
   
   * 堆的完全二叉树常常用数组来描述
   
![avatar](/image/heap_tree_arry.png)
 
   * 堆中每一个节点都满足堆的条件，也就是说每一个关键字的值都大于或等于这个节点的子节点的关键字值
   
   * 堆的二叉树是弱序的，即其左右节点并没有顺序规则，只要求了父子节点要要排序规则。
   > 堆的这种近乎无序的规则似乎毫无用处，不过对于快速移除最大节点的操作，以及快速插入新节点的操作，这种顺序已经足够了

  * 堆虽然是一颗树，但是通常存放在一个数组中，父节点和孩子节点的父子关系通过数组下标来确定(从上至下，再从左到右)

![avatar](/image/heap_tree_arary_contact.png)
 
**根据堆节点的下表找到其左右子节点和父节点：**

```
 public int left(int i) {
        return (i + 1) * 2 - 1;
    }

    public int right(int i) {
        return (i + 1) * 2;
    }

    public int parent(int i) {
        // i为根结点
        if (i == 0) {
            return -1;
        }
        return (i - 1) / 2;
    }
```

[java数据结构堆](https://blog.csdn.net/u013728021/article/details/84034420)
 
  