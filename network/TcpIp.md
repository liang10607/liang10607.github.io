# TCP/IP相关

![avatar](/image/tcpip_struct.png)

 如上图所示，tcp/ip相关知识包含多层内容

* TCP/IP模式
* 数据链路层
* 网络层
* ping
* traceroute
* tcp/udp
* dns
* tcp连接的建立和终止
* tcp流量控制
* tcp拥塞控制

# 1. TCP/IP协议模型

  TCP/IP包含了一系列构成互联网基础的网络协议，是Internet的核心协议

![avatar](/image/tcpip_osi.png)

* 应用层：http,ftp等
* 传输层：TCP和UDP协议
* 网络层：IP协议，它负责堆数据加上IP地址和其他数据以确定传输的目标
* 网络接口层：数据链路层，为待传输的数据加入一个以太网头，并进行CRC编码，为最后的数据传输做准备

![avatar](/image/tcpip_data_structs.png)

**Http应用层协议得数据封装传输过程**

![avatar](/image/tcpip_http_data_structs.png)

# 2. 数据链路层

 把IP协议封装过的数据转化为二级制数据，并把这些二进制数据转成数据帧从一个节点传输到临近得另外一节点，这些节点是通过Mac来唯一标识的

 ![tcpip_link_struct_data](/image/tcpip_link_struct_data.png)

* 封装成帧：把网络层数据报加头和尾，封装成帧，帧头中包括源MAC地址和目的MAC地址。
* 透明传输：零比特填充、转义字符。
* 可靠传输：在出错率很低的链路上很少用，但是无线链路WLAN会保证可靠传输。
* 差错检测(CRC)：接收者检测错误,如果发现差错，丢弃该帧。

# 3. 网络层（IP协议）

  IP协议是ICP/IP协议的核心，所有的TCP，UDP,ICMP,IGCP的数据都是以IP数据格式传输。

  **注意：** IP不是可靠的协议，其没有提供数据未传达以后的处理机制，数据传输的可靠性是传输层ICP/udp要做的事情

### 3.1 IP地址

* A类IP地址：1.0.0.0~127.0.0.0
* B类IP地址：128.0.0.0~191.255.255.255
* C类IP地址：192.0.0.0~223.255.255.2

### IP协议头

![tcpip_ip_header_structs](../image/tcpip_ip_header_structs.png)






