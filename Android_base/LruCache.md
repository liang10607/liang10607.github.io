[回目录页](..)

# 1. LRU算法简介

  LRU是最近最少使用的算法，它的核心思想是当缓存满时，会优先淘汰那些最近最少使用的缓存对象。
  
  采用LRU算法的缓存有两种：LrhCache和DisLruCache，分别用于实现内存缓存和硬盘缓存，其核心思想都是LRU缓存算法。
  
# 2. LRU使用

   LruCache是Android 3.1所提供的一个缓存类，所以在Android中可以直接使用LruCache实现内存缓存。

   而DisLruCache目前在Android 还不是Android SDK的一部分，但Android官方文档推荐使用该算法来实现硬盘缓存。

   LruCache是个泛型类，主要算法原理是把最近使用的对象用强引用（即我们平常使用的对象引用方式）存储在 LinkedHashMap 中。
   
   当缓存满时，把最近最少使用的对象从内存中移除，并提供了get和put方法来完成缓存的获取和添加操作。
   
* **实际使用**

```
int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
   int cacheSize = maxMemory / 8;
   mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
       @Override
       protected int sizeOf(String key, Bitmap value) {
           return value.getRowBytes() * value.getHeight() / 1024;
       }
   };
```   

①设置LruCache缓存的大小，一般为当前进程可用**容量的1/8**
②重写sizeOf方法，计算出要缓存的每张图片的大小。

**注意**： 缓存的总容量和每个缓存对象的大小所用单位要一致。


# 2. LRU原理

LruCache的核心思想很好理解，就是要维护一个缓存对象列表，其中对象列表的排列方式是按照访问顺序实现的，即一直没访问的对象，将放在队尾，即将被淘汰。

而最近访问的对象将放在队头，最后被淘汰

那么这个队列到底是由谁来维护的，前面已经介绍了是由LinkedHashMap来维护。

### 2.1 LinkedHashMap

  由于LinkedHashMap是HashMap的子类，所以LinkedHashMap自然会拥有HashMap的所有特性。

  它额外维护了一个双向链表用于保持迭代顺序，LinkedHashMap可以很好的支持LRU算法。
  
  LinkedHashMap增加了时间和空间上的开销，但是它通过维护一个额外的双向链表保证了迭代顺序。
  
  特别地，该迭代顺序可以是插入顺序，也可以是访问顺序。
  
  因此，根据链表中元素的顺序可以将LinkedHashMap分为：保持插入顺序的LinkedHashMap 和 保持访问顺序的LinkedHashMap，其中LinkedHashMap的**默认实现是按插入顺序排序的**。
  
* 成员变量定义

```
  /**
     * The head of the doubly linked list.
     */
    private transient Entry<K,V> header;  // 双向链表的表头元素

    /**
     * The iteration ordering method for this linked hash map: <tt>true</tt>
     * for access-order, <tt>false</tt> for insertion-order.
     *
     * @serial
     */
    private final boolean accessOrder;  //true表示按照访问顺序迭代，false时表示按照插入顺序 
```    

* LinkedHashMap的Entry
　　LinkedHashMap采用的hash算法和HashMap相同，但是它重新定义了Entry。LinkedHashMap中的Entry增加了两个指针 before 和 after，它们分别用于维护双向链接列表。

    特别需要注意的是，next用于维护HashMap各个桶中Entry的连接顺序，before、after用于维护Entry插入的先后顺序的，源代码如下：

```
private static class Entry<K,V> extends HashMap.Entry<K,V> {

    // These fields comprise the doubly linked list used for iteration.
    Entry<K,V> before, after;

    Entry(int hash, K key, V value, HashMap.Entry<K,V> next) {
        super(hash, key, value, next);
    }
    ...
}
```

### 2.2 LRU基于LinkedHashMap实现
```
public LruCache(int maxSize) {
	if (maxSize <= 0) {
		throw new IllegalArgumentException("maxSize <= 0");
	}
	this.maxSize = maxSize;
	this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
}
```

