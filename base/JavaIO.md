# Java IO和文件
## Java IO数据流
### 字节流和字符流
 字节流和字符流字节流和字符流和用法几乎完全一样，区别在于字节流和字符流所操作的数据单元不同。
字符流的由来： 因为数据编码的不同，而有了对字符进行高效操作的流对象。本质其实就是基于字节流读取时，去查了指定的码表。字节流和字符流的区别：
1. 读写单位不同：字节流以字节（8bit）为单位，字符流以字符为单位，根据码表映射字符，一次可能读多个字节。
2. 处理对象不同：字节流能处理所有类型的数据（如图片、avi等），而字符流只能处理字符类型的数据。

只要是处理纯文本数据，就优先考虑使用字符流。 除此之外都使用字节流。

### 节点流和处理流
按照流的角色来分，可以分为节点流和处理流。
可以从/向一个特定的IO设备（如磁盘、网络）读/写数据的流，称为节点流，节点流也被成为低级流。
处理流是对一个已存在的流进行连接或封装，通过封装后的流来实现数据读/写功能，处理流也被称为高级流。
```
//节点流，直接传入的参数是IO设备
FileInputStream fis = new FileInputStream("test.txt");
//处理流，直接传入的参数是流对象
BufferedInputStream bis = new BufferedInputStream(fis);
```
### 字节流和字符流结构图
![avatar](/image/java_io_class.png)

## Java NIO
### 一、NIO的概念Java
NIO(New IO)是一个可以替代标准Java IO API的IO API(从Java1.4开始)，Java NIO提供了与标准IO不同的IO工作方式。

所以Java NIO是一种新式的IO标准，与之间的普通IO的工作方式不同。标准的IO基于字节流和字符流进行操作的，而NIO是基于通道(Channel)和缓冲区(Buffer)进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入通道也类似。

由上面的定义就说明NIO是一种新型的IO，但NIO不仅仅就是等于Non-blocking IO（非阻塞IO），NIO中有实现非阻塞IO的具体类，但不代表NIO就是Non-blocking IO（非阻塞IO）。

Java NIO 由以下几个核心部分组成：

Buffer
Channel
Selector
传统的IO操作面向数据流，意味着每次从流中读一个或多个字节，直至完成，数据没有被缓存在任何地方。NIO操作面向缓冲区，数据从Channel读取到Buffer缓冲区，随后在Buffer中处理数据。

### 分配一个Buffer（Allocating a Buffer）
为了获取一个Buffer对象，你必须先分配。每个Buffer实现类都有一个allocate()方法用于分配内存。下面看一个实例,开辟一个48字节大小的buffer：
```
ByteBuffer buf = ByteBuffer.allocate(48);
```
开辟一个1024个字符的CharBuffer：
```
CharBuffer buf = CharBuffer.allocate(1024);
```
### Buffer的实现类
![avatar](/image/buffer_impl.png)

## Channel的使用
Java NIO Channel通道和流非常相似，主要有以下几点区别：

* 通道可以读也可以写，流一般来说是单向的（只能读或者写）。
* 通道可以异步读写。
* 通道总是基于缓冲区Buffer来读写。
* 正如上面提到的，我们可以从通道中读取数据，写入到buffer；也可以中buffer内读数据，写入到通道中。下面有个示意图：

Channel的实现类有：
* FileChannel
* DatagramChannel
* SocketChannel
* ServerSocketChannel

FileChannel用于文件的数据读写。 DatagramChannel用于UDP的数据读写。 SocketChannel用于TCP的数据读写。 ServerSocketChannel允许我们监听TCP链接请求，每个请求会创建会一个SocketChannel。
Channel使用实例
```
RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
    FileChannel inChannel = aFile.getChannel();

    ByteBuffer buf = ByteBuffer.allocate(48);

    int bytesRead = inChannel.read(buf);
    while (bytesRead != -1) {

      System.out.println("Read " + bytesRead);
      buf.flip();

      while(buf.hasRemaining()){
          System.out.print((char) buf.get());
      }

      buf.clear();
      bytesRead = inChannel.read(buf);
    }
    aFile.close();
```

### 阻塞/非阻塞/同步/非同步的关系

### NIO中的blocking IO/nonblocking IO/IO multiplexing/asynchronous IO

##  Selector使用
Selector是Java NIO中的一个组件，用于检查一个或多个NIO Channel的状态是否处于可读、可写。如此可以实现单线程管理多个channels,也就是可以管理多个网络链接。Selector与Channel之间的关系图如下：
![avatar](/image/java_selector.png)

### Selector具体使用步骤
1. 创建一个Selector可以通过Selector.open()方法：
```
Selector selector = Selector.open();
```
2. 注册Channel到Selector上：
```
channel.configureBlocking(false);
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
```
3. Selector使用的完整案例如下代码：
```
Selector selector = Selector.open();

channel.configureBlocking(false);

SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

while(true) {

  int readyChannels = selector.select();

  if(readyChannels == 0) continue;

  Set<SelectionKey> selectedKeys = selector.selectedKeys();

  Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

  while(keyIterator.hasNext()) {

    SelectionKey key = keyIterator.next();

    if(key.isAcceptable()) {
        // a connection was accepted by a ServerSocketChannel.

    } else if (key.isConnectable()) {
        // a connection was established with a remote server.

    } else if (key.isReadable()) {
        // a channel is ready for reading

    } else if (key.isWritable()) {
        // a channel is ready for writing
    }

    keyIterator.remove();
  }
}
```

