package edu.uestc.mq.rocketmq;/*
@author 黄大宁Rhinos
@date 2019/7/2 - 16:36
**/

import edu.uestc.mq.MQSender;
import edu.uestc.mq.SeckillMessage;
import edu.uestc.redis.RedisService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RocketMQSender implements MQSender {

    private DefaultMQProducer producer;

    private static final String groupName = "seckillGroupProducer";

    private static final String nameAddr = "localhost:9876";

    public void sendMiaoshaMessage(SeckillMessage message)throws Exception  {
        if(producer == null){
            producer = new DefaultMQProducer(groupName);
            producer.setNamesrvAddr(nameAddr);
            producer.start();
        }
        String beanJson = RedisService.beanToString(message);
        Long id = message.getGoodsId();
        Message msg =  new Message("seckillTopic",id.toString(),beanJson.getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult result = producer.send(msg);
        System.out.printf("%s%n", result);
    }

    public void closeProducer(){
        producer.shutdown();
    }
}