


## 实践案例


- [x] 配置中心
  - [x] 传统读取配置文件方式： 在项目本地配置文件application.yml中填写配置项，使用的地方使用@Value注解来进行获取
  - [x] 从配置中心中读取配置项： 配置项不再放到配置文件中，而是放到Nacos中
  - [x] 从共享配置文件中读取配置项： 实践Nacos支持的『共享配置文件』的高级特性
  - [x] 把共享配置文件中的配置项映射成JavaBean： 面向对象，当然少不了面向对象的方式操作配置文件了
  - [x] 以服务名作为命名空间进行配置隔离： 实际开发过程时的一些实践方式
  - [x] 以环境名作为命名空间进行配置隔离： 实际开发过程时的一些实践方式
  - [ ] 灰度发布
  - [x] 自定义全局异常，并实现异常信息的国际化 [Java统一异常处理(配置文件集中化定义)](https://mp.weixin.qq.com/s/XE4R2wOj08qNivo8Ms5ZRQ)


## nacos安装

1. 下载地址： https://github.com/alibaba/nacos/releases/tag/1.2.1
2. 安装过程

/usr/lib/systemd/system/nacos.service

```shell
nacos-1.2.1
[Unit]
Description=nacos-1.2.1
After=network.target

[Service]
Type=forking
ExecStart=/usr/setup/nacos-1.2.1/bin/startup.sh -m standalone
ExecReload=/usr/setup/nacos-1.2.1/bin/shutdown.sh
ExecStop=/usr/setup/nacos-1.2.1/bin/shutdown.sh
PrivateTmp=true

[Install]
WantedBy=multi-user.target

```