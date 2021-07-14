[回目录页](..)

# Http协议

![avatar](/image/http_base_structs.png)

# 1. tcp/ip结构

### 1.1 四层协议系统

![avatar](/image/http_tcp_ip_protocal.png)

* 链路层

   也称做数据链路层或者网络接口层，通常包括操作系统中的设备驱动和网络接口卡，他们一起处理与电缆的接口细节。ARP和RARP协议位于这一层，mac地址解析。
   
* 网络层

   网络层协议包括IP协议（网际协议），ICMP协议（Internet互联网控制报文协议），以及IGMP协议（Internet组管理协议）  
   
   其中ICMP:IP层用它来与其他主机或路由器交换错误报文和其他重要信息
   
   IGMP:nternet组管理协议。它用来把一个UDP数据报多播到多个主机
   
* 传输层

   主要指的就是TCP和UDP
   
* 应用层

  应用层决定了向用户提供应用服务时通信的活动。TCP/IP 协议族内预存了各类通用的应用服务。包括 HTTP，FTP（File Transfer Protocol，文件传输协议），DNS（Domain Name System，域名系统）服务

###  1.2 网络数据封装过程 

![avatar](/image/network_data_structs.png)

![avatar](/image/network_data_parser_flow.png)

![avatar](/image/network_http_data_paser_flow.png)

# 2. Http请求报文

![avatar](/image/http_data_structs.png)


![avatar](/image/http_response_struct.png)

# 3. Http网络交互

### http状态码

* 1xx：表示服务器已接收了客户端请求，客户端可继续发送请求;
* 2xx：表示服务器已成功接收到请求并进行处理;
* 3xx：表示服务器要求客户端重定向;
* 4xx：表示客户端的请求有非法内容;
* 5xx：表示服务器未能正常处理客户端的请求而出现意外错误;  

### http请求响应顺序

1.客户端连接到web服务器：HTTP 客户端与web服务器建立一个 TCP 连接;
2.客户端向服务器发起 HTTP 请求：通过已建立的TCP 连接，客户端向服务器发送一个请求报文;
3.服务器接收 HTTP 请求并返回 HTTP 响应：服务器解析请求，定位请求资源，服务器将资源副本写到 TCP 连接，由客户端读取;
4.释放 TCP 连接：若connection 模式为close，则服务器主动关闭TCP 连接，客户端被动关闭连接，释放TCP 连接;若connection 模式为keepalive，则该连接会保持一段时间，在该时间内可以继续接收请求;

### 浏览器输入url后，网络流程

1、浏览器向DNS 服务器请求解析该 URL 中的域名所对应的 IP 地址;
2、解析出 IP 地址后，根据该 IP 地址和默认端口 80，和服务器建立 TCP 连接;
3、浏览器发出读取文件(URL 中域名后面部分对应的文件)的HTTP 请求，该请求报文作为 TCP 三次握手的第三个报文的数据发送给服务器;
4、服务器对浏览器请求作出响应，并把对应的 html 文本发送给浏览器;
5、释放 TCP 连接;
6、浏览器将该 html 文本并显示内容;



# 4. Http各个协议版本异同

### http 1.0和http1.1的差异

1. http1.1的缓存策略比http1.0丰富
2. http1.1支持断点续传，引入了range头域
3. http1.1增加了24个错误码，如409（Conflict）表示请求的资源与资源的当前状态发生冲突；410（Gone）表示服务器上的某个资源被永久性的删除 
4. http1.1支持同一个主机上（同一个IP）有多个域名的情况
5. http1.1开始支持长连接：HTTP 1.1支持长连接（PersistentConnection）和请求的流水线（Pipelining）处理，在一个TCP连接上可以传送多个HTTP请求和响应，减少了建立和关闭连接的消耗和延迟，在HTTP1.1中默认开启Connection： keep-alive，一定程度上弥补了HTTP1.0每次请求都要创建连接的缺点

### Http1.0的队头阻塞问题

Http1.1 通过引入长连接和流水线技术处理了这个问题。 通过长连接解决了http1.0重复连接的资源消耗。

http1.1就允许客户端通过同一连接发送多个请求。不巧的是，这优化策略有个瓶颈。

当一个队头的请求不能收到响应的资源，它将会阻塞后面的请求。这就是知名的队头阻塞问题。虽然添加**并行的tcp连接**能够减轻这个问题，但是tcp连接的数量是有限的，且每个新的连接需要额外的资源



### http2.0的新特性

* **Http2 二进制框架层的优势**

 在http2 ， 二进制框架层编码 请求和响应 并把它们分成更小的包，能显著的提高传输的灵活性.
 
* **Http2 流的优先级**

流优先级不仅仅解决同一资源的竞态问题，也允许开发人员自定义请求的权重。

* **http的缓冲区溢出问题**

    因为Http1.1 依靠传输层来避免流溢出，每个tcp连接需要一个独立的流控制机制。

    http2通过一个连接来多路复用。 结果是在传输层的tcp连接不足以管理每个流的发送。http2允许客户端和服务器端实现他们自己的流控制机制，而不是依赖传输层。两端在传输层交换可用的缓冲区大小，来让他们在多路复用流上设置自己的接收窗口
    
 
* **压缩技术**
常用的优化web应用是用压缩算法减少HTTP消息大小。 HTTP/1.1 和2 都用该技术HTTP/1.1不支持整个消息压缩。下面我们来聊聊区别。

Http1.1
Gzip已经被用于压缩http消息很久了，特别是减少CSS和JS脚本的文件。然而消息头部分依然是纯文本发送。尽管每个头都很小，但随着请求越来越多，连接的负担就会越重，如果带了cookie. Header将变得更大。

Http2
Http2 的二进制框架层在细节上表现出强大的控制力，在头压缩上也是如此。 http2 能把头从他们的数据中分离，并封城头帧和数据帧。 http2特定的压缩程序HPACK可以压缩头帧。 该算法用Huffman编码头metadata，可以很大程度上减少头大小。此外， HPACK可以跟踪先前传输的metadata字段，然后通过动态改变服务器端和客户端共享的索引来进一步压缩。如下：

          