package edu.uestc.mq.rocketmq;/*
@author 黄大宁Rhinos
@date 2019/7/2 - 16:39
**/

import edu.uestc.domain.SeckillOrder;
import edu.uestc.domain.SeckillUser;
import edu.uestc.mq.MQReceiver;
import edu.uestc.mq.SeckillMessage;
import edu.uestc.redis.RedisService;
import edu.uestc.service.GoodsService;
import edu.uestc.service.OrderService;
import edu.uestc.service.SeckillService;
import edu.uestc.vo.GoodsVo;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RocketMQReceiver implements MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    private DefaultMQPushConsumer consumer;

    private static final String groupName = "seckillGroupConsumer";

    private static final String nameAddr = "localhost:9876";

    private static final String topic = "seckillTopic";

    public RocketMQReceiver()throws Exception{
        consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameAddr);
        consumer.subscribe(topic, "*");
        consumer.setPullBatchSize(1);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                String beanInfo = new String(msg.getBody());
                SeckillMessage seckillMessage = RedisService.stringToBean(beanInfo, SeckillMessage.class);
                // 获取秒杀用户信息与商品id
                SeckillUser user = seckillMessage.getUser();
                long goodsId = seckillMessage.getGoodsId();

                // 获取商品的库存
                GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
                Integer stockCount = goods.getStockCount();
                if (stockCount <= 0)
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

                // 判断是否已经秒杀到了
                SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
                if (order != null)
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

                // 减库存 下订单 写入秒杀订单
                seckillService.seckill(user, goods);

                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }

    @Override
    public void receiveMiaoshaInfo(String message) throws Exception {

    }
}