[回目录页](/README.md)

## Java集合框架

* 集合分类

  Java集合大致可以分为Set、List、Queue和Map四种体系其中Set代表无序、不可重复的集合；List代表有序、重复的集合；而Map则代表具有映射关系的集合。Java 5 又增加了Queue体系集合，代表一种队列集合实现。
Java集合就像一种容器，可以把多个对象（实际上是对象的引用，但习惯上都称对象）“丢进”该容器中。从Java 5 增加了泛型以后，Java集合可以记住容器中对象的数据类型，使得编码更加简洁、健壮。


* Java集合和数组的区别：

1. 数组长度在初始化时指定，意味着只能保存定长的数据。而集合可以保存数量不确定的数据。同时可以保存具有映射关系的数据（即关联数组，键值对 key-value）。
2. 数组元素即可以是基本类型的值，也可以是对象。集合里只能保存对象（实际上只是保存对象的引用变量），基本数据类型的变量要转换成对应的包装类才能放入集合类中。

### ArrayList
  数据存储方式是数据，数组的默认长度是10，创建ArrayList对象时 ，可以设定器初始长度。每次添加数据时，会判断当前长度是否已经不够用时，会按照1.5的倍数扩大数据范围，进行数据长度扩容时，使用Systemd的api Arrays.copyOf

### LinkedList

1. LinkedList是基于双向链表实现，通过index访问元素时，需要进行遍历查询，i小于size/2时从链表表头开始查，若是i大于size/2则从末尾往前遍历。

2. 通过下标进行的删除和插入操作也需要遍历链表

3. 随机存取，ArrayList的效率要高于LinkedList。插入和删除操作，两者的效率差异要根据实际情况分析

### HashMap

1. 两个重要的参数：容量，负载因子。容量表示的是链表Node数组结构的length,Load Factr就是数据Bucket填满的最大比例。当数据Bucket已经填充的内容数量大于Capacity*load factor仕就需要调整bucket数组的大小为当前的2倍了

2. Hashmap数据put的大致思路为：
     (1) 对key的hashcode进行高低位异或后与capacity进行位的&运算得到index
     (2) 如果没用碰撞，直接创建一个链表节点，并放入数据的index位置
     (3) 如果计算出来的数组index所在位置有数据，即发生了碰撞，则把该数据存储到链表中
    （4）存入链表中时，也需要判断链表的长度，若是链表长度超过8，则转为红黑树.链表插入时，会进行equals判断，看数据是否已经存在
    （5）若是发现节点粗真乃，则替换old value(保证key的唯一性)
    （6）若是数组结构bucket满了（超过capacity*load factor），就要resize,即扩容
     (7) bucket碰撞时，是hash碰撞，话需要进行equals比较

3. Hash函数的实现
```
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

### LinkedHashMap
1. 集成自HashMap,具有HashMap所有功能，但比HashMap多了一个双向链表存储EntrySet,保持数据(Value)的有序性
2. 对HashMap的EntrySet进行访问，插入，删除操作，都会重新排需那个双向链表，重要的函数主要包括：
```
// Callbacks to allow LinkedHashMap post-actions
void afterNodeAccess(Node<K,V> p) { }
void afterNodeInsertion(boolean evict) { }
void afterNodeRemoval(Node<K,V> p) { }
```

### TreeMap
1. TreeMap类似于HashMap,它可以保证Key的有序性
2. 存储数据是基于红黑树实现的
3. 红黑树算法结构，还需花实际细细学习，再来看TreeMap



