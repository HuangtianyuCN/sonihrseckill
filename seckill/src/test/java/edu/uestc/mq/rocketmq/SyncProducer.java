package edu.uestc.mq.rocketmq;/*
@author 黄大宁Rhinos
@date 2019/7/2 - 16:36
**/

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class SyncProducer {
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("localhost:9876");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 1; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        /**
         * 顺序生产
         * */
//        //消息会发送到队列1中，send的第三个变量arg表示发到第几个队列
//        Message message = new Message("TopicTest" /* Topic */,
//                "顺序" /* Tag */,"顺序信息".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        producer.send(message, new MessageQueueSelector() {
//            @Override
//            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
//                Integer index = (Integer)o;
//                return list.get(index);
//            }
//        },1);

        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}