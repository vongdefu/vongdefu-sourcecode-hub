
## 背景

由于微服务众多，就会产生下面的问题：

1. 无法得知某一个请求的调用链路；
2. 无法得知调用链路上的每一个阶段的请求时间；
3. 无法得知调用链路上的性能瓶颈；

## 常见的企业级解决方案

市面上有很多链路追踪的项目，其中也不乏一些优秀的，如下：

1. cat：由大众点评开源，基于Java开发的实时应用监控平台，包括实时应用监控、业务监控。 集成方案是通过代码埋点的方式来实现监控，比如： 拦截器，过滤器等。 
   对代码的侵入性很大，集成成本较高，风险较大。
2. zipkin ：由Twitter公司开源，开放源代码分布式的跟踪系统，用于收集服务的定时数据，以解决微服务架构中的延迟问题，包括：数据的收集、存储、查找和展现。该产品结合 
   spring-cloud-sleuth 使用较为简单，集成很方便，但是功能较简单。
3. pinpoint ：韩国人开源的基于字节码注入的调用链分析，以及应用监控分析工具。特点是支持多种插件，UI功能强大，接入端无代码侵入
4. skywalking ：SkyWalking是本土开源的基于字节码注入的调用链分析，以及应用监控分析工具。特点是支持多种插件，UI功能较强，接入端无代码侵入。目前已加入Apache孵化器。
5. Sleuth ：SpringCloud 提供的分布式系统中链路追踪解决方案。很可惜的是阿里系并没有链路追踪相关的开源项目。

我们可以采用Spring Cloud Sleuth+Zipkin来做链路追踪的解决方案。

## Sleuth

学习Sleuth之前必须了解它的几个概念：

1. Span：基本的工作单元，相当于链表中的一个节点，通过一个唯一ID标记它的开始、具体过程和结束。我们可以通过其中存储的开始和结束的时间戳来统计服务调用的耗时。除此之外还可以获取事件的名称、请求信息等。
2. Trace：一系列的Span串联形成的一个树状结构，当请求到达系统的入口时就会创建一个唯一ID（traceId），唯一标识一条链路。这个traceId始终在服务之间传递，直到请求的返回，那么就可以使用这个traceId将整个请求串联起来，形成一条完整的链路。
3. Annotation：一些核心注解用来标注微服务调用之间的事件，重要的几个注解如下： 
   - cs(Client Send)：客户端发出请求，开始一个请求的生命周期
   - sr（Server Received）：服务端接受请求并处理；sr-cs = 网络延迟 = 服务调用的时间
   - ss（Server Send）：服务端处理完毕准备发送到客户端；ss - sr = 服务器上的请求处理时间
   - cr（Client Reveived）：客户端接受到服务端的响应，请求结束； cr - sr = 请求的总时间

sleuth 并没有提供 可视化 的页面，因此还需要结合 zipkin 进行使用【这种方案也是SpringCloud官方推荐的方式】，并且SpringCloud中提供的 zipkin-starter 
中已经包含了 sleuth 的相关依赖。


## 需求分析

总的来说，就是模拟一个请求，然后通过这个请求作为入口，然后依次调用不同的服务。

在本案例中： sleuth-gateway 调用  sleuth-order ， sleuth-order 调用 sleuth-product 。

sleuth-order 与 sleuth-prduct 之间通过 openfeign 的方式进行调用。因此需要额外的注册中心，这里选用nacos。

## 实现过程

### 环境准备

1. nacos
2. zipkin-server 的安装

```text
// 下载 https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.4/
// 直接启动即可 java -jar zipkin-server-2.23.4-exec.jar 
```

### 高性能

默认的方式是HTTP方式，但是这种方式的性能不高，因此官方提供了高性能的方式，那就是引入mq。

```text
java -jar zipkin-server-2.23.4-exec.jar --zipkin.collector.rabbitmq.addresses=192.168.10.105 --zipkin.collector.rabbitmq.username=admin --zipkin.collector.rabbitmq.password=root1003
```