**put()方法**

```
public final V put(K key, V value) {
         //不可为空，否则抛出异常
	if (key == null || value == null) {
		throw new NullPointerException("key == null || value == null");
	}
	V previous;
	synchronized (this) {
            //插入的缓存对象值加1
		putCount++;
            //增加已有缓存的大小
		size += safeSizeOf(key, value);
           //向map中加入缓存对象
		previous = map.put(key, value);
            //如果已有缓存对象，则缓存大小恢复到之前
		if (previous != null) {
			size -= safeSizeOf(key, previous);
		}
	}
        //entryRemoved()是个空方法，可以自行实现
	if (previous != null) {
		entryRemoved(false, key, previous, value);
	}
        //调整缓存大小(关键方法)
	trimToSize(maxSize);
	return previous;
}
```

可以看到put()方法并没有什么难点，重要的就是在添加过缓存对象后，调用 trimToSize()方法，来判断缓存是否已满，如果满了就要删除近期最少使用的算法。

**trimToSize()方法**

```
public void trimToSize(int maxSize) {
    //死循环
	while (true) {
		K key;
		V value;
		synchronized (this) {
            //如果map为空并且缓存size不等于0或者缓存size小于0，抛出异常
			if (size < 0 || (map.isEmpty() && size != 0)) {
				throw new IllegalStateException(getClass().getName()
					+ ".sizeOf() is reporting inconsistent results!");
			}
            //如果缓存大小size小于最大缓存，或者map为空，不需要再删除缓存对象，跳出循环
			if (size <= maxSize || map.isEmpty()) {
				break;
			}
            //迭代器获取第一个对象，即队尾的元素，近期最少访问的元素
			Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
			key = toEvict.getKey();
			value = toEvict.getValue();
            //删除该对象，并更新缓存大小
			map.remove(key);
			size -= safeSizeOf(key, value);
			evictionCount++;
		}
		entryRemoved(true, key, value, null);
	}
}
```

当调用LruCache的get()方法获取集合中的缓存对象时，就代表访问了一次该元素，将会更新队列，保持整个队列是按照访问顺序排序。这个更新过程就是在LinkedHashMap中的get()方法中完成的。

先看LruCache的get()方法

get()方法

```
public final V get(K key) {
        //key为空抛出异常
	if (key == null) {
		throw new NullPointerException("key == null");
	}

	V mapValue;
	synchronized (this) {
            //获取对应的缓存对象
            //get()方法会实现将访问的元素更新到队列头部的功能
		mapValue = map.get(key);
		if (mapValue != null) {
			hitCount++;
			return mapValue;
		}
		missCount++;
	}

```

其中LinkedHashMap的get()方法如下：

```
public V get(Object key) {
	LinkedHashMapEntry<K,V> e = (LinkedHashMapEntry<K,V>)getEntry(key);
	if (e == null)
		return null;
    //实现排序的关键方法
	e.recordAccess(this);
	return e.value;
}
调用recordAccess()方法如下：

void recordAccess(HashMap<K,V> m) {
	LinkedHashMap<K,V> lm = (LinkedHashMap<K,V>)m;
    //判断是否是访问排序
	if (lm.accessOrder) {
		lm.modCount++;
        //删除此元素
		remove();
        //将此元素移动到队列的头部
		addBefore(lm.header);
	}
}
```

由此可见LruCache中维护了一个集合LinkedHashMap，该LinkedHashMap是以访问顺序排序的。
当调用put()方法时，就会在结合中添加元素，并调用trimToSize()判断缓存是否已满，如果满了就用LinkedHashMap的迭代器删除队尾元素，即近期最少访问的元素。

当调用get()方法访问缓存对象时，就会调用LinkedHashMap的get()方法获得对应集合元素，同时会更新该元素到队头。
