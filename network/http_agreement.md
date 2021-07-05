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

# 3. Http各个协议版本异同




          