### 持久化

zipkin-server 的默认方式是内存方式保存，即所有的链路调用信息都存放在内存中，所以当 zipkin-server 
关闭后，所有的链路信息就会丢失，因此需要对 链路调用信息 进行持久化。

支持的持久化方式有： 

- 内存：服务重启将会失效，不推荐
- MySQL：数据量越大性能较低
- Elasticsearch：主流的解决方案，推荐使用
- Cassandra：技术太牛批，用的人少，自己选择，不过官方

我们这里采用 MySQL 的方式：


```text
// 建库 zipkin
// 建表
CREATE TABLE IF NOT EXISTS zipkin_spans (
  `trace_id_high` BIGINT NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` BIGINT NOT NULL,
  `id` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `remote_service_name` VARCHAR(255),
  `parent_id` BIGINT,
  `debug` BIT(1),
  `start_ts` BIGINT COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` BIGINT COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  PRIMARY KEY (`trace_id_high`, `trace_id`, `id`)
) ENGINE=InnoDB ROW_FORMAT=COMPRESSED CHARACTER SET=utf8 COLLATE utf8_general_ci;

ALTER TABLE zipkin_spans ADD INDEX(`trace_id_high`, `trace_id`) COMMENT 'for getTracesByIds';
ALTER TABLE zipkin_spans ADD INDEX(`name`) COMMENT 'for getTraces and getSpanNames';
ALTER TABLE zipkin_spans ADD INDEX(`remote_service_name`) COMMENT 'for getTraces and getRemoteServiceNames';
ALTER TABLE zipkin_spans ADD INDEX(`start_ts`) COMMENT 'for getTraces ordering and range';

CREATE TABLE IF NOT EXISTS zipkin_annotations (
  `trace_id_high` BIGINT NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` BIGINT NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` BIGINT NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` VARCHAR(255) NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` BLOB COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` INT NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` BIGINT COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` INT COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` BINARY(16) COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` SMALLINT COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` VARCHAR(255) COMMENT 'Null when Binary/Annotation.endpoint is null'
) ENGINE=InnoDB ROW_FORMAT=COMPRESSED CHARACTER SET=utf8 COLLATE utf8_general_ci;

ALTER TABLE zipkin_annotations ADD UNIQUE KEY(`trace_id_high`, `trace_id`, `span_id`, `a_key`, `a_timestamp`) COMMENT 'Ignore insert on duplicate';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id_high`, `trace_id`, `span_id`) COMMENT 'for joining with zipkin_spans';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id_high`, `trace_id`) COMMENT 'for getTraces/ByIds';
ALTER TABLE zipkin_annotations ADD INDEX(`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames';
ALTER TABLE zipkin_annotations ADD INDEX(`a_type`) COMMENT 'for getTraces and autocomplete values';
ALTER TABLE zipkin_annotations ADD INDEX(`a_key`) COMMENT 'for getTraces and autocomplete values';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id`, `span_id`, `a_key`) COMMENT 'for dependencies job';

CREATE TABLE IF NOT EXISTS zipkin_dependencies (
  `day` DATE NOT NULL,
  `parent` VARCHAR(255) NOT NULL,
  `child` VARCHAR(255) NOT NULL,
  `call_count` BIGINT,
  `error_count` BIGINT,
  PRIMARY KEY (`day`, `parent`, `child`)
) ENGINE=InnoDB ROW_FORMAT=COMPRESSED CHARACTER SET=utf8 COLLATE utf8_general_ci;

// 启动
java -jar zipkin-server-2.23.4-exec.jar --STORAGE_TYPE=mysql --MYSQL_HOST=192.168.10.105 --MYSQL_TCP_PORT=3306 --MYSQL_DB=zipkin --MYSQL_USER=root --MYSQL_PASS=root1003 --zipkin.collector.rabbitmq.addresses=192.168.10.105 --zipkin.collector.rabbitmq.username=admin --zipkin.collector.rabbitmq.password=root1003
```
