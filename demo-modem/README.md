#前言
linux环境下的modem串口通信编程示例\
工程代码基于https://github.com/demoModel/rxtx，重新改造成SpringBoot项目

# 功能说明
1.定时发送命令到串口\
2.自动接收来自串口的消息并打印\
3.可设置发送命令的内容

## 目录说明
环境：idea+gradle\
src/main : 项目的源代码\
application.properties : 项目的配置文件，主要配置串口的连接信息\
sendLine.txt：设置发送的命令内容

打包：包含linux环境下所需的librxtxSerial.so文件，以及打包后的jar，可直接使用java -jar 运行
