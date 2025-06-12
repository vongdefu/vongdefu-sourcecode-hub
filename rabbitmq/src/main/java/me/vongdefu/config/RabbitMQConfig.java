package me.vongdefu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
public class RabbitMQConfig {

    @Resource
    private CachingConnectionFactory connectionFactory;

    /**
     * 交换机。
     *
     *  在控制台页面-Exchanges标签页中，可以 add new exchange 。
     *
     *  在 type 下拉列表中有四个选项，可以选择交换机的类型：
     *      1. direct
     *      2. topic
     *      3. fanout
     *      4. headers
     *  还可以设置 是否持久化 、 是否自动删除；
     *  还可以设置 Internal ； If yes, clients cannot publish to this exchange directly. It can only be used with exchange to exchange bindings.
     *  还可以设置 参数 ；如 alternate-exchange ，可配合 Internal 使用。 使用map存放。
     *          If messages to this exchange cannot otherwise be routed, send them to the alternate exchange named here. (Sets the "alternate-exchange" argument.)
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange(){

//        Map<String, Object> params = new HashMap<>();
//        params.put("alternate-exchange", "internal.exchange");
//        DirectExchange directExchange = new DirectExchange("deviceCmdExchange", true, false, params);
        return new DirectExchange("deviceCmdExchange",true,false);
    }

    /**
     * 既可以在 RabbitMQ 服务端提供的控制台上面，直接创建 队列 ； 也可以通过 Java 客户端声明Bean的方式进行创建。
     * 创建队列的时候，可以给队列关联一些 policy ，这些 policy （策略），可以用来控制队列的相关属性，
     *  如
     *      1. 队列最大长度；
     *      2. 队列最大容量；
     *      3. 消息的存活时间；
     *      4. 超出后的执行策略；
     *      5. ......
     *
     *
     *
     * @return
     */
    @Bean
    public Queue deviceCmdQueue(){
        // 常规队列与死信交换机的绑定关系
        Map<String, Object> queueParams = new HashMap<>(2);
        // 设置队列长度和队列的容量，谁先到达，谁生效。
        queueParams.put("x-max-length", 2);
        queueParams.put("max-length-bytes", 1048576);

        //使用 overflow 设置来配置队列溢出行为。如果 overflow 设置为 reject-publish 或 reject-publish-dlx ，则最近发布的消息将被丢弃。
        // 此外，如果启用了发布者确认，发布者将通过 basic.nack 消息被告知拒绝。
        // 如果一条消息路由到多个队列，并且被其中至少一个队列拒绝，通道将通过 basic.nack 通知发布者。该消息仍将发布到所有其他可以入队的队列。
        // reject-publish 和 reject-publish-dlx 的区别在于 reject-publish-dlx 还会将拒绝的消息发送到死信队列。
        // 如果设置了 队列长度 和 队列容量， 但是没有设置 溢出行为 ， 那么交换机依旧会把消息投递给队列，只不过是队列不接受，并且交换机依旧返回ack；并且也不会执行 ReturnCallback 方法。
        queueParams.put("x-overflow", "reject-publish");

        return new Queue("deviceCmdQueue", true, false, false, queueParams);
    }

    @Bean
    public Binding deviceCommandBinding(){
        return BindingBuilder.bind(deviceCmdQueue())
                .to(directExchange())
                .with("deviceBinding");
    }


    /**
     * 由于消费者中配置了对应的队列，所以如果rabbitmq-server中不存在对应的队列，那么项目就无法启动，因此需要在此配置一个队列。
     * @return
     */
    @Bean
    public Queue cmdResponseQueue(){
        return new Queue("cmdresponse", true);
    }

    /**
     * 由于消费者中配置了对应的队列，所以如果rabbitmq-server中不存在对应的队列，那么项目就无法启动，因此需要在此配置一个队列。
     * @return
     */
    @Bean
    public Queue dataReportQueue(){
        return new Queue("datareport", true);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        /**
         * 客户端和服务端建立连接时，推荐将缓存模式设置为CONNECTION模式。
         *
         * 该模式下支持创建多个Connection，程序会缓存一定数量的Connection，每个Connection中缓存一定的Channel。
         *
         * 云消息队列 RabbitMQ 版是集群分布式架构，在CONNECTION模式下，
         * 客户端能够更均衡地连接到集群中的多个服务节点。这种方法可以有效避免出现负载热点问题，
         * 从而提高消息的发送和消费效率。
         */
//        // 缓存模式推荐设置为CONNECTION。
//        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
//        // CONNECTION模式下，最大可缓存的connection数量。
//        connectionFactory.setConnectionCacheSize(10);
//        // CONNECTION模式下，最大可缓存的Channel数量。
//        connectionFactory.setChannelCacheSize(64);

        // 请务必开启Connection自动重连功能，保证服务端发布时客户端可自动重新连接上服务端。
        connectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);

        //设置“发送消息后进行确认”
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        //设置“发送消息后返回确认信息”
        connectionFactory.setPublisherReturns(true);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 发送消息后，如果发送成功，则输出“消息发送成功”的反馈信息
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData,ack,cause));

        // 发送消息后，如果发送失败，则输出“消息发送失败-消息丢失”的反馈信息
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message));

        /**
         * 消息投递给 exchange 之后，exchange 会返回 ack 和 nack 消息。
         *  返回 ack 表示确认投递给了 exchange 了，但是不确定 exchange 是否投递给了 queue 。
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                //获取对应的ID消息，因为不确认是否有ID被传入，所以取值需要判空
                String id = correlationData == null ? "" : correlationData.getId();
                //校验是否成功发送
                if (ack) {
                    log.info("消息已经成功交给了交换机，对应消息ID为：{}", id);
                } else {
                    log.info("消息未能成功发送给交换机，对应消息ID为：{}，异常原因：{}", id, cause);
                }
            }
        });

        /**
         * 当 exchange 投递给 queue 失败时，就会执行 setReturnCallback 方法。
         *
         * 比如 直接删除队列 。
         *
         * 如果投递失败，rabbitmq-server 会回调 这个匿名类，然后执行 returnedMessage 方法。
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                // 要对队列设置 queueParams.put("x-overflow", "reject-publish");
                // 投递失败，记录日志
                // 示例：
                //      消息丢失:exchange(deviceCmdExchange),route(deviceBinding),replyCode(312),replyText(NO_ROUTE),
                //          message:(Body:'"test"' MessageProperties [headers={__TypeId__=java.lang.String}, contentType=application/json, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0])
                // 在日志中，我们可以看到 服务端 返回的异常代码（312）和异常信息（NO_ROUTE）。
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
                // 如有需要可以重试
            }
        });

//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//
//            log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
//
//        });
        // 定义消息传输的格式为JSON字符串格式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());


        // 最终返回RabbitMQ的操作组件实例RabbitTemplate
        return rabbitTemplate;
    }


}
