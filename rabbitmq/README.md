
## 控制台



## 项目框架搭建

1. xml.pom
2. application.yml
3. config

### 设置RabbitMQ

1. 交换机的设置
2. 队列的设置
3. 交换机与队列的绑定

4. 消息投递到交换机的确认
- setConfirmCallback

5. 交换机投递到队列的确认
- setMandatory
- setReturnCallback

### 发送消息

1. 普通的发送消息；
2. 为消息设置唯一的ID；

### 接收消息

1. 收到后回复确认 channel.basicAck(deliveryTag, true);


