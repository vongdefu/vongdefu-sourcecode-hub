

## skywalking

skywalking是一个优秀的国产开源框架，2015年由个人吴晟（华为开发者）开源 ， 2017年加入Apache孵化器。短短两年就被Apache收入麾下，实力可见一斑。

skywalking支持dubbo，SpringCloud，SpringBoot集成，代码无侵入，通信方式采用GRPC，性能较好，实现方式是java探针，支持告警，支持JVM监控，支持全局调用统计等等，功能较完善

与zipkin 对比：

skywalking采用字节码增强的技术实现代码无侵入，zipKin代码侵入性比较高
skywalking功能比较丰富，报表统计，UI界面更加人性化

> 如果是新的架构，建议优先选择skywalking。


下载地址： https://archive.apache.org/dist/skywalking/8.7.0/
参考： https://juejin.cn/post/7049920780569673736




在启动参数指定即可，命令如下：

-javaagent:D:\01-download\packages\apache-skywalking-apm-8.7.0.tar\apache-skywalking-apm-8.7.0\apache-skywalking-apm-bin\agent\skywalking-agent.jar
-Dskywalking.agent.service_name=skywalking-gateway
-Dskywalking.collector.backend_service=127.0.0.1:11800


-javaagent:D:\01-download\packages\apache-skywalking-apm-8.7.0.tar\apache-skywalking-apm-8.7.0\apache-skywalking-apm-bin\agent\skywalking-agent.jar
-Dskywalking.agent.service_name=skywalking-order
-Dskywalking.collector.backend_service=127.0.0.1:11800


-javaagent:D:\01-download\packages\apache-skywalking-apm-8.7.0.tar\apache-skywalking-apm-8.7.0\apache-skywalking-apm-bin\agent\skywalking-agent.jar
-Dskywalking.agent.service_name=skywalking-product
-Dskywalking.collector.backend_service=127.0.0.1:11800