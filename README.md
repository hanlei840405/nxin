# etl-designer-server

基于Pentaho data-integration产品(kettle)二次开发，精选近50种常用组件进行重写，
涵盖关系型数据库、网络服务、NoSQL数据库,将图形化设计由C/S架构升级为B/S架构，
脱离客户端束缚，随时随地修改脚本,添加定时调度、项目管理、版本控制、发布回滚、
日志追踪、性能监控等丰富实用的功能,新恩数造支持MySQL、Oracle、PostgreSQL、
SqlServer、DB2等关系型数据库在内的多种数据库。

系统运行强制依赖：

redis： 1、缓存找回密码的动态验证码

ftp服务：需连接ftp服务器，用于存放etl运行的脚本，worker执行脚本时，会从ftp服务器拉取最新文件，本地开发时可使用docker启动ftp服务器，数据迁移时，需将ftp内目录完整迁移至其他环境
windows开启ftp/sftp功能：安装openSSH工具，下载地址：https://github.com/powershell/win32-openssh/releases 或https://pan.baidu.com/s/12TCh9a3YevUOpVLVrx2VWg?pwd=65xh
macos开启ftp/sftp功能：https://blog.csdn.net/King_zj/article/details/125991856

web-server:用于设计转换与任务的服务
worker:用于执行web-server端发布的Job服务


作者<br/>
jesse.18@163.com<br/>
用户名: jesse.18@163.com
密码: 123456 (请勿修改)

![system.jpg](system.jpg)
![login.jpg](login.jpg)
![project.jpg](project.jpg)
![database.jpg](database.jpg)
![privilege.jpg](privilege.jpg)
![privilege-grant.jpg](privilege-grant.jpg)
![user.jpg](user.jpg)
![user-grant.jpg](user-grant.jpg)
![designer1.jpg](designer1.jpg)
![designer2.jpg](designer2.jpg)
![designer3.jpg](designer3.jpg)
![designer4.jpg](designer4.jpg)
![designer5.jpg](designer5.jpg)
![designer6.jpg](designer6.jpg)
![deploy.jpg](deploy.jpg)
![depend.jpg](depend.jpg)
![batch-task.jpg](batch-task.jpg)