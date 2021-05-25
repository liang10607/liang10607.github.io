[回目录页](..)

# Java虚拟机

## 重点学习资料
[Intro.pdf](/Files/Java_JVM_II.pdf)

## Java内存区域和内存溢出
### java运行时的数据区

![avatar](/image/java_jvm_data_area.png)

1. 程序计数器
   程序计数器（Program Counter Register）是一块较小的内存空间，它可以看作是当前**线程**所执行的字节码的行号指示器。字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。
3. 
