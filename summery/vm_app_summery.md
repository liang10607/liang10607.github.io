# 1. VM App已有技术总结

### 1.1 硬件和系统相关相关

* 硬件控制，JNI技术，串口协议对接；指纹锁，纸币器，流量探针，电机驱动，电子锁，压缩机等等

* 基于硬件的一整套异常处理机制，硬件状态机，线上向下联动的故障处理机制；

* Android系统的的远程屏幕控制

* 系统运行状态相关的Monitor监控

* Launcher桌面及其对应的离线密码验证

### 1.2 Vm app基本功能应用技术

* 网络相关：OkHttp，Retrofit,HttpDNS，断点续传；长连接：mina, mqtt, edp。

* 数据加密压缩：md5加盐，数据验签，edp非对称加密，mqtt的证书校验，https证书的双向校验

* Android动画，视频播放

* Fragment管理

* 组件化，模块化

* AOP技术

* Kotlin , 协程；

* Java线程池，多线程同步

* VM App的MVP重构

* MVVM架构搭建和开发

* 第三方SDK: EventBus,Glide,Retrofit,Okhttp,ARoute，Dagger等

* 不同App之间的provider跨进程通讯


### 1.3 性能监控和优化

* 省流量优化：分app流量消耗监控，接口维度流量消耗监控；图片的webp，资源的重复下载问题，http的zstd数据压缩，部分接口的频繁调用，断点续传重试的流量消耗。

call_time	
dns_time	
connect_time	
handshake_time	
business_time	
byte_count

* 弱网优化：HttpDNS，离线二维码，数据同步机制，网络故障的自重启机制

* 性能监控：Bugly监控，Leakcanary，自研的页面卡顿监控

* 性能优化：绘制优化，泄漏优化，内存优化，ANR。 

# 2. 目前的VM优化的一些思考



# 3. 长远规划




