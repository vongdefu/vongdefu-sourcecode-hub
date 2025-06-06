package me.vongdefu.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Component
@Slf4j
public class RabbitConsumer {

    /***
     * 使用  @Payload  会自动把监听到的消息转化为对应的实体
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = "deviceCmdQueue")
    public void onMessage(@Payload String msg, Message message, Channel channel) throws Exception {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageId = message.getMessageProperties().getMessageId();
        log.info("监听到消息-消息载荷： {}， 消息ID： {}， 消息deliveryTag： {}", msg, messageId, deliveryTag);

        if ("test-consumer-msg-10".equals(messageId)) {
//            channel.basicNack(deliveryTag, false, true);
            log.info("丢弃消息-消息载荷： {}， 消息ID： {}， 消息deliveryTag： {}", msg, messageId, deliveryTag);
        } else {
            channel.basicAck(deliveryTag, true);
        }

    }


}
