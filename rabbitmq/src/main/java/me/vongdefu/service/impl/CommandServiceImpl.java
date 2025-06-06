package me.vongdefu.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.vongdefu.service.CommandService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Service
@Slf4j
public class CommandServiceImpl implements CommandService {


    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    public void produceOneMsg() {

        // 设置消息id
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("deviceCmdExchange", "deviceBinding", "test", correlationId);
    }


    @Override
    public void produceNMsg() {
        new Thread(() -> {

            for (int i = 0; i < 20; i++) {

                /**
                 * CorrelationData 相关数据，主要用于跟踪消息的发送结果。在异步发送消息的情况下，你可以使用此参数来关联发送请求和响应。
                 * CorrelationData 包含一个唯一标识符，用于识别消息。当收到响应时，可以通过这个标识符找到原始请求。
                 */

                /*rabbitTemplate.convertAndSend("deviceCmdExchange", "deviceBinding", msg, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                        return message;
                    }
                });*/
                String msgid = "test-consumer-msg-" + i;
                rabbitTemplate.convertAndSend(
                        "deviceCmdExchange",
                        "deviceBinding",
                        "test-consumer",
                        (message) -> {
                            message.getMessageProperties().setMessageId(msgid);
                            return message;
                        }
                );
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
