package edu.uestc.mq;/*
@author 黄大宁Rhinos
@date 2019/7/3 - 14:51
**/

public interface MQSender {
    void sendMiaoshaMessage(SeckillMessage message) throws Exception;
}
