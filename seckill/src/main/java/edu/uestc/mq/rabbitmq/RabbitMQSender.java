package edu.uestc.mq.rabbitmq;

import edu.uestc.mq.MQSender;
import edu.uestc.mq.SeckillMessage;
import edu.uestc.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * c6：
 * MQ的信息发送者
 */

@Service
public class RabbitMQSender implements MQSender {

    private static Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        logger.info("MQ send message: " + msg);
        // 第一个参数为消息队列名，第二个参数为发送的消息
        amqpTemplate.convertAndSend(RabbitMQConfig.QUEUE, msg);
    }


    /**
     * 将为消息投递到topic exchange上
     *
     * @param message
     */
    public void sendTopic(Object message) {
        String msg = RedisService.beanToString(message);
        logger.info("Send topic message: " + msg);
        // 将消息投递到topic exchange
        amqpTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.key1", msg + "1");
        amqpTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.key2", msg + "2");
    }

    /**
     * 将消息投递到fanout exchange上
     *
     * @param message
     */
    public void sendFanout(Object message) {
        String msg = RedisService.beanToString(message);
        logger.info("Send fanout message: " + msg);
        amqpTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", msg);
    }

    /**
     * 将消息投递到header exchange上
     *
     * @param message
     */
    public void sendHeader(Object message) {
        String msg = RedisService.beanToString(message);
        logger.info("Send fanout message:" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(RabbitMQConfig.HEADERS_EXCHANGE, "", obj);
    }

    /**
     * 将用户秒杀信息投递到MQ中（使用direct模式的exchange）
     *
     * @param message
     */
    public void sendMiaoshaMessage(SeckillMessage message) {
        String msg = RedisService.beanToString(message);
        logger.info("MQ send message: " + msg);
        // 第一个参数为消息队列名，第二个参数为发送的消息
        amqpTemplate.convertAndSend(RabbitMQConfig.SECKILL_QUEUE, msg);
    }
}
